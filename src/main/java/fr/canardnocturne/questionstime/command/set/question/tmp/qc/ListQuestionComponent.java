package fr.canardnocturne.questionstime.command.set.question.tmp.qc;

import fr.canardnocturne.questionstime.command.set.question.QuestionComponentParameter;
import fr.canardnocturne.questionstime.command.set.question.tmp.SetQuestionAddComponentExecutor;
import fr.canardnocturne.questionstime.question.Question;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.parameter.Parameter;

import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ListQuestionComponent extends QuestionComponentComplex<Collection<String>> {

    private final String singular;
    private final String plural;
    private final Function<Question, Collection<String>> componentExtractor;
    private final Function<Question, Collection<String>> componentCopy;
    private final Parameter.Value<String> addParameter;
    private final boolean valueDoubleQuote;

    public ListQuestionComponent(final String componentName, final String commandDescription, final String singular, final String plural,
                                 final Function<Question, Collection<String>> componentExtractor,
                                 final Function<Question, Collection<String>> componentCopy,
                                 final BiConsumer<Question.QuestionBuilder, Collection<String>> setComponent,
                                 final boolean valueDoubleQuote) {
        this(componentName, commandDescription, singular, plural, componentExtractor, componentCopy,  setComponent, SetQuestionAddComponentExecutor.COMPONENT, valueDoubleQuote);
    }

    public ListQuestionComponent(final String componentName, final String commandDescription, final String singular, final String plural,
                                 final Function<Question, Collection<String>> componentExtractor,
                                 final Function<Question, Collection<String>> componentCopy,
                                 final BiConsumer<Question.QuestionBuilder, Collection<String>> setComponent,
                                 final Parameter.Value<String> addParameter, final boolean valueDoubleQuote) {
        super(componentName, commandDescription, setComponent);
        this.singular = singular;
        this.plural = plural;
        this.componentExtractor = componentExtractor;
        this.componentCopy = componentCopy;
        this.addParameter = addParameter;
        this.valueDoubleQuote = valueDoubleQuote;
    }

    @Override
    public Command.Parameterized create(final Parameter.Value<Question> specificQuestionParameter, final QuestionModifierComplex questionModifier) {
        final Command.Parameterized commandQTSetQuestionListComponents = Command.builder()
                .shortDescription(Component.text("List the question " + this.getPlural()).color(NamedTextColor.YELLOW))
                .executor(new SetQuestionListComponentComplexExecutor(specificQuestionParameter, this))
                .build();

        final Command.Parameterized commandQTSetQuestionAddComponents = Command.builder()
                .shortDescription(Component.text("Add " + this.getPlural() + " to the question").color(NamedTextColor.YELLOW))
                .addParameters(addParameter)
                .executor(new SetQuestionAddComponentComplexExecutor(specificQuestionParameter, new QuestionModifierAddComplex(questionModifier), this))
                .build();

        final Parameter.Value<String> removeComponentParameter = QuestionComponentParameter.create("remove-component", specificQuestionParameter, componentExtractor);
        final Command.Parameterized commandQTSetQuestionRemoveComponents = Command.builder()
                .shortDescription(Component.text("Remove " + this.getName() + " from the question").color(NamedTextColor.YELLOW))
                .addParameters(removeComponentParameter)
                .executor(new SetQuestionRemoveComponentComplexExecutor(specificQuestionParameter, removeComponentParameter, new QuestionModifierRemoveComplex(questionModifier), this))
                .build();

        return Command.builder()
                .shortDescription(Component.text("Set the question " + this.getName()).color(NamedTextColor.YELLOW))
                .addChild(commandQTSetQuestionListComponents, "list")
                .addChild(commandQTSetQuestionAddComponents, "add")
                .addChild(commandQTSetQuestionRemoveComponents, "remove")
                .build();
    }

    public String getSingular() {
        return singular;
    }

    public String getPlural() {
        return plural;
    }

    public Function<Question, Collection<String>> getComponentExtractor() {
        return componentExtractor;
    }

    public Parameter.Value<String> getAddParameter() {
        return addParameter;
    }

    public boolean isValueDoubleQuote() {
        return valueDoubleQuote;
    }

    public Function<Question, Collection<String>> getComponentCopy() {
        return componentCopy;
    }
}
