package fr.canardnocturne.questionstime.command.set.propositions;

import net.kyori.adventure.text.Component;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;

public class SetQuestionPropositions implements CommandExecutor {

    private static final Parameter.Value<String> PROPOSITION = Parameter.remainingJoinedStrings().key("proposition").build();
    private static final Parameter.Value<String> ADD_REMOVE_ACTION = Parameter.choices("add", "remove").key("add_remove").build();
    public static final Parameter ACTIONS = Parameter.seq(ADD_REMOVE_ACTION,  PROPOSITION);

     @Override
     public CommandResult execute(final CommandContext context) throws CommandException {
         final String action = context.requireOne(ADD_REMOVE_ACTION);
         final String answer = context.requireOne(PROPOSITION);
         context.sendMessage(Component.text("Action: " + action + ", answer: " + answer));
         return CommandResult.success();
     }
}
