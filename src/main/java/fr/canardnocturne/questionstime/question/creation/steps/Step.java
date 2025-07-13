package fr.canardnocturne.questionstime.question.creation.steps;

import fr.canardnocturne.questionstime.question.creation.QuestionCreator;
import fr.canardnocturne.questionstime.question.creation.orchestrator.StepVisitor;

public interface Step {

    Step next(final QuestionCreator questionCreator);

    Step accept(final StepVisitor stepVisitor, final String answer);

}
