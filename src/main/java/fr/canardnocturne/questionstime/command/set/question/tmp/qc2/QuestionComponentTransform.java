package fr.canardnocturne.questionstime.command.set.question.tmp.qc2;

import fr.canardnocturne.questionstime.question.Question;

public class QuestionComponentTransform extends ComponentTransform<Question, Question.QuestionBuilder> {

    public QuestionComponentTransform(final Question question) {
        super(question);
    }

    @Override
    Question getType() {
        return this.question;
    }

    @Override
    public Question.QuestionBuilder toBuilder() {
        return question.toBuilder();
    }

    @Override
    public Question build(final Question.QuestionBuilder builder) {
        return builder.build();
    }

}
