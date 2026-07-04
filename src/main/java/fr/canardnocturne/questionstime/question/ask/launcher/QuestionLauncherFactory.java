package fr.canardnocturne.questionstime.question.ask.launcher;

import fr.canardnocturne.questionstime.config.Config;
import fr.canardnocturne.questionstime.config.QuestionTimeConfiguration;
import fr.canardnocturne.questionstime.question.ask.QuestionAskManager;
import org.spongepowered.api.Game;
import org.spongepowered.plugin.PluginContainer;

public class QuestionLauncherFactory {

    private final PluginContainer pluginContainer;
    private final Game game;
    private final QuestionAskManager questionAskManager;
    private final Config<Integer> cooldownConfig;
    private final Config<Integer> minCooldownConfig;
    private final Config<Integer> maxCooldownConfig;

    public QuestionLauncherFactory(final PluginContainer pluginContainer, final Game game, final QuestionAskManager questionAskManager, final Config<Integer> cooldownConfig, final Config<Integer> minCooldownConfig, final Config<Integer> maxCooldownConfig) {
        this.pluginContainer = pluginContainer;
        this.game = game;
        this.questionAskManager = questionAskManager;
        this.cooldownConfig = cooldownConfig;
        this.minCooldownConfig = minCooldownConfig;
        this.maxCooldownConfig = maxCooldownConfig;
    }

    public QuestionLauncher create(final QuestionTimeConfiguration.Mode mode) {
        return switch (mode) {
            case FIXED -> new FixTimeQuestionLauncher(pluginContainer, game, questionAskManager, cooldownConfig);
            case INTERVAL ->
                    new IntervalTimeQuestionLauncher(pluginContainer, game, questionAskManager, minCooldownConfig, maxCooldownConfig);
            case MANUAL -> null;
        };
    }

}
