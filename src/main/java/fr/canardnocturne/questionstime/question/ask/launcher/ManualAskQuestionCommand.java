package fr.canardnocturne.questionstime.question.ask.launcher;

import fr.canardnocturne.questionstime.question.ask.QuestionAskManager;
import fr.canardnocturne.questionstime.question.Question;
import fr.canardnocturne.questionstime.util.TextUtils;
import org.apache.logging.log4j.Logger;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;

import java.util.Collection;
import java.util.Collections;

public class ManualAskQuestionCommand implements CommandExecutor {

    public static final Parameter.Value<String> RANDOM_QUESTION_ARG = Parameter.choices("random").key("random_question").build();

    private final QuestionAskManager askManager;
    private final QuestionLauncher questionLauncher;
    private final Parameter.Value<Question> specificQuestionParam;
    private final Parameter.Value<String> tagsParam;
    private final Logger logger;

    public ManualAskQuestionCommand(final QuestionAskManager askManager, final QuestionLauncher questionLauncher,
                                    final Parameter.Value<Question> specificQuestionParam, final Parameter.Value<String> tagsParam,
                                    final Logger logger) {
        this.askManager = askManager;
        this.questionLauncher = questionLauncher;
        this.specificQuestionParam = specificQuestionParam;
        this.tagsParam = tagsParam;
        this.logger = logger;
    }

    @Override
    public CommandResult execute(final CommandContext context) throws CommandException {
        if(this.askManager.isQuestionHasBeenAsked()) {
            return CommandResult.error(TextUtils.errorWithPrefix("A question has already being asked"));
        }

        final String causeIdentifier = context.friendlyIdentifier().orElse(context.identifier());
        if(this.questionLauncher != null) {
            this.questionLauncher.stop();
        }

        if(context.hasAny(RANDOM_QUESTION_ARG)) {
            final Collection<String> tags = Collections.unmodifiableCollection(context.all(this.tagsParam));
            this.logger.info("A random question has been manually asked by {} with the tags {}", causeIdentifier,  tags);
            try {
                this.askManager.askRandomQuestion(tags);
            } catch (final IllegalArgumentException e) {
                return CommandResult.error(TextUtils.errorWithPrefix(e.getMessage()));
            }
        } else {
            final Question questionArg = context.requireOne(this.specificQuestionParam);
            if(!this.askManager.enoughEligiblePlayers(questionArg)) {
                return CommandResult.error(TextUtils.errorWithPrefix("Not enough eligible players to ask this question"));
            }
            this.logger.info("Question '{}' manually asked by {}", questionArg.getQuestion(), causeIdentifier);
            this.askManager.askQuestion(questionArg);
        }
        return CommandResult.success();
    }

}
