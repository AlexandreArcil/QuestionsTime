package fr.canardnocturne.questionstime.command.set.question.tmp.qc2;

import fr.canardnocturne.questionstime.QuestionException;
import fr.canardnocturne.questionstime.util.TextUtils;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;

import java.io.IOException;
import java.util.Collection;

public class SetQuestionRemoveCollectionComponentComplex<B, T, W, V extends Collection<W>> {

    private final QuestionModifierComplexRemove<B, T, W, V> questionModifier;
    protected final QuestionComponentComplexCollection<B, T, W, V> questionComponent;

    public SetQuestionRemoveCollectionComponentComplex(final QuestionModifierComplexRemove<B, T, W, V> questionModifier,
                                                       final QuestionComponentComplexCollection<B, T, W, V> questionComponent) {
        this.questionModifier = questionModifier;
        this.questionComponent = questionComponent;
    }

    public CommandResult execute(final CommandContext context, final ComponentTransform<T, B> componentTransform, final V values) throws CommandException {
        try {
            this.questionModifier.set(componentTransform, values);
            final Collection<String> modifiedComponents = values.stream().map(this.questionComponent.getValueUnmapper()).toList();
            if(modifiedComponents.size() == 1) {
                context.sendMessage(TextUtils.composed(StringUtils.capitalize(this.questionComponent.getSingular()) + " ", modifiedComponents.iterator().next(), " removed !"));
            } else {
                context.sendMessage(TextUtils.composed(StringUtils.capitalize(this.questionComponent.getPlural()) + " ", String.join(", ", modifiedComponents), " removed !"));
            }
            return CommandResult.success();
        } catch (final QuestionException | IllegalArgumentException e) {
            return CommandResult.error(TextUtils.errorWithPrefix(e.getMessage()));
        } catch (final IOException e) {
            return CommandResult.error(TextUtils.errorWithPrefix("An error occurred while trying to save the question. See the log for details."));
        }
    }
}
