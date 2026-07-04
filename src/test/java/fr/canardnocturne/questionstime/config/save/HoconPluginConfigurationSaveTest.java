package fr.canardnocturne.questionstime.config.save;

import fr.canardnocturne.questionstime.config.ConfigField;
import fr.canardnocturne.questionstime.config.QuestionTimeConfiguration;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.serialize.SerializationException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class HoconPluginConfigurationSaveTest {

    @Mock
    private ConfigurationLoader<CommentedConfigurationNode> configLoader;

    @Mock
    private Logger logger;

    @InjectMocks
    private HoconPluginConfigurationSave hoconPluginConfigurationSave;

    @Test
    void saveConfiguration() throws IOException {
        final CommentedConfigurationNode rootNode = Mockito.mock(CommentedConfigurationNode.class);
        Mockito.when(rootNode.node(Mockito.anyString())).thenReturn(rootNode);
        Mockito.when(configLoader.load()).thenReturn(rootNode);

        final QuestionTimeConfiguration questionTimeConfiguration = Mockito.mock(QuestionTimeConfiguration.class);
        Mockito.when(questionTimeConfiguration.getCooldown()).thenReturn(10);
        Mockito.when(questionTimeConfiguration.getMode()).thenReturn(QuestionTimeConfiguration.Mode.MANUAL);
        Mockito.when(questionTimeConfiguration.getMinCooldown()).thenReturn(5);
        Mockito.when(questionTimeConfiguration.getMaxCooldown()).thenReturn(15);
        Mockito.when(questionTimeConfiguration.isPersonalAnswer()).thenReturn(true);
        Mockito.when(questionTimeConfiguration.getMinConnected()).thenReturn(3);
        this.hoconPluginConfigurationSave.save(questionTimeConfiguration);

        for (final ConfigField configField : ConfigField.values()) {
            if(configField != ConfigField.VERSION) {
                Mockito.verify(rootNode).node(configField.getName());
            }
        }
        Mockito.verify(rootNode, Mockito.times(6)).set(Mockito.notNull());
        Mockito.verify(configLoader).save(Mockito.any(CommentedConfigurationNode.class));
    }

    @Test
    void saveThrowsSerializationException() throws IOException {
        final CommentedConfigurationNode rootNode = Mockito.mock(CommentedConfigurationNode.class);
        Mockito.when(rootNode.node(Mockito.anyString())).thenReturn(rootNode);
        Mockito.when(configLoader.load()).thenReturn(rootNode);
        Mockito.when(rootNode.set(Mockito.any())).thenThrow(new SerializationException("Serialization error"));

        final QuestionTimeConfiguration questionTimeConfiguration = Mockito.mock(QuestionTimeConfiguration.class);
        assertThrows(SerializationException.class, () -> this.hoconPluginConfigurationSave.save(questionTimeConfiguration));

        Mockito.verify(configLoader, Mockito.never()).save(Mockito.any(CommentedConfigurationNode.class));
    }

    @Test
    void saveThrowsConfigurationException() throws IOException {
        final CommentedConfigurationNode rootNode = Mockito.mock(CommentedConfigurationNode.class);
        Mockito.when(rootNode.node(Mockito.anyString())).thenReturn(rootNode);
        Mockito.when(configLoader.load()).thenReturn(rootNode);
        Mockito.doThrow(new ConfigurateException("Configuration error")).when(configLoader).save(Mockito.any(CommentedConfigurationNode.class));

        final QuestionTimeConfiguration questionTimeConfiguration = Mockito.mock(QuestionTimeConfiguration.class);
        assertThrows(ConfigurateException.class, () -> this.hoconPluginConfigurationSave.save(questionTimeConfiguration));
    }

    @Test
    void getConfigurationNodeThrowsConfigurationException() throws IOException {
        Mockito.when(configLoader.load()).thenThrow(new ConfigurateException("Configuration error"));

        final QuestionTimeConfiguration questionTimeConfiguration = Mockito.mock(QuestionTimeConfiguration.class);
        assertThrows(IllegalStateException.class, () -> this.hoconPluginConfigurationSave.save(questionTimeConfiguration));

        Mockito.verify(configLoader, Mockito.never()).save(Mockito.any(CommentedConfigurationNode.class));
    }

}