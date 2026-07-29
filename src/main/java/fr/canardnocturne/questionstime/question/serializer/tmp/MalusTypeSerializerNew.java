package fr.canardnocturne.questionstime.question.serializer.tmp;

import fr.canardnocturne.questionstime.command.set.question.tmp.qc3.section.QuestionSectionBase;
import fr.canardnocturne.questionstime.command.set.question.tmp.qc3.section.QuestionSectionMalus;
import fr.canardnocturne.questionstime.question.component.Malus;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.Set;
import java.util.stream.Collectors;

public class MalusTypeSerializerNew implements TypeSerializer<Malus> {

    private final Set<QuestionSectionMalus<?>> malusComponents;

    public MalusTypeSerializerNew(final Set<QuestionSectionBase<?, ?, ?>> components) {
        this.malusComponents = components.stream()
                .filter(sectionBase -> sectionBase instanceof QuestionSectionMalus)
                .map(sectionBase -> (QuestionSectionMalus<?>) sectionBase)
                .collect(Collectors.toSet());
    }

    @Nullable
    @Override
    public Malus deserialize(@NonNull final Type type, @NonNull final ConfigurationNode node) throws SerializationException {
        final Malus.Builder builder = Malus.builder();
        for (final QuestionSectionMalus<?> malusComponent : this.malusComponents) {
            malusComponent.load(node, builder);
        }
        return builder.build();
    }

    @Override
    public void serialize(@NonNull final Type type, @Nullable final Malus malus, @NonNull final ConfigurationNode node) throws SerializationException {
        if (malus != null && !malus.isEmpty()) {
            for (final QuestionSectionMalus<?> malusComponent : this.malusComponents) {
                malusComponent.save(node, malus);
            }
        }
    }
}
