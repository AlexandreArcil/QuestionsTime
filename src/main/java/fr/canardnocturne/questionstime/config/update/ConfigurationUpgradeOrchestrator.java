package fr.canardnocturne.questionstime.config.update;

import fr.canardnocturne.questionstime.config.QuestionTimeConfiguration;
import org.apache.logging.log4j.Logger;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ConfigurationUpgradeOrchestrator {

    private final Map<Integer, ConfigurationUpdater> configurationUpdaters;
    private final Logger logger;

    public ConfigurationUpgradeOrchestrator(final List<ConfigurationUpdater> configurationUpdaters, final Logger logger) {
        this.configurationUpdaters = configurationUpdaters.stream()
                .collect(Collectors.toMap(ConfigurationUpdater::getVersion, Function.identity()));
        this.logger = logger;
    }

    public void upgradeConfiguration(final ConfigurationLoader<CommentedConfigurationNode> loader) {
        try {
            final CommentedConfigurationNode configNodeRoot = loader.load();
            final int configVersion = configNodeRoot.node("version").getInt(0);
            if(configVersion < QuestionTimeConfiguration.DefaultValues.VERSION) {
                for (int version = configVersion; version < QuestionTimeConfiguration.DefaultValues.VERSION; version++) {
                    final ConfigurationUpdater configurationUpdater = configurationUpdaters.get(version);
                    if (configurationUpdater != null) {
                        configurationUpdater.update(configNodeRoot);
                        configNodeRoot.node("version").set(version + 1).comment(QuestionTimeConfiguration.Comments.VERSION);
                    } else {
                        logger.warn("No config updater found for version {}", version);
                    }
                }
                loader.save(configNodeRoot);
                logger.info("Configuration upgraded from version {} to {}", configVersion, QuestionTimeConfiguration.DefaultValues.VERSION);
            }
        } catch (final SerializationException e) {
            logger.error("An error occurred while upgrading the config file. No upgrade has been saved.", e);
        } catch (final ConfigurateException e) {
            logger.error("An error occurred while loading the config file", e);
        }
    }

}
