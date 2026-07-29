package fr.canardnocturne.questionstime.question.serializer.tmp;

import fr.canardnocturne.questionstime.command.set.question.tmp.qc3.section.QuestionSectionBase;
import fr.canardnocturne.questionstime.command.set.question.tmp.qc3.section.QuestionSectionQuestion;
import fr.canardnocturne.questionstime.question.Question;
import fr.canardnocturne.questionstime.question.component.Malus;
import fr.canardnocturne.questionstime.question.component.Prize;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class QuestionSerializerNew implements TypeSerializer<Question> {

    private final Set<QuestionSectionQuestion<?>> questionComponents;

    public QuestionSerializerNew(final Set<QuestionSectionBase<?, ?, ?>> components) {
        this.questionComponents = components.stream()
                .filter(sectionBase -> sectionBase instanceof QuestionSectionQuestion)
                .map(sectionBase -> (QuestionSectionQuestion<?>) sectionBase)
                .collect(Collectors.toSet());
    }

    @Override
    public Question deserialize(final Type type, final ConfigurationNode node) throws SerializationException {
        if (this.isInvalid(node)) {
            throw new SerializationException(node, Question.class, "The question " + node.key() + " contain one or several errors. " +
                    "Check if it contains the sections \"question\" and \"answer\" at least.");
        }

        final Question.QuestionBuilder builder = Question.builder();
        for (final QuestionSectionQuestion<?> component : questionComponents) {
            component.load(node, builder);
        }

        final Set<Prize> prizes = new HashSet<>(node.node("prizes").getList(Prize.class, Collections.emptyList()));
        builder.setPrizes(prizes);
        final Malus malus = node.node("malus").get(Malus.class);
        builder.setMalus(malus);

        try {
            return builder.build();
        } catch (final Exception e) {
            throw new SerializationException(node, Question.class, "Failed to build question.", e);
        }
    }

    @Override
    public void serialize(final Type type, @Nullable final Question question, final ConfigurationNode node) throws SerializationException {
        if(question != null) {
            for (final QuestionSectionQuestion<?> component : questionComponents) {
                component.save(node, question);
            }

            if (!question.getPrizes().isEmpty()) {
                node.node("prizes").setList(Prize.class, new ArrayList<>(question.getPrizes()));
            }

            final Optional<Malus> malusOptional = question.getMalus();
            if (malusOptional.isPresent()) {
                node.node("malus").set(Malus.class, malusOptional.get());
            }
        }
    }

    private boolean isInvalid(final ConfigurationNode node) {
        return node.node("question").empty() || node.node("answer").empty();
    }
}
