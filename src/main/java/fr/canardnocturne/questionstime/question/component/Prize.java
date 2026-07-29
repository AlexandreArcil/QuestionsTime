package fr.canardnocturne.questionstime.question.component;

import org.spongepowered.api.data.Key;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.data.value.Value;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackComparators;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class Prize {

    private final int money;
    private final boolean announce;
    private final Set<ItemStack> items;
    private final Set<OutcomeCommand> commands;
    private final int position;

    public Prize(final int money, final boolean announce, final Set<ItemStack> is, final Set<OutcomeCommand> commands, final int position) {
        this.money = Math.max(money, 0);
        this.announce = announce;
        this.items = Collections.unmodifiableSet(is);
        this.commands = Collections.unmodifiableSet(commands);
        this.position = position;
    }

    public Prize(final Builder builder) {
        this.money = builder.money;
        this.announce = builder.announce;
        this.items = Collections.unmodifiableSet(builder.items);
        this.commands = Collections.unmodifiableSet(builder.commands);
        this.position = builder.position;
    }

    public Prize(final Prize prize) {
        this.money = prize.money;
        this.announce = prize.announce;
        this.items = prize.items.stream().map(ItemStack::copy).collect(Collectors.toSet());
        this.commands = prize.commands.stream().map(outcomeCommand -> new OutcomeCommand(outcomeCommand.message(), outcomeCommand.command())).collect(Collectors.toSet());
        this.position = prize.position;
    }

    public int getMoney() {
        return money;
    }

    public Set<ItemStack> getItemStacks() {
        return this.items;
    }

    public Set<OutcomeCommand> getCommands() {
        return commands;
    }

    public boolean isAnnounce() {
        return announce;
    }

    public int getPosition() {
        return position;
    }

    public boolean isEmpty() {
        return this.money <= 0 && this.items.isEmpty() && this.commands.isEmpty();
    }

    @Override
    public String toString() {
        return "Prize{" +
                "money=" + money +
                ", announce=" + announce +
                ", items=" + items +
                ", commands=" + commands +
                ", position=" + position +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        final Prize prize = (Prize) o;
        return position == prize.position;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(position);
    }

    public Builder toBuilder() {
        return new Builder(this);
    }

    public static Builder builder(final int position) {
        return new Builder(position);
    }

    public static class Builder {

        private int money;
        private boolean announce;
        private final Set<ItemStack> items;
        private final Set<OutcomeCommand> commands;
        private final int position;

        private Builder(final int position) {
            this.position = position;
            this.items = new HashSet<>();
            this.commands = new HashSet<>();
        }

        private Builder(final Prize prize) {
            this.money = prize.money;
            this.announce = prize.announce;
            this.position = prize.position;
            this.items = new HashSet<>();
            for (final ItemStack item : prize.items) {
                this.items.add(item.copy());
            }
            this.commands = new HashSet<>();
            for (final OutcomeCommand command : prize.commands) {
                this.commands.add(new OutcomeCommand(command.message(), command.command()));
            }
        }

        public int getMoney() {
            return money;
        }

        public Builder setMoney(final int money) {
            this.money = money;
            return this;
        }

        public boolean isAnnounce() {
            return announce;
        }

        public Builder setAnnounce(final boolean announce) {
            this.announce = announce;
            return this;
        }

        public int getPosition() {
            return position;
        }

        public Prize build() {
            return new Prize(this);
        }

        public Builder addCommand(final OutcomeCommand outcomeCommand) {
            this.commands.add(outcomeCommand);
            return this;
        }

        public Set<OutcomeCommand> getCommands() {
            return commands;
        }

        public Set<ItemStack> getItems() {
            return items;
        }

        public Builder setItems(final Set<ItemStack> items) {
            this.items.clear();
            this.items.addAll(items);
            return this;
        }

        public Builder addItem(final ItemStack is) {
            this.items.add(is);
            return this;
        }

        public boolean removeItem(final ItemStack is) {
            return this.items.removeIf(isp -> ItemStackComparators.TYPE_SIZE.get().compare(isp, is) == 0
            && valueEquals(is, isp, Keys.CUSTOM_NAME) && valueEquals(is, isp, Keys.LORE));
        }

        private <E, V extends Value<E>> boolean valueEquals(final ItemStack is, final ItemStack iss, final Key<V> value) {
            final Optional<V> valueIs = is.getValue(value);
            final Optional<V> valueIss = iss.getValue(value);
            if(valueIs.isEmpty() && valueIss.isEmpty()) {
                return true;
            }
            if (valueIs.isEmpty() ^ valueIss.isEmpty()) {
                return false;
            }
            return valueIs.get().get().equals(valueIss.get().get());
        }

        public boolean removeCommand(final OutcomeCommand outcomeCommand) {
            return this.commands.remove(outcomeCommand);
        }

        public boolean hasRewards() {
            return !this.items.isEmpty() || !this.commands.isEmpty() || this.money > 0;
        }

        public Builder setCommands(Set<OutcomeCommand> outcomeCommands) {
            this.commands.clear();
            this.commands.addAll(outcomeCommands);
            return this;
        }
    }
}
