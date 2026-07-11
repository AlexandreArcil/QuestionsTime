package fr.canardnocturne.questionstime.question.ask.picker;

import fr.canardnocturne.questionstime.question.ask.pool.QuestionPool;
import fr.canardnocturne.questionstime.question.Question;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WeightedRandomnessQuestionPickerTest {

    @Test
    void pickQuestion() {
        final QuestionPool questionPool = Mockito.mock(QuestionPool.class);
        final Logger logger = Mockito.mock(Logger.class);
        final Question question1 = Mockito.mock(Question.class);
        final Question question2 = Mockito.mock(Question.class);
        Mockito.when(question1.getWeight()).thenReturn(1);
        Mockito.when(question2.getWeight()).thenReturn(3);
        Mockito.when(questionPool.getAll()).thenReturn(Set.of(question1, question2));

        final WeightedRandomnessQuestionPicker picker = new WeightedRandomnessQuestionPicker(questionPool, logger);
        final Question question = picker.pick(Collections.emptyList());

        assertNotNull(question);
    }

    @Test
    void pickQuestionWithTags() {
        final QuestionPool questionPool = Mockito.mock(QuestionPool.class);
        final Logger logger = Mockito.mock(Logger.class);
        final Question question1 = Mockito.mock(Question.class);
        final Question question2 = Mockito.mock(Question.class);
        final Question question3 = Mockito.mock(Question.class);
        final Question question4 = Mockito.mock(Question.class);
        final Question question5 = Mockito.mock(Question.class);
        Mockito.when(question1.getWeight()).thenReturn(1);
        Mockito.when(question2.getWeight()).thenReturn(3);
        Mockito.when(question2.getTags()).thenReturn(Set.of("tag1", "tag2"));
        Mockito.when(question3.getWeight()).thenReturn(2);
        Mockito.when(question3.getTags()).thenReturn(Set.of("tag3"));
        Mockito.when(question4.getWeight()).thenReturn(4);
        Mockito.when(question4.getTags()).thenReturn(Set.of("tag1"));
        Mockito.when(question5.getWeight()).thenReturn(5);
        Mockito.when(question5.getTags()).thenReturn(Set.of("tag1", "tag2"));
        Mockito.when(questionPool.getAll()).thenReturn(Set.of(question1, question2, question3, question4, question5));

        final WeightedRandomnessQuestionPicker picker = new WeightedRandomnessQuestionPicker(questionPool, logger);
        final Question question = picker.pick(List.of("tag1", "tag2"));

        assertNotNull(question);
    }

    @Test
    void noQuestionsWithTheRequiredTags() {
        final QuestionPool questionPool = Mockito.mock(QuestionPool.class);
        final Logger logger = Mockito.mock(Logger.class);
        final Question question1 = Mockito.mock(Question.class);
        final Question question2 = Mockito.mock(Question.class);
        final Question question3 = Mockito.mock(Question.class);
        final Question question4 = Mockito.mock(Question.class);
        final Question question5 = Mockito.mock(Question.class);
        Mockito.when(question1.getWeight()).thenReturn(1);
        Mockito.when(question2.getWeight()).thenReturn(3);
        Mockito.when(question2.getTags()).thenReturn(Set.of("tag1", "tag2"));
        Mockito.when(question3.getWeight()).thenReturn(2);
        Mockito.when(question3.getTags()).thenReturn(Set.of("tag3"));
        Mockito.when(question4.getWeight()).thenReturn(4);
        Mockito.when(question4.getTags()).thenReturn(Set.of("tag1"));
        Mockito.when(question5.getWeight()).thenReturn(5);
        Mockito.when(question5.getTags()).thenReturn(Set.of("tag1", "tag2"));
        Mockito.when(questionPool.getAll()).thenReturn(Set.of(question1, question2, question3, question4, question5));

        final WeightedRandomnessQuestionPicker picker = new WeightedRandomnessQuestionPicker(questionPool, logger);
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> picker.pick(List.of("tag4")));

        assertEquals("No question found with tags: tag4", exception.getMessage());
    }

}