package fr.canardnocturne.questionstime.util;

import fr.canardnocturne.questionstime.question.serializer.ItemStackSerializer;
import io.leangen.geantyref.TypeToken;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.spongepowered.api.Game;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.Key;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.registry.Registry;
import org.spongepowered.api.registry.RegistryEntry;
import org.spongepowered.api.registry.RegistryType;
import org.spongepowered.api.registry.RegistryTypes;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;

public class ItemStackSerializerTest {

    MockedStatic<RegistryType> registryTypeMock;
    MockedStatic<ResourceKey> resKeyMock;
    MockedStatic<Key> keyMock;

    @BeforeEach
    void setUp() {
        registryTypeMock = Mockito.mockStatic(RegistryType.class);
        resKeyMock = Mockito.mockStatic(ResourceKey.class);
        keyMock = Mockito.mockStatic(Key.class);

        final RegistryType registryTypeMockReturn = Mockito.mock(RegistryType.class);
        registryTypeMock.when(() -> RegistryType.of(Mockito.any(), Mockito.any())).thenReturn(registryTypeMockReturn);
        final Key.Builder keyBuilderMock = Mockito.mock(Key.Builder.class);
        Mockito.when(keyBuilderMock.key(Mockito.any())).thenReturn(keyBuilderMock);
        Mockito.when(keyBuilderMock.elementType(Mockito.any(Class.class))).thenReturn(keyBuilderMock);
        Mockito.when(keyBuilderMock.elementType(Mockito.any(TypeToken.class))).thenReturn(keyBuilderMock);
        Mockito.when(keyBuilderMock.weightedCollectionElementType(Mockito.any(Class.class))).thenReturn(keyBuilderMock);
        Mockito.when(keyBuilderMock.listElementType(Mockito.any(Class.class))).thenReturn(keyBuilderMock);
        Mockito.when(keyBuilderMock.mapElementType(Mockito.any(Class.class), Mockito.any(Class.class))).thenReturn(keyBuilderMock);
        Mockito.when(keyBuilderMock.setElementType(Mockito.any(Class.class))).thenReturn(keyBuilderMock);
        Mockito.when(keyBuilderMock.mapElementType(Mockito.any(TypeToken.class), Mockito.any(TypeToken.class))).thenReturn(keyBuilderMock);
        Mockito.when(keyBuilderMock.build()).thenAnswer(invocation -> Mockito.mock(Key.class));
        keyMock.when(Key::builder).thenReturn(keyBuilderMock);
    }

    @AfterEach
    void tearDown() {
        registryTypeMock.close();
        resKeyMock.close();
        keyMock.close();
    }

    @MethodSource
    static Stream<Arguments> itemstacks() {
        return Stream.of(Arguments.of(1, null, null, "minecraft:sand"),
                Arguments.of(1, Component.text("test", NamedTextColor.YELLOW, TextDecoration.BOLD),
                        null, "minecraft:sand;;<bold><yellow>test"),
                Arguments.of(1, null, Arrays.asList(
                        Component.text("line1", NamedTextColor.RED, TextDecoration.ITALIC),
                        Component.text("line2", NamedTextColor.BLUE, TextDecoration.UNDERLINED)),
                        "minecraft:sand;;;<italic><red>line1</red></italic><br><underlined><blue>line2"),
                Arguments.of(1, Component.text("test", NamedTextColor.YELLOW, TextDecoration.BOLD),
                        Arrays.asList(Component.text("line1", NamedTextColor.RED, TextDecoration.ITALIC),
                        Component.text("line2", NamedTextColor.BLUE, TextDecoration.UNDERLINED)),
                        "minecraft:sand;;<bold><yellow>test;<italic><red>line1</red></italic><br><underlined><blue>line2"),
                Arguments.of(5, null, null, "minecraft:sand;5"),
                Arguments.of(5, Component.text("test", NamedTextColor.YELLOW, TextDecoration.BOLD),
                        null, "minecraft:sand;5;<bold><yellow>test"),
                Arguments.of(5, null, Arrays.asList(
                        Component.text("line1", NamedTextColor.RED, TextDecoration.ITALIC),
                        Component.text("line2", NamedTextColor.BLUE, TextDecoration.UNDERLINED)),
                        "minecraft:sand;5;;<italic><red>line1</red></italic><br><underlined><blue>line2"),
                Arguments.of(5, Component.text("test", NamedTextColor.YELLOW, TextDecoration.BOLD),
                        Arrays.asList(Component.text("line1", NamedTextColor.RED, TextDecoration.ITALIC),
                        Component.text("line2", NamedTextColor.BLUE, TextDecoration.UNDERLINED)),
                        "minecraft:sand;5;<bold><yellow>test;<italic><red>line1</red></italic><br><underlined><blue>line2")
        );
    }

