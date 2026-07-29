package fr.canardnocturne.questionstime.command.set.question.tmp.qc2;

import fr.canardnocturne.questionstime.question.Question;
import fr.canardnocturne.questionstime.question.ask.pool.QuestionPool;
import fr.canardnocturne.questionstime.question.component.Prize;
import fr.canardnocturne.questionstime.question.save.QuestionRegister;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.parameter.Parameter;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class QuestionComponentComplexPrizeInteger extends QuestionComponentComplexInteger<Prize.Builder, Prize> {


    public QuestionComponentComplexPrizeInteger(final String name, final String singular, final String commandDescription,
                                                final BiConsumer<Prize.Builder, Integer> setComponent,
                                                final Function<Prize, Integer> getComponent) {
        super(name, commandDescription, singular, setComponent, getComponent);
    }

    @Override
    public Command.Parameterized createSetCommand(final Parameter.Value<Question> specificQuestionParameter,
                                                  final QuestionRegister questionRegister,
                                                  final QuestionPool questionPool) {
        final Command.Parameterized commandQTSetQuestionListComponents = Command.builder()
                .shortDescription(Component.text("List the question prize " + this.singular).color(NamedTextColor.YELLOW))
                .executor(new SetQuestionListComponentPrizeInteger(specificQuestionParameter))
                .build();

        final QuestionModifierComplex<Prize.Builder, Prize, Integer> questionModifier =
                new QuestionModifierComplexSet<>(questionRegister, questionPool, this);
        final Command.Parameterized commandQTSetQuestionSetComponents = Command.builder()
                .shortDescription(Component.text(this.commandDescription).color(NamedTextColor.YELLOW))
                .addParameters(valueParameter)
                .executor(new SetQuestionComponentPrizeExecutor<>(specificQuestionParameter, questionModifier, this))
                .build();

        return Command.builder()
                .shortDescription(Component.text("Set the question prize " + this.singular).color(NamedTextColor.YELLOW))
                .addChild(commandQTSetQuestionListComponents, "list")
                .addChild(commandQTSetQuestionSetComponents, "set")
                .build();
    }

    @Override
    public Class<Prize> getType() {
        return Prize.class;
    }
}
