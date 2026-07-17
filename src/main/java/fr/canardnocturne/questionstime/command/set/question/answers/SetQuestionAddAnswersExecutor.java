package fr.canardnocturne.questionstime.command.set.question.answers;

import fr.canardnocturne.questionstime.QuestionException;
import fr.canardnocturne.questionstime.command.set.question.CharacterParameter;
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
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class SetQuestionAddAnswersExecutor implements CommandExecutor {

    public static final Parameter.Value<String> ANSWERS = Parameter.string().consumeAllRemaining().key("answers").build();

    private final Parameter.Value<Question> specificQuestionParameter;
    private final QuestionModifier questionModifier;
    private final QuestionPool questionPool;
    private final QuestionRegister questionRegister;

    public SetQuestionAddAnswersExecutor(final Parameter.Value<Question> specificQuestionParameter, final QuestionModifier questionModifier, final QuestionPool questionPool, final QuestionRegister questionRegister) {
        this.specificQuestionParameter = specificQuestionParameter;
        this.questionModifier = questionModifier;
        this.questionPool = questionPool;
        this.questionRegister = questionRegister;
    }

    @Override
     public CommandResult execute(final CommandContext context) throws CommandException {
        final Question question = context.requireOne(this.specificQuestionParameter);
        final Collection<String> answers = Collections.unmodifiableCollection(context.all(ANSWERS));
         try {
             final Set<String> existingAnswers = question.getAnswers();
             final Question modifiedQuestion = this.questionModifier.add(question, QuestionComponent.ANSWERS, answers);
             this.questionRegister.replace(question, modifiedQuestion);
             this.questionPool.replace(question, modifiedQuestion);
             final Set<String> newAnswersSet = modifiedQuestion.getAnswers().stream()
                     .filter(answer -> !existingAnswers.contains(answer)).collect(Collectors.toSet());
             if(newAnswersSet.isEmpty()) {
                 context.sendMessage(TextUtils.composed("No new answers to add."));
             } else if(newAnswersSet.size() == 1) {
                 context.sendMessage(TextUtils.composed("Answer ", newAnswersSet.iterator().next(), " added !"));
             } else {
                 context.sendMessage(TextUtils.composed("Answers ", String.join(", ", newAnswersSet), " added !"));
             }
             return CommandResult.success();
         } catch (final QuestionException | IllegalArgumentException e) {
             return CommandResult.error(TextUtils.errorWithPrefix(e.getMessage()));
         } catch (final IOException e) {
             return CommandResult.error(TextUtils.errorWithPrefix("An error occurred while trying to save the question. See the log for details."));
         }
     }
}
