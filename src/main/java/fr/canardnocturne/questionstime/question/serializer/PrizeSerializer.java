package fr.canardnocturne.questionstime.question.serializer;

import fr.canardnocturne.questionstime.question.component.Prize;
import fr.canardnocturne.questionstime.util.ItemStackSerializer;
import org.apache.logging.log4j.Logger;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PrizeSerializer implements TypeSerializer<Prize> {

    private final Logger logger;

    public PrizeSerializer(final Logger logger) {
        this.logger = logger;
    }

    @Nullable
    @Override
    public Prize deserialize(final Type type, final ConfigurationNode node) throws SerializationException {
        final int money = node.node("money").getInt(-1);
        final boolean announce = node.node("announce").getBoolean(false);

        final ArrayList<ItemStack> itemPrizes = new ArrayList<>();
        final ConfigurationNode items = node.node("items");
        if (!items.isNull()) {
            items.childrenList().forEach(itemNode -> {
                final String item = itemNode.getString();
                if (item != null) {
                    final ItemStack is = ItemStackSerializer.fromString(item);
                    if (!is.type().equals(ItemTypes.AIR.get())) {
                        itemPrizes.add(is);
                    } else {
                        logger.error("Error when convert '{}' to an item stack, or the item type is Air", item);
                    }
                } else {
                    logger.error("Found an undefined item as prize");
                }
            });
        }
        return new Prize(money, announce, itemPrizes.toArray(new ItemStack[0]));
    }

    @Override
    public void serialize(final Type type, final Prize prize, final ConfigurationNode node) throws SerializationException {
        if (this.needToSerialize(prize)) {
            node.node("announce").set(prize.isAnnounce());
            node.node("money").set(prize.getMoney());
            if (prize.getItemStacks().length > 0) {
                final List<String> isList = Arrays.stream(prize.getItemStacks())
                        .map(ItemStackSerializer::fromItemStack)
                        .toList();
                node.node("items").set(isList);
            }
        }
    }

    private boolean needToSerialize(final Prize prize) {
        return prize != null && (prize.getMoney() > 0 || prize.getItemStacks().length > 0);
    }

}
