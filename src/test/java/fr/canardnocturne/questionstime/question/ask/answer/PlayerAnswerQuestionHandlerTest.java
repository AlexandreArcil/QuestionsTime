package fr.canardnocturne.questionstime.question.ask.answer;

import fr.canardnocturne.questionstime.message.Messages;
import fr.canardnocturne.questionstime.question.component.Malus;
import fr.canardnocturne.questionstime.question.component.OutcomeCommand;
import fr.canardnocturne.questionstime.question.component.Prize;
import fr.canardnocturne.questionstime.question.type.Question;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.spongepowered.api.*;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.manager.CommandManager;
import org.spongepowered.api.data.Key;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.CauseStackManager;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.entity.PlayerInventory;
import org.spongepowered.api.registry.RegistryType;
import org.spongepowered.api.scheduler.ScheduledTask;
import org.spongepowered.api.scheduler.Scheduler;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.service.ServiceProvider;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.plugin.PluginContainer;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class PlayerAnswerQuestionHandlerTest {

    @Mock
    Logger logger;

    @Mock
    PluginContainer pluginContainer;

    @Mock
    Game game;

    @Mock
    ServerPlayer player;

    @Captor
    ArgumentCaptor<Component> message;

    @Captor
    ArgumentCaptor<Runnable> taskCaptor;

    @Test
    void playerNotEligible() {
        final Question question = Question.builder().setQuestion("question").setAnswers(Set.of("answer")).setWeight(1).build();
        final PlayerAnswerQuestionHandler handler = new PlayerAnswerQuestionHandler(logger, question, game, pluginContainer);

        final ServerPlayer eligiblePlayer = Mockito.mock(ServerPlayer.class);
        assertFalse(handler.answer(player, "answer", List.of(eligiblePlayer)));
        Mockito.verify(player, Mockito.never()).sendMessage(Mockito.any(Component.class));
    }

    @Test
    void playerWrongAnswer() throws CommandException {
        final UUID playerUUID = UUID.randomUUID();
        try (final MockedStatic<Sponge> spongeMock = Mockito.mockStatic(Sponge.class);
             final MockedStatic<Task> taskMock = Mockito.mockStatic(Task.class)) {
            final ServiceProvider.ServerScoped serviceProvider = Mockito.mock(ServiceProvider.ServerScoped.class);
            final EconomyService economyService = Mockito.mock(EconomyService.class);
            final UniqueAccount uniqueAccount = Mockito.mock(UniqueAccount.class);
            Mockito.when(economyService.findOrCreateAccount(playerUUID)).thenReturn(Optional.of(uniqueAccount));
            final Currency currency = Mockito.mock(Currency.class);
            Mockito.when(economyService.defaultCurrency()).thenReturn(currency);
            Mockito.when(serviceProvider.provide(EconomyService.class)).thenReturn(Optional.of(economyService));
            final Server server = Mockito.mock(Server.class);
            Mockito.when(server.serviceProvider()).thenReturn(serviceProvider);
            spongeMock.when(Sponge::server).thenReturn(server);
            final SystemSubject systemSubject = Mockito.mock(SystemSubject.class);
            spongeMock.when(Sponge::systemSubject).thenReturn(systemSubject);
            final Task.Builder taskBuilderMock = Mockito.mock(Task.Builder.class);
            Mockito.when(taskBuilderMock.execute(taskCaptor.capture())).thenReturn(taskBuilderMock);
            Mockito.when(taskBuilderMock.plugin(Mockito.any(PluginContainer.class))).thenReturn(taskBuilderMock);
            Mockito.when(taskBuilderMock.build()).thenReturn(Mockito.mock(Task.class));
            taskMock.when(Task::builder).thenReturn(taskBuilderMock);
            final Scheduler schedulerMock = Mockito.mock(Scheduler.class);
            Mockito.when(game.server()).thenReturn(server);
            Mockito.when(server.scheduler()).thenReturn(schedulerMock);
            final ScheduledTask scheduledTaskMock = Mockito.mock(ScheduledTask.class);
            Mockito.when(schedulerMock.submit(Mockito.any(Task.class))).then(invocation -> {
                taskCaptor.getValue().run();
                return scheduledTaskMock;
            });

            final CauseStackManager causeStackManager = Mockito.mock(CauseStackManager.class);
            Mockito.when(server.causeStackManager()).thenReturn(causeStackManager);
            final CommandManager commandManager = Mockito.mock(CommandManager.class);
            Mockito.when(server.commandManager()).thenReturn(commandManager);

            final int malusAmount = 50;
            final String command = "/command @loser coin";
            final Question question = Question.builder().setQuestion("question").setAnswers(Set.of("answer")).setWeight(1)
                    .setTimeBetweenAnswer(50).setMalus(new Malus(malusAmount, false,
                            new OutcomeCommand[]{new OutcomeCommand("message", command)}))
                    .build();
            final PlayerAnswerQuestionHandler handler = new PlayerAnswerQuestionHandler(logger, question, game, pluginContainer);

            Mockito.when(player.uniqueId()).thenReturn(playerUUID);
            Mockito.when(player.name()).thenReturn("CanardNocturne");
            assertFalse(handler.answer(player, "wrong answer", List.of(player)));
            Mockito.verify(player, Mockito.times(2)).sendMessage(Mockito.any(Component.class));
            Mockito.verify(uniqueAccount).withdraw(currency, BigDecimal.valueOf(malusAmount));
            Mockito.verify(commandManager).process(systemSubject, player, command.replace("@loser", player.name()));
        }
    }

    @Test
    void foundAnswerButNotFinished() {
        final Question question = Question.builder().setQuestion("question").setAnswers(Set.of("answer"))
                .setPrizes(Set.of(Prize.builder(1).build(), Prize.builder(2).build()))
                .setWeight(1).build();
        final PlayerAnswerQuestionHandler handler = new PlayerAnswerQuestionHandler(logger, question, game, pluginContainer);

        assertFalse(handler.answer(player, "answer", List.of(player)));
        Mockito.verify(player).sendMessage(message.capture());
        assertTrue(MiniMessage.miniMessage().serialize(message.getValue()).contains(Messages.FOUND_ANSWER.getMessage()));
    }

    @Test
    void foundAnswerAndFinished() throws CommandException {
        final UUID playerUUID = UUID.randomUUID();
        try (final MockedStatic<Sponge> spongeMock = Mockito.mockStatic(Sponge.class);
             final MockedStatic<Task> taskMock = Mockito.mockStatic(Task.class);
             final MockedStatic<ResourceKey> resKeyMock = Mockito.mockStatic(ResourceKey.class);
             final MockedStatic<Key> keyMock = Mockito.mockStatic(Key.class);
             final MockedStatic<RegistryType> registryTypeMock = Mockito.mockStatic(RegistryType.class)) {
            final ServiceProvider.ServerScoped serviceProvider = Mockito.mock(ServiceProvider.ServerScoped.class);
            final EconomyService economyService = Mockito.mock(EconomyService.class);
            final UniqueAccount uniqueAccount = Mockito.mock(UniqueAccount.class);
            Mockito.when(economyService.findOrCreateAccount(playerUUID)).thenReturn(Optional.of(uniqueAccount));
            final Currency currency = Mockito.mock(Currency.class);
            Mockito.when(economyService.defaultCurrency()).thenReturn(currency);
            Mockito.when(serviceProvider.provide(EconomyService.class)).thenReturn(Optional.of(economyService));
            final Server server = Mockito.mock(Server.class);
            Mockito.when(server.serviceProvider()).thenReturn(serviceProvider);
            spongeMock.when(Sponge::server).thenReturn(server);
            final SystemSubject systemSubject = Mockito.mock(SystemSubject.class);
            spongeMock.when(Sponge::systemSubject).thenReturn(systemSubject);
            final Task.Builder taskBuilderMock = Mockito.mock(Task.Builder.class);
            Mockito.when(taskBuilderMock.execute(taskCaptor.capture())).thenReturn(taskBuilderMock);
            Mockito.when(taskBuilderMock.delay(Mockito.anyLong(), Mockito.eq(TimeUnit.SECONDS))).thenReturn(taskBuilderMock);
            Mockito.when(taskBuilderMock.plugin(Mockito.any(PluginContainer.class))).thenReturn(taskBuilderMock);
            Mockito.when(taskBuilderMock.build()).thenReturn(Mockito.mock(Task.class));
            taskMock.when(Task::builder).thenReturn(taskBuilderMock);
            final Scheduler schedulerMock = Mockito.mock(Scheduler.class);
            Mockito.when(game.asyncScheduler()).thenReturn(schedulerMock);
            final ScheduledTask scheduledTaskMock = Mockito.mock(ScheduledTask.class);
            Mockito.when(schedulerMock.submit(Mockito.any(Task.class), Mockito.anyString())).then(invocation -> {
                taskCaptor.getValue().run();
                return scheduledTaskMock;
            });
            Mockito.when(schedulerMock.submit(Mockito.any(Task.class))).then(invocation -> {
                taskCaptor.getValue().run();
                return scheduledTaskMock;
            });

            final ResourceKey resourceKeyMock = Mockito.mock(ResourceKey.class);
            Mockito.when(resourceKeyMock.namespace()).thenReturn(ResourceKey.MINECRAFT_NAMESPACE);
            resKeyMock.when(() -> ResourceKey.minecraft(Mockito.anyString())).thenReturn(resourceKeyMock);
            final Key.Builder keyBuilderMock = Mockito.mock(Key.Builder.class);
            spongeMock.when(Sponge::game).thenReturn(game);
            keyMock.when(Key::builder).thenReturn(keyBuilderMock);
            final RegistryType registryTypeMockReturn = Mockito.mock(RegistryType.class);
            registryTypeMock.when(() -> RegistryType.of(Mockito.any(), Mockito.any())).thenReturn(registryTypeMockReturn);
            Mockito.when(game.server()).thenReturn(server);
            Mockito.when(server.scheduler()).thenReturn(schedulerMock);

            final CauseStackManager causeStackManager = Mockito.mock(CauseStackManager.class);
            Mockito.when(server.causeStackManager()).thenReturn(causeStackManager);
            final CommandManager commandManager = Mockito.mock(CommandManager.class);
            Mockito.when(server.commandManager()).thenReturn(commandManager);

            final ItemStack itemStackMock = Mockito.mock(ItemStack.class);
            final ItemType itemTypeMock = Mockito.mock(ItemType.class);
            Mockito.when(itemTypeMock.key(Mockito.any())).thenReturn(resourceKeyMock);
            Mockito.when(itemStackMock.type()).thenReturn(itemTypeMock);
            Mockito.when(itemStackMock.copy()).thenReturn(itemStackMock);
            final String command = "/command @winner coin";
            final Question question = Question.builder().setQuestion("question").setAnswers(Set.of("answer"))
                    .setPrizes(Set.of(Prize.builder(1).setMoney(50).addItem(itemStackMock).build(),
                            Prize.builder(2).addCommand(new OutcomeCommand("message", command)).build()))
                    .setWeight(1).build();
            final PlayerAnswerQuestionHandler handler = new PlayerAnswerQuestionHandler(logger, question, game, pluginContainer);

            final PlayerInventory playerInventory = Mockito.mock(PlayerInventory.class);
            Mockito.when(player.uniqueId()).thenReturn(playerUUID);
            Mockito.when(player.name()).thenReturn("CanardNocturne");
            final ServerPlayer player2 = Mockito.mock(ServerPlayer.class);
            Mockito.when(player2.inventory()).thenReturn(playerInventory);
            Mockito.when(player2.uniqueId()).thenReturn(playerUUID);
            final ServerPlayer playerLoser = Mockito.mock(ServerPlayer.class);
            Mockito.when(playerLoser.uniqueId()).thenReturn(playerUUID);
            assertFalse(handler.answer(player2, "answer", List.of(player, player2, playerLoser)));
            assertTrue(handler.answer(player, "answer", List.of(player, player2, playerLoser)));
            Mockito.verify(playerLoser, Mockito.times(2)).sendMessage(Mockito.any(Component.class));
            Mockito.verify(player, Mockito.times(4)).sendMessage(Mockito.any(Component.class));
            Mockito.verify(player2, Mockito.times(6)).sendMessage(Mockito.any(Component.class));
            Mockito.verify(uniqueAccount).deposit(currency, BigDecimal.valueOf(50));
            Mockito.verify(playerInventory).offer(itemStackMock);
            Mockito.verify(commandManager).process(systemSubject, player, command.replace("@winner", player.name()));
        }
    }

    @Test
    void timeout() {
        final Question question = Question.builder().setQuestion("question").setAnswers(Set.of("answer"))
                .setWeight(1).build();
        final PlayerAnswerQuestionHandler handler = new PlayerAnswerQuestionHandler(logger, question, game, pluginContainer);

        handler.end(List.of(player));
        Mockito.verify(player).sendMessage(message.capture());
        assertTrue(MiniMessage.miniMessage().serialize(message.getValue()).contains(Messages.QUESTION_TIMER_OUT.getMessage()));
    }

}