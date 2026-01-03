package fr.canardnocturne.questionstime.question.creation.steps;

import fr.canardnocturne.questionstime.question.creation.QuestionCreator;
import fr.canardnocturne.questionstime.util.MiniMessageTest;
import net.kyori.adventure.audience.Audience;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class SimpleQuestionAnswerStepTest {
    
    @Mock
    private QuestionCreator qc;
    
    @Mock
    private Audience sender;

    @Test
    void questionDefined() {
        assertNotNull(SimpleQuestionAnswerStep.INSTANCE.question());
    }

    @Test
    void confirmWithNoAnswers() {
        Mockito.when(qc.getAnswers()).thenReturn(new ArrayList<>());

        final boolean finished = SimpleQuestionAnswerStep.INSTANCE.handle(sender, "confirm", qc);

        assertFalse(finished);
        Mockito.verify(sender).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).contains("The question must have at least one answer")
        ));
    }

    @Test
    void confirmWithAnswers() {
        final List<String> answers = new ArrayList<>(List.of("A"));
        Mockito.when(qc.getAnswers()).thenReturn(answers);

        final boolean finished = SimpleQuestionAnswerStep.INSTANCE.handle(sender, "confirm", qc);

        assertTrue(finished);
    }

    @Test
    void listNoAnswers() {
        Mockito.when(qc.getAnswers()).thenReturn(new ArrayList<>());

        final boolean finished = SimpleQuestionAnswerStep.INSTANCE.handle(sender, "list", qc);

        assertFalse(finished);
        Mockito.verify(sender).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).contains("No answer has been added yet")
        ));
    }

    @Test
    void listWithAnswers() {
        final List<String> answers = new ArrayList<>(List.of("One", "Two"));
        Mockito.when(qc.getAnswers()).thenReturn(answers);

        final boolean finished = SimpleQuestionAnswerStep.INSTANCE.handle(sender, "list", qc);

        assertFalse(finished);
        Mockito.verify(sender).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.containsAll(component, "[X] 1] One", "[X] 2] Two")
        ));
    }

    @Test
    void addSingleAnswer() {
        final List<String> answers = new ArrayList<>();
        Mockito.when(qc.getAnswers()).thenReturn(answers);

        final boolean finished = SimpleQuestionAnswerStep.INSTANCE.handle(sender, "add Coin", qc);

        assertFalse(finished);
        assertTrue(answers.contains("Coin"));
    }

    @Test
    void addMultipleAnswers() {
        final List<String> answers = new ArrayList<>();
        Mockito.when(qc.getAnswers()).thenReturn(answers);

        final boolean finished = SimpleQuestionAnswerStep.INSTANCE.handle(sender, "add A;B", qc);

        assertFalse(finished);
        assertTrue(answers.contains("A"));
        assertTrue(answers.contains("B"));
    }

    @Test
    void addExistingAnswer() {
        final List<String> answers = new ArrayList<>(List.of("Coin"));
        Mockito.when(qc.getAnswers()).thenReturn(answers);

        final boolean finished = SimpleQuestionAnswerStep.INSTANCE.handle(sender, "add Coin", qc);

        assertFalse(finished);
        Mockito.verify(sender).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).contains("Answer Coin already exists")
        ));
    }

    @Test
    void removeAnswerByPosition() {
        final List<String> answers = new ArrayList<>(List.of("First", "Second"));
        Mockito.when(qc.getAnswers()).thenReturn(answers);

        final boolean finished = SimpleQuestionAnswerStep.INSTANCE.handle(sender, "del 1", qc);

        assertFalse(finished);
        assertFalse(answers.contains("First"));
    }

    @Test
    void removeNonNumericPosition() {
        final boolean finished = SimpleQuestionAnswerStep.INSTANCE.handle(sender, "del a", qc);

        assertFalse(finished);
        Mockito.verify(sender).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).contains("a is not a valid position")
        ));
    }

    @Test
    void removeOutOfBounds() {
        
        
        final List<String> answers = new ArrayList<>(List.of("OnlyOne"));
        Mockito.when(qc.getAnswers()).thenReturn(answers);

        final boolean finished = SimpleQuestionAnswerStep.INSTANCE.handle(sender, "del 2", qc);

        assertFalse(finished);
        Mockito.verify(sender).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).contains("No answer is at the position 2")
        ));
    }

    @Test
    void removeNegativePosition() {
        final boolean finished = SimpleQuestionAnswerStep.INSTANCE.handle(sender, "del -1", qc);

        assertFalse(finished);
        Mockito.verify(sender).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).contains("No answer is at the position -1")
        ));
    }

    @Test
    void deleteWithoutPosition() {
        final boolean finished = SimpleQuestionAnswerStep.INSTANCE.handle(sender, "del", qc);

        assertFalse(finished);
        Mockito.verify(sender).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).contains("You must specify the position of the answer to delete")
        ));
    }

    @Test
    void addWithoutAnswer() {
        final boolean finished = SimpleQuestionAnswerStep.INSTANCE.handle(sender, "add", qc);

        assertFalse(finished);
        Mockito.verify(sender).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).contains("You must specify an answer")
        ));
    }

    @Test
    void unknownCommand() {
        final boolean finished = SimpleQuestionAnswerStep.INSTANCE.handle(sender, "coin", qc);

        assertFalse(finished);
        Mockito.verify(sender).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).contains("Unknown command coin between add, del and list")
        ));
    }

    @Test
    void shouldNotSkip() {
        assertFalse(SimpleQuestionAnswerStep.INSTANCE.shouldSkip(qc));
    }

    @Test
    void nextDefined() {
        assertNotNull(SimpleQuestionAnswerStep.INSTANCE.next(qc));
    }
}