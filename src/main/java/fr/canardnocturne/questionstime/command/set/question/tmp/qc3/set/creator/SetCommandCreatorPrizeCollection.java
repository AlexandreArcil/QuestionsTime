package fr.canardnocturne.questionstime.command.set.question.tmp.qc3.set.creator;

import fr.canardnocturne.questionstime.command.set.question.tmp.qc3.component.QuestionComponentCollection;
import fr.canardnocturne.questionstime.command.set.question.tmp.qc3.section.QuestionSectionPrize;
import fr.canardnocturne.questionstime.command.set.question.tmp.qc3.set.QuestionRemoveComponentParameter;
import fr.canardnocturne.questionstime.command.set.question.tmp.qc3.set.SetQuestionComponentAddExecutor;
import fr.canardnocturne.questionstime.command.set.question.tmp.qc3.set.SetQuestionComponentListExecutor;
import fr.canardnocturne.questionstime.command.set.question.tmp.qc3.set.SetQuestionComponentRemoveExecutor;
import fr.canardnocturne.questionstime.question.Question;
import fr.canardnocturne.questionstime.question.ask.pool.QuestionPool;
import fr.canardnocturne.questionstime.question.component.Prize;
import fr.canardnocturne.questionstime.question.save.QuestionRegister;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.parameter.Parameter;

import java.util.Collection;

public class SetCommandCreatorPrizeCollection<W, V extends Collection<W>> extends SetCommandCreator<Prize, Prize.Builder, V> {

    private final QuestionSectionPrize<V> section;
    private final QuestionComponentCollection<W, V> component;

    public SetCommandCreatorPrizeCollection(final QuestionSectionPrize<V> section, final QuestionComponentCollection<W, V> component) {
        super(section, component);
        this.section = section;
        this.component = component;
    }

    @Override
    public Command.Parameterized create(final Parameter.Value<Question> specificQuestionParameter,
                                        final QuestionRegister questionRegister, final QuestionPool questionPool) {
        final Command.Parameterized commandQTSetQuestionListComponents = Command.builder()
                .shortDescription(Component.text("List the question " + component.getPlural()).color(NamedTextColor.YELLOW))
                .executor(new SetQuestionComponentListExecutor<>(specificQuestionParameter, section))
                .build();

        final Command.Parameterized commandQTSetQuestionAddComponents = Command.builder()
                .shortDescription(Component.text("Add " + component.getPlural() + " to the question").color(NamedTextColor.YELLOW))
                .addParameters(QuestionSectionPrize.POSITION, component.getParameter())
                .executor(new SetQuestionComponentAddExecutor<>(specificQuestionParameter, questionPool, questionRegister, component, section))
                .build();

        final Parameter.Value<String> removeComponentParameter = QuestionRemoveComponentParameter.create(specificQuestionParameter, section, component);
        final Command.Parameterized commandQTSetQuestionRemoveComponents = Command.builder()
                .shortDescription(Component.text("Remove " + component.getPlural() + " from the question").color(NamedTextColor.YELLOW))
                .addParameters(QuestionSectionPrize.POSITION, removeComponentParameter)
                .executor(new SetQuestionComponentRemoveExecutor<>(specificQuestionParameter, removeComponentParameter, questionPool, questionRegister, component, section))
                .build();

        return Command.builder()
                .shortDescription(Component.text("Set the question " + component.getPlural()).color(NamedTextColor.YELLOW))
                .addChild(commandQTSetQuestionListComponents, "list")
                .addChild(commandQTSetQuestionAddComponents, "add")
                .addChild(commandQTSetQuestionRemoveComponents, "remove")
                .build();
    }
}
