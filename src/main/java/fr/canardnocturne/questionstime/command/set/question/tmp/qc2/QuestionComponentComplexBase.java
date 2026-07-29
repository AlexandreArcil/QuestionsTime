package fr.canardnocturne.questionstime.command.set.question.tmp.qc2;

import fr.canardnocturne.questionstime.command.set.question.tmp.qc.QuestionModifierComplex;
import fr.canardnocturne.questionstime.question.Question;
import fr.canardnocturne.questionstime.question.ask.pool.QuestionPool;
import fr.canardnocturne.questionstime.question.save.QuestionRegister;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class QuestionComponentComplexBase<B, T, V> {

    protected final String name;
    protected final String singular;
    protected final String commandDescription;
    protected final BiConsumer<B, V> setComponent;
    protected final Function<T, V> getComponent;

    public QuestionComponentComplexBase(final String name, final String singular, final String commandDescription,
                                        final BiConsumer<B, V> setComponent, final Function<T, V> getComponent) {
        this.name = name;
        this.singular = singular;
        this.commandDescription = commandDescription;
        this.setComponent = setComponent;
        this.getComponent = getComponent;
    }

    public abstract Command.Parameterized createSetCommand(final Parameter.Value<Question> specificQuestionParameter,
                                                           final QuestionRegister questionRegister,
                                                           final QuestionPool questionPool);

    public abstract V get(CommandContext context);

    public abstract void load(final B builder, final ConfigurationNode node) throws SerializationException;

    public abstract void save(final T type, final ConfigurationNode node) throws SerializationException;

    public abstract Class<T> getType();

    public String getSingular() {
        return singular;
    }
}
