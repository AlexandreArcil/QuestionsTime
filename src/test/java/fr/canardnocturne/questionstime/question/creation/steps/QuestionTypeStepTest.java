package fr.canardnocturne.questionstime.question.creation.steps;

import fr.canardnocturne.questionstime.question.creation.QuestionCreator;
import fr.canardnocturne.questionstime.question.type.Question;
import fr.canardnocturne.questionstime.util.MiniMessageTest;
import net.kyori.adventure.audience.Audience;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class QuestionTypeStepTest {

    @Mock
    private QuestionCreator qc;
    
    @Mock
    private Audience sender;
    
    @Test
    void questionDefined() {
        assertNotNull(QuestionTypeStep.INSTANCE.question());
    }

    @Test
    void setSimpleType() {
        final boolean finished = QuestionTypeStep.INSTANCE.handle(sender, "simple", qc);

        assertTrue(finished);
        Mockito.verify(qc).setQuestionType(Question.Types.SIMPLE);
        Mockito.verifyNoMoreInteractions(sender);
    }

    @Test
    void setPropositionType() {
        final boolean finished = QuestionTypeStep.INSTANCE.handle(sender, "proposition", qc);

        assertTrue(finished);
        Mockito.verify(qc).setQuestionType(Question.Types.MULTI);
        Mockito.verifyNoMoreInteractions(sender);
    }

    @Test
    void invalidType() {
        final boolean finished = QuestionTypeStep.INSTANCE.handle(sender, "unknown", qc);

        assertFalse(finished);
        Mockito.verify(sender).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).contains("The question's type can only be simple or proposition, not unknown")
        ));
    }

    @Test
    void nextReturnsSimpleStepForSimpleType() {
        Mockito.when(qc.getQuestionType()).thenReturn(Question.Types.SIMPLE);

        assertEquals(SimpleQuestionAnswerStep.INSTANCE, QuestionTypeStep.INSTANCE.next(qc));
    }

    @Test
    void nextReturnsPropositionStepForMultiType() {
        Mockito.when(qc.getQuestionType()).thenReturn(Question.Types.MULTI);

        assertEquals(QuestionPropositionStep.INSTANCE, QuestionTypeStep.INSTANCE.next(qc));
    }

    @Test
    void nextThrowsWhenTypeUndefined() {
        Mockito.when(qc.getQuestionType()).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> QuestionTypeStep.INSTANCE.next(qc));
    }

    @Test
    void shouldNotSkip() {
        assertFalse(QuestionTypeStep.INSTANCE.shouldSkip(qc));
    }
}