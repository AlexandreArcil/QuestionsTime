package fr.canardnocturne.questionstime.question.ask.pool;

import fr.canardnocturne.questionstime.question.type.Question;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WeightSortedQuestionPoolTest {

    @Test
    void addQuestion() {
        final Question question = Mockito.mock(Question.class);
        Mockito.when(question.getWeight()).thenReturn(5);
        Mockito.when(question.getQuestion()).thenReturn("Is CanardNocturne a duck?");

        final WeightSortedQuestionPool pool = new WeightSortedQuestionPool(new ArrayList<>());
        pool.add(question);

        assertTrue(pool.getAll().contains(question));
    }

    @Test
    void getQuestion() {
        final Question question = Mockito.mock(Question.class);
        final String questionText = "Is CanardNocturne a duck?";
        Mockito.when(question.getQuestion()).thenReturn(questionText);

        final WeightSortedQuestionPool pool = new WeightSortedQuestionPool(new ArrayList<>(List.of(question)));
        final Optional<Question> questionFound = pool.get(questionText);

        assertTrue(questionFound.isPresent());
        assertEquals(question, questionFound.get());
    }

    @Test
    void questionAreSortedByWeight() {
        final Question lowWeightQuestion = Mockito.mock(Question.class);
        Mockito.when(lowWeightQuestion.getWeight()).thenReturn(1);
        Mockito.when(lowWeightQuestion.getQuestion()).thenReturn("Low weight question");

        final Question highWeightQuestion = Mockito.mock(Question.class);
        Mockito.when(highWeightQuestion.getWeight()).thenReturn(10);
        Mockito.when(highWeightQuestion.getQuestion()).thenReturn("High weight question");

        final WeightSortedQuestionPool pool = new WeightSortedQuestionPool(new ArrayList<>(List.of(highWeightQuestion, lowWeightQuestion)));
        final List<Question> allQuestions = new ArrayList<>(pool.getAll());

        assertEquals(lowWeightQuestion, allQuestions.get(0));
        assertEquals(highWeightQuestion, allQuestions.get(1));
    }

}