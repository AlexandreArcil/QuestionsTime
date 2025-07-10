package fr.canardnocturne.questionstime.question.creation.steps;

import fr.canardnocturne.questionstime.question.creation.QuestionCreator;
import fr.canardnocturne.questionstime.question.serializer.ItemStackSerializer;
import fr.canardnocturne.questionstime.util.NumberUtils;
import fr.canardnocturne.questionstime.util.TextUtils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.spongepowered.api.item.inventory.ItemStack;

public class PrizeItemsStep implements CreationStep {

    public static final CreationStep INSTANCE = new PrizeItemsStep();

    private static final String ITEM_FORMAT = "{ModID:}[ItemID];{Count};{DisplayName};{Lore};{Position}";

    @Override
    public Component question() {
        return TextUtils.composed("Add an item as prize with ",  "/qtc "+ITEM_FORMAT)
                .appendNewline().append(TextUtils.composed("Which ", "[...]", " is mandatory"))
                .appendNewline().append(TextUtils.composed("And ", "{...}", " optional"))
                .appendNewline().append(TextUtils.composed("The ", "Lore", " can have multiple lines by separating them with ", "\\n"))
                .appendNewline().append(TextUtils.composed("The ", "Position", " is the winner position, default is the first"))
                .appendNewline().append(TextUtils.example("/qtc minecraft:stone;5;Old Stone;Emits a low light"))
                .appendNewline().append(TextUtils.normalWithPrefix("To go to the next step or skip this step, type "))
                .append(TextUtils.commandShortcut("confirm"));
    }

    @Override
    public boolean handle(final Audience sender, final String answer, final QuestionCreator questionCreator) {
        if ("confirm".equals(answer)) {
            return true;
        }

        try {
            final TextUtils.AnswerPosition answerPosition = TextUtils.extractPositionFromAnswer(answer);
            final int position = answerPosition.position();
            if(position <= 0) {
                sender.sendMessage(TextUtils.specialWithPrefix(String.valueOf(position))
                        .append(TextUtils.normal(" is not a positive number")));
                return false;
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
        return false;
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
