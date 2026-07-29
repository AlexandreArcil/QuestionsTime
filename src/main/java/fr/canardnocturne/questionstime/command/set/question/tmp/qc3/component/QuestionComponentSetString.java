package fr.canardnocturne.questionstime.command.set.question.tmp.qc3.component;

import fr.canardnocturne.questionstime.question.Question;
import fr.canardnocturne.questionstime.question.ask.pool.QuestionPool;
import fr.canardnocturne.questionstime.question.save.QuestionRegister;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.Set;
import java.util.function.Function;

public class QuestionComponentSetString extends QuestionComponentSet<String> {

    public QuestionComponentSetString(final String name, final String singular, final String plural) {
        super(name, singular, plural, Function.identity(), Function.identity());
    }

}
