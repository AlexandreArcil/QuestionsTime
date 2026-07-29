package fr.canardnocturne.questionstime.command.set.question.tmp.qc3.set.creator;

import fr.canardnocturne.questionstime.command.set.question.tmp.qc3.component.QuestionComponentBase;
import fr.canardnocturne.questionstime.command.set.question.tmp.qc3.section.QuestionSectionBase;
import fr.canardnocturne.questionstime.question.Question;
import fr.canardnocturne.questionstime.question.ask.pool.QuestionPool;
import fr.canardnocturne.questionstime.question.save.QuestionRegister;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.parameter.Parameter;

public abstract class SetCommandCreator<T, B, V> {

    private final QuestionSectionBase<T, B, V> section;
    private final QuestionComponentBase<V> component;

    public SetCommandCreator(final QuestionSectionBase<T, B, V> section, final QuestionComponentBase<V> component) {
        this.section = section;
        this.component = component;
    }

    public abstract Command.Parameterized create(final Parameter.Value<Question> specificQuestionParameter,
                                 final QuestionRegister questionRegister, final QuestionPool questionPool);

    public String getAlias() {
        return this.component.getName();
    }

    public String getSection() {
        return this.section.getSection();
    }

}
