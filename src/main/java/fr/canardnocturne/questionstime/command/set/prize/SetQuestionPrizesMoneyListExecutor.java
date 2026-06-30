package fr.canardnocturne.questionstime.command.set.prize;

import fr.canardnocturne.questionstime.QuestionsTime;
import fr.canardnocturne.questionstime.question.Question;
import fr.canardnocturne.questionstime.util.TextUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.EconomyService;

import java.util.Optional;

public class SetQuestionPrizesMoneyListExecutor implements CommandExecutor {

    private final Parameter.Value<Question> specificQuestionParameter;

    public SetQuestionPrizesMoneyListExecutor(final Parameter.Value<Question> specificQuestionParameter) {
        this.specificQuestionParameter = specificQuestionParameter;
    }

    @Override
    public CommandResult execute(final CommandContext context) throws CommandException {
        final Question question = context.requireOne(specificQuestionParameter);
        if(question.getPrizes().isEmpty()) {
            context.sendMessage(TextUtils.normalWithPrefix("No prizes"));
        } else {
            final TextComponent.Builder message = Component.text().append(TextUtils.normalWithPrefix("Prize money: ")).appendNewline();
            final Optional<Component> currencySymbolOpt = Sponge.server().serviceProvider().provide(EconomyService.class)
                    .map(EconomyService::defaultCurrency)
                    .map(Currency::symbol);
            message.append(Component.join(JoinConfiguration.newlines(), question.getPrizes().stream().map(prize -> {
                final TextComponent.Builder prizeMessage = Component.text().append(TextUtils.normalWithPrefix("Position " + prize.getPosition() + ":"))
                        .appendNewline();
                if (prize.getMoney() == 0) {
                    prizeMessage.append(TextUtils.normalWithPrefix("  No money"));
                } else {
                    prizeMessage.append(QuestionsTime.PREFIX.appendSpace().appendSpace()
                            .append(Component.text("[X]", NamedTextColor.RED, TextDecoration.BOLD)
                                        .clickEvent(ClickEvent.runCommand("/qt set question \"" + question.getQuestion() + "\" prizes money set " + prize.getPosition() + " 0"))
                                        .hoverEvent(HoverEvent.showText(Component.text("Remove money"))))
                                .appendSpace()
                                .append(TextUtils.normal(String.valueOf(prize.getMoney()))));
                    currencySymbolOpt.ifPresent(prizeMessage::append);
                }
                return prizeMessage.build();
            }).toList()));
            context.sendMessage(message.build());
        }
        return CommandResult.success();
    }
}
