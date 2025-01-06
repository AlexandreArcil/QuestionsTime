package fr.canardnocturne.questionstime.command;

import fr.canardnocturne.questionstime.util.TextUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;

public class BaseCommandExecutor implements CommandExecutor {

    @Override
    public CommandResult execute(final CommandContext context) throws CommandException {
        context.sendMessage(Component.text("---- ", NamedTextColor.AQUA)
                .append(Component.text("QuestionsTime", NamedTextColor.YELLOW, TextDecoration.BOLD))
                .append(Component.text(" ----", NamedTextColor.AQUA)));
        context.sendMessage(this.commandDescription("/qt ask [random/<question>]", "Ask a random or specific question now")
                .appendNewline().append(this.commandDescription("/qtc", "Create a question")));
        return CommandResult.success();
    }

    private Component commandDescription(final String command, final String description) {
        return Component.text().append(Component.text(command, NamedTextColor.GREEN, TextDecoration.BOLD))
                .appendSpace().append(TextUtils.special(description)).build();
    }
}
