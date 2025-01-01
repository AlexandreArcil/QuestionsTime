package fr.canardnocturne.questionstime.message.component;

import fr.canardnocturne.questionstime.QuestionsTime;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.registry.RegistryTypes;
import org.spongepowered.plugin.PluginContainer;

import java.util.Optional;

public class ComponentModID extends MessageComponent<ItemStack> {

    public ComponentModID(final String name) {
        super(name);
    }

    @Override
    public Component process(final ItemStack itemStack) {
        final String modId = itemStack.type().key(RegistryTypes.ITEM_TYPE).namespace();
        if (modId.startsWith(ResourceKey.MINECRAFT_NAMESPACE))
            return Component.empty();
        else {
            final PluginContainer pluginCont = Sponge.pluginManager().plugin(modId)
                    .orElseThrow(() -> new IllegalArgumentException("The mod '" + modId + "' doesn't exist, but it should have been detected when loading the config, report the bug please."));
            return Component.text(pluginCont.metadata().name().orElse(modId));
        }
    }
}
