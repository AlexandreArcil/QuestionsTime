package fr.canardnocturne.questionstime.command.set.prize;

import fr.canardnocturne.questionstime.QuestionException;
import fr.canardnocturne.questionstime.QuestionsTime;
import fr.canardnocturne.questionstime.question.QuestionComponent;
import fr.canardnocturne.questionstime.question.modifier.QuestionModifier;
import fr.canardnocturne.questionstime.question.Question;
import fr.canardnocturne.questionstime.question.ask.pool.QuestionPool;
import fr.canardnocturne.questionstime.question.save.QuestionRegister;
import fr.canardnocturne.questionstime.util.TextUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.EconomyService;

import java.io.IOException;
import java.util.Optional;

public class SetQuestionPrizesMoneyExecutor implements CommandExecutor {

    public static final Parameter.Value<Integer> POSITION = Parameter.integerNumber().key("position").build();
    public static final Parameter.Value<Integer> AMOUNT = Parameter.integerNumber().key("amount").build();

    private final Parameter.Value<Question> specificQuestionParameter;
    private final QuestionModifier questionModifier;
    private final QuestionPool questionPool;
    private final QuestionRegister questionRegister;

    public SetQuestionPrizesMoneyExecutor(final Parameter.Value<Question> specificQuestionParameter, final QuestionModifier questionModifier, final QuestionPool questionPool, final QuestionRegister questionRegister) {
        this.specificQuestionParameter = specificQuestionParameter;
        this.questionModifier = questionModifier;
        this.questionPool = questionPool;
        this.questionRegister = questionRegister;
    }

    @Override
    public CommandResult execute(final CommandContext context) throws CommandException {
        final Question question = context.requireOne(this.specificQuestionParameter);
        final Integer position = context.requireOne(POSITION);
        final Integer amount = context.requireOne(AMOUNT);
        try {
            final Question modifiedQuestion = this.questionModifier.set(question, QuestionComponent.PRIZE_MONEY, position, amount);
            this.questionRegister.replace(question, modifiedQuestion);
            this.questionPool.replace(question, modifiedQuestion);

            final TextComponent.Builder resultMessage = Component.text()
                    .append(TextUtils.composed("Prize amount for position ", String.valueOf(position)));
            if(amount == 0) {
                resultMessage.append(TextUtils.normal(" removed"));
            } else {
                resultMessage.append(TextUtils.composedWithoutPrefix(" set to ", String.valueOf(amount)));
                final Optional<Component> currencySymbolOpt = Sponge.server().serviceProvider().provide(EconomyService.class)
                        .map(EconomyService::defaultCurrency)
                        .map(Currency::symbol);
                if(currencySymbolOpt.isPresent()) {
                    resultMessage.append(currencySymbolOpt.get())
                            .append(TextUtils.normal(" !"));
                } else {
                    resultMessage.append(TextUtils.normal(" !"))
                            .appendNewline()
                            .append(QuestionsTime.PREFIX)
                            .append(Component.text("Warning: Economy plugin not found, the money won't be given.", NamedTextColor.YELLOW));
                }
            }
            context.sendMessage(resultMessage.build());
            return CommandResult.success();
        } catch (final QuestionException | IllegalArgumentException e) {
            return CommandResult.error(TextUtils.errorWithPrefix(e.getMessage()));
        } catch (final IOException e) {
            return CommandResult.error(TextUtils.errorWithPrefix("An error occurred while trying to save the question. See the log for details."));
        }
    }
}
