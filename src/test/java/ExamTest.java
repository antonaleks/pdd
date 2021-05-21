import org.antonaleks.pdd.model.Category;
import org.antonaleks.pdd.model.Exam;
import org.junit.jupiter.api.Test;

public class ExamTest {
    @Test
    void testNewExam() {
        Exam exam = new Exam(Category.AB);
        exam.run();
    }

    @Test
    void testSaveStatistic() {

    }

    @Test
    void testExamFailedByTimer() {

    }

    @Test
    void testExamFailedByMistakes() {

    }

    @Test
    void testExamAddNewQuestionsByMistakes() {

    }

    @Test
    void testExamSuccess() {

    }
}
