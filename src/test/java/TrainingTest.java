
import org.antonaleks.pdd.db.MongoHelper;
import org.antonaleks.pdd.entity.Topic;
import org.antonaleks.pdd.model.Category;
import org.antonaleks.pdd.model.Training;
import org.antonaleks.pdd.utils.PropertiesManager;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TrainingTest {
    @Test
    void testNewTrainingByTopic() {
        List<Topic> topics = MongoHelper.getInstance().getDocumentList(Topic.class, PropertiesManager.getDbCollectionTopics());
        Training training = new Training(topics.get(0), Category.AB);
        training.run();
    }

    @Test
    void testTrainingFail() {

    }

    @Test
    void testNewTrainingByTicket() {
        Training training = new Training(1, Category.AB);
        training.run();
    }
}
