package fr.canardnocturne.questionstime.question.ask.announcer;

import fr.canardnocturne.questionstime.QuestionsTime;
import fr.canardnocturne.questionstime.message.Messages;
import fr.canardnocturne.questionstime.question.component.Malus;
import fr.canardnocturne.questionstime.question.component.OutcomeCommand;
import fr.canardnocturne.questionstime.question.component.Prize;
import fr.canardnocturne.questionstime.question.type.Question;
import fr.canardnocturne.questionstime.question.type.QuestionMulti;
import fr.canardnocturne.questionstime.util.MiniMessageTest;
import io.leangen.geantyref.TypeToken;
import net.kyori.adventure.text.Component;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.spongepowered.api.Game;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.Key;
import org.spongepowered.api.data.value.Value;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.registry.BuilderProvider;
import org.spongepowered.api.registry.FactoryProvider;
import org.spongepowered.api.registry.RegistryType;
import org.spongepowered.api.scheduler.ScheduledTask;
import org.spongepowered.api.scheduler.Scheduler;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.service.ServiceProvider;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.plugin.PluginContainer;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SimpleQuestionAnnouncerTest {

    @Test
    void testAnnounceSimpleQuestion() {
        final ItemStack is1 = Mockito.mock(ItemStack.class);
        final ItemStack is2 = Mockito.mock(ItemStack.class);
        final ItemType it = Mockito.mock(ItemType.class);
        final ResourceKey rk = Mockito.mock(ResourceKey.class);
        Mockito.when(rk.namespace()).thenReturn(ResourceKey.MINECRAFT_NAMESPACE);
        Mockito.when(it.key(Mockito.any())).thenReturn(rk);
        Mockito.when(is1.type()).thenReturn(it);
        Mockito.when(is2.type()).thenReturn(it);
        final OutcomeCommand winner1 = new OutcomeCommand("winner1", "");
        final OutcomeCommand winner2 = new OutcomeCommand("winner2", "");
        final OutcomeCommand malus1 = new OutcomeCommand("malus1", "");
        final OutcomeCommand malus2 = new OutcomeCommand("malus2", "");
        final Malus malus = new Malus(50, true, new OutcomeCommand[]{malus1, malus2});
        final Question question = Question.builder().setQuestion("question").setAnswers(Set.of("answer")).setWeight(1)
                .setPrizes(Set.of(Prize.builder(1).setAnnounce(true).setMoney(50)
                                .addCommand(winner1)
                                .addCommand(winner2)
                                .addItem(is1).addItem(is2)
                                .build(),
                        Prize.builder(2).setAnnounce(true).setMoney(55).build()))
                .setMalus(malus)
                .build();
        this.mock(true, question, (economyService) -> List.of(Component.text(Messages.QUESTION_NEW.getMessage()),
                Component.text(question.getQuestion()),
                Messages.PRIZE_ANNOUNCE_POSITION.format().setWinnerPosition(1).message(),
                Messages.PRIZE_ITEM.format().setItem(is1).setModId(is1).setQuantity(is1.quantity()).message(),
                Messages.PRIZE_ITEM.format().setItem(is2).setModId(is1).setQuantity(is2.quantity()).message(),
                Messages.OUTCOME_COMMAND.format().setCommand(winner1).message(),
                Messages.OUTCOME_COMMAND.format().setCommand(winner2).message(),
                Messages.PRIZE_MONEY.format().setMoney(50).setCurrency(economyService).message(),
                Messages.PRIZE_ANNOUNCE_POSITION.format().setWinnerPosition(2).message(),
                Messages.PRIZE_MONEY.format().setMoney(55).setCurrency(economyService).message(),
                Component.text(Messages.MALUS_ANNOUNCE.getMessage()),
                Messages.MALUS_MONEY.format().setMoney(malus.getMoney()).setCurrency(economyService).message(),
                Messages.OUTCOME_COMMAND.format().setCommand(malus1).message(),
                Messages.OUTCOME_COMMAND.format().setCommand(malus2).message(),
                Component.text(Messages.ANSWER_ANNOUNCE.getMessage()),
                Component.text(Messages.QUESTION_END.getMessage())));
    }

    @Test
    void testAnnouncePropositionQuestion() {
        final ItemStack is1 = Mockito.mock(ItemStack.class);
        final ItemStack is2 = Mockito.mock(ItemStack.class);
        final ItemType it = Mockito.mock(ItemType.class);
        final ResourceKey rk = Mockito.mock(ResourceKey.class);
        Mockito.when(rk.namespace()).thenReturn(ResourceKey.MINECRAFT_NAMESPACE);
        Mockito.when(it.key(Mockito.any())).thenReturn(rk);
        Mockito.when(is1.type()).thenReturn(it);
        Mockito.when(is2.type()).thenReturn(it);
        final OutcomeCommand winner1 = new OutcomeCommand("winner1", "");
        final OutcomeCommand winner2 = new OutcomeCommand("winner2", "");
        final OutcomeCommand malus1 = new OutcomeCommand("malus1", "");
        final OutcomeCommand malus2 = new OutcomeCommand("malus2", "");
        final Malus malus = new Malus(50, true, new OutcomeCommand[]{malus1, malus2});
        final LinkedHashSet<String> propositions = new LinkedHashSet<>();
        propositions.add("proposition1");
        propositions.add("proposition2");
        propositions.add("proposition3");
        final Question question = QuestionMulti.builder().setPropositions(propositions).setQuestion("question").setAnswers(Set.of("1")).setWeight(1)
                .setPrizes(Set.of(Prize.builder(1).setAnnounce(true).setMoney(50)
                                .addCommand(winner1)
                                .addCommand(winner2)
                                .addItem(is1).addItem(is2)
                                .build(),
                        Prize.builder(2).setAnnounce(true).setMoney(55).build()))
                .setMalus(malus)
                .build();
        this.mock(true, question, economyService -> List.of(Component.text(Messages.QUESTION_NEW.getMessage()),
                Component.text(question.getQuestion()),
                QuestionsTime.PREFIX.append(Messages.QUESTION_PROPOSITION.format().setPosition((byte) 1).setProposition("proposition1").message()),
                QuestionsTime.PREFIX.append(Messages.QUESTION_PROPOSITION.format().setPosition((byte) 2).setProposition("proposition2").message()),
                QuestionsTime.PREFIX.append(Messages.QUESTION_PROPOSITION.format().setPosition((byte) 3).setProposition("proposition3").message()),
                Messages.PRIZE_ANNOUNCE_POSITION.format().setWinnerPosition(1).message(),
                Messages.PRIZE_ITEM.format().setItem(is1).setModId(is1).setQuantity(is1.quantity()).message(),
                Messages.PRIZE_ITEM.format().setItem(is2).setModId(is1).setQuantity(is2.quantity()).message(),
                Messages.OUTCOME_COMMAND.format().setCommand(winner1).message(),
                Messages.OUTCOME_COMMAND.format().setCommand(winner2).message(),
                Messages.PRIZE_MONEY.format().setMoney(50).setCurrency(economyService).message(),
                Messages.PRIZE_ANNOUNCE_POSITION.format().setWinnerPosition(2).message(),
                Messages.PRIZE_MONEY.format().setMoney(55).setCurrency(economyService).message(),
                Component.text(Messages.MALUS_ANNOUNCE.getMessage()),
                Messages.MALUS_MONEY.format().setMoney(malus.getMoney()).setCurrency(economyService).message(),
                Messages.OUTCOME_COMMAND.format().setCommand(malus1).message(),
                Messages.OUTCOME_COMMAND.format().setCommand(malus2).message(),
                Component.text(Messages.ANSWER_ANNOUNCE.getMessage()),
                Component.text(Messages.QUESTION_END.getMessage())));
    }

    @Test
    void testAnnounceWithoutEconomyService() {
        final ItemStack is1 = Mockito.mock(ItemStack.class);
        final ItemStack is2 = Mockito.mock(ItemStack.class);
        final ItemType it = Mockito.mock(ItemType.class);
        final ResourceKey rk = Mockito.mock(ResourceKey.class);
        Mockito.when(rk.namespace()).thenReturn(ResourceKey.MINECRAFT_NAMESPACE);
        Mockito.when(it.key(Mockito.any())).thenReturn(rk);
        Mockito.when(is1.type()).thenReturn(it);
        Mockito.when(is2.type()).thenReturn(it);
        final Malus malus = new Malus(50, true, new OutcomeCommand[0]);
        final Question question = Question.builder().setQuestion("question").setAnswers(Set.of("answer")).setWeight(1)
                .setPrizes(Set.of(Prize.builder(1).setAnnounce(true).setMoney(50).build()))
                .setMalus(malus)
                .build();
        this.mock(false, question, economyService -> List.of(Component.text(Messages.QUESTION_NEW.getMessage()),
                Component.text(question.getQuestion()),
                Component.text(Messages.ANSWER_ANNOUNCE.getMessage()),
                Component.text(Messages.QUESTION_END.getMessage())));
    }

    @Test
    void testAnnounceTimedQuestion() {
        final ItemStack is1 = Mockito.mock(ItemStack.class);
        final ItemStack is2 = Mockito.mock(ItemStack.class);
        final ItemType it = Mockito.mock(ItemType.class);
        final ResourceKey rk = Mockito.mock(ResourceKey.class);
        Mockito.when(rk.namespace()).thenReturn(ResourceKey.MINECRAFT_NAMESPACE);
        Mockito.when(it.key(Mockito.any())).thenReturn(rk);
        Mockito.when(is1.type()).thenReturn(it);
        Mockito.when(is2.type()).thenReturn(it);
        final Malus malus = new Malus(50, true, new OutcomeCommand[0]);
        final OutcomeCommand outcomeCommand = new OutcomeCommand("message", "");
        final Question question = Question.builder().setQuestion("question").setAnswers(Set.of("answer")).setWeight(1)
                .setTimer(60)
                .setPrizes(Set.of(Prize.builder(1).setAnnounce(true).addCommand(outcomeCommand).build()))
                .setMalus(malus)
                .build();
        this.mock(true, question, economyService -> List.of(Component.text(Messages.QUESTION_NEW.getMessage()),
                Component.text(question.getQuestion()),
                Component.text(Messages.PRIZE_ANNOUNCE.getMessage()),
                Messages.OUTCOME_COMMAND.format().setCommand(outcomeCommand).message(),
                Component.text(Messages.MALUS_ANNOUNCE.getMessage()),
                Messages.MALUS_MONEY.format().setMoney(malus.getMoney()).setCurrency(economyService).message(),
                Component.text(Messages.ANSWER_ANNOUNCE.getMessage()),
                QuestionsTime.PREFIX.append(Messages.QUESTION_TIMER_END.format().setTimer(question.getTimer()).message())));
    }

    @Test
    void testAnnounceNoPrize() {
        final ItemStack is1 = Mockito.mock(ItemStack.class);
        final ItemStack is2 = Mockito.mock(ItemStack.class);
        final ItemType it = Mockito.mock(ItemType.class);
        final ResourceKey rk = Mockito.mock(ResourceKey.class);
        Mockito.when(rk.namespace()).thenReturn(ResourceKey.MINECRAFT_NAMESPACE);
        Mockito.when(it.key(Mockito.any())).thenReturn(rk);
        Mockito.when(is1.type()).thenReturn(it);
        Mockito.when(is2.type()).thenReturn(it);
        final OutcomeCommand outcomeCommand = new OutcomeCommand("message", "");
        final Malus malus = new Malus(50, true, new OutcomeCommand[]{outcomeCommand});
        final Question question = Question.builder().setQuestion("question").setAnswers(Set.of("answer")).setWeight(1)
                .setTimer(60)
                .setMalus(malus)
                .build();
        this.mock(true, question, economyService -> List.of(Component.text(Messages.QUESTION_NEW.getMessage()),
                Component.text(question.getQuestion()),
                Component.text(Messages.MALUS_ANNOUNCE.getMessage()),
                Messages.MALUS_MONEY.format().setMoney(malus.getMoney()).setCurrency(economyService).message(),
                Messages.OUTCOME_COMMAND.format().setCommand(outcomeCommand).message(),
                Component.text(Messages.ANSWER_ANNOUNCE.getMessage()),
                QuestionsTime.PREFIX.append(Messages.QUESTION_TIMER_END.format().setTimer(question.getTimer()).message())));
    }

    @Test
    void testAnnounceNoMalus() {
        final ItemStack is1 = Mockito.mock(ItemStack.class);
        final ItemStack is2 = Mockito.mock(ItemStack.class);
        final ItemType it = Mockito.mock(ItemType.class);
        final ResourceKey rk = Mockito.mock(ResourceKey.class);
        Mockito.when(rk.namespace()).thenReturn(ResourceKey.MINECRAFT_NAMESPACE);
        Mockito.when(it.key(Mockito.any())).thenReturn(rk);
        Mockito.when(is1.type()).thenReturn(it);
        Mockito.when(is2.type()).thenReturn(it);
        final OutcomeCommand outcomeCommand = new OutcomeCommand("message", "");
        final Question question = Question.builder().setQuestion("question").setAnswers(Set.of("answer")).setWeight(1)
                .setTimer(60)
                .setPrizes(Set.of(Prize.builder(1).setAnnounce(true).addCommand(outcomeCommand).build()))
                .build();
        this.mock(true, question, economyService -> List.of(Component.text(Messages.QUESTION_NEW.getMessage()),
                Component.text(question.getQuestion()),
                Component.text(Messages.PRIZE_ANNOUNCE.getMessage()),
                Messages.OUTCOME_COMMAND.format().setCommand(outcomeCommand).message(),
                Component.text(Messages.ANSWER_ANNOUNCE.getMessage()),
                QuestionsTime.PREFIX.append(Messages.QUESTION_TIMER_END.format().setTimer(question.getTimer()).message())));
    }

    @Test
    void announceNoPrizeMalusAnnounce() {
        final ItemStack is1 = Mockito.mock(ItemStack.class);
        final ItemStack is2 = Mockito.mock(ItemStack.class);
        final ItemType it = Mockito.mock(ItemType.class);
        final ResourceKey rk = Mockito.mock(ResourceKey.class);
        Mockito.when(rk.namespace()).thenReturn(ResourceKey.MINECRAFT_NAMESPACE);
        Mockito.when(it.key(Mockito.any())).thenReturn(rk);
        Mockito.when(is1.type()).thenReturn(it);
        Mockito.when(is2.type()).thenReturn(it);
        final OutcomeCommand winner1 = new OutcomeCommand("winner1", "");
        final OutcomeCommand winner2 = new OutcomeCommand("winner2", "");
        final OutcomeCommand malus1 = new OutcomeCommand("malus1", "");
        final OutcomeCommand malus2 = new OutcomeCommand("malus2", "");
        final Malus malus = new Malus(50, false, new OutcomeCommand[]{malus1, malus2});
        final Question question = Question.builder().setQuestion("question").setAnswers(Set.of("answer")).setWeight(1)
                .setPrizes(Set.of(Prize.builder(1).setMoney(50)
                                .addCommand(winner1)
                                .addCommand(winner2)
                                .addItem(is1).addItem(is2)
                                .build(),
                        Prize.builder(2).setMoney(55).build()))
                .setMalus(malus)
                .build();
        this.mock(true, question, economyService -> List.of(Component.text(Messages.QUESTION_NEW.getMessage()),
                Component.text(question.getQuestion()),
                Component.text(Messages.ANSWER_ANNOUNCE.getMessage()),
                Component.text(Messages.QUESTION_END.getMessage())));
    }

    private void mock(final boolean economyService, final Question question, final Function<EconomyService, List<Component>> expectedContainsMessagesFun) {
        try (final MockedStatic<Sponge> spongeMock = Mockito.mockStatic(Sponge.class);
             final MockedStatic<Task> taskMock = Mockito.mockStatic(Task.class);
             final MockedStatic<ResourceKey> resKeyMock = Mockito.mockStatic(ResourceKey.class);
             final MockedStatic<Key> keyMock = Mockito.mockStatic(Key.class);
             final MockedStatic<RegistryType> registryTypeMock = Mockito.mockStatic(RegistryType.class)) {
            final Server serverMock = Mockito.mock(Server.class);
            final ServiceProvider.ServerScoped serviceProviderMock = Mockito.mock(ServiceProvider.ServerScoped.class);
            EconomyService economyServiceMock = null;
            if(economyService) {
                economyServiceMock = Mockito.mock(EconomyService.class);
                final Currency currency = Mockito.mock(Currency.class);
                Mockito.when(currency.displayName()).thenReturn(Component.text("Coin"));
                Mockito.when(economyServiceMock.defaultCurrency()).thenReturn(currency);
                Mockito.when(serviceProviderMock.provide(EconomyService.class)).thenReturn(Optional.of(economyServiceMock));
            } else {
                Mockito.when(serviceProviderMock.provide(EconomyService.class)).thenReturn(Optional.empty());
            }
            Mockito.when(serverMock.serviceProvider()).thenReturn(serviceProviderMock);
            spongeMock.when(Sponge::server).thenReturn(serverMock);
            final Game game = Mockito.mock(Game.class);
            final BuilderProvider builderProvider = Mockito.mock(BuilderProvider.class);
            final Key.Builder keyBuilderMock = Mockito.mock(Key.Builder.class);
            Mockito.when(keyBuilderMock.key(Mockito.any())).thenReturn(keyBuilderMock);
            Mockito.when(keyBuilderMock.elementType(Mockito.any(Class.class))).thenReturn(keyBuilderMock);
            Mockito.when(keyBuilderMock.elementType(Mockito.any(TypeToken.class))).thenReturn(keyBuilderMock);
            Mockito.when(keyBuilderMock.weightedCollectionElementType(Mockito.any(Class.class))).thenReturn(keyBuilderMock);
            Mockito.when(keyBuilderMock.listElementType(Mockito.any(Class.class))).thenReturn(keyBuilderMock);
            Mockito.when(keyBuilderMock.mapElementType(Mockito.any(Class.class), Mockito.any(Class.class))).thenReturn(keyBuilderMock);
            Mockito.when(keyBuilderMock.setElementType(Mockito.any(Class.class))).thenReturn(keyBuilderMock);
            Mockito.when(keyBuilderMock.mapElementType(Mockito.any(TypeToken.class), Mockito.any(TypeToken.class))).thenReturn(keyBuilderMock);

            final Key<Value<?>> keyValueMock = Mockito.mock(Key.class);
            Mockito.when(keyBuilderMock.build()).thenReturn(keyValueMock);
            Mockito.when(builderProvider.provide(Key.Builder.class)).thenReturn(keyBuilderMock);
            Mockito.when(game.builderProvider()).thenReturn(builderProvider);
            spongeMock.when(Sponge::game).thenReturn(game);
            keyMock.when(Key::builder).thenReturn(keyBuilderMock);
            final FactoryProvider factoryProviderMock = Mockito.mock(FactoryProvider.class);
            final RegistryType.Factory registryFactoryMock = Mockito.mock(RegistryType.Factory.class);
            Mockito.when(factoryProviderMock.provide(RegistryType.Factory.class)).thenReturn(registryFactoryMock);
            Mockito.when(game.factoryProvider()).thenReturn(factoryProviderMock);
            final ResourceKey resourceKeyMock = Mockito.mock(ResourceKey.class);
            resKeyMock.when(() -> ResourceKey.minecraft(Mockito.anyString())).thenReturn(resourceKeyMock);
            final RegistryType registryTypeMockReturn = Mockito.mock(RegistryType.class);
            registryTypeMock.when(() -> RegistryType.of(Mockito.any(), Mockito.any())).thenReturn(registryTypeMockReturn);

            final ArgumentCaptor<Consumer<ScheduledTask>> taskCaptor = ArgumentCaptor.forClass(Consumer.class);
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
                taskCaptor.getValue().accept(scheduledTaskMock);
                return scheduledTaskMock;
            });
            final PluginContainer plugin = Mockito.mock(PluginContainer.class);
            final SimpleQuestionAnnouncer announcer = new SimpleQuestionAnnouncer(game, plugin);

            final ServerPlayer player = Mockito.mock(ServerPlayer.class);
            announcer.announce(question, List.of(player));
            final List<Component> expectedContainsMessages = expectedContainsMessagesFun.apply(economyServiceMock);
            final ArgumentCaptor<Component> componentCaptor = ArgumentCaptor.forClass(Component.class);
            Mockito.verify(player, Mockito.times(expectedContainsMessages.size())).sendMessage(componentCaptor.capture());

            final List<Component> messages = componentCaptor.getAllValues();
            assertEquals(expectedContainsMessages.size(), messages.size());
            for (int i = 0; i < expectedContainsMessages.size(); i++) {
                final String message = MiniMessageTest.NO_STYLE_COMPONENT.serialize(messages.get(i));
                final String expectedMesssage = MiniMessageTest.NO_STYLE_COMPONENT.serialize(expectedContainsMessages.get(i));
                assertTrue(message.contains(expectedMesssage),
                        "Expected message '" + message + "' contains '" + expectedMesssage + "'");
            }
        }
    }


}