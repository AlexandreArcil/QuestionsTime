package fr.canardnocturne.questionstime.command.set.question.tmp.qc3.section.transform;

import fr.canardnocturne.questionstime.question.Question;

public abstract class SectionTransform<B, T> {

    public final Question question;

    protected SectionTransform(final Question question) {
        this.question = question;
    }

    public abstract B toBuilder();

    public abstract Question build(final B builder);

    public abstract T getType();

    public Question getQuestion() {
        return question;
    }

}
