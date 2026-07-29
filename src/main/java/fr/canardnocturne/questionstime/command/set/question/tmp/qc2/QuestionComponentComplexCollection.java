package fr.canardnocturne.questionstime.command.set.question.tmp.qc2;

import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class QuestionComponentComplexCollection<B, T, W, V extends Collection<W>> extends QuestionComponentComplexBase<B, T, V> {

    private static final Parameter.Value<String> COMPONENT = Parameter.string().consumeAllRemaining().key("component").build();

    private final Parameter.Value<String> addParameter;
    protected final Parameter.Value<String> valueParameter;
    protected final String plural;
    private final Function<V, V> collectionFactory;
    protected final Function<String, W> valueMapper;
    private final Function<W, String> valueUnmapper;

    public QuestionComponentComplexCollection(final String name, final String singular, final String plural,
                                              final String commandDescription,
                                              final BiConsumer<B, V> setComponent,
                                              final Function<T, V> getComponent,
                                              final Function<V, V> collectionFactory,
                                              final Function<String, W> valueMapper,
                                              final Function<W, String> valueUnmapper) {
        this(name, singular, plural, commandDescription, setComponent, getComponent, collectionFactory, valueMapper, valueUnmapper, COMPONENT);
    }

    public QuestionComponentComplexCollection(final String name, final String singular, final String plural,
                                              final String commandDescription,
                                              final BiConsumer<B, V> setComponent,
                                              final Function<T, V> getComponent,
                                              final Function<V, V> collectionFactory,
                                              final Function<String, W> valueMapper,
                                              final Function<W, String> valueUnmapper,
                                              final Parameter.Value<String> addParameter) {
        super(name, commandDescription, singular, setComponent, getComponent);
        this.plural = plural;
        this.collectionFactory = collectionFactory;
        this.valueMapper = valueMapper;
        this.valueUnmapper = valueUnmapper;
        this.addParameter = addParameter;
        this.valueParameter = Parameter.string().consumeAllRemaining().key("values").build();
    }

    @Override
    public V get(final CommandContext context) {
        return (V) context.all(this.valueParameter).stream().map(this.valueMapper).toList();
    }

    public void addAll(final T type, final B builder, final V values) {
        final V currentValues = this.getComponent.apply(type);
        final V modifiedValues = this.collectionFactory.apply(currentValues);
        modifiedValues.addAll(values);
        this.setComponent.accept(builder, modifiedValues);
    }

    public void removeAll(final T type, final B builder, final V values) {
        final V currentValues = this.getComponent.apply(type);
        final V modifiedValues = this.collectionFactory.apply(currentValues);
        modifiedValues.removeAll(values);
        this.setComponent.accept(builder, modifiedValues);
    }

    @Override
    public void save(final T type, final ConfigurationNode node) throws SerializationException {
        final V value = this.getComponent.apply(type);
        final List<String> mappedValues = value.stream().map(this.valueUnmapper).toList();
        node.node(this.name).setList(String.class, new ArrayList<>(mappedValues));
    }

    public String getPlural() {
        return plural;
    }

    public Function<W, String> getValueUnmapper() {
        return valueUnmapper;
    }

    public Parameter.Value<String> getAddParameter() {
        return addParameter;
    }
}
