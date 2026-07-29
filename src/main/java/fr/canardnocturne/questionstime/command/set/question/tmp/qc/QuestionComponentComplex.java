package fr.canardnocturne.questionstime.command.set.question.tmp.qc;

import fr.canardnocturne.questionstime.question.Question;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.parameter.Parameter;

import java.util.function.BiConsumer;

public abstract class QuestionComponentComplex<T> extends QuestionComponentComplexBase<T> {

    private final BiConsumer<Question.QuestionBuilder, T> setComponent;

    public QuestionComponentComplex(final String componentName, final String commandDescription,
                                    final BiConsumer<Question.QuestionBuilder, T> setComponent) {
        super(componentName, commandDescription);
        this.setComponent = setComponent;
    }

    public BiConsumer<Question.QuestionBuilder, T> getSetComponent() {
        return setComponent;
    }

}
