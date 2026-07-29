package fr.canardnocturne.questionstime.command.set.question.tmp;

import fr.canardnocturne.questionstime.command.set.question.QuestionComponentParameter;
import fr.canardnocturne.questionstime.command.set.question.permissions.exclude.SetQuestionRemoveExcludePermissionsExecutor;
import fr.canardnocturne.questionstime.question.Question;
import fr.canardnocturne.questionstime.question.QuestionComponent;
import fr.canardnocturne.questionstime.question.ask.pool.QuestionPool;
import fr.canardnocturne.questionstime.question.modifier.QuestionModifier;
import fr.canardnocturne.questionstime.question.save.QuestionRegister;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.parameter.Parameter;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

public class SetQuestionAddListRemoveComponentFactory {

    private final Parameter.Value<Question> specificQuestionParameter;
    private final QuestionModifier questionModifier;
    private final QuestionPool questionPool;
    private final QuestionRegister questionRegister;

    public SetQuestionAddListRemoveComponentFactory(final Parameter.Value<Question> specificQuestionParameter,
                                                    final QuestionModifier questionModifier, final QuestionPool questionPool,
                                                    final QuestionRegister questionRegister) {
        this.specificQuestionParameter = specificQuestionParameter;
        this.questionModifier = questionModifier;
        this.questionPool = questionPool;
        this.questionRegister = questionRegister;
    }

    public Command.Parameterized create(final QuestionComponent component, final Function<Question, Collection<String>> componentExtractor,
                                        final boolean valueDoubleQuote) {
        return this.create(component, componentExtractor, SetQuestionAddComponentExecutor.COMPONENT, valueDoubleQuote);
    }

    public Command.Parameterized create(final QuestionComponent component, final Function<Question, Collection<String>> componentExtractor,
                                        final Parameter.Value<String> addParameter, final boolean valueDoubleQuote) {
        final Command.Parameterized commandQTSetQuestionListComponents = Command.builder()
                .shortDescription(Component.text("List the question " + component.getPlural()).color(NamedTextColor.YELLOW))
                .executor(new SetQuestionListComponentExecutor(this.specificQuestionParameter, componentExtractor, component, valueDoubleQuote))
                .build();

        final Command.Parameterized commandQTSetQuestionAddComponents = Command.builder()
                .shortDescription(Component.text("Add " + component.getPlural() + " to the question").color(NamedTextColor.YELLOW))
                .addParameters(addParameter)
                .executor(new SetQuestionAddComponentExecutor(specificQuestionParameter, questionModifier, questionPool, questionRegister, componentExtractor, component, addParameter))
                .build();

        final Parameter.Value<String> removeComponentParameter = QuestionComponentParameter.create("remove-component", specificQuestionParameter, componentExtractor);
        final Command.Parameterized commandQTSetQuestionRemoveComponents = Command.builder()
                .shortDescription(Component.text("Remove " + component.getPlural() + " from the question").color(NamedTextColor.YELLOW))
                .addParameters(removeComponentParameter)
                .executor(new SetQuestionRemoveComponentExecutor(specificQuestionParameter, removeComponentParameter, questionModifier, questionPool, questionRegister, componentExtractor, component))
                .build();

        return Command.builder()
                .shortDescription(Component.text("Set the question " + component.getPlural()).color(NamedTextColor.YELLOW))
                .addChild(commandQTSetQuestionListComponents, "list")
                .addChild(commandQTSetQuestionAddComponents, "add")
                .addChild(commandQTSetQuestionRemoveComponents, "remove")
                .build();
    }

    public Command.Parameterized create(final QuestionComponent component, final Function<Question, Map<Integer, Collection<String>>> componentExtractor) {
        final Parameter.Value<Integer> positionParameter = Parameter.integerNumber().key("position").build();
        final Command.Parameterized commandQTSetQuestionListComponents = Command.builder()
                .shortDescription(Component.text("List the question " + component.getPlural()).color(NamedTextColor.YELLOW))
                .executor(new SetQuestionListComponentAtPositionExecutor(this.specificQuestionParameter, positionParameter, componentExtractor, component))
                .build();

        final Command.Parameterized commandQTSetQuestionAddComponents = Command.builder()
                .shortDescription(Component.text("Add " + component.getPlural() + " to the question").color(NamedTextColor.YELLOW))
                .addParameters(positionParameter, SetQuestionAddComponentExecutor.COMPONENT)
                .executor(new SetQuestionAddComponentAtPositionExecutor(specificQuestionParameter, positionParameter, questionModifier, questionPool, questionRegister, componentExtractor, component))
                .build();

        final Parameter.Value<String> removeComponentParameter = QuestionComponentParameter.create("remove-component", specificQuestionParameter, positionParameter, componentExtractor);
        final Command.Parameterized commandQTSetQuestionRemoveComponents = Command.builder()
                .shortDescription(Component.text("Remove " + component.getPlural() + " from the question").color(NamedTextColor.YELLOW))
                .addParameters(positionParameter, removeComponentParameter)
                .executor(new SetQuestionRemoveComponentAtPositionExecutor(specificQuestionParameter, positionParameter, removeComponentParameter, questionModifier, questionPool, questionRegister, componentExtractor, component))
                .build();

        return Command.builder()
                .shortDescription(Component.text("Set the question " + component.getPlural()).color(NamedTextColor.YELLOW))
                .addChild(commandQTSetQuestionListComponents, "list")
                .addChild(commandQTSetQuestionAddComponents, "add")
                .addChild(commandQTSetQuestionRemoveComponents, "remove")
                .build();
    }

}