    @ParameterizedTest
    @MethodSource("itemstacks")
    public void serializeCompleteItemStackToString(final int quantity, final Component customName, final List<Component> lore, final String expected) {
        final ResourceKey resourceKeyMock = Mockito.mock(ResourceKey.class);
        final ItemType it = Mockito.mock(ItemType.class);
        Mockito.when(resourceKeyMock.asString()).thenReturn("minecraft:sand");
        Mockito.when(it.key(RegistryTypes.ITEM_TYPE)).thenReturn(resourceKeyMock);
        final ItemStack is = Mockito.mock(ItemStack.class);
        Mockito.when(is.type()).thenReturn(it);
        Mockito.when(is.quantity()).thenReturn(quantity);
        Mockito.when(is.get(any())).thenReturn(Optional.ofNullable(customName), Optional.ofNullable(lore));

        final String isStr = ItemStackSerializer.fromItemStack(is);

        assertEquals(expected, isStr);
    }

    @Test
    public void serializeStringToItemStack() {
        try(final MockedStatic<Sponge> spongeMock = Mockito.mockStatic(Sponge.class);
            final MockedStatic<ItemStack> itemStackMock = Mockito.mockStatic(ItemStack.class)) {
            final ResourceKey resourceKeyMockReturn = Mockito.mock(ResourceKey.class);
            resKeyMock.when(() -> ResourceKey.of(anyString(), anyString())).thenReturn(resourceKeyMockReturn);
            final Registry registryMock = Mockito.mock(Registry.class);
            final Game gameMock = Mockito.mock(Game.class);
            final RegistryEntry registryEntryMock = Mockito.mock(RegistryEntry.class);
            final ItemType itemTypeMock = Mockito.mock(ItemType.class);
            Mockito.when(registryEntryMock.value()).thenReturn(itemTypeMock);
            Mockito.when(registryMock.findEntry(any(ResourceKey.class))).thenReturn(Optional.of(registryEntryMock));
            Mockito.when(gameMock.registry(RegistryTypes.ITEM_TYPE)).thenReturn(registryMock);
            spongeMock.when(Sponge::game).thenReturn(gameMock);

            final ItemStack.Builder itemStackBuilderMock = Mockito.mock(ItemStack.Builder.class);
            Mockito.when(itemStackBuilderMock.itemType(any(ItemType.class))).thenReturn(itemStackBuilderMock);
            Mockito.when(itemStackBuilderMock.quantity(anyInt())).thenReturn(itemStackBuilderMock);
            Mockito.when(itemStackBuilderMock.add(any(Key.class), any(Component.class))).thenReturn(itemStackBuilderMock);
            Mockito.when(itemStackBuilderMock.add(any(Key.class), anyList())).thenReturn(itemStackBuilderMock);
            final ItemStack itemStackMockReturn = Mockito.mock(ItemStack.class);
            Mockito.when(itemStackBuilderMock.build()).thenReturn(itemStackMockReturn);
            itemStackMock.when(ItemStack::builder).thenReturn(itemStackBuilderMock);

            final String isStr = "sand;5;<yellow><bold>Name;<blue>line1</blue><br>line2";
            final ItemStack is = ItemStackSerializer.fromString(isStr);

            assertEquals(itemStackMockReturn, is);
            Mockito.verify(itemStackBuilderMock).itemType(itemTypeMock);
            Mockito.verify(itemStackBuilderMock).quantity(5);
            Mockito.verify(itemStackBuilderMock).add(Keys.CUSTOM_NAME, Component.text("Name", NamedTextColor.YELLOW, TextDecoration.BOLD));
            Mockito.verify(itemStackBuilderMock).add(Keys.LORE, List.of(
                    Component.text("line1", NamedTextColor.BLUE),
                    Component.text("line2")
            ));
        }
    }

