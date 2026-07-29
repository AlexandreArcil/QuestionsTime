package fr.canardnocturne.questionstime.question.component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Malus {

    private final boolean announce;
    private final int money;
    private final Set<OutcomeCommand> commands;

    public Malus(final int money, final boolean announce, final Set<OutcomeCommand> commands) {
        this.money = Math.max(money, 0);
        this.announce = announce;
        this.commands = commands;
    }

    public Malus(final Malus malus)  {
        this.announce = malus.announce;
        this.money = malus.money;
        this.commands = Collections.unmodifiableSet(malus.commands);
    }

    public Malus(final Builder builder) {
        this.announce = builder.announce;
        this.money = builder.money;
        this.commands = Collections.unmodifiableSet(builder.commands);
    }

    public int getMoney() {
        return money;
    }

    public boolean isAnnounce() {
        return announce;
    }

    public Set<OutcomeCommand> getCommands() {
        return this.commands;
    }

    public boolean isEmpty() {
        return this.commands.isEmpty() && this.money == 0;
    }

    @Override
    public String toString() {
        return "Malus{" +
                "announce=" + announce +
                ", money=" + money +
                ", commands=" + this.commands +
                '}';
    }

    public Builder toBuilder() {
        return new Builder(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private boolean announce;
        private int money;
        private Set<OutcomeCommand> commands;

        private Builder() {
            this.commands = new HashSet<>();
        }

        private Builder(final Malus malus) {
            this.announce = malus.announce;
            this.money = malus.money;
            this.commands = new HashSet<>(malus.commands);
        }

        public Builder setAnnounce(final boolean announce) {
            this.announce = announce;
            return this;
        }

        public Builder setMoney(final int money) {
            this.money = money;
            return this;
        }

        public Builder setCommands(final Set<OutcomeCommand> commands) {
            this.commands = commands;
            return this;
        }

        public Builder addCommand(final OutcomeCommand command) {
            this.commands.add(command);
            return this;
        }

        public Set<OutcomeCommand> getCommands() {
            return commands;
        }

        public Malus build() {
            return new Malus(this);
        }
    }
}
