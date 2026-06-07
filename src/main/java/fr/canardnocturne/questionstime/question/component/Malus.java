package fr.canardnocturne.questionstime.question.component;

import org.apache.commons.lang3.ArrayUtils;

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

    public Malus(final Malus malus)  {
        this.announce = malus.announce;
        this.money = malus.money;
        this.commands = new OutcomeCommand[malus.commands.length];
        for (int i = 0; i < malus.commands.length; i++) {
            final OutcomeCommand command = malus.commands[i];
            this.commands[i] = new OutcomeCommand(command.message(), command.command());
        }
    }

    public Malus(final Builder builder) {
        this.announce = builder.announce;
        this.money = builder.money;
        this.commands = builder.commands != null ? builder.commands : new OutcomeCommand[0];
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

    public Builder toBuilder() {
        return new Builder(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private boolean announce;
        private int money;
        private OutcomeCommand[] commands;

        private Builder() {}

        private Builder(final Malus malus) {
            this.announce = malus.announce;
            this.money = malus.money;
            this.commands = malus.commands;
        }

        public Builder setAnnounce(final boolean announce) {
            this.announce = announce;
            return this;
        }

        public Builder setMoney(final int money) {
            this.money = money;
            return this;
        }

        public Builder setCommands(final OutcomeCommand[] commands) {
            this.commands = commands;
            return this;
        }

        public Builder addCommand(final OutcomeCommand command) {
            this.commands = ArrayUtils.add(this.commands, command);
            return this;
        }

        public OutcomeCommand[] getCommands() {
            return commands;
        }

        public Malus build() {
            return new Malus(this);
        }
    }
}
