package org.antonaleks.pdd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.*;
import org.antonaleks.pdd.entity.Question;
import org.antonaleks.pdd.utils.ResourceHelper;

import java.io.*;
import java.sql.SQLException;
import java.util.*;


import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;

import org.bson.Document;

public class Main {
    public static void main(String[] args) throws IOException, SQLException {

        ResourceHelper app = new ResourceHelper();
        String fileName = "assets/question.json";

        System.out.println("getResourceAsStream : " + fileName);
        InputStream is = app.getFileFromResourceAsStream(fileName);
//        //Creating a Scanner object
        Scanner sc = new Scanner(is);
//        //Reading line by line from scanner to StringBuffer
        StringBuffer sb = new StringBuffer();
        while (sc.hasNext()) {
            sb.append(sc.nextLine());
        }
        System.out.println(sb.toString());

        var objectMapper = new ObjectMapper();
//        JsonNode jsonNode = objectMapper.readTree(sb.toString());


        Question question = objectMapper.readValue(sb.toString(), Question.class);

        MongoClient mongoClient = new MongoClient("localhost");

        MongoDatabase database = mongoClient.getDatabase("test");

        MongoCollection<Document> collection = database.getCollection("question");


        String questionAsString = objectMapper.writeValueAsString(question);

//        collection.insertOne(Document.parse(questionAsString));
        Document bson = Document.parse(questionAsString);

        Document myDoc = collection.find().first();
        Question question_from_db = objectMapper.readValue(myDoc.toJson(), Question.class);

        System.out.println(question.equals(question_from_db));
        System.out.println();




//        TypeReference<List<Question>> typeRef = new TypeReference<List<Question>>() {
//        };
       // List<Question> questions = objectMapper.readValue(jsonNode.get("questions").traverse(), typeRef);


//        System.out.println(questions);

//        var question = questions.get(3);
//        question.getTopicsJson();
//        question.getOptionsJson();

          // Postgres SQL
//        String url = "jdbc:postgresql://localhost:5432/postgres?currentSchema=pdd";
//        String user = "postgres";
//        String password = "root";
//
//        Connection con = DriverManager.getConnection(url, user, password);
//        for (Question question :
//                questions) {
//
//            PreparedStatement st = con.prepareStatement("insert into questions(number,block_number , ticket_number, cat, text, image, comment, right_option, topics, options,id) " +
//                    "values (?,?,?,?,?,?,?,?,?,?,?)");
//            st.setInt(1, question.getNumber());
//            st.setInt(2, question.getBlockNumber());
//            st.setInt(3, question.getTicketNumber());
//            st.setInt(4, question.getCat());
//            st.setString(5, question.getText());
//            st.setBytes(6, question.getImage());
//            st.setString(7, question.getComment());
//            st.setInt(8, question.getRightOption());
//            PGobject jsonObject = new PGobject();
//            jsonObject.setType("json");
//            jsonObject.setValue(question.getTopicsJson());
//            st.setObject(9, jsonObject);
//            jsonObject.setValue(question.getOptionsJson());
//            st.setObject(10, jsonObject);
//            st.setInt(11, question.getId());
//            st.executeUpdate();
//
//            st.close();
//        }
//        con.close();

    }


}

