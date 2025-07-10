package fr.canardnocturne.questionstime.message.component;

import fr.canardnocturne.questionstime.util.NumberUtils;
import net.kyori.adventure.text.Component;

public class ComponentWinnerPosition extends MessageComponent<Integer> {

    public ComponentWinnerPosition(final String name) {
        super(name);
    }

    @Override
    public Component process(final Integer position) {
        return Component.text(NumberUtils.toOrdinal(position));
    }
}
