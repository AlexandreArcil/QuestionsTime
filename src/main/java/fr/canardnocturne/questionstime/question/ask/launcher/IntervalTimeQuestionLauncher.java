package fr.canardnocturne.questionstime.question.ask.launcher;

import fr.canardnocturne.questionstime.question.ask.QuestionAskManager;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.spongepowered.api.Game;
import org.spongepowered.api.scheduler.ScheduledTask;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.util.Ticks;
import org.spongepowered.plugin.PluginContainer;

public class IntervalTimeQuestionLauncher implements QuestionLauncher {

    private final PluginContainer pluginContainer;
    private final Game game;
    private final QuestionAskManager questionAskManager;
    private final int minCooldown;
    private final int maxCooldown;
    private ScheduledTask intervalTimeQuestionLauncherTask;

    protected IntervalTimeQuestionLauncher(final PluginContainer pluginContainer, final Game game, final QuestionAskManager questionAskManager, final int minCooldown, final int maxCooldown) {
        this.pluginContainer = pluginContainer;
        this.game = game;
        this.questionAskManager = questionAskManager;
        this.minCooldown = minCooldown;
        this.maxCooldown = maxCooldown;
    }

    @Override
    public void start() {
        final int cooldown = this.minCooldown + RandomUtils.nextInt(1, this.maxCooldown - this.minCooldown);
        final Task mainTask = Task.builder().execute(this.questionAskManager::askRandomQuestion)
                .plugin(this.pluginContainer)
                .delay(Ticks.of(cooldown)).build();
        this.pluginContainer.logger().info("Next question will be asked in {}", DurationFormatUtils.formatDuration((cooldown / 20L) * 1000L, "H:mm:ss"));
        this.intervalTimeQuestionLauncherTask = this.game.asyncScheduler().submit(mainTask, "[QT]IntervalQuestion");
    }

    @Override
    public void stop() {
        if(this.intervalTimeQuestionLauncherTask != null) {
            this.intervalTimeQuestionLauncherTask.cancel();
        }
    }
}
