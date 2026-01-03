package fr.canardnocturne.questionstime.config.upgrade;

import fr.canardnocturne.questionstime.config.QuestionTimeConfiguration;
import fr.canardnocturne.questionstime.config.upgrade.update.ConfigurationUpdater;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.ArrayList;
import java.util.List;

class ConfigurationUpgradeOrchestratorTest {

    @Test
    void upgradeVersionFromZeroToLast() throws ConfigurateException, ConfigurationUpgradeException {
        final Logger logger = Mockito.mock(Logger.class);
        final List<ConfigurationUpdater> configUpdaters = this.createConfigUpdaters();
        final CommentedConfigurationNode node = Mockito.mock(CommentedConfigurationNode.class);
        Mockito.when(node.node("version")).thenReturn(node);
        Mockito.when(node.getInt(0)).thenReturn(0);
        Mockito.when(node.set(Mockito.anyInt())).thenReturn(node);
        final ConfigurationLoader<CommentedConfigurationNode> configLoader = Mockito.mock(ConfigurationLoader.class);
        Mockito.when(configLoader.load()).thenReturn(node);

        final ConfigurationUpgradeOrchestrator configUpgrader = new ConfigurationUpgradeOrchestrator(configUpdaters, logger);
        configUpgrader.upgrade(configLoader);

        for (final ConfigurationUpdater configUpdater : configUpdaters) {
            Mockito.verify(configUpdater).update(node);
        }
        Mockito.verify(node).set(QuestionTimeConfiguration.DefaultValues.VERSION);
        Mockito.verify(configLoader).save(node);
    }

    @Test
    void upgradeVersionFromOneToLast() throws ConfigurateException, ConfigurationUpgradeException {
        final Logger logger = Mockito.mock(Logger.class);
        final List<ConfigurationUpdater> configUpdaters = this.createConfigUpdaters();
        final CommentedConfigurationNode node = Mockito.mock(CommentedConfigurationNode.class);
        Mockito.when(node.node("version")).thenReturn(node);
        Mockito.when(node.getInt(0)).thenReturn(1);
        Mockito.when(node.set(Mockito.anyInt())).thenReturn(node);
        final ConfigurationLoader<CommentedConfigurationNode> configLoader = Mockito.mock(ConfigurationLoader.class);
        Mockito.when(configLoader.load()).thenReturn(node);

        final ConfigurationUpgradeOrchestrator configUpgrader = new ConfigurationUpgradeOrchestrator(configUpdaters, logger);
        configUpgrader.upgrade(configLoader);

        Mockito.verify(node, Mockito.never()).set(1);
        Mockito.verify(configUpdaters.get(0), Mockito.never()).update(node);
        for (int i = 1; i < QuestionTimeConfiguration.DefaultValues.VERSION; i++) {
            final ConfigurationUpdater configUpdater = configUpdaters.get(i);
            Mockito.verify(configUpdater).update(node);
        }
        Mockito.verify(node).set(QuestionTimeConfiguration.DefaultValues.VERSION);
        Mockito.verify(configLoader).save(node);
    }

    @Test
    void noUpgradeWhenVersionIsLatest() throws ConfigurateException, ConfigurationUpgradeException {
        final Logger logger = Mockito.mock(Logger.class);
        final List<ConfigurationUpdater> configUpdaters = this.createConfigUpdaters();
        final CommentedConfigurationNode node = Mockito.mock(CommentedConfigurationNode.class);
        Mockito.when(node.node("version")).thenReturn(node);
        Mockito.when(node.getInt(0)).thenReturn(QuestionTimeConfiguration.DefaultValues.VERSION);
        final ConfigurationLoader<CommentedConfigurationNode> configLoader = Mockito.mock(ConfigurationLoader.class);
        Mockito.when(configLoader.load()).thenReturn(node);

        final ConfigurationUpgradeOrchestrator configUpgrader = new ConfigurationUpgradeOrchestrator(configUpdaters, logger);
        configUpgrader.upgrade(configLoader);

        for (final ConfigurationUpdater configUpdater : configUpdaters) {
            Mockito.verify(configUpdater, Mockito.never()).update(node);
        }
        Mockito.verify(configLoader, Mockito.never()).save(node);
    }

