import com.fasterxml.jackson.core.type.TypeReference;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.antonaleks.pdd.db.MongoHelper;
import org.antonaleks.pdd.entity.Question;
import org.antonaleks.pdd.entity.Topic;
import org.antonaleks.pdd.utils.HashUtils;
import org.antonaleks.pdd.utils.PropertiesManager;
import org.antonaleks.pdd.utils.ResourceHelper;
import org.bson.Document;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;


public class MongoTest {

    @Test
    void testRemoveAll() {
        MongoHelper.getInstance().removeAll(PropertiesManager.getDbCollectionQuestion());
        MongoHelper.getInstance().removeAll(PropertiesManager.getDbCollectionTopics());

    }

    @Test
    void testInsertQuestions() throws IOException {

        ResourceHelper app = new ResourceHelper();
        String fileName = "assets/questions.json";

        InputStream is = app.getFileFromResourceAsStream(fileName);
        Scanner sc = new Scanner(is);
        StringBuffer sb = new StringBuffer();
        while (sc.hasNext()) {
            sb.append(sc.nextLine());
        }
        TypeReference<List<Question>> typeRef = new TypeReference<List<Question>>() {
        };
        MongoHelper.getInstance().<Question>insertJsonMany(sb.toString(), PropertiesManager.getDbCollectionQuestion(), "questions", typeRef);

    }

    @Test
    void testInsertTopics() throws IOException {

        ResourceHelper app = new ResourceHelper();
        String fileName = "assets/topics.json";

        InputStream is = app.getFileFromResourceAsStream(fileName);
        Scanner sc = new Scanner(is);
        StringBuffer sb = new StringBuffer();
        while (sc.hasNext()) {
            sb.append(sc.nextLine());
        }
        TypeReference<List<Topic>> typeRef = new TypeReference<List<Topic>>() {
        };
        MongoHelper.getInstance().<Topic>insertJsonMany(sb.toString(), PropertiesManager.getDbCollectionTopics(), "topics", typeRef);

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

    @Test
    void testLicense() throws SocketException, UnknownHostException {
        MongoHelper.getInstance().removeAll("license");

        MongoClientURI uri = new MongoClientURI(PropertiesManager.getDbUrl());


        MongoClient mongoClient = new MongoClient(uri);

        MongoDatabase database = mongoClient.getDatabase(PropertiesManager.getDbName());

        MongoCollection<Document> collection = database.getCollection("license");

        String uid = UUID.randomUUID().toString();
        Document doc = new Document("key", HashUtils.generateSecurePassword(uid)).append("activated", false);

        collection.insertOne(doc);
        Document d = collection.find().first();
        String mac = getMacAddress();
        String secureMac = mac + d.get("key");
        Document docUp = new Document("key", HashUtils.generateSecurePassword(secureMac)).append("activated", true);

        collection.replaceOne(eq("key", HashUtils.generateSecurePassword(uid)), docUp);
        d = collection.find().first();
        if (d != null && (boolean) d.get("activated"))
            System.out.println(HashUtils.verifyHashString(mac + HashUtils.generateSecurePassword(uid), d.getString("key")));

    }

    @Test
    void testInsertLicense() throws IOException {
//        MongoHelper.getInstance().removeAll("license");

        MongoClientURI uri = new MongoClientURI(PropertiesManager.getDbUrl());


        MongoClient mongoClient = new MongoClient(uri);

        MongoDatabase database = mongoClient.getDatabase(PropertiesManager.getDbName());

        MongoCollection<Document> collection = database.getCollection("license");
        FileWriter writer = new FileWriter("licenses.txt");

        List<Document> doc = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            String uid = UUID.randomUUID().toString();
            String secureKey = HashUtils.generateSecurePassword(uid);
            doc.add(new Document("key", secureKey).append("activated", false).append("mackey", ""));
            writer.write(uid + "\t" + secureKey + System.lineSeparator());
        }
        writer.close();

        collection.insertMany(doc);

    }

    @Test
    void testActivateLicense() throws SocketException, UnknownHostException {
        MongoClientURI uri = new MongoClientURI(PropertiesManager.getDbUrl());
        MongoClient mongoClient = new MongoClient(uri);
        MongoDatabase database = mongoClient.getDatabase(PropertiesManager.getDbName());
        MongoCollection<Document> collection = database.getCollection("license");
        String mac = getMacAddress();
        Document dmac = collection.find(eq("mackey", HashUtils.generateSecurePassword(mac))).first();
        if (dmac != null)
            System.out.println("License already activated");
        else {
            String licenseKey = "7e36a45b-4e5d-4f59-8749-76aef045c116";
            Document d = collection.find(eq("key", HashUtils.generateSecurePassword(licenseKey))).first();

            if (d != null) {
                if ((boolean) d.get("activated"))
                    System.out.println("License already activated " + HashUtils.verifyHashString(mac, d.getString("mackey")));
                else {
                    Document docUp = new Document("mackey", HashUtils.generateSecurePassword(mac)).append("activated", true);

                    collection.updateOne(eq("key", HashUtils.generateSecurePassword(licenseKey)), new Document("$set", docUp));
                    d = collection.find(eq("key", HashUtils.generateSecurePassword(licenseKey))).first();

                    if (d != null && (boolean) d.get("activated"))
                        System.out.println("Now activated new license " + HashUtils.verifyHashString(mac, d.getString("mackey")));
                }
            } else
                System.out.println("Wrong license key");
        }
    }
}