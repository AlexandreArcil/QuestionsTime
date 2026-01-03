package fr.canardnocturne.questionstime.question.type;

import fr.canardnocturne.questionstime.QuestionException;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class QuestionMultiTest {

    @Test
    void noPropositions() {
        final QuestionMulti.QuestionMultiBuilder builder = QuestionMulti.builder()
                .setQuestion("What sound a duck does?")
                .setAnswers(new LinkedHashSet<>(List.of("Quack")))
                .setWeight(1)
                .setPropositions(new LinkedHashSet<>());
        final Exception exception = assertThrows(QuestionException.class, builder::build);
        assertEquals("The question need at least 2 propositions", exception.getMessage());
    }

    @Test
    void oneProposition() {
        final QuestionMulti.QuestionMultiBuilder builder = QuestionMulti.builder()
                .setQuestion("What sound a duck does?")
                .setAnswers(new LinkedHashSet<>(List.of("1")))
                .setWeight(1)
                .setPropositions(new LinkedHashSet<>(List.of("Quack")));
        final Exception exception = assertThrows(QuestionException.class, builder::build);
        assertEquals("The question need at least 2 propositions", exception.getMessage());
    }

    @Test
    void tooManyPropositions() {
        final LinkedHashSet<String> propositions = new LinkedHashSet<>();
        for (int i = 0; i < 129; i++) {
            propositions.add("Quack " + i);
        }
        final QuestionMulti.QuestionMultiBuilder builder = QuestionMulti.builder()
                .setQuestion("What sound a duck does?")
                .setAnswers(new LinkedHashSet<>(List.of("1")))
                .setWeight(1)
                .setPropositions(propositions);
        final Exception exception = assertThrows(QuestionException.class, builder::build);
        assertEquals("The question need at most 128 propositions", exception.getMessage());
    }

    @Test
    void answerNotANumber() {
        final QuestionMulti.QuestionMultiBuilder builder = QuestionMulti.builder()
                .setQuestion("What sound a duck does?")
                .setAnswers(new LinkedHashSet<>(List.of("Quack")))
                .setWeight(1)
                .setPropositions(new LinkedHashSet<>(List.of("Quack", "Meow", "Woof")));
        final Exception exception = assertThrows(QuestionException.class, builder::build);
        assertEquals("The question answer 'Quack' needs to be a number between 1 and 3", exception.getMessage());
    }

    @Test
    void answerOutOfBounds() {
        final QuestionMulti.QuestionMultiBuilder builder = QuestionMulti.builder()
                .setQuestion("What sound a duck does?")
                .setAnswers(new LinkedHashSet<>(List.of("5")))
                .setWeight(1)
                .setPropositions(new LinkedHashSet<>(List.of("Quack", "Meow", "Woof")));
        final Exception exception = assertThrows(QuestionException.class, builder::build);
        assertEquals("The question answer '5' needs to be a number between 1 and 3", exception.getMessage());
    }

    @Test
    void answerNegative() {
        final QuestionMulti.QuestionMultiBuilder builder = QuestionMulti.builder()
                .setQuestion("What sound a duck does?")
                .setAnswers(new LinkedHashSet<>(List.of("-1")))
                .setWeight(1)
                .setPropositions(new LinkedHashSet<>(List.of("Quack", "Meow", "Woof")));
        final Exception exception = assertThrows(QuestionException.class, builder::build);
        assertEquals("The question answer '-1' needs to be a number between 1 and 3", exception.getMessage());
    }

}