package fr.canardnocturne.questionstime.message.component.set;

import fr.canardnocturne.questionstime.message.component.MessageComponents;
import fr.canardnocturne.questionstime.message.format.MessageFormat;

public interface SetQuantity<T extends MessageFormat.Format> extends SetComponent {

    default T setQuantity(final int quantity) {
        setComponent(MessageComponents.QUANTITY, quantity);
        return (T) this;
    }

}
