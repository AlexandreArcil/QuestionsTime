package fr.canardnocturne.questionstime.config.serializer;

import fr.canardnocturne.questionstime.config.QuestionTimeConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mockito;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.lang.reflect.Type;

class ModeTypeSerializerTest {

    @ParameterizedTest
    @EnumSource
    void serializeSuccess(final QuestionTimeConfiguration.Mode mode) throws SerializationException {
        final ModeTypeSerializer serializer = new ModeTypeSerializer();
        final ConfigurationNode node = Mockito.mock(ConfigurationNode.class);

        serializer.serialize((Type) null, mode, node);

        Mockito.verify(node).set(mode.toString().toLowerCase());
    }

    @Test
    void serializeNullInput() throws SerializationException {
        final ModeTypeSerializer serializer = new ModeTypeSerializer();
        final ConfigurationNode node = Mockito.mock(ConfigurationNode.class);

        serializer.serialize((Type) null, null, node);

        Mockito.verifyNoInteractions(node);
    }

    @ParameterizedTest
    @EnumSource
    void deserializeSuccess(final QuestionTimeConfiguration.Mode mode) throws SerializationException {
        final ModeTypeSerializer serializer = new ModeTypeSerializer();
        final ConfigurationNode node = Mockito.mock(ConfigurationNode.class);
        Mockito.when(node.getString(Mockito.anyString())).thenReturn(mode.toString().toLowerCase());

        final QuestionTimeConfiguration.Mode deserializedMode = serializer.deserialize((Type) null, node);

        Assertions.assertEquals(mode, deserializedMode);
    }

    @Test
    void deserializeException() {
        final ModeTypeSerializer serializer = new ModeTypeSerializer();
        final ConfigurationNode node = Mockito.mock(ConfigurationNode.class);
        Mockito.when(node.getString(Mockito.anyString())).thenReturn("coin");

        Assertions.assertThrows(SerializationException.class, () -> serializer.deserialize((Type) null, node));
    }

}