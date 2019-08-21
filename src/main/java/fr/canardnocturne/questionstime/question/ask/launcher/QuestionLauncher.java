package fr.canardnocturne.questionstime.question.ask.launcher;

import fr.canardnocturne.questionstime.question.ask.QuestionAskManager;
import org.spongepowered.api.Game;
import org.spongepowered.plugin.PluginContainer;

public abstract class QuestionLauncher {

    protected final PluginContainer pluginContainer;
    protected final Game game;
    protected final QuestionAskManager questionAskManager;

    protected QuestionLauncher(final PluginContainer pluginContainer, final Game game, final QuestionAskManager questionAskManager) {
        this.pluginContainer = pluginContainer;
        this.game = game;
        this.questionAskManager = questionAskManager;
    }

    public abstract void start();
}
