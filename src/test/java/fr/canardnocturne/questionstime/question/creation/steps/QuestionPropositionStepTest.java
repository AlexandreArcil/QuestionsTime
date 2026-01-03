package fr.canardnocturne.questionstime.question.creation.steps;

import fr.canardnocturne.questionstime.question.creation.QuestionCreator;
import fr.canardnocturne.questionstime.util.MiniMessageTest;
import net.kyori.adventure.audience.Audience;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class QuestionPropositionStepTest {

    @Mock
    private QuestionCreator questionCreator;
    
    @Mock
    private Audience sender;
    
    @Test
    void questionDefined() {
        assertNotNull(QuestionPropositionStep.INSTANCE.question());
    }

    @Test
    void addProposition() {
        final String answer = "add Coin";
        final List<String> propositions = new ArrayList<>();
        Mockito.when(questionCreator.getPropositions()).thenReturn(propositions);

        final boolean finished = QuestionPropositionStep.INSTANCE.handle(sender, answer, questionCreator);

        assertFalse(finished);
        assertEquals(1, propositions.size());
        assertEquals("Coin", propositions.getFirst());
        Mockito.verify(sender).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.containsAll(component, "Propositions added:", "[1] Coin")
        ));
    }

    @Test
    void addMultiplePropositions() {
        final String answer = "add Coin;Duck;Feather";
        final List<String> propositions = new ArrayList<>();
        Mockito.when(questionCreator.getPropositions()).thenReturn(propositions);

        final boolean finished = QuestionPropositionStep.INSTANCE.handle(sender, answer, questionCreator);

        assertFalse(finished);
        assertEquals(3, propositions.size());
        Mockito.verify(sender).sendMessage(Mockito.argThat(component ->
            MiniMessageTest.containsAll(component, "Propositions added:", "[1] Coin", "[2] Duck", "[3] Feather")
        ));
    }

    @Test
    void addPropositionAlreadyExists() {
        final String answer = "add Coin";
        final List<String> propositions = List.of("Coin");
        Mockito.when(questionCreator.getPropositions()).thenReturn(propositions);

        final boolean finished = QuestionPropositionStep.INSTANCE.handle(sender, answer, questionCreator);

        assertFalse(finished);
        assertEquals(1, propositions.size());
        Mockito.verify(sender).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).contains("Proposition Coin already exists")
        ));
    }

    @Test
    void addPropositionWhileFull() {
        final String answer = "add Coin";
        final ArrayList<String> propositions = Mockito.mock(ArrayList.class);
        Mockito.when(propositions.size()).thenReturn(128);
        Mockito.when(questionCreator.getPropositions()).thenReturn(propositions);

        final boolean finished = QuestionPropositionStep.INSTANCE.handle(sender, answer, questionCreator);

        assertFalse(finished);
        Mockito.verify(sender).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).contains("You cannot add more than 128 propositions. Please remove some propositions before adding new ones")
        ));
    }

    @Test
    void modifiesExistingProposition() {
        final String answer = "set 1 NewCoin";
        final List<String> propositions = new ArrayList<>(List.of("OldCoin"));
        Mockito.when(questionCreator.getPropositions()).thenReturn(propositions);

        final boolean finished = QuestionPropositionStep.INSTANCE.handle(sender, answer, questionCreator);

        assertFalse(finished);
        assertEquals("NewCoin", propositions.getFirst());
        Mockito.verify(sender).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).contains("Proposition [1] modified from OldCoin to NewCoin")
        ));
    }

    @Test
    void missingPropositionToModify() {
        final String answer = "set 1";
        final List<String> propositions = List.of("OnlyOne");
        Mockito.when(questionCreator.getPropositions()).thenReturn(propositions);

        final boolean finished = QuestionPropositionStep.INSTANCE.handle(sender, answer, questionCreator);

        assertFalse(finished);
        Mockito.verify(sender).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).contains("The proposition is missing")
        ));
    }

    @Test
    void setPrepositionWithNonNumericPosition() {
        final String answer = "set a New";

        final boolean finished = QuestionPropositionStep.INSTANCE.handle(sender, answer, questionCreator);

        assertFalse(finished);
        Mockito.verify(sender).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).contains("The position should be a positive number, corresponding to a proposition in the [/qtc list]")
        ));
    }

    @Test
    void setPropositionOutOfBounds() {
        final String answer = "set 2 New";
        final List<String> propositions = List.of("OnlyOne");
        Mockito.when(questionCreator.getPropositions()).thenReturn(propositions);

        final boolean finished = QuestionPropositionStep.INSTANCE.handle(sender, answer, questionCreator);

        assertFalse(finished);
        Mockito.verify(sender).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).contains("There is no proposition at position 2. Type [/qtc list] to see them")
        ));
    }

    @Test
    void removesProposition() {
        final String answer = "del 1";
        final List<String> propositions = new ArrayList<>(List.of("Coin", "Other"));
        final List<String> answers = new ArrayList<>();
        Mockito.when(questionCreator.getPropositions()).thenReturn(propositions);
        Mockito.when(questionCreator.getAnswers()).thenReturn(answers);

        final boolean finished = QuestionPropositionStep.INSTANCE.handle(sender, answer, questionCreator);

        assertFalse(finished);
        assertEquals(1, propositions.size());
        assertEquals("Other", propositions.getFirst());
        Mockito.verify(sender).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).contains("Proposition [1] Coin deleted !")
        ));
        Mockito.verify(sender, Mockito.never()).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).contains("The proposition was automatically removed from the answers")
        ));
    }

    @Test
    void removesPropositionAndAnswer() {
        final String answer = "del 1";
        final List<String> propositions = new ArrayList<>(List.of("Coin", "Other"));
        final List<String> answers = new ArrayList<>(List.of("Coin"));
        Mockito.when(questionCreator.getPropositions()).thenReturn(propositions);
        Mockito.when(questionCreator.getAnswers()).thenReturn(answers);

        final boolean finished = QuestionPropositionStep.INSTANCE.handle(sender, answer, questionCreator);

        assertFalse(finished);
        assertEquals(1, propositions.size());
        assertEquals("Other", propositions.getFirst());
        Mockito.verify(sender).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).contains("Proposition [1] Coin deleted !")
        ));
        Mockito.verify(sender).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).contains("The proposition was automatically removed from the answers")
        ));
        assertFalse(answers.contains("Coin"));
    }

    @Test
    void addPropositionAsAnswer() {
        final String answer = "answers 1";
        final List<String> propositions = List.of("Coin", "Other");
        final List<String> answers = new ArrayList<>();
        Mockito.when(questionCreator.getPropositions()).thenReturn(propositions);
        Mockito.when(questionCreator.getAnswers()).thenReturn(answers);

        final boolean finished = QuestionPropositionStep.INSTANCE.handle(sender, answer, questionCreator);

        assertFalse(finished);
        assertEquals(1, answers.size());
        assertEquals("Coin", answers.getFirst());
        Mockito.verify(sender).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).contains("Proposition [1] Coin added as an answer")
        ));
    }

    @Test
    void propositionAlreadyAnAnswer() {
        final String answer = "answers 1";
        final List<String> propositions = List.of("Coin", "Other");
        final List<String> answers = new ArrayList<>(List.of("Coin"));
        Mockito.when(questionCreator.getPropositions()).thenReturn(propositions);
        Mockito.when(questionCreator.getAnswers()).thenReturn(answers);

        final boolean finished = QuestionPropositionStep.INSTANCE.handle(sender, answer, questionCreator);

        assertFalse(finished);
        assertEquals(1, answers.size());
        Mockito.verify(sender).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).contains("Proposition [1] Coin is already an answer")
        ));
    }

    @Test
    void addAnswerOutOfBounds() {
        final String answer = "answers 2";
        final List<String> propositions = List.of("Coin");
        Mockito.when(questionCreator.getPropositions()).thenReturn(propositions);

        final boolean finished = QuestionPropositionStep.INSTANCE.handle(sender, answer, questionCreator);

        assertFalse(finished);
        Mockito.verify(sender).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).contains("There is no proposition at position 2. Type [/qtc list] to see them")
        ));
    }

    @Test
    void listPropositions() {
        final String answer = "list";
        final List<String> propositions = List.of("Coin", "Duck");
        final List<String> answers = List.of("Coin");
        Mockito.when(questionCreator.getPropositions()).thenReturn(propositions);
        Mockito.when(questionCreator.getAnswers()).thenReturn(answers);

        final boolean finished = QuestionPropositionStep.INSTANCE.handle(sender, answer, questionCreator);

        assertFalse(finished);
        Mockito.verify(sender).sendMessage(Mockito.argThat(component ->
            MiniMessageTest.containsAll(component, "1] Coin (an answer)", "2] Duck")
        ));
    }

    @Test
    void listPropositionsWhenEmpty() {
        final String answer = "list";
        final List<String> propositions = new ArrayList<>();
        Mockito.when(questionCreator.getPropositions()).thenReturn(propositions);

        final boolean finished = QuestionPropositionStep.INSTANCE.handle(sender, answer, questionCreator);

        assertFalse(finished);
        Mockito.verify(sender).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).contains("No propositions have been made")
        ));
    }

    @Test
    void confirmPropositions() {
        final String answer = "confirm";
        final List<String> propositions = List.of("Coin", "Duck");
        final List<String> answers = List.of("Coin");
        Mockito.when(questionCreator.getPropositions()).thenReturn(propositions);
        Mockito.when(questionCreator.getAnswers()).thenReturn(answers);

        final boolean finished = QuestionPropositionStep.INSTANCE.handle(sender, answer, questionCreator);

        assertTrue(finished);
    }

    @Test
    void confirmPropositionsNotEnoughAnswers() {
        final String answer = "confirm";
        final List<String> propositions = List.of("Coin", "Duck");
        final List<String> answers = new ArrayList<>();
        Mockito.when(questionCreator.getPropositions()).thenReturn(propositions);
        Mockito.when(questionCreator.getAnswers()).thenReturn(answers);

        final boolean finished = QuestionPropositionStep.INSTANCE.handle(sender, answer, questionCreator);

        assertFalse(finished);
        Mockito.verify(sender).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).contains("You need to choose at least one answer with /qtc answers [proposition] before confirming")
        ));
    }

    @Test
    void confirmPropositionsNotEnoughPropositions() {
        final String answer = "confirm";
        final List<String> propositions = List.of("OnlyOne");
        Mockito.when(questionCreator.getPropositions()).thenReturn(propositions);

        final boolean finished = QuestionPropositionStep.INSTANCE.handle(sender, answer, questionCreator);

        assertFalse(finished);
        Mockito.verify(sender).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).contains("You need to write at least 2 propositions with /qtc add [proposition] before confirming")
        ));
    }

    @Test
    void confirmTwoArguments() {
        final String answer = "confirm extra";

        final boolean finished = QuestionPropositionStep.INSTANCE.handle(sender, answer, questionCreator);

        assertFalse(finished);
        Mockito.verify(sender).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).contains("Command confirm doesn't take a second argument")
        ));
    }

    @Test
    void listTwoArguments() {
        final String answer = "list extra";

        final boolean finished = QuestionPropositionStep.INSTANCE.handle(sender, answer, questionCreator);

        assertFalse(finished);
        Mockito.verify(sender).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).contains("Command list doesn't take a second argument")
        ));
    }

    @Test
    void unknownCommandWithArgument() {
        final String answer = "unknown command";

        final boolean finished = QuestionPropositionStep.INSTANCE.handle(sender, answer, questionCreator);

        assertFalse(finished);
        Mockito.verify(sender).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).contains("Answer unknown command not recognized between add, set, del, list, answers or confirm")
        ));
    }

    static Stream<Arguments> oneArgumentCommandsWithMissingArgument() {
        return Stream.of(Arguments.of("add", "Command add needs to be followed by a proposition"),
                Arguments.of("set", "Command set needs to be followed by a position then a proposition"),
                Arguments.of("del", "Command del needs to be followed by a position"),
                Arguments.of("answers", "Command answers needs to be followed by a position")
        );
    }

    @ParameterizedTest
    @MethodSource("oneArgumentCommandsWithMissingArgument")
    void addNoProposition(final String answer, final String expectedMessage) {
        final boolean finished = QuestionPropositionStep.INSTANCE.handle(sender, answer, questionCreator);

        assertFalse(finished);
        Mockito.verify(sender).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).contains(expectedMessage)
        ));
    }

    @Test
    void unknownCommand() {
        final String answer = "foobar";

        final boolean finished = QuestionPropositionStep.INSTANCE.handle(sender, answer, questionCreator);

        assertFalse(finished);
        Mockito.verify(sender).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).contains("Answer foobar not recognized between add, set, del, list, answers or confirm")
        ));
    }

    @Test
    void shouldNotSkip() {
        assertFalse(QuestionPropositionStep.INSTANCE.shouldSkip(Mockito.mock(QuestionCreator.class)));
    }

    @Test
    void nextStepDefined() {
        assertNotNull(QuestionPropositionStep.INSTANCE.next(Mockito.mock(QuestionCreator.class)));
    }


}