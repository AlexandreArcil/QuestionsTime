package fr.canardnocturne.questionstime.command.change;

public enum QuestionComponent {

    QUESTION("question"),
    WEIGHT("weight"),
    TIMER("timer"),
    TIMER_BETWEEN_ANSWER("time_between_answer"),
    ANSWERS("answers"),
    PRIZE_MONEY("prize_money"),
    PRIZE_ITEMS("prize_items"),
    PRIZE_COMMANDS("prize_commands"),
    PRIZE_ANNOUNCE("prize_announce"),
    MALUS_MONEY("malus_money"),
    MALUS_COMMANDS("malus_commands"),
    MALUS_ANNOUNCE("malus_announce"),
    PROPOSITIONS("propositions");

    private final String name;

    QuestionComponent(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
