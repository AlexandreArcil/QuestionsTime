package fr.canardnocturne.questionstime.command.set.question.tmp.qc3.section.transform;

import fr.canardnocturne.questionstime.question.Question;

public class QuestionSectionTransform extends SectionTransform<Question.QuestionBuilder, Question> {

    public QuestionSectionTransform(final Question question) {
        super(question);
    }

    @Override
    public Question.QuestionBuilder toBuilder() {
        return question.toBuilder();
    }

    @Override
    public Question build(final Question.QuestionBuilder builder) {
        return builder.build();
    }

    @Override
    public Question getType() {
        return this.question;
    }

}
