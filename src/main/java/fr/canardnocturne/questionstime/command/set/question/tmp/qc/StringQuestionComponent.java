package fr.canardnocturne.questionstime.command.set.question.tmp.qc;

import fr.canardnocturne.questionstime.question.Question;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.parameter.Parameter;

import java.util.function.BiConsumer;

public class StringQuestionComponent extends QuestionComponentComplex<String> {

    public StringQuestionComponent(final String componentName, final String commandDescription,
                                   final BiConsumer<Question.QuestionBuilder, String> setComponent) {
        super(componentName, commandDescription, setComponent);
    }

    @Override
    public Command.Parameterized create(final Parameter.Value<Question> specificQuestionParameter, final QuestionModifierComplex questionModifier) {
        return Command.builder()
                .shortDescription(Component.text(this.commandDescription).color(NamedTextColor.YELLOW))
                .addParameters(SetQuestionStringExecutor.VALUE)
                .executor(new SetQuestionStringExecutor(specificQuestionParameter, questionModifier, this))
                .build();
    }
}
