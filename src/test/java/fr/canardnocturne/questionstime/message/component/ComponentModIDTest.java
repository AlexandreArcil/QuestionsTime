package fr.canardnocturne.questionstime.message.component;

import fr.canardnocturne.questionstime.util.SpongeMock;
import net.kyori.adventure.text.Component;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.plugin.PluginManager;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.metadata.PluginMetadata;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ComponentModIDTest extends SpongeMock {

    @Test
    void modIdIsMinecraftNamespace() {
        final ItemStack is = Mockito.mock(ItemStack.class);
        final ItemType type = Mockito.mock(ItemType.class);
        Mockito.when(is.type()).thenReturn(type);
        final ResourceKey key = Mockito.mock(ResourceKey.class);
        Mockito.when(type.key(Mockito.any())).thenReturn(key);
        Mockito.when(key.namespace()).thenReturn(ResourceKey.MINECRAFT_NAMESPACE);

        final ComponentModID componentModID = new ComponentModID("modid");
        final Component modId = componentModID.process(is);

        assertEquals(Component.empty(), modId);
    }

    @Test
    void modIdIsCustomModWithName() {
        final String modId = "custommod";
        final String modName = "Custom Mod";

        final ItemStack is = Mockito.mock(ItemStack.class);
        final ItemType type = Mockito.mock(ItemType.class);
        Mockito.when(is.type()).thenReturn(type);
        final ResourceKey key = Mockito.mock(ResourceKey.class);
        Mockito.when(type.key(Mockito.any())).thenReturn(key);
        Mockito.when(key.namespace()).thenReturn(modId);

        final PluginManager pluginManager = Mockito.mock(PluginManager.class);
        final PluginContainer pluginContainer = Mockito.mock(PluginContainer.class);
        final PluginMetadata metadata = Mockito.mock(PluginMetadata.class);
        Mockito.when(pluginContainer.metadata()).thenReturn(metadata);
        Mockito.when(metadata.name()).thenReturn(Optional.of(modName));
        Mockito.when(pluginManager.plugin(modId)).thenReturn(Optional.of(pluginContainer));
        this.getSpongeMock().when(Sponge::pluginManager).thenReturn(pluginManager);

        final ComponentModID componentModID = new ComponentModID("modid");
        final Component formattedModName = componentModID.process(is);

        assertTrue(Component.EQUALS.test(Component.text(modName), formattedModName));
    }

    @Test
    void modIdIsCustomModWithoutName() {
        final String modId = "custommod";

        final ItemStack is = Mockito.mock(ItemStack.class);
        final ItemType type = Mockito.mock(ItemType.class);
        Mockito.when(is.type()).thenReturn(type);
        final ResourceKey key = Mockito.mock(ResourceKey.class);
        Mockito.when(type.key(Mockito.any())).thenReturn(key);
        Mockito.when(key.namespace()).thenReturn(modId);

        final PluginManager pluginManager = Mockito.mock(PluginManager.class);
        final PluginContainer pluginContainer = Mockito.mock(PluginContainer.class);
        final PluginMetadata metadata = Mockito.mock(PluginMetadata.class);
        Mockito.when(pluginContainer.metadata()).thenReturn(metadata);
        Mockito.when(metadata.name()).thenReturn(Optional.empty());
        Mockito.when(pluginManager.plugin(modId)).thenReturn(Optional.of(pluginContainer));
        this.getSpongeMock().when(Sponge::pluginManager).thenReturn(pluginManager);

        final ComponentModID componentModID = new ComponentModID("modid");
        final Component formattedModName = componentModID.process(is);

        assertTrue(Component.EQUALS.test(Component.text(modId), formattedModName));
    }

    @Test
    void modDoesNotExist() {
        final String modId = "nonexistentmod";

        final ItemStack is = Mockito.mock(ItemStack.class);
        final ItemType type = Mockito.mock(ItemType.class);
        Mockito.when(is.type()).thenReturn(type);
        final ResourceKey key = Mockito.mock(ResourceKey.class);
        Mockito.when(type.key(Mockito.any())).thenReturn(key);
        Mockito.when(key.namespace()).thenReturn(modId);

        final PluginManager pluginManager = Mockito.mock(PluginManager.class);
        Mockito.when(pluginManager.plugin(modId)).thenReturn(Optional.empty());
        this.getSpongeMock().when(Sponge::pluginManager).thenReturn(pluginManager);

        final ComponentModID componentModID = new ComponentModID("modid");

        assertThrows(IllegalArgumentException.class, () -> componentModID.process(is));
    }

}