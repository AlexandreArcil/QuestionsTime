package fr.canardnocturne.questionstime.command.set.question.tmp.qc2;

import fr.canardnocturne.questionstime.question.Question;
import fr.canardnocturne.questionstime.question.ask.pool.QuestionPool;
import fr.canardnocturne.questionstime.question.save.QuestionRegister;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.parameter.Parameter;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class QuestionComponentComplexQuestionSetString extends QuestionComponentComplexSetString<Question.QuestionBuilder, Question> {

    public QuestionComponentComplexQuestionSetString(final String name, final String singular, final String plural,
                                                     final String commandDescription,
                                                     final BiConsumer<Question.QuestionBuilder, Set<String>> setComponent,
                                                     final Function<Question, Set<String>> getComponent) {
        super(name, singular, plural, commandDescription, setComponent, getComponent);
    }

    public QuestionComponentComplexQuestionSetString(final String name, final String singular, final String plural,
                                                     final String commandDescription,
                                                     final BiConsumer<Question.QuestionBuilder, Set<String>> setComponent,
                                                     final Function<Question, Set<String>> getComponent,
                                                     final Parameter.Value<String> addParameter) {
        super(name, singular, plural, commandDescription, setComponent, getComponent, addParameter);
    }

    @Override
    public Command.Parameterized createSetCommand(final Parameter.Value<Question> specificQuestionParameter,
                                                  final QuestionRegister questionRegister,
                                                  final QuestionPool questionPool) {
        final Command.Parameterized commandQTSetQuestionListComponents = Command.builder()
                .shortDescription(Component.text("List the question " + this.plural).color(NamedTextColor.YELLOW))
                .executor(new SetQuestionListComponentComplexQuestionExecutor<>(specificQuestionParameter, this))
                .build();

        final Command.Parameterized commandQTSetQuestionAddComponents = Command.builder()
                .shortDescription(Component.text("Add " + this.plural + " to the question").color(NamedTextColor.YELLOW))
                .addParameters(this.getAddParameter())
                .executor(new SetQuestionAddQuestionComponentComplexExecutor<>(specificQuestionParameter, new QuestionModifierComplexAdd<>(questionRegister, questionPool, this), this))
                .build();

        final Parameter.Value<String> removeComponentParameter = QuestionRemoveComponentParameter.create(specificQuestionParameter, this);
        final Command.Parameterized commandQTSetQuestionRemoveComponents = Command.builder()
                .shortDescription(Component.text("Remove " + this.plural + " from the question").color(NamedTextColor.YELLOW))
                .addParameters(removeComponentParameter)
                .executor(new SetQuestionRemoveQuestionComponentComplex<>(specificQuestionParameter, removeComponentParameter, new QuestionModifierComplexRemove<>(questionRegister, questionPool, this), this))
                .build();

        return Command.builder()
                .shortDescription(Component.text("Set the question " + this.plural).color(NamedTextColor.YELLOW))
                .addChild(commandQTSetQuestionListComponents, "list")
                .addChild(commandQTSetQuestionAddComponents, "add")
                .addChild(commandQTSetQuestionRemoveComponents, "remove")
                .build();
    }

    @Override
    public Class<Question> getType() {
        return Question.class;
    }
}
