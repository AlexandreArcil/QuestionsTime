package fr.canardnocturne.questionstime.message.component.set;

import fr.canardnocturne.questionstime.message.component.MessageComponents;
import fr.canardnocturne.questionstime.message.format.MessageFormat;
import org.spongepowered.api.service.economy.EconomyService;

public interface SetCurrency<T extends MessageFormat.Format> extends SetComponent {

    default T setCurrency(final EconomyService economyService) {
        setComponent(MessageComponents.CURRENCY, economyService);
        return (T) this;
    }

}
