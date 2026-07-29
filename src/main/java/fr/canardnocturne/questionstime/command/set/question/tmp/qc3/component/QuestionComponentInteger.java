package fr.canardnocturne.questionstime.command.set.question.tmp.qc3.component;

import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

public class QuestionComponentInteger extends QuestionComponentBase<Integer> {

    private final Parameter.Value<Integer> valueParameter;

    public QuestionComponentInteger(final String name, final String singular) {
        super(name, singular);
        this.valueParameter = Parameter.integerNumber().key("value").build();
    }

    @Override
    public Integer get(final CommandContext context) {
        return context.requireOne(this.valueParameter);
    }

    @Override
    public Integer load(final ConfigurationNode node) throws SerializationException {
        return node.node(this.name).getInt();
    }

    @Override
    public void save(final ConfigurationNode node, final Integer value) throws SerializationException {
        node.node(this.name).set(value);
    }

    @Override
    public Parameter getParameter() {
        return this.valueParameter;
    }
}
