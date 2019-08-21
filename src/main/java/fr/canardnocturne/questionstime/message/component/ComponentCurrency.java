package fr.canardnocturne.questionstime.message.component;

import net.kyori.adventure.text.Component;
import org.spongepowered.api.service.economy.EconomyService;

public class ComponentCurrency extends MessageComponent<EconomyService> {

    public ComponentCurrency(final String name) {
        super(name);
    }

    @Override
    public Component process(final EconomyService economyService) {
        return economyService.defaultCurrency().displayName();
    }
}
