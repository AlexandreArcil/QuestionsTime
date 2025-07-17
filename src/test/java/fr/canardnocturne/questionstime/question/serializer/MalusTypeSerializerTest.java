package fr.canardnocturne.questionstime.question.serializer;

import fr.canardnocturne.questionstime.question.component.Malus;
import fr.canardnocturne.questionstime.question.component.OutcomeCommand;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MalusTypeSerializerTest {

    @Test
    void deserialize() throws SerializationException {
        final List<OutcomeCommand> outcomeCommands = List.of(new OutcomeCommand("testCommand", "testArgs"),
                new OutcomeCommand("anotherCommand", "anotherArgs"));
        final ConfigurationNode root = Mockito.mock(ConfigurationNode.class);
        final ConfigurationNode node = Mockito.mock(ConfigurationNode.class);
        Mockito.when(root.node(Mockito.anyString())).thenReturn(node);
        Mockito.when(node.getBoolean(Mockito.anyBoolean())).thenReturn(true);
        Mockito.when(node.getInt(Mockito.anyInt())).thenReturn(100);
        Mockito.when(node.getList(Mockito.eq(OutcomeCommand.class), Mockito.anyList())).thenReturn(outcomeCommands);
        final MalusTypeSerializer serializer = new MalusTypeSerializer();
        final Malus serialized = serializer.deserialize(Malus.class, root);
        assertNotNull(serialized);
        assertTrue(serialized.isAnnounce());
        assertEquals(100, serialized.getMoney());
        assertArrayEquals(outcomeCommands.toArray(new OutcomeCommand[0]), serialized.getCommands());
    }

    @Test
    void serialize() throws SerializationException {
        final Malus malus = new Malus(100, true, new OutcomeCommand[]{
                new OutcomeCommand("testCommand", "testArgs"),
                new OutcomeCommand("anotherCommand", "anotherArgs")});
        final ConfigurationNode root = Mockito.mock(ConfigurationNode.class);
        final ConfigurationNode node = Mockito.mock(ConfigurationNode.class);
        Mockito.when(root.node(Mockito.anyString())).thenReturn(node);

        final MalusTypeSerializer serializer = new MalusTypeSerializer();
        serializer.serialize(Malus.class, malus, root);

        Mockito.verify(root).node("announce");
        Mockito.verify(node).set(true);
        Mockito.verify(root).node("money");
        Mockito.verify(node).set(100);
        Mockito.verify(root).node("commands");
        Mockito.verify(node).setList(OutcomeCommand.class, Arrays.asList(malus.getCommands()));
    }

    @Test
    void notSerialized() throws SerializationException {
        final Malus malus = new Malus(0, false, new OutcomeCommand[]{});
        final ConfigurationNode root = Mockito.mock(ConfigurationNode.class);

        final MalusTypeSerializer serializer = new MalusTypeSerializer();
        serializer.serialize(Malus.class, null, root);
        serializer.serialize(Malus.class, malus, root);

        Mockito.verifyNoInteractions(root);
    }

}