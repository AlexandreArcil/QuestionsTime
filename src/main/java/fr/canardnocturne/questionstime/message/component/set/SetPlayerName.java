package fr.canardnocturne.questionstime.message.component.set;

import fr.canardnocturne.questionstime.message.component.MessageComponents;
import fr.canardnocturne.questionstime.message.format.MessageFormat;
import org.spongepowered.api.entity.living.player.Player;

public interface SetPlayerName<T extends MessageFormat.Format> extends SetComponent {

    default T setPlayerName(final Player player) {
        setComponent(MessageComponents.PLAYER_NAME, player);
        return (T) this;
    }
}
