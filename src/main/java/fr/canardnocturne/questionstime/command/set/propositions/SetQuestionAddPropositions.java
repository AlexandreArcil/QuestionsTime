package fr.canardnocturne.questionstime.command.set.propositions;

import fr.canardnocturne.questionstime.QuestionException;
import fr.canardnocturne.questionstime.question.QuestionComponent;
import fr.canardnocturne.questionstime.question.modifier.QuestionModifier;
import fr.canardnocturne.questionstime.question.Question;
import fr.canardnocturne.questionstime.question.ask.pool.QuestionPool;
import fr.canardnocturne.questionstime.question.save.QuestionRegister;
import fr.canardnocturne.questionstime.util.TextUtils;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;

import java.io.IOException;

public class SetQuestionAddPropositions implements CommandExecutor {

    public static final Parameter.Value<String> PROPOSITION = Parameter.remainingJoinedStrings().key("proposition").build();

    private final Parameter.Value<Question> specificQuestionParameter;
    private final QuestionModifier questionModifier;
    private final QuestionPool questionPool;
    private final QuestionRegister questionRegister;

    public SetQuestionAddPropositions(final Parameter.Value<Question> specificQuestionParameter, final QuestionModifier questionModifier, final QuestionPool questionPool, final QuestionRegister questionRegister) {
        this.specificQuestionParameter = specificQuestionParameter;
        this.questionModifier = questionModifier;
        this.questionPool = questionPool;
        this.questionRegister = questionRegister;
    }

    @Override
    public CommandResult execute(final CommandContext context) throws CommandException {
        final String proposition = context.requireOne(PROPOSITION);
        final Question question = context.requireOne(this.specificQuestionParameter);
        try {
            final Question modifiedQuestion = this.questionModifier.add(question, QuestionComponent.PROPOSITIONS, proposition);
            this.questionRegister.replace(question, modifiedQuestion);
            this.questionPool.replace(question, modifiedQuestion);
            final String[] propositions = proposition.split(";");
            if(propositions.length == 1) {
                context.sendMessage(TextUtils.composed("Proposition ", proposition, " added !"));
            } else {
                context.sendMessage(TextUtils.composed("Propositions ", String.join(", ", propositions), " added !"));
            }
            return CommandResult.success();
        } catch (final QuestionException | IllegalArgumentException e) {
            return CommandResult.error(TextUtils.errorWithPrefix(e.getMessage()));
        } catch (final IOException e) {
            return CommandResult.error(TextUtils.errorWithPrefix("An error occurred while trying to save the question. See the log for details."));
        }
    }
}
