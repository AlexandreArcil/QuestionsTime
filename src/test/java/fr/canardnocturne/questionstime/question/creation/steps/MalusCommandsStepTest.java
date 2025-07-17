package fr.canardnocturne.questionstime.question.creation.steps;

import fr.canardnocturne.questionstime.question.component.OutcomeCommand;
import fr.canardnocturne.questionstime.question.creation.QuestionCreator;
import fr.canardnocturne.questionstime.question.serializer.OutcomeCommandSerializer;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MalusCommandsStepTest {

    @Mock
    Audience sender;

    @Captor
    ArgumentCaptor<Component> message;

    QuestionCreator questionCreator;

    @BeforeEach
    void setUp() {
        questionCreator = new QuestionCreator();
        questionCreator.setQuestion("question");
        questionCreator.getAnswers().add("answer");
    }

    @Test
    void questionNotNull() {
        assertNotNull(MalusCommandsStep.INSTANCE.question());
    }

    @Test
    void addCommandMalus() {
        final String displayMessage = "Teleport to the Hell";
        final String command = "tp @loser 0 -64 0";
        assertFalse(MalusCommandsStep.INSTANCE.handle(sender, "add " + displayMessage + ";" + command, questionCreator));

        assertEquals(1, questionCreator.getCommandsMalus().size());
        final OutcomeCommand outcomeCommand = questionCreator.getCommandsMalus().getFirst();
        assertEquals(displayMessage, outcomeCommand.message());
        assertEquals(command, outcomeCommand.command());

        this.verifyPlayerReceiveMessage();
    }

    @Test
    void addInvalidSyntax() {
        assertFalse(MalusCommandsStep.INSTANCE.handle(sender, "add Teleport to the Hell", questionCreator));
        assertEquals(0, questionCreator.getCommandsMalus().size());
        this.verifyPlayerReceiveMessage();
    }

    @Test
    void addNotEnoughArguments() {
        assertFalse(MalusCommandsStep.INSTANCE.handle(sender, "add", questionCreator));
        assertEquals(0, questionCreator.getCommandsMalus().size());
        this.verifyPlayerReceiveMessage();
    }

    @Test
    void listCommandEmpty() {
        assertFalse(MalusCommandsStep.INSTANCE.handle(sender, "list", questionCreator));

        this.verifyPlayerReceiveMessages(Collections.singletonList("No command malus added yet."));
    }

    @Test
    void listCommands() {
        final String firstCmdMessage = "Teleport to the Hell";
        final String firstCmd = "tp @loser 0 -64 0";
        final String secondCmdMessage = "Teleport to the Heaven";
        final String secondCmd = "tp @loser 0 256 0";
        final OutcomeCommand firstOutcomeCommand = new OutcomeCommand(firstCmdMessage, firstCmd);
        final OutcomeCommand secondOutcomeCommand = new OutcomeCommand(secondCmdMessage, secondCmd);
        questionCreator.getCommandsMalus().add(firstOutcomeCommand);
        questionCreator.getCommandsMalus().add(secondOutcomeCommand);
        assertFalse(MalusCommandsStep.INSTANCE.handle(sender, "list", questionCreator));

        final String delFirstCmd = "/qtc del " + OutcomeCommandSerializer.serialize(firstOutcomeCommand);
        final String delSecondCmd = "/qtc del " + OutcomeCommandSerializer.serialize(secondOutcomeCommand);
        this.verifyPlayerReceiveMessages(List.of(firstCmdMessage, firstCmd, delFirstCmd, secondCmdMessage, secondCmd, delSecondCmd));
    }

    @Test
    void removeCommandMalus() {
        final OutcomeCommand outcomeCommand = new OutcomeCommand("Teleport to the Hell", "tp @loser 0 -64 0");
        questionCreator.getCommandsMalus().add(outcomeCommand);
        assertFalse(MalusCommandsStep.INSTANCE.handle(sender, "del " + OutcomeCommandSerializer.serialize(outcomeCommand), questionCreator));

        assertEquals(0, questionCreator.getCommandsMalus().size());
        this.verifyPlayerReceiveMessage();
    }

    @Test
    void removeCommandMalusNotFound() {
        final OutcomeCommand outcomeCommand = new OutcomeCommand("Teleport to the Hell", "tp @loser 0 -64 0");
        assertFalse(MalusCommandsStep.INSTANCE.handle(sender, "del " + OutcomeCommandSerializer.serialize(outcomeCommand), questionCreator));

        assertEquals(0, questionCreator.getCommandsMalus().size());
        this.verifyPlayerReceiveMessages(Collections.singletonList("No command malus"));
    }

    @Test
    void removeCommandMalusNotEnoughArguments() {
        assertFalse(MalusCommandsStep.INSTANCE.handle(sender, "del", questionCreator));
        this.verifyPlayerReceiveMessage();
    }

    @Test
    void unknownCommand() {
        assertFalse(MalusCommandsStep.INSTANCE.handle(sender, "unknown", questionCreator));
        this.verifyPlayerReceiveMessages(Collections.singletonList("Unknown command: unknown"));
    }

    @Test
    void confirmStep() {
        assertTrue(MalusCommandsStep.INSTANCE.handle(sender, "confirm", questionCreator));
    }

    @Test
    void nextStepNotNull() {
        assertNotNull(MalusCommandsStep.INSTANCE.next(questionCreator));
    }

    private void verifyPlayerReceiveMessage() {
        this.verifyPlayerReceiveMessages(Collections.emptyList());
    }

    private void verifyPlayerReceiveMessages(final List<String> expectedMessages) {
        Mockito.verify(sender).sendMessage(message.capture());
        assertNotNull(message.getValue());
        if(!expectedMessages.isEmpty()) {
            final String messageReceived = MiniMessage.miniMessage().serialize(message.getValue());
            for (final String expectedMessage : expectedMessages) {
                assertTrue(messageReceived.contains(expectedMessage), "Expected message '" + messageReceived + "' to contain '" + expectedMessage + "'");
            }
        }
    }

}