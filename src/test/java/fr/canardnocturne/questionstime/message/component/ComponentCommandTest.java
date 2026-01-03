package fr.canardnocturne.questionstime.message.component;

import fr.canardnocturne.questionstime.question.component.OutcomeCommand;
import fr.canardnocturne.questionstime.util.MiniMessageTest;
import net.kyori.adventure.text.Component;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ComponentCommandTest {

    @Test
    void testProcess() {
        final ComponentCommand componentCommand = new ComponentCommand("command");
        final String message = "Coin coin!";
        final OutcomeCommand command = new OutcomeCommand(message, "command");

        final Component result = componentCommand.process(command);

        assertNotNull(result);
        assertEquals(message, MiniMessageTest.NO_STYLE_COMPONENT.serialize(result));
    }

}