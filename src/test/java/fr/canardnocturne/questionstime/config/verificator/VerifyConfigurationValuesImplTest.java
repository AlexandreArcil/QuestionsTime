package fr.canardnocturne.questionstime.config.verificator;

import fr.canardnocturne.questionstime.config.ConfigField;
import fr.canardnocturne.questionstime.config.QuestionTimeConfiguration;
import fr.canardnocturne.questionstime.util.SpongeMock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VerifyConfigurationValuesImplTest extends SpongeMock {

    @Test
    void minConnectedShouldBePositive() {
        final QuestionTimeConfiguration config = new QuestionTimeConfiguration();
        config.setMinConnected(0);

        final VerifyConfigurationValuesImpl verificator = new VerifyConfigurationValuesImpl();
        final VerifyConfigurationValues.Result verify = verificator.verify(config);

        assertFalse(verify.isSuccess());
        assertTrue(verify.getWrongValues().containsKey(ConfigField.MINIMUM_CONNECTED));
    }

    @Test
    void cooldownShouldBePositive() {
        final QuestionTimeConfiguration config = new QuestionTimeConfiguration();
        config.setCooldown(-5);

        final VerifyConfigurationValuesImpl verificator = new VerifyConfigurationValuesImpl();
        final VerifyConfigurationValues.Result verify = verificator.verify(config);

        assertFalse(verify.isSuccess());
        assertTrue(verify.getWrongValues().containsKey(ConfigField.COOLDOWN));
    }

    @Test
    void minCooldownShouldBePositive() {
        final QuestionTimeConfiguration config = new QuestionTimeConfiguration();
        config.setMinCooldown(-5);

        final VerifyConfigurationValuesImpl verificator = new VerifyConfigurationValuesImpl();
        final VerifyConfigurationValues.Result verify = verificator.verify(config);

        assertFalse(verify.isSuccess());
        assertTrue(verify.getWrongValues().containsKey(ConfigField.MIN_COOLDOWN));
    }

    @Test
    void maxCooldownShouldBePositive() {
        final QuestionTimeConfiguration config = new QuestionTimeConfiguration();
        config.setMaxCooldown(-5);

        final VerifyConfigurationValuesImpl verificator = new VerifyConfigurationValuesImpl();
        final VerifyConfigurationValues.Result verify = verificator.verify(config);

        assertFalse(verify.isSuccess());
        assertTrue(verify.getWrongValues().containsKey(ConfigField.MAX_COOLDOWN));
    }

    @Test
    void minCooldownShouldNotBeGreaterThanMaxCooldown() {
        final QuestionTimeConfiguration config = new QuestionTimeConfiguration();
        config.setMinCooldown(50);
        config.setMaxCooldown(5);

        final VerifyConfigurationValuesImpl verificator = new VerifyConfigurationValuesImpl();
        final VerifyConfigurationValues.Result verify = verificator.verify(config);

        assertFalse(verify.isSuccess());
        assertTrue(verify.getWrongValues().containsKey(ConfigField.MIN_COOLDOWN));
    }

    @Test
    void versionShouldNotBeGreaterThanCurrentVersion() {
        final QuestionTimeConfiguration config = new QuestionTimeConfiguration();
        config.setVersion(QuestionTimeConfiguration.DefaultValues.VERSION + 1);

        final VerifyConfigurationValuesImpl verificator = new VerifyConfigurationValuesImpl();
        final VerifyConfigurationValues.Result verify = verificator.verify(config);

        assertFalse(verify.isSuccess());
        assertTrue(verify.getWrongValues().containsKey(ConfigField.VERSION));
    }

    @Test
    void allFieldsAreWrong() {
        final QuestionTimeConfiguration config = new QuestionTimeConfiguration();
        config.setMinConnected(0);
        config.setCooldown(-5);
        config.setMaxCooldown(-5);
        config.setVersion(QuestionTimeConfiguration.DefaultValues.VERSION + 1);
        config.setMinCooldown(-2);

        final VerifyConfigurationValuesImpl verificator = new VerifyConfigurationValuesImpl();
        final VerifyConfigurationValues.Result verify = verificator.verify(config);

        assertFalse(verify.isSuccess());
        assertTrue(verify.getWrongValues().containsKey(ConfigField.MINIMUM_CONNECTED));
        assertEquals(1, verify.getWrongValues().get(ConfigField.MINIMUM_CONNECTED).size());
        assertTrue(verify.getWrongValues().containsKey(ConfigField.COOLDOWN));
        assertTrue(verify.getWrongValues().containsKey(ConfigField.MAX_COOLDOWN));
        assertTrue(verify.getWrongValues().containsKey(ConfigField.VERSION));
        assertTrue(verify.getWrongValues().containsKey(ConfigField.MIN_COOLDOWN));
    }

    @Test
    void allValuesAreCorrect() {
        final QuestionTimeConfiguration config = new QuestionTimeConfiguration();
        config.setMinConnected(2);
        config.setCooldown(30);
        config.setMaxCooldown(30);
        config.setMinCooldown(10);
        config.setVersion(QuestionTimeConfiguration.DefaultValues.VERSION);

        final VerifyConfigurationValuesImpl verificator = new VerifyConfigurationValuesImpl();
        final VerifyConfigurationValues.Result verify = verificator.verify(config);

        assertTrue(verify.isSuccess());
        assertTrue(verify.getWrongValues().isEmpty());
    }


}