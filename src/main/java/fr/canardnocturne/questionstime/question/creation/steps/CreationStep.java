package fr.canardnocturne.questionstime.question.creation.steps;

import fr.canardnocturne.questionstime.question.creation.QuestionCreator;
import fr.canardnocturne.questionstime.question.creation.Visitor;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

public interface CreationStep extends Step {

    Component question();

    boolean handle(Audience sender, String input, QuestionCreator questionCreator);

    boolean shouldSkip(QuestionCreator questionCreator);

    default Step accept(final Visitor visitor, final String answer) {
        return visitor.visit(this, answer);
    }

}
