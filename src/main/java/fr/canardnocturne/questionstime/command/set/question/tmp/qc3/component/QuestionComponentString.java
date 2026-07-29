package fr.canardnocturne.questionstime.command.set.question.tmp.qc3.component;

import fr.canardnocturne.questionstime.question.Question;
import fr.canardnocturne.questionstime.question.ask.pool.QuestionPool;
import fr.canardnocturne.questionstime.question.save.QuestionRegister;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

public class QuestionComponentString extends QuestionComponentBase<String> {

    private final Parameter.Value<String> valueParameter;

    public QuestionComponentString(final String name, final String singular) {
        super(name, singular);
        this.valueParameter = Parameter.string().key("value").build();
    }

    @Override
    public String get(final CommandContext context) {
        return context.requireOne(this.valueParameter);
    }

    @Override
    public String load(final ConfigurationNode node) throws SerializationException {
        return node.node(this.name).getString();
    }

    @Override
    public void save(final ConfigurationNode node, final String value) throws SerializationException {
        node.node(this.name).set(value);
    }

    @Override
    public Parameter getParameter() {
        return this.valueParameter;
    }
}
