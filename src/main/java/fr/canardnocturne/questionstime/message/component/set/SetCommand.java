package fr.canardnocturne.questionstime.message.component.set;

import fr.canardnocturne.questionstime.message.component.MessageComponents;
import fr.canardnocturne.questionstime.message.format.MessageFormat;
import fr.canardnocturne.questionstime.question.component.OutcomeCommand;

public interface SetCommand<T extends MessageFormat.Format> extends SetComponent {

    default T setCommand(final OutcomeCommand command) {
        setComponent(MessageComponents.COMMAND, command);
        return (T) this;
    }
}
