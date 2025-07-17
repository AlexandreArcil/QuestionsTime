package fr.canardnocturne.questionstime.question.serializer;

import fr.canardnocturne.questionstime.question.component.OutcomeCommand;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public class OutcomeCommandTypeSerializer implements TypeSerializer<OutcomeCommand> {

    @Override
    public OutcomeCommand deserialize(final Type type, final ConfigurationNode node) throws SerializationException {
        final String command = node.getString();
        if(command != null) {
            try {
                return OutcomeCommandSerializer.deserialize(command);
            } catch (final IllegalArgumentException e) {
                throw new SerializationException(e);
            }
        } else {
            throw new SerializationException("Found an undefined command as prize");
        }
    }

    @Override
    public void serialize(final Type type, @Nullable final OutcomeCommand outcomeCommand, final ConfigurationNode node) throws SerializationException {
        if(outcomeCommand != null) {
            node.set(OutcomeCommandSerializer.serialize(outcomeCommand));
        }
    }
}
