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
class WeightStepTest {

    @Mock
    private QuestionCreator qc;

    @Mock
    private Audience sender;

    @Test
    void questionDefined() {
        assertNotNull(WeightStep.INSTANCE.question());
    }

    @Test
    void confirm() {
        final boolean finished = WeightStep.INSTANCE.handle(sender, "yes", qc);

        assertTrue(finished);
        Mockito.verify(qc, Mockito.never()).setWeight(Mockito.anyInt());
        Mockito.verifyNoMoreInteractions(sender);
    }

    @Test
    void setPositiveWeight() {
        final boolean finished = WeightStep.INSTANCE.handle(sender, "3", qc);

        assertFalse(finished);
        Mockito.verify(qc).setWeight(3);
        Mockito.verify(sender).sendMessage(Mockito.argThat(component ->
            MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).contains("Is 3 correct ? If yes, answer with [/qtc yes] or just re-answer to change the value")
        ));
    }

    @Test
    void noWeightSet() {
        final boolean finished = WeightStep.INSTANCE.handle(sender, "1", qc);

        assertFalse(finished);
        Mockito.verify(qc).setWeight(1);
        Mockito.verify(sender).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).contains("You really don't want to set a weight for this question ? If yes, answer with [/qtc yes] or just re-answer to change the value")
        ));
    }

    @Test
    void invalidNumber() {
        final boolean finished = WeightStep.INSTANCE.handle(sender, "coin", qc);

        assertFalse(finished);
        Mockito.verify(sender).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).contains("coin is not a number")
        ));
    }

    @Test
    void negativeWeight() {
        final boolean finished = WeightStep.INSTANCE.handle(sender, "0", qc);

        assertFalse(finished);
        Mockito.verify(sender).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).contains("0 is not a amount superior or equal to 1")
        ));
    }

    @Test
    void shouldNotSkip() {
        assertFalse(WeightStep.INSTANCE.shouldSkip(qc));
    }

    @Test
    void nextDefined() {
        assertNull(WeightStep.INSTANCE.next(qc));
    }

}