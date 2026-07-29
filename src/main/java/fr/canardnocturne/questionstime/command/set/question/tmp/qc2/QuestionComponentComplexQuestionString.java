package fr.canardnocturne.questionstime.command.set.question.tmp.qc2;

import fr.canardnocturne.questionstime.question.Question;
import fr.canardnocturne.questionstime.question.ask.pool.QuestionPool;
import fr.canardnocturne.questionstime.question.save.QuestionRegister;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.parameter.Parameter;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class QuestionComponentComplexQuestionString extends QuestionComponentComplexString<Question.QuestionBuilder, Question> {


    public QuestionComponentComplexQuestionString(final String name, final String singular, final String commandDescription,
                                                  final BiConsumer<Question.QuestionBuilder, String> setComponent,
                                                  final Function<Question, String> getComponent) {
        super(name, commandDescription, singular, setComponent, getComponent);
    }

    @Override
    public Command.Parameterized createSetCommand(final Parameter.Value<Question> specificQuestionParameter,
                                                  final QuestionRegister questionRegister,
                                                  final QuestionPool questionPool) {
        final QuestionModifierComplexSet<Question.QuestionBuilder, Question, String> questionModifier =
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
