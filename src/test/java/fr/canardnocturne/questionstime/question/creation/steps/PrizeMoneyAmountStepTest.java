package fr.canardnocturne.questionstime.question.creation.steps;

import fr.canardnocturne.questionstime.question.creation.QuestionCreator;
import fr.canardnocturne.questionstime.util.MiniMessageTest;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.service.ServiceProvider;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.EconomyService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(MockitoExtension.class)
class PrizeMoneyAmountStepTest {
    
    @Mock
    private QuestionCreator questionCreator;

    @Test
    void questionDefined() {
        try(final MockedStatic<Sponge> spongeMock = Mockito.mockStatic(Sponge.class)) {
            final Server server = Mockito.mock(Server.class);
            final ServiceProvider.ServerScoped serviceProvider = Mockito.mock(ServiceProvider.ServerScoped.class);
            final EconomyService economyService = Mockito.mock(EconomyService.class);
            final Currency currency = Mockito.mock(Currency.class);
            spongeMock.when(Sponge::server).thenReturn(server);
            Mockito.when(server.serviceProvider()).thenReturn(serviceProvider);
            Mockito.when(serviceProvider.provide(EconomyService.class)).thenReturn(Optional.of(economyService));
            Mockito.when(economyService.defaultCurrency()).thenReturn(currency);
            Mockito.when(currency.pluralDisplayName()).thenReturn(Component.text("Coins"));

            assertNotNull(PrizeMoneyAmountStep.INSTANCE.question());
        }
    }

