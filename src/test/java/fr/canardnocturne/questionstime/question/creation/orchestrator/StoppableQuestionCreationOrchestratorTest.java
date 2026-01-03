package fr.canardnocturne.questionstime.question.creation.orchestrator;

import fr.canardnocturne.questionstime.question.creation.QuestionCreator;
import fr.canardnocturne.questionstime.question.creation.steps.CreationStep;
import fr.canardnocturne.questionstime.question.creation.steps.Step;
import fr.canardnocturne.questionstime.util.MiniMessageTest;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.spongepowered.api.entity.living.player.Player;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class StoppableQuestionCreationOrchestratorTest {

    @Mock
    private Player player;

    @InjectMocks
    private StoppableQuestionCreationOrchestrator orchestrator;

    @Test
    void startQuestionCreation() {
        assertEquals(StoppableQuestionCreationOrchestrator.Status.NOT_STARTED, this.getStatus());

        orchestrator.start();

        assertEquals(StoppableQuestionCreationOrchestrator.Status.RUNNING, this.getStatus());
        assertFalse(orchestrator.isFinished());
        assertFalse(orchestrator.isSuccessful());
    }

    @Test
    void continueCreationSameStep() {
        orchestrator.start();
        final CreationStepMock step2 = new CreationStepMock(false, null);
        this.setStep(step2);

        orchestrator.handle("Coin");

        assertEquals(StoppableQuestionCreationOrchestrator.Status.RUNNING, this.getStatus());
        assertFalse(orchestrator.isFinished());
        assertFalse(orchestrator.isSuccessful());
    }

    @Test
    void continueCreationNextStep() {
        orchestrator.start();
        final CreationStepMock step2 = new CreationStepMock(true, new CreationStepMock(false, null));
        this.setStep(step2);

        orchestrator.handle("Coin");

        assertEquals(StoppableQuestionCreationOrchestrator.Status.RUNNING, this.getStatus());
        assertFalse(orchestrator.isFinished());
        assertFalse(orchestrator.isSuccessful());
    }

    @Test
    void finishCreation() {
        final CreationStepMock step2 = new CreationStepMock(true, null);
        this.setStep(step2);

        orchestrator.handle("Coin");

        assertEquals(StoppableQuestionCreationOrchestrator.Status.FINISHED_SUCCESS, this.getStatus());
        assertTrue(orchestrator.isFinished());
        assertTrue(orchestrator.isSuccessful());
    }

    @Test
    void stopCreation() {
        final CreationStepMock step2 = new CreationStepMock(false, null);
        this.setStep(step2);

        orchestrator.handle("stop");
        assertEquals(StoppableQuestionCreationOrchestrator.Status.STOPPING, this.getStatus());

        orchestrator.handle("yes");
        Mockito.verify(this.player).sendMessage(Mockito.any(Component.class));
        assertEquals(StoppableQuestionCreationOrchestrator.Status.FINISHED_STOPPED, this.getStatus());
        assertTrue(orchestrator.isFinished());
        assertFalse(orchestrator.isSuccessful());
    }

    @Test
    void resumeCreationAfterStopDeclined() {
        final CreationStepMock step2 = new CreationStepMock(false, null);
        this.setStep(step2);

        orchestrator.handle("stop");
        assertEquals(StoppableQuestionCreationOrchestrator.Status.STOPPING, this.getStatus());

        orchestrator.handle("no");
        assertEquals(StoppableQuestionCreationOrchestrator.Status.RUNNING, this.getStatus());

        assertFalse(orchestrator.isFinished());
        assertFalse(orchestrator.isSuccessful());
    }

    @Test
    void handleFailure() {
        orchestrator.handleFailure();
        Mockito.verify(this.player).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).contains("Question creation stopped")));
    }

    private void setStep(final CreationStepMock step) {
        try {
            final var field = StoppableQuestionCreationOrchestrator.class.getDeclaredField("currentStep");
            field.setAccessible(true);
            field.set(orchestrator, step);
        } catch (final NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    
    private StoppableQuestionCreationOrchestrator.Status getStatus() {
        try {
            final var field = StoppableQuestionCreationOrchestrator.class.getDeclaredField("status");
            field.setAccessible(true);
            return (StoppableQuestionCreationOrchestrator.Status) field.get(orchestrator);
        } catch (final NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private record CreationStepMock(boolean finished, CreationStep nextStep) implements CreationStep {

        @Override
            public Step next(final QuestionCreator questionCreator) {
                return this.nextStep;
            }

            @Override
            public Component question() {
                return null;
            }

            @Override
            public boolean handle(final Audience sender, final String input, final QuestionCreator questionCreator) {
                return this.finished;
            }

            @Override
            public boolean shouldSkip(final QuestionCreator questionCreator) {
                return false;
            }
        }

}