package fr.canardnocturne.questionstime.question.creation.steps;

import fr.canardnocturne.questionstime.question.creation.QuestionCreator;
import fr.canardnocturne.questionstime.question.creation.Visitor;

public interface Step {

    Step next(final QuestionCreator questionCreator);

    Step accept(final Visitor visitor, final String answer);

}
