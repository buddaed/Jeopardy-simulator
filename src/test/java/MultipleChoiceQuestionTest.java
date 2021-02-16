import QuizQuestion.MultipleChoiceQuestion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * some tests to ensure the basic functions of the class work as intended
 */
class MultipleChoiceQuestionTest {
    MultipleChoiceQuestion mcq;

    @BeforeEach
    void innit() {
        mcq = new MultipleChoiceQuestion();
    }

    @Test
    void testSetAndGetQuestion() {
        mcq.setQuestionText("In March of 1979, Egypt was suspended from this organization for signing peace treaty with Israel");
        assertTrue(mcq.getQuestion().contains("In March of 1979"));
    }

    @Test
    void testCorrectAndAddChoice() {
        mcq.addChoice(" ", false);
        mcq.addChoice("saint", false);
        mcq.addChoice("topless", false);
        mcq.addChoice("the Arab League", true);
        assertEquals("the Arab League", mcq.correctText());
    }

    @Test
    void testEvaluate() {
        mcq.addChoice(" ", false);
        mcq.addChoice("saint", false);
        mcq.addChoice("topless", false);
        mcq.addChoice("the Arab League", true);
        assertEquals(true, mcq.evaluate(3));
    }

    @Test
    void testToString() {
        mcq.setQuestionText("In March of 1979, Egypt was suspended from this organization for signing peace treaty with Israel");
        mcq.addChoice("false", false);
        mcq.addChoice("flase", false);
        mcq.addChoice("fliss", false);
        mcq.addChoice("true", true);
        System.out.println(mcq);
        assertTrue(mcq.toString().contains("0: flase\n" +
                "1: fliss\n" +
                "2: false\n" +
                "3: true"));
    }

    @Test
    void testIfMoreThan4Choices() {
        mcq.addChoice(" ", false);
        mcq.addChoice("saint", false);
        mcq.addChoice("topless", false);
        mcq.addChoice("the Arab League", false);
        mcq.addChoice("the Arab League", false);
        assertEquals(4, mcq.getChoicesNum().size());
    }
}