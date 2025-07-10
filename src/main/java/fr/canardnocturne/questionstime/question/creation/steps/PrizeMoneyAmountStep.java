package fr.canardnocturne.questionstime.question.creation.steps;

import fr.canardnocturne.questionstime.question.creation.QuestionCreator;
import fr.canardnocturne.questionstime.util.NumberUtils;
import fr.canardnocturne.questionstime.util.TextUtils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.service.economy.EconomyService;

public class PrizeMoneyAmountStep implements CreationStep {

    public static final CreationStep INSTANCE = new PrizeMoneyAmountStep();

    private static final String COMMAND_FORMAT = "[amount];{position}";

    @Override
    public Component question() {
        final EconomyService economyService = Sponge.server().serviceProvider().provide(EconomyService.class)
                .orElseThrow(() -> new IllegalStateException("Economy service should be present as this step is skipped if it's not"));
        return TextUtils.normalWithPrefix("How much ").append(economyService.defaultCurrency().pluralDisplayName())
                .append(TextUtils.normal(" do players win if they give the correct answer? Answer with "))
                .append(TextUtils.special("/qtc " + COMMAND_FORMAT))
                .appendNewline()
                .append(TextUtils.composed("where ", "amount", " is a positive number"))
                .appendNewline()
                .append(TextUtils.composed("and ", "position", " is an optional winner position, default is the first"))
                .appendNewline()
                .append(TextUtils.normal("If you doesn't want, just answer with "))
                .append(TextUtils.commandShortcut("confirm"));
    }

    @Override
    public boolean handle(final Audience sender, final String answer, final QuestionCreator questionCreator) {
        if ("confirm".equals(answer)) {
            return true;
        }
        final String[] respond = answer.split(";");
        if (respond.length > 2) {
            sender.sendMessage(TextUtils.composed("The amount prize ", answer, " doesn't follow the syntax ", COMMAND_FORMAT));
            return false;
        }
        final String amount = respond.length == 2 ? respond[0] : answer;
        final int prizeAmount;
        try {
            prizeAmount = Integer.parseInt(amount);
        } catch (final NumberFormatException e) {
            sender.sendMessage(TextUtils.specialWithPrefix(answer)
                    .append(TextUtils.normal(" is not a number")));
            return false;
        }
        if(prizeAmount < 0) {
            sender.sendMessage(TextUtils.specialWithPrefix(amount)
                    .append(TextUtils.normal(" is not a positive amount")));
            return false;
        }
        int position = 1;
        if (respond.length == 2) {
            try {
                position = Integer.parseInt(respond[1]);
            } catch (final NumberFormatException e) {
                sender.sendMessage(TextUtils.specialWithPrefix(respond[1])
                        .append(TextUtils.normal(" is not a number")));
                return false;
            }
        }
        if(position <= 0) {
            sender.sendMessage(TextUtils.specialWithPrefix(respond[1])
                    .append(TextUtils.normal(" is not a positive number")));
            return false;
        }

        questionCreator.setMoneyPrize(position, prizeAmount);
        final EconomyService economyService = Sponge.server().serviceProvider().provide(EconomyService.class)
                .orElseThrow(() -> new IllegalStateException("Economy service should be present as this step is skipped if it's not"));
        final Component currency = economyService.defaultCurrency().pluralDisplayName();
        if(prizeAmount > 0) {
            sender.sendMessage(TextUtils.composed("The ", NumberUtils.toOrdinal(position), " winner will gain ", amount).appendSpace().append(currency));
        } else {
            sender.sendMessage(TextUtils.composed("", NumberUtils.toOrdinal(position), " winner will not gain ").append(currency));
        }
        return false;
    }

    @Override
    public boolean shouldSkip(final QuestionCreator questionCreator) {
        return Sponge.server().serviceProvider().provide(EconomyService.class).isEmpty();
    }

    @Override
    public Step next(final QuestionCreator questionCreator) {
        return MissingPrizePosition.INSTANCE;
    }
}
