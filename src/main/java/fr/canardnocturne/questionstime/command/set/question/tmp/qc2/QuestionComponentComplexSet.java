package fr.canardnocturne.questionstime.command.set.question.tmp.qc2;

import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class QuestionComponentComplexSet<B, T, W> extends QuestionComponentComplexCollection<B, T, W, Set<W>> {

    public QuestionComponentComplexSet(final String name, final String singular, final String plural,
                                       final String commandDescription,
                                       final BiConsumer<B, Set<W>> setComponent,
                                       final Function<T, Set<W>> getComponent,
                                       final Function<String, W> valueMapper,
                                       final Function<W, String> valueUnmapper) {
        super(name, plural, commandDescription, singular, setComponent, getComponent, HashSet::new, valueMapper, valueUnmapper);
    }

    public QuestionComponentComplexSet(final String name, final String singular, final String plural,
                                       final String commandDescription,
                                       final BiConsumer<B, Set<W>> setComponent,
                                       final Function<T, Set<W>> getComponent,
                                       final Function<String, W> valueMapper,
                                       final Function<W, String> valueUnmapper,
                                       final Parameter.Value<String> addParameter) {
        super(name, plural, commandDescription, singular, setComponent, getComponent, HashSet::new, valueMapper, valueUnmapper, addParameter);
    }

    @Override
    public void load(final B builder, final ConfigurationNode node) throws SerializationException {
        final List<String> values = node.node(this.name).getList(String.class, Collections.emptyList());
        final Set<W> mappedValues = values.stream().map(this.valueMapper).collect(Collectors.toSet());
        this.setComponent.accept(builder, mappedValues);
    }

}
