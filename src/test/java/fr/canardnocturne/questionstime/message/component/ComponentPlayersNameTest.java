package fr.canardnocturne.questionstime.message.component;

import net.kyori.adventure.text.Component;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.spongepowered.api.entity.living.player.Player;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ComponentPlayersNameTest {

    @Test
    void processSinglePlayer() {
        final Player player = Mockito.mock(Player.class);
        Mockito.when(player.name()).thenReturn("Coin1");

        final ComponentPlayersName component = new ComponentPlayersName("playerName");
        final Component result = component.process(Set.of(player));

        assertTrue(Component.EQUALS.test(Component.text("Coin1"), result));
    }

    @Test
    void processTwoPlayers() {
        final Player player1 = Mockito.mock(Player.class);
        Mockito.when(player1.name()).thenReturn("Coin1");
        final Player player2 = Mockito.mock(Player.class);
        Mockito.when(player2.name()).thenReturn("Coin2");

        final ComponentPlayersName component = new ComponentPlayersName("playerName");
        final Component result = component.process(Set.of(player1, player2));

        assertTrue(Component.EQUALS.test(Component.text("Coin1 and Coin2"), result) ||
                Component.EQUALS.test(Component.text("Coin2 and Coin1"), result));
    }

    @Test
    void processMultiplePlayers() {
        final Player player1 = Mockito.mock(Player.class);
        Mockito.when(player1.name()).thenReturn("Coin1");
        final Player player2 = Mockito.mock(Player.class);
        Mockito.when(player2.name()).thenReturn("Coin2");
        final Player player3 = Mockito.mock(Player.class);
        Mockito.when(player3.name()).thenReturn("Coin3");

        final ComponentPlayersName component = new ComponentPlayersName("playerName");
        final Component result = component.process(Set.of(player1, player2, player3));

        assertTrue(Component.EQUALS.test(Component.text("Coin1, Coin2 and Coin3"), result) ||
                Component.EQUALS.test(Component.text("Coin1, Coin3 and Coin2"), result) ||
                Component.EQUALS.test(Component.text("Coin2, Coin1 and Coin3"), result) ||
                Component.EQUALS.test(Component.text("Coin2, Coin3 and Coin1"), result) ||
                Component.EQUALS.test(Component.text("Coin3, Coin1 and Coin2"), result) ||
                Component.EQUALS.test(Component.text("Coin3, Coin2 and Coin1"), result));
    }

}