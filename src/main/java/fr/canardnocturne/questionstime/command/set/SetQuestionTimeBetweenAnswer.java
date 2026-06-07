package fr.canardnocturne.questionstime.command.set;

import net.kyori.adventure.text.Component;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;

public class SetQuestionTimeBetweenAnswer implements CommandExecutor {

     public static final Parameter.Value<Integer> TIME_BETWEEN_ANSWER = Parameter.integerNumber().key("time_between_answer").build();

     @Override
     public CommandResult execute(final CommandContext context) throws CommandException {
         final Integer timeBetweenAnswer = context.requireOne(TIME_BETWEEN_ANSWER);
         context.sendMessage(Component.text("Time between answer: " + timeBetweenAnswer));
         return CommandResult.success();
     }
}
