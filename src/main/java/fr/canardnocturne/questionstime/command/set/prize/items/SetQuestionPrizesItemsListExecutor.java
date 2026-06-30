package fr.canardnocturne.questionstime.command.set.prize.items;

import fr.canardnocturne.questionstime.QuestionsTime;
import fr.canardnocturne.questionstime.question.Question;
import fr.canardnocturne.questionstime.question.serializer.ItemStackSerializer;
import fr.canardnocturne.questionstime.util.TextUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;

import java.util.stream.Stream;

public class SetQuestionPrizesItemsListExecutor implements CommandExecutor {

    private final Parameter.Value<Question> specificQuestionParameter;

    public SetQuestionPrizesItemsListExecutor(final Parameter.Value<Question> specificQuestionParameter) {
        this.specificQuestionParameter = specificQuestionParameter;
    }

    @Override
    public CommandResult execute(final CommandContext context) throws CommandException {
        final Question question = context.requireOne(specificQuestionParameter);
        final TextComponent.Builder message = Component.text();
        if(question.getPrizes().isEmpty()) {
            message.append(TextUtils.normalWithPrefix("No prizes"));
        } else {
            message.append(TextUtils.normalWithPrefix("Prize items: ")).appendNewline()
                    .append(Component.join(JoinConfiguration.newlines(), question.getPrizes().stream().map(prize -> {
                final TextComponent.Builder prizeMessage = Component.text().append(TextUtils.normalWithPrefix("Position " + prize.getPosition() + ":"))
                        .appendNewline();
                if (prize.getItemStacks().length == 0) {
                    prizeMessage.append(TextUtils.normalWithPrefix("  No items"));
                } else {
                    prizeMessage.append(Component.join(JoinConfiguration.newlines(),  Stream.of(prize.getItemStacks()).map(item -> {
                        final String itemSerialized = ItemStackSerializer.fromItemStack(item);
                        return QuestionsTime.PREFIX.appendSpace().appendSpace()
                                .append(Component.text("[X]", NamedTextColor.RED, TextDecoration.BOLD)
                                        .clickEvent(ClickEvent.runCommand("/qt set question \"" + question.getQuestion() + "\" prizes items remove " + prize.getPosition() + " " + itemSerialized))
                                        .hoverEvent(HoverEvent.showText(Component.text("Delete the item"))))
                                .appendSpace()
                                .append(TextUtils.displayItem(item));
                    }).toList()));
                }
                return prizeMessage.build();
            }).toList()));
        }
        message.appendNewline()
                        .append(TextUtils.normalWithPrefix("You can add an item with the command "))
                        .append(TextUtils.commandSuggestion("set question \"" + question.getQuestion() + "\" prizes items add position_number {ModID:}ItemID{;Count}{;DisplayName}{;Lore}"));
        context.sendMessage(message.build());
        return CommandResult.success();
    }
}
