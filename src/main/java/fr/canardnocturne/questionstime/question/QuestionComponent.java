package fr.canardnocturne.questionstime.question;

public enum QuestionComponent {

    QUESTION,
    WEIGHT,
    TIMER,
    TIMER_BETWEEN_ANSWER,
    ANSWERS("answer"),
    PRIZE_MONEY,
    PRIZE_ITEMS,
    PRIZE_COMMANDS,
    PRIZE_ANNOUNCE,
    MALUS_MONEY,
    MALUS_COMMANDS("malus command"),
    MALUS_ANNOUNCE,
    PROPOSITIONS("proposition"),
    REVEAL_ANSWER,
    TAGS("tag"),
    INCLUDE_PERMISSIONS("include permission"),
    EXCLUDE_PERMISSIONS("exclude permission");

    private final String singular;
    private final String plural;

    QuestionComponent() {
        this.singular = this.name().toLowerCase();
        this.plural = this.singular + "s";
    }

    QuestionComponent(final String singular) {
        this.singular = singular;
        this.plural = singular + "s";
    }

    public String getSingular() {
        return singular;
    }

    public String getPlural() {
        return plural;
    }
}
