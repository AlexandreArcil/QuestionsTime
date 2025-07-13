package fr.canardnocturne.questionstime.question.component;

import org.spongepowered.api.data.Key;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.data.value.Value;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackComparators;

import java.util.*;

public class Prize {

    private final int money;
    private final boolean announce;
    private final ItemStack[] items;
    private final PrizeCommand[] commands;
    private final int position;

    public Prize(final int money, final boolean announce, final ItemStack[] is, final PrizeCommand[] commands, final int position) {
        this.money = Math.max(money, 0);
        this.announce = announce;
        this.items = is != null ? is : new ItemStack[0];
        this.commands = commands != null ? commands : new PrizeCommand[0];
        this.position = position;
    }

    public Prize(final Builder builder) {
        this.money = builder.money;
        this.announce = builder.announce;
        this.items = builder.items.toArray(new ItemStack[0]);
        this.commands = builder.commands.toArray(new PrizeCommand[0]);
        this.position = builder.position;
    }

    public int getMoney() {
        return money;
    }

    public ItemStack[] getItemStacks() {
        return this.items;
    }

    public PrizeCommand[] getCommands() {
        return commands;
    }

    public boolean isAnnounce() {
        return announce;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "Prize{" +
                "money=" + money +
                ", announce=" + announce +
                ", items=" + Arrays.toString(items) +
                ", commands=" + Arrays.toString(commands) +
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

    public static Builder builder(final int position) {
        return new Builder(position);
    }

    public static class Builder {

        private int money;
        private boolean announce;
        private final List<ItemStack> items;
        private final List<PrizeCommand> commands;
        private final int position;

        private Builder(final int position) {
            this.position = position;
            this.items = new ArrayList<>();
            this.commands = new ArrayList<>();
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

        public void addCommand(final PrizeCommand prizeCommand) {
            this.commands.add(prizeCommand);
        }

        public List<PrizeCommand> getCommands() {
            return commands;
        }

        public List<ItemStack> getItems() {
            return items;
        }

        public void addItem(final ItemStack is) {
            this.items.add(is);
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

        public boolean removeCommand(final PrizeCommand prizeCommand) {
            return this.commands.remove(prizeCommand);
        }

        public boolean hasRewards() {
            return !this.items.isEmpty() || !this.commands.isEmpty() || this.money > 0;
        }
    }
}
