package fr.canardnocturne.questionstime.command.set.question.tmp;

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

public class SetQuestionAddComponentExecutor implements CommandExecutor  {

    public static final Parameter.Value<String> COMPONENT = Parameter.string().consumeAllRemaining().key("component").build();

    private final Parameter.Value<Question> specificQuestionParameter;
    private final QuestionModifier questionModifier;
    private final QuestionPool questionPool;
    private final QuestionRegister questionRegister;
    private final Function<Question, Collection<String>> componentExtractor;
    private final QuestionComponent questionComponent;
    private final Parameter.Value<String> componentParameter;

    public SetQuestionAddComponentExecutor(final Parameter.Value<Question> specificQuestionParameter,
                                           final QuestionModifier questionModifier, final QuestionPool questionPool,
                                           final QuestionRegister questionRegister,
                                           final Function<Question, Collection<String>> componentExtractor,
                                           final QuestionComponent questionComponent, final Parameter.Value<String> componentParameter) {
        this.specificQuestionParameter = specificQuestionParameter;
        this.questionModifier = questionModifier;
        this.questionPool = questionPool;
        this.questionRegister = questionRegister;
        this.componentExtractor = componentExtractor;
        this.questionComponent = questionComponent;
        this.componentParameter = componentParameter;
    }

    @Override
    public CommandResult execute(final CommandContext context) throws CommandException {
        final Question question = context.requireOne(this.specificQuestionParameter);
        final Collection<String> components = Collections.unmodifiableCollection(context.all(this.componentParameter));
        try {
            final Collection<String> existingComponents = this.componentExtractor.apply(question);
            final Question modifiedQuestion = this.questionModifier.add(question, this.questionComponent, components);
            this.questionRegister.replace(question, modifiedQuestion);
            this.questionPool.replace(question, modifiedQuestion);
            final Collection<String> componentsSet = this.componentExtractor.apply(modifiedQuestion);
            final Set<String> newComponentsSet = componentsSet.stream()
                    .filter(componentSet -> !existingComponents.contains(componentSet)).collect(Collectors.toSet());
            if(newComponentsSet.size() == 1) {
                context.sendMessage(TextUtils.composed(StringUtils.capitalize(this.questionComponent.getSingular()) + " ", newComponentsSet.iterator().next(), " added !"));
            } else {
                context.sendMessage(TextUtils.composed(StringUtils.capitalize(this.questionComponent.getPlural()) + " ", String.join(", ", newComponentsSet), " added !"));
            }
            return CommandResult.success();
        } catch (final QuestionException | IllegalArgumentException e) {
            return CommandResult.error(TextUtils.errorWithPrefix(e.getMessage()));
        } catch (final IOException e) {
            return CommandResult.error(TextUtils.errorWithPrefix("An error occurred while trying to save the question. See the log for details."));
        }
    }
}
