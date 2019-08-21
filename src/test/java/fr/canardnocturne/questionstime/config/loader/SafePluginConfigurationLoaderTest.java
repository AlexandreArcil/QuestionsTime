package fr.canardnocturne.questionstime.config.loader;

import fr.canardnocturne.questionstime.config.QuestionTimeConfiguration;
import org.apache.logging.log4j.LogManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.serialize.SerializationException;

@Disabled("Sponge not initialized when creating the ItemStack in QuestionTimeConfiguration")
public class SafePluginConfigurationLoaderTest {

    @Test
    public void unableReadConfigFile() {
        Assertions.assertDoesNotThrow(() -> {
            final SafePluginConfigurationLoader pluginConfigLoader = new SafePluginConfigurationLoader(LogManager.getLogger());
            final ConfigurationLoader<CommentedConfigurationNode> loader = Mockito.mock(ConfigurationLoader.class);
            Mockito.when(loader.load()).thenThrow(new ConfigurateException());

            QuestionTimeConfiguration defaultConfig = pluginConfigLoader.load(loader);
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

            QuestionTimeConfiguration defaultConfig = pluginConfigLoader.load(loader);
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

            QuestionTimeConfiguration defaultConfig = pluginConfigLoader.load(loader);
            Assertions.assertNotNull(defaultConfig);
        });
    }

    @Test
    public void configFileReadWithSuccess() {
        Assertions.assertDoesNotThrow(() -> {
            final SafePluginConfigurationLoader pluginConfigLoader = new SafePluginConfigurationLoader(LogManager.getLogger());
            final CommentedConfigurationNode rootNode = Mockito.mock(CommentedConfigurationNode.class);
            Mockito.when(rootNode.get(QuestionTimeConfiguration.class)).thenReturn(new QuestionTimeConfiguration());
            final ConfigurationLoader<CommentedConfigurationNode> loader = Mockito.mock(ConfigurationLoader.class);
            Mockito.when(loader.load()).thenReturn(rootNode);

            QuestionTimeConfiguration defaultConfig = pluginConfigLoader.load(loader);
            Assertions.assertNotNull(defaultConfig);
        });
    }

}
