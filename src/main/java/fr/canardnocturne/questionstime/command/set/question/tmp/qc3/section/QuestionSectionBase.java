package fr.canardnocturne.questionstime.command.set.question.tmp.qc3.section;

import fr.canardnocturne.questionstime.command.set.question.tmp.qc3.component.QuestionComponentBase;
import fr.canardnocturne.questionstime.command.set.question.tmp.qc3.section.transform.SectionTransform;
import fr.canardnocturne.questionstime.question.Question;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class QuestionSectionBase<T, B, V> {

    protected final BiConsumer<B, V> setComponent;
    protected final Function<T, V> getComponent;
    protected final QuestionComponentBase<V> questionComponentBase;

    public QuestionSectionBase(final BiConsumer<B, V> setComponent,
                               final Function<T, V> getComponent,
                               final QuestionComponentBase<V> questionComponentBase) {
        this.setComponent = setComponent;
        this.getComponent = getComponent;
        this.questionComponentBase = questionComponentBase;
    }

    public abstract SectionTransform<B, T> createTransform(final CommandContext context, final Question question);

    public abstract Component display(final Question question);

    public abstract void load(final ConfigurationNode node, final B builder) throws SerializationException;

    public abstract void save(final ConfigurationNode node, final T type) throws SerializationException;

    public abstract Class<T> getType();

    public BiConsumer<B, V> getSetComponent() {
        return setComponent;
    }

    public Function<T, V> getGetComponent() {
        return getComponent;
    }

    public abstract String getSection();
}
