package fr.canardnocturne.questionstime.command.set.question.tmp.qc2;

import fr.canardnocturne.questionstime.question.Question;
import fr.canardnocturne.questionstime.question.component.Malus;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;

import java.util.Collection;
import java.util.Collections;

public class SetQuestionRemoveMalusComponentComplex<W, V extends Collection<W>> extends SetQuestionRemoveCollectionComponentComplex<Malus.Builder, Malus, W, V> implements CommandExecutor {

    private final Parameter.Value<Question> specificQuestionParameter;
    private final Parameter.Value<W> removeComponentParameter;

    public SetQuestionRemoveMalusComponentComplex(final Parameter.Value<Question> specificQuestionParameter,
                                                  final Parameter.Value<W> removeComponentParameter,
                                                  final QuestionModifierComplexRemove<Malus.Builder, Malus, W, V> questionModifier,
                                                  final QuestionComponentComplexCollection<Malus.Builder, Malus, W, V> questionComponent) {
        super(questionModifier, questionComponent);
        this.specificQuestionParameter = specificQuestionParameter;
        this.removeComponentParameter = removeComponentParameter;
    }

    @Override
    public CommandResult execute(final CommandContext context) throws CommandException {
        final Question question = context.requireOne(this.specificQuestionParameter);
        final V components = (V) Collections.unmodifiableCollection(context.all(this.removeComponentParameter));
        final MalusComponentTransform transform = new MalusComponentTransform(question);
        return this.execute(context, transform, components);
    }
}
