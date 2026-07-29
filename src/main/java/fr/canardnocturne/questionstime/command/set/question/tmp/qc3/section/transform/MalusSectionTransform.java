package fr.canardnocturne.questionstime.command.set.question.tmp.qc3.section.transform;

import fr.canardnocturne.questionstime.question.Question;
import fr.canardnocturne.questionstime.question.component.Malus;

public class MalusSectionTransform extends SectionTransform<Malus.Builder, Malus> {

    public MalusSectionTransform(final Question question) {
        super(question);
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

    @Override
    public Malus getType() {
        return this.question.getMalus().orElse(Malus.builder().build());
    }

}
