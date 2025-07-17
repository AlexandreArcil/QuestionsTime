package fr.canardnocturne.questionstime.question.serializer;

import fr.canardnocturne.questionstime.question.component.OutcomeCommand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OutcomeCommandSerializerTest {

    @Test
    void deserializeValidCommand() {
        final String input = "Test message;/testCommand";
        final OutcomeCommand outcomeCommand = OutcomeCommandSerializer.deserialize(input);
        assertEquals("Test message", outcomeCommand.message());
        assertEquals("testCommand", outcomeCommand.command());
    }

    @Test
    void deserializeValidCommandWithoutSlash() {
        final String input = "Test message;testCommand";
        final OutcomeCommand outcomeCommand = OutcomeCommandSerializer.deserialize(input);
        assertEquals("Test message", outcomeCommand.message());
        assertEquals("testCommand", outcomeCommand.command());
    }

    @Test
    void deserializeInvalidCommand() {
        final String input = "Invalid command";
        final Exception exception = assertThrows(IllegalArgumentException.class, () -> OutcomeCommandSerializer.deserialize(input));
        assertEquals("The outcome command 'Invalid command' doesn't have a message or a command", exception.getMessage());
    }

    @Test
    void serializeValidCommand() {
        final OutcomeCommand outcomeCommand = new OutcomeCommand("Test message", "testCommand");
        final String serialized = OutcomeCommandSerializer.serialize(outcomeCommand);
        assertEquals("Test message;testCommand", serialized);
    }

}