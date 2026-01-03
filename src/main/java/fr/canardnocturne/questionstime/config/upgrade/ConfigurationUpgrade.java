package fr.canardnocturne.questionstime.config.upgrade;

import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.loader.ConfigurationLoader;

public interface ConfigurationUpgrade {

    void upgrade(final ConfigurationLoader<CommentedConfigurationNode> loader) throws ConfigurationUpgradeException;

}
