package fr.canardnocturne.questionstime.question.ask.launcher;

import fr.canardnocturne.questionstime.config.Config;
import fr.canardnocturne.questionstime.question.ask.QuestionAskManager;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.spongepowered.api.Game;
import org.spongepowered.api.scheduler.ScheduledTask;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.util.Ticks;
import org.spongepowered.plugin.PluginContainer;

import java.util.Collections;

public class IntervalTimeQuestionLauncher implements QuestionLauncher {

    private final PluginContainer pluginContainer;
    private final Game game;
    private final QuestionAskManager questionAskManager;
    private final Config<Integer> minCooldown;
    private final Config<Integer> maxCooldown;
    private ScheduledTask intervalTimeQuestionLauncherTask;

    protected IntervalTimeQuestionLauncher(final PluginContainer pluginContainer, final Game game, final QuestionAskManager questionAskManager, final Config<Integer> minCooldown, final Config<Integer> maxCooldown) {
        this.pluginContainer = pluginContainer;
        this.game = game;
        this.questionAskManager = questionAskManager;
        this.minCooldown = minCooldown;
        this.maxCooldown = maxCooldown;
    }

    @Override
    public void start() {
        final int cooldown = this.minCooldown.getValue() + RandomUtils.nextInt(1, this.maxCooldown.getValue() - this.minCooldown.getValue());
        final Task mainTask = Task.builder().execute(() -> this.questionAskManager.askRandomQuestion(Collections.emptyList()))
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
