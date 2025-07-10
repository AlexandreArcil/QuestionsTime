package fr.canardnocturne.questionstime.message.component;

import net.kyori.adventure.text.Component;
import org.spongepowered.api.entity.living.player.Player;

import java.util.Set;

public class ComponentPlayersName extends MessageComponent<Set<Player>> {

    public ComponentPlayersName(final String name) {
        super(name);
    }

    @Override
    public Component process(final Set<Player> players) {
        if(players.size() == 1) {
            return Component.text(players.iterator().next().name());
        }

        final StringBuilder stringBuilder = new StringBuilder();
        int position = 0;
        //A, B, C, D and E
        for (final Player player : players) {
            stringBuilder.append(player.name());
            if(position == players.size() - 2) {
                stringBuilder.append(" and ");
            } else if(position < players.size() - 2) {
                stringBuilder.append(", ");
            }
            position++;
        }

        return Component.text(stringBuilder.toString());
    }
}