    @Test
    void updaterThrowSerializationException() throws ConfigurateException {
        final Logger logger = Mockito.mock(Logger.class);
        final List<ConfigurationUpdater> configUpdaters = this.createConfigUpdaters();
        final CommentedConfigurationNode node = Mockito.mock(CommentedConfigurationNode.class);
        Mockito.when(node.node("version")).thenReturn(node);
        Mockito.when(node.getInt(0)).thenReturn(0);
        final ConfigurationLoader<CommentedConfigurationNode> configLoader = Mockito.mock(ConfigurationLoader.class);
        Mockito.when(configLoader.load()).thenReturn(node);
        Mockito.doThrow(SerializationException.class).when(configUpdaters.get(0)).update(node);

        final ConfigurationUpgradeOrchestrator configUpgrader = new ConfigurationUpgradeOrchestrator(configUpdaters, logger);
        Assertions.assertThrows(ConfigurationUpgradeException.class, () -> configUpgrader.upgrade(configLoader));

        Mockito.verify(node, Mockito.never()).set(Mockito.anyInt());
        Mockito.verify(configLoader, Mockito.never()).save(node);
    }

    @Test
    void configLoaderThrowConfigurateException() throws ConfigurateException {
        final Logger logger = Mockito.mock(Logger.class);
        final List<ConfigurationUpdater> configUpdaters = this.createConfigUpdaters();
        final ConfigurationLoader<CommentedConfigurationNode> configLoader = Mockito.mock(ConfigurationLoader.class);
        Mockito.when(configLoader.load()).thenThrow(ConfigurateException.class);

        final ConfigurationUpgradeOrchestrator configUpgrader = new ConfigurationUpgradeOrchestrator(configUpdaters, logger);
        Assertions.assertThrows(ConfigurationUpgradeException.class, () -> configUpgrader.upgrade(configLoader));

        for (final ConfigurationUpdater configUpdater : configUpdaters) {
            Mockito.verify(configUpdater, Mockito.never()).update(Mockito.any());
        }
        Mockito.verify(configLoader, Mockito.never()).save(Mockito.any());
    }

    @Test
    void invalidNumberOfUpdaters() {
        final Logger logger = Mockito.mock(Logger.class);
        final List<ConfigurationUpdater> configUpdaters = this.createConfigUpdaters();
        configUpdaters.removeFirst(); //Make the list invalid

        Assertions.assertThrows(IllegalArgumentException.class, () -> new ConfigurationUpgradeOrchestrator(configUpdaters, logger));
    }

    @Test
    void duplicateVersionOfUpdaters() {
        final Logger logger = Mockito.mock(Logger.class);
        final List<ConfigurationUpdater> configUpdaters = this.createConfigUpdaters();
        //Make a duplicate version
        final ConfigurationUpdater duplicateUpdater = Mockito.mock(ConfigurationUpdater.class);
        Mockito.when(duplicateUpdater.getVersion()).thenReturn(0);
        configUpdaters.set(1, duplicateUpdater);

        Assertions.assertThrows(IllegalArgumentException.class, () -> new ConfigurationUpgradeOrchestrator(configUpdaters, logger));
    }

    @Test
    void missingVersionOfUpdaters() {
        final Logger logger = Mockito.mock(Logger.class);
        final List<ConfigurationUpdater> configUpdaters = this.createConfigUpdaters();
        //Make a missing version
        final ConfigurationUpdater missingVersionUpdater = Mockito.mock(ConfigurationUpdater.class);
        Mockito.when(missingVersionUpdater.getVersion()).thenReturn(QuestionTimeConfiguration.DefaultValues.VERSION);
        configUpdaters.set(0, missingVersionUpdater);

        Assertions.assertThrows(IllegalArgumentException.class, () -> new ConfigurationUpgradeOrchestrator(configUpdaters, logger));
    }

    @Test
    void nullUpdatersList() {
        final Logger logger = Mockito.mock(Logger.class);

        Assertions.assertThrows(NullPointerException.class, () -> new ConfigurationUpgradeOrchestrator(null, logger));
    }

    @Test
    void nullLogger() {
        final List<ConfigurationUpdater> configUpdaters = this.createConfigUpdaters();

        Assertions.assertThrows(NullPointerException.class, () -> new ConfigurationUpgradeOrchestrator(configUpdaters, null));
    }

    @Test
    void nullConfigLoader() {
        final Logger logger = Mockito.mock(Logger.class);
        final List<ConfigurationUpdater> configUpdaters = this.createConfigUpdaters();
        final ConfigurationUpgradeOrchestrator configUpgrader = new ConfigurationUpgradeOrchestrator(configUpdaters, logger);

        Assertions.assertThrows(NullPointerException.class, () -> configUpgrader.upgrade(null));
    }

    private List<ConfigurationUpdater> createConfigUpdaters() {
        final List<ConfigurationUpdater> configUpdaters = new ArrayList<>();
        for(int i = 0; i < QuestionTimeConfiguration.DefaultValues.VERSION; i++) {
            final ConfigurationUpdater configUpdater = Mockito.mock(ConfigurationUpdater.class);
            Mockito.when(configUpdater.getVersion()).thenReturn(i);
            configUpdaters.add(configUpdater);
        }
        return configUpdaters;
    }

}