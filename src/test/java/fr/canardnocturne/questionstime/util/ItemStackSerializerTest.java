package fr.canardnocturne.questionstime.util;

import fr.canardnocturne.questionstime.question.serializer.ItemStackSerializer;
import io.leangen.geantyref.TypeToken;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;

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
        final ResourceKey resourceKeyMock = Mockito.mock(ResourceKey.class);
        resKeyMock.when(() -> ResourceKey.minecraft(Mockito.anyString())).thenReturn(resourceKeyMock);
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

    @Test
    public void serializeItemStackToString() {
        final ResourceKey resourceKeyMock = Mockito.mock(ResourceKey.class);
        final ItemType it = Mockito.mock(ItemType.class);
        Mockito.when(resourceKeyMock.asString()).thenReturn("minecraft:sand");
        Mockito.when(it.key(RegistryTypes.ITEM_TYPE)).thenReturn(resourceKeyMock);
        final ItemStack is = Mockito.mock(ItemStack.class);
        Mockito.when(is.type()).thenReturn(it);
        Mockito.when(is.quantity()).thenReturn(5);
        Mockito.when(is.get(any())).thenReturn(
                Optional.of(Component.text("test", NamedTextColor.YELLOW, TextDecoration.BOLD)),
                Optional.of(Arrays.asList(
                        Component.text("line1", NamedTextColor.RED, TextDecoration.ITALIC),
                        Component.text("line2", NamedTextColor.BLUE, TextDecoration.UNDERLINED))));
        final String isStr = ItemStackSerializer.fromItemStack(is);
        assertEquals("minecraft:sand;5;<bold><yellow>test;<italic><red>line1</red></italic><br><underlined><blue>line2", isStr);
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

            final String isStr = "minecraft:sand;5;<yellow><bold>Name;<blue>line1</blue><br>line2";
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

}
