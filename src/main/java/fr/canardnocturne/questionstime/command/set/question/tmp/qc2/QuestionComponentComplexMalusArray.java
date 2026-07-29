package fr.canardnocturne.questionstime.command.set.question.tmp.qc2;

import fr.canardnocturne.questionstime.question.Question;
import fr.canardnocturne.questionstime.question.ask.pool.QuestionPool;
import fr.canardnocturne.questionstime.question.component.Malus;
import fr.canardnocturne.questionstime.question.save.QuestionRegister;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.parameter.Parameter;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class QuestionComponentComplexMalusArray<V> extends QuestionComponentComplexArray<Malus.Builder, Malus, V> {

    public QuestionComponentComplexMalusArray(final String name, final String singular, final String plural, final String commandDescription, final BiConsumer<Malus.Builder, V[]> setComponent, final Function<Malus, V[]> getComponent, final Function<String, V> valueMapper, final Function<V, String> valueUnmapper) {
        super(name, singular, plural, commandDescription, setComponent, getComponent, valueMapper, valueUnmapper);
    }

    @Override
    public Command.Parameterized createSetCommand(final Parameter.Value<Question> specificQuestionParameter, final QuestionRegister questionRegister, final QuestionPool questionPool) {
        return null;
    }

    @Override
    public Class<Malus> getType() {
        return Malus.class;
    }
}
