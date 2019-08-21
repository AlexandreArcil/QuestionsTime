package fr.canardnocturne.questionstime.question.creation.steps;

import fr.canardnocturne.questionstime.QuestionsTime;
import fr.canardnocturne.questionstime.question.creation.QuestionCreator;
import fr.canardnocturne.questionstime.util.TextUtils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

public class PrizeMoneyAmountStep implements CreationStep {

    public static final CreationStep INSTANCE = new PrizeMoneyAmountStep();

    @Override
    public Component question(final QuestionsTime plugin) {
        return TextUtils.normalWithPrefix("How much ").append(plugin.getEconomy().get().defaultCurrency().pluralDisplayName())
                .append(TextUtils.normal(" do players win if they give the correct answer? Answer with "))
                .append(TextUtils.special("/qtc amount"))
                .appendNewline()
                .append(TextUtils.composed("where ", "amount", " is a positive number"))
                .appendNewline()
                .append(TextUtils.normal("If you doesn't want, just answer with "))
                .append(TextUtils.commandShortcut("0"));
    }

    @Override
    public boolean handle(final Audience sender, final String answer, final QuestionCreator questionCreator) {
        if ("yes".equals(answer) && questionCreator.getMoneyPrize() >= 0) {
            return true;
        }
        try {
            final int prizeAmount = Integer.parseInt(answer);
            if (prizeAmount >= 0) {
                questionCreator.setMoneyPrize(prizeAmount);
                final Component message;
                if (prizeAmount > 0) {
                    message = TextUtils.composed("Is ", answer, " correct ?");
                } else {
                    message = TextUtils.normalWithPrefix("You really don't want to add money as a prize ?");
                }
                sender.sendMessage(message
                        .append(TextUtils.normal("If yes, answer with "))
                        .append(TextUtils.commandShortcut("yes"))
                        .append(TextUtils.normal(" or just re-answer to change the value")));
            } else {
                sender.sendMessage(TextUtils.specialWithPrefix(answer)
                        .append(TextUtils.normal(" is not a positive amount")));
            }
        } catch (final NumberFormatException e) {
            sender.sendMessage(TextUtils.specialWithPrefix(answer)
                    .append(TextUtils.normal(" is not a number")));
        }
        return false;
    }

    @Override
    public boolean shouldSkip(final QuestionsTime plugin, final QuestionCreator questionCreator) {
        return plugin.getEconomy().isEmpty();
    }

    @Override
    public CreationStep next(final QuestionCreator questionCreator) {
        return AnnouncePrizeStep.INSTANCE;
    }
}
