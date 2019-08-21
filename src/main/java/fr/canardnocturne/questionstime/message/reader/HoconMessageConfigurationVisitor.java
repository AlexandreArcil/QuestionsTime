package fr.canardnocturne.questionstime.message.reader;

import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.ConfigurationVisitor;

import java.util.HashMap;
import java.util.Map;

public abstract class HoconMessageConfigurationVisitor implements ConfigurationVisitor.Safe<Map<String, String>, Map<String, String>> {
    @Override
    public Map<String, String> newState() {
        return new HashMap<>();
    }

    @Override
    public void beginVisit(final ConfigurationNode node, final Map<String, String> state) {
    }

    @Override
    public void enterNode(final ConfigurationNode node, final Map<String, String> state) {
    }

    @Override
    public void enterMappingNode(final ConfigurationNode node, final Map<String, String> state) {
    }

    @Override
    public void enterListNode(final ConfigurationNode node, final Map<String, String> state) {
    }

    @Override
    public void exitMappingNode(final ConfigurationNode node, final Map<String, String> state) {
    }

    @Override
    public void exitListNode(final ConfigurationNode node, final Map<String, String> state) {
    }

    @Override
    public Map<String, String> endVisit(final Map<String, String> state) {
        return state;
    }
}
