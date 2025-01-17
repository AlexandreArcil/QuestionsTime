package fr.canardnocturne.questionstime.question.ask.launcher;

import fr.canardnocturne.questionstime.config.QuestionTimeConfiguration;
import fr.canardnocturne.questionstime.question.ask.QuestionAskManager;
import org.spongepowered.api.Game;
import org.spongepowered.plugin.PluginContainer;

public class QuestionLauncherFactory {

    private QuestionLauncherFactory() {}

    public static QuestionLauncher create(final QuestionTimeConfiguration config, final PluginContainer pluginContainer, final Game game, final QuestionAskManager questionAskManager) {
        return switch (config.getMode()) {
            case FIXED -> new FixTimeQuestionLauncher(pluginContainer, game, questionAskManager, config.getCooldown());
            case INTERVAL ->
                    new IntervalTimeQuestionLauncher(pluginContainer, game, questionAskManager, config.getMinCooldown(), config.getMaxCooldown());
            case MANUAL -> null;
        };
    }

}
