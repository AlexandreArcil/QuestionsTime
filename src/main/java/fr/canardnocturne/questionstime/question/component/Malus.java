package fr.canardnocturne.questionstime.question.component;

public class Malus {

    private final boolean announce;
    private final int money;

    public Malus(final int money, final boolean announce) {
        this.money = Math.max(money, 0);
        this.announce = announce;
    }

    public int getMoney() {
        return money;
    }

    public boolean isAnnounce() {
        return announce;
    }

    @Override
    public String toString() {
        return "Malus{" +
                "announce=" + announce +
                ", money=" + money +
                '}';
    }
}
