package fr.canardnocturne.questionstime.message.component.set;

import fr.canardnocturne.questionstime.message.component.MessageComponents;
import fr.canardnocturne.questionstime.message.format.MessageFormat;
import org.spongepowered.api.item.inventory.ItemStack;

public interface SetItem<T extends MessageFormat.Format> extends SetComponent {

    default T setItem(final ItemStack itemStack) {
        setComponent(MessageComponents.ITEM, itemStack);
        return (T) this;
    }

}
