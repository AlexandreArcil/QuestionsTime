package fr.canardnocturne.questionstime.message.component.set;

import fr.canardnocturne.questionstime.message.component.MessageComponents;
import fr.canardnocturne.questionstime.message.format.MessageFormat;

public interface SetMoney<T extends MessageFormat.Format> extends SetComponent {

    default T setMoney(final int money) {
        setComponent(MessageComponents.MONEY, money);
        return (T) this;
    }
}
