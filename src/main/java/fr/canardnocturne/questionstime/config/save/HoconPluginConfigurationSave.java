package fr.canardnocturne.questionstime.config.save;

import fr.canardnocturne.questionstime.config.ConfigField;
import fr.canardnocturne.questionstime.config.QuestionTimeConfiguration;
import org.apache.logging.log4j.Logger;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.serialize.SerializationException;

import java.io.IOException;

public class HoconPluginConfigurationSave implements PluginConfigurationSave {

    private final ConfigurationLoader<CommentedConfigurationNode> configLoader;
    private CommentedConfigurationNode configurationNode;
    private final Logger logger;

    public HoconPluginConfigurationSave(final ConfigurationLoader<CommentedConfigurationNode> configLoader, final Logger logger) {
        this.configLoader = configLoader;
        this.logger = logger;
    }

    @Override
    public void save(final QuestionTimeConfiguration configuration) throws IOException {
        final CommentedConfigurationNode rootNode = this.getConfigurationNode();
        try {
            rootNode.node(ConfigField.COOLDOWN.getName()).set(configuration.getCooldown());
            rootNode.node(ConfigField.MODE.getName()).set(configuration.getMode());
            rootNode.node(ConfigField.MIN_COOLDOWN.getName()).set(configuration.getMinCooldown());
            rootNode.node(ConfigField.MAX_COOLDOWN.getName()).set(configuration.getMaxCooldown());
            rootNode.node(ConfigField.PERSONAL_ANSWER.getName()).set(configuration.isPersonalAnswer());
            rootNode.node(ConfigField.MINIMUM_CONNECTED.getName()).set(configuration.getMinConnected());
            this.configLoader.save(rootNode);
        } catch (final SerializationException e) {
            logger.error("Unable to serialize the config", e);
            throw e;
        } catch (final ConfigurateException e) {
            logger.error("Unable to save the config file", e);
            throw e;
        }
    }

    private CommentedConfigurationNode getConfigurationNode() {
        if (this.configurationNode == null) {
            try {
                this.configurationNode = this.configLoader.load();
            } catch (final ConfigurateException e) {
                logger.error("Unable to load the config file ", e);
                throw new IllegalStateException(e);
            }
        }
        return this.configurationNode;
    }
}
