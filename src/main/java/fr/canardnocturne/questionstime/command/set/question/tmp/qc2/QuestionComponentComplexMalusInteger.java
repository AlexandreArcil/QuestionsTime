package fr.canardnocturne.questionstime.command.set.question.tmp.qc2;

import fr.canardnocturne.questionstime.question.Question;
import fr.canardnocturne.questionstime.question.ask.pool.QuestionPool;
import fr.canardnocturne.questionstime.question.component.Malus;
import fr.canardnocturne.questionstime.question.save.QuestionRegister;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.parameter.Parameter;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class QuestionComponentComplexMalusInteger extends QuestionComponentComplexInteger<Malus.Builder, Malus> {

    public QuestionComponentComplexMalusInteger(final String name, final String singular,
                                                final String commandDescription,
                                                final BiConsumer<Malus.Builder, Integer> setComponent,
                                                final Function<Malus, Integer> getComponent) {
        super(name, singular, commandDescription, setComponent, getComponent);
    }

    @Override
    public Command.Parameterized createSetCommand(final Parameter.Value<Question> specificQuestionParameter,
                                                  final QuestionRegister questionRegister,
                                                  final QuestionPool questionPool) {
        final QuestionModifierComplexSet<Malus.Builder, Malus, Integer> questionModifier =
                new QuestionModifierComplexSet<>(questionRegister, questionPool, this);
        return Command.builder()
                .shortDescription(Component.text(this.commandDescription).color(NamedTextColor.YELLOW))
                .addParameters(valueParameter)
                .executor(new SetQuestionComponentMalusExecutor<>(specificQuestionParameter, questionModifier, this))
                .build();
    }

    @Override
    public Class<Malus> getType() {
        return Malus.class;
    }


}
