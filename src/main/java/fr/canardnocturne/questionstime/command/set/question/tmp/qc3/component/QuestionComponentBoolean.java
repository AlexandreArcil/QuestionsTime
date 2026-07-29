package fr.canardnocturne.questionstime.command.set.question.tmp.qc3.component;

import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

public class QuestionComponentBoolean extends QuestionComponentBase<Boolean> {

    private final Parameter.Value<Boolean> parameter;

    public QuestionComponentBoolean(final String name, final String singular) {
        super(name, singular);
        this.parameter = Parameter.bool().key("value").build();
    }

    @Override
    public Boolean get(final CommandContext context) {
        return context.requireOne(this.parameter);
    }

    @Override
    public Boolean load(final ConfigurationNode node) throws SerializationException {
        return node.node(this.name).getBoolean();
    }

    @Override
    public void save(final ConfigurationNode node, final Boolean value) throws SerializationException {
        node.node(this.name).set(value);
    }

    @Override
    public Parameter getParameter() {
        return this.parameter;
    }

}
