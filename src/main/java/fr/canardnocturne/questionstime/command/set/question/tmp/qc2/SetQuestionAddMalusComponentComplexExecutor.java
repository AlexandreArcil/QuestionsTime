package fr.canardnocturne.questionstime.command.set.question.tmp.qc2;

import fr.canardnocturne.questionstime.question.Question;
import fr.canardnocturne.questionstime.question.component.Malus;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;

import java.util.Collection;

public class SetQuestionAddMalusComponentComplexExecutor<W, V extends Collection<W>> extends SetQuestionAddCollectionComponentComplex<Malus.Builder, Malus, W, V> implements CommandExecutor {

    private final Parameter.Value<Question> specificQuestionParameter;

    public SetQuestionAddMalusComponentComplexExecutor(final Parameter.Value<Question> specificQuestionParameter,
                                                       final QuestionModifierComplexAdd<Malus.Builder, Malus, W, V> questionModifier,
                                                       final QuestionComponentComplexCollection<Malus.Builder, Malus, W, V> questionComponent) {
        super(questionModifier, questionComponent);
        this.specificQuestionParameter = specificQuestionParameter;
    }

    @Override
    public CommandResult execute(final CommandContext context) throws CommandException {
        final Question question = context.requireOne(this.specificQuestionParameter);
        final V components = this.questionComponent.get(context);
        final MalusComponentTransform transform = new MalusComponentTransform(question);
        return this.execute(context, transform, components);
    }
}
