package fr.canardnocturne.questionstime.message.component.set;

import fr.canardnocturne.questionstime.message.component.MessageComponents;
import fr.canardnocturne.questionstime.message.format.MessageFormat;

public interface SetPosition<T extends MessageFormat.Format> extends SetComponent {

    default T setPosition(final byte pos) {
        setComponent(MessageComponents.POSITION, pos);
        return (T) this;
    }

}
