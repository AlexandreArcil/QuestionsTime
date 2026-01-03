package fr.canardnocturne.questionstime.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MiniMessageTest {

    private final static Logger LOGGER = LogManager.getLogger(MiniMessageTest.class);

    public final static MiniMessage NO_STYLE_COMPONENT = MiniMessage.builder().tags(TagResolver.empty()).build();

    public static boolean containsAll(final Component component, final String... toContain) {
        final String message = NO_STYLE_COMPONENT.serialize(component);
        for (final String str : toContain) {
            if (!message.contains(str)) {
                LOGGER.warn("Message '{}' does not contain expected substring '{}'", message, str);
                return false;
            }
        }
        return true;
    }

}
