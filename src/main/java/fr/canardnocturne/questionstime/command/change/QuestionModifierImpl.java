package fr.canardnocturne.questionstime.command.change;

import fr.canardnocturne.questionstime.question.Question;
import fr.canardnocturne.questionstime.question.component.Malus;
import fr.canardnocturne.questionstime.question.component.OutcomeCommand;
import fr.canardnocturne.questionstime.question.component.Prize;
import fr.canardnocturne.questionstime.question.serializer.ItemStackSerializer;
import fr.canardnocturne.questionstime.question.serializer.OutcomeCommandSerializer;
import org.apache.commons.lang3.ArrayUtils;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;

public class QuestionModifierImpl implements QuestionModifier {

    @Override
    public Question set(final Question question, final QuestionComponent component, final String value) {
        final Question.QuestionBuilder builder = question.toBuilder();
        if (component == QuestionComponent.QUESTION) {
            builder.setQuestion(value);
        } else {
            throw new IllegalArgumentException("Unknown type '" + component + "' for set string");
        }
        return builder.build();
    }

    @Override
    public Question set(final Question question, final QuestionComponent component, final int value) {
        final Question.QuestionBuilder builder = question.toBuilder();
        switch (component) {
            case WEIGHT:
                builder.setWeight(value);
                break;
            case TIMER:
                builder.setTimer(value);
                break;
            case TIMER_BETWEEN_ANSWER:
                builder.setTimeBetweenAnswer(value);
                break;
            case MALUS_MONEY:
                final Malus.Builder malus = question.getMalus()
                        .map(Malus::toBuilder)
                        .orElseGet(Malus::builder);
                malus.setMoney(value);
                builder.setMalus(malus.build());
                break;
            default:
                throw new IllegalArgumentException("Unknown type '" + component + "' for set integer");
        }
        return builder.build();
    }

    @Override
    public Question set(final Question question, final QuestionComponent component, final int position, final int value) {
        final Question.QuestionBuilder builder = question.toBuilder();
        if (component == QuestionComponent.PRIZE_MONEY) {
            final TreeSet<Prize> prizes = new TreeSet<>(question.getPrizes());
            final Prize.Builder prize = prizes.stream()
                    .filter(prize1 -> prize1.getPosition() == position)
                    .findFirst()
                    .map(Prize::toBuilder)
                    .orElseGet(() -> Prize.builder(position));
            prize.setMoney(value);
            prizes.removeIf(prize1 -> prize1.getPosition() == position);
            prizes.add(prize.build());
            builder.setPrizes(prizes);
        } else {
            throw new IllegalArgumentException("Unknown type '" + component + "' for set integer with position");
        }
        return builder.build();
    }

    @Override
    public Question set(final Question question, final QuestionComponent component, final int position, final boolean value) {
        final Question.QuestionBuilder builder = question.toBuilder();
        if (Objects.requireNonNull(component) == QuestionComponent.PRIZE_ANNOUNCE) {
            final TreeSet<Prize> prizes = new TreeSet<>(question.getPrizes());
            final Prize.Builder prize = prizes.stream()
                    .filter(prize1 -> prize1.getPosition() == position)
                    .findFirst()
                    .map(Prize::toBuilder)
                    .orElseGet(() -> Prize.builder(position));
            prize.setAnnounce(value);
            prizes.removeIf(prize1 -> prize1.getPosition() == position);
            prizes.add(prize.build());
            builder.setPrizes(prizes);
        } else {
            throw new IllegalArgumentException("Unknown type '" + component + "' for set boolean");
        }
        return builder.build();
    }

    @Override
    public Question add(final Question question, final QuestionComponent component, final int position, final String value) {
        final Question.QuestionBuilder builder = question.toBuilder();
        switch (component) {
            case PRIZE_ITEMS -> {
                final ItemStack itemStack = ItemStackSerializer.fromString(value);
                final TreeSet<Prize> prizes = new TreeSet<>(question.getPrizes());
                final Prize.Builder prize = prizes.stream()
                        .filter(prize1 -> prize1.getPosition() == position)
                        .findFirst()
                        .map(Prize::toBuilder)
                        .orElseGet(() -> Prize.builder(position));
                prize.addItem(itemStack);
                prizes.removeIf(prize1 -> prize1.getPosition() == position);
                prizes.add(prize.build());
                builder.setPrizes(prizes);
            }
            case PRIZE_COMMANDS -> {
                final OutcomeCommand outcomeCommand = OutcomeCommandSerializer.deserialize(value);
                final TreeSet<Prize> prizes = new TreeSet<>(question.getPrizes());
                final Prize.Builder prize = prizes.stream()
                        .filter(prize1 -> prize1.getPosition() == position)
                        .findFirst()
                        .map(Prize::toBuilder)
                        .orElseGet(() -> Prize.builder(position));
                prize.addCommand(outcomeCommand);
                prizes.removeIf(prize1 -> prize1.getPosition() == position);
                prizes.add(prize.build());
                builder.setPrizes(prizes);
            }
            default -> throw new IllegalArgumentException("Unknown type '" + component + "' for add string with position");
        }
        return builder.build();
    }

