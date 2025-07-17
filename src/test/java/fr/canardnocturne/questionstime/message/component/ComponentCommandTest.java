package fr.canardnocturne.questionstime.message.component;

import fr.canardnocturne.questionstime.question.component.OutcomeCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ComponentCommandTest {

    @Test
    void testProcess() {
        final ComponentCommand componentCommand = new ComponentCommand("command");
        final String message = "Coin coin!";
        final OutcomeCommand command = new OutcomeCommand(message, "command");

        final Component result = componentCommand.process(command);

        assertNotNull(result);
        assertTrue(MiniMessage.miniMessage().serialize(result).contains(message));
    }

}