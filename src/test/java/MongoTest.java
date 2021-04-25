import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.antonaleks.pdd.db.MongoHelper;
import org.antonaleks.pdd.entity.Question;
import org.antonaleks.pdd.utils.PropertiesManager;
import org.antonaleks.pdd.utils.ResourceHelper;
import org.bson.Document;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MongoTest {

    @Test
    void testRemoveAll() {
        MongoHelper.getInstance().removeAll(PropertiesManager.getDbCollectionQuestion());
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

        MongoHelper.getInstance().insertJsonMany(sb.toString(), PropertiesManager.getDbCollectionQuestion());

    }

}
