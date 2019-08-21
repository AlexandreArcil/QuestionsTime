package fr.canardnocturne.questionstime.question.component;

import org.spongepowered.api.item.inventory.ItemStack;

import java.util.Arrays;

public class Prize {

    private final int money;
    private final boolean announce;
    private final ItemStack[] items;

    public Prize(final int money, final boolean announce, final ItemStack[] is) {
        this.money = Math.max(money, 0);
        this.announce = announce;
        this.items = is != null ? is : new ItemStack[0];
    }

    public int getMoney() {
        return money;
    }

    public ItemStack[] getItemStacks() {
        return this.items;
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
                '}';
    }
}
