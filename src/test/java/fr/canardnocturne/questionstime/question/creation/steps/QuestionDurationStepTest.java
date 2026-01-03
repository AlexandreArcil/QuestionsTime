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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class QuestionDurationStepTest {
    
    @Mock
    private QuestionCreator questionCreator;
    
    @Mock
    private Audience sender;

    @Test
    void questionDefined() {
        assertNotNull(QuestionDurationStep.INSTANCE.question());
    }

    @Test
    void setDuration() {
        final String answer = "1h30m0s";

        final boolean finished = QuestionDurationStep.INSTANCE.handle(sender, answer, questionCreator);

        assertFalse(finished);
        Mockito.verify(questionCreator).setDuration(LocalTime.of(1, 30));
        Mockito.verify(sender).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.containsAll(component, "Is the question duration 1h30m0s correct ?", "Answer with [/qtc yes] or just repeat the command")) );
    }

    @Test
    void setZeroDuration() {
        final String answer = "0h0m0s";

        final boolean finished = QuestionDurationStep.INSTANCE.handle(sender, answer, questionCreator);

        assertFalse(finished);
        Mockito.verify(questionCreator).setDuration(LocalTime.of(0, 0));
        Mockito.verify(sender).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.containsAll(component, "You don't want to add a time to answer the question ?",  "Answer with [/qtc yes] or just repeat the command")));
    }

    @Test
    void answerInvalidFormat() {
        final String answer = "invalid_format";

        final boolean finished = QuestionDurationStep.INSTANCE.handle(sender, answer, questionCreator);

        assertFalse(finished);
        Mockito.verify(sender).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).contains("invalid_format doesn't match the expected time format xhxmxs where x is a number, or it's an incorrect time")));
    }

    @Test
    void confirmAnswer() {
        final String answer = "yes";

        final boolean finished = QuestionDurationStep.INSTANCE.handle(sender, answer, questionCreator);

        assertTrue(finished);
        Mockito.verifyNoInteractions(questionCreator);
        Mockito.verifyNoMoreInteractions(sender);
    }

    @Test
    void shouldNotSkip() {
        assertFalse(QuestionDurationStep.INSTANCE.shouldSkip(questionCreator));
    }

    @Test
    void nextStepDefined() {
        assertNotNull(QuestionDurationStep.INSTANCE.next(questionCreator));
    }

}