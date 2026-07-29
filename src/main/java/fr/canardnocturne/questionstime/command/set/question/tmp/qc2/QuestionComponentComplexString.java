package fr.canardnocturne.questionstime.command.set.question.tmp.qc2;

import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class QuestionComponentComplexString<B, T> extends QuestionComponentComplexBase<B, T, String> {

    protected final Parameter.Value<String> valueParameter;

    public QuestionComponentComplexString(final String name, final String singular,
                                          final String commandDescription,
                                          final BiConsumer<B, String> setComponent, final Function<T, String> getComponent) {
        super(name, commandDescription, singular, setComponent, getComponent);
        this.valueParameter = Parameter.string().key("value").build();
    }

    @Override
    public String get(final CommandContext context) {
        return context.requireOne(this.valueParameter);
    }

    @Override
    public void save(final T type, final ConfigurationNode node) throws SerializationException {
        final String value = this.getComponent.apply(type);
        node.node(this.name).set(value);
    }

    @Override
    public void load(final B builder, final ConfigurationNode node) {
        final String value = node.node(this.name).getString();
        this.setComponent.accept(builder, value);
    }
}
