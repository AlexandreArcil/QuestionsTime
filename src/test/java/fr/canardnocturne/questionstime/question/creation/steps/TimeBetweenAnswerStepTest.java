package fr.canardnocturne.questionstime.question.creation.steps;

import fr.canardnocturne.questionstime.question.creation.QuestionCreator;
import fr.canardnocturne.questionstime.util.MiniMessageTest;
import net.kyori.adventure.audience.Audience;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class TimeBetweenAnswerStepTest {

    @Mock
    private QuestionCreator qc;

    @Mock
    private Audience sender;

    @Test
    void questionDefined() {
        assertNotNull(TimeBetweenAnswerStep.INSTANCE.question());
    }

    @Test
    void confirm() {
        Mockito.when(qc.getTimeBetweenAnswer()).thenReturn(10);

        final boolean finished = TimeBetweenAnswerStep.INSTANCE.handle(sender, "yes", qc);

        assertTrue(finished);
        Mockito.verify(qc, Mockito.never()).setTimeBetweenAnswer(Mockito.any());
        Mockito.verifyNoMoreInteractions(sender);
    }

    @Test
    void setValidTime() {
        final boolean finished = TimeBetweenAnswerStep.INSTANCE.handle(sender, "1h0m5s", qc);

        assertFalse(finished);
        Mockito.verify(qc).setTimeBetweenAnswer(LocalTime.of(1, 0, 5));
        Mockito.verify(sender).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.containsAll(component, "Is the time 1h0m5s between two answers correct ?", "[/qtc yes] or just repeat the command")
        ));
    }

    @Test
    void removeTime() {
        final boolean finished = TimeBetweenAnswerStep.INSTANCE.handle(sender, "0h0m0s", qc);

        assertFalse(finished);
        Mockito.verify(qc).setTimeBetweenAnswer(LocalTime.MIDNIGHT);
        Mockito.verify(sender).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.containsAll(component,"You don't want to add a time between answers ?", "[/qtc yes] or just repeat the command")
        ));
    }

    @Test
    void invalidFormat() {
        final Audience sender = Mockito.mock(Audience.class);
        final QuestionCreator qc = Mockito.mock(QuestionCreator.class);

        final boolean finished = TimeBetweenAnswerStep.INSTANCE.handle(sender, "invalid", qc);

        assertFalse(finished);
        Mockito.verify(sender).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).contains("invalid doesn't match the expected time format xhxmxs where x is a number, or it's an incorrect time")
        ));
    }

    @Test
    void shouldNotSkip() {
        final QuestionCreator qc = Mockito.mock(QuestionCreator.class);
        assertFalse(TimeBetweenAnswerStep.INSTANCE.shouldSkip(qc));
    }

    @Test
    void nextDefined() {
        final QuestionCreator qc = Mockito.mock(QuestionCreator.class);
        assertEquals(WeightStep.INSTANCE, TimeBetweenAnswerStep.INSTANCE.next(qc));
    }

}