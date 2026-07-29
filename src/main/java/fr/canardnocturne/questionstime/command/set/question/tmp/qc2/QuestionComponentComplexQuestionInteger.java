package fr.canardnocturne.questionstime.command.set.question.tmp.qc2;

import fr.canardnocturne.questionstime.question.Question;
import fr.canardnocturne.questionstime.question.ask.pool.QuestionPool;
import fr.canardnocturne.questionstime.question.save.QuestionRegister;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class QuestionComponentComplexQuestionInteger extends QuestionComponentComplexInteger<Question.QuestionBuilder, Question> {

    public QuestionComponentComplexQuestionInteger(final String name, final String singular,
                                                   final String commandDescription,
                                                   final BiConsumer<Question.QuestionBuilder, Integer> setComponent,
                                                   final Function<Question, Integer> getComponent) {
        super(name, singular, commandDescription, setComponent, getComponent);
    }

    @Override
    public Command.Parameterized createSetCommand(final Parameter.Value<Question> specificQuestionParameter,
                                                  final QuestionRegister questionRegister,
                                                  final QuestionPool questionPool) {
        final QuestionModifierComplexSet<Question.QuestionBuilder, Question, Integer> questionModifier =
                new QuestionModifierComplexSet<>(questionRegister, questionPool, this);
        return Command.builder()
                .shortDescription(Component.text(this.commandDescription).color(NamedTextColor.YELLOW))
                .addParameters(valueParameter)
                .executor(new SetQuestionComponentQuestionExecutor<>(specificQuestionParameter, questionModifier, this))
                .build();
    }

    @Override
    public Class<Question> getType() {
        return Question.class;
    }

}
