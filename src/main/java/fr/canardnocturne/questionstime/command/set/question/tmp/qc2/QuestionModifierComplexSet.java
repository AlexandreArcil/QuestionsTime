package fr.canardnocturne.questionstime.command.set.question.tmp.qc2;

import fr.canardnocturne.questionstime.question.Question;
import fr.canardnocturne.questionstime.question.ask.pool.QuestionPool;
import fr.canardnocturne.questionstime.question.save.QuestionRegister;

import java.io.IOException;

public class QuestionModifierComplexSet<B, T, V> extends QuestionModifierComplex<B, T, V> {

    private final QuestionComponentComplexBase<B, T, V> questionComponent;

    public QuestionModifierComplexSet(final QuestionRegister questionRegister, final QuestionPool questionPool,
                                      final QuestionComponentComplexBase<B, T, V> questionComponent) {
        super(questionRegister, questionPool);
        this.questionComponent = questionComponent;
    }

    @Override
    protected void modify(final T type, final B builder, final V value) {
        this.questionComponent.setComponent.accept(builder, value);
    }
}