    @Override
    public Question set(final Question question, final QuestionComponent component, final boolean value) {
        final Question.QuestionBuilder builder = question.toBuilder();
        if (component == QuestionComponent.MALUS_ANNOUNCE) {
            final Malus.Builder malus = question.getMalus()
                    .map(Malus::toBuilder)
                    .orElseGet(Malus::builder);
            malus.setAnnounce(value);
            builder.setMalus(malus.build());
        } else {
            throw new IllegalArgumentException("Unknown type '" + component + "' for set boolean");
        }
        return builder.build();
    }

    @Override
    public Question add(final Question question, final QuestionComponent component, final String value) {
        final Question.QuestionBuilder builder = question.toBuilder();
        switch (component) {
            case ANSWERS:
                final HashSet<String> answers = new HashSet<>(question.getAnswers());
                answers.add(value);
                builder.setAnswers(answers);
                break;
            case PROPOSITIONS:
                final List<String> propositions = new ArrayList<>(question.getPropositions());
                propositions.add(value);
                builder.setPropositions(propositions);
                break;
            case MALUS_COMMANDS:
                final OutcomeCommand outcomeCommand = OutcomeCommandSerializer.deserialize(value);
                final Malus.Builder malus = question.getMalus()
                        .map(Malus::toBuilder)
                        .orElseGet(Malus::builder);
                malus.addCommand(outcomeCommand);
                builder.setMalus(malus.build());
                break;
            default:
                throw new IllegalArgumentException("Unknown type '" + component + "' for add string");
        }
        return builder.build();
    }

    @Override
    public Question remove(final Question question, final QuestionComponent component, final String value) {
        final Question.QuestionBuilder builder = question.toBuilder();
        switch (component) {
            case ANSWERS:
                final HashSet<String> answers = new HashSet<>(question.getAnswers());
                answers.remove(value);
                builder.setAnswers(answers);
                break;
            case PROPOSITIONS:
                final List<String> propositions = new ArrayList<>(question.getPropositions());
                propositions.remove(value);
                builder.setPropositions(propositions);
                break;
            case MALUS_COMMANDS:
                final OutcomeCommand outcomeCommand = OutcomeCommandSerializer.deserialize(value);
                final Malus.Builder malus = question.getMalus()
                        .map(Malus::toBuilder)
                        .orElseGet(Malus::builder);
                final int position = ArrayUtils.indexOf(malus.getCommands(), outcomeCommand);
                if (position == ArrayUtils.INDEX_NOT_FOUND) {
                    throw new IllegalArgumentException("Command '" + value + "' not found in malus");
                }
                final OutcomeCommand[] result = ArrayUtils.remove(malus.getCommands(), position);
                malus.setCommands(result);
                builder.setMalus(malus.build());
                break;
            default:
                throw new IllegalArgumentException("Unknown type '" + component + "' for remove string");
        }
        return builder.build();
    }

    @Override
    public Question remove(final Question question, final QuestionComponent component, final int position, final String value) {
        final Question.QuestionBuilder builder = question.toBuilder();
        switch (component) {
            case PRIZE_ITEMS -> {
                final TreeSet<Prize> prizes = new TreeSet<>(question.getPrizes());
                final Prize.Builder prize = prizes.stream()
                        .filter(prize1 -> prize1.getPosition() == position)
                        .findFirst()
                        .map(Prize::toBuilder)
                        .orElseThrow(() -> new IllegalArgumentException("Prize with position " + position + " not found"));
                final ItemStack itemStack = ItemStackSerializer.fromString(value);
                final boolean removed = prize.getItems().removeIf(item -> item.equalTo(itemStack));
                if (removed) {
                    throw new IllegalArgumentException("Item '" + value + "' not found in prize with position " + position);
                }
                prizes.add(prize.build());
                builder.setPrizes(prizes);
            }
            case PRIZE_COMMANDS -> {
                final TreeSet<Prize> prizes = new TreeSet<>(question.getPrizes());
                final Prize.Builder prize = prizes.stream()
                        .filter(prize1 -> prize1.getPosition() == position)
                        .findFirst()
                        .map(Prize::toBuilder)
                        .orElseThrow(() -> new IllegalArgumentException("Prize with position " + position + " not found"));
                final OutcomeCommand outcomeCommand = OutcomeCommandSerializer.deserialize(value);
                final boolean removed = prize.getCommands().removeIf(command -> command.equals(outcomeCommand));
                if (removed) {
                    throw new IllegalArgumentException("Command '" + value + "' not found in prize with position " + position);
                }
                prizes.add(prize.build());
                builder.setPrizes(prizes);
            }
            default -> throw new IllegalArgumentException("Unknown type '" + component + "' for remove string with position");
        }
        return builder.build();
    }

}
