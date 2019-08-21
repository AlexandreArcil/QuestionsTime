package fr.canardnocturne.questionstime.message.component.set;

import fr.canardnocturne.questionstime.message.component.MessageComponents;
import fr.canardnocturne.questionstime.message.format.MessageFormat;

public interface SetAnswer<T extends MessageFormat.Format> extends SetComponent {

    default T setAnswer(final String answer) {
        setComponent(MessageComponents.ANSWER, answer);
        return (T) this;
    }
}
