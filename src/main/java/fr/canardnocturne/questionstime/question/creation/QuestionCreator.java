package fr.canardnocturne.questionstime.question.creation;

import fr.canardnocturne.questionstime.question.component.Malus;
import fr.canardnocturne.questionstime.question.component.Prize;
import fr.canardnocturne.questionstime.question.component.PrizeCommand;
import fr.canardnocturne.questionstime.question.type.Question;
import fr.canardnocturne.questionstime.question.type.Question.Types;
import fr.canardnocturne.questionstime.question.type.QuestionMulti;
import org.spongepowered.api.item.inventory.ItemStack;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

public class QuestionCreator {

    private Types questionType;
    private String question;
    private final List<String> answers;
    private final List<String> propositions;
    private final List<ItemStack> itemsPrize;
    private final List<PrizeCommand> commandsPrize;
    private int moneyPrize = -1;
    private boolean announcePrize;
    private int moneyMalus = -1;
    private boolean announceMalus;
    private int duration = -1;
    private int timeBetweenAnswer = -1;
    private boolean stopped;
    private int weight = 1;

    public QuestionCreator() {
        this.answers = new ArrayList<>();
        this.propositions = new ArrayList<>();
        this.itemsPrize = new ArrayList<>();
        this.commandsPrize = new ArrayList<>();
    }

    public Question build() {
        final ItemStack[] itemsPrize = this.itemsPrize.toArray(new ItemStack[0]);
        final PrizeCommand[] commandsPrize = this.commandsPrize.toArray(new PrizeCommand[0]);
        final Prize prize = this.moneyPrize > 0 || itemsPrize.length > 0 || commandsPrize.length > 0 ? new Prize(this.moneyPrize, this.announcePrize, itemsPrize, commandsPrize) : null;
        final Malus malus = this.moneyMalus > 0 ? new Malus(this.moneyMalus, this.announceMalus) : null;
        final Question.QuestionBuilder questionBuilder;
        if (this.questionType == Types.MULTI) {
            this.answers.replaceAll(proposition -> String.valueOf(this.propositions.indexOf(proposition) + 1));
            questionBuilder = QuestionMulti.builder().setPropositions(new LinkedHashSet<>(this.propositions));
        } else {
            questionBuilder = Question.builder();
        }
        return questionBuilder.setQuestion(this.question).setAnswers(new HashSet<>(this.answers))
                .setPrize(prize).setMalus(malus).setTimer(this.duration).setTimeBetweenAnswer(this.timeBetweenAnswer).setWeight(this.weight).build();
    }

    public Types getQuestionType() {
        return questionType;
    }

    public List<String> getPropositions() {
        return propositions;
    }

    public void addItemPrize(final ItemStack is) {
        for (final ItemStack itemStack : this.itemsPrize) {
            if (itemStack.equalTo(is)) {
                itemStack.setQuantity(itemStack.quantity() + is.quantity());
                return;
            }
        }
        this.itemsPrize.add(is);
    }

    public List<ItemStack> getItemsPrize() {
        return itemsPrize;
    }

    public void addCommandPrize(final PrizeCommand prizeCommand) {
        this.commandsPrize.add(prizeCommand);
    }

    public List<PrizeCommand> getCommandsPrize() {
        return commandsPrize;
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

    public int getMoneyPrize() {
        return moneyPrize;
    }

    public void setMoneyPrize(final int moneyPrize) {
        this.moneyPrize = moneyPrize;
    }

    public void setAnnouncePrize(final boolean announcePrize) {
        this.announcePrize = announcePrize;
    }

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
