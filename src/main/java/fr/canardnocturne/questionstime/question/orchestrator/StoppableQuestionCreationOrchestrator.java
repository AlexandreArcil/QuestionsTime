package fr.canardnocturne.questionstime.question.orchestrator;

import fr.canardnocturne.questionstime.question.creation.QuestionCreator;
import fr.canardnocturne.questionstime.question.creation.steps.CreationStep;
import fr.canardnocturne.questionstime.question.creation.steps.QuestionStep;
import fr.canardnocturne.questionstime.question.creation.steps.StopQuestionCreationStep;
import org.spongepowered.api.entity.living.player.Player;

public class StoppableQuestionCreationOrchestrator implements QuestionCreationOrchestrator {

    private final Player player;
    private final QuestionCreator questionCreator;
    private CreationStep currentStep;
    private Status status;

    public StoppableQuestionCreationOrchestrator(final Player player) {
        this.player = player;
        this.questionCreator = new QuestionCreator();
        this.status = Status.NOT_STARTED;
    }

    @Override
    public void start() {
        this.currentStep = QuestionStep.INSTANCE;
        this.resume();
    }

    private void resume() {
        this.player.sendMessage(this.currentStep.question());
        this.status = Status.RUNNING;
    }

    @Override
    public void handle(final String answer) {
        if (this.isFinished())
            return;
        if (status == Status.STOPPING) {
            final boolean answered = StopQuestionCreationStep.INSTANCE.handle(this.player, answer, this.questionCreator);
            if (answered) {
                if (this.questionCreator.isStopped()) {
                    this.status = Status.FINISHED_STOPPED;
                } else {
                    this.resume();
                }
            }
        } else if ("stop".equals(answer)) {
            this.player.sendMessage(StopQuestionCreationStep.INSTANCE.question());
            this.status = Status.STOPPING;
        } else {
            final boolean nextStep = this.currentStep.handle(this.player, answer, this.questionCreator);
            if (nextStep) {
                do {
                    this.currentStep = this.currentStep.next(this.questionCreator);
                    if (this.currentStep == null) {
                        this.status = Status.FINISHED_SUCCESS;
                        return;
                    }
                } while (this.currentStep.shouldSkip(this.questionCreator));
                this.player.sendMessage(this.currentStep.question());
            }
        }
    }

    @Override
    public boolean isFinished() {
        return status == Status.FINISHED_STOPPED || status == Status.FINISHED_SUCCESS;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public QuestionCreator getQuestionCreator() {
        return questionCreator;
    }

}
