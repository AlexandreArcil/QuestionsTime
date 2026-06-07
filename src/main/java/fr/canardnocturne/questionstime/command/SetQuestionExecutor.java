package fr.canardnocturne.questionstime.command;

import net.kyori.adventure.text.Component;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;

public class SetQuestionExecutor implements CommandExecutor {

    public static final Parameter.Value<String> QUESTION_COMPONENT_PARAMETER = Parameter.string().key("question_component").build();

    @Override
    public CommandResult execute(final CommandContext context) throws CommandException {
        context.sendMessage(Component.text("Ok !"));
        return CommandResult.success();
    }

}
