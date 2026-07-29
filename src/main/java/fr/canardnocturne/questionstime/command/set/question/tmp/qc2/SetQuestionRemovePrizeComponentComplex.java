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
import java.util.Collections;

public class SetQuestionRemovePrizeComponentComplex<W, V extends Collection<W>> extends SetQuestionRemoveCollectionComponentComplex<Prize.Builder, Prize, W, V> implements CommandExecutor {

    private static final Parameter.Value<Integer> POSITION = Parameter.integerNumber().key("position").build();

    private final Parameter.Value<Question> specificQuestionParameter;
    private final Parameter.Value<W> removeComponentParameter;

    public SetQuestionRemovePrizeComponentComplex(final Parameter.Value<Question> specificQuestionParameter,
                                                  final Parameter.Value<W> removeComponentParameter,
                                                  final QuestionModifierComplexRemove<Prize.Builder, Prize, W, V> questionModifier,
                                                  final QuestionComponentComplexCollection<Prize.Builder, Prize, W, V> questionComponent) {
        super(questionModifier, questionComponent);
        this.specificQuestionParameter = specificQuestionParameter;
        this.removeComponentParameter = removeComponentParameter;
    }

    @Override
    public CommandResult execute(final CommandContext context) throws CommandException {
        final Question question = context.requireOne(this.specificQuestionParameter);
        final int position = context.requireOne(POSITION);
        final V components = (V) Collections.unmodifiableCollection(context.all(this.removeComponentParameter));
        final PrizeTransform transform = new PrizeTransform(question, position);
        return this.execute(context, transform, components);
    }
}
