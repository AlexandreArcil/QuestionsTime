package fr.canardnocturne.questionstime.question.ask.launcher;

import fr.canardnocturne.questionstime.question.ask.QuestionAskManager;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.spongepowered.api.Game;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.util.Ticks;
import org.spongepowered.plugin.PluginContainer;

public class IntervalTimeQuestionLauncher extends QuestionLauncher {

    private final int minCooldown;
    private final int maxCooldown;

    protected IntervalTimeQuestionLauncher(final PluginContainer pluginContainer, final Game game, final QuestionAskManager questionAskManager, final int minCooldown, final int maxCooldown) {
        super(pluginContainer, game, questionAskManager);
        this.minCooldown = minCooldown;
        this.maxCooldown = maxCooldown;
    }

    @Override
    public void start() {
        final int cooldown = this.minCooldown + RandomUtils.nextInt(1, this.maxCooldown - this.minCooldown);
        final Task mainTask = Task.builder().execute(this.questionAskManager::askQuestion)
                .plugin(this.pluginContainer)
                .delay(Ticks.of(cooldown)).build();
        this.pluginContainer.logger().info("Next question will be asked in {}", DurationFormatUtils.formatDuration((cooldown / 20L) * 1000L, "H:mm:ss"));
        this.game.asyncScheduler().submit(mainTask, "[QT]IntervalQuestion");
    }
}
