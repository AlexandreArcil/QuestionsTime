package fr.canardnocturne.questionstime.command.set.question.tmp.qc2;

import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class QuestionComponentComplexList<B, T, W> extends QuestionComponentComplexCollection<B, T, W, List<W>> {

    public QuestionComponentComplexList(final String name, final String singular, final String plural,
                                       final String commandDescription,
                                       final BiConsumer<B, List<W>> setComponent,
                                       final Function<T, List<W>> getComponent,
                                       final Function<String, W> valueMapper,
                                       final Function<W, String> valueUnmapper) {
        super(name, plural, commandDescription, singular, setComponent, getComponent, ArrayList::new, valueMapper, valueUnmapper);
    }

    public QuestionComponentComplexList(final String name, final String singular, final String plural,
                                       final String commandDescription,
                                       final BiConsumer<B, List<W>> setComponent,
                                       final Function<T, List<W>> getComponent,
                                       final Function<String, W> valueMapper,
                                       final Function<W, String> valueUnmapper,
                                       final Parameter.Value<String> addParameter) {
        super(name, plural, commandDescription, singular, setComponent, getComponent, ArrayList::new, valueMapper, valueUnmapper, addParameter);
    }

    @Override
    public void load(final B builder, final ConfigurationNode node) throws SerializationException {
        final List<String> values = node.node(this.name).getList(String.class, Collections.emptyList());
        final List<W> mappedValues = values.stream().map(this.valueMapper).toList();
        this.setComponent.accept(builder, mappedValues);
    }

}
