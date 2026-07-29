package fr.canardnocturne.questionstime.command.set.question.tmp.qc;

import fr.canardnocturne.questionstime.question.Question;
import fr.canardnocturne.questionstime.question.component.Prize;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class QuestionComponentComplexPrize<T> extends QuestionComponentComplexPosition<T> {

    public QuestionComponentComplexPrize(final String componentName, final String commandDescription,
                                         final BiConsumer<Prize.Builder, T> setComponent) {
        super(componentName, commandDescription, createSetQuestionComponent(setComponent));
    }

    private static <Q> TriConsumer<Question.QuestionBuilder, Integer, Q> createSetQuestionComponent(final BiConsumer<Prize.Builder, Q> setPrizeComponent) {
        return (builder, position, value) -> {
            final SortedSet<Prize> prizes = new TreeSet<>(builder.getPrizes());
            final Prize oldPrize = prizes.stream()
                    .filter(prize1 -> prize1.getPosition() == position)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Prize with position " + position + " not found"));
            final Prize.Builder newPrizeBuilder = oldPrize.toBuilder();
            setPrizeComponent.accept(newPrizeBuilder, value);
            prizes.remove(oldPrize);
            final Prize newPrize = newPrizeBuilder.build();
            if(!newPrize.isEmpty()) {
                prizes.add(newPrize);
            }
            builder.setPrizes(prizes);
        };
    }

}
