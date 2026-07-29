package fr.canardnocturne.questionstime.command.set.question.tmp.qc2;

import fr.canardnocturne.questionstime.question.Question;
import fr.canardnocturne.questionstime.question.component.Prize;

import java.util.TreeSet;

public class PrizeTransform extends ComponentTransform<Prize, Prize.Builder> {

    private final int position;

    public PrizeTransform(final Question question, final int position) {
        super(question);
        this.position = position;
    }

    @Override
    Prize getType() {
        return this.question.getPrizes().stream()
                .filter(prize -> prize.getPosition() == position)
                .findFirst()
                .orElse(Prize.builder(position).build());
    }

    @Override
    public Prize.Builder toBuilder() {
        return question.getPrizes().stream()
                .filter(prize -> prize.getPosition() == position)
                .findFirst()
                .map(Prize::toBuilder)
                .orElseGet(() -> Prize.builder(position));
    }

    @Override
    public Question build(final Prize.Builder builder) {
        final Question.QuestionBuilder questionBuilder = this.question.toBuilder();
        final TreeSet<Prize> prizes = new TreeSet<>(question.getPrizes());
        prizes.removeIf(prize -> prize.getPosition() == position);
        final Prize newPrize = builder.build();
        if(!newPrize.isEmpty()) {
            prizes.add(newPrize);
        }
        questionBuilder.setPrizes(prizes);
        return questionBuilder.build();
    }

}
