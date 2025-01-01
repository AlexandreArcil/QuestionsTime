package fr.canardnocturne.questionstime.question.creation.steps;

import fr.canardnocturne.questionstime.question.creation.QuestionCreator;
import fr.canardnocturne.questionstime.util.TextUtils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class QuestionDurationStep implements CreationStep {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("H'h'm'm's's'");

    public static final CreationStep INSTANCE = new QuestionDurationStep();

    @Override
    public Component question() {
        return TextUtils.composed("How long the players have to answer the question ? Answer with this format ", "/qtc xhxmxs", ", where ", "x", " is a number")
                .appendNewline().append(TextUtils.normalWithPrefix("If you doesn't want, just answer with "))
                .append(TextUtils.commandShortcut("0h0m0s"));
    }

    @Override
    public boolean handle(final Audience sender, final String answer, final QuestionCreator questionCreator) {
        if ("yes".equals(answer)) {
            return true;
        }
        try {
            final LocalTime questionDuration = LocalTime.parse(answer, TIME_FORMATTER);
            questionCreator.setDuration(questionDuration);
            final Component confirmMessage;
            if (questionDuration.toSecondOfDay() == 0) {
                confirmMessage = TextUtils.normalWithPrefix("You don't want to add a time to answer the question ?");
            } else {
                confirmMessage = TextUtils.composed("Is the question duration ", questionDuration.format(TIME_FORMATTER), " correct ?");
            }
            sender.sendMessage(confirmMessage.appendNewline()
                    .append(TextUtils.normal("Answer with "))
                    .append(TextUtils.commandShortcut("yes"))
                    .append(TextUtils.normal(" or just repeat the command")));
        } catch (final DateTimeParseException e) {
            sender.sendMessage(TextUtils.specialWithPrefix(answer)
                    .append(TextUtils.composedWithoutPrefix(" doesn't match the expected time format ", "xhxmxs", " where ", "x", " is a number, or it's an incorrect time")));
        }
        return false;
    }

    @Override
    public boolean shouldSkip(final QuestionCreator questionCreator) {
        return false;
    }

    @Override
    public CreationStep next(final QuestionCreator questionCreator) {
        return TimeBetweenAnswerStep.INSTANCE;
    }
}
