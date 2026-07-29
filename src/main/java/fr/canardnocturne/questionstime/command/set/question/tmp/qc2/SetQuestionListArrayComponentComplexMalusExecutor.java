package fr.canardnocturne.questionstime.command.set.question.tmp.qc2;

import fr.canardnocturne.questionstime.question.Question;
import fr.canardnocturne.questionstime.question.component.Malus;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;

public class SetQuestionListArrayComponentComplexMalusExecutor<V> extends SetQuestionListArrayComponentComplexExecutor<Malus.Builder, Malus, V> implements CommandExecutor {

    private final Parameter.Value<Question> specificQuestionParameter;

    public SetQuestionListArrayComponentComplexMalusExecutor(final Parameter.Value<Question> specificQuestionParameter, final QuestionComponentComplexMalusArray<V> questionComponent) {
        super(questionComponent);
        this.specificQuestionParameter = specificQuestionParameter;
    }

    @Override
    public CommandResult execute(final CommandContext context) throws CommandException {
        final Question question = context.requireOne(specificQuestionParameter);
        final MalusComponentTransform transform = new MalusComponentTransform(question);
        final V[] componentValues = questionComponent.getComponent.apply(transform.getType());
        return this.execute(context, question.getQuestion(), componentValues);
    }
}
