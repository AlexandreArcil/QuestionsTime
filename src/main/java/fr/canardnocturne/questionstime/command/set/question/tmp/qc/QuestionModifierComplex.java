package fr.canardnocturne.questionstime.command.set.question.tmp.qc;

import fr.canardnocturne.questionstime.question.Question;
import fr.canardnocturne.questionstime.question.ask.pool.QuestionPool;
import fr.canardnocturne.questionstime.question.save.QuestionRegister;

import java.io.IOException;

public class QuestionModifierComplex {

    private final QuestionRegister questionRegister;
    private final QuestionPool questionPool;

    public QuestionModifierComplex(final QuestionRegister questionRegister, final QuestionPool questionPool) {
        this.questionRegister = questionRegister;
        this.questionPool = questionPool;
    }

    <T> Question set(final Question question, final QuestionComponentComplex<T> questionComponent, final T value) throws IOException {
        final Question.QuestionBuilder builder = question.toBuilder();
        questionComponent.getSetComponent().accept(builder, value);
        final Question modifiedQuestion = builder.build();
        this.questionRegister.replace(question, modifiedQuestion);
        this.questionPool.replace(question, modifiedQuestion);
        return question;
    }

}
