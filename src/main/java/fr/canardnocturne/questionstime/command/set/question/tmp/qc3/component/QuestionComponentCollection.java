package fr.canardnocturne.questionstime.command.set.question.tmp.qc3.component;

import fr.canardnocturne.questionstime.QuestionsTime;
import fr.canardnocturne.questionstime.question.Question;
import fr.canardnocturne.questionstime.question.serializer.OutcomeCommandSerializer;
import fr.canardnocturne.questionstime.util.TextUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public abstract class QuestionComponentCollection<W, V extends Collection<W>> extends QuestionComponentBase<V> {

    private final String plural;
    private final Function<V, V> collectionFactory;
    protected final Function<String, W> valueMapper;
    private final Function<W, String> valueUnmapper;
    private final Parameter.Value<String> valueParameter;

    public QuestionComponentCollection(final String name, final String singular, final String plural,
                                       final Function<V, V> collectionFactory,
                                       final Function<String, W> valueMapper,
                                       final Function<W, String> valueUnmapper) {
        super(name, singular);
        this.plural = plural;
        this.collectionFactory = collectionFactory;
        this.valueMapper = valueMapper;
        this.valueUnmapper = valueUnmapper;
        this.valueParameter = Parameter.string().consumeAllRemaining().key("values").build();
    }

    @Override
    public V get(final CommandContext context) {
        return (V) context.all(this.valueParameter).stream().map(this.valueMapper).toList();
    }

    public V addAll(final V currentValues, final V values) {
        final V modifiedValues = this.collectionFactory.apply(currentValues);
        modifiedValues.addAll(values);
        return modifiedValues;
    }

    public V removeAll(final V currentValues, final V values) {
        final V modifiedValues = this.collectionFactory.apply(currentValues);
        modifiedValues.removeAll(values);
        return modifiedValues;
    }

    @Override
    public Component display(final V values, final String question) {
        final TextComponent.Builder display = Component.text();
        if(values.isEmpty()) {
            display.append(Component.text("No " + this.plural));
        } else {
            display.append(Component.join(JoinConfiguration.newlines(), values.stream().map(value -> {
                final String valueSerialized = this.valueUnmapper.apply(value);
                final String valueQuoted = TextUtils.shouldBeDoubleQuote(valueSerialized) ? "\"" + valueSerialized + "\"" : valueSerialized;
                return QuestionsTime.PREFIX.append(Component.text("[X]", NamedTextColor.RED, TextDecoration.BOLD)
                                .clickEvent(ClickEvent.runCommand("/qt set question \"" + question + "\" " + this.plural + " remove " + valueQuoted))
                                .hoverEvent(HoverEvent.showText(Component.text("Delete the " + this.singular + " '" + valueSerialized + "'"))))
                        .append(TextUtils.composedWithoutPrefix(" ", valueSerialized));
            }).toList()));
        }
        return display.build();
    }

    @Override
    public void save(final ConfigurationNode node, final V value) throws SerializationException {
        final List<String> mappedValues = value.stream().map(this.valueUnmapper).toList();
        if(!mappedValues.isEmpty()) {
            node.node(this.name).setList(String.class, new ArrayList<>(mappedValues));
        }
    }

    public String getPlural() {
        return this.plural;
    }

    public Function<String, W> getValueMapper() {
        return valueMapper;
    }

    public Function<W, String> getValueUnmapper() {
        return valueUnmapper;
    }

    @Override
    public Parameter getParameter() {
        return this.valueParameter;
    }
}
