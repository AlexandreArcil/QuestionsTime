package fr.canardnocturne.questionstime.command.set.prize;

import net.kyori.adventure.text.Component;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;

public class SetQuestionPrizesCommandsExecutor implements CommandExecutor {

    private static final Parameter.Value<Integer> POSITION = Parameter.integerNumber().key("position").build(); //TODO change to choices if possible
    private static final Parameter.Value<Integer> COMMAND = Parameter.integerNumber().key("command").build();
    private static final Parameter.Value<String> ADD_REMOVE_ACTION = Parameter.choices("add", "remove").key("add_remove").build();
    public static final Parameter ACTIONS = Parameter.seq(ADD_REMOVE_ACTION, POSITION, COMMAND);

    @Override
    public CommandResult execute(final CommandContext context) throws CommandException {
        final String action = context.requireOne(ADD_REMOVE_ACTION);
        final Integer position = context.requireOne(POSITION);
        final Integer command = context.requireOne(COMMAND);
        context.sendMessage(Component.text("Set command " + command + " for position " + position + " with action " + action));
        return CommandResult.success();
    }
}
