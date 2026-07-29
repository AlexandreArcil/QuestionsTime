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

public class SetQuestionAddPrizeComponentComplexExecutor<W, V extends Collection<W>> extends SetQuestionAddCollectionComponentComplex<Prize.Builder, Prize, W, V> implements CommandExecutor {

    private static final Parameter.Value<Integer> POSITION = Parameter.integerNumber().key("position").build();

    private final Parameter.Value<Question> specificQuestionParameter;

    public SetQuestionAddPrizeComponentComplexExecutor(final Parameter.Value<Question> specificQuestionParameter,
                                                       final QuestionModifierComplexAdd<Prize.Builder, Prize, W, V> questionModifier,
                                                       final QuestionComponentComplexCollection<Prize.Builder, Prize, W, V> questionComponent) {
        super(questionModifier, questionComponent);
        this.specificQuestionParameter = specificQuestionParameter;
    }

    @Override
    public CommandResult execute(final CommandContext context) throws CommandException {
        final Question question = context.requireOne(this.specificQuestionParameter);
        final int position = context.requireOne(POSITION);
        final V components = this.questionComponent.get(context);
        final PrizeTransform transform = new PrizeTransform(question, position);
        return this.execute(context, transform, components);
    }
}
