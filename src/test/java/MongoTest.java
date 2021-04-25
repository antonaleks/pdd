import org.antonaleks.pdd.db.MongoHelper;
import org.antonaleks.pdd.entity.Question;
import org.antonaleks.pdd.entity.Topic;
import org.antonaleks.pdd.utils.PropertiesManager;
import org.antonaleks.pdd.utils.ResourceHelper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

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

        MongoHelper.getInstance().<Question>insertJsonMany(sb.toString(), PropertiesManager.getDbCollectionQuestion(), "questions");

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

        MongoHelper.getInstance().<Topic>insertJsonMany(sb.toString(), PropertiesManager.getDbCollectionTopics(), "topics");

    }

}
