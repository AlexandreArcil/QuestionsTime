package fr.canardnocturne.questionstime.question.serializer;

import fr.canardnocturne.questionstime.question.component.OutcomeCommand;
import fr.canardnocturne.questionstime.question.component.Prize;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PrizeTypeSerializerTest {

    @Test
    void deserializeWithValidData() throws SerializationException {
        try(final MockedStatic<ItemStackSerializer> itemStackSerializerMock = Mockito.mockStatic(ItemStackSerializer.class)) {
            final ItemStack is1 = Mockito.mock(ItemStack.class);
            final ItemStack is2 = Mockito.mock(ItemStack.class);
            final Set<ItemStack> itemStacks = Set.of(is1, is2);
            itemStackSerializerMock.when(() -> ItemStackSerializer.fromString("sand")).thenReturn(is1);
            itemStackSerializerMock.when(() -> ItemStackSerializer.fromString("stone")).thenReturn(is2);
            final Set<OutcomeCommand> outcomeCommands = Set.of(new OutcomeCommand("testCommand", "testArgs"),
                    new OutcomeCommand("anotherCommand", "anotherArgs"));
            final ConfigurationNode root = Mockito.mock(ConfigurationNode.class);
            final ConfigurationNode node = Mockito.mock(ConfigurationNode.class);
            Mockito.when(root.node(Mockito.anyString())).thenReturn(node);
            Mockito.when(node.getBoolean(Mockito.anyBoolean())).thenReturn(true);
            Mockito.when(node.getInt(Mockito.anyInt())).thenReturn(100, 1);
            Mockito.when(node.getList(Mockito.eq(OutcomeCommand.class), Mockito.anyList())).thenReturn(new ArrayList<>(outcomeCommands));
            Mockito.when(node.getString()).thenReturn("sand", "stone");
            Mockito.<List<? extends ConfigurationNode>>when(node.childrenList()).thenReturn(List.of(node, node));

            final PrizeTypeSerializer prizeTypeSerializer = new PrizeTypeSerializer();
            final Prize prize = prizeTypeSerializer.deserialize(Prize.class, root);
            assertNotNull(prize);
            assertEquals(100, prize.getMoney());
            assertTrue(prize.isAnnounce());
            assertEquals(outcomeCommands, prize.getCommands());
            assertEquals(itemStacks, prize.getItemStacks());
            assertEquals(1, prize.getPosition());
        }
    }

    @Test
    void deserializeWithInvalidPosition() {
        final ConfigurationNode root = Mockito.mock(ConfigurationNode.class);
        final ConfigurationNode node = Mockito.mock(ConfigurationNode.class);
        Mockito.when(root.node(Mockito.anyString())).thenReturn(node);
        Mockito.when(node.getInt(Mockito.anyInt())).thenReturn(50, -1);
        Mockito.when(node.getBoolean(Mockito.anyBoolean())).thenReturn(false);

        final PrizeTypeSerializer prizeTypeSerializer = new PrizeTypeSerializer();

        assertThrows(SerializationException.class, () -> prizeTypeSerializer.deserialize(Prize.class, root));
    }

    @Test
    void deserializeWithNullItemStack() {
        final ConfigurationNode root = Mockito.mock(ConfigurationNode.class);
        final ConfigurationNode node = Mockito.mock(ConfigurationNode.class);
        Mockito.when(root.node(Mockito.anyString())).thenReturn(node);
        Mockito.when(node.getInt(Mockito.anyInt())).thenReturn(100, 1);
        Mockito.when(node.getBoolean(Mockito.anyBoolean())).thenReturn(true);
        Mockito.when(node.getString()).thenReturn(null);
        Mockito.<List<? extends ConfigurationNode>>when(node.childrenList()).thenReturn(List.of(node, node));

        final PrizeTypeSerializer prizeTypeSerializer = new PrizeTypeSerializer();
        assertThrows(SerializationException.class, () -> prizeTypeSerializer.deserialize(Prize.class, root));
    }

    @Test
    void deserializeWithIncorrectItemStackSyntax() {
        try(final MockedStatic<ItemStackSerializer> itemStackSerializerMock = Mockito.mockStatic(ItemStackSerializer.class)) {
            itemStackSerializerMock.when(() -> ItemStackSerializer.fromString(Mockito.anyString())).thenThrow(new IllegalArgumentException("Invalid item syntax"));
            final ConfigurationNode root = Mockito.mock(ConfigurationNode.class);
            final ConfigurationNode node = Mockito.mock(ConfigurationNode.class);
            Mockito.when(root.node(Mockito.anyString())).thenReturn(node);
            Mockito.when(node.getInt(Mockito.anyInt())).thenReturn(100, 1);
            Mockito.when(node.getBoolean(Mockito.anyBoolean())).thenReturn(true);
            Mockito.when(node.getString()).thenReturn("invalidItem");
            Mockito.<List<? extends ConfigurationNode>>when(node.childrenList()).thenReturn(List.of(node, node));

            final PrizeTypeSerializer prizeTypeSerializer = new PrizeTypeSerializer();
            assertThrows(SerializationException.class, () -> prizeTypeSerializer.deserialize(Prize.class, root));
        }
    }

    @Test
    void serializeWithValidPrize() throws SerializationException {
        try(final MockedStatic<ItemStackSerializer> itemStackSerializerMock = Mockito.mockStatic(ItemStackSerializer.class)) {
            final ItemStack itemStack = Mockito.mock(ItemStack.class);
            itemStackSerializerMock.when(() -> ItemStackSerializer.fromItemStack(itemStack)).thenReturn("sand");
            final OutcomeCommand command = new OutcomeCommand("testCommand", "testArgs");
            final Prize prize = new Prize(100, true, Set.of(itemStack), Set.of(command), 1);
            final ConfigurationNode root = Mockito.mock(ConfigurationNode.class);
            final ConfigurationNode node = Mockito.mock(ConfigurationNode.class);
            Mockito.when(root.node(Mockito.anyString())).thenReturn(node);

            final PrizeTypeSerializer prizeTypeSerializer = new PrizeTypeSerializer();
            prizeTypeSerializer.serialize(Prize.class, prize, root);

            Mockito.verify(root).node("announce");
            Mockito.verify(node).set(true);
            Mockito.verify(root).node("money");
            Mockito.verify(node).set(100);
            Mockito.verify(root).node("position");
            Mockito.verify(node).set(1);
            Mockito.verify(root).node("items");
            Mockito.verify(node).set(List.of("sand", "sand"));
            Mockito.verify(root).node("commands");
            Mockito.verify(node).setList(OutcomeCommand.class, List.of(command, command));
        }
    }

    @Test
    void serializeWithEmptyPrize() throws SerializationException {
        final Prize prize = new Prize(0, false, new HashSet<>(), new HashSet<>(), 1);
        final ConfigurationNode root = Mockito.mock(ConfigurationNode.class);
        final ConfigurationNode node = Mockito.mock(ConfigurationNode.class);
        Mockito.when(root.node(Mockito.anyString())).thenReturn(node);

        final PrizeTypeSerializer prizeTypeSerializer = new PrizeTypeSerializer();
        prizeTypeSerializer.serialize(Prize.class, prize, root);

        Mockito.verifyNoInteractions(root);
    }

    @Test
    void serializeWithOnlyMoneyPrize() throws SerializationException {
        final Prize prize = new Prize(50, false, new HashSet<>(), new HashSet<>(), 1);
        final ConfigurationNode root = Mockito.mock(ConfigurationNode.class);
        final ConfigurationNode node = Mockito.mock(ConfigurationNode.class);
        Mockito.when(root.node(Mockito.anyString())).thenReturn(node);

        final PrizeTypeSerializer prizeTypeSerializer = new PrizeTypeSerializer();
        prizeTypeSerializer.serialize(Prize.class, prize, root);

        Mockito.verify(root).node("money");
        Mockito.verify(node).set(50);
    }

}