package fr.canardnocturne.questionstime.command.set.question.tmp.qc;

import fr.canardnocturne.questionstime.question.Question;
import fr.canardnocturne.questionstime.question.component.Malus;

import java.util.function.BiConsumer;

public class BooleanQuestionMalusComponent extends BooleanQuestionComponent {

    public BooleanQuestionMalusComponent(final String componentName, final String commandDescription,
                                         final BiConsumer<Malus.Builder, Boolean> malusSetComponent) {
        super(componentName, commandDescription, createQuestionSetComponent(malusSetComponent));
    }

    private static BiConsumer<Question.QuestionBuilder, Boolean> createQuestionSetComponent(final BiConsumer<Malus.Builder, Boolean> malusSetComponent) {
        return (question, value) -> {
            final Malus.Builder malus = question.getMalus()
                    .map(Malus::toBuilder)
                    .orElseGet(Malus::builder);
            malusSetComponent.accept(malus, value);
            question.setMalus(malus.build());
        };
    }

}
