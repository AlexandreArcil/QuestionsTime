package fr.canardnocturne.questionstime.command.set.question.tmp.qc2;

import fr.canardnocturne.questionstime.question.Question;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;

public class SetQuestionComponentQuestionExecutor<V> extends SetQuestionComponentExecutor<Question.QuestionBuilder, Question, V> implements CommandExecutor {

    private final Parameter.Value<Question> specificQuestionParameter;

    public SetQuestionComponentQuestionExecutor(final Parameter.Value<Question> specificQuestionParameter,
                                                final QuestionModifierComplex<Question.QuestionBuilder, Question, V> questionModifier,
                                                final QuestionComponentComplexBase<Question.QuestionBuilder, Question, V> questionComponent) {
        super(questionModifier, questionComponent);
        this.specificQuestionParameter = specificQuestionParameter;
    }

    @Override
    public CommandResult execute(final CommandContext context) throws CommandException {
        final Question question = context.requireOne(this.specificQuestionParameter);
        final V value = this.questionComponent.get(context);
        final QuestionComponentTransform transform = new QuestionComponentTransform(question);
        return this.execute(context, transform, value);
    }

}
