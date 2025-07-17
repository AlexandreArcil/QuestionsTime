package fr.canardnocturne.questionstime.question.creation.steps;

import fr.canardnocturne.questionstime.question.creation.QuestionCreator;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.service.ServiceProvider;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.EconomyService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MalusAmountStepTest {

    @Mock
    Audience sender;

    @Captor
    ArgumentCaptor<Component> message;

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
        assertFalse(MalusAmountStep.INSTANCE.handle(sender, answer, questionCreator));

        assertEquals(100, questionCreator.getMoneyMalus());
        Mockito.verify(sender).sendMessage(message.capture());
        assertNotNull(message.getValue());
    }

    @Test
    void handleZeroMalusAmount() {
        questionCreator.setMoneyMalus(50);
        final String answer = "0";
        assertFalse(MalusAmountStep.INSTANCE.handle(sender, answer, questionCreator));

        assertEquals(0, questionCreator.getMoneyMalus());
        Mockito.verify(sender).sendMessage(message.capture());
        assertNotNull(message.getValue());
    }

    @Test
    void handleNegativeMalusAmount() {
        questionCreator.setMoneyMalus(50);
        final String answer = "-50";
        assertFalse(MalusAmountStep.INSTANCE.handle(sender, answer, questionCreator));

        assertEquals(50, questionCreator.getMoneyMalus());
        Mockito.verify(sender).sendMessage(message.capture());
        assertNotNull(message.getValue());
    }

    @Test
    void handleInvalidNumber() {
        questionCreator.setMoneyMalus(50);
        final String answer = "invalid";
        assertFalse(MalusAmountStep.INSTANCE.handle(sender, answer, questionCreator));

        assertEquals(50, questionCreator.getMoneyMalus());
        Mockito.verify(sender).sendMessage(message.capture());
        assertNotNull(message.getValue());
    }

    @Test
    void handleYesAnswer() {
        questionCreator.setMoneyMalus(50);
        final String answer = "yes";
        assertTrue(MalusAmountStep.INSTANCE.handle(sender, answer, questionCreator));
    }

    @Test
    void shouldSkipWhenEconomyServiceNotPresent() {
        try (final MockedStatic<Sponge> spongeMock = Mockito.mockStatic(Sponge.class)) {
            final Server serverMock = Mockito.mock(Server.class);
            final ServiceProvider.ServerScoped serviceProviderMock = Mockito.mock(ServiceProvider.ServerScoped.class);
            Mockito.when(serviceProviderMock.provide(EconomyService.class)).thenReturn(Optional.empty());
            Mockito.when(serverMock.serviceProvider()).thenReturn(serviceProviderMock);
            spongeMock.when(Sponge::server).thenReturn(serverMock);

            assertTrue(MalusAmountStep.INSTANCE.shouldSkip(questionCreator));
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

            assertFalse(MalusAmountStep.INSTANCE.shouldSkip(questionCreator));
        }
    }

    @Test
    void nextStepDefined() {
        assertNotNull(MalusAmountStep.INSTANCE.next(questionCreator));
    }

}