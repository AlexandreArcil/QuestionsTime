package fr.canardnocturne.questionstime.question.creation.steps;

import fr.canardnocturne.questionstime.question.creation.QuestionCreator;
import fr.canardnocturne.questionstime.question.creation.Visitor;

public interface VerifyStep extends Step {

    boolean verify(final QuestionCreator questionCreator);

    Step returnTo();

    @Override
    default Step accept(final Visitor visitor, final String answer) {
        return visitor.visit(this);
    }
}
