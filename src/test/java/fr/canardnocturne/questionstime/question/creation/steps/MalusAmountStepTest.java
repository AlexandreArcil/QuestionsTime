package fr.canardnocturne.questionstime.question.creation.steps;

import fr.canardnocturne.questionstime.question.creation.QuestionCreator;
import fr.canardnocturne.questionstime.util.MiniMessageTest;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.junit.jupiter.api.BeforeEach;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class MalusAmountStepTest {

    @Mock
    Audience sender;

    QuestionCreator questionCreator;

    @BeforeEach
    void setUp() {
        questionCreator = new QuestionCreator();
        questionCreator.setQuestion("question");
        questionCreator.getAnswers().add("answer");
    }

    @Test
    void questionNotNull() {
        try (final MockedStatic<Sponge> spongeMock = Mockito.mockStatic(Sponge.class)) {
            final Server serverMock = Mockito.mock(Server.class);
            final EconomyService economyServiceMock = Mockito.mock(EconomyService.class);
            final Currency currencyMock = Mockito.mock(Currency.class);
            Mockito.when(currencyMock.pluralDisplayName()).thenReturn(Component.text("Coins"));
            Mockito.when(economyServiceMock.defaultCurrency()).thenReturn(currencyMock);
            final ServiceProvider.ServerScoped serviceProviderMock = Mockito.mock(ServiceProvider.ServerScoped.class);
            Mockito.when(serviceProviderMock.provide(EconomyService.class)).thenReturn(Optional.of(economyServiceMock));
            Mockito.when(serverMock.serviceProvider()).thenReturn(serviceProviderMock);
            spongeMock.when(Sponge::server).thenReturn(serverMock);

            assertNotNull(MalusAmountStep.INSTANCE.question());
        }
    }

    @Test
    void handleValidMalusAmount() {
        final String answer = "100";

        final boolean stepCompleted = MalusAmountStep.INSTANCE.handle(sender, answer, questionCreator);

        assertFalse(stepCompleted);
        assertEquals(100, questionCreator.getMoneyMalus());
        Mockito.verify(sender).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).contains("Is 100 correct ? If yes, answer with [/qtc yes] or just re-answer to change the value")));
    }

    @Test
    void handleZeroMalusAmount() {
        questionCreator.setMoneyMalus(50);
        final String answer = "0";

        final boolean stepCompleted = MalusAmountStep.INSTANCE.handle(sender, answer, questionCreator);

        assertFalse(stepCompleted);
        assertEquals(0, questionCreator.getMoneyMalus());
        Mockito.verify(sender).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).contains("You really don't want to add money as a malus ? If yes, answer with [/qtc yes] or just re-answer to change the value")));
    }

    @Test
    void handleNegativeMalusAmount() {
        questionCreator.setMoneyMalus(50);
        final String answer = "-50";

        final boolean stepCompleted = MalusAmountStep.INSTANCE.handle(sender, answer, questionCreator);

        assertFalse(stepCompleted);
        assertEquals(50, questionCreator.getMoneyMalus());
        Mockito.verify(sender).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).contains("-50 is not a positive amount")));
    }

    @Test
    void handleInvalidNumber() {
        questionCreator.setMoneyMalus(50);
        final String answer = "invalid";

        final boolean stepCompleted = MalusAmountStep.INSTANCE.handle(sender, answer, questionCreator);

        assertFalse(stepCompleted);
        assertEquals(50, questionCreator.getMoneyMalus());
        Mockito.verify(sender).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).contains("invalid is not a number")));
    }

    @Test
    void handleYesAnswer() {
        questionCreator.setMoneyMalus(50);
        final String answer = "yes";

        final boolean stepCompleted = MalusAmountStep.INSTANCE.handle(sender, answer, questionCreator);

        assertTrue(stepCompleted);
    }

    @Test
    void shouldSkipWhenEconomyServiceNotPresent() {
        try (final MockedStatic<Sponge> spongeMock = Mockito.mockStatic(Sponge.class)) {
            final Server serverMock = Mockito.mock(Server.class);
            final ServiceProvider.ServerScoped serviceProviderMock = Mockito.mock(ServiceProvider.ServerScoped.class);
            Mockito.when(serviceProviderMock.provide(EconomyService.class)).thenReturn(Optional.empty());
            Mockito.when(serverMock.serviceProvider()).thenReturn(serviceProviderMock);
            spongeMock.when(Sponge::server).thenReturn(serverMock);

            final boolean shouldSkip = MalusAmountStep.INSTANCE.shouldSkip(questionCreator);

            assertTrue(shouldSkip);
        }
    }

    @Test
    void shouldNotSkipWhenEconomyServicePresent() {
        try (final MockedStatic<Sponge> spongeMock = Mockito.mockStatic(Sponge.class)) {
            final Server serverMock = Mockito.mock(Server.class);
            final EconomyService economyServiceMock = Mockito.mock(EconomyService.class);
            final ServiceProvider.ServerScoped serviceProviderMock = Mockito.mock(ServiceProvider.ServerScoped.class);
            Mockito.when(serviceProviderMock.provide(EconomyService.class)).thenReturn(Optional.of(economyServiceMock));
            Mockito.when(serverMock.serviceProvider()).thenReturn(serviceProviderMock);
            spongeMock.when(Sponge::server).thenReturn(serverMock);

            final boolean shouldSkip = MalusAmountStep.INSTANCE.shouldSkip(questionCreator);

            assertFalse(shouldSkip);
        }
    }

    @Test
    void nextStepDefined() {
        assertNotNull(MalusAmountStep.INSTANCE.next(questionCreator));
    }

}