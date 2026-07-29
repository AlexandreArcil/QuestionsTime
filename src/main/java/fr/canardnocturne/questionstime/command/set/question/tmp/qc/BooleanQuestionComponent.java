package fr.canardnocturne.questionstime.command.set.question.tmp.qc;

import fr.canardnocturne.questionstime.question.Question;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.parameter.Parameter;

import java.util.function.BiConsumer;

public class BooleanQuestionComponent extends QuestionComponentComplex<Boolean> {

    public BooleanQuestionComponent(final String componentName, final String commandDescription,
                                    final BiConsumer<Question.QuestionBuilder, Boolean> setComponent) {
        super(componentName, commandDescription, setComponent);
    }

    public Command.Parameterized create(final Parameter.Value<Question> specificQuestionParameter,
                                        final QuestionModifierComplex questionModifier) {
        return Command.builder()
                .shortDescription(Component.text(this.commandDescription).color(NamedTextColor.YELLOW))
                .addParameters(SetQuestionBooleanExecutor.VALUE)
                .executor(new SetQuestionBooleanExecutor(specificQuestionParameter, questionModifier, this))
                .build();
    }

}
