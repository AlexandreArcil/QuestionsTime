package fr.canardnocturne.questionstime.question.creation.orchestrator;

import fr.canardnocturne.questionstime.question.creation.QuestionCreator;
import fr.canardnocturne.questionstime.question.creation.steps.QuestionStep;
import fr.canardnocturne.questionstime.question.creation.steps.Step;
import fr.canardnocturne.questionstime.question.creation.steps.StopQuestionCreationStep;
import fr.canardnocturne.questionstime.util.TextUtils;
import org.spongepowered.api.entity.living.player.Player;

public class StoppableQuestionCreationOrchestrator implements QuestionCreationOrchestrator {

    private final Player player;
    private final QuestionCreator questionCreator;
    private final StepVisitor stepVisitor;
    private Step currentStep;
    private Status status;

    public StoppableQuestionCreationOrchestrator(final Player player) {
        this.player = player;
        this.questionCreator = new QuestionCreator();
        this.stepVisitor = new StepOrchestrator(player, this.questionCreator);
        this.status = Status.NOT_STARTED;
    }

    @Override
    public void start() {
        this.currentStep = QuestionStep.INSTANCE;
        this.resume();
    }

    private void resume() {
        this.currentStep = this.currentStep.accept(this.stepVisitor, null);
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
            this.currentStep = this.currentStep.accept(this.stepVisitor, answer);
            if (this.currentStep == null) {
                this.status = Status.FINISHED_SUCCESS;
            }
        }
    }

    @Override
    public boolean isFinished() {
        return status == Status.FINISHED_STOPPED || status == Status.FINISHED_SUCCESS;
    }

    @Override
    public boolean isSuccessful() {
        return status == Status.FINISHED_SUCCESS;
    }

    @Override
    public void handleFailure() {
        player.sendMessage(TextUtils.normalWithPrefix("Question creation stopped"));
    }

    @Override
    public QuestionCreator getQuestionCreator() {
        return questionCreator;
    }

    enum Status {
        NOT_STARTED, RUNNING, STOPPING, FINISHED_STOPPED, FINISHED_SUCCESS
    }

    public static class StoppableQuestionCreationOrchestratorFactory implements QuestionCreationOrchestratorFactory {

        @Override
        public QuestionCreationOrchestrator create(final Player player) {
            return new StoppableQuestionCreationOrchestrator(player);
        }

    }

}
