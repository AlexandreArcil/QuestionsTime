package fr.canardnocturne.questionstime.config.upgrade;

import fr.canardnocturne.questionstime.config.QuestionTimeConfiguration;
import fr.canardnocturne.questionstime.config.upgrade.update.ConfigurationUpdater;
import org.apache.logging.log4j.Logger;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ConfigurationUpgradeOrchestrator implements ConfigurationUpgrade {

    private final Map<Integer, ConfigurationUpdater> configurationUpdaters;
    private final Logger logger;

    public ConfigurationUpgradeOrchestrator(final List<ConfigurationUpdater> configurationUpdaters, final Logger logger) {
        Objects.requireNonNull(configurationUpdaters, "configurationUpdaters");
        Objects.requireNonNull(logger, "logger");
        this.validateUpdaters(configurationUpdaters);
        this.configurationUpdaters = configurationUpdaters.stream()
                .collect(Collectors.toMap(ConfigurationUpdater::getVersion, Function.identity()));
        this.logger = logger;
    }

    public void upgrade(final ConfigurationLoader<CommentedConfigurationNode> loader) throws ConfigurationUpgradeException {
        try {
            final CommentedConfigurationNode configNodeRoot = loader.load();
            final int configVersion = configNodeRoot.node("version").getInt(0);
            if(configVersion < QuestionTimeConfiguration.DefaultValues.VERSION) {
                for (int version = configVersion; version < QuestionTimeConfiguration.DefaultValues.VERSION; version++) {
                    final ConfigurationUpdater configurationUpdater = configurationUpdaters.get(version);
                    configurationUpdater.update(configNodeRoot);
                }
                configNodeRoot.node("version")
                        .set(QuestionTimeConfiguration.DefaultValues.VERSION)
                        .comment(QuestionTimeConfiguration.Comments.VERSION);
                loader.save(configNodeRoot);
                logger.info("Configuration upgraded from version {} to {}", configVersion, QuestionTimeConfiguration.DefaultValues.VERSION);
            }
        } catch (final SerializationException e) {
            logger.error("An error occurred while upgrading the config file. No upgrade has been saved.", e);
            throw new ConfigurationUpgradeException(e);
        } catch (final ConfigurateException e) {
            logger.error("An error occurred while loading the config file", e);
            throw new ConfigurationUpgradeException(e);
        }
    }

    /**
     * Validate the list of configuration updaters: size must be equal to VERSION, each version in [0, VERSION-1] and unique
     * @param configurationUpdaters The list of configuration updaters to validate
     * @throws IllegalArgumentException If the list is invalid
     */
    private void validateUpdaters(final List<ConfigurationUpdater> configurationUpdaters) {
        if (configurationUpdaters.size() != QuestionTimeConfiguration.DefaultValues.VERSION) {
            throw new IllegalArgumentException("The number of configuration updaters must be equal to " + QuestionTimeConfiguration.DefaultValues.VERSION);
        }

        final Set<Integer> seenVersions = new HashSet<>();
        for (final ConfigurationUpdater updater : configurationUpdaters) {
            final int version = updater.getVersion();
            if (version < 0 || version >= QuestionTimeConfiguration.DefaultValues.VERSION) {
                throw new IllegalArgumentException("Configuration updater version must be between 0 (inclusive) and "
                        + (QuestionTimeConfiguration.DefaultValues.VERSION) + " (exclusive). Found: " + version);
            }
            if (!seenVersions.add(version)) {
                throw new IllegalArgumentException("Duplicate configuration updater version found: " + version);
            }
        }
    }

}
