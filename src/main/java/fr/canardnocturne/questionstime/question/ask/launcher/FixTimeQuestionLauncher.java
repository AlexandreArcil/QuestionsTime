package fr.canardnocturne.questionstime.question.ask.launcher;

import fr.canardnocturne.questionstime.question.ask.QuestionAskManager;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.spongepowered.api.Game;
import org.spongepowered.api.scheduler.ScheduledTask;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.util.Ticks;
import org.spongepowered.plugin.PluginContainer;

public class FixTimeQuestionLauncher implements QuestionLauncher {

    private final PluginContainer pluginContainer;
    private final Game game;
    private final QuestionAskManager questionAskManager;
    private final int cooldown;
    private ScheduledTask fixTimeQuestionLauncherTask;

    protected FixTimeQuestionLauncher(final PluginContainer pluginContainer, final Game game, final QuestionAskManager questionAskManager, final int cooldown) {
        this.pluginContainer = pluginContainer;
        this.game = game;
        this.questionAskManager = questionAskManager;
        this.cooldown = cooldown;
    }

    @Override
    public void start() {
        final Task task = Task.builder().execute(questionAskManager::askRandomQuestion)
                .delay(Ticks.of(this.cooldown))
                .plugin(this.pluginContainer)
                .build();
        this.pluginContainer.logger().info("Next question will be asked in {}", DurationFormatUtils.formatDuration((cooldown / 20L) * 1000L, "H:mm:ss"));
        this.fixTimeQuestionLauncherTask = this.game.asyncScheduler().submit(task, "[QT]FixTimeQuestion");
    }

    @Override
    public void stop() {
        if(this.fixTimeQuestionLauncherTask != null) {
            this.fixTimeQuestionLauncherTask.cancel();
        }
    }

}
