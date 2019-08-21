package fr.canardnocturne.questionstime.question.ask.picker;

import fr.canardnocturne.questionstime.question.ask.pool.QuestionPool;
import fr.canardnocturne.questionstime.question.type.Question;

public abstract class QuestionPicker {

    protected final QuestionPool questionPool;

    protected QuestionPicker(final QuestionPool questionPool) {
        this.questionPool = questionPool;
    }

    public abstract Question pick();

}
