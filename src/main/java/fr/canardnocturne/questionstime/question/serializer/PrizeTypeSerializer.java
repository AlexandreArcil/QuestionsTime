package fr.canardnocturne.questionstime.question.serializer;

import fr.canardnocturne.questionstime.question.component.Prize;
import fr.canardnocturne.questionstime.question.component.PrizeCommand;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PrizeTypeSerializer implements TypeSerializer<Prize> {

    @Nullable
    @Override
    public Prize deserialize(final Type type, final ConfigurationNode node) throws SerializationException {
        final int money = node.node("money").getInt(-1);
        final boolean announce = node.node("announce").getBoolean(false);
        final List<PrizeCommand> commandPrizes = node.node("commands").getList(PrizeCommand.class, new ArrayList<>());

        final ArrayList<ItemStack> itemPrizes = new ArrayList<>();
        final ConfigurationNode items = node.node("items");
        if (!items.isNull()) {
            for (ConfigurationNode itemNode : items.childrenList()) {
                final String item = itemNode.getString();
                if (item != null) {
                    try {
                        final ItemStack is = ItemStackSerializer.fromString(item);
                        itemPrizes.add(is);
                    } catch (IllegalArgumentException e) {
                        throw new SerializationException(e);
                    }
                } else {
                    throw new SerializationException("Found an undefined item as prize");
                }
            }
        }

        return new Prize(money, announce, itemPrizes.toArray(new ItemStack[0]), commandPrizes.toArray(new PrizeCommand[0]));
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
            if(prize.getCommands().length > 0) {
                node.node("commands").setList(PrizeCommand.class, Arrays.asList(prize.getCommands()));
            }
        }
    }

    private boolean needToSerialize(final Prize prize) {
        return prize != null && (prize.getMoney() > 0 || prize.getItemStacks().length > 0 || prize.getCommands().length > 0);
    }

}
