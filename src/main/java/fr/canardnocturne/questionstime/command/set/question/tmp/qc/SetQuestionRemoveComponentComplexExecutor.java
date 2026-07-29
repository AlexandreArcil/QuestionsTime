package fr.canardnocturne.questionstime.command.set.question.tmp.qc;

import fr.canardnocturne.questionstime.QuestionException;
import fr.canardnocturne.questionstime.question.Question;
import fr.canardnocturne.questionstime.question.QuestionComponent;
import fr.canardnocturne.questionstime.question.ask.pool.QuestionPool;
import fr.canardnocturne.questionstime.question.modifier.QuestionModifier;
import fr.canardnocturne.questionstime.question.save.QuestionRegister;
import fr.canardnocturne.questionstime.util.TextUtils;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SetQuestionRemoveComponentComplexExecutor implements CommandExecutor {

    private final Parameter.Value<Question> specificQuestionParameter;
    private final Parameter.Value<String> componentParameter;
    private final QuestionModifierRemoveComplex questionModifier;
    private final ListQuestionComponent questionComponent;

    public SetQuestionRemoveComponentComplexExecutor(final Parameter.Value<Question> specificQuestionParameter,
                                                     final Parameter.Value<String> componentParameter,
                                                     final QuestionModifierRemoveComplex questionModifier,
                                                     final ListQuestionComponent questionComponent) {
        this.specificQuestionParameter = specificQuestionParameter;
        this.componentParameter = componentParameter;
        this.questionModifier = questionModifier;
        this.questionComponent = questionComponent;
    }

    @Override
    public CommandResult execute(final CommandContext context) throws CommandException {
        final Question question = context.requireOne(this.specificQuestionParameter);
        final Collection<String> components = Collections.unmodifiableCollection(context.all(this.componentParameter));
        try {
            final Question modifiedQuestion = this.questionModifier.remove(question, this.questionComponent, components);
            final Collection<String> modifiedComponents = this.questionComponent.getComponentExtractor().apply(modifiedQuestion);
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
