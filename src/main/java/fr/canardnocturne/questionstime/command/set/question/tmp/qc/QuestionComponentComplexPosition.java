package fr.canardnocturne.questionstime.command.set.question.tmp.qc;

import fr.canardnocturne.questionstime.question.Question;
import org.apache.logging.log4j.util.TriConsumer;

public abstract class QuestionComponentComplexPosition<T> extends QuestionComponentComplexBase<T> {

    private final TriConsumer<Question.QuestionBuilder, Integer, T> setComponent;

    public QuestionComponentComplexPosition(final String componentName, final String commandDescription,
                                            final TriConsumer<Question.QuestionBuilder, Integer, T> setComponent) {
        super(componentName, commandDescription);
        this.setComponent = setComponent;
    }

    public TriConsumer<Question.QuestionBuilder, Integer, T> getSetComponent() {
        return setComponent;
    }
}
