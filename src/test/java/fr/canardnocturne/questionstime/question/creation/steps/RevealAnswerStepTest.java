package fr.canardnocturne.questionstime.question.creation.steps;

import fr.canardnocturne.questionstime.question.creation.QuestionCreator;
import net.kyori.adventure.audience.Audience;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RevealAnswerStepTest {

    @Mock
    private QuestionCreator questionCreator;

    @Mock
    private Audience sender;

    @Test
    void questionNonNull() {
        assertNotNull(RevealAnswerStep.INSTANCE.question());
    }

    @Test
    void revealAnswer() {
        assertTrue(RevealAnswerStep.INSTANCE.handle(sender, "yes", questionCreator));
        Mockito.verify(questionCreator).setRevealAnswer(true);
    }

    @Test
    void notRevealAnswer() {
        assertTrue(RevealAnswerStep.INSTANCE.handle(sender, "no", questionCreator));
        Mockito.verify(questionCreator).setRevealAnswer(false);
    }

    @Test
    void invalidAnswer() {
        assertFalse(RevealAnswerStep.INSTANCE.handle(sender, "coin", questionCreator));
        Mockito.verify(questionCreator, Mockito.never()).setRevealAnswer(Mockito.anyBoolean());
    }

    @Test
    void shouldNeverSkip() {
        assertFalse(RevealAnswerStep.INSTANCE.shouldSkip(questionCreator));
    }

    @Test
    void nextStepDefined() {
        assertNotNull(RevealAnswerStep.INSTANCE.next(questionCreator));
    }

}