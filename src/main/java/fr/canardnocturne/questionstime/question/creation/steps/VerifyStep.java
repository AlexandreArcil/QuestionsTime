package fr.canardnocturne.questionstime.question.creation.steps;

import fr.canardnocturne.questionstime.question.creation.QuestionCreator;
import fr.canardnocturne.questionstime.question.creation.orchestrator.StepVisitor;
import net.kyori.adventure.text.Component;

public interface VerifyStep extends Step {

    boolean verify(final QuestionCreator questionCreator);

    Component mistake(final QuestionCreator questionCreator);

    Step returnTo();

    @Override
    default Step accept(final StepVisitor stepVisitor, final String answer) {
        return stepVisitor.visit(this);
    }
}
