package fr.canardnocturne.questionstime.config;

public enum ConfigField {

    VERSION("version"),
    COOLDOWN("cooldown"),
    MODE("mode"),
    MIN_COOLDOWN("min-cooldown"),
    MAX_COOLDOWN("max-cooldown"),
    PERSONAL_ANSWER("personal-answer"),
    MINIMUM_CONNECTED("min-connected");

    private final String name;

    ConfigField(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
