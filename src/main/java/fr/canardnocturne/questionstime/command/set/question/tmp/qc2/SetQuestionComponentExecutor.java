package fr.canardnocturne.questionstime.command.set.question.tmp.qc2;

import fr.canardnocturne.questionstime.QuestionException;
import fr.canardnocturne.questionstime.question.Question;
import fr.canardnocturne.questionstime.util.TextUtils;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;

import java.io.IOException;

public abstract class SetQuestionComponentExecutor<B, T, V> {

    private final QuestionModifierComplex<B, T, V> questionModifier;
    protected final QuestionComponentComplexBase<B, T, V> questionComponent;

    public SetQuestionComponentExecutor(final QuestionModifierComplex<B, T, V> questionModifier,
                                      final QuestionComponentComplexBase<B, T, V> questionComponent) {
        this.questionModifier = questionModifier;
        this.questionComponent = questionComponent;
    }

    public CommandResult execute(final CommandContext context, final ComponentTransform<T, B> componentTransform, final V value) throws CommandException {
        try {
            this.questionModifier.set(componentTransform, value);
            final String componentName = StringUtils.capitalize(this.questionComponent.getSingular());
            context.sendMessage(TextUtils.composed( componentName + " sets to ", String.valueOf(value), " !"));
            return CommandResult.success();
        } catch (final QuestionException | IllegalArgumentException e) {
            return CommandResult.error(TextUtils.errorWithPrefix(e.getMessage()));
        } catch (final IOException e) {
            return CommandResult.error(TextUtils.errorWithPrefix("An error occurred while trying to save the question. See the log for details."));
        }
    }

}
