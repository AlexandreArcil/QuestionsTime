package fr.canardnocturne.questionstime.command.set.question.tmp.qc;

import fr.canardnocturne.questionstime.QuestionException;
import fr.canardnocturne.questionstime.question.Question;
import fr.canardnocturne.questionstime.util.TextUtils;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;

import java.io.IOException;

public class SetQuestionStringExecutor implements CommandExecutor {

    public static final Parameter.Value<String> VALUE = Parameter.string().key("value").build();
    private final Parameter.Value<Question> specificQuestionParameter;
    private final QuestionModifierComplex questionModifier;
    private final StringQuestionComponent questionComponent;

    public SetQuestionStringExecutor(final Parameter.Value<Question> specificQuestionParameter, final QuestionModifierComplex questionModifier, final StringQuestionComponent questionComponent) {
        this.specificQuestionParameter = specificQuestionParameter;
        this.questionModifier = questionModifier;
        this.questionComponent = questionComponent;
    }

    @Override
    public CommandResult execute(final CommandContext context) throws CommandException {
        final Question question = context.requireOne(this.specificQuestionParameter);
        final String value = context.requireOne(VALUE);
        try {
            this.questionModifier.set(question, this.questionComponent, value);
            final String componentName = StringUtils.capitalize(this.questionComponent.getName());
            context.sendMessage(TextUtils.composed( componentName + " sets to ", String.valueOf(value), " !"));
            return CommandResult.success();
        } catch (final QuestionException | IllegalArgumentException e) {
            return CommandResult.error(TextUtils.errorWithPrefix(e.getMessage()));
        } catch (final IOException e) {
            return CommandResult.error(TextUtils.errorWithPrefix("An error occurred while trying to save the question. See the log for details."));
        }
    }

}
