package fr.canardnocturne.questionstime.question.creation.orchestrator;

import fr.canardnocturne.questionstime.question.creation.QuestionCreator;

public interface QuestionCreationOrchestrator {

    void start();

    void handle(String answer);

    boolean isFinished();

    Status getStatus();

    QuestionCreator getQuestionCreator();

    enum Status {
        NOT_STARTED, RUNNING, STOPPING, FINISHED_STOPPED, FINISHED_SUCCESS
    }

}
