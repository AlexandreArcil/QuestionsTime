package fr.canardnocturne.questionstime.command.set.question.tmp.qc3.component;

import fr.canardnocturne.questionstime.question.Question;
import fr.canardnocturne.questionstime.question.ask.pool.QuestionPool;
import fr.canardnocturne.questionstime.question.save.QuestionRegister;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.parameter.Parameter;

import java.util.function.Function;

public class QuestionComponentListString extends QuestionComponentList<String> {

    public QuestionComponentListString(final String name, final String singular, final String plural) {
        super(name, singular, plural, Function.identity(), Function.identity());
    }

}
