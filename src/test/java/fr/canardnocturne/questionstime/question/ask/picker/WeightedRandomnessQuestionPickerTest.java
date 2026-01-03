package fr.canardnocturne.questionstime.question.ask.picker;

import fr.canardnocturne.questionstime.question.ask.pool.QuestionPool;
import fr.canardnocturne.questionstime.question.type.Question;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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
        final Question question = picker.pick();

        assertNotNull(question);
    }

}