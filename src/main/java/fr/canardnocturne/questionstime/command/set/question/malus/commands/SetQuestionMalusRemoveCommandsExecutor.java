package fr.canardnocturne.questionstime.command.set.question.malus.commands;

import fr.canardnocturne.questionstime.QuestionException;
import fr.canardnocturne.questionstime.question.QuestionComponent;
import fr.canardnocturne.questionstime.question.modifier.QuestionModifier;
import fr.canardnocturne.questionstime.question.Question;
import fr.canardnocturne.questionstime.question.ask.pool.QuestionPool;
import fr.canardnocturne.questionstime.question.save.QuestionRegister;
import fr.canardnocturne.questionstime.util.TextUtils;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

public class SetQuestionMalusRemoveCommandsExecutor implements CommandExecutor {

    private final Parameter.Value<Question> specificQuestionParameter;
    private final Parameter.Value<String> malusCommandParameter;
    private final QuestionModifier questionModifier;
    private final QuestionPool questionPool;
    private final QuestionRegister questionRegister;

    public SetQuestionMalusRemoveCommandsExecutor(final Parameter.Value<Question> specificQuestionParameter, final Parameter.Value<String> malusCommandParameter, final QuestionModifier questionModifier, final QuestionPool questionPool, final QuestionRegister questionRegister) {
        this.specificQuestionParameter = specificQuestionParameter;
        this.malusCommandParameter = malusCommandParameter;
        this.questionModifier = questionModifier;
        this.questionPool = questionPool;
        this.questionRegister = questionRegister;
    }

    @Override
    public CommandResult execute(final CommandContext context) throws CommandException {
        final Question question = context.requireOne(this.specificQuestionParameter);
        final Collection<String> commands = Collections.unmodifiableCollection(context.all(this.malusCommandParameter));
        try {
            final Question modifiedQuestion = this.questionModifier.remove(question, QuestionComponent.MALUS_COMMANDS, commands);
            this.questionRegister.replace(question, modifiedQuestion);
            this.questionPool.replace(question, modifiedQuestion);
            if(commands.size() == 1) {
                context.sendMessage(TextUtils.composed("Command ", commands.iterator().next(), " removed !"));
            } else {
                context.sendMessage(TextUtils.composed("Commands ", String.join(", ", commands), " removed !"));
            }
            return CommandResult.success();
        } catch (final QuestionException | IllegalArgumentException e) {
            return CommandResult.error(TextUtils.errorWithPrefix(e.getMessage()));
        } catch (final IOException e) {
            return CommandResult.error(TextUtils.errorWithPrefix("An error occurred while trying to save the question. See the log for details."));
        }
    }
}
