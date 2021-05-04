package org.antonaleks.pdd.db;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Filters;
import org.antonaleks.pdd.entity.JsonSerializable;
import org.antonaleks.pdd.entity.Question;
import org.antonaleks.pdd.entity.Ticket;
import org.antonaleks.pdd.entity.Topic;
import org.antonaleks.pdd.model.Category;
import org.antonaleks.pdd.utils.PropertiesManager;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
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


        List<Question>  list = (List<Question> )StreamSupport.stream(coll.spliterator(), true)
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


        List<T> list = ( List<T>)StreamSupport.stream(coll.spliterator(), true)
                .collect(Collectors.toList());
        return list;
    }

    public List<Question> getQuestionListByTopic(Topic topic, Category cat) {

        return getQuestionList(and(elemMatch("topics", Filters.eq("id", topic.getId())), eq("cat", cat.getCategory())));

    }

    public Ticket getTicketByNumber(int number, Category cat) {

        return new Ticket(getQuestionList(and(Filters.eq("ticketNumber", number), eq("cat", cat.getCategory()))), number);

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

    public int getMaxTicket(){
        MongoCollection<Document> collection = database.getCollection(PropertiesManager.getDbCollectionQuestion());
        DBObject sort = new BasicDBObject();
        sort.put("ticketNumber",-1);
        MongoCursor cursor= collection.find().sort((Bson) sort).limit(1).cursor();
        return (int) ((Document)cursor.next()).get("ticketNumber");
    }


}
