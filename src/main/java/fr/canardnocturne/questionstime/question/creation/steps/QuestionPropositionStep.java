package fr.canardnocturne.questionstime.question.creation.steps;

import fr.canardnocturne.questionstime.QuestionsTime;
import fr.canardnocturne.questionstime.question.creation.QuestionCreator;
import fr.canardnocturne.questionstime.util.TextUtils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.function.Consumer;

public class QuestionPropositionStep implements CreationStep {

    public static final CreationStep INSTANCE = new QuestionPropositionStep();

    @Override
    public Component question() {
        return TextUtils.composed("Write a proposition with ", "/qtc add [proposition]")
                .appendNewline().append(TextUtils.composed("You can separate each proposition with a ", ";"))
                .appendNewline().append(TextUtils.composed("Modify a proposition with ", "/qtc set [position] [proposition]"))
                .appendNewline().append(TextUtils.composed("Delete a proposition with ", "/qtc del [position]"))
                .appendNewline().append(TextUtils.composed("Choose the answers with ", "/qtc answers [position]"))
                .appendNewline().append(TextUtils.composed("You can list the propositions with ", "/qtc list"))
                .appendNewline().append(TextUtils.composed("The number of proposition need to be between ", "2", " and ", "127"))
                .appendNewline().append(TextUtils.normalWithPrefix("When you're done, type "))
                .append(TextUtils.commandShortcut("confirm"));
    }

    @Override
    public boolean handle(final Audience sender, final String answer, final QuestionCreator questionCreator) {
        final String[] split = answer.split(" ", 2);
        if (split.length == 1) {
            return this.handleOneArgument(sender, answer, questionCreator);
        } else if (split.length == 2) {
            this.handleTwoArguments(sender, split, questionCreator);
        }
        return false;
    }

    private boolean handleOneArgument(final Audience sender, final String answer, final QuestionCreator questionCreator) {
        return switch (answer) {
            case "list" -> this.listPropositions(sender, questionCreator);
            case "confirm" -> this.confirmPropositions(sender, questionCreator);
            case "add" -> {
                sender.sendMessage(TextUtils.composed("Command ", "add", " need to be followed by a proposition"));
                yield false;
            }
            case "set" -> {
                sender.sendMessage(TextUtils.composed("Command ", "set", " need to be followed by a position then a proposition"));
                yield false;
            }
            case "del" -> {
                sender.sendMessage(TextUtils.composed("Command ", "del", " need to be followed by a position"));
                yield false;
            }
            case "answers" -> {
                sender.sendMessage(TextUtils.composed("Command ", "answers", " need to be followed by a position"));
                yield false;
            }
            case null, default -> {
                sender.sendMessage(TextUtils.composed("Answer ", answer, " not recognized between ", "add, set, del, list, answers or confirm"));
                yield false;
            }
        };
    }

    private boolean listPropositions(final Audience sender, final QuestionCreator questionCreator) {
        if (questionCreator.getPropositions().isEmpty()) {
            sender.sendMessage(TextUtils.normalWithPrefix("No propositions have been made"));
        } else {
            for (int position = 0; position < questionCreator.getPropositions().size(); position++) {
                final String proposition = questionCreator.getPropositions().get(position);
                if (questionCreator.getAnswers().contains(proposition)) {
                    sender.sendMessage(QuestionsTime.PREFIX.append(Component.text("[X]", NamedTextColor.RED, TextDecoration.BOLD)
                                    .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/qtc del " + (position + 1)))
                                    .hoverEvent(HoverEvent.showText(Component.text("Delete the proposition " + (position + 1)))))
                            .append(Component.text("     "))
                            .append(Component.text(" " + (position + 1) + "] " + proposition + " (an answer)", NamedTextColor.BLUE)));
                } else {
                    sender.sendMessage(QuestionsTime.PREFIX.append(Component.text("[X]", NamedTextColor.RED, TextDecoration.BOLD)
                                    .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/qtc del " + (position + 1)))
                                    .hoverEvent(HoverEvent.showText(Component.text("Delete the proposition " + (position + 1)))))
                            .append(Component.text("[A]", NamedTextColor.BLUE, TextDecoration.BOLD)
                                    .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/qtc answers " + (position + 1)))
                                    .hoverEvent(HoverEvent.showText(Component.text("Add the proposition " + (position + 1) + " as answer "))))
                            .append(TextUtils.composedWithoutPrefix(" ", (position + 1) + "] ", proposition)));
                }
            }
        }
        return false;
    }

    private boolean confirmPropositions(final Audience sender, final QuestionCreator questionCreator) {
        if (questionCreator.getPropositions().size() < 2) {
            sender.sendMessage(TextUtils.composed("You need to write at least ", String.valueOf(2), " propositions with ", "/qtc add [proposition]", " before confirming"));
        } else if (questionCreator.getAnswers().isEmpty()) {
            sender.sendMessage(TextUtils.composed("You need to choose at least one answer with ", "/qtc answers [proposition]", " before confirming"));
        } else {
            return true;
        }
        return false;
    }

