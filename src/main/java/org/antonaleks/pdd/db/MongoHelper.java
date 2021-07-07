package org.antonaleks.pdd.db;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import org.antonaleks.pdd.entity.*;
import org.antonaleks.pdd.model.Category;
import org.antonaleks.pdd.utils.HashUtils;
import org.antonaleks.pdd.utils.PropertiesManager;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

import static com.mongodb.client.model.Filters.*;

public final class MongoHelper {

    private static volatile MongoHelper instance = null;

    private MongoDatabase database;
    private MongoClient mongoClient;

    private MongoHelper() {

        MongoClientURI uri = new MongoClientURI(PropertiesManager.getDbUrl());


        this.mongoClient = new MongoClient(uri);

        this.database = mongoClient.getDatabase(PropertiesManager.getDbName());

    }

    public static MongoHelper getInstance() {
        if (instance == null) {
            synchronized (MongoHelper.class) {
                if (instance == null) {
                    instance = new MongoHelper();
                }
            }
        }
        return instance;
    }


    public List<Question> getQuestionListByTicket(int ticketId, Category cat) {

        return getQuestionList(and((eq("ticketNumber", ticketId)), eq("cat", cat.getCategory())));

    }

    private List<Question> getQuestionList(Bson filter) {
        MongoCollection<Document> collection = database.getCollection(PropertiesManager.getDbCollectionQuestion());

        ObjectMapper objectMapper = new ObjectMapper();

        MongoIterable coll = collection.find(filter).map(x -> {
            try {
                return objectMapper.readValue(x.toJson(), Question.class);
            } catch (JsonProcessingException e) {
                return null;
            }
        });


        List<Question> list = (List<Question>) StreamSupport.stream(coll.spliterator(), true)
                .collect(Collectors.toList());
        return list;
    }


    public <T extends JsonSerializable> List<T> getDocumentList(Class<T> classType, String collectionPath) {
        MongoCollection<Document> collection = database.getCollection(collectionPath);

        ObjectMapper objectMapper = new ObjectMapper();

        MongoIterable coll = collection.find().map(x -> {
            try {
                return objectMapper.readValue(x.toJson(), classType);
            } catch (JsonProcessingException e) {
                return null;
            }
        });


        List<T> list = (List<T>) StreamSupport.stream(coll.spliterator(), true)
                .collect(Collectors.toList());
        return list;
    }

    public List<User> getUserList(Bson filter, String... fields) {
        MongoCollection<Document> collection = database.getCollection(PropertiesManager.getDbCollectionUser());

        ObjectMapper objectMapper = new ObjectMapper();

        FindIterable coll = collection.find(filter);
        if (fields != null) {
            coll = coll.projection(Projections.include(fields));
        }

        MongoIterable<User> collResult = coll.map(x -> {
            try {
                return objectMapper.readValue(((Document) x).toJson(), User.class);
            } catch (JsonProcessingException e) {
                return null;
            }
        });


        List<User> list = (List<User>) StreamSupport.stream(collResult.spliterator(), true)
                .collect(Collectors.toList());
        return list;
    }


    public <T extends JsonSerializable> List<T> getDocumentList(Class<T> classType, String collectionPath, String filter, String fields) {
        MongoCollection<Document> collection = database.getCollection(collectionPath);

        ObjectMapper objectMapper = new ObjectMapper();

        MongoIterable coll = collection.find(new BsonDocument()).map(x -> {
            try {
                return objectMapper.readValue(x.toJson(), classType);
            } catch (JsonProcessingException e) {
                return null;
            }
        });


        List<T> list = (List<T>) StreamSupport.stream(coll.spliterator(), true)
                .collect(Collectors.toList());
        return list;
    }

    public List<Question> getQuestionListByTopic(Topic topic, Category cat) {

        return getQuestionList(and(elemMatch("topics", Filters.eq("id", topic.getId())), eq("cat", cat.getCategory())));

    }

    public Ticket getTicketByNumber(int number, Category cat) {

        return new Ticket(getQuestionList(and(Filters.eq("ticketNumber", number), eq("cat", cat.getCategory()))), number);

    }

    public Ticket getTicketForExam(Category cat) {
        List<Question> questions = new ArrayList<>();


        List<Integer> range = IntStream.range(1, 40).boxed()
                .collect(Collectors.toCollection(ArrayList::new));
        Collections.shuffle(range);

        for (int blockNumber = 1; blockNumber <= 5; blockNumber++) {
            questions.addAll(getQuestionList(and(Filters.eq("ticketNumber", range.get(blockNumber - 1)), eq("blockNumber", blockNumber), eq("cat", cat.getCategory()))));
        }

        questions.addAll(getQuestionList(and(Filters.eq("ticketNumber", range.get(5)), eq("blockNumber", new Random().nextInt(6) + 1), eq("cat", cat.getCategory()))));
        questions.addAll(getQuestionList(and(Filters.eq("ticketNumber", range.get(6)), eq("blockNumber", new Random().nextInt(6) + 1), eq("cat", cat.getCategory()))));

        return new Ticket(questions, 1);

    }

