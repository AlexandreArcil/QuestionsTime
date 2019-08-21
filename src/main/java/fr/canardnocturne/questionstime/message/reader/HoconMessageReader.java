package fr.canardnocturne.questionstime.message.reader;

import org.apache.logging.log4j.Logger;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.loader.ConfigurationLoader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HoconMessageReader implements MessageReader {

    private final Logger logger;

    public HoconMessageReader(final Logger logger) {
        this.logger = logger;
    }

    @Override
    public Map<String, String> readMessages(final Path path) {
        final ConfigurationLoader<CommentedConfigurationNode> loader = HoconConfigurationLoader.builder().path(path).build();
        final CommentedConfigurationNode root;
        try {
            root = loader.load();
        } catch (final IOException e) {
            logger.error("Unable to load the messages", e);
            return new HashMap<>();
        }

        final Map<String, String> messages = root.visit(new HoconMessageConfigurationVisitor() {
            @Override
            public void enterScalarNode(final ConfigurationNode node, final Map<String, String> messages) {
                final String section = String.join(".", Arrays.stream(node.path().array()).toArray(String[]::new));
                final String newMessage = node.getString();
                messages.put(section, newMessage);
            }
        });
        return messages;
    }
}
