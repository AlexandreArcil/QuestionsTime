package fr.canardnocturne.questionstime.command.set.question.tmp.qc2;

import fr.canardnocturne.questionstime.question.Question;
import fr.canardnocturne.questionstime.question.ask.pool.QuestionPool;
import fr.canardnocturne.questionstime.question.component.Malus;
import fr.canardnocturne.questionstime.question.save.QuestionRegister;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.parameter.Parameter;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class QuestionComponentComplexMalusSetString extends QuestionComponentComplexSetString<Malus.Builder, Malus> {

    public QuestionComponentComplexMalusSetString(final String name, final String singular, final String plural,
                                                  final String commandDescription,
                                                  final BiConsumer<Malus.Builder, Set<String>> setComponent,
                                                  final Function<Malus, Set<String>> getComponent) {
        super(name, singular, plural, commandDescription, setComponent, getComponent);
    }

    public QuestionComponentComplexMalusSetString(final String name, final String singular, final String plural,
                                                  final String commandDescription,
                                                  final BiConsumer<Malus.Builder, Set<String>> setComponent,
                                                  final Function<Malus, Set<String>> getComponent,
                                                  final Parameter.Value<String> addParameter) {
        super(name, singular, plural, commandDescription, setComponent, getComponent, addParameter);
    }

    @Override
    public Command.Parameterized createSetCommand(final Parameter.Value<Question> specificQuestionParameter,
                                                  final QuestionRegister questionRegister,
                                                  final QuestionPool questionPool) {
        final Command.Parameterized commandQTSetQuestionListComponents = Command.builder()
                .shortDescription(Component.text("List the question " + this.plural).color(NamedTextColor.YELLOW))
                .executor(new SetQuestionListComponentComplexMalusExecutor<>(specificQuestionParameter, this))
                .build();

        final Command.Parameterized commandQTSetQuestionAddComponents = Command.builder()
                .shortDescription(Component.text("Add " + this.plural + " to the question").color(NamedTextColor.YELLOW))
                .addParameters(this.getAddParameter())
                .executor(new SetQuestionAddMalusComponentComplexExecutor<>(specificQuestionParameter, new QuestionModifierComplexAdd<>(questionRegister, questionPool, this), this))
                .build();

        final Parameter.Value<String> removeComponentParameter = QuestionRemoveComponentParameter.createMalus(specificQuestionParameter, this, String.class);
        final Command.Parameterized commandQTSetQuestionRemoveComponents = Command.builder()
                .shortDescription(Component.text("Remove " + this.plural + " from the question").color(NamedTextColor.YELLOW))
                .addParameters(removeComponentParameter)
                .executor(new SetQuestionRemoveMalusComponentComplex<>(specificQuestionParameter, removeComponentParameter, new QuestionModifierComplexRemove<>(questionRegister, questionPool, this), this))
                .build();

        return Command.builder()
                .shortDescription(Component.text("Set the question " + this.plural).color(NamedTextColor.YELLOW))
                .addChild(commandQTSetQuestionListComponents, "list")
                .addChild(commandQTSetQuestionAddComponents, "add")
                .addChild(commandQTSetQuestionRemoveComponents, "remove")
                .build();
    }

    @Override
    public Class<Malus> getType() {
        return Malus.class;
    }
}
