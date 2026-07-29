package fr.canardnocturne.questionstime.command.set.question.tmp.qc3.set.creator;

import fr.canardnocturne.questionstime.command.set.question.tmp.qc3.component.QuestionComponentBase;
import fr.canardnocturne.questionstime.command.set.question.tmp.qc3.section.QuestionSectionBase;
import fr.canardnocturne.questionstime.command.set.question.tmp.qc3.set.SetQuestionComponentSetExecutor;
import fr.canardnocturne.questionstime.question.Question;
import fr.canardnocturne.questionstime.question.ask.pool.QuestionPool;
import fr.canardnocturne.questionstime.question.save.QuestionRegister;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.parameter.Parameter;

public class SetCommandCreatorBase<T, B, V> extends SetCommandCreator<T, B, V> {

    private final QuestionSectionBase<T, B, V> section;
    private final QuestionComponentBase<V> component;

    public SetCommandCreatorBase(final QuestionSectionBase<T, B, V> section, final QuestionComponentBase<V> component) {
        super(section, component);
        this.section = section;
        this.component = component;
    }

    @Override
    public Command.Parameterized create(final Parameter.Value<Question> specificQuestionParameter,
                                        final QuestionRegister questionRegister, final QuestionPool questionPool) {
        return Command.builder()
                .shortDescription(Component.text("Set the " + component.getSingular() + " of a question").color(NamedTextColor.YELLOW))
                .addParameter(component.getParameter())
                .executor(new SetQuestionComponentSetExecutor<>(specificQuestionParameter, questionPool, questionRegister, component, section))
                .build();
    }
}