    @Test
    void serializeStringToModItemStack() {
        try(final MockedStatic<Sponge> spongeMock = Mockito.mockStatic(Sponge.class);
            final MockedStatic<ItemStack> itemStackMock = Mockito.mockStatic(ItemStack.class)) {
            final ResourceKey resourceKeyMockReturn = Mockito.mock(ResourceKey.class);
            resKeyMock.when(() -> ResourceKey.of(anyString(), anyString())).thenReturn(resourceKeyMockReturn);
            final Registry registryMock = Mockito.mock(Registry.class);
            final Game gameMock = Mockito.mock(Game.class);
            final RegistryEntry registryEntryMock = Mockito.mock(RegistryEntry.class);
            final ItemType itemTypeMock = Mockito.mock(ItemType.class);
            Mockito.when(registryEntryMock.value()).thenReturn(itemTypeMock);
            Mockito.when(registryMock.findEntry(any(ResourceKey.class))).thenReturn(Optional.of(registryEntryMock));
            Mockito.when(gameMock.registry(RegistryTypes.ITEM_TYPE)).thenReturn(registryMock);
            spongeMock.when(Sponge::game).thenReturn(gameMock);

            final ItemStack.Builder itemStackBuilderMock = Mockito.mock(ItemStack.Builder.class);
            Mockito.when(itemStackBuilderMock.itemType(any(ItemType.class))).thenReturn(itemStackBuilderMock);
            Mockito.when(itemStackBuilderMock.quantity(anyInt())).thenReturn(itemStackBuilderMock);
            Mockito.when(itemStackBuilderMock.add(any(Key.class), any(Component.class))).thenReturn(itemStackBuilderMock);
            Mockito.when(itemStackBuilderMock.add(any(Key.class), anyList())).thenReturn(itemStackBuilderMock);
            final ItemStack itemStackMockReturn = Mockito.mock(ItemStack.class);
            Mockito.when(itemStackBuilderMock.build()).thenReturn(itemStackMockReturn);
            itemStackMock.when(ItemStack::builder).thenReturn(itemStackBuilderMock);

            final String isStr = "modid:custom_item";
            final ItemStack is = ItemStackSerializer.fromString(isStr);

            assertEquals(itemStackMockReturn, is);
            Mockito.verify(itemStackBuilderMock).itemType(itemTypeMock);
            Mockito.verify(itemStackBuilderMock).quantity(1);
            Mockito.verify(itemStackBuilderMock, Mockito.never()).add(Mockito.any(Key.class), Mockito.any(Component.class));
            Mockito.verify(itemStackBuilderMock, Mockito.never()).add(Mockito.any(Key.class), Mockito.anyList());
        }
    }

    @Test
    void serializeStringToUndefinedItemStack() {
        try(final MockedStatic<Sponge> spongeMock = Mockito.mockStatic(Sponge.class)) {
            final ResourceKey resourceKeyMockReturn = Mockito.mock(ResourceKey.class);
            resKeyMock.when(() -> ResourceKey.of(anyString(), anyString())).thenReturn(resourceKeyMockReturn);
            final Registry registryMock = Mockito.mock(Registry.class);
            Mockito.when(registryMock.findEntry(Mockito.any(ResourceKey.class))).thenReturn(Optional.empty());
            final Game gameMock = Mockito.mock(Game.class);
            Mockito.when(gameMock.registry(RegistryTypes.ITEM_TYPE)).thenReturn(registryMock);
            spongeMock.when(Sponge::game).thenReturn(gameMock);

            final String isStr = "modid:custom_item";
            Assertions.assertThrows(IllegalArgumentException.class, () -> ItemStackSerializer.fromString(isStr));
        }
    }

    @Test
    void serializeStringToNegativeCountItemStack() {
        try(final MockedStatic<Sponge> spongeMock = Mockito.mockStatic(Sponge.class)) {
            final ResourceKey resourceKeyMockReturn = Mockito.mock(ResourceKey.class);
            resKeyMock.when(() -> ResourceKey.of(anyString(), anyString())).thenReturn(resourceKeyMockReturn);
            final Registry registryMock = Mockito.mock(Registry.class);
            final RegistryEntry registryEntryMock = Mockito.mock(RegistryEntry.class);
            final ItemType itemTypeMock = Mockito.mock(ItemType.class);
            Mockito.when(registryEntryMock.value()).thenReturn(itemTypeMock);
            Mockito.when(registryMock.findEntry(any(ResourceKey.class))).thenReturn(Optional.of(registryEntryMock));
            final Game gameMock = Mockito.mock(Game.class);
            Mockito.when(gameMock.registry(RegistryTypes.ITEM_TYPE)).thenReturn(registryMock);
            spongeMock.when(Sponge::game).thenReturn(gameMock);

            final String isStr = "minecraft:sand;-5";
            final IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> ItemStackSerializer.fromString(isStr));
            assertEquals("The item count isn't an number or is negative: '-5'", exception.getMessage());
        }
    }

    @Test
    void serializeStringToNonNumericCountItemStack() {
        try(final MockedStatic<Sponge> spongeMock = Mockito.mockStatic(Sponge.class)) {
            final ResourceKey resourceKeyMockReturn = Mockito.mock(ResourceKey.class);
            resKeyMock.when(() -> ResourceKey.of(anyString(), anyString())).thenReturn(resourceKeyMockReturn);
            final Registry registryMock = Mockito.mock(Registry.class);
            final RegistryEntry registryEntryMock = Mockito.mock(RegistryEntry.class);
            final ItemType itemTypeMock = Mockito.mock(ItemType.class);
            Mockito.when(registryEntryMock.value()).thenReturn(itemTypeMock);
            Mockito.when(registryMock.findEntry(any(ResourceKey.class))).thenReturn(Optional.of(registryEntryMock));
            final Game gameMock = Mockito.mock(Game.class);
            Mockito.when(gameMock.registry(RegistryTypes.ITEM_TYPE)).thenReturn(registryMock);
            spongeMock.when(Sponge::game).thenReturn(gameMock);

            final String isStr = "minecraft:sand;five";
            final IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> ItemStackSerializer.fromString(isStr));
            assertEquals("The item count isn't an number or is negative: 'five'", exception.getMessage());
        }
    }

}
