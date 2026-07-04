package fr.canardnocturne.questionstime.config.verificator;

import fr.canardnocturne.questionstime.config.ConfigField;
import fr.canardnocturne.questionstime.config.QuestionTimeConfiguration;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public interface VerifyConfigurationValues {

    Result verify(final QuestionTimeConfiguration questionTimeConfiguration);

    class Result {

        private final boolean success;
        private final Map<ConfigField, List<String>> wrongValues;

        private Result(final boolean success, final Map<ConfigField, List<String>> wrongValues) {
            this.success = success;
            this.wrongValues = wrongValues;
        }

        public static Result success() {
            return new Result(true, Collections.emptyMap());
        }

        public static Result failure(final Map<ConfigField, List<String>> wrongValues) {
            return new Result(false, wrongValues);
        }

        public boolean isSuccess() {
            return success;
        }

        public Map<ConfigField, List<String>> getWrongValues() {
            return wrongValues;
        }
    }

}
