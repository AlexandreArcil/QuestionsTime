package fr.canardnocturne.questionstime.command.set.question.tmp.qc2;

import fr.canardnocturne.questionstime.question.Question;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;

import java.util.Collection;

public class SetQuestionAddQuestionComponentComplexExecutor<W, V extends Collection<W>> extends SetQuestionAddCollectionComponentComplex<Question.QuestionBuilder, Question, W, V> implements CommandExecutor {

    private final Parameter.Value<Question> specificQuestionParameter;

    public SetQuestionAddQuestionComponentComplexExecutor(final Parameter.Value<Question> specificQuestionParameter,
                                                          final QuestionModifierComplexAdd<Question.QuestionBuilder, Question, W, V> questionModifier,
                                                          final QuestionComponentComplexCollection<Question.QuestionBuilder, Question, W, V> questionComponent) {
        super(questionModifier, questionComponent);
        this.specificQuestionParameter = specificQuestionParameter;
    }

    @Override
    public CommandResult execute(final CommandContext context) throws CommandException {
        final Question question = context.requireOne(this.specificQuestionParameter);
        final V components = this.questionComponent.get(context);
        final QuestionComponentTransform transform = new QuestionComponentTransform(question);
        return this.execute(context, transform, components);
    }
}
