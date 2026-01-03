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
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class PrizeCommandsStepTest {

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
    void questionNonNull() {
        assertNotNull(PrizeCommandsStep.INSTANCE.question());
    }

    @Test
    void handleAddCommand() {
        final String message = "message";
        final String command = "command";
        assertFalse(PrizeCommandsStep.INSTANCE.handle(sender, "add " + message + ";" + command, questionCreator));
        assertEquals(1, questionCreator.getCommandsPrize().size());
        assertEquals(1, questionCreator.getCommandsPrize().get(1).size());
        assertEquals(message, questionCreator.getCommandsPrize().get(1).getFirst().message());
        assertEquals(command, questionCreator.getCommandsPrize().get(1).getFirst().command());
        this.verifyPlayerReceiveMessage();
    }

    @Test
    void handleAddCommandWithPosition() {
        final String message = "message";
        final String command = "command";
        final int position = 2;
        assertFalse(PrizeCommandsStep.INSTANCE.handle(sender, "add " + message + ";" + command + ";" + position, questionCreator));
        assertEquals(1, questionCreator.getCommandsPrize().size());
        assertTrue(questionCreator.getCommandsPrize().containsKey(position));
        final List<OutcomeCommand> outcomeCommands = questionCreator.getCommandsPrize().get(position);
        assertEquals(1, outcomeCommands.size());
        assertEquals(message, outcomeCommands.getFirst().message());
        assertEquals(command, outcomeCommands.getFirst().command());
        this.verifyPlayerReceiveMessage();
    }

    @Test
    void handleAddCommandWithInvalidPosition() {
        final String message = "message";
        final String command = "command";
        final int position = 0;
        assertFalse(PrizeCommandsStep.INSTANCE.handle(sender, "add " + message + ";" + command + ";" + position, questionCreator));
        assertEquals(0, questionCreator.getCommandsPrize().size());
        this.verifyPlayerReceiveMessages(Collections.singletonList("is not a positive number"));
    }

    @Test
    void handleAddCommandWithInvalidFormat() {
        final String invalidCommand = "invalid command format";
        assertFalse(PrizeCommandsStep.INSTANCE.handle(sender, "add " + invalidCommand, questionCreator));
        assertEquals(0, questionCreator.getCommandsPrize().size());
        this.verifyPlayerReceiveMessages(Collections.singletonList("doesn't follow the syntax"));
    }

    @Test
    void handleAddCommandNoCommand() {
        assertFalse(PrizeCommandsStep.INSTANCE.handle(sender, "add", questionCreator));
        assertEquals(0, questionCreator.getCommandsPrize().size());
        this.verifyPlayerReceiveMessages(Collections.singletonList("You must provide a command to add"));
    }

    @Test
    void handleListCommandsEmpty() {
        assertFalse(PrizeCommandsStep.INSTANCE.handle(sender, "list", questionCreator));
        this.verifyPlayerReceiveMessages(Collections.singletonList("No command prizes added yet."));
    }

    @Test
    void handleListCommandsWithPrizes() {
        final OutcomeCommand outcomeCommand1 = new OutcomeCommand("message1", "command1");
        final OutcomeCommand outcomeCommand2 = new OutcomeCommand("message2", "command2");
        final OutcomeCommand outcomeCommand3 = new OutcomeCommand("message3", "command3");
        questionCreator.addCommandPrize(1, outcomeCommand1);
        questionCreator.addCommandPrize(1, outcomeCommand2);
        questionCreator.addCommandPrize(2, outcomeCommand3);
        assertFalse(PrizeCommandsStep.INSTANCE.handle(sender, "list", questionCreator));

        final String delFirstCmd = "/qtc del " + OutcomeCommandSerializer.serialize(outcomeCommand1);
        final String delSecondCmd = "/qtc del " + OutcomeCommandSerializer.serialize(outcomeCommand2);
        final String delThirdCmd = "/qtc del " + OutcomeCommandSerializer.serialize(outcomeCommand3);
        this.verifyPlayerReceiveMessages(List.of(outcomeCommand1.message(), outcomeCommand1.command(), delFirstCmd,
                outcomeCommand2.message(), outcomeCommand2.command(), delSecondCmd,
                outcomeCommand3.message(), outcomeCommand3.command(), delThirdCmd));
    }

    @Test
    void handleRemoveCommand() {
        final OutcomeCommand outcomeCommand = new OutcomeCommand("message", "command");
        questionCreator.addCommandPrize(1, outcomeCommand);
        assertFalse(PrizeCommandsStep.INSTANCE.handle(sender, "del " + OutcomeCommandSerializer.serialize(outcomeCommand), questionCreator));
        assertEquals(0, questionCreator.getCommandsPrize().size());
        this.verifyPlayerReceiveMessage();
    }

    @Test
    void handleRemoveCommandNotFound() {
        final OutcomeCommand outcomeCommand = new OutcomeCommand("message", "command");
        questionCreator.addCommandPrize(1, outcomeCommand);
        assertFalse(PrizeCommandsStep.INSTANCE.handle(sender, "del " + OutcomeCommandSerializer.serialize(new OutcomeCommand("otherMessage", "otherCommand")), questionCreator));
        assertEquals(1, questionCreator.getCommandsPrize().get(1).size());
        this.verifyPlayerReceiveMessages(Collections.singletonList("No command prize"));
    }

    @Test
    void handleRemoveCommandWithPosition() {
        final OutcomeCommand outcomeCommand = new OutcomeCommand("message", "command");
        final int position = 2;
        questionCreator.addCommandPrize(position, outcomeCommand);
        questionCreator.addCommandPrize(1, new OutcomeCommand("otherMessage", "otherCommand"));
        assertFalse(PrizeCommandsStep.INSTANCE.handle(sender, "del " + OutcomeCommandSerializer.serialize(outcomeCommand) + ";" + position, questionCreator));
        final Map<Integer, List<OutcomeCommand>> commandsPrize = questionCreator.getCommandsPrize();
        assertEquals(1, commandsPrize.size());
        assertFalse(commandsPrize.containsKey(position));
        assertEquals(1, commandsPrize.get(1).size());
        this.verifyPlayerReceiveMessage();
    }

    @Test
    void handleRemoveCommandWithInvalidPosition() {
        final OutcomeCommand outcomeCommand = new OutcomeCommand("message", "command");
        final int position = 0;
        questionCreator.addCommandPrize(1, outcomeCommand);
        assertFalse(PrizeCommandsStep.INSTANCE.handle(sender, "del " + OutcomeCommandSerializer.serialize(outcomeCommand) + ";" + position, questionCreator));
        assertEquals(1, questionCreator.getCommandsPrize().get(1).size());
        this.verifyPlayerReceiveMessages(Collections.singletonList("is not a positive number"));
    }

    @Test
    void handleRemoveCommandInvalidSyntax() {
        final OutcomeCommand outcomeCommand = new OutcomeCommand("message", "command");
        questionCreator.addCommandPrize(1, outcomeCommand);
        assertFalse(PrizeCommandsStep.INSTANCE.handle(sender, "del invalid command format", questionCreator));
        assertEquals(1, questionCreator.getCommandsPrize().get(1).size());
        this.verifyPlayerReceiveMessages(Collections.singletonList("doesn't follow the syntax"));
    }

    @Test
    void handleRemoveCommandNoCommand() {
        final OutcomeCommand outcomeCommand = new OutcomeCommand("message", "command");
        questionCreator.addCommandPrize(1, outcomeCommand);
        assertFalse(PrizeCommandsStep.INSTANCE.handle(sender, "del", questionCreator));
        assertEquals(1, questionCreator.getCommandsPrize().get(1).size());
        this.verifyPlayerReceiveMessages(Collections.singletonList("You must provide a command to remove"));
    }

    @Test
    void handleUnknownCommand() {
        assertFalse(PrizeCommandsStep.INSTANCE.handle(sender, "unknown", questionCreator));
        this.verifyPlayerReceiveMessages(Collections.singletonList("Unknown command: unknown"));
    }

    @Test
    void handleConfirm() {
        assertTrue(PrizeCommandsStep.INSTANCE.handle(sender, "confirm", questionCreator));
    }

    @Test
    void next() {
        assertNotNull(PrizeCommandsStep.INSTANCE.next(questionCreator));
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