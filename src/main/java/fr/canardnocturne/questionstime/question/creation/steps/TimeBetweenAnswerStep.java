package fr.canardnocturne.questionstime.question.creation.steps;

import fr.canardnocturne.questionstime.QuestionsTime;
import fr.canardnocturne.questionstime.question.creation.QuestionCreator;
import fr.canardnocturne.questionstime.util.TextUtils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class TimeBetweenAnswerStep implements CreationStep {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("H'h'm'm's's'");

    public static final CreationStep INSTANCE = new TimeBetweenAnswerStep();

    @Override
    public Component question(final QuestionsTime plugin) {
        return TextUtils.composed("What is the time between two answers ? Answer with this time format ", "/qtc xhxmxs", ", where ")
                .append(TextUtils.special("x")).append(TextUtils.normal(" is a number"))
                .appendNewline()
                .append(TextUtils.normal("Answer with ").append(TextUtils.commandShortcut("0h0m0s")).append(TextUtils.normal(" if you don't want to add any.")));
    }

    @Override
    public boolean handle(final Audience sender, final String answer, final QuestionCreator questionCreator) {
        if ("yes".equals(answer) && questionCreator.getTimeBetweenAnswer() >= 0) {
            return true;
        }
        try {
            final LocalTime timeBetweenAnswer = LocalTime.parse(answer, TIME_FORMATTER);
            questionCreator.setTimeBetweenAnswer(timeBetweenAnswer);
            final Component confirmMessage;
            if (timeBetweenAnswer.toSecondOfDay() == 0) {
                confirmMessage = TextUtils.normalWithPrefix("You don't want to add a time between answers ?");
            } else {
                confirmMessage = TextUtils.composed("Is the time ", timeBetweenAnswer.format(TIME_FORMATTER), " between two answers correct ?");
            }
            sender.sendMessage(confirmMessage.appendNewline().append(TextUtils.commandShortcut("yes"))
                    .append(TextUtils.normal(" or just repeat the command")));
        } catch (final DateTimeParseException e) {
            sender.sendMessage(TextUtils.specialWithPrefix(answer)
                    .append(TextUtils.composedWithoutPrefix(" doesn't match the expected time format ", "xhxmxs", " where ", "x", " is a number, or it's an incorrect time")));
        }
        return false;
    }

    @Override
    public boolean shouldSkip(final QuestionsTime plugin, final QuestionCreator questionCreator) {
        return false;
    }

    @Override
    public CreationStep next(final QuestionCreator questionCreator) {
        return WeightStep.INSTANCE;
    }

}
