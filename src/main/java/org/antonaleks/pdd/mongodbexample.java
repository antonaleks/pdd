package org.antonaleks.pdd;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Filters.eq;

public class mongodbexample {

    public static void main(String[] args) {
        MongoClient mongoClient = new MongoClient("localhost");


        MongoDatabase database = mongoClient.getDatabase("mydb");

        MongoCollection<Document> collection = database.getCollection("questions");


        Block<Document> printBlock = new Block<Document>() {
            @Override
            public void apply(final Document document) {
                System.out.println(document.toJson());
            }
        };

//        collection.find(eq("name", "Общие положения"))
//                .forEach(printBlock);

        collection.find(and(gte("id", 2), lt("id", 9), eq("ticketNumber", 1)))
                .forEach(printBlock);


//        Document doc = new Document("name", "MongoDB")
//                .append("type", "database")
//                .append("count", 1)
//                .append("versions", Arrays.asList("v3.2", "v3.0", "v2.6"))
//                .append("info", new Document("x", 203).append("y", 102));
//
//        collection.insertOne(doc);
//
//        List<Document> documents = new ArrayList<Document>();
//        for (int i = 0; i < 100; i++) {
//            documents.add(new Document("i", i));
//        }
//
//        collection.insertMany(documents);
//
//        System.out.println(collection.count());
//
//        Document myDoc = collection.find().first();
//        System.out.println(myDoc.toJson());
//
//        MongoCursor<Document> cursor = collection.find().iterator();
//        try {
//            while (cursor.hasNext()) {
//                System.out.println(cursor.next().toJson());
//            }
//        } finally {
//            cursor.close();
//        }
//
//        for (Document cur : collection.find()) {
//            System.out.println(cur.toJson());
//        }
//
//        myDoc = collection.find(eq("i", 71)).first();
//        System.out.println(myDoc.toJson());
//
//
//        Block<Document> printBlock = new Block<Document>() {
//            @Override
//            public void apply(final Document document) {
//                System.out.println(document.toJson());
//            }
//        };
//
//        collection.find(gt("i", 50)).forEach(printBlock);
//
//        collection.find(and(gt("i", 50), lte("i", 100))).forEach(printBlock);
//
//
//        collection.updateOne(eq("i", 10), new Document("$set", new Document("i", 110)));
//
//        UpdateResult updateResult = collection.updateMany(lt("i", 100), inc("i", 100));
//        System.out.println(updateResult.getModifiedCount());
//
//        collection.deleteOne(eq("i", 110));
//        DeleteResult deleteResult = collection.deleteMany(gte("i", 100));
//        System.out.println(deleteResult.getDeletedCount());
//
//        collection.createIndex(new Document("i", 1));

//        //Creating a Scanner object
//        Scanner sc = new Scanner(is);
//        //Reading line by line from scanner to StringBuffer
//        StringBuffer sb = new StringBuffer();
//        while (sc.hasNext()) {
//            sb.append(sc.nextLine());
//        }
//        System.out.println(sb.toString());
//
//        Document newDoc = Document.parse(sb.toString());

//        List<Document> documents = (List<Document>) newDoc.get("questions");
//        collection.insertMany(documents);
    }
}
