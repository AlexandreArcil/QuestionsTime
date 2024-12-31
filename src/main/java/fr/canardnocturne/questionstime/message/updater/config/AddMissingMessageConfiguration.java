package fr.canardnocturne.questionstime.message.updater.config;

import fr.canardnocturne.questionstime.message.Messages;
import fr.canardnocturne.questionstime.message.SimpleMessage;
import org.apache.logging.log4j.Logger;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.serialize.SerializationException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

public class AddMissingMessageConfiguration implements MessageConfigurationUpdater {

    private final Logger logger;

    public AddMissingMessageConfiguration(final Logger logger) {
        this.logger = logger;
    }

    @Override
    public void updateConfig(final Map<String, String> messagesRead, final Path messagesConfig) {
        final ConfigurationLoader<CommentedConfigurationNode> loader = HoconConfigurationLoader.builder().path(messagesConfig).build();
        final CommentedConfigurationNode root;
        try {
            root = loader.load();
        } catch (final IOException e) {
            logger.error("Unable to read the messages", e);
            return;
        }

        int addedMessages = 0;
        try {
            for (final SimpleMessage message : Messages.getAll()) {
                if (!messagesRead.containsKey(message.getSection())) {
                    root.node(message.getSection()).set(message.getMessage());
                    addedMessages++;
                    logger.debug("Added the message '{}': '{}'", message.getSection(), message.getMessage());
                }
            }
        } catch (final SerializationException e) {
            logger.error("Unable to update the messages", e);
            return;
        }

        try {
            loader.save(root);
            this.logger.info("Added {} missing messages", addedMessages);
        } catch (final ConfigurateException e) {
            logger.error("Unable to save the messages", e);
        }
    }
}
