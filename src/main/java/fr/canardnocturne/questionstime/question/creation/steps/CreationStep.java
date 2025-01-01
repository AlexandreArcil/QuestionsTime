package fr.canardnocturne.questionstime.question.creation.steps;

import fr.canardnocturne.questionstime.question.creation.QuestionCreator;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

public interface CreationStep {

    Component question();

    boolean handle(Audience sender, String input, QuestionCreator questionCreator);

    boolean shouldSkip(QuestionCreator questionCreator);

    CreationStep next(QuestionCreator questionCreator);

}
