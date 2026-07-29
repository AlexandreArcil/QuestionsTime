package fr.canardnocturne.questionstime.command.set.question.tmp.qc3.set.creator;

import fr.canardnocturne.questionstime.command.set.question.tmp.qc3.component.QuestionComponentBase;
import fr.canardnocturne.questionstime.command.set.question.tmp.qc3.section.QuestionSectionPrize;
import fr.canardnocturne.questionstime.command.set.question.tmp.qc3.set.SetQuestionComponentListExecutor;
import fr.canardnocturne.questionstime.command.set.question.tmp.qc3.set.SetQuestionComponentSetExecutor;
import fr.canardnocturne.questionstime.question.Question;
import fr.canardnocturne.questionstime.question.ask.pool.QuestionPool;
import fr.canardnocturne.questionstime.question.component.Prize;
import fr.canardnocturne.questionstime.question.save.QuestionRegister;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.parameter.Parameter;

public class SetCommandCreatorPrizeBase<V> extends SetCommandCreator<Prize, Prize.Builder, V> {

    private final QuestionSectionPrize<V> prizeSection;
    private final QuestionComponentBase<V> component;

    public SetCommandCreatorPrizeBase(final QuestionSectionPrize<V> prizeSection, final QuestionComponentBase<V> component) {
        super(prizeSection, component);
        this.prizeSection = prizeSection;
        this.component = component;
    }

    @Override
    public Command.Parameterized create(final Parameter.Value<Question> specificQuestionParameter,
                                        final QuestionRegister questionRegister, final QuestionPool questionPool) {

        final Command.Parameterized setCommandSetPrize = Command.builder()
                .shortDescription(Component.text("Set the " + component.getSingular() + " of a question").color(NamedTextColor.YELLOW))
                .addParameters(QuestionSectionPrize.POSITION, component.getParameter())
                .executor(new SetQuestionComponentSetExecutor<>(specificQuestionParameter, questionPool, questionRegister, component, prizeSection))
                .build();

        final Command.Parameterized SetCommandListPrize = Command.builder()
                .shortDescription(Component.text("List the " + component.getSingular() + " of a question").color(NamedTextColor.YELLOW))
                .addParameters(component.getParameter())
                .executor(new SetQuestionComponentListExecutor<>(specificQuestionParameter, prizeSection))
                .build();

        return Command.builder()
                .shortDescription(Component.text("Set the " + component.getSingular() + " of a question").color(NamedTextColor.YELLOW))
                .addChild(setCommandSetPrize, "set")
                .addChild(SetCommandListPrize, "list")
                .build();
    }
}
