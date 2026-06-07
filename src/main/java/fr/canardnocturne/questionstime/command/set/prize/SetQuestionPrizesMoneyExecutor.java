package fr.canardnocturne.questionstime.command.set.prize;

import net.kyori.adventure.text.Component;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;

public class SetQuestionPrizesMoneyExecutor implements CommandExecutor {

    public static final Parameter.Value<Integer> POSITION = Parameter.integerNumber().key("position").build(); //TODO change to choices if possible
    public static final Parameter.Value<Integer> AMOUNT = Parameter.integerNumber().key("amount").build();

    @Override
    public CommandResult execute(final CommandContext context) throws CommandException {
        final Integer position = context.requireOne(POSITION);
        final Integer amount = context.requireOne(AMOUNT);
        context.sendMessage(Component.text("Set prize amount for position " + position + " to " + amount));
        return CommandResult.success();
    }
}
