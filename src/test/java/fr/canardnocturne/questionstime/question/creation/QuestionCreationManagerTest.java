package fr.canardnocturne.questionstime.question.creation;

import fr.canardnocturne.questionstime.question.ask.pool.QuestionPool;
import fr.canardnocturne.questionstime.question.creation.orchestrator.QuestionCreationOrchestrator;
import fr.canardnocturne.questionstime.question.creation.orchestrator.QuestionCreationOrchestratorFactory;
import fr.canardnocturne.questionstime.question.save.QuestionRegister;
import fr.canardnocturne.questionstime.question.type.Question;
import fr.canardnocturne.questionstime.util.MiniMessageTest;
import net.kyori.adventure.text.Component;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.spongepowered.api.entity.living.player.Player;

import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class QuestionCreationManagerTest {

    @Mock
    QuestionCreationOrchestratorFactory orchestratorFactory;

    @Mock
    QuestionCreationOrchestrator orchestrator;

    @Mock
    QuestionPool questionPool;

    @Mock
    QuestionRegister questionRegister;

    @Mock
    Logger logger;

    @Mock
    Player player;

    @Mock
    QuestionCreator questionCreator;

    @Mock
    Question question;

    @InjectMocks
    QuestionCreationManager manager;

    UUID uuid;

    @BeforeEach
    void setup() {
        uuid = UUID.randomUUID();
        Mockito.when(player.uniqueId()).thenReturn(uuid);
        Mockito.when(orchestratorFactory.create(player)).thenReturn(orchestrator);
        Mockito.lenient().when(orchestrator.getQuestionCreator()).thenReturn(questionCreator);
        Mockito.lenient().when(questionCreator.build()).thenReturn(question);
    }

    @Test
    void startQuestionCreation() {
        manager.handlePlayerArguments(player, "");

        Mockito.verify(player).sendMessage(Mockito.any(Component.class));
        assertTrue(manager.isCreator(uuid));
        Mockito.verify(orchestratorFactory).create(player);
        Mockito.verify(orchestrator).start();
    }

    @Test
    void handleQuestionCreation() {
        final String answer = "Some answer";
        Mockito.when(orchestrator.isFinished()).thenReturn(false);

        manager.handlePlayerArguments(player, "");
        manager.handlePlayerArguments(player, answer);

        Mockito.verify(orchestrator).handle(answer);
        assertTrue(manager.isCreator(uuid));
    }

    @Test
    void successfulCreation() throws Exception {
        final String answer = "Some answer";
        Mockito.when(orchestrator.isFinished()).thenReturn(true);
        Mockito.when(orchestrator.isSuccessful()).thenReturn(true);

        final QuestionCreationManager manager = new QuestionCreationManager(orchestratorFactory, questionPool, questionRegister, logger);
        manager.handlePlayerArguments(player, "");
        manager.handlePlayerArguments(player, answer);

        Mockito.verify(questionRegister).register(question);
        Mockito.verify(questionPool).add(question);
        Mockito.verify(player).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.containsAll(component, "Question created and registered in the config file!","The question can be asked by the plugin at any time")));
        assertFalse(manager.isCreator(uuid));
    }

    @Test
    void failedCreation() {
        final String answer = "Some answer";
        Mockito.when(orchestrator.isFinished()).thenReturn(true);
        Mockito.when(orchestrator.isSuccessful()).thenReturn(false);

        manager.handlePlayerArguments(player, "");
        manager.handlePlayerArguments(player, answer);

        Mockito.verify(orchestrator).handleFailure();
        assertFalse(manager.isCreator(uuid));
    }

    @Test
    void failedQuestionRegister() throws Exception {
        final String answer = "Some answer";
        Mockito.when(orchestrator.isFinished()).thenReturn(true);
        Mockito.when(orchestrator.isSuccessful()).thenReturn(true);
        Mockito.doThrow(new IOException()).when(questionRegister).register(question);

        manager.handlePlayerArguments(player, "");
        manager.handlePlayerArguments(player, answer);

        Mockito.verify(player).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).contains("An error occurred when saving the question, see the latest.log. The question has been logged in debug.log")));
        assertFalse(manager.isCreator(uuid));
    }

    @Test
    void emptyArgs() {
        manager.handlePlayerArguments(player, "");
        manager.handlePlayerArguments(player, "");

        Mockito.verify(player).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).contains("Your answer can not be empty")));
    }

    @Test
    void creatorDisconnect() {
        manager.handlePlayerArguments(player, "");
        manager.onPlayerDisconnect(uuid, "CanardNocturne");

        assertFalse(manager.isCreator(uuid));
    }

}