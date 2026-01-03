package fr.canardnocturne.questionstime.question.creation.orchestrator;

import fr.canardnocturne.questionstime.question.creation.QuestionCreator;

public interface QuestionCreationOrchestrator {

    void start();

    void handle(String answer);

    boolean isFinished();

    boolean isSuccessful();

    void handleFailure();

    QuestionCreator getQuestionCreator();

}
