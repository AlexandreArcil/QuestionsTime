package fr.canardnocturne.questionstime.command.set.question.tmp.qc3.section;

import fr.canardnocturne.questionstime.command.set.question.tmp.qc3.component.QuestionComponentBase;
import fr.canardnocturne.questionstime.command.set.question.tmp.qc3.section.transform.MalusSectionTransform;
import fr.canardnocturne.questionstime.command.set.question.tmp.qc3.section.transform.SectionTransform;
import fr.canardnocturne.questionstime.question.Question;
import fr.canardnocturne.questionstime.question.ask.pool.QuestionPool;
import fr.canardnocturne.questionstime.question.component.Malus;
import fr.canardnocturne.questionstime.question.save.QuestionRegister;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class QuestionSectionMalus<V> extends QuestionSectionBase<Malus, Malus.Builder, V> {

    public QuestionSectionMalus(final BiConsumer<Malus.Builder, V> setComponent,
                                final Function<Malus, V> getComponent,
                                final QuestionComponentBase<V> questionComponentBase) {
        super(setComponent, getComponent, questionComponentBase);
    }

    @Override
    public SectionTransform<Malus.Builder, Malus> createTransform(final CommandContext context, final Question question) {
        return new MalusSectionTransform(question);
    }

    @Override
    public Component display(final Question question) {
        return this.questionComponentBase.display(this.getComponent.apply(question.getMalus()
                .orElseGet(() -> Malus.builder().build())), question.getQuestion());
    }

    @Override
    public void load(final ConfigurationNode node, final Malus.Builder builder) throws SerializationException {
        final V value = this.questionComponentBase.load(node);
        this.setComponent.accept(builder, value);
    }

    @Override
    public void save(final ConfigurationNode node, final Malus type) throws SerializationException {
        final V value = this.getComponent.apply(type);
        this.questionComponentBase.save(node, value);
    }

    @Override
    public Class<Malus> getType() {
        return Malus.class;
    }

    @Override
    public String getSection() {
        return "malus";
    }
}
