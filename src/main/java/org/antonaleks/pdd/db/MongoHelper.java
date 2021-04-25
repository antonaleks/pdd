package org.antonaleks.pdd.db;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import org.antonaleks.pdd.entity.Question;
import org.antonaleks.pdd.model.Category;
import org.antonaleks.pdd.utils.PropertiesManager;
import org.bson.Document;

import java.io.IOException;
import java.util.ArrayList;
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
        this.mongoClient = new MongoClient(PropertiesManager.getDbUrl());

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


    public List<Question> getQuestionListByTicket(int ticketId, Category cat) throws JsonProcessingException {
        MongoCollection<Document> collection = database.getCollection(PropertiesManager.getDbCollectionQuestion());

        var objectMapper = new ObjectMapper();
        var coll = collection.find(and((eq("ticketNumber", ticketId)), eq("cat", cat.getCategory()))).map(x -> {
            try {
                return objectMapper.readValue(x.toJson(), Question.class);
            } catch (JsonProcessingException e) {
                return null;
            }
        });


        var list = StreamSupport.stream(coll.spliterator(), true)
                .collect(Collectors.toList());
        return list;

    }

    public void insertJsonMany(String json, String collectionPath) throws IOException {
        MongoCollection<Document> collection = database.getCollection(collectionPath);

        var objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(json);
        TypeReference<List<Question>> typeRef = new TypeReference<List<Question>>() {
        };
        List<Question> questions = objectMapper.readValue(jsonNode.get("questions").traverse(), typeRef);
        String questionAsString = objectMapper.writeValueAsString(questions);

        List<Document> documents = (List<Document>) objectMapper.readValue(questionAsString, List.class)
                .stream().map(listItem -> new Document((LinkedHashMap) listItem))
                .collect(Collectors.toList());

        collection.insertMany(documents);

    }

    public void removeAll(String collectionPath) {
        MongoCollection<Document> collection = database.getCollection(collectionPath);

        collection.deleteMany(new Document());

    }

}
