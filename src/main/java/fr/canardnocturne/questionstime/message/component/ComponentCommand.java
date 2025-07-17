package fr.canardnocturne.questionstime.message.component;

import fr.canardnocturne.questionstime.question.component.OutcomeCommand;
import net.kyori.adventure.text.Component;

public class ComponentCommand  extends MessageComponent<OutcomeCommand> {

    public ComponentCommand(final String name) {
        super(name);
    }

    @Override
    public Component process(final OutcomeCommand command) {
        return Component.text(command.message());
    }
}
