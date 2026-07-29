package fr.canardnocturne.questionstime.command.set.question.tmp.qc2;

import fr.canardnocturne.questionstime.question.Question;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;

import java.util.Collection;

public class SetQuestionListComponentComplexQuestionExecutor<W, V extends Collection<W>> extends SetQuestionListComponentComplexExecutor<Question.QuestionBuilder, Question, W, V> implements CommandExecutor {

    private final Parameter.Value<Question> specificQuestionParameter;

    public SetQuestionListComponentComplexQuestionExecutor(final Parameter.Value<Question> specificQuestionParameter,
                                                           final QuestionComponentComplexCollection<Question.QuestionBuilder, Question, W, V> questionComponent) {
        super(questionComponent);
        this.specificQuestionParameter = specificQuestionParameter;
    }

    @Override
    public CommandResult execute(final CommandContext context) throws CommandException {
        final Question question = context.requireOne(specificQuestionParameter);
        final QuestionComponentTransform transform = new QuestionComponentTransform(question);
        final V componentValues = questionComponent.getComponent.apply(transform.getType());
        return this.execute(context, question.getQuestion(), componentValues);
    }

}
