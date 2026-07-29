package fr.canardnocturne.questionstime.command.set.question.tmp.qc3.set;

import fr.canardnocturne.questionstime.QuestionException;
import fr.canardnocturne.questionstime.command.set.question.tmp.qc3.component.QuestionComponentCollection;
import fr.canardnocturne.questionstime.command.set.question.tmp.qc3.section.QuestionSectionBase;
import fr.canardnocturne.questionstime.command.set.question.tmp.qc3.section.transform.SectionTransform;
import fr.canardnocturne.questionstime.question.Question;
import fr.canardnocturne.questionstime.question.ask.pool.QuestionPool;
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

public class SetQuestionComponentAddExecutor<B, T, W, V extends Collection<W>> implements CommandExecutor {

    private final Parameter.Value<Question> specificQuestionParameter;
    private final Parameter.Value<String> valuesParameter;
    private final QuestionPool questionPool;
    private final QuestionRegister questionRegister;
    private final QuestionComponentCollection<W, V> questionComponentBase;
    private final QuestionSectionBase<T, B, V> questionSectionBase;

    public SetQuestionComponentAddExecutor(final Parameter.Value<Question> specificQuestionParameter,
                                           final QuestionPool questionPool, final QuestionRegister questionRegister,
                                           final QuestionComponentCollection<W, V> questionComponentBase,
                                           final QuestionSectionBase<T, B, V> questionSectionBase) {
        this(specificQuestionParameter, null, questionPool, questionRegister, questionComponentBase, questionSectionBase);
    }

    public SetQuestionComponentAddExecutor(final Parameter.Value<Question> specificQuestionParameter,
                                           final Parameter.Value<String> valuesParameter,
                                           final QuestionPool questionPool, final QuestionRegister questionRegister,
                                           final QuestionComponentCollection<W, V> questionComponentBase,
                                           final QuestionSectionBase<T, B, V> questionSectionBase) {
        this.specificQuestionParameter = specificQuestionParameter;
        this.valuesParameter = valuesParameter;
        this.questionPool = questionPool;
        this.questionRegister = questionRegister;
        this.questionComponentBase = questionComponentBase;
        this.questionSectionBase = questionSectionBase;
    }

    @Override
    public CommandResult execute(final CommandContext context) throws CommandException {
        try {
            final Question question = context.requireOne(this.specificQuestionParameter);
            final V values = this.valuesParameter == null ? this.questionComponentBase.get(context)
                    : (V) context.all(this.valuesParameter).stream()
                    .map(this.questionComponentBase.getValueMapper()).toList();
            final SectionTransform<B, T> transform = this.questionSectionBase.createTransform(context, question);
            final V currentValues = this.questionSectionBase.getGetComponent().apply(transform.getType());
            final V modifiedValues = this.questionComponentBase.addAll(currentValues, values);
            final B builder = transform.toBuilder();
            this.questionSectionBase.getSetComponent().accept(builder, modifiedValues);
            final Question modifiedQuestion = transform.build(builder);
            this.questionRegister.replace(question, modifiedQuestion);
            this.questionPool.replace(question, modifiedQuestion);
            final Collection<String> modifiedComponents = values.stream().map(this.questionComponentBase.getValueUnmapper()).toList();
            if(modifiedComponents.size() == 1) {
                context.sendMessage(TextUtils.composed(StringUtils.capitalize(this.questionComponentBase.getSingular()) + " ", modifiedComponents.iterator().next(), " added !"));
            } else {
                context.sendMessage(TextUtils.composed(StringUtils.capitalize(this.questionComponentBase.getPlural()) + " ", String.join(", ", modifiedComponents), " added !"));
            }
            return CommandResult.success();
        } catch (final QuestionException | IllegalArgumentException e) {
            return CommandResult.error(TextUtils.errorWithPrefix(e.getMessage()));
        } catch (final IOException e) {
            return CommandResult.error(TextUtils.errorWithPrefix("An error occurred while trying to save the question. See the log for details."));
        }
    }

}
