package fr.canardnocturne.questionstime.config;

public enum Config {

    COOLDOWN("cooldown"),
    MODE("mode"),
    MIN_COOLDOWN("minimum_cooldown"),
    MAX_COOLDOWN("maximum_cooldown"),
    PERSONAL_ANSWER("personal_answer"),
    MINIMUM_CONNECTED("minimum_connected");

    private final String name;

    Config(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
