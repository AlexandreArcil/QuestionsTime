package fr.canardnocturne.questionstime.question.creation.steps;

import fr.canardnocturne.questionstime.QuestionsTime;
import fr.canardnocturne.questionstime.question.creation.QuestionCreator;
import fr.canardnocturne.questionstime.util.ItemStackSerializer;
import fr.canardnocturne.questionstime.util.TextUtils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;

public class PrizeItemsStep implements CreationStep {

    public static final CreationStep INSTANCE = new PrizeItemsStep();

    private static final String ITEM_FORMAT = "[ModID:]{ItemID};[Count];[DisplayName];[Lore]";

    @Override
    public Component question(final QuestionsTime plugin) {
        return TextUtils.composed("Add an item as prize with /qtc ", ITEM_FORMAT, "")
                .appendNewline().append(TextUtils.composed("Which ", "{...}", " is obligatory"))
                .appendNewline().append(TextUtils.composed("And ", "[...]", " optional"))
                .appendNewline().append(TextUtils.composed("The ", "Lore", " can have multiple lines by separating them with ", "\\n"))
                .appendNewline().append(TextUtils.example("/qtc minecraft:stone;5;Old Stone;Emits a low light"))
                .appendNewline().append(TextUtils.normalWithPrefix("To go to the next step or skip this step, type "))
                .append(TextUtils.commandShortcut("confirm"));
    }

    @Override
    public boolean handle(final Audience sender, final String answer, final QuestionCreator questionCreator) {
        if ("confirm".equals(answer)) {
            return true;
        }

        final ItemStack is = ItemStackSerializer.fromString(answer);
        if (!is.type().isAnyOf(ItemTypes.AIR.get())) {
            questionCreator.addItemPrize(is);
            sender.sendMessage(TextUtils.normalWithPrefix("Added ")
                    .append(TextUtils.displayItem(is))
                    .append(Component.text(" * " + is.quantity(), NamedTextColor.LIGHT_PURPLE))
                    .append(TextUtils.normal("")));
        } else {
            sender.sendMessage(TextUtils.specialWithPrefix(answer)
                    .append(TextUtils.composedWithoutPrefix(" doesn't match the expected item format : ", ITEM_FORMAT)));
        }
        return false;
    }

    @Override
    public boolean shouldSkip(final QuestionsTime plugin, final QuestionCreator questionCreator) {
        return false;
    }

    @Override
    public CreationStep next(final QuestionCreator questionCreator) {
        return PrizeMoneyAmountStep.INSTANCE;
    }
}
