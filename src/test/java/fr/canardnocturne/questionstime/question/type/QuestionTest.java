package fr.canardnocturne.questionstime.question.type;

import fr.canardnocturne.questionstime.QuestionException;
import fr.canardnocturne.questionstime.question.component.Prize;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

class QuestionTest {

    @Test
    void noQuestion() {
        final QuestionException exception = assertThrows(QuestionException.class, () -> Question.builder().build());
        Assertions.assertEquals("The question is not defined", exception.getMessage());
    }

    @Test
    void noAnswer() {
        final QuestionException exception = assertThrows(QuestionException.class, () -> Question.builder().setQuestion("What sound a duck does?").build());
        Assertions.assertEquals("The answer is not defined", exception.getMessage());
    }

    @Test
    void noWeight() {
        final QuestionException exception = assertThrows(QuestionException.class, () -> Question.builder().setQuestion("What sound a duck does?")
                .setAnswers(Set.of("Quack")).build());
        Assertions.assertEquals("Weight must be greater or equal than 1", exception.getMessage());
    }

    @Test
    void holesInPrizes() {
        final QuestionException exception = assertThrows(QuestionException.class, () -> Question.builder().setQuestion("What sound a duck does?")
                .setAnswers(Set.of("Quack"))
                .setWeight(1)
                .setPrizes(Set.of(new Prize(50, false, null, null, 1),
                        new Prize(50, false, null, null, 5)))
                .build());
        Assertions.assertEquals("The position prize 2 is missing", exception.getMessage());
    }

}