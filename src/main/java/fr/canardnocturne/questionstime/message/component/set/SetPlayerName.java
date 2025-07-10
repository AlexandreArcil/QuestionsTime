package fr.canardnocturne.questionstime.message.component.set;

import fr.canardnocturne.questionstime.message.component.MessageComponents;
import fr.canardnocturne.questionstime.message.format.MessageFormat;
import org.spongepowered.api.entity.living.player.Player;

import java.util.Set;

public interface SetPlayerName<T extends MessageFormat.Format> extends SetComponent {

    default T setPlayerNames(final Set<Player> players) {
        setComponent(MessageComponents.PLAYERS_NAME, players);
        return (T) this;
    }
}
