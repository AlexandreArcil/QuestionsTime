package fr.canardnocturne.questionstime.command.set.question.tmp.qc3.component;

import fr.canardnocturne.questionstime.util.TextUtils;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

public abstract class QuestionComponentBase<V> {

    protected final String name;
    protected final String singular;

    public QuestionComponentBase(final String name, final String singular) {
        this.name = name;
        this.singular = singular;
    }

    public Component display(final V value, final String question) {
        return TextUtils.composed("", this.singular,  "'s value is ", String.valueOf(value));
    }

    public abstract V get(CommandContext context);

    public abstract V load(final ConfigurationNode node) throws SerializationException;

    public abstract void save(final ConfigurationNode node, final V value) throws SerializationException;

    public String getSingular() {
        return singular;
    }

    public String getName() {
        return name;
    }

    public abstract Parameter getParameter();
}
