package fr.canardnocturne.questionstime.message.component.set;

import fr.canardnocturne.questionstime.message.component.MessageComponents;
import fr.canardnocturne.questionstime.message.format.MessageFormat;

public interface SetWinnerPosition<T extends MessageFormat.Format> extends SetComponent {

    default T setWinnerPosition(final int position) {
        setComponent(MessageComponents.WINNER_POSITION, position);
        return (T) this;
    }
}
