
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.antonaleks.pdd.utils.PropertiesManager;
import org.bson.Document;
import org.antonaleks.pdd.entity.Question;
import org.antonaleks.pdd.utils.ResourceHelper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


import java.io.InputStream;
import java.util.Scanner;

public class QuestionTest {

    @Test
    void testQuestionDBImport() throws JsonProcessingException {
        ResourceHelper app = new ResourceHelper();
        String fileName = "assets/question.json";

        InputStream is = app.getFileFromResourceAsStream(fileName);
        Scanner sc = new Scanner(is);
        StringBuffer sb = new StringBuffer();
        while (sc.hasNext()) {
            sb.append(sc.nextLine());
        }

        ObjectMapper objectMapper = new ObjectMapper();

        Question question = objectMapper.readValue(sb.toString(), Question.class);

        MongoClient mongoClient = new MongoClient(PropertiesManager.getDbUrl());

        MongoDatabase database = mongoClient.getDatabase(PropertiesManager.getDbName());

        MongoCollection<Document> collection = database.getCollection(PropertiesManager.getDbCollectionQuestion());

        Document myDoc = collection.find().first();
        Question question_from_db = objectMapper.readValue(myDoc.toJson(), Question.class);

        assertEquals(question, question_from_db);

    }



}
