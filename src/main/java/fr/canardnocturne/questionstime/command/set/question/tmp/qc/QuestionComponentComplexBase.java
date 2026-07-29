package fr.canardnocturne.questionstime.command.set.question.tmp.qc;

import fr.canardnocturne.questionstime.question.Question;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.parameter.Parameter;

public abstract class QuestionComponentComplexBase<T> {

    private final String name;
    protected final String commandDescription;

    public QuestionComponentComplexBase(final String name, final String commandDescription) {
        this.name = name;
        this.commandDescription = commandDescription;
    }

    public abstract Command.Parameterized create(final Parameter.Value<Question> specificQuestionParameter,
                                                 final QuestionModifierComplex questionModifier);

    public String getName() {
        return name;
    }

}
