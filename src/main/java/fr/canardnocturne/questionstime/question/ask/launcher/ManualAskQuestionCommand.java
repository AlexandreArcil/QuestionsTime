package fr.canardnocturne.questionstime.question.ask.launcher;

import fr.canardnocturne.questionstime.question.ask.QuestionAskManager;
import fr.canardnocturne.questionstime.question.type.Question;
import fr.canardnocturne.questionstime.util.TextUtils;
import org.apache.logging.log4j.Logger;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;

public class ManualAskQuestionCommand implements CommandExecutor {

    public static final Parameter.Value<String> RANDOM_QUESTION_ARG = Parameter.choices("random").key("random_question").build();

    private final QuestionAskManager askManager;
    private final QuestionLauncher questionLauncher;
    private final Parameter.Value<Question> specificQuestionParam;
    private final Logger logger;

    public ManualAskQuestionCommand(QuestionAskManager askManager, QuestionLauncher questionLauncher, Parameter.Value<Question> specificQuestionParam, Logger logger) {
        this.askManager = askManager;
        this.questionLauncher = questionLauncher;
        this.specificQuestionParam = specificQuestionParam;
        this.logger = logger;
    }

    @Override
    public CommandResult execute(CommandContext context) throws CommandException {
        if(this.askManager.isQuestionHasBeenAsked()) {
            return CommandResult.error(TextUtils.errorWithPrefix("A question has already being asked"));
        }
        if(!this.askManager.enoughEligiblePlayers()) {
            return CommandResult.error(TextUtils.errorWithPrefix("Not enough eligible players to ask a question"));
        }

        String causeIdentifier = context.friendlyIdentifier().orElse(context.identifier());
        this.questionLauncher.stop();
        if(context.hasAny(RANDOM_QUESTION_ARG)) {
            this.logger.info("A random question has been manually asked by {}", causeIdentifier);
            this.askManager.askRandomQuestion();
        } else {
            final Question questionArg = context.requireOne(this.specificQuestionParam);
            this.logger.info("Question '{}' manually asked by {}", questionArg.getQuestion(), causeIdentifier);
            this.askManager.askQuestion(questionArg);
        }
        return CommandResult.success();
    }

    // Aim to avoid the system to ask a new question after the previous one finished
    //private boolean asked;

    //protected ManualLauncher(PluginContainer pluginContainer, Game game, QuestionAskManager questionAskManager) {
    //    super(pluginContainer, game, questionAskManager);
    //}

    //@Override
    //public void start() {
        /*if(!this.questionAskManager.isQuestionHasBeenAsked()) {
            if(this.asked) {
                this.asked = false;
            } else {
                this.questionAskManager.askQuestion();
                this.asked = true;
            }
        }*/
    //}
}
