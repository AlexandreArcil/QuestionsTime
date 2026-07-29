package fr.canardnocturne.questionstime.command.set.question.tmp.qc2;

import fr.canardnocturne.questionstime.question.Question;
import fr.canardnocturne.questionstime.question.component.Malus;

public class MalusComponentTransform extends ComponentTransform<Malus, Malus.Builder> {

    public MalusComponentTransform(final Question question) {
        super(question);
    }

    @Override
    Malus getType() {
        return this.question.getMalus().orElse(Malus.builder().build());
    }

    @Override
    public Malus.Builder toBuilder() {
        return question.getMalus()
                .map(Malus::toBuilder)
                .orElseGet(Malus::builder);
    }

    @Override
    public Question build(final Malus.Builder builder) {
        final Question.QuestionBuilder questionBuilder = question.toBuilder();
        questionBuilder.setMalus(builder.build());
        return questionBuilder.build();
    }

}
