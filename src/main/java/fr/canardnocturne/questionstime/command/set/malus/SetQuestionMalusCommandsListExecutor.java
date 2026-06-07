package fr.canardnocturne.questionstime.command.set.malus;

import net.kyori.adventure.text.Component;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;

public class SetQuestionMalusCommandsListExecutor implements CommandExecutor {

    @Override
    public CommandResult execute(final CommandContext context) throws CommandException {
        context.sendMessage(Component.text("List malus"));
        return CommandResult.success();
    }
}
