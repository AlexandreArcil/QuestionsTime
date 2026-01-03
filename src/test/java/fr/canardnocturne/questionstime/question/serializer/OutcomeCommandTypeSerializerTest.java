package fr.canardnocturne.questionstime.question.serializer;

import fr.canardnocturne.questionstime.question.component.OutcomeCommand;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OutcomeCommandTypeSerializerTest {

    @Test
    void deserializeWithValidCommand() throws SerializationException {
        try(final MockedStatic<OutcomeCommandSerializer> outcomeCommandSerializerMock = Mockito.mockStatic(OutcomeCommandSerializer.class)) {
            outcomeCommandSerializerMock.when(() -> OutcomeCommandSerializer.deserialize(Mockito.anyString()))
                    .thenReturn(new OutcomeCommand("message", "command"));
            final ConfigurationNode node = Mockito.mock(ConfigurationNode.class);
            Mockito.when(node.getString()).thenReturn("message;command");
            final OutcomeCommandTypeSerializer serializer = new OutcomeCommandTypeSerializer();
            final OutcomeCommand outcomeCommand = serializer.deserialize(OutcomeCommand.class, node);
            assertNotNull(outcomeCommand);
        }
    }

    @Test
    void deserializeWithInvalidCommand() {
        try(final MockedStatic<OutcomeCommandSerializer> outcomeCommandSerializerMock = Mockito.mockStatic(OutcomeCommandSerializer.class)) {
            outcomeCommandSerializerMock.when(() -> OutcomeCommandSerializer.deserialize(Mockito.anyString()))
                    .thenThrow(new IllegalArgumentException("Invalid command format"));
            final ConfigurationNode node = Mockito.mock(ConfigurationNode.class);
            Mockito.when(node.getString()).thenReturn("invalid;command");
            final OutcomeCommandTypeSerializer serializer = new OutcomeCommandTypeSerializer();
            assertThrows(SerializationException.class, () -> serializer.deserialize(OutcomeCommand.class, node));
        }
    }

    @Test
    void deserializeWithNullCommand() {
        final ConfigurationNode node = Mockito.mock(ConfigurationNode.class);
        Mockito.when(node.getString()).thenReturn(null);
        final OutcomeCommandTypeSerializer serializer = new OutcomeCommandTypeSerializer();
        assertThrows(SerializationException.class, () -> serializer.deserialize(OutcomeCommand.class, node));
    }

    @Test
    void serializeWithValidOutcomeCommand() throws SerializationException {
        try(final MockedStatic<OutcomeCommandSerializer> outcomeCommandSerializerMock = Mockito.mockStatic(OutcomeCommandSerializer.class)) {
            final String input = "message;command";
            final OutcomeCommand outcomeCommand = new OutcomeCommand("message", "command");
            outcomeCommandSerializerMock.when(() -> OutcomeCommandSerializer.serialize(outcomeCommand))
                    .thenReturn(input);
            final ConfigurationNode node = Mockito.mock(ConfigurationNode.class);
            final OutcomeCommandTypeSerializer serializer = new OutcomeCommandTypeSerializer();
            serializer.serialize(OutcomeCommand.class, outcomeCommand, node);
            Mockito.verify(node).set(input);
        }
    }

    @Test
    void serializeWithNullOutcomeCommand() throws SerializationException {
        final ConfigurationNode node = Mockito.mock(ConfigurationNode.class);
        final OutcomeCommandTypeSerializer serializer = new OutcomeCommandTypeSerializer();
        serializer.serialize(OutcomeCommand.class, null, node);
        Mockito.verifyNoInteractions(node);
    }

}