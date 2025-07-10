package fr.canardnocturne.questionstime.question.creation;

import fr.canardnocturne.questionstime.question.component.Malus;
import fr.canardnocturne.questionstime.question.component.Prize;
import fr.canardnocturne.questionstime.question.component.PrizeCommand;
import fr.canardnocturne.questionstime.question.type.Question;
import fr.canardnocturne.questionstime.question.type.Question.Types;
import fr.canardnocturne.questionstime.question.type.QuestionMulti;
import org.spongepowered.api.item.inventory.ItemStack;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class QuestionCreator {

    private Types questionType;
    private String question;
    private final List<String> answers;
    private final List<String> propositions;
    private final Map<Integer, Prize.Builder> prizeBuilders;
    private int moneyMalus = -1;
    private boolean announceMalus;
    private int duration = -1;
    private int timeBetweenAnswer = -1;
    private boolean stopped;
    private int weight = 1;

    public QuestionCreator() {
        this.answers = new ArrayList<>();
        this.propositions = new ArrayList<>();
        this.prizeBuilders = new HashMap<>();
    }

    public Question build() {
        final Set<Prize> prizes = this.prizeBuilders.values().stream().map(Prize.Builder::build).collect(Collectors.toSet());
        final Malus malus = this.moneyMalus > 0 ? new Malus(this.moneyMalus, this.announceMalus) : null;
        final Question.QuestionBuilder questionBuilder;
        if (this.questionType == Types.MULTI) {
            this.answers.replaceAll(proposition -> String.valueOf(this.propositions.indexOf(proposition) + 1));
            questionBuilder = QuestionMulti.builder().setPropositions(new LinkedHashSet<>(this.propositions));
        } else {
            questionBuilder = Question.builder();
        }
        return questionBuilder.setQuestion(this.question).setAnswers(new HashSet<>(this.answers))
                .setPrizes(prizes).setMalus(malus).setTimer(this.duration).setTimeBetweenAnswer(this.timeBetweenAnswer).setWeight(this.weight).build();
    }

    public Types getQuestionType() {
        return questionType;
    }

    public List<String> getPropositions() {
        return propositions;
    }

    public void addItemPrize(final int position, final ItemStack is) {
        final Prize.Builder prizeBuilder = this.getPrizeBuilder(position);
        for (final ItemStack itemStack : prizeBuilder.getItems()) {
            if (itemStack.equalTo(is)) {
                itemStack.setQuantity(itemStack.quantity() + is.quantity());
                return;
            }
        }
        prizeBuilder.addItem(is);
    }

    public List<ItemStack> getItemsPrize() {
        return this.prizeBuilders.values().stream().map(Prize.Builder::getItems).flatMap(Collection::stream).toList();
    }

    public void addCommandPrize(final int position, final PrizeCommand prizeCommand) {
        final Prize.Builder prizeBuilder = this.getPrizeBuilder(position);
        prizeBuilder.addCommand(prizeCommand);
    }

    public List<PrizeCommand> getCommandsPrize() {
        return this.prizeBuilders.values().stream().map(Prize.Builder::getCommands).flatMap(Collection::stream).toList();
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(final String question) {
        this.question = question;
    }

    public int getTimeBetweenAnswer() {
        return timeBetweenAnswer;
    }

    public void setTimeBetweenAnswer(final LocalTime timeBetweenAnswer) {
        this.timeBetweenAnswer = timeBetweenAnswer.toSecondOfDay() * 20;
    }

    public void setDuration(final LocalTime questionDuration) {
        this.duration = questionDuration.toSecondOfDay() * 20;
    }

    public void setMoneyMalus(final int moneyMalus) {
        this.moneyMalus = moneyMalus;
    }

    public int getMoneyMalus() {
        return moneyMalus;
    }

    public void setAnnounceMalus(final boolean announceMalus) {
        this.announceMalus = announceMalus;
    }

    public List<Integer> getMoneyPrize() {
        return this.prizeBuilders.values().stream().map(Prize.Builder::getMoney).filter(money -> money > 0).toList();
    }

    public void setMoneyPrize(final int position, final int money) {
        final Prize.Builder prizeBuilder = this.getPrizeBuilder(position);
        prizeBuilder.setMoney(money);
    }

    private Prize.Builder getPrizeBuilder(final int position) {
        if(!this.prizeBuilders.containsKey(position)) {
            this.prizeBuilders.put(position, Prize.builder(position));
        }
        return this.prizeBuilders.get(position);
    }

    public void setAnnouncePrize(final boolean announcePrize) {
        for (final Prize.Builder prizeBuilder : this.prizeBuilders.values()) {
            prizeBuilder.setAnnounce(announcePrize);
        }
    }

    public

    public List<String> getAnswers() {
        return answers;
    }

    public void setQuestionType(final Types questionType) {
        this.questionType = questionType;
    }

    public void setWeight(final int weight) {
        this.weight = weight;
    }

    public void setStopped() {
        this.stopped = true;
    }

    public boolean isStopped() {
        return stopped;
    }
}
