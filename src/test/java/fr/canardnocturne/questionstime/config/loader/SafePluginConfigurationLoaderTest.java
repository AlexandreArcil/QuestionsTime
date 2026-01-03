package fr.canardnocturne.questionstime.config.loader;

import fr.canardnocturne.questionstime.config.QuestionTimeConfiguration;
import fr.canardnocturne.questionstime.util.SpongeMock;
import org.apache.logging.log4j.LogManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.serialize.SerializationException;

public class SafePluginConfigurationLoaderTest extends SpongeMock {


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
