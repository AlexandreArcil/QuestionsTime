package fr.canardnocturne.questionstime.command.set.question.permissions.include;

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

public class SetQuestionAddIncludePermissionsExecutor implements CommandExecutor {

    private final Parameter.Value<Question> specificQuestionParameter;
    private final Parameter.Value<String> permissionsParameter;
    private final QuestionModifier questionModifier;
    private final QuestionPool questionPool;
    private final QuestionRegister questionRegister;

    public SetQuestionAddIncludePermissionsExecutor(final Parameter.Value<Question> specificQuestionParameter,
                                                    final Parameter.Value<String> permissionsParameter,
                                                    final QuestionModifier questionModifier, final QuestionPool questionPool,
                                                    final QuestionRegister questionRegister) {
        this.specificQuestionParameter = specificQuestionParameter;
        this.permissionsParameter = permissionsParameter;
        this.questionModifier = questionModifier;
        this.questionPool = questionPool;
        this.questionRegister = questionRegister;
    }

    @Override
    public CommandResult execute(final CommandContext context) throws CommandException {
        /*final String permissions = String.join(" ", context.all(this.permissionsParameter));
        final Question question = context.requireOne(this.specificQuestionParameter);
        try {
            final Set<String> existingPermissions = question.getIncludePermissions();
            final Question modifiedQuestion = this.questionModifier.add(question, QuestionComponent.INCLUDE_PERMISSIONS, permissions);
            this.questionRegister.replace(question, modifiedQuestion);
            this.questionPool.replace(question, modifiedQuestion);
            final Set<String> permissionsSet = modifiedQuestion.getIncludePermissions();
            final Set<String> newPermissionsSet = permissionsSet.stream().filter(permission -> !existingPermissions.contains(permission)).collect(Collectors.toSet());
            if(newPermissionsSet.isEmpty()) {
                context.sendMessage(TextUtils.composed("No new include permissions to add."));
            } else if(newPermissionsSet.size() == 1) {
                context.sendMessage(TextUtils.composed("Include permission ", newPermissionsSet.iterator().next(), " added !"));
            } else {
                context.sendMessage(TextUtils.composed("Include permissions ", String.join(", ", newPermissionsSet), " added !"));
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
