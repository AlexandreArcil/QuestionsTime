package fr.canardnocturne.questionstime.config.verificator;

import fr.canardnocturne.questionstime.config.QuestionTimeConfiguration;
import org.apache.logging.log4j.Logger;

public class SetDefaultWrongConfigurationValues implements VerifyConfigurationValues {

    private final Logger pluginLogger;

    public SetDefaultWrongConfigurationValues(final Logger pluginLogger) {
        this.pluginLogger = pluginLogger;
    }

    @Override
    public void verify(final QuestionTimeConfiguration config) {
        if (config.getMinConnected() <= 0) {
            this.logWrongConfigValue("min_connected", config.getMinConnected(), "which is less than or equal to 0", QuestionTimeConfiguration.DefaultValues.MIN_CONNECTED);
            config.setMinConnected(QuestionTimeConfiguration.DefaultValues.MIN_CONNECTED);
        }
        if (config.getCooldown() <= 0) {
            this.logWrongConfigValue("cooldown", config.getCooldown(), "which is less than or equal to 0", QuestionTimeConfiguration.DefaultValues.COOLDOWN);
            config.setCooldown(QuestionTimeConfiguration.DefaultValues.COOLDOWN);
        }
        if (config.getMinCooldown() <= 0) {
            this.logWrongConfigValue("min_cooldown", config.getMinCooldown(), "which is less than or equal to 0", QuestionTimeConfiguration.DefaultValues.MIN_COOLDOWN);
            config.setMinCooldown(QuestionTimeConfiguration.DefaultValues.MIN_COOLDOWN);
        }
        if (config.getMaxCooldown() <= 0) {
            this.logWrongConfigValue("max_cooldown", config.getMaxCooldown(), "which is less than or equal to 0", QuestionTimeConfiguration.DefaultValues.MAX_COOLDOWN);
            config.setMaxCooldown(QuestionTimeConfiguration.DefaultValues.MAX_COOLDOWN);
        }

        if (config.getMinCooldown() > config.getMaxCooldown()) {
            this.pluginLogger.warn("The config 'min_cooldown' value is superior to 'max_cooldown'. The default values {} and {} will be used", QuestionTimeConfiguration.DefaultValues.MIN_COOLDOWN, QuestionTimeConfiguration.DefaultValues.MAX_COOLDOWN);
            config.setMinCooldown(QuestionTimeConfiguration.DefaultValues.MIN_COOLDOWN);
            config.setMaxCooldown(QuestionTimeConfiguration.DefaultValues.MAX_COOLDOWN);
        }

        if(config.getVersion() > QuestionTimeConfiguration.DefaultValues.VERSION) {
            this.pluginLogger.warn("The configuration version is higher than the current version, so it will be updated to version {}. You should not modify this value !", QuestionTimeConfiguration.DefaultValues.VERSION);
            config.setVersion(QuestionTimeConfiguration.DefaultValues.VERSION);
        }
    }

    private void logWrongConfigValue(final String configName, final int valueInConfig, final String wrongReason, final int defaultValue) {
        this.pluginLogger.warn("The config '{}' value is {}, {}. The default value {} will be used instead", configName, valueInConfig, wrongReason, defaultValue);
    }

}
