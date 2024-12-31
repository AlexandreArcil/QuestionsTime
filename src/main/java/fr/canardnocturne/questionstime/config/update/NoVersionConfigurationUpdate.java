package fr.canardnocturne.questionstime.config.update;

import fr.canardnocturne.questionstime.config.QuestionTimeConfiguration;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

public class NoVersionConfigurationUpdate implements ConfigurationUpdater {

    @Override
    public void update(final CommentedConfigurationNode configNodeRoot) throws SerializationException {
        final CommentedConfigurationNode randomTimeNode = configNodeRoot.node("randomTime");
        QuestionTimeConfiguration.Mode mode = QuestionTimeConfiguration.Mode.FIXED;
        if(!randomTimeNode.virtual()) {
            final boolean randomTime = randomTimeNode.getBoolean(false);
            configNodeRoot.removeChild("randomTime");
            mode = randomTime ? QuestionTimeConfiguration.Mode.INTERVAL : QuestionTimeConfiguration.Mode.FIXED;
        }
        configNodeRoot.node("mode").set(mode).comment(QuestionTimeConfiguration.Comments.MODE);
    }

    @Override
    public int getVersion() {
        return 0;
    }
}
