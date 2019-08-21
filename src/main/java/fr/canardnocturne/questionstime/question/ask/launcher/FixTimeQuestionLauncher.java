package fr.canardnocturne.questionstime.question.ask.launcher;

import fr.canardnocturne.questionstime.question.ask.QuestionAskManager;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.spongepowered.api.Game;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.util.Ticks;
import org.spongepowered.plugin.PluginContainer;

public class FixTimeQuestionLauncher extends QuestionLauncher {

    private final int cooldown;

    protected FixTimeQuestionLauncher(final PluginContainer pluginContainer, final Game game, final QuestionAskManager questionAskManager, final int cooldown) {
        super(pluginContainer, game, questionAskManager);
        this.cooldown = cooldown;
    }

    @Override
    public void start() {
        final Task task = Task.builder().execute(questionAskManager::askQuestion)
                .delay(Ticks.of(this.cooldown))
                .plugin(this.pluginContainer)
                .build();
        this.pluginContainer.logger().info("Next question will be asked in {}", DurationFormatUtils.formatDuration((cooldown / 20L) * 1000L, "H:mm:ss"));
        this.game.asyncScheduler().submit(task, "[QT]FixTimeQuestion");
    }

}
