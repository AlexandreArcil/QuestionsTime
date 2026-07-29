package fr.canardnocturne.questionstime.command.set.question.tmp.qc;

import fr.canardnocturne.questionstime.command.set.question.QuestionComponentParameter;
import fr.canardnocturne.questionstime.command.set.question.tmp.SetQuestionAddComponentAtPositionExecutor;
import fr.canardnocturne.questionstime.command.set.question.tmp.SetQuestionAddComponentExecutor;
import fr.canardnocturne.questionstime.command.set.question.tmp.SetQuestionListComponentAtPositionExecutor;
import fr.canardnocturne.questionstime.command.set.question.tmp.SetQuestionRemoveComponentAtPositionExecutor;
import fr.canardnocturne.questionstime.question.Question;
import fr.canardnocturne.questionstime.question.component.Prize;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.apache.logging.log4j.util.TriConsumer;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.parameter.Parameter;

import java.util.Collection;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class ListQuestionPositionComponent extends QuestionComponentComplexPosition<Collection<String>> {

    private final String singular;
    private final String plural;
    private final Function<Question, Map<Integer, Collection<String>>> componentExtractor;
    private final BiFunction<Question, Integer, Collection<String>> componentCopy;
//    private final Parameter.Value<String> addParameter;
    private final boolean valueDoubleQuote;

    /*public ListQuestionPositionComponent(final String componentName, final String commandDescription, final String singular, final String plural,
                                         final TriConsumer<Question.QuestionBuilder, Integer, Collection<String>> componentExtractor,
                                         final TriConsumer<Question.QuestionBuilder, Integer, Collection<String>> componentCopy,
                                         final TriConsumer<Question.QuestionBuilder, Integer, Collection<String>> setComponent,
                                         final boolean valueDoubleQuote) {
        this(componentName, commandDescription, singular, plural, componentExtractor, componentCopy,  setComponent, *//*SetQuestionAddComponentExecutor.COMPONENT,*//* valueDoubleQuote);
    }*/

    public ListQuestionPositionComponent(final String componentName, final String commandDescription, final String singular, final String plural,
                                         final Function<Question, Map<Integer, Collection<String>>> componentExtractor,
                                         final BiFunction<Question, Integer, Collection<String>> componentCopy,
                                         final TriConsumer<Question.QuestionBuilder, Integer, Collection<String>> setComponent,
                                         /*final Parameter.Value<String> addParameter,*/ final boolean valueDoubleQuote) {
        super(componentName, commandDescription, setComponent);
        this.singular = singular;
        this.plural = plural;
        this.componentExtractor = componentExtractor;
        this.componentCopy = componentCopy;
//        this.addParameter = addParameter;
        this.valueDoubleQuote = valueDoubleQuote;
    }

    @Override
    public Command.Parameterized create(final Parameter.Value<Question> specificQuestionParameter, final QuestionModifierComplex questionModifier) {
        final Parameter.Value<Integer> positionParameter = Parameter.integerNumber().key("position").build();
        final Command.Parameterized commandQTSetQuestionListComponents = Command.builder()
                .shortDescription(Component.text("List the question " + this.getPlural()).color(NamedTextColor.YELLOW))
                .executor(new SetQuestionListComponentComplexAtPositionExecutor(specificQuestionParameter, positionParameter, this))
                .build();

        final Command.Parameterized commandQTSetQuestionAddComponents = Command.builder()
                .shortDescription(Component.text("Add " + this.getPlural() + " to the question").color(NamedTextColor.YELLOW))
                .addParameters(positionParameter, SetQuestionAddComponentExecutor.COMPONENT)
                .executor(new SetQuestionAddComponentComplexPositionExecutor(specificQuestionParameter, new QuestionModifierAddPositionComplex(questionModifier), this))
                .build();

        final Parameter.Value<String> removeComponentParameter = QuestionComponentParameter.create("remove-component", specificQuestionParameter, positionParameter, componentExtractor);
        final Command.Parameterized commandQTSetQuestionRemoveComponents = Command.builder()
                .shortDescription(Component.text("Remove " + this.getPlural() + " from the question").color(NamedTextColor.YELLOW))
                .addParameters(positionParameter, removeComponentParameter)
//                .executor(new SetQuestionRemoveComponentComplexPositionExecutor(specificQuestionParameter, positionParameter, removeComponentParameter, questionModifier, this))
                .build();

        return Command.builder()
                .shortDescription(Component.text("Set the question prizes " + this.getPlural()).color(NamedTextColor.YELLOW))
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

    public Function<Question, Map<Integer, Collection<String>>> getComponentExtractor() {
        return componentExtractor;
    }

    /*public Parameter.Value<String> getAddParameter() {
        return addParameter;
    }*/

    public boolean isValueDoubleQuote() {
        return valueDoubleQuote;
    }

    public BiFunction<Question, Integer, Collection<String>> getComponentCopy() {
        return componentCopy;
    }
}
