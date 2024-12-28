package fr.canardnocturne.questionstime.util;

import fr.canardnocturne.questionstime.question.serializer.ItemStackSerializer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.Arrays;

@Disabled("Game null")
public class ItemStackSerializerTest {

    @Test
    public void serializeItemStackToString() {
        ItemStack is = ItemStack.builder().itemType(ItemTypes.SAND)
                .quantity(5)
                .add(Keys.DISPLAY_NAME, Component.text("test", NamedTextColor.YELLOW, TextDecoration.BOLD))
                .add(Keys.LORE, Arrays.asList(Component.text("line1", NamedTextColor.RED, TextDecoration.ITALIC),
                        Component.text("line2", NamedTextColor.BLUE, TextDecoration.UNDERLINED)))
                .build();
        String isStr = ItemStackSerializer.fromItemStack(is);
        Assertions.assertEquals("minecraft:sand;5;§e§ltest;§c§oline1\\n§9§nline2", isStr);
    }

    @Test
    public void serializeStringToItemStack() {
        String isStr = "minecraft:sand;5;§e§ltest;§c§oline1\\n§9§nline2";
        ItemStack is = ItemStackSerializer.fromString(isStr);
        Assertions.assertEquals(ItemStack.builder().itemType(ItemTypes.SAND)
                .quantity(5)
                .add(Keys.DISPLAY_NAME, Component.text("test", NamedTextColor.YELLOW, TextDecoration.BOLD))
                .add(Keys.LORE, Arrays.asList(Component.text("line1", NamedTextColor.RED, TextDecoration.ITALIC),
                        Component.text("line2", NamedTextColor.BLUE, TextDecoration.UNDERLINED)))
                .build(), is);
    }

}
