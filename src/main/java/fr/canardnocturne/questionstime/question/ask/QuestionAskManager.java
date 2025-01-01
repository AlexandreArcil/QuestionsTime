package fr.canardnocturne.questionstime.question.ask;

import fr.canardnocturne.questionstime.message.Messages;
import fr.canardnocturne.questionstime.question.ask.announcer.QuestionAnnouncer;
import fr.canardnocturne.questionstime.question.ask.answer.AnswerHandler;
import fr.canardnocturne.questionstime.question.ask.answer.PlayerAnswerQuestionHandler;
import fr.canardnocturne.questionstime.question.ask.launcher.QuestionLauncher;
import fr.canardnocturne.questionstime.question.ask.picker.QuestionPicker;
import fr.canardnocturne.questionstime.question.creation.QuestionCreationManager;
import fr.canardnocturne.questionstime.question.type.Question;
import fr.canardnocturne.questionstime.util.TextUtils;
import net.kyori.adventure.text.Component;
import org.apache.logging.log4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.scheduler.ScheduledTask;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.plugin.PluginContainer;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class QuestionAskManager {

    private final QuestionPicker questionPicker;
    private final QuestionAnnouncer questionAnnouncer;
    private final QuestionCreationManager questionCreationManager;
    private final Game game;
    private final PluginContainer plugin;
    private final Logger logger;
    private final int minimumConnectedPlayers;

    private QuestionLauncher questionLauncher;
    private Question currentQuestion;
    private ScheduledTask timerTask;
    private AnswerHandler playerAnswerQuestionHandler;
    private long timerStarted;

    public QuestionAskManager(final QuestionPicker questionPicker, final QuestionAnnouncer questionAnnouncer, final QuestionCreationManager questionCreationManager, final Game game, final PluginContainer plugin, final Logger logger, final int minimumConnectedPlayers) {
        this.questionPicker = questionPicker;
        this.questionAnnouncer = questionAnnouncer;
        this.questionCreationManager = questionCreationManager;
        this.game = game;
        this.plugin = plugin;
        this.logger = logger;
        this.minimumConnectedPlayers = minimumConnectedPlayers;
    }

    public void askRandomQuestion() {
        final Question randomQuestion = this.questionPicker.pick();
        this.askQuestion(randomQuestion);
    }

    public void askQuestion(final Question question) {
        if (!this.isQuestionHasBeenAsked()) {
            final List<ServerPlayer> eligiblePlayers = this.getEligiblePlayers();
            if (this.enoughEligiblePlayers(eligiblePlayers)) {
                this.currentQuestion = question;
                this.questionAnnouncer.announce(this.currentQuestion, eligiblePlayers);
                this.playerAnswerQuestionHandler = new PlayerAnswerQuestionHandler(this.logger, this.currentQuestion, this.game, this.plugin);
                if (this.currentQuestion.isTimed()) {
                    this.startTimer(this.currentQuestion.getTimer());
                }
            } else {
                this.logger.info("No enough eligible players ({}/{}), no question asked.", eligiblePlayers.size(), this.minimumConnectedPlayers);
                if (this.questionLauncher != null) {
                    this.questionLauncher.start();
                }
            }
        } else {
            this.logger.warn("Tried to ask a question while one is in progress");
        }
    }

    public void answer(final Player player, final String answer) {
        if (this.currentQuestion != null) {
            if (!this.questionCreationManager.isCreator(player.uniqueId())) {
                final boolean answerFound = this.playerAnswerQuestionHandler.answer(player, answer, this.getEligiblePlayers());
                if (answerFound) {
                    this.askNewQuestion();
                }
            } else {
                player.sendMessage(TextUtils.normalWithPrefix("You can't answer to a question when you are creating one!"));
            }
        } else {
            player.sendMessage(TextUtils.normalWithPrefix("No question has been asked, wait for the next one!"));
        }
    }

    public void setQuestionLauncher(final QuestionLauncher questionLauncher) {
        if (this.questionLauncher == null) {
            this.questionLauncher = questionLauncher;
        }
    }

    private void startTimer(final int timerSeconds) {
        final Task task = Task.builder().execute(consumer -> {
                    final List<ServerPlayer> eligiblePlayers = this.getEligiblePlayers();
                    final long secondStarted = (System.currentTimeMillis() - this.timerStarted) / 1000;
                    final int timeLeft = (int) (timerSeconds - secondStarted);
                    if (timeLeft == 0) {
                        TextUtils.sendTextToEveryone(Component.text(Messages.QUESTION_TIMER_OUT.getMessage()), eligiblePlayers);
                        this.askNewQuestion();
                    } else if (timeLeft % 3600 == 0 || timeLeft == 1800 || timeLeft == 900 || timeLeft == 300 || timeLeft == 60 || timeLeft == 30 || timeLeft == 15
                            || timeLeft == 5 || timeLeft == 4 || timeLeft == 3 || timeLeft == 2 || timeLeft == 1) {
                        TextUtils.sendTextToEveryone(Messages.QUESTION_TIMER_LEFT.format().setTimer(timeLeft).message(), eligiblePlayers);
                    }
                })
                .delay(1, TimeUnit.SECONDS)
                .interval(1, TimeUnit.SECONDS)
                .plugin(this.plugin)
                .build();
        this.timerTask = this.game.asyncScheduler().submit(task, "[QT]QuestionTimer");
        this.timerStarted = System.currentTimeMillis();
    }

    private void askNewQuestion() {
        if (this.timerTask != null) {
            this.timerTask.cancel();
        }
        this.playerAnswerQuestionHandler = null;
        this.currentQuestion = null;
        this.timerTask = null;
        this.timerStarted = 0;
        if (this.questionLauncher != null) {
            this.questionLauncher.start();
        }
    }

    public boolean enoughEligiblePlayers() {
        final List<ServerPlayer> eligiblePlayers = this.getEligiblePlayers();
        return this.enoughEligiblePlayers(eligiblePlayers);
    }

    private boolean enoughEligiblePlayers(List<ServerPlayer> eligiblePlayers) {
        return eligiblePlayers.size() >= this.minimumConnectedPlayers;
    }

    private List<ServerPlayer> getEligiblePlayers() {
        return this.game.server().onlinePlayers().stream()
                .filter(player -> !this.questionCreationManager.isCreator(player.uniqueId()))
                .toList();
    }

    public boolean isQuestionHasBeenAsked() {
        return this.currentQuestion != null;
    }

}
