package fr.canardnocturne.questionstime.message.component.set;

import fr.canardnocturne.questionstime.message.component.MessageComponents;
import fr.canardnocturne.questionstime.message.format.MessageFormat;

public interface SetProposition<T extends MessageFormat.Format> extends SetComponent {

    default T setProposition(final String proposition) {
        setComponent(MessageComponents.PROPOSITION, proposition);
        return (T) this;
    }

}
