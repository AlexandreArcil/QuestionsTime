package fr.canardnocturne.questionstime.question.creation.steps;

import fr.canardnocturne.questionstime.question.creation.QuestionCreator;
import fr.canardnocturne.questionstime.question.serializer.ItemStackSerializer;
import fr.canardnocturne.questionstime.util.NumberUtils;
import fr.canardnocturne.questionstime.util.TextUtils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public class PrizeItemsStep implements CreationStep {

    public static final CreationStep INSTANCE = new PrizeItemsStep();

    private static final String ITEM_FORMAT = "{ModID:}[ItemID];{Count};{DisplayName};{Lore};{Position}";

    @Override
    public Component question() {
        return TextUtils.composed("Add an item as prize with ",  "/qtc add "+ITEM_FORMAT)
                .appendNewline().append(TextUtils.composed("List prizes with ")).append(TextUtils.commandShortcut("list"))
                .appendNewline().append(TextUtils.normalWithPrefix("Remove prizes with ")).append(TextUtils.commandShortcut("list"))
                    .append(TextUtils.composedWithoutPrefix(" then clicking on the ", "[X]", " icon"))
                .appendNewline().append(TextUtils.normalWithPrefix("You can format the messages by following the "))
                    .append(Component.text("MiniMessage formatting", NamedTextColor.YELLOW, TextDecoration.UNDERLINED)
                            .clickEvent(ClickEvent.openUrl("https://docs.advntr.dev/minimessage/format.html")))
                .appendNewline().append(TextUtils.composed("The ", "Lore", " can have multiple lines by separating them with ", "<br>"))
                .appendNewline().append(TextUtils.composed("The ", "Position", " is the winner position, default is the first"))
                .appendNewline().append(TextUtils.example("/qtc minecraft:stone;5;<yellow><bold>Old Stone;Emits a low light"))
                .appendNewline().append(TextUtils.normalWithPrefix("To go to the next step or skip this step, type "))
                .append(TextUtils.commandShortcut("confirm"));
    }

    @Override
    public boolean handle(final Audience sender, final String answer, final QuestionCreator questionCreator) {
        if ("confirm".equals(answer)) {
            return true;
        }
        final String[] args = answer.split(" ", 2);
        switch (args[0]) {
            case "add":
                if (args.length < 2) {
                    sender.sendMessage(TextUtils.normalWithPrefix("You must provide an item to add"));
                } else {
                    this.addItemPrize(sender, questionCreator, args[1]);
                }
                break;
            case "list":
                this.listItemPrizes(questionCreator, sender);
                break;
            case "del":
                if (args.length < 2) {
                    sender.sendMessage(TextUtils.normalWithPrefix("You must provide an item to remove"));
                } else {
                    this.removeItemPrize(sender, questionCreator, args[1]);
                }
                break;
            default:
                sender.sendMessage(TextUtils.normalWithPrefix("Unknown command: " + args[0]));
                break;
        }
        return false;
    }

    private void addItemPrize(final Audience sender, final QuestionCreator questionCreator, final String answer) {
        try {
            final TextUtils.AnswerPosition answerPosition = TextUtils.extractPositionFromAnswer(answer);
            final int position = answerPosition.position();
            if(position <= 0) {
                sender.sendMessage(TextUtils.specialWithPrefix(String.valueOf(position))
                        .append(TextUtils.normal(" is not a positive number")));
                return;
            }
            final ItemStack is = ItemStackSerializer.fromString(answerPosition.answer());
            questionCreator.addItemPrize(position, is);
            sender.sendMessage(TextUtils.normalWithPrefix("Added ")
                    .append(TextUtils.displayItem(is))
                    .append(Component.text(" * " + is.quantity(), NamedTextColor.LIGHT_PURPLE))
                    .append(TextUtils.composedWithoutPrefix(" for ", NumberUtils.toOrdinal(position), " winner")));
        } catch (final IllegalArgumentException e) {
            sender.sendMessage(TextUtils.normalWithPrefix(e.getMessage()));
        }
    }

    private void listItemPrizes(final QuestionCreator questionCreator, final Audience sender) {
        final Map<Integer, List<ItemStack>> itemsPrize = questionCreator.getItemsPrize();
        if (itemsPrize.isEmpty()) {
            sender.sendMessage(TextUtils.normalWithPrefix("No item prizes added yet."));
            return;
        }
        final TextComponent.Builder listItemPrizesBuilder = Component.text().append(TextUtils.normalWithPrefix("Item prizes:")).appendNewline();
        listItemPrizesBuilder.append(Component.join(JoinConfiguration.newlines(), itemsPrize.entrySet().stream().map(entry -> {
            final int position = entry.getKey();
            final List<ItemStack> items = entry.getValue();
            return Component.text()
                    .append(TextUtils.specialWithPrefix(NumberUtils.toOrdinal(position)))
                    .append(TextUtils.normal(": "))
                    .appendNewline()
                    .append(Component.join(JoinConfiguration.newlines(),
                            items.stream().map(itemStack -> TextUtils.normalWithPrefix("- ")
                                    .append(TextUtils.displayItem(itemStack))
                                    .appendSpace()
                                    .append(Component.text("[X]", NamedTextColor.RED, TextDecoration.BOLD)
                                            .hoverEvent(HoverEvent.showText(TextUtils.normal("Click to remove this command prize")))
                                            .clickEvent(ClickEvent.runCommand("/qtc del " + ItemStackSerializer.fromItemStack(itemStack) + ";" + position)
                                            ))).toList()))
                    .build();
        }).toList()));
        sender.sendMessage(listItemPrizesBuilder.build());
    }

    private void removeItemPrize(final Audience sender, final QuestionCreator questionCreator, final String answer) {
        try {
            final TextUtils.AnswerPosition answerPosition = TextUtils.extractPositionFromAnswer(answer);
            final int position = answerPosition.position();
            if(position <= 0) {
                sender.sendMessage(TextUtils.specialWithPrefix(String.valueOf(position))
                        .append(TextUtils.normal(" is not a positive number")));
                return;
            }
            final ItemStack is = ItemStackSerializer.fromString(answerPosition.answer());
            final boolean removed = questionCreator.removeItemPrize(position, is);
            if (!removed) {
                sender.sendMessage(TextUtils.normalWithPrefix("Item prize ")
                        .append(TextUtils.displayItem(is))
                        .append(TextUtils.composedWithoutPrefix(" for the ", NumberUtils.toOrdinal(position), " winner not found.")));
            } else {
                sender.sendMessage(TextUtils.normalWithPrefix("Removed item prize ")
                        .append(TextUtils.displayItem(is))
                        .append(TextUtils.composedWithoutPrefix(" for the ", NumberUtils.toOrdinal(position), " winner.")));
            }
        } catch (final IllegalArgumentException e) {
            sender.sendMessage(TextUtils.normalWithPrefix(e.getMessage()));
        }
    }

    @Override
    public boolean shouldSkip(final QuestionCreator questionCreator) {
        return false;
    }

    @Override
    public Step next(final QuestionCreator questionCreator) {
        return PrizeCommandsStep.INSTANCE;
    }
}
