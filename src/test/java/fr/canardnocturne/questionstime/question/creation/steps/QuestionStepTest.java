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
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class QuestionStepTest {
    
    @Mock
    private Audience sender;
    
    @Mock
    private QuestionCreator questionCreator;

    @Test
    void questionDefined() {
        assertNotNull(QuestionStep.INSTANCE.question());
    }

    @Test
    void setQuestion() {
        final String answer = "What sound a duck do?";

        final boolean finished = QuestionStep.INSTANCE.handle(sender, answer, questionCreator);

        assertFalse(finished);
        Mockito.verify(questionCreator).setQuestion(answer);
        Mockito.verify(sender).sendMessage(Mockito.argThat(component ->
            MiniMessageTest.containsAll(component, "Is " + answer + " correct ?", "[/qtc yes] or just repeat the command")
        ));
    }

    @Test
    void confirmQuestion() {
        final String answer = "yes";
        Mockito.when(questionCreator.getQuestion()).thenReturn("What sound a duck do?");

        final boolean finished = QuestionStep.INSTANCE.handle(sender, answer, questionCreator);

        assertTrue(finished);
        Mockito.verify(questionCreator, Mockito.never()).setQuestion(Mockito.any());
        Mockito.verifyNoMoreInteractions(sender);
    }

    @Test
    void yesWhenNoExistingQuestionSets() {
        final String answer = "yes";
        Mockito.when(questionCreator.getQuestion()).thenReturn("");

        final boolean finished = QuestionStep.INSTANCE.handle(sender, answer, questionCreator);

        assertFalse(finished);
        Mockito.verify(questionCreator).setQuestion(answer);
        Mockito.verify(sender).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.containsAll(component, "Is " + answer + " correct ?", "[/qtc yes] or just repeat the command")
        ));
    }

    @Test
    void shouldNotSkip() {
        assertFalse(QuestionStep.INSTANCE.shouldSkip(questionCreator));
    }

    @Test
    void nextStepDefined() {
        assertNotNull(QuestionStep.INSTANCE.next(questionCreator));
    }


}