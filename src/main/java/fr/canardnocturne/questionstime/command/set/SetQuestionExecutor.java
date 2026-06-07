package fr.canardnocturne.questionstime.command.set;

import net.kyori.adventure.text.Component;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;

public class SetQuestionExecutor implements CommandExecutor {

    public static final Parameter.Value<String> QUESTION = Parameter.string().key("question").build();

    @Override
    public CommandResult execute(final CommandContext context) throws CommandException {
        final String question = context.requireOne(QUESTION);
        context.sendMessage(Component.text("Question: " + question));
        return CommandResult.success();
    }
}
