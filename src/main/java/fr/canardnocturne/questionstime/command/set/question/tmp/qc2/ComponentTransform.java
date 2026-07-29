package fr.canardnocturne.questionstime.command.set.question.tmp.qc2;

import fr.canardnocturne.questionstime.question.Question;

public abstract class ComponentTransform<T, B> {

    protected final Question question;

    protected ComponentTransform(final Question question) {
        this.question = question;
    }

    abstract T getType();

    abstract B toBuilder();

    abstract Question build(final B builder);

    public Question getQuestion() {
        return this.question;
    }

}
