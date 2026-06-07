package fr.canardnocturne.questionstime.command.set;

import net.kyori.adventure.text.Component;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;

public class SetQuestionTimerExecutor implements CommandExecutor {

    public static final Parameter.Value<Integer> TIMER = Parameter.integerNumber().key("timer").build();

    @Override
    public CommandResult execute(final CommandContext context) throws CommandException {
        final Integer timer = context.requireOne(TIMER);
        context.sendMessage(Component.text("Timer: " + timer));
        return CommandResult.success();
    }
}
