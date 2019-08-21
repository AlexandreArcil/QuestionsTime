package fr.canardnocturne.questionstime.message.updater;

import fr.canardnocturne.questionstime.message.Messages;
import fr.canardnocturne.questionstime.message.SimpleMessage;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class SafeMessageUpdater implements MessageUpdater {

    private final Logger logger;

    public SafeMessageUpdater(final Logger pluginLogger) {
        this.logger = pluginLogger;
    }

    @Override
    public void updateMessages(final Map<String, String> messages) {
        int changedMessages = 0;
        for (final Map.Entry<String, String> message : messages.entrySet()) {
            final String section = message.getKey();
            final SimpleMessage registeredMessage = Messages.getMessage(section);
            if (registeredMessage != null) {
                try {
                    final String newMessage = message.getValue();
                    final String originalMessage = registeredMessage.getMessage();
                    if (!newMessage.equals(originalMessage)) {
                        registeredMessage.setMessage(newMessage);
                        changedMessages++;
                        logger.debug("'{}': \"{}\" -> {}", registeredMessage.getSection(), originalMessage, newMessage);
                    } else {
                        logger.debug("'{}': \"{}\" not modified", section, originalMessage);
                    }
                } catch (final IllegalArgumentException e) {
                    logger.warn(e.getMessage());
                }
            } else {
                logger.warn("The section '{}' doesn't refer to a known message section", section);
            }
        }
        this.logger.info("Messages loaded, {} messages of {} replaced", changedMessages, Messages.registeredMessagesCount());
    }

}
