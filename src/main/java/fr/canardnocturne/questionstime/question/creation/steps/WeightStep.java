package fr.canardnocturne.questionstime.question.creation.steps;

import fr.canardnocturne.questionstime.QuestionsTime;
import fr.canardnocturne.questionstime.question.creation.QuestionCreator;
import fr.canardnocturne.questionstime.util.TextUtils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

public class WeightStep implements CreationStep {

    public static CreationStep INSTANCE = new WeightStep();

    @Override
    public Component question(final QuestionsTime plugin) {
        return TextUtils.composed("Which ", "weight", " this question should have ?")
                .appendNewline()
                .append(TextUtils.composedWithoutPrefix("Answer with ", "/qtc [weight]", " where ", "weight", " is a positive number"))
                .appendNewline()
                .append(TextUtils.normalWithPrefix("If you don't want, just type ").append(TextUtils.commandShortcut("1")));
    }

    @Override
    public boolean handle(final Audience sender, final String answer, final QuestionCreator questionCreator) {
        if ("yes".equals(answer)) {
            return true;
        }
        try {
            final int weight = Integer.parseInt(answer);
            if (weight >= 1) {
                questionCreator.setWeight(weight);
                final Component message;
                if (weight > 1) {
                    message = TextUtils.composed("Is ", answer, " correct ?");
                } else {
                    message = TextUtils.normalWithPrefix("You really don't want to set a weight for this question ?");
                }
                sender.sendMessage(message
                        .append(TextUtils.normal(" If yes, answer with "))
                        .append(TextUtils.commandShortcut("yes"))
                        .append(TextUtils.normal(" or just re-answer to change the value")));
            } else {
                sender.sendMessage(TextUtils.specialWithPrefix(answer)
                        .append(TextUtils.normal(" is not a amount superior or equal to 1")));
            }
        } catch (final NumberFormatException e) {
            sender.sendMessage(TextUtils.specialWithPrefix(answer)
                    .append(TextUtils.normal(" is not a number")));
        }
        return false;
    }

    @Override
    public boolean shouldSkip(final QuestionsTime plugin, final QuestionCreator questionCreator) {
        return false;
    }

    @Override
    public CreationStep next(final QuestionCreator questionCreator) {
        return null;
    }
}
