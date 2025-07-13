package fr.canardnocturne.questionstime.question.creation.orchestrator;

import fr.canardnocturne.questionstime.question.creation.steps.CreationStep;
import fr.canardnocturne.questionstime.question.creation.steps.Step;
import fr.canardnocturne.questionstime.question.creation.steps.VerifyStep;

public interface StepVisitor {

    Step visit(final CreationStep creationStep, final String answer);

    Step visit(final VerifyStep verifyStep);

}
