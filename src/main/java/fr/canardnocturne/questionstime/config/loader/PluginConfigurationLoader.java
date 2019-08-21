package fr.canardnocturne.questionstime.config.loader;

import fr.canardnocturne.questionstime.config.QuestionTimeConfiguration;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.loader.ConfigurationLoader;

public interface PluginConfigurationLoader {

    QuestionTimeConfiguration load(final ConfigurationLoader<CommentedConfigurationNode> configLoader);

}
