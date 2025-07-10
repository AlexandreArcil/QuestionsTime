package fr.canardnocturne.questionstime.question.creation.steps;

import fr.canardnocturne.questionstime.question.creation.QuestionCreator;
import fr.canardnocturne.questionstime.util.TextUtils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.service.economy.EconomyService;

public class MalusAmountStep implements CreationStep {

    public static final CreationStep INSTANCE = new MalusAmountStep();

    @Override
    public Component question() {
        final EconomyService economyService = Sponge.server().serviceProvider().provide(EconomyService.class)
                .orElseThrow(() -> new IllegalStateException("Economy service should be present as this step is skipped if it's not"));
        return TextUtils.normalWithPrefix("How much ").append(economyService.defaultCurrency().pluralDisplayName())
                .append(TextUtils.normal(" do players lose if they give a wrong answer? Answer with "))
                .append(TextUtils.special("/qtc [amount]"))
                .appendNewline()
                .append(TextUtils.composed("where ", "amount", " is a positive number"))
                .appendNewline()
                .append(TextUtils.normalWithPrefix("If you doesn't want, just answer with "))
                .append(TextUtils.commandShortcut("0"));
    }

    @Override
    public boolean handle(final Audience sender, final String answer, final QuestionCreator questionCreator) {
        if ("yes".equals(answer) && questionCreator.getMoneyMalus() >= 0) {
            return true;
        }
        try {
            final int malusAmount = Integer.parseInt(answer);
            if (malusAmount >= 0) {
                questionCreator.setMoneyMalus(malusAmount);
                final Component message;
                if (malusAmount > 0) {
                    message = TextUtils.composed("Is ", answer, " correct ?");
                } else {
                    message = TextUtils.normalWithPrefix("You really don't want to add money as a malus ?");
                }
                sender.sendMessage(message
                        .append(TextUtils.normal(" If yes, answer with "))
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
    public boolean shouldSkip(final QuestionCreator questionCreator) {
        return Sponge.server().serviceProvider().provide(EconomyService.class).isEmpty();
    }

    @Override
    public Step next(final QuestionCreator questionCreator) {
        return AnnounceMalusStep.INSTANCE;
    }
}
