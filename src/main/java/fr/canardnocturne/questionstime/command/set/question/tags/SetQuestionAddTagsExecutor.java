package fr.canardnocturne.questionstime.command.set.question.tags;

import fr.canardnocturne.questionstime.QuestionException;
import fr.canardnocturne.questionstime.question.Question;
import fr.canardnocturne.questionstime.question.QuestionComponent;
import fr.canardnocturne.questionstime.question.ask.pool.QuestionPool;
import fr.canardnocturne.questionstime.question.modifier.QuestionModifier;
import fr.canardnocturne.questionstime.question.save.QuestionRegister;
import fr.canardnocturne.questionstime.util.TextUtils;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

public class SetQuestionAddTagsExecutor implements CommandExecutor {

    public static final Parameter.Value<String> TAGS = Parameter.remainingJoinedStrings().key("tags").build();

    private final Parameter.Value<Question> specificQuestionParameter;
    private final QuestionModifier questionModifier;
    private final QuestionPool questionPool;
    private final QuestionRegister questionRegister;

    public SetQuestionAddTagsExecutor(final Parameter.Value<Question> specificQuestionParameter, final QuestionModifier questionModifier, final QuestionPool questionPool, final QuestionRegister questionRegister) {
        this.specificQuestionParameter = specificQuestionParameter;
        this.questionModifier = questionModifier;
        this.questionPool = questionPool;
        this.questionRegister = questionRegister;
    }

    @Override
    public CommandResult execute(final CommandContext context) throws CommandException {
        /*final String tags = context.requireOne(TAGS);
        final Question question = context.requireOne(this.specificQuestionParameter);
        try {
            final Set<String> existingTags = question.getTags();
            final Question modifiedQuestion = this.questionModifier.add(question, QuestionComponent.TAGS, tags);
            this.questionRegister.replace(question, modifiedQuestion);
            this.questionPool.replace(question, modifiedQuestion);
            final Set<String> tagsSet = modifiedQuestion.getTags();
            final Set<String> newTagsSet = tagsSet.stream().filter(tag -> !existingTags.contains(tag)).collect(Collectors.toSet());
            if(newTagsSet.isEmpty()) {
                context.sendMessage(TextUtils.composed("No new tags to add."));
            } else if(newTagsSet.size() == 1) {
                context.sendMessage(TextUtils.composed("Tag ", newTagsSet.iterator().next(), " added !"));
            } else {
                context.sendMessage(TextUtils.composed("Tags ", String.join(", ", newTagsSet), " added !"));
            }
            return CommandResult.success();
        } catch (final QuestionException | IllegalArgumentException e) {
            return CommandResult.error(TextUtils.errorWithPrefix(e.getMessage()));
        } catch (final IOException e) {
            return CommandResult.error(TextUtils.errorWithPrefix("An error occurred while trying to save the question. See the log for details."));
        }*/
        return CommandResult.success();
    }
}
