
import org.antonaleks.pdd.db.MongoHelper;
import org.antonaleks.pdd.model.Category;
import org.antonaleks.pdd.model.Training;
import org.junit.jupiter.api.Test;

public class TrainingTest {
    @Test
    void testNewTrainingByTopic() {
        var topics = MongoHelper.getInstance().getTopicList();
        var training = new Training(topics.get(0), Category.AB);
        training.run();
    }

    @Test
    void testTrainingFail() {

    }

    @Test
    void testNewTrainingByTicket() {
        var training = new Training(1, Category.AB);
        training.run();
    }
}
