package fr.canardnocturne.questionstime.command.set.question.tmp.qc3.component;

import fr.canardnocturne.questionstime.question.Question;
import fr.canardnocturne.questionstime.question.ask.pool.QuestionPool;
import fr.canardnocturne.questionstime.question.component.OutcomeCommand;
import fr.canardnocturne.questionstime.question.save.QuestionRegister;
import fr.canardnocturne.questionstime.question.serializer.OutcomeCommandSerializer;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.parameter.Parameter;

import java.util.function.Function;

public class QuestionComponentSetOutcomeCommands extends QuestionComponentSet<OutcomeCommand> {

    public QuestionComponentSetOutcomeCommands(final String name, final String singular, final String plural) {
        super(name, singular, plural, OutcomeCommandSerializer::deserialize, OutcomeCommandSerializer::serialize);
    }

}
