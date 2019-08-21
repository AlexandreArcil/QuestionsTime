package fr.canardnocturne.questionstime.message.component;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.concurrent.TimeUnit;

public class ComponentTimer extends MessageComponent<Integer> {

    public ComponentTimer(final String name) {
        super(name);
    }

    @Override
    public Component process(Integer second) {
        final long hours = TimeUnit.SECONDS.toHours(second);
        second -= (int) TimeUnit.HOURS.toSeconds(hours);
        final long minutes = TimeUnit.SECONDS.toMinutes(second);
        second -= (int) TimeUnit.MINUTES.toSeconds(minutes);
        final long seconds = TimeUnit.SECONDS.toSeconds(second);
        final StringBuilder strBuilder = new StringBuilder();
        if (hours == 1)
            strBuilder.append(hours).append("h");
        else if (hours > 1)
            strBuilder.append(hours).append("hs");
        if (minutes == 1)
            strBuilder.append(minutes).append("min");
        else if (minutes > 1)
            strBuilder.append(minutes).append("mins");
        if (seconds == 1)
            strBuilder.append(seconds).append("sec");
        else if (seconds > 1)
            strBuilder.append(seconds).append("secs");
        return Component.text(strBuilder.toString(), NamedTextColor.AQUA);
    }
}
