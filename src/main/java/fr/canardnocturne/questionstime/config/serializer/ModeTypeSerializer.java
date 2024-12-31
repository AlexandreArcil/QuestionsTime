package fr.canardnocturne.questionstime.config.serializer;

import fr.canardnocturne.questionstime.config.QuestionTimeConfiguration;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.Locale;

public class ModeTypeSerializer implements TypeSerializer<QuestionTimeConfiguration.Mode> {

    @Override
    public QuestionTimeConfiguration.Mode deserialize(final Type type, final ConfigurationNode node) throws SerializationException {
        try {
            return QuestionTimeConfiguration.Mode.valueOf(node.getString(QuestionTimeConfiguration.DefaultValues.MODE.toString()).toUpperCase(Locale.ROOT));
        } catch (final IllegalArgumentException e) {
            throw new SerializationException(e);
        }
    }

    @Override
    public void serialize(final Type type, final QuestionTimeConfiguration.@Nullable Mode obj, final ConfigurationNode node) throws SerializationException {
        if (obj != null) {
            node.set(obj.toString().toLowerCase());
        }
    }
}
