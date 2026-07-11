package fr.canardnocturne.questionstime.config.verificator;

import fr.canardnocturne.questionstime.config.ConfigField;
import fr.canardnocturne.questionstime.config.QuestionTimeConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VerifyConfigurationValuesImpl implements VerifyConfigurationValues {

    @Override
    public Result verify(final QuestionTimeConfiguration config) {
        final Map<ConfigField, List<String>> wrongConfigurationValues = new HashMap<>();

        if (config.getMinConnected() <= 0) {
            wrongConfigurationValues.computeIfAbsent(ConfigField.MINIMUM_CONNECTED, key -> new ArrayList<>()).add("less than or equal to 0");
        }
        
        if(config.getCooldown() <= 0) {
            wrongConfigurationValues.computeIfAbsent(ConfigField.COOLDOWN, key -> new ArrayList<>()).add("less than or equal to 0");
        }
        
        if (config.getMinCooldown() <= 0) {
            wrongConfigurationValues.computeIfAbsent(ConfigField.MIN_COOLDOWN, key -> new ArrayList<>()).add("less than or equal to 0");
        }

        if (config.getMaxCooldown() <= 0) {
            wrongConfigurationValues.computeIfAbsent(ConfigField.MAX_COOLDOWN, key -> new ArrayList<>()).add("less than or equal to 0");
        }

        if (config.getMinCooldown() > config.getMaxCooldown()) {
            wrongConfigurationValues.computeIfAbsent(ConfigField.MIN_COOLDOWN, key -> new ArrayList<>()).add("greater than the 'max_cooldown' value");
        }

        if(config.getVersion() > QuestionTimeConfiguration.DefaultValues.VERSION) {
            wrongConfigurationValues.computeIfAbsent(ConfigField.VERSION, key -> new ArrayList<>()).add("higher than the current version");
        }

        if (wrongConfigurationValues.isEmpty()) {
            return Result.success();
        } else {
            return Result.failure(wrongConfigurationValues);
        }
    }

}
