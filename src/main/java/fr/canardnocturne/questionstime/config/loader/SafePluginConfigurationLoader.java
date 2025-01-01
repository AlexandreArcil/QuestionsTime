package fr.canardnocturne.questionstime.config.loader;

import fr.canardnocturne.questionstime.config.QuestionTimeConfiguration;
import org.apache.logging.log4j.Logger;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.serialize.SerializationException;

public class SafePluginConfigurationLoader implements PluginConfigurationLoader {

    private final Logger logger;

    public SafePluginConfigurationLoader(final Logger logger) {
        this.logger = logger;
    }

    public QuestionTimeConfiguration load(final ConfigurationLoader<CommentedConfigurationNode> configLoader) {
        final CommentedConfigurationNode configNodeRoot;
        try {
            configNodeRoot = configLoader.load();
        } catch (final ConfigurateException e) {
            this.logger.info("Unable to read the configuration file, the default values will be used. Reason: '{}'", e.getMessage());
            this.logger.debug(e);
            return new QuestionTimeConfiguration();
        }

        final QuestionTimeConfiguration qTConfiguration;
        try {
            qTConfiguration = configNodeRoot.get(QuestionTimeConfiguration.class);
        } catch (final SerializationException e) {
            this.logger.info("Unable to read the values in the config file, check if it contains errors. The default values will be used until it is fixed. Reason: '{}'", e.getMessage());
            this.logger.debug(e);
            return new QuestionTimeConfiguration();
        }

        if (qTConfiguration == null) {
            this.logger.info("The config file doesn't contain the wanted fields and/or in the proper type. The default values will be used until it is fixed");
            return new QuestionTimeConfiguration();
        } else {
            this.logger.debug("Loaded config values: {}", qTConfiguration);
            return qTConfiguration;
        }
    }

}
