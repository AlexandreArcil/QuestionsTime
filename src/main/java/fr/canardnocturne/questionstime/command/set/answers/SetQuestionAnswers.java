package fr.canardnocturne.questionstime.command.set.answers;

import fr.canardnocturne.questionstime.QuestionException;
import fr.canardnocturne.questionstime.command.change.QuestionModifier;
import fr.canardnocturne.questionstime.command.change.QuestionComponent;
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

public class SetQuestionAnswers implements CommandExecutor {

    private static final String ADD_ACTION = "add";
    private static final String REMOVE_ACTION = "remove";
    private static final Parameter.Value<String> ANSWER = Parameter.remainingJoinedStrings().key("answer").build();
    private static final Parameter.Value<String> ADD_REMOVE_ACTION = Parameter.choices(ADD_ACTION, REMOVE_ACTION).key("add_remove").build();
    public static final Parameter ACTIONS = Parameter.seq(ADD_REMOVE_ACTION, ANSWER);

    private final Parameter.Value<Question> specificQuestionParameter;
    private final QuestionModifier questionModifier;
    private final QuestionPool questionPool;
    private final QuestionRegister questionRegister;

    public SetQuestionAnswers(final Parameter.Value<Question> specificQuestionParameter, final QuestionModifier questionModifier, final QuestionPool questionPool, final QuestionRegister questionRegister) {
        this.specificQuestionParameter = specificQuestionParameter;
        this.questionModifier = questionModifier;
        this.questionPool = questionPool;
        this.questionRegister = questionRegister;
    }

    @Override
     public CommandResult execute(final CommandContext context) throws CommandException {
         final String action = context.requireOne(ADD_REMOVE_ACTION);
         final String answer = context.requireOne(ANSWER);
         final Question question = context.requireOne(this.specificQuestionParameter);
         try {
             final Question modifiedQuestion;
             if (action.equals(REMOVE_ACTION)) {
                 modifiedQuestion = this.questionModifier.remove(question, QuestionComponent.ANSWERS, answer);
             } else if (action.equals(ADD_ACTION)) {
                 modifiedQuestion = this.questionModifier.add(question, QuestionComponent.ANSWERS, answer);
             } else {
                 throw new CommandException(TextUtils.composed("Action ", action, " not found!"));
             }
             this.questionRegister.update(modifiedQuestion);
             this.questionPool.replace(question, modifiedQuestion);
             if (action.equals(REMOVE_ACTION)) {
                 context.sendMessage(TextUtils.composed("Answer ", answer, " removed !"));
             } else {
                 context.sendMessage(TextUtils.composed("Answer ", answer, " added !"));
             }
             return CommandResult.success();
         } catch (final QuestionException | IllegalArgumentException e) {
             return CommandResult.error(TextUtils.errorWithPrefix(e.getMessage()));
         } catch (final IOException e) {
             return CommandResult.error(TextUtils.errorWithPrefix("An error occurred while trying to save the question. See the log for details."));
         }
     }
}
