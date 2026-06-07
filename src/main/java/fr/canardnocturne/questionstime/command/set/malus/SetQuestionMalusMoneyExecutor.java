package fr.canardnocturne.questionstime.command.set.malus;

import net.kyori.adventure.text.Component;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;

public class SetQuestionMalusMoneyExecutor implements CommandExecutor {

    private static final Parameter.Value<Integer> MONEY = Parameter.integerNumber().key("money").build();

    @Override
    public CommandResult execute(final CommandContext context) throws CommandException {
        final Integer money = context.requireOne(MONEY);
        context.sendMessage(Component.text("Set money " + money));
        return CommandResult.success();
    }
}
