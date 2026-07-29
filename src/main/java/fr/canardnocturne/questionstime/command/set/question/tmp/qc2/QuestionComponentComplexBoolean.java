package fr.canardnocturne.questionstime.command.set.question.tmp.qc2;

import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class QuestionComponentComplexBoolean<B, T> extends QuestionComponentComplexBase<B, T, Boolean> {

    protected final Parameter.Value<Boolean> valueParameter;

    public QuestionComponentComplexBoolean(final String name, final String singular,
                                           final String commandDescription,
                                           final BiConsumer<B, Boolean> setComponent,
                                           final Function<T, Boolean> getComponent) {
        super(name, singular, commandDescription, setComponent, getComponent);
        this.valueParameter = Parameter.bool().key("value").build();
    }

    @Override
    public Boolean get(final CommandContext context) {
        return context.requireOne(this.valueParameter);
    }

    @Override
    public void save(final T type, final ConfigurationNode node) throws SerializationException {
        final boolean value = this.getComponent.apply(type);
        node.node(this.name).set(value);
    }

    @Override
    public void load(final B builder, final ConfigurationNode node) {
        final boolean value = node.node(this.name).getBoolean();
        this.setComponent.accept(builder, value);
    }


}
