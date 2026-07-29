package fr.canardnocturne.questionstime.question.serializer;

import fr.canardnocturne.questionstime.question.component.Prize;
import fr.canardnocturne.questionstime.question.component.OutcomeCommand;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PrizeTypeSerializer implements TypeSerializer<Prize> {

    @Nullable
    @Override
    public Prize deserialize(final Type type, final ConfigurationNode node) throws SerializationException {
        final int money = node.node("money").getInt(-1);
        final boolean announce = node.node("announce").getBoolean(false);
        final Set<OutcomeCommand> commandPrizes = new HashSet<>(node.node("commands").getList(OutcomeCommand.class, new ArrayList<>()));

        final int position = node.node("position").getInt(1);
        if(position <= 0) {
            throw new SerializationException("Position must be greater than 0");
        }

        final Set<ItemStack> itemPrizes = new HashSet<>();
        final ConfigurationNode items = node.node("items");
        if (!items.isNull()) {
            for (final ConfigurationNode itemNode : items.childrenList()) {
                final String item = itemNode.getString();
                if (item != null) {
                    try {
                        final ItemStack is = ItemStackSerializer.fromString(item);
                        itemPrizes.add(is);
                    } catch (final IllegalArgumentException e) {
                        throw new SerializationException(e);
                    }
                } else {
                    throw new SerializationException("Found an undefined item as prize");
                }
            }
        }

        return new Prize(money, announce, itemPrizes, commandPrizes, position);
    }

    @Override
    public void serialize(final Type type, final Prize prize, final ConfigurationNode node) throws SerializationException {
        if (this.needToSerialize(prize)) {
            node.node("announce").set(prize.isAnnounce());
            node.node("money").set(prize.getMoney());
            node.node("position").set(prize.getPosition());
            if (!prize.getItemStacks().isEmpty()) {
                final List<String> isList = prize.getItemStacks().stream()
                        .map(ItemStackSerializer::fromItemStack)
                        .toList();
                node.node("items").set(isList);
            }
            if(!prize.getCommands().isEmpty()) {
                node.node("commands").setList(OutcomeCommand.class, prize.getCommands().stream().toList());
            }
        }
    }

    private boolean needToSerialize(final Prize prize) {
        return prize != null && (prize.getMoney() > 0 || !prize.getItemStacks().isEmpty() || !prize.getCommands().isEmpty());
    }

}
