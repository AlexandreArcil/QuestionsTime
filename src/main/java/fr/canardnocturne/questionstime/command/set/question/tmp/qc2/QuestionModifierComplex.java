package fr.canardnocturne.questionstime.command.set.question.tmp.qc2;

import fr.canardnocturne.questionstime.question.Question;
import fr.canardnocturne.questionstime.question.ask.pool.QuestionPool;
import fr.canardnocturne.questionstime.question.save.QuestionRegister;

import java.io.IOException;

public abstract class QuestionModifierComplex<B, T, V> {

    private final QuestionRegister questionRegister;
    private final QuestionPool questionPool;

    public QuestionModifierComplex(final QuestionRegister questionRegister, final QuestionPool questionPool) {
        this.questionRegister = questionRegister;
        this.questionPool = questionPool;
    }

    Question set(final ComponentTransform<T, B> componentTransform, final V value) throws IOException {
        final B builder = componentTransform.toBuilder();
        final T type = componentTransform.getType();
        this.modify(type, builder, value);
        final Question modifiedQuestion = componentTransform.build(builder);
        final Question question = componentTransform.getQuestion();
        this.questionRegister.replace(question, modifiedQuestion);
        this.questionPool.replace(question, modifiedQuestion);
        return modifiedQuestion;
    }

    protected abstract void modify(final T type, final B builder, final V value);

}
