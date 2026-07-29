package fr.canardnocturne.questionstime.command.set.question.tmp.qc3.set;

import fr.canardnocturne.questionstime.QuestionException;
import fr.canardnocturne.questionstime.command.set.question.tmp.qc3.component.QuestionComponentBase;
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

public class SetQuestionComponentSetExecutor<B, T, V> implements CommandExecutor {

    private final Parameter.Value<Question> specificQuestionParameter;
    private final QuestionPool questionPool;
    private final QuestionRegister questionRegister;
    private final QuestionComponentBase<V> questionComponentBase;
    private final QuestionSectionBase<T, B, V> questionSectionBase;

    public SetQuestionComponentSetExecutor(final Parameter.Value<Question> specificQuestionParameter,
                                           final QuestionPool questionPool, final QuestionRegister questionRegister,
                                           final QuestionComponentBase<V> questionComponentBase,
                                           final QuestionSectionBase<T, B, V> questionSectionBase) {
        this.specificQuestionParameter = specificQuestionParameter;
        this.questionPool = questionPool;
        this.questionRegister = questionRegister;
        this.questionComponentBase = questionComponentBase;
        this.questionSectionBase = questionSectionBase;
    }

    @Override
    public CommandResult execute(final CommandContext context) throws CommandException {
        try {
            final Question question = context.requireOne(this.specificQuestionParameter);
            final V value = this.questionComponentBase.get(context);
            final SectionTransform<B, T> transform = this.questionSectionBase.createTransform(context, question);
            final B builder = transform.toBuilder();
            this.questionSectionBase.getSetComponent().accept(builder, value);
            final Question modifiedQuestion = transform.build(builder);
            this.questionRegister.replace(question, modifiedQuestion);
            this.questionPool.replace(question, modifiedQuestion);
            final String componentName = StringUtils.capitalize(this.questionComponentBase.getSingular());
            context.sendMessage(TextUtils.composed( componentName + " sets to ", String.valueOf(value), " !"));
            return CommandResult.success();
        } catch (final QuestionException | IllegalArgumentException e) {
            return CommandResult.error(TextUtils.errorWithPrefix(e.getMessage()));
        } catch (final IOException e) {
            return CommandResult.error(TextUtils.errorWithPrefix("An error occurred while trying to save the question. See the log for details."));
        }
    }

}