    @Test
    void addMoneyPrizeWithPosition() {
        try(final MockedStatic<Sponge> spongeMock = Mockito.mockStatic(Sponge.class)) {
            final Audience audience = Mockito.mock(Audience.class);
            final String answer = "50;2";
            final Server server = Mockito.mock(Server.class);
            final ServiceProvider.ServerScoped serviceProvider = Mockito.mock(ServiceProvider.ServerScoped.class);
            final EconomyService economyService = Mockito.mock(EconomyService.class);
            final Currency currency = Mockito.mock(Currency.class);
            spongeMock.when(Sponge::server).thenReturn(server);
            Mockito.when(server.serviceProvider()).thenReturn(serviceProvider);
            Mockito.when(serviceProvider.provide(EconomyService.class)).thenReturn(Optional.of(economyService));
            Mockito.when(economyService.defaultCurrency()).thenReturn(currency);
            Mockito.when(currency.pluralDisplayName()).thenReturn(Component.text("Coins"));

            final boolean finished = PrizeMoneyAmountStep.INSTANCE.handle(audience, answer, questionCreator);

            assertFalse(finished);
            Mockito.verify(audience).sendMessage(Mockito.argThat(component ->
                    MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).contains("The 2nd winner will gain 50 Coins")));
            Mockito.verify(questionCreator).setMoneyPrize(2, 50);
        }
    }

    @Test
    void addMoneyPrize() {
        try(final MockedStatic<Sponge> spongeMock = Mockito.mockStatic(Sponge.class)) {
            final Audience audience = Mockito.mock(Audience.class);
            final String answer = "50";
            final Server server = Mockito.mock(Server.class);
            final ServiceProvider.ServerScoped serviceProvider = Mockito.mock(ServiceProvider.ServerScoped.class);
            final EconomyService economyService = Mockito.mock(EconomyService.class);
            final Currency currency = Mockito.mock(Currency.class);
            spongeMock.when(Sponge::server).thenReturn(server);
            Mockito.when(server.serviceProvider()).thenReturn(serviceProvider);
            Mockito.when(serviceProvider.provide(EconomyService.class)).thenReturn(Optional.of(economyService));
            Mockito.when(economyService.defaultCurrency()).thenReturn(currency);
            Mockito.when(currency.pluralDisplayName()).thenReturn(Component.text("Coins"));

            final boolean finished = PrizeMoneyAmountStep.INSTANCE.handle(audience, answer, questionCreator);

            assertFalse(finished);
            Mockito.verify(audience).sendMessage(Mockito.argThat(component ->
                    MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).contains("The 1st winner will gain 50 Coins")));
            Mockito.verify(questionCreator).setMoneyPrize(1, 50);
        }
    }

    @Test
    void removeMoneyPrize() {
        try(final MockedStatic<Sponge> spongeMock = Mockito.mockStatic(Sponge.class)) {
            final Audience audience = Mockito.mock(Audience.class);
            final String answer = "0;3";
            final Server server = Mockito.mock(Server.class);
            final ServiceProvider.ServerScoped serviceProvider = Mockito.mock(ServiceProvider.ServerScoped.class);
            final EconomyService economyService = Mockito.mock(EconomyService.class);
            final Currency currency = Mockito.mock(Currency.class);
            spongeMock.when(Sponge::server).thenReturn(server);
            Mockito.when(server.serviceProvider()).thenReturn(serviceProvider);
            Mockito.when(serviceProvider.provide(EconomyService.class)).thenReturn(Optional.of(economyService));
            Mockito.when(economyService.defaultCurrency()).thenReturn(currency);
            Mockito.when(currency.pluralDisplayName()).thenReturn(Component.text("Coins"));

            final boolean finished = PrizeMoneyAmountStep.INSTANCE.handle(audience, answer, questionCreator);

            assertFalse(finished);
            Mockito.verify(audience).sendMessage(Mockito.argThat(component ->
                    MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).contains("3rd winner will not gain Coins")));
            Mockito.verify(questionCreator).setMoneyPrize(3, 0);
        }
    }

    @Test
    void positionNegative() {
        final Audience audience = Mockito.mock(Audience.class);
        final String answer = "100;-1";

        final boolean finished = PrizeMoneyAmountStep.INSTANCE.handle(audience, answer, questionCreator);

        assertFalse(finished);
        Mockito.verify(audience).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).contains("-1 is not a positive number")));
        Mockito.verifyNoInteractions(questionCreator);
    }

    @Test
    void positionNotANumber() {
        final Audience audience = Mockito.mock(Audience.class);
        final String answer = "100;abc";

        final boolean finished = PrizeMoneyAmountStep.INSTANCE.handle(audience, answer, questionCreator);

        assertFalse(finished);
        Mockito.verify(audience).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).contains("abc is not a number")));
        Mockito.verifyNoInteractions(questionCreator);
    }

    @Test
    void amountNegative() {
        final Audience audience = Mockito.mock(Audience.class);
        final String answer = "-50;1";

        final boolean finished = PrizeMoneyAmountStep.INSTANCE.handle(audience, answer, questionCreator);

        assertFalse(finished);
        Mockito.verify(audience).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).contains("-50 is not a positive amount")));
        Mockito.verifyNoInteractions(questionCreator);
    }

    @Test
    void amountNotANumber() {
        final Audience audience = Mockito.mock(Audience.class);
        final String answer = "abc;1";

        final boolean finished = PrizeMoneyAmountStep.INSTANCE.handle(audience, answer, questionCreator);

        assertFalse(finished);
        Mockito.verify(audience).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).contains("abc;1 is not a number")));
        Mockito.verifyNoInteractions(questionCreator);
    }

    @Test
    void tooManyArguments() {
        final Audience audience = Mockito.mock(Audience.class);
        final String answer = "100;2;extra";

        final boolean finished = PrizeMoneyAmountStep.INSTANCE.handle(audience, answer, questionCreator);

        assertFalse(finished);
        Mockito.verify(audience).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).contains("The amount prize 100;2;extra doesn't follow the syntax [amount];{position}")));
        Mockito.verifyNoInteractions(questionCreator);
    }

    @Test
    void confirmAnswer() {
        final Audience audience = Mockito.mock(Audience.class);
        final String answer = "confirm";

        final boolean finished = PrizeMoneyAmountStep.INSTANCE.handle(audience, answer, questionCreator);

        assertTrue(finished);
        Mockito.verifyNoInteractions(audience);
        Mockito.verifyNoInteractions(questionCreator);
    }

    @Test
    void shouldSkipWhenNoEconomyService() {
        try(final MockedStatic<Sponge> spongeMock = Mockito.mockStatic(Sponge.class)) {
            final Server server = Mockito.mock(Server.class);
            final ServiceProvider.ServerScoped serviceProvider = Mockito.mock(ServiceProvider.ServerScoped.class);
            spongeMock.when(Sponge::server).thenReturn(server);
            Mockito.when(server.serviceProvider()).thenReturn(serviceProvider);
            Mockito.when(serviceProvider.provide(EconomyService.class)).thenReturn(Optional.empty());

            final boolean shouldSkip = PrizeMoneyAmountStep.INSTANCE.shouldSkip(questionCreator);

            assertTrue(shouldSkip);
        }
    }

    @Test
    void shouldNotSkipWhenEconomyServicePresent() {
        try(final MockedStatic<Sponge> spongeMock = Mockito.mockStatic(Sponge.class)) {
            final Server server = Mockito.mock(Server.class);
            final ServiceProvider.ServerScoped serviceProvider = Mockito.mock(ServiceProvider.ServerScoped.class);
            final EconomyService economyService = Mockito.mock(EconomyService.class);
            spongeMock.when(Sponge::server).thenReturn(server);
            Mockito.when(server.serviceProvider()).thenReturn(serviceProvider);
            Mockito.when(serviceProvider.provide(EconomyService.class)).thenReturn(Optional.of(economyService));

            final boolean shouldSkip = PrizeMoneyAmountStep.INSTANCE.shouldSkip(questionCreator);

            assertFalse(shouldSkip);
        }
    }

    @Test
    void nextStepDefined() {
        final Step nextStep = PrizeMoneyAmountStep.INSTANCE.next(questionCreator);
        assertNotNull(nextStep);
    }

}