package fr.canardnocturne.questionstime.question.creation;

import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

public class CreateQuestionCommand implements CommandExecutor {

    public static Parameter.Value<String> STEP_ARG = Parameter.remainingJoinedStrings().optional().key("step_args").build();

    private final QuestionCreationManager questionCreationManager;

    public CreateQuestionCommand(final QuestionCreationManager questionCreationManager) {
        this.questionCreationManager = questionCreationManager;
    }

    @Override
    public CommandResult execute(final CommandContext context) {
        final ServerPlayer player = (ServerPlayer) context.cause().root();
        final String stepArgs = context.one(STEP_ARG).orElse("");
        this.questionCreationManager.handlePlayerArguments(player, stepArgs);
        return CommandResult.success();
    }

}
