package fr.canardnocturne.questionstime.question.creation.orchestrator;

import fr.canardnocturne.questionstime.question.creation.QuestionCreator;
import fr.canardnocturne.questionstime.question.creation.steps.CreationStep;
import fr.canardnocturne.questionstime.question.creation.steps.Step;
import fr.canardnocturne.questionstime.question.creation.steps.VerifyStep;
import net.kyori.adventure.text.Component;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.spongepowered.api.entity.living.player.Player;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class StepOrchestratorTest {

    @Mock
    private Player player;

    @Mock
    private QuestionCreator questionCreator;

    @InjectMocks
    private StepOrchestrator stepOrchestrator;

    @Test
    void creationStepSkipNextStepAnswerEmpty() {
        final CreationStep creationStep = Mockito.mock(CreationStep.class);
        final Step nextStep = Mockito.mock(Step.class);
        Mockito.when(creationStep.shouldSkip(questionCreator)).thenReturn(true);
        Mockito.when(creationStep.next(questionCreator)).thenReturn(nextStep);
        Mockito.when(nextStep.accept(stepOrchestrator, null)).thenReturn(nextStep);

        final Step result = stepOrchestrator.visit(creationStep, null);

        assertEquals(nextStep, result);
        Mockito.verify(nextStep).accept(stepOrchestrator, null);
    }

    @Test
    void creationStepNextStepAnswerEmpty() {
        final CreationStep creationStep = Mockito.mock(CreationStep.class);
        final Component component  = Mockito.mock(Component.class);
        Mockito.when(creationStep.shouldSkip(questionCreator)).thenReturn(false);
        Mockito.when(creationStep.question()).thenReturn(component);

        final Step result = stepOrchestrator.visit(creationStep, null);

        assertEquals(creationStep, result);
        Mockito.verify(player).sendMessage(component);
    }

    @Test
    void creationStepNextStepAnswerProvided() {
        final CreationStep creationStep = Mockito.mock(CreationStep.class);
        final Step nextStep = Mockito.mock(Step.class);
        final String answer = "answer";
        Mockito.when(creationStep.handle(player, answer, questionCreator)).thenReturn(true);
        Mockito.when(creationStep.next(questionCreator)).thenReturn(nextStep);
        Mockito.when(nextStep.accept(stepOrchestrator, null)).thenReturn(nextStep);

        final Step result = stepOrchestrator.visit(creationStep, answer);

        assertEquals(nextStep, result);
        Mockito.verify(nextStep).accept(stepOrchestrator, null);
    }

    @Test
    void creationStepStayOnCurrentStepAnswerProvided() {
        final CreationStep creationStep = Mockito.mock(CreationStep.class);
        final String answer = "answer";
        Mockito.when(creationStep.handle(player, answer, questionCreator)).thenReturn(false);

        final Step result = stepOrchestrator.visit(creationStep, answer);

        assertEquals(creationStep, result);
    }

    @Test
    void verifyStepCorrect() {
        final Step nextStep = Mockito.mock(Step.class);
        final VerifyStep verifyStep = Mockito.mock(VerifyStep.class);
        Mockito.when(verifyStep.verify(questionCreator)).thenReturn(true);
        Mockito.when(verifyStep.next(questionCreator)).thenReturn(nextStep);
        Mockito.when(nextStep.accept(stepOrchestrator, null)).thenReturn(nextStep);

        final Step result = stepOrchestrator.visit(verifyStep);

        assertEquals(nextStep, result);
        Mockito.verify(nextStep).accept(stepOrchestrator, null);
    }

    @Test
    void verifyStepIncorrect() {
        final Step previousStep = Mockito.mock(Step.class);
        final VerifyStep verifyStep = Mockito.mock(VerifyStep.class);
        final Component component  = Mockito.mock(Component.class);
        Mockito.when(verifyStep.verify(questionCreator)).thenReturn(false);
        Mockito.when(verifyStep.mistake(questionCreator)).thenReturn(component);
        Mockito.when(verifyStep.returnTo()).thenReturn(previousStep);
        Mockito.when(previousStep.accept(stepOrchestrator, null)).thenReturn(previousStep);

        final Step result = stepOrchestrator.visit(verifyStep);

        assertEquals(previousStep, result);
        Mockito.verify(player).sendMessage(component);
        Mockito.verify(previousStep).accept(stepOrchestrator, null);
    }

}