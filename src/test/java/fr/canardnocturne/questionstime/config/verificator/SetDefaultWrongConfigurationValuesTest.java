package fr.canardnocturne.questionstime.config.verificator;

import fr.canardnocturne.questionstime.config.QuestionTimeConfiguration;
import fr.canardnocturne.questionstime.util.SpongeMock;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SetDefaultWrongConfigurationValuesTest extends SpongeMock {

    @Test
    void minConnectedShouldBePositive() {
        final QuestionTimeConfiguration config = new QuestionTimeConfiguration();
        config.setMinConnected(0);

        final Logger logger = Mockito.mock(Logger.class);
        final SetDefaultWrongConfigurationValues verificator = new SetDefaultWrongConfigurationValues(logger);
        verificator.verify(config);

        assertEquals(QuestionTimeConfiguration.DefaultValues.MIN_CONNECTED, config.getMinConnected());
        Mockito.verify(logger).warn(Mockito.anyString(), Mockito.any(Object.class), Mockito.any(Object.class), Mockito.any(Object.class), Mockito.any(Object.class));
    }

    @Test
    void cooldownShouldBePositive() {
        final QuestionTimeConfiguration config = new QuestionTimeConfiguration();
        config.setCooldown(-5);

        final Logger logger = Mockito.mock(Logger.class);
        final SetDefaultWrongConfigurationValues verificator = new SetDefaultWrongConfigurationValues(logger);
        verificator.verify(config);

        assertEquals(QuestionTimeConfiguration.DefaultValues.COOLDOWN, config.getCooldown());
        Mockito.verify(logger).warn(Mockito.anyString(), Mockito.any(Object.class), Mockito.any(Object.class), Mockito.any(Object.class), Mockito.any(Object.class));
    }

    @Test
    void minCooldownShouldBePositive() {
        final QuestionTimeConfiguration config = new QuestionTimeConfiguration();
        config.setMinCooldown(-5);

        final Logger logger = Mockito.mock(Logger.class);
        final SetDefaultWrongConfigurationValues verificator = new SetDefaultWrongConfigurationValues(logger);
        verificator.verify(config);

        assertEquals(QuestionTimeConfiguration.DefaultValues.MIN_COOLDOWN, config.getMinCooldown());
        Mockito.verify(logger).warn(Mockito.anyString(), Mockito.any(Object.class), Mockito.any(Object.class), Mockito.any(Object.class), Mockito.any(Object.class));
    }

    @Test
    void maxCooldownShouldBePositive() {
        final QuestionTimeConfiguration config = new QuestionTimeConfiguration();
        config.setMaxCooldown(-5);

        final Logger logger = Mockito.mock(Logger.class);
        final SetDefaultWrongConfigurationValues verificator = new SetDefaultWrongConfigurationValues(logger);
        verificator.verify(config);

        assertEquals(QuestionTimeConfiguration.DefaultValues.MAX_COOLDOWN, config.getMaxCooldown());
        Mockito.verify(logger).warn(Mockito.anyString(), Mockito.any(Object.class), Mockito.any(Object.class), Mockito.any(Object.class), Mockito.any(Object.class));
    }

    @Test
    void minCooldownShouldNotBeGreaterThanMaxCooldown() {
        final QuestionTimeConfiguration config = new QuestionTimeConfiguration();
        config.setMinCooldown(50);
        config.setMaxCooldown(5);

        final Logger logger = Mockito.mock(Logger.class);
        final SetDefaultWrongConfigurationValues verificator = new SetDefaultWrongConfigurationValues(logger);
        verificator.verify(config);

        assertEquals(QuestionTimeConfiguration.DefaultValues.MIN_COOLDOWN, config.getMinCooldown());
        assertEquals(QuestionTimeConfiguration.DefaultValues.MAX_COOLDOWN, config.getMaxCooldown());
        Mockito.verify(logger).warn(Mockito.anyString(), Mockito.any(Object.class), Mockito.any(Object.class));
    }

    @Test
    void versionShouldNotBeGreaterThanCurrentVersion() {
        final QuestionTimeConfiguration config = new QuestionTimeConfiguration();
        config.setVersion(QuestionTimeConfiguration.DefaultValues.VERSION + 1);

        final Logger logger = Mockito.mock(Logger.class);
        final SetDefaultWrongConfigurationValues verificator = new SetDefaultWrongConfigurationValues(logger);
        verificator.verify(config);

        assertEquals(QuestionTimeConfiguration.DefaultValues.VERSION, config.getVersion());
        Mockito.verify(logger).warn(Mockito.anyString(), Mockito.any(Object.class));
    }



}