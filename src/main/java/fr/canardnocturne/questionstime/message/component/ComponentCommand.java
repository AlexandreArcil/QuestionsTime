package fr.canardnocturne.questionstime.message.component;

import fr.canardnocturne.questionstime.question.component.PrizeCommand;
import net.kyori.adventure.text.Component;

public class ComponentCommand  extends MessageComponent<PrizeCommand> {

    public ComponentCommand(final String name) {
        super(name);
    }

    @Override
    public Component process(final PrizeCommand command) {
        return Component.text(command.message());
    }
}
