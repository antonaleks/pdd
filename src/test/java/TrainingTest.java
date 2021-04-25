import com.fasterxml.jackson.core.JsonProcessingException;
import org.antonaleks.pdd.model.Category;
import org.antonaleks.pdd.model.Training;
import org.junit.jupiter.api.Test;

public class TrainingTest {
    @Test
    void testNewTrainingByTopic() {

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
