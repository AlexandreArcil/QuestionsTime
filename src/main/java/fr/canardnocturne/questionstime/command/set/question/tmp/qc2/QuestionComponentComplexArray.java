package fr.canardnocturne.questionstime.command.set.question.tmp.qc2;

import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class QuestionComponentComplexArray<B, T, V> extends QuestionComponentComplexBase<B, T, V[]> {

    protected final Parameter.Value<String> valueParameter;
    protected final String plural;
    protected final Function<String, V> valueMapper;
    private final Function<V, String> valueUnmapper;

    public QuestionComponentComplexArray(final String name, final String singular, final String plural, final String commandDescription,
                                         final BiConsumer<B, V[]> setComponent, final Function<T, V[]> getComponent,
                                         final Function<String, V> valueMapper, final Function<V, String> valueUnmapper) {
        super(name, singular, commandDescription, setComponent, getComponent);
        this.plural = plural;
        this.valueMapper = valueMapper;
        this.valueUnmapper = valueUnmapper;
        this.valueParameter = Parameter.string().consumeAllRemaining().key("values").build();
    }

    @Override
    public V[] get(final CommandContext context) {
        return (V[]) context.all(this.valueParameter).stream().map(this.valueMapper).toList().toArray();
    }

    public void addAll(final T type, final B builder, final V[] values) {
        final V[] currentValues = this.getComponent.apply(type);
        final V[] modifiedValues = Arrays.copyOf(currentValues, currentValues.length + values.length);
        System.arraycopy(values, 0, modifiedValues, currentValues.length, values.length);
        this.setComponent.accept(builder, modifiedValues);
    }

    public void removeAll(final T type, final B builder, final V[] values) {
        final V[] currentValues = this.getComponent.apply(type);
        final List<V> modifiedValues = new ArrayList<>(List.of(currentValues));
        modifiedValues.removeAll(List.of(values));
        this.setComponent.accept(builder, (V[]) modifiedValues.toArray());
    }

    @Override
    public void load(final B builder, final ConfigurationNode node) throws SerializationException {
        final List<String> values = node.node(this.name).getList(String.class, Collections.emptyList());
        final V[] mappedValues = (V[]) values.stream().map(this.valueMapper).toArray();
        this.setComponent.accept(builder, mappedValues);
    }

    @Override
    public void save(final T type, final ConfigurationNode node) throws SerializationException {
        final V[] value = this.getComponent.apply(type);
        final List<String> mappedValues = Arrays.stream(value).map(this.valueUnmapper).toList();
        node.node(this.name).setList(String.class, mappedValues);
    }

    public String getPlural() {
        return plural;
    }

    public Function<V, String> getValueUnmapper() {
        return valueUnmapper;
    }
}
