package fr.canardnocturne.questionstime.question.modifier;

import fr.canardnocturne.questionstime.question.Question;
import fr.canardnocturne.questionstime.question.QuestionComponent;
import fr.canardnocturne.questionstime.question.component.Malus;
import fr.canardnocturne.questionstime.question.component.OutcomeCommand;
import fr.canardnocturne.questionstime.question.component.Prize;
import fr.canardnocturne.questionstime.question.serializer.ItemStackSerializer;
import fr.canardnocturne.questionstime.question.serializer.OutcomeCommandSerializer;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
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
            final Prize.Builder newPrizeBuilder = prizes.stream()
                    .filter(prize1 -> prize1.getPosition() == position)
                    .findFirst()
                    .map(Prize::toBuilder)
                    .orElseGet(() -> Prize.builder(position));
            newPrizeBuilder.setMoney(value);
            prizes.removeIf(prize -> prize.getPosition() == position);
            final Prize newPrize = newPrizeBuilder.build();
            if(!newPrize.isEmpty()) {
                prizes.add(newPrize);
            }
            builder.setPrizes(prizes);
        } else {
            throw new IllegalArgumentException("Unknown type '" + component + "' for set integer with position");
        }
        return builder.build();
    }

    @Override
    public Question set(final Question question, final QuestionComponent component, final int position, final boolean value) {
        final Question.QuestionBuilder builder = question.toBuilder();
        if (component == QuestionComponent.PRIZE_ANNOUNCE) {
            final TreeSet<Prize> prizes = new TreeSet<>(question.getPrizes());
            final Prize.Builder prize = prizes.stream()
                    .filter(prize1 -> prize1.getPosition() == position)
                    .findFirst()
                    .map(Prize::toBuilder)
                    .orElseThrow(() -> new IllegalArgumentException("No prize with position " + position + " is present in the question"));
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
    public Question add(final Question question, final QuestionComponent component, final int position, final Collection<String> values) {
        final Question.QuestionBuilder builder = question.toBuilder();
        switch (component) {
            case PRIZE_ITEMS -> {
                final TreeSet<Prize> prizes = new TreeSet<>(question.getPrizes());
                final Prize.Builder prize = prizes.stream()
                        .filter(prize1 -> prize1.getPosition() == position)
                        .findFirst()
                        .map(Prize::toBuilder)
                        .orElseGet(() -> Prize.builder(position));
                for (final String value : values) {
                    final ItemStack itemStack = ItemStackSerializer.fromString(value);
                    prize.addItem(itemStack);
                }
                prizes.removeIf(prize1 -> prize1.getPosition() == position);
                prizes.add(prize.build());
                builder.setPrizes(prizes);
            }
            case PRIZE_COMMANDS -> {
                final TreeSet<Prize> prizes = new TreeSet<>(question.getPrizes());
                final Prize.Builder prize = prizes.stream()
                        .filter(prize1 -> prize1.getPosition() == position)
                        .findFirst()
                        .map(Prize::toBuilder)
                        .orElseGet(() -> Prize.builder(position));
                for (final String value : values) {
                    final OutcomeCommand outcomeCommand = OutcomeCommandSerializer.deserialize(value);
                    prize.addCommand(outcomeCommand);
                }
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
        } else if(component == QuestionComponent.REVEAL_ANSWER) {
            builder.setRevealAnswer(value);
        } else {
            throw new IllegalArgumentException("Unknown type '" + component + "' for set boolean");
        }
        return builder.build();
    }

    @Override
    public Question add(final Question question, final QuestionComponent component, final Collection<String> values) {
        final Question.QuestionBuilder builder = question.toBuilder();
        if(component == QuestionComponent.MALUS_COMMANDS) {
            final Malus.Builder malus = question.getMalus()
                    .map(Malus::toBuilder)
                    .orElseGet(Malus::builder);
            for (final String value : values) {
                final OutcomeCommand outcomeCommand = OutcomeCommandSerializer.deserialize(value);
                malus.addCommand(outcomeCommand);
            }
            builder.setMalus(malus.build());
        } else {
            final Collection<String> components = switch (component) {
                case ANSWERS ->  new HashSet<>(question.getAnswers());
                case PROPOSITIONS ->  new ArrayList<>(question.getPropositions());
                case TAGS ->  new HashSet<>(question.getTags());
                case EXCLUDE_PERMISSIONS ->  new HashSet<>(question.getExcludePermissions());
                case INCLUDE_PERMISSIONS ->  new HashSet<>(question.getIncludePermissions());
                default -> throw new IllegalArgumentException("Unknown type '" + component + "' for remove");
            };
            final int initialSize = components.size();
            components.addAll(values);
            final int expectedSize = initialSize + values.size();
            if (components.size() != expectedSize) {
                final String componentNameError = StringUtils.capitalize(component.getSingular()) + "(s)";
                throw new IllegalArgumentException(componentNameError + " '"
                        + String.join(", ", values) + "' is/are already present in the question");
            }
            switch (component) {
                case ANSWERS -> builder.setAnswers((Set<String>) components);
                case PROPOSITIONS -> builder.setPropositions((List<String>) components);
                case TAGS -> builder.setTags((Set<String>) components);
                case EXCLUDE_PERMISSIONS -> builder.setExcludePermissions((Set<String>) components);
                case INCLUDE_PERMISSIONS -> builder.setIncludePermissions((Set<String>) components);
                default -> throw new IllegalArgumentException("Unknown type '" + component + "' for remove");
            }
        }
        return builder.build();
    }

    @Override
    public Question remove(final Question question, final QuestionComponent component, final Collection<String> values) {
        final Question.QuestionBuilder builder = question.toBuilder();
        if(component == QuestionComponent.MALUS_COMMANDS) {
            final Malus.Builder malusBuilder = question.getMalus()
                    .map(Malus::toBuilder)
                    .orElseThrow(() -> new IllegalArgumentException("No malus is present in the question"));
            final Set<OutcomeCommand> commandsToRemove = new HashSet<>();
            for (final String value : values) {
                final OutcomeCommand outcomeCommand = OutcomeCommandSerializer.deserialize(value);
                commandsToRemove.add(outcomeCommand);
            }
            final Set<OutcomeCommand> commands = new HashSet<>(malusBuilder.getCommands());
            commands.removeAll(commandsToRemove);
            malusBuilder.setCommands(commands);
            final Malus malus = malusBuilder.build();
            if (!malus.isEmpty()) {
                builder.setMalus(malus);
            } else {
                builder.setMalus(null);
            }
        } else {
            final Collection<String> components = switch (component) {
                case ANSWERS ->  new HashSet<>(question.getAnswers());
                case PROPOSITIONS ->  new ArrayList<>(question.getPropositions());
                case TAGS ->  new HashSet<>(question.getTags());
                case EXCLUDE_PERMISSIONS ->  new HashSet<>(question.getExcludePermissions());
                case INCLUDE_PERMISSIONS ->  new HashSet<>(question.getIncludePermissions());
                default -> throw new IllegalArgumentException("Unknown type '" + component + "' for remove");
            };
            final int initialSize = components.size();
            components.removeAll(values);
            final int expectedSize = initialSize - values.size();
            if (components.size() != expectedSize) {
                final String componentNameError = StringUtils.capitalize(component.getSingular()) + "(s)";
                throw new IllegalArgumentException(componentNameError + " '"
                        + String.join(", ", values) + "' is/are not present in the question");
            }
            switch (component) {
                case ANSWERS -> builder.setAnswers((Set<String>) components);
                case PROPOSITIONS -> builder.setPropositions((List<String>) components);
                case TAGS -> builder.setTags((Set<String>) components);
                case EXCLUDE_PERMISSIONS -> builder.setExcludePermissions((Set<String>) components);
                case INCLUDE_PERMISSIONS -> builder.setIncludePermissions((Set<String>) components);
                default -> throw new IllegalArgumentException("Unknown type '" + component + "' for remove");
            }
        }
        return builder.build();
    }

    @Override
    public Question remove(final Question question, final QuestionComponent component, final int position, final Collection<String> values) {
        final Question.QuestionBuilder builder = question.toBuilder();
        final SortedSet<Prize> prizes = new TreeSet<>(question.getPrizes());
        final Prize oldPrize = prizes.stream()
                .filter(prize1 -> prize1.getPosition() == position)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Prize with position " + position + " not found"));
        final Prize.Builder newPrizeBuilder = oldPrize.toBuilder();
        switch (component) {
            case PRIZE_ITEMS -> {
                for (final String value : values) {
                    final ItemStack itemStack = ItemStackSerializer.fromString(value);
                    final boolean removed = newPrizeBuilder.getItems().removeIf(item -> item.equalTo(itemStack));
                    if (!removed) {
                        throw new IllegalArgumentException("Item '" + value + "' not found in prize with position " + position);
                    }
                }
            }
            case PRIZE_COMMANDS -> {
                for (final String value : values) {
                    final OutcomeCommand outcomeCommand = OutcomeCommandSerializer.deserialize(value);
                    final boolean removed = newPrizeBuilder.getCommands().remove(outcomeCommand);
                    if (!removed) {
                        throw new IllegalArgumentException("Command '" + value + "' not found in prize with position " + position);
                    }
                }
            }
            default -> throw new IllegalArgumentException("Unknown type '" + component + "' for remove string with position");
        }
        prizes.remove(oldPrize);
        final Prize newPrize = newPrizeBuilder.build();
        if(!newPrize.isEmpty()) {
            prizes.add(newPrize);
        }
        builder.setPrizes(prizes);
        return builder.build();
    }

}
