package fr.canardnocturne.questionstime.message.component.set;

import fr.canardnocturne.questionstime.message.component.MessageComponents;
import fr.canardnocturne.questionstime.message.format.MessageFormat;

public interface SetTimer<T extends MessageFormat.Format> extends SetComponent {

    default T setTimer(final int timer) {
        setComponent(MessageComponents.TIMER, timer);
        return (T) this;
    }

}
