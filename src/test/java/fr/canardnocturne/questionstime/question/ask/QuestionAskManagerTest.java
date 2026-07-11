package fr.canardnocturne.questionstime.question.ask;

import fr.canardnocturne.questionstime.config.ConfigMutable;
import fr.canardnocturne.questionstime.question.ask.announcer.QuestionAnnouncer;
import fr.canardnocturne.questionstime.question.ask.launcher.QuestionLauncher;
import fr.canardnocturne.questionstime.question.ask.picker.QuestionPicker;
import fr.canardnocturne.questionstime.question.creation.QuestionCreationManager;
import fr.canardnocturne.questionstime.question.Question;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.spongepowered.api.Game;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.scheduler.ScheduledTask;
import org.spongepowered.api.scheduler.Scheduler;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.plugin.PluginContainer;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class QuestionAskManagerTest {

    @Mock
    private QuestionPicker questionPicker;

    @Mock
    private QuestionAnnouncer questionAnnouncer;

    @Mock
    private QuestionCreationManager questionCreationManager;

    @Mock
    private Game game;

    @Mock
    private PluginContainer plugin;

    @Mock
    private Logger logger;

    @Test
    void askRandomTimedQuestion() {
        final Collection<String> tags = Collections.singleton("tag1");
        try(final MockedStatic<Task> taskMock = Mockito.mockStatic(Task.class)) {
            final Question question = Mockito.mock(Question.class);
            Mockito.when(question.isTimed()).thenReturn(true);
            Mockito.when(question.getTimer()).thenReturn(5);
            Mockito.when(questionPicker.pick(Mockito.eq(tags))).thenReturn(question);

            final Server server = Mockito.mock(Server.class);
            final ServerPlayer sp = Mockito.mock(ServerPlayer.class);
            Mockito.when(game.server()).thenReturn(server);
            Mockito.when(server.onlinePlayers()).thenReturn(Set.of(sp));
            Mockito.when(questionCreationManager.isCreator(Mockito.any())).thenReturn(false);

            final Scheduler asyncScheduler = Mockito.mock(Scheduler.class);
            Mockito.when(game.asyncScheduler()).thenReturn(asyncScheduler);
            Mockito.when(asyncScheduler.submit(Mockito.any(Task.class), Mockito.anyString())).thenReturn(Mockito.mock(ScheduledTask.class));

            final Task.Builder taskBuilder = Mockito.mock(Task.Builder.class);
            final Task task = Mockito.mock(Task.class);
            taskMock.when(Task::builder).thenReturn(taskBuilder);
            Mockito.when(taskBuilder.execute(Mockito.any(Consumer.class))).thenReturn(taskBuilder);
            Mockito.when(taskBuilder.delay(Mockito.anyLong(), Mockito.any(TimeUnit.class))).thenReturn(taskBuilder);
            Mockito.when(taskBuilder.interval(Mockito.anyLong(), Mockito.any(TimeUnit.class))).thenReturn(taskBuilder);
            Mockito.when(taskBuilder.plugin(Mockito.any(PluginContainer.class))).thenReturn(taskBuilder);
            Mockito.when(taskBuilder.build()).thenReturn(task);
            
            final QuestionAskManager manager = new QuestionAskManager(questionPicker, questionAnnouncer, questionCreationManager, game, plugin, logger, new ConfigMutable<>(1), new ConfigMutable<>(null));
            manager.askRandomQuestion(tags);

            Mockito.verify(questionPicker).pick(Mockito.eq(tags));
            Mockito.verify(questionAnnouncer).announce(Mockito.eq(question), Mockito.anyList());
            Mockito.verify(asyncScheduler).submit(Mockito.any(Task.class), Mockito.anyString());
        }
    }

    @Test
    void askQuestionNotEnoughPlayers() {
        final QuestionLauncher launcher = Mockito.mock(QuestionLauncher.class);
        final Question question = Mockito.mock(Question.class);

        final Server server = Mockito.mock(Server.class);
        Mockito.when(game.server()).thenReturn(server);
        Mockito.when(server.onlinePlayers()).thenReturn(Set.of());

        final QuestionAskManager manager = new QuestionAskManager(questionPicker, questionAnnouncer, questionCreationManager, game, plugin, logger, new ConfigMutable<>(1), new ConfigMutable<>(launcher));
        manager.askQuestion(question);

        Mockito.verify(questionAnnouncer, Mockito.never()).announce(Mockito.any(), Mockito.anyList());
        Mockito.verify(launcher).start();
    }

    @Test
    void playerAnswerWithoutCurrentQuestion() {
        final Player player = Mockito.mock(Player.class);

        final QuestionAskManager manager = new QuestionAskManager(questionPicker, questionAnnouncer, questionCreationManager, game, plugin, logger, new ConfigMutable<>(1), new ConfigMutable<>(null));
        manager.answer(player, "coin");

        Mockito.verify(player).sendMessage(Mockito.any());
        Mockito.verifyNoInteractions(questionCreationManager);
    }

    @Test
    void playerAnswerWhenIsCreator() {
        final Player player = Mockito.mock(Player.class);
        final UUID uuid = UUID.randomUUID();
        Mockito.when(player.uniqueId()).thenReturn(uuid);
        Mockito.when(questionCreationManager.isCreator(uuid)).thenReturn(true);
        final ServerPlayer player2 = Mockito.mock(ServerPlayer.class);
        final UUID uuid2 = UUID.randomUUID();
        Mockito.when(player2.uniqueId()).thenReturn(uuid2);
        Mockito.when(questionCreationManager.isCreator(uuid2)).thenReturn(false);

        final Server server = Mockito.mock(Server.class);
        final Question question = Mockito.mock(Question.class);
        Mockito.when(game.server()).thenReturn(server);
        Mockito.when(server.onlinePlayers()).thenReturn(Set.of(player2));
        Mockito.when(questionPicker.pick(Mockito.eq(Collections.emptyList()))).thenReturn(question);
        Mockito.when(question.getPrizes()).thenReturn(Collections.emptySortedSet());

        final QuestionAskManager manager = new QuestionAskManager(questionPicker, questionAnnouncer, questionCreationManager, game, plugin, logger, new ConfigMutable<>(1), new ConfigMutable<>(null));
        manager.askRandomQuestion(Collections.emptyList());
        manager.answer(player, "any");

        Mockito.verify(player).sendMessage(Mockito.any());
    }

    @Test
    void answerFound() {
        try(final MockedStatic<Sponge> spongeMock = Mockito.mockStatic(Sponge.class)) {
            final Server server = Mockito.mock(Server.class);
            spongeMock.when(Sponge::server).thenReturn(server);

            final QuestionLauncher launcher = Mockito.mock(QuestionLauncher.class);
            final ServerPlayer player = Mockito.mock(ServerPlayer.class);
            final UUID uuid = UUID.randomUUID();
            final Question question = Mockito.mock(Question.class);
            final String answer = "answer";
            Mockito.when(player.uniqueId()).thenReturn(uuid);
            Mockito.when(questionCreationManager.isCreator(uuid)).thenReturn(false);
            Mockito.when(questionPicker.pick(Mockito.eq(Collections.emptyList()))).thenReturn(question);
            Mockito.when(game.server()).thenReturn(server);
            Mockito.when(server.onlinePlayers()).thenReturn(Set.of(player));
            Mockito.when(question.getAnswers()).thenReturn(Set.of(answer));
            Mockito.when(question.getPrizes()).thenReturn(Collections.emptySortedSet());

            final QuestionAskManager manager = new QuestionAskManager(questionPicker, questionAnnouncer, questionCreationManager, game, plugin, logger, new ConfigMutable<>(1), new ConfigMutable<>(launcher));
            manager.askRandomQuestion(Collections.emptyList());
            manager.answer(player, answer);

            assertFalse(manager.isQuestionHasBeenAsked());
            Mockito.verify(launcher).start();
        }
    }

    @Test
    void answerFoundTimedQuestionAndManualLauncher() {
        try(final MockedStatic<Sponge> spongeMock = Mockito.mockStatic(Sponge.class);
            final MockedStatic<Task> taskMock = Mockito.mockStatic(Task.class)) {
            final Server server = Mockito.mock(Server.class);
            spongeMock.when(Sponge::server).thenReturn(server);

            final QuestionLauncher launcher = Mockito.mock(QuestionLauncher.class);
            final ServerPlayer player = Mockito.mock(ServerPlayer.class);
            final UUID uuid = UUID.randomUUID();
            final Question question = Mockito.mock(Question.class);
            final String answer = "answer";
            Mockito.when(player.uniqueId()).thenReturn(uuid);
            Mockito.when(questionCreationManager.isCreator(uuid)).thenReturn(false);
            Mockito.when(questionPicker.pick(Collections.emptyList())).thenReturn(question);
            Mockito.when(game.server()).thenReturn(server);
            Mockito.when(server.onlinePlayers()).thenReturn(Set.of(player));
            Mockito.when(question.getAnswers()).thenReturn(Set.of(answer));
            Mockito.when(question.getPrizes()).thenReturn(Collections.emptySortedSet());
            Mockito.when(question.isTimed()).thenReturn(true);
            Mockito.when(question.getTimer()).thenReturn(5);

            final Scheduler asyncScheduler = Mockito.mock(Scheduler.class);
            Mockito.when(game.asyncScheduler()).thenReturn(asyncScheduler);
            Mockito.when(asyncScheduler.submit(Mockito.any(Task.class), Mockito.anyString())).thenReturn(Mockito.mock(ScheduledTask.class));

            final Task.Builder taskBuilder = Mockito.mock(Task.Builder.class);
            final Task task = Mockito.mock(Task.class);
            taskMock.when(Task::builder).thenReturn(taskBuilder);
            Mockito.when(taskBuilder.execute(Mockito.any(Consumer.class))).thenReturn(taskBuilder);
            Mockito.when(taskBuilder.delay(Mockito.anyLong(), Mockito.any(TimeUnit.class))).thenReturn(taskBuilder);
            Mockito.when(taskBuilder.interval(Mockito.anyLong(), Mockito.any(TimeUnit.class))).thenReturn(taskBuilder);
            Mockito.when(taskBuilder.plugin(Mockito.any(PluginContainer.class))).thenReturn(taskBuilder);
            Mockito.when(taskBuilder.build()).thenReturn(task);

            final QuestionAskManager manager = new QuestionAskManager(questionPicker, questionAnnouncer, questionCreationManager, game, plugin, logger, new ConfigMutable<>(1), new ConfigMutable<>(null));
            manager.askRandomQuestion(Collections.emptyList());
            manager.answer(player, answer);

            assertFalse(manager.isQuestionHasBeenAsked());
            Mockito.verify(launcher, Mockito.never()).start();
        }
    }

    @Test
    void enoughEligiblePlayers() {
        final Server server = Mockito.mock(Server.class);
        final ServerPlayer sp = Mockito.mock(ServerPlayer.class);
        Mockito.when(game.server()).thenReturn(server);
        Mockito.when(server.onlinePlayers()).thenReturn(Set.of(sp));
        Mockito.when(questionCreationManager.isCreator(Mockito.any())).thenReturn(false);

        final QuestionAskManager manager = new QuestionAskManager(questionPicker, questionAnnouncer, questionCreationManager, game, plugin, logger, new ConfigMutable<>(1), new ConfigMutable<>(null));

        assertTrue(manager.enoughEligiblePlayers());
    }

    @Test
    void notEnoughEligiblePlayers() {
        final Server server = Mockito.mock(Server.class);
        final ServerPlayer sp = Mockito.mock(ServerPlayer.class);
        final UUID uuid = UUID.randomUUID();
        Mockito.when(sp.uniqueId()).thenReturn(uuid);
        final ServerPlayer sp2 = Mockito.mock(ServerPlayer.class);
        final UUID uuid2 = UUID.randomUUID();
        Mockito.when(sp2.uniqueId()).thenReturn(uuid2);
        Mockito.when(game.server()).thenReturn(server);
        Mockito.when(server.onlinePlayers()).thenReturn(Set.of(sp, sp2));
        Mockito.when(questionCreationManager.isCreator(uuid)).thenReturn(true);
        Mockito.when(questionCreationManager.isCreator(uuid2)).thenReturn(false);

        final QuestionAskManager manager = new QuestionAskManager(questionPicker, questionAnnouncer, questionCreationManager, game, plugin, logger, new ConfigMutable<>(2), new ConfigMutable<>(null));

        assertFalse(manager.enoughEligiblePlayers());
    }

}