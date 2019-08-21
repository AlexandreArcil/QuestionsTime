package fr.canardnocturne.questionstime.message.component.set;

import fr.canardnocturne.questionstime.message.component.MessageComponents;
import fr.canardnocturne.questionstime.message.format.MessageFormat;
import org.spongepowered.api.item.inventory.ItemStack;

public interface SetModId<T extends MessageFormat.Format> extends SetComponent {

    default T setModId(final ItemStack itemStack) {
        setComponent(MessageComponents.MOD_ID, itemStack);
        return (T) this;
    }

}
