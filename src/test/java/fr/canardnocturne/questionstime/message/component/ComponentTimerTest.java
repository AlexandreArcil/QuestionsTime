package fr.canardnocturne.questionstime.message.component;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ComponentTimerTest {

    @Test
    void processFullSingularTime() {
        final ComponentTimer timer = new ComponentTimer("timer");

        final Component result = timer.process(3600 + 60 + 1);

        assertTrue(Component.EQUALS.test(Component.text("1h1min1sec", NamedTextColor.AQUA), result));
    }

    @Test
    void processFullPluralTime() {
        final ComponentTimer timer = new ComponentTimer("timer");

        final Component result = timer.process(7200 + 120 + 2);

        assertTrue(Component.EQUALS.test(Component.text("2hs2mins2secs", NamedTextColor.AQUA), result));
    }

}