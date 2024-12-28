package fr.canardnocturne.questionstime.question.component;

import org.spongepowered.api.item.inventory.ItemStack;

import java.util.Arrays;

public class Prize {

    private final int money;
    private final boolean announce;
    private final ItemStack[] items;
    private final PrizeCommand[] commands;

    public Prize(final int money, final boolean announce, final ItemStack[] is, PrizeCommand[] commands) {
        this.money = Math.max(money, 0);
        this.announce = announce;
        this.items = is != null ? is : new ItemStack[0];
        this.commands = commands != null ? commands : new PrizeCommand[0];
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

    @Override
    public String toString() {
        return "Prize{" +
                "money=" + money +
                ", announce=" + announce +
                ", items=" + Arrays.toString(items) +
                ", commands=" + Arrays.toString(commands) +
                '}';
    }
}
