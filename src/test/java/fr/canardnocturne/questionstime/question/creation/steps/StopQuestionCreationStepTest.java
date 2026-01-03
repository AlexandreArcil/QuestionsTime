package fr.canardnocturne.questionstime.question.creation.steps;

import fr.canardnocturne.questionstime.question.creation.QuestionCreator;
import fr.canardnocturne.questionstime.util.MiniMessageTest;
import net.kyori.adventure.audience.Audience;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class StopQuestionCreationStepTest {

    @Mock
    private QuestionCreator qc;

    @Mock
    private Audience sender;

    @Test
    void questionDefined() {
        assertNotNull(StopQuestionCreationStep.INSTANCE.question());
    }

    @Test
    void questionCreationStopped() {
        final boolean finished = StopQuestionCreationStep.INSTANCE.handle(sender, "yes", qc);

        assertTrue(finished);
        Mockito.verify(qc).setStopped();
        Mockito.verifyNoMoreInteractions(sender);
    }

    @Test
    void questionCreationContinue() {
        final boolean finished = StopQuestionCreationStep.INSTANCE.handle(sender, "no", qc);

        assertTrue(finished);
        Mockito.verify(qc, Mockito.never()).setStopped();
        Mockito.verifyNoMoreInteractions(sender);
    }

    @Test
    void invalidAnswer() {
        final boolean finished = StopQuestionCreationStep.INSTANCE.handle(sender, "maybe", qc);

        assertFalse(finished);
        Mockito.verify(sender).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).contains("The answer need to be yes OR no")
        ));
    }

    @Test
    void shouldSkip() {
        assertFalse(StopQuestionCreationStep.INSTANCE.shouldSkip(qc));
    }

    @Test
    void nextStep() {
        assertNull(StopQuestionCreationStep.INSTANCE.next(qc));
    }
}