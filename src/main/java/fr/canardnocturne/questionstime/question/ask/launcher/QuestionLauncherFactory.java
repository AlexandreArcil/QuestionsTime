package fr.canardnocturne.questionstime.question.ask.launcher;

import fr.canardnocturne.questionstime.config.QuestionTimeConfiguration;
import fr.canardnocturne.questionstime.question.ask.QuestionAskManager;
import org.spongepowered.api.Game;
import org.spongepowered.plugin.PluginContainer;

public class QuestionLauncherFactory {

    private QuestionLauncherFactory() {}

    public static QuestionLauncher create(final QuestionTimeConfiguration config, final PluginContainer pluginContainer, final Game game, final QuestionAskManager questionAskManager) {
        if (config.isRandom()) {
            return new IntervalTimeQuestionLauncher(pluginContainer, game, questionAskManager, config.getMinCooldown(), config.getMaxCooldown());
        } else {
            return new FixTimeQuestionLauncher(pluginContainer, game, questionAskManager, config.getCooldown());
        }
    }

}
