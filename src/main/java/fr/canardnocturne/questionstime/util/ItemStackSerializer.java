package fr.canardnocturne.questionstime.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.SystemSubject;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.registry.RegistryEntry;
import org.spongepowered.api.registry.RegistryTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ItemStackSerializer {

    /**
     * Syntax : [ModID:]{ItemID};[Count];[DisplayName];[Lore]
     * Where {...} is obligatory and [...] not
     */
    public static ItemStack fromString(final String syntax) {
        final SystemSubject console = Sponge.systemSubject();
        final String[] itemSplit = syntax.split(";");
        int count = 1;
        Component customName = Component.empty();
        final ArrayList<Component> lore = new ArrayList<>();

        final String[] itemID = itemSplit[0].split(":");
        final String namespace = itemID.length >= 2 ? itemID[0] : ResourceKey.MINECRAFT_NAMESPACE;
        final String itemId = itemID.length >= 2 ? itemID[1] : itemID[0];
        final ItemType it = Sponge.game().registry(RegistryTypes.ITEM_TYPE).findEntry(ResourceKey.of(namespace, itemId))
                .map(RegistryEntry::value).orElse(ItemTypes.AIR.get());
        if (it.equals(ItemTypes.AIR)) {
            console.sendMessage(TextUtils.errorWithPrefix("  The item's id \"" + namespace + ":" + itemId + "\") doesn't exist"));
        }
        if (itemSplit.length >= 2) {
            final String itemCount = itemSplit[1];
            if (!itemCount.isEmpty()) {
                if (StringUtils.isNumeric(itemCount)) {
                    if (Integer.parseInt(itemCount) >= 0) {
                        count = Integer.parseInt(itemCount);
                    } else {
                        console.sendMessage(TextUtils.errorWithPrefix("  The item's count is negative (\"" + syntax + "\" -> \"" + itemCount + "\")"));
                    }
                } else {
                    console.sendMessage(TextUtils.errorWithPrefix("  The item's count isn't an number (\"" + syntax + "\" -> \"" + itemCount + "\")"));
                }
            }
        }
        if (itemSplit.length >= 3) {
            customName = MiniMessage.miniMessage().deserialize(itemSplit[2]);
        }
        if (itemSplit.length >= 4) {
            final String[] itemLore = itemSplit[3].split("\\\\n");
            for (final String line : itemLore) {
                lore.add(MiniMessage.miniMessage().deserialize(line));
            }
        }

        final ItemStack.Builder isBuilder = ItemStack.builder().itemType(it).quantity(count);
        if (!customName.equals(Component.empty())) {
            isBuilder.add(Keys.CUSTOM_NAME, customName);
        }
        if (!lore.isEmpty()) {
            isBuilder.add(Keys.LORE, lore);
        }
        return isBuilder.build();
    }

    public static String fromItemStack(final ItemStack is) {
        final StringBuilder isSer = new StringBuilder();
        isSer.append(is.type().key(RegistryTypes.ITEM_TYPE).asString());
        final boolean hasMultiple = is.quantity() > 1;
        if (hasMultiple) {
            isSer.append(";");
            isSer.append(is.quantity());
        }
        final Optional<Component> customNameOpt = is.get(Keys.CUSTOM_NAME);
        customNameOpt.ifPresent(customName -> {
            if (!hasMultiple) {
                isSer.append(";");
            }
            isSer.append(";");
            isSer.append(MiniMessage.miniMessage().serialize(customName));
        });
        is.get(Keys.LORE).ifPresent(lore -> {
            if (!hasMultiple) {
                isSer.append(";");
            }
            if (customNameOpt.isEmpty()) {
                isSer.append(";");
            }
            isSer.append(";");
            final List<String> loreStr = lore.stream().map(line -> MiniMessage.miniMessage().serialize(line)).toList();
            isSer.append(String.join("\n", loreStr));
        });
        return isSer.toString();
    }

}
