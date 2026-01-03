package fr.canardnocturne.questionstime.question.creation;

import fr.canardnocturne.questionstime.question.component.Malus;
import fr.canardnocturne.questionstime.question.component.Prize;
import fr.canardnocturne.questionstime.question.component.OutcomeCommand;
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
    private final List<OutcomeCommand> commandsMalus;
    private int duration = -1;
    private int timeBetweenAnswer = -1;
    private boolean stopped;
    private int weight = 1;

    public QuestionCreator() {
        this.answers = new ArrayList<>();
        this.propositions = new ArrayList<>();
        this.prizeBuilders = new HashMap<>();
        this.commandsMalus = new ArrayList<>();
    }

    public Question build() {
        final Set<Prize> prizes = this.prizeBuilders.values().stream()
                .filter(Prize.Builder::hasRewards)
                .map(Prize.Builder::build)
                .collect(Collectors.toSet());
        final Malus malus = this.moneyMalus > 0 || !this.commandsMalus.isEmpty() ?
                new Malus(this.moneyMalus, this.announceMalus, this.commandsMalus.toArray(new OutcomeCommand[0])) : null;
        final Question.QuestionBuilder questionBuilder;
        if (this.questionType == Types.MULTI) {
            this.answers.replaceAll(answer -> String.valueOf(this.propositions.indexOf(answer) + 1));
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

    public Map<Integer, List<ItemStack>> getItemsPrize() {
        return this.prizeBuilders.entrySet().stream()
                .filter(entry -> !entry.getValue().getItems().isEmpty())
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getItems()));
    }

    public void addCommandPrize(final int position, final OutcomeCommand outcomeCommand) {
        final Prize.Builder prizeBuilder = this.getPrizeBuilder(position);
        prizeBuilder.addCommand(outcomeCommand);
    }

    public Map<Integer, List<OutcomeCommand>> getCommandsPrize() {
        return this.prizeBuilders.entrySet().stream()
                .filter(entry -> !entry.getValue().getCommands().isEmpty())
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getCommands()));
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

    public List<OutcomeCommand> getCommandsMalus() {
        return commandsMalus;
    }

    public Map<Integer, Integer> getMoneyPrize() {
        return this.prizeBuilders.entrySet().stream()
                .filter(entry -> entry.getValue().getMoney() > 0)
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getMoney()));
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

    public boolean removeItemPrize(final Integer position, final ItemStack itemStack) {
        final Prize.Builder builder = this.prizeBuilders.get(position);
        if (builder != null) {
            return builder.removeItem(itemStack);
        } else {
            return false;
        }
    }

    public boolean removeCommandPrize(final Integer position, final OutcomeCommand outcomeCommand) {
        final Prize.Builder builder = this.prizeBuilders.get(position);
        if (builder != null) {
            return builder.removeCommand(outcomeCommand);
        } else {
            return false;
        }
    }
}