    public Ticket getTicketByTopic(Topic topic, Category cat) {

        return new Ticket(getQuestionList(and(elemMatch("topics", Filters.eq("id", topic.getId())), eq("cat", cat.getCategory()))), topic.getId());

    }


    public <T extends JsonSerializable> void insertJsonMany(String json, String collectionPath, String filePath, TypeReference<List<T>> typeRef) throws IOException {
        MongoCollection<Document> collection = database.getCollection(collectionPath);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(json);
        List<T> topics = objectMapper.readValue(jsonNode.get(filePath).traverse(), typeRef);
        String questionAsString = objectMapper.writeValueAsString(topics);

        List<Document> documents = (List<Document>) objectMapper.readValue(questionAsString, List.class)
                .stream().map(listItem -> new Document((LinkedHashMap) listItem))
                .collect(Collectors.toList());

        collection.insertMany(documents);

    }

    public void updateUserStatistic(User user) throws IOException {
        MongoCollection<Document> collection = database.getCollection(PropertiesManager.getDbCollectionUser());

        BasicDBObject query = new BasicDBObject();
        query.put("login", user.getLogin());
        query.put("password", user.getPassword());


        BasicDBObject newDocument = new BasicDBObject();
        ObjectMapper objectMapper = new ObjectMapper();
        String questionAsString = objectMapper.writeValueAsString(user.getStatistic());
        List<Document> docs = objectMapper.readValue(questionAsString, List.class);
        newDocument.put("statistic", docs);

        BasicDBObject updateObject = new BasicDBObject();
        updateObject.put("$set", newDocument);
        collection.updateOne(query, updateObject);

    }

    public void updateUser(User user) throws IOException {
        MongoCollection<Document> collection = database.getCollection(PropertiesManager.getDbCollectionUser());

        BasicDBObject query = new BasicDBObject();
        query.put("login", user.getLogin());

        BasicDBObject newDocument = new BasicDBObject();
        ObjectMapper objectMapper = new ObjectMapper();
        String questionAsString = objectMapper.writeValueAsString(user);
        Document doc = Document.parse(questionAsString);

        BasicDBObject updateObject = new BasicDBObject();
        updateObject.put("$set", doc);
        collection.updateOne(query, updateObject);

    }

    public <T extends JsonSerializable> void insertJsonMany(List<T> objectList, String collectionPath) throws IOException {
        MongoCollection<Document> collection = database.getCollection(collectionPath);
        ObjectMapper objectMapper = new ObjectMapper();

        String questionAsString = objectMapper.writeValueAsString(objectList);

        List<Document> documents = (List<Document>) objectMapper.readValue(questionAsString, List.class)
                .stream().map(listItem -> new Document((LinkedHashMap) listItem))
                .collect(Collectors.toList());

        collection.insertMany(documents);

    }

    public void removeAll(String collectionPath) {
        MongoCollection<Document> collection = database.getCollection(collectionPath);

        collection.deleteMany(new Document());


    }

    public void remove(String collectionPath, Bson filter) {
        MongoCollection<Document> collection = database.getCollection(collectionPath);

        collection.deleteOne(filter);


    }

    public int getMaxTicket() {
        MongoCollection<Document> collection = database.getCollection(PropertiesManager.getDbCollectionQuestion());
        DBObject sort = new BasicDBObject();
        sort.put("ticketNumber", -1);
        MongoCursor cursor = collection.find().sort((Bson) sort).limit(1).cursor();
        return (int) ((Document) cursor.next()).get("ticketNumber");
    }

    public boolean checkLicense() throws SocketException, UnknownHostException {
        MongoCollection<Document> collection = database.getCollection(PropertiesManager.getDbCollectionLicense());
        String mac = getMacAddress();
        Document dmac = collection.find(eq("mackey", HashUtils.generateSecurePassword(mac))).first();
        return dmac != null;
    }


    public boolean activateLicense(String licenseKey) throws SocketException, UnknownHostException {
        MongoCollection<Document> collection = database.getCollection(PropertiesManager.getDbCollectionLicense());

        Document d = collection.find(eq("key", HashUtils.generateSecurePassword(licenseKey))).first();

        if (d != null) {
            if ((boolean) d.get("activated"))
                return false;
            else {
                String mac = getMacAddress();
                Document docUp = new Document("mackey", HashUtils.generateSecurePassword(mac)).append("activated", true);

                collection.updateOne(eq("key", HashUtils.generateSecurePassword(licenseKey)), new Document("$set", docUp));
                d = collection.find(eq("key", HashUtils.generateSecurePassword(licenseKey))).first();

                if (d != null && (boolean) d.get("activated"))
                    return true;
            }
        }
        return false;
    }


    private String getMacAddress() throws UnknownHostException, SocketException {
        InetAddress ip = InetAddress.getLocalHost();

        NetworkInterface network = NetworkInterface.getByInetAddress(ip);

        byte[] mac = network.getHardwareAddress();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mac.length; i++) {
            sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
        }

        return sb.toString();
    }
}
