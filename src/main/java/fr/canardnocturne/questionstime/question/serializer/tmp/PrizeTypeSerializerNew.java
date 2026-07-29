package fr.canardnocturne.questionstime.question.serializer.tmp;

import fr.canardnocturne.questionstime.command.set.question.tmp.qc3.section.QuestionSectionBase;
import fr.canardnocturne.questionstime.command.set.question.tmp.qc3.section.QuestionSectionPrize;
import fr.canardnocturne.questionstime.question.component.Prize;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.Set;
import java.util.stream.Collectors;

public class PrizeTypeSerializerNew implements TypeSerializer<Prize> {

    private final Set<QuestionSectionPrize<?>> prizeComponents;

    public PrizeTypeSerializerNew(final Set<QuestionSectionBase<?, ?, ?>> components) {
        this.prizeComponents = components.stream()
                .filter(sectionBase -> sectionBase instanceof QuestionSectionPrize)
                .map(sectionBase -> (QuestionSectionPrize<?>) sectionBase)
                .collect(Collectors.toSet());
    }

    @Nullable
    @Override
    public Prize deserialize(final Type type, final ConfigurationNode node) throws SerializationException {
        final int position = node.node("position").getInt(-1);
        if(position <= 0) {
            throw new SerializationException("Position must be greater than 0");
        }

        final Prize.Builder builder = Prize.builder(position);
        for (final QuestionSectionPrize<?> prizeComponent : this.prizeComponents) {
            prizeComponent.load(node, builder);
        }
        return builder.build();
    }

    @Override
    public void serialize(final Type type, final Prize prize, final ConfigurationNode node) throws SerializationException {
        if (this.needToSerialize(prize)) {
            for (final QuestionSectionPrize<?> prizeComponent : this.prizeComponents) {
                prizeComponent.save(node, prize);
            }
        }
    }

    private boolean needToSerialize(final Prize prize) {
        return prize != null && !prize.isEmpty();
    }

}
