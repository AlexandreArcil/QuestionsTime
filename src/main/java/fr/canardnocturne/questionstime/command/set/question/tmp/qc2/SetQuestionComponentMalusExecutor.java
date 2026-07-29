package fr.canardnocturne.questionstime.command.set.question.tmp.qc2;

import fr.canardnocturne.questionstime.question.Question;
import fr.canardnocturne.questionstime.question.component.Malus;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;

public class SetQuestionComponentMalusExecutor<V> extends SetQuestionComponentExecutor<Malus.Builder, Malus, V> implements CommandExecutor {

    private final Parameter.Value<Question> specificQuestionParameter;

    public SetQuestionComponentMalusExecutor(final Parameter.Value<Question> specificQuestionParameter,
                                             final QuestionModifierComplex<Malus.Builder, Malus, V> questionModifier,
                                             final QuestionComponentComplexBase<Malus.Builder, Malus, V> questionComponent) {
        super(questionModifier, questionComponent);
        this.specificQuestionParameter = specificQuestionParameter;
    }

    @Override
    public CommandResult execute(final CommandContext context) throws CommandException {
        final Question question = context.requireOne(this.specificQuestionParameter);
        final V value = this.questionComponent.get(context);
        final MalusComponentTransform transform = new MalusComponentTransform(question);
        return this.execute(context, transform, value);
    }

}
