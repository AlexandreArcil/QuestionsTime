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
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SetQuestionAddComponentAtPositionExecutor implements CommandExecutor  {

    private static final Parameter.Value<String> COMPONENT = Parameter.string().consumeAllRemaining().key("component").build();

    private final Parameter.Value<Question> specificQuestionParameter;
    private final Parameter.Value<Integer> positionParameter;
    private final QuestionModifier questionModifier;
    private final QuestionPool questionPool;
    private final QuestionRegister questionRegister;
    private final Function<Question, Map<Integer, Collection<String>>> componentExtractor;
    private final QuestionComponent questionComponent;

    public SetQuestionAddComponentAtPositionExecutor(final Parameter.Value<Question> specificQuestionParameter, final Parameter.Value<Integer> positionParameter,
                                                     final QuestionModifier questionModifier, final QuestionPool questionPool,
                                                     final QuestionRegister questionRegister,
                                                     final Function<Question, Map<Integer, Collection<String>>> componentExtractor,
                                                     final QuestionComponent questionComponent) {
        this.specificQuestionParameter = specificQuestionParameter;
        this.positionParameter = positionParameter;
        this.questionModifier = questionModifier;
        this.questionPool = questionPool;
        this.questionRegister = questionRegister;
        this.componentExtractor = componentExtractor;
        this.questionComponent = questionComponent;
    }

    @Override
    public CommandResult execute(final CommandContext context) throws CommandException {
        final Question question = context.requireOne(this.specificQuestionParameter);
        final int position = context.requireOne(this.positionParameter);
        final Collection<String> components = Collections.unmodifiableCollection(context.all(COMPONENT));
        try {
            final Collection<String> existingComponents = this.componentExtractor.apply(question).get(position);
            final Question modifiedQuestion = this.questionModifier.add(question, this.questionComponent, position, components);
            this.questionRegister.replace(question, modifiedQuestion);
            this.questionPool.replace(question, modifiedQuestion);
            final Collection<String> componentsSet = this.componentExtractor.apply(modifiedQuestion).get(position);
            final Set<String> newComponentsSet = componentsSet.stream()
                    .filter(componentSet -> !existingComponents.contains(componentSet)).collect(Collectors.toSet());
            if(newComponentsSet.size() == 1) {
                context.sendMessage(TextUtils.composed(StringUtils.capitalize(this.questionComponent.getSingular()) + " ", newComponentsSet.iterator().next(), " added to position ", String.valueOf(position), " !"));
            } else {
                context.sendMessage(TextUtils.composed(StringUtils.capitalize(this.questionComponent.getPlural()) + " ", String.join(", ", newComponentsSet), " added to position ", String.valueOf(position), " !"));
            }
            return CommandResult.success();
        } catch (final QuestionException | IllegalArgumentException e) {
            return CommandResult.error(TextUtils.errorWithPrefix(e.getMessage()));
        } catch (final IOException e) {
            return CommandResult.error(TextUtils.errorWithPrefix("An error occurred while trying to save the question. See the log for details."));
        }
    }
}
