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

public class SetQuestionBooleanExecutor implements CommandExecutor {

    public static final Parameter.Value<Boolean> VALUE = Parameter.bool().key("value").build();

    private final Parameter.Value<Question> specificQuestionParameter;
    private final QuestionModifierComplex questionModifier;
    private final BooleanQuestionComponent questionComponent;

    public SetQuestionBooleanExecutor(final Parameter.Value<Question> specificQuestionParameter, final QuestionModifierComplex questionModifier,
                                      final BooleanQuestionComponent questionComponent) {
        this.specificQuestionParameter = specificQuestionParameter;
        this.questionModifier = questionModifier;
        this.questionComponent = questionComponent;
    }

    @Override
    public CommandResult execute(final CommandContext context) throws CommandException {
        final Question question = context.requireOne(this.specificQuestionParameter);
        final boolean value = context.requireOne(VALUE);
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
