package fr.canardnocturne.questionstime.config.upgrade.update;

import fr.canardnocturne.questionstime.config.upgrade.ConfigurationUpgradeException;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

public interface ConfigurationUpdater {

    void update(final CommentedConfigurationNode configNodeRoot) throws SerializationException, ConfigurationUpgradeException;

    int getVersion();

}
