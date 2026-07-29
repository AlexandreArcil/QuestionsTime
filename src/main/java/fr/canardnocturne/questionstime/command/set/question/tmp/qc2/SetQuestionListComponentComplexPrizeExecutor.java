package fr.canardnocturne.questionstime.command.set.question.tmp.qc2;

import fr.canardnocturne.questionstime.question.Question;
import fr.canardnocturne.questionstime.question.component.Malus;
import fr.canardnocturne.questionstime.question.component.Prize;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;

import java.util.Collection;

public class SetQuestionListComponentComplexPrizeExecutor<W, V extends Collection<W>> extends SetQuestionListComponentComplexExecutor<Prize.Builder, Prize, W, V> implements CommandExecutor {

    private static final Parameter.Value<Integer> POSITION = Parameter.integerNumber().key("position").build();

    private final Parameter.Value<Question> specificQuestionParameter;

    public SetQuestionListComponentComplexPrizeExecutor(final Parameter.Value<Question> specificQuestionParameter,
                                                        final QuestionComponentComplexCollection<Prize.Builder, Prize, W, V> questionComponent) {
        super(questionComponent);
        this.specificQuestionParameter = specificQuestionParameter;
    }

    @Override
    public CommandResult execute(final CommandContext context) throws CommandException {
        final Question question = context.requireOne(specificQuestionParameter);
        final int position = context.requireOne(POSITION);
        final PrizeTransform transform = new PrizeTransform(question, position);
        final V componentValues = questionComponent.getComponent.apply(transform.getType());
        return this.execute(context, question.getQuestion(), componentValues);
    }

}
