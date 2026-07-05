package fr.canardnocturne.questionstime.question.ask.announcer;

import fr.canardnocturne.questionstime.question.ask.QuestionAskManager;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ServerSideConnectionEvent;

import java.util.List;

public class AskQuestionOnPlayerJoinEventHandler {

    private final QuestionAskManager questionAskManager;
    private final QuestionAnnouncer questionAnnouncer;

    public AskQuestionOnPlayerJoinEventHandler(final QuestionAskManager questionAskManager, final QuestionAnnouncer questionAnnouncer) {
        this.questionAskManager = questionAskManager;
        this.questionAnnouncer = questionAnnouncer;
    }

    @Listener
    public void execute(final ServerSideConnectionEvent.Join event) {
        if(this.questionAskManager.isQuestionHasBeenAsked()) {
            this.questionAnnouncer.announce(this.questionAskManager.getCurrentQuestion().get(), List.of(event.player()));
        }
    }

}
