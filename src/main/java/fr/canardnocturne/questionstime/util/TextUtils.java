package fr.canardnocturne.questionstime.util;

import fr.canardnocturne.questionstime.QuestionsTime;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.registry.RegistryTypes;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public final class TextUtils {

    private TextUtils() {
    }

    public static Component normal(final String message) {
        return Component.text(message, NamedTextColor.GREEN);
    }

    public static Component normalWithPrefix(final String message) {
        return QuestionsTime.PREFIX.appendSpace().append(normal(message));
    }

    public static Component special(final String message) {
        return Component.text(message, NamedTextColor.BLUE);
    }

    public static Component specialWithPrefix(final String message) {
        return QuestionsTime.PREFIX.appendSpace().append(special(message));
    }

    public static Component composed(final String... texts) {
        return QuestionsTime.PREFIX.appendSpace().append(composedWithoutPrefix(texts));
    }

    public static Component composedWithoutPrefix(final String... texts) {
        final TextComponent.Builder finalText = Component.empty().toBuilder();
        for (int i = 0; i < texts.length; i++) {
            final String text = texts[i];
            if (i % 2 == 0) {
                finalText.append(normal(text));
            } else {
                finalText.append(special(text));
            }
        }
        return finalText.build();
    }

    public static Component commandShortcut(final String commandArg) {
        return Component.text("[/qtc " + commandArg + "]", NamedTextColor.GREEN, TextDecoration.BOLD)
                .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/qtc " + commandArg));
    }

    public static Component example(final String content) {
        return QuestionsTime.PREFIX.appendSpace()
                .append(Component.text("Example: ", NamedTextColor.DARK_GRAY))
                .append(Component.text(content, NamedTextColor.GRAY, TextDecoration.ITALIC));
    }

    public static void sendTextToEveryone(final Component text, final List<ServerPlayer> players) {
        players.forEach(player -> player.sendMessage(QuestionsTime.PREFIX.appendSpace().append(text)));
    }

    public static Component displayItem(final ItemStack is) {
        final Component type = is.type().asComponent();
        final Optional<Component> name = is.get(Keys.CUSTOM_NAME);
        final List<Component> lore = is.get(Keys.LORE).orElse(Collections.emptyList());
        final String namespace = is.type().key(RegistryTypes.ITEM_TYPE).namespace();
        if (name.isPresent() || !lore.isEmpty() || !namespace.equals(ResourceKey.MINECRAFT_NAMESPACE)) {
            final TextComponent.Builder hoverText = Component.text()
                    .append(name.orElse(type));
            lore.forEach(line -> hoverText.appendNewline().append(line.color(NamedTextColor.GRAY)));
            Sponge.pluginManager().plugin(namespace).ifPresent(pluginContainer -> {
                if (!namespace.equals(ResourceKey.MINECRAFT_NAMESPACE)) {
                    hoverText.appendNewline().append(Component.text(pluginContainer.metadata().name().orElse(pluginContainer.metadata().id()), NamedTextColor.DARK_GRAY));
                }
            });
            return type.decorate(TextDecoration.UNDERLINED)
                    .hoverEvent(HoverEvent.showText(hoverText));
        } else {
            return type;
        }
    }

    public static Component errorWithPrefix(final String message) {
        return QuestionsTime.PREFIX.append(Component.text(message, NamedTextColor.RED));
    }

}
