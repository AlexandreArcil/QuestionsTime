package fr.canardnocturne.questionstime.question.type;

import fr.canardnocturne.questionstime.QuestionException;
import fr.canardnocturne.questionstime.question.Question;
import fr.canardnocturne.questionstime.question.component.Prize;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @Test
    void oneProposition() {
        final Question.QuestionBuilder builder = Question.builder()
                .setQuestion("What sound a duck does?")
                .setAnswers(new LinkedHashSet<>(List.of("Quack")))
                .setWeight(1)
                .setPropositions(List.of("Quack"));
        final Exception exception = assertThrows(QuestionException.class, builder::build);
        assertEquals("The question need at least 2 propositions", exception.getMessage());
    }

    @Test
    void tooManyPropositions() {
        final List<String> propositions = new ArrayList<>();
        for (int i = 0; i < 129; i++) {
            propositions.add("Quack " + i);
        }
        final Question.QuestionBuilder builder = Question.builder()
                .setQuestion("What sound a duck does?")
                .setAnswers(new LinkedHashSet<>(List.of("Quack 1")))
                .setWeight(1)
                .setPropositions(propositions);
        final Exception exception = assertThrows(QuestionException.class, builder::build);
        assertEquals("The question need at most 128 propositions", exception.getMessage());
    }

    @Test
    void answerOutOfBounds() {
        final Question.QuestionBuilder builder = Question.builder()
                .setQuestion("What sound a duck does?")
                .setAnswers(new LinkedHashSet<>(List.of("Beeee")))
                .setWeight(1)
                .setPropositions(List.of("Quack", "Meow", "Woof"));
        final Exception exception = assertThrows(QuestionException.class, builder::build);
        assertEquals("The question answers '[Beeee]' need to be a proposition", exception.getMessage());
    }

}