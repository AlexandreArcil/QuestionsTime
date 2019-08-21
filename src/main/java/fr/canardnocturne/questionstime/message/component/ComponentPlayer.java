package fr.canardnocturne.questionstime.message.component;

import net.kyori.adventure.text.Component;
import org.spongepowered.api.entity.living.player.Player;

public class ComponentPlayer extends MessageComponent<Player> {

    public ComponentPlayer(final String name) {
        super(name);
    }

    @Override
    public Component process(final Player player) {
        return Component.text(player.name());
    }
}
