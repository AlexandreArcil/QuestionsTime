package fr.canardnocturne.questionstime.question.creation;

import fr.canardnocturne.questionstime.question.creation.orchestrator.QuestionCreationOrchestrator;
import fr.canardnocturne.questionstime.question.creation.steps.CreationStep;
import fr.canardnocturne.questionstime.question.creation.steps.Step;
import fr.canardnocturne.questionstime.question.creation.steps.VerifyStep;
import org.spongepowered.api.entity.living.player.Player;

public class StepOrchestrator implements Visitor {

    private Player player;
    private QuestionCreator questionCreator;

    @Override
    public Step visit(final CreationStep creationStep, final String answer) {
        if(answer == null) {
            if(creationStep.shouldSkip(questionCreator)) {
                return this.nextStep(creationStep);
            }
            this.player.sendMessage(creationStep.question());
        } else {
            final boolean nextStep = creationStep.handle(this.player, answer, this.questionCreator);
            if (nextStep) {
                return this.nextStep(creationStep);
            }
        }
        return creationStep;
    }

    @Override
    public Step visit(final VerifyStep verifyStep) {
        final boolean correct = verifyStep.verify(this.questionCreator);
        if(correct) {
            return this.nextStep(verifyStep);
        } else {
            final Step previousStep = verifyStep.returnTo();
            return previousStep.accept(this, null);
        }
    }

    private Step nextStep(final Step creationStep) {
        final Step nextStep = creationStep.next(this.questionCreator);
        if (nextStep == null) {
            return null;
        }
        return nextStep.accept(this, null);
    }

}
