package fr.canardnocturne.questionstime.message.component;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;

import java.util.Set;

public class ComponentAnswers extends MessageComponent<Set<String>> {

    public ComponentAnswers(final String name) {
        super(name);
    }

    @Override
    public Component process(final Set<String> answers) {
        return Component.join(JoinConfiguration.separators(Component.text(", "), Component.text(" and ")),
                answers.stream().map(Component::text).toList());
    }

}
