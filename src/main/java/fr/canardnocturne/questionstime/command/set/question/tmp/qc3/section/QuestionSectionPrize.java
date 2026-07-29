package fr.canardnocturne.questionstime.command.set.question.tmp.qc3.section;

import fr.canardnocturne.questionstime.command.set.question.tmp.qc3.component.QuestionComponentBase;
import fr.canardnocturne.questionstime.command.set.question.tmp.qc3.section.transform.PrizeSectionTransform;
import fr.canardnocturne.questionstime.command.set.question.tmp.qc3.section.transform.SectionTransform;
import fr.canardnocturne.questionstime.question.Question;
import fr.canardnocturne.questionstime.question.component.Prize;
import fr.canardnocturne.questionstime.util.TextUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class QuestionSectionPrize<V> extends QuestionSectionBase<Prize, Prize.Builder, V> {

    public static final Parameter.Value<Integer> POSITION = Parameter.integerNumber().key("position").build();

    public QuestionSectionPrize(final BiConsumer<Prize.Builder, V> setComponent,
                                final Function<Prize, V> getComponent,
                                final QuestionComponentBase<V> questionComponentBase) {
        super(setComponent, getComponent, questionComponentBase);
    }

    @Override
    public SectionTransform<Prize.Builder, Prize> createTransform(final CommandContext context, final Question question) {
        final int position = context.requireOne(POSITION);
        return new PrizeSectionTransform(question, position);
    }

    @Override
    public Component display(final Question question) {
        return Component.join(JoinConfiguration.newlines(), question.getPrizes().stream().map(prize ->
                        TextUtils.normalWithPrefix("Position " + prize.getPosition() + ":")
                        .appendNewline()
                        .append(this.questionComponentBase.display(this.getComponent.apply(prize), question.getQuestion())))
                .toList());
    }

    @Override
    public void load(final ConfigurationNode node, final Prize.Builder builder) throws SerializationException {
        final V value = this.questionComponentBase.load(node);
        this.setComponent.accept(builder, value);
    }

    @Override
    public void save(final ConfigurationNode node, final Prize type) throws SerializationException {
        final V value = this.getComponent.apply(type);
        this.questionComponentBase.save(node, value);
    }

    @Override
    public Class<Prize> getType() {
        return Prize.class;
    }

    @Override
    public String getSection() {
        return "prize";
    }
}
