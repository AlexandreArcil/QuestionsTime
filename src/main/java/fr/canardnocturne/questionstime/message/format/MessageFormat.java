package fr.canardnocturne.questionstime.message.format;

import fr.canardnocturne.questionstime.message.SimpleMessage;
import fr.canardnocturne.questionstime.message.component.MessageComponent;
import fr.canardnocturne.questionstime.message.component.set.SetComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public abstract class MessageFormat<T extends MessageFormat.Format> extends SimpleMessage {

    private static final Pattern PATTERN_COMPONENT = Pattern.compile("\\{[^}]*\\}");

    public MessageFormat(final String section, final String message) {
        super(section, message);
    }

    @Override
    public void setMessage(final String newMessage) throws IllegalArgumentException {
        final Set<String> messageComponents = this.getComponentsName(this.message);
        final Set<String> givenMessageComponents = this.getComponentsName(newMessage);
        if (messageComponents.equals(givenMessageComponents))
            super.setMessage(newMessage);
        else {
            final String wantedComponents = String.join(", ", messageComponents);
            final String givenComponents = String.join(", ", givenMessageComponents);
            throw new IllegalArgumentException("The message at the section '" + this.section + "' doesn't have the right arguments. Wanted: [" + wantedComponents + "], given: [" + givenComponents + "]. The default value will be used.");
        }
    }

    private Set<String> getComponentsName(final String message) {
        return PATTERN_COMPONENT.matcher(message).results().map(MatchResult::group).collect(Collectors.toUnmodifiableSet());
    }

    public abstract T format();

    public class Format implements SetComponent {

        public Map<MessageComponent, Object> componentsMapper = new HashMap<>();

        @Override
        public <V> void setComponent(final MessageComponent<V> component, final V value) {
            this.componentsMapper.put(component, value);
        }

        public Component message() {
            Component messageToFormat = Component.text(message);
            for (final Map.Entry<MessageComponent, Object> componentMapper : this.componentsMapper.entrySet()) {
                final MessageComponent component = componentMapper.getKey();
                final Component text = component.process(componentMapper.getValue());
                messageToFormat = messageToFormat.replaceText(TextReplacementConfig.builder().match(Pattern.quote("{") + component.getName() + Pattern.quote("}")).replacement(text).build());
            }
            return messageToFormat;
        }

    }

}
