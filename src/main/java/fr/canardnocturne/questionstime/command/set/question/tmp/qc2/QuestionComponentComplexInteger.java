package fr.canardnocturne.questionstime.command.set.question.tmp.qc2;

import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class QuestionComponentComplexInteger<B, T> extends QuestionComponentComplexBase<B, T, Integer> {

    protected final Parameter.Value<Integer> valueParameter;

    public QuestionComponentComplexInteger(final String name, final String singular,
                                           final String commandDescription,
                                           final BiConsumer<B, Integer> setComponent,
                                           final Function<T, Integer> getComponent) {
        super(name, singular, commandDescription, setComponent, getComponent);
        this.valueParameter = Parameter.integerNumber().key("value").build();
    }

    @Override
    public Integer get(final CommandContext context) {
        return context.requireOne(this.valueParameter);
    }

    @Override
    public void save(final T type, final ConfigurationNode node) throws SerializationException {
        final int value = this.getComponent.apply(type);
        node.node(this.name).set(value);
    }

    @Override
    public void load(final B builder, final ConfigurationNode node) {
        final int value = node.node(this.name).getInt();
        this.setComponent.accept(builder, value);
    }


}
