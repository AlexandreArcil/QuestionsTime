package fr.canardnocturne.questionstime.question.component;

import java.util.Arrays;

public class Malus {

    private final boolean announce;
    private final int money;
    private final OutcomeCommand[] commands;

    public Malus(final int money, final boolean announce, final OutcomeCommand[] commands) {
        this.money = Math.max(money, 0);
        this.announce = announce;
        this.commands = commands;
    }

    public int getMoney() {
        return money;
    }

    public boolean isAnnounce() {
        return announce;
    }

    public OutcomeCommand[] getCommands() {
        return this.commands;
    }

    @Override
    public String toString() {
        return "Malus{" +
                "announce=" + announce +
                ", money=" + money +
                ", commands=" + Arrays.toString(commands) +
                '}';
    }
}
