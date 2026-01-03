package fr.canardnocturne.questionstime.util;

import io.leangen.geantyref.TypeToken;
import net.kyori.adventure.text.Component;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.spongepowered.api.Game;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.Key;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.registry.BuilderProvider;
import org.spongepowered.api.registry.FactoryProvider;
import org.spongepowered.api.registry.RegistryKey;
import org.spongepowered.api.registry.RegistryType;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

public class SpongeMock {

    MockedStatic<Sponge> spongeMock;
    MockedStatic<RegistryKey> registryKeyMock;
    MockedStatic<ItemStack> itemStackMock;
    MockedStatic<Key> keyMock;

    @BeforeEach
    void init() {
        spongeMock = Mockito.mockStatic(Sponge.class);
        registryKeyMock = Mockito.mockStatic(RegistryKey.class);
        itemStackMock = Mockito.mockStatic(ItemStack.class);
        keyMock = Mockito.mockStatic(Key.class);

        final Game gameMock = Mockito.mock(Game.class);
        final BuilderProvider builderProviderMock = Mockito.mock(BuilderProvider.class);
        Mockito.when(gameMock.builderProvider()).thenReturn(builderProviderMock);
        spongeMock.when(Sponge::game).thenReturn(gameMock);
        final FactoryProvider factoryProviderMock = Mockito.mock(FactoryProvider.class);
        final ResourceKey.Factory resourceKeyFactoryMock = Mockito.mock(ResourceKey.Factory.class);
        final ResourceKey resourceKeyMock = Mockito.mock(ResourceKey.class);
        Mockito.when(resourceKeyFactoryMock.of(anyString(), anyString())).thenReturn(resourceKeyMock);
        Mockito.when(factoryProviderMock.provide(ResourceKey.Factory.class)).thenReturn(resourceKeyFactoryMock);
        Mockito.when(gameMock.factoryProvider()).thenReturn(factoryProviderMock);
        final RegistryType.Factory registryTypeMockFactory = Mockito.mock(RegistryType.Factory.class);
        final RegistryType registryTypeMock = Mockito.mock(RegistryType.class);
        Mockito.when(registryTypeMockFactory.create(any(), any())).thenReturn(registryTypeMock);
        Mockito.when(factoryProviderMock.provide(RegistryType.Factory.class)).thenReturn(registryTypeMockFactory);
        final RegistryKey registryKeyReturnMock = Mockito.mock(RegistryKey.class);
        registryKeyMock.when(() -> RegistryKey.of(any(), any())).thenReturn(registryKeyReturnMock);

        final Key.Builder keyBuilderMock = Mockito.mock(Key.Builder.class);
        Mockito.when(keyBuilderMock.key(Mockito.any())).thenReturn(keyBuilderMock);
        Mockito.when(keyBuilderMock.elementType(Mockito.any(Class.class))).thenReturn(keyBuilderMock);
        Mockito.when(keyBuilderMock.elementType(Mockito.any(TypeToken.class))).thenReturn(keyBuilderMock);
        Mockito.when(keyBuilderMock.weightedCollectionElementType(Mockito.any(Class.class))).thenReturn(keyBuilderMock);
        Mockito.when(keyBuilderMock.listElementType(Mockito.any(Class.class))).thenReturn(keyBuilderMock);
        Mockito.when(keyBuilderMock.mapElementType(Mockito.any(Class.class), Mockito.any(Class.class))).thenReturn(keyBuilderMock);
        Mockito.when(keyBuilderMock.setElementType(Mockito.any(Class.class))).thenReturn(keyBuilderMock);
        Mockito.when(keyBuilderMock.mapElementType(Mockito.any(TypeToken.class), Mockito.any(TypeToken.class))).thenReturn(keyBuilderMock);
        keyMock.when(Key::builder).thenReturn(keyBuilderMock);

        final ItemStack.Builder itemStackBuilderMock = Mockito.mock(ItemStack.Builder.class);
        Mockito.when(itemStackBuilderMock.itemType(ItemTypes.SAND)).thenReturn(itemStackBuilderMock);
        Mockito.when(itemStackBuilderMock.quantity(anyInt())).thenReturn(itemStackBuilderMock);
        Mockito.when(itemStackBuilderMock.add(eq(Keys.CUSTOM_NAME), any(Component.class))).thenReturn(itemStackBuilderMock);
        Mockito.when(itemStackBuilderMock.add(eq(Keys.LORE), any(List.class))).thenReturn(itemStackBuilderMock);
        Mockito.when(itemStackBuilderMock.build()).thenReturn(Mockito.mock(ItemStack.class));
        itemStackMock.when(ItemStack::builder).thenReturn(itemStackBuilderMock);
    }

    @AfterEach
    void close() {
        spongeMock.close();
        registryKeyMock.close();
        itemStackMock.close();
        keyMock.close();
    }

    public MockedStatic<Sponge> getSpongeMock() {
        return spongeMock;
    }
}
