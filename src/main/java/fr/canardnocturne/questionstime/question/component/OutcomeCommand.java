package fr.canardnocturne.questionstime.question.component;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public record OutcomeCommand(String message, String command) {

    public Component format() {
        return Component.text(this.message(), NamedTextColor.BLUE, TextDecoration.UNDERLINED)
                .hoverEvent(HoverEvent.showText(Component.text(this.command())));
    }

}
