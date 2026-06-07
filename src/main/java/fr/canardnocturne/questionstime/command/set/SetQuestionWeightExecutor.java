package fr.canardnocturne.questionstime.command.set;

import net.kyori.adventure.text.Component;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;

public class SetQuestionWeightExecutor implements CommandExecutor {

    public static final Parameter.Value<Integer> WEIGHT = Parameter.integerNumber().key("weight").build();

    @Override
    public CommandResult execute(final CommandContext context) throws CommandException {
        final Integer weight = context.requireOne(WEIGHT);
        context.sendMessage(Component.text("Weight: " + weight));
        return CommandResult.success();
    }
}
