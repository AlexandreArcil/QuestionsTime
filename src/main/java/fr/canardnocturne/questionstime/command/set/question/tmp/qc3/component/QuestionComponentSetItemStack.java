package fr.canardnocturne.questionstime.command.set.question.tmp.qc3.component;

import fr.canardnocturne.questionstime.question.serializer.ItemStackSerializer;
import org.spongepowered.api.item.inventory.ItemStack;

public class QuestionComponentSetItemStack extends QuestionComponentSet<ItemStack> {

    public QuestionComponentSetItemStack(final String name, final String singular, final String plural) {
        super(name, singular, plural, ItemStackSerializer::fromString, ItemStackSerializer::fromItemStack);
    }

}
