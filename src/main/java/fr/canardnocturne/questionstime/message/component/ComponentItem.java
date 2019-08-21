package fr.canardnocturne.questionstime.message.component;

import fr.canardnocturne.questionstime.util.TextUtils;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.item.inventory.ItemStack;

public class ComponentItem extends MessageComponent<ItemStack> {

    public ComponentItem(final String name) {
        super(name);
    }

    @Override
    public Component process(final ItemStack itemStack) {
        return TextUtils.displayItem(itemStack);
    }

}
