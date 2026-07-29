package fr.canardnocturne.questionstime.question.serializer;

import fr.canardnocturne.questionstime.question.component.Malus;
import fr.canardnocturne.questionstime.question.component.OutcomeCommand;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MalusTypeSerializer implements TypeSerializer<Malus> {

    @Nullable
    @Override
    public Malus deserialize(@NonNull final Type type, @NonNull final ConfigurationNode value) throws SerializationException {
        final boolean announce = value.node("announce").getBoolean(false);
        final int money = value.node("money").getInt(-1);
        final Set<OutcomeCommand> commands = new HashSet<>(value.node("commands").getList(OutcomeCommand.class, List.of()));
        return new Malus(money, announce, commands);
    }

    @Override
    public void serialize(@NonNull final Type type, @Nullable final Malus malus, @NonNull final ConfigurationNode value) throws SerializationException {
        if (malus != null && !malus.isEmpty()) {
            value.node("announce").set(malus.isAnnounce());
            value.node("money").set(malus.getMoney());
            value.node("commands").setList(OutcomeCommand.class, malus.getCommands().stream().toList());
        }
    }
}
