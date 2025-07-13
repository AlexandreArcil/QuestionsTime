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
import org.apache.commons.lang3.StringUtils;

public class SimpleQuestionAnswerStep implements CreationStep {

    public static final CreationStep INSTANCE = new SimpleQuestionAnswerStep();

    @Override
    public Component question() {
        return TextUtils.composed("What's the answer to the question ? Answer with ", "/qtc add [answer]")
                .appendNewline().append(TextUtils.composed("The question can have multiple answers, separate them with ", ";", " or use ", "/qtc add [answer]", " multiple times"))
                .appendNewline().append(TextUtils.composed("To see the answers, use ", "/qtc list"))
                .appendNewline().append(TextUtils.composed("To remove an answer, use ", "/qtc del [position]"))
                .appendNewline().append(TextUtils.normalWithPrefix("When you're done, type "))
                .append(TextUtils.commandShortcut("confirm"));
    }

    @Override
    public boolean handle(final Audience sender, final String answer, final QuestionCreator questionCreator) {
        if ("confirm".equals(answer)) {
            if(questionCreator.getAnswers().isEmpty()) {
                sender.sendMessage(TextUtils.normalWithPrefix("The question must have at least one answer"));
                return false;
            } else {
                return true;
            }
        }
        switch (answer) {
            case "list" -> this.listAnswers(sender, questionCreator);
            case "del" -> sender.sendMessage(TextUtils.normalWithPrefix("You must specify the position of the answer to delete"));
            case "add" -> sender.sendMessage(TextUtils.normalWithPrefix("You must specify an answer"));
            default -> {
                if (answer.startsWith("del ")) {
                    this.removeAnswer(sender, StringUtils.substringAfter(answer, "del "), questionCreator);
                } else if(answer.startsWith("add ")) {
                    this.addAnswer(sender, StringUtils.substringAfter(answer, "add "), questionCreator);
                } else {
                    sender.sendMessage(TextUtils.composed("Unknown command ", answer, " between ", "add, del and list"));
                }
            }
        }
        return false;
    }

    private void addAnswer(final Audience sender, final String answer, final QuestionCreator questionCreator) {
        final String[] answers = answer.split(";");
        if(answers.length == 1) {
            if(questionCreator.getAnswers().contains(answer)) {
                sender.sendMessage(TextUtils.composed("Answer ", answer, " already exists"));
            } else {
                questionCreator.getAnswers().add(answer);
                sender.sendMessage(TextUtils.composed("Answer ", answer, " added"));
            }
        } else {
            sender.sendMessage(TextUtils.normalWithPrefix("Answers added:"));
            int position = 1;
            for (final String questionAnswer : answers) {
                if(questionCreator.getAnswers().contains(questionAnswer)) {
                    sender.sendMessage(TextUtils.composed("Answer ", questionAnswer, " already exists"));
                } else {
                    questionCreator.getAnswers().add(questionAnswer);
                    sender.sendMessage(TextUtils.composed("", "[" + (position) + "] ", questionAnswer));
                    position++;
                }
            }
        }
    }

    private void removeAnswer(final Audience sender, final String answer, final QuestionCreator questionCreator) {
        try {
            final int position = Integer.parseInt(answer);
            if(position <= 0 || position > questionCreator.getAnswers().size()) {
                sender.sendMessage(TextUtils.composed("No answer is at the position ", String.valueOf(position)));
            } else {
                final String removed = questionCreator.getAnswers().remove(position - 1);
                sender.sendMessage(TextUtils.composed("Answer ", removed, " removed"));
            }
        } catch (final NumberFormatException e) {
            sender.sendMessage(TextUtils.composed("", String.valueOf(answer), " is not a valid position"));
        }
    }

    private void listAnswers(final Audience sender, final QuestionCreator questionCreator) {
        if(questionCreator.getAnswers().isEmpty()) {
            sender.sendMessage(TextUtils.normalWithPrefix("No answer has been added yet"));
        } else {
            sender.sendMessage(TextUtils.normalWithPrefix("Answers: "));
            for (int position = 0; position < questionCreator.getAnswers().size(); position++) {
                sender.sendMessage(QuestionsTime.PREFIX.append(Component.text("[X] ", NamedTextColor.RED, TextDecoration.BOLD)
                                .clickEvent(ClickEvent.runCommand("/qtc del " + (position + 1)))
                                .hoverEvent(HoverEvent.showText(Component.text("Delete the answer " + (position + 1)))))
                        .append(TextUtils.composedWithoutPrefix("", position + 1 + "] ", questionCreator.getAnswers().get(position))));
            }
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
