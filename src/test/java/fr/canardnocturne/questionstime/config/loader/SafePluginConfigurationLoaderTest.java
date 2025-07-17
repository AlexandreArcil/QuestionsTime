package fr.canardnocturne.questionstime.config.loader;

import fr.canardnocturne.questionstime.config.QuestionTimeConfiguration;
import io.leangen.geantyref.TypeToken;
import net.kyori.adventure.text.Component;
import org.apache.logging.log4j.LogManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;

public class SafePluginConfigurationLoaderTest {

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

    @Test
    public void unableReadConfigFile() {
        Assertions.assertDoesNotThrow(() -> {
            final SafePluginConfigurationLoader pluginConfigLoader = new SafePluginConfigurationLoader(LogManager.getLogger());
            final ConfigurationLoader<CommentedConfigurationNode> loader = Mockito.mock(ConfigurationLoader.class);
            Mockito.when(loader.load()).thenThrow(new ConfigurateException());

            final QuestionTimeConfiguration defaultConfig = pluginConfigLoader.load(loader);
            Assertions.assertNotNull(defaultConfig);
        });
    }

    @Test
    public void unableToSerialize() {
        Assertions.assertDoesNotThrow(() -> {
            final SafePluginConfigurationLoader pluginConfigLoader = new SafePluginConfigurationLoader(LogManager.getLogger());
            final CommentedConfigurationNode rootNode = Mockito.mock(CommentedConfigurationNode.class);
            Mockito.when(rootNode.get(QuestionTimeConfiguration.class)).thenThrow(new SerializationException());
            final ConfigurationLoader<CommentedConfigurationNode> loader = Mockito.mock(ConfigurationLoader.class);
            Mockito.when(loader.load()).thenReturn(rootNode);

            final QuestionTimeConfiguration defaultConfig = pluginConfigLoader.load(loader);
            Assertions.assertNotNull(defaultConfig);
        });
    }

    @Test
    public void configFileEmpty() {
        Assertions.assertDoesNotThrow(() -> {
            final SafePluginConfigurationLoader pluginConfigLoader = new SafePluginConfigurationLoader(LogManager.getLogger());
            final CommentedConfigurationNode rootNode = Mockito.mock(CommentedConfigurationNode.class);
            Mockito.when(rootNode.get(QuestionTimeConfiguration.class)).thenReturn(null);
            final ConfigurationLoader<CommentedConfigurationNode> loader = Mockito.mock(ConfigurationLoader.class);
            Mockito.when(loader.load()).thenReturn(rootNode);

            final QuestionTimeConfiguration defaultConfig = pluginConfigLoader.load(loader);
            Assertions.assertNotNull(defaultConfig);
        });
    }

    @Test
    public void configFileReadWithSuccess() {
        Assertions.assertDoesNotThrow(() -> {
            final SafePluginConfigurationLoader pluginConfigLoader = new SafePluginConfigurationLoader(LogManager.getLogger());
            final CommentedConfigurationNode rootNode = Mockito.mock(CommentedConfigurationNode.class);
            final QuestionTimeConfiguration questionTimeConfiguration = new QuestionTimeConfiguration();
            Mockito.when(rootNode.get(QuestionTimeConfiguration.class)).thenReturn(questionTimeConfiguration);
            final ConfigurationLoader<CommentedConfigurationNode> loader = Mockito.mock(ConfigurationLoader.class);
            Mockito.when(loader.load()).thenReturn(rootNode);

            final QuestionTimeConfiguration defaultConfig = pluginConfigLoader.load(loader);
            Assertions.assertNotNull(defaultConfig);
            Assertions.assertEquals(questionTimeConfiguration, defaultConfig);
        });
    }

}
