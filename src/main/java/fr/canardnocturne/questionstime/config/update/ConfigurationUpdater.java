package fr.canardnocturne.questionstime.config.update;

import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

public interface ConfigurationUpdater {

    void update(CommentedConfigurationNode configNodeRoot) throws SerializationException;

    int getVersion();

}