    private void handleTwoArguments(final Audience sender, final String[] answers, final QuestionCreator questionCreator) {
        final String action = answers[0];
        final String argument = answers[1];
        switch (action) {
            case "add" -> addProposition(sender, argument, questionCreator);
            case "set" -> setProposition(sender, argument, questionCreator);
            case "del" -> deleteProposition(sender, argument, questionCreator);
            case "answers" -> addAnswer(sender, argument, questionCreator);
            case "list", "confirm" ->
                    sender.sendMessage(TextUtils.composed("Command ", action, " doesn't take a second argument"));
            default ->
                    sender.sendMessage(TextUtils.composed("Answer ", action + " " + argument, " not recognized between ", "add, set, del, list, answers or confirm"));
        }
    }

    private void addProposition(final Audience sender, final String argument, final QuestionCreator questionCreator) {
        final String[] propositions = argument.split(";");
        if (propositions.length + questionCreator.getPropositions().size() > 128) {
            sender.sendMessage(TextUtils.composed("You cannot add more than ", String.valueOf(128), " propositions. Please remove some propositions before adding new ones"));
        } else {
            sender.sendMessage(TextUtils.normalWithPrefix("Propositions added: "));
            for (int i = 0; i < propositions.length; i++) {
                final String proposition = propositions[i];
                if(questionCreator.getPropositions().contains(proposition)) {
                    sender.sendMessage(TextUtils.composed("Proposition ", proposition, " already exists"));
                } else {
                    questionCreator.getPropositions().add(proposition);
                    sender.sendMessage(TextUtils.composed("", "[" + (i + 1) + "] ", proposition));
                }
            }
        }
    }

    private void setProposition(final Audience sender, final String answer, final QuestionCreator questionCreator) {
        final String[] arguments = answer.split(" ", 2);
        this.handlePositionArgument(sender, arguments[0], questionCreator, position -> {
            if (arguments.length == 1) {
                sender.sendMessage(TextUtils.normalWithPrefix("The proposition is missing"));
            } else {
                final String proposition = arguments[1];
                final String previousProposition = questionCreator.getPropositions().set(position, proposition);
                sender.sendMessage(TextUtils.composed("Proposition ", "[" + (position + 1) + "]", " modified from ", previousProposition, " to ", proposition));
            }
        });
    }

    private void deleteProposition(final Audience sender, final String answer, final QuestionCreator questionCreator) {
        this.handlePositionArgument(sender, answer, questionCreator, position -> {
            final String removedProposition = questionCreator.getPropositions().remove(position.intValue());
            sender.sendMessage(TextUtils.composed("Proposition ", "[" + (position + 1) + "] " + removedProposition, " deleted !"));
            final boolean answerRemoved = questionCreator.getAnswers().remove(removedProposition);
            if (answerRemoved) {
                sender.sendMessage(TextUtils.composed("The proposition was automatically removed from the answers"));
            }
        });
    }

    private void addAnswer(final Audience sender, final String answer, final QuestionCreator questionCreator) {
        this.handlePositionArgument(sender, answer, questionCreator, position -> {
            final String answerProposition = questionCreator.getPropositions().get(position);
            if (questionCreator.getAnswers().contains(answerProposition)) {
                sender.sendMessage(TextUtils.composed("Proposition ", "[" + (position + 1) + "] " + answerProposition, " is already an answer"));
            } else {
                questionCreator.getAnswers().add(answerProposition);
                sender.sendMessage(TextUtils.composed("Proposition ", "[" + (position + 1) + "] " + answerProposition, " added as an answer"));
            }
        });
    }

    private void handlePositionArgument(final Audience sender, final String answer, final QuestionCreator questionCreator, final Consumer<Integer> positiveParsed) {
        final int position = this.parseStrToInt(answer) - 1;
        if (position < 0) {
            sender.sendMessage(TextUtils.composed("The ", "position", " should be a positive number, corresponding to a proposition in the ")
                    .append(TextUtils.commandShortcut("list")));
        } else if (position >= questionCreator.getPropositions().size()) {
            sender.sendMessage(TextUtils.composed("There is no proposition at position ", answer, ". Type ")
                    .append(TextUtils.commandShortcut("list"))
                    .append(TextUtils.normal(" to see them")));
        } else {
            positiveParsed.accept(position);
        }
    }

    private int parseStrToInt(final String number) {
        try {
            return Integer.parseInt(number);
        } catch (final NumberFormatException e) {
            return -1;
        }
    }

    @Override
    public boolean shouldSkip(final QuestionCreator questionCreator) {
        return false;
    }

    @Override
    public Step next(final QuestionCreator questionCreator) {
        return PrizeItemsStep.INSTANCE;
    }
}
