package fr.canardnocturne.questionstime.command.set.question.tmp.qc3.section;

import fr.canardnocturne.questionstime.command.set.question.tmp.qc3.component.QuestionComponentBase;
import fr.canardnocturne.questionstime.command.set.question.tmp.qc3.section.transform.QuestionSectionTransform;
import fr.canardnocturne.questionstime.command.set.question.tmp.qc3.section.transform.SectionTransform;
import fr.canardnocturne.questionstime.question.Question;
import net.kyori.adventure.text.Component;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class QuestionSectionQuestion<V> extends QuestionSectionBase<Question, Question.QuestionBuilder, V> {

    public QuestionSectionQuestion(final BiConsumer<Question.QuestionBuilder, V> setComponent,
                                   final Function<Question, V> getComponent,
                                   final QuestionComponentBase<V> questionComponentBase) {
        super(setComponent, getComponent, questionComponentBase);
    }

    @Override
    public SectionTransform<Question.QuestionBuilder, Question> createTransform(final CommandContext context, final Question question) {
        return new QuestionSectionTransform(question);
    }

    @Override
    public Component display(final Question question) {
        return this.questionComponentBase.display(this.getComponent.apply(question), question.getQuestion());
    }

    @Override
    public void load(final ConfigurationNode node, final Question.QuestionBuilder builder) throws SerializationException {
        final V value = this.questionComponentBase.load(node);
        this.setComponent.accept(builder, value);
    }

    @Override
    public void save(final ConfigurationNode node, final Question type) throws SerializationException {
        final V value = this.getComponent.apply(type);
        this.questionComponentBase.save(node, value);
    }

    @Override
    public Class<Question> getType() {
        return Question.class;
    }

    @Override
    public String getSection() {
        return StringUtils.EMPTY;
    }
}
