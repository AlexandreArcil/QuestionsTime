package fr.canardnocturne.questionstime;

import fr.canardnocturne.questionstime.question.Question;
import fr.canardnocturne.questionstime.question.ask.QuestionAskManager;
import fr.canardnocturne.questionstime.question.ask.announcer.AskQuestionOnPlayerJoinEventHandler;
import fr.canardnocturne.questionstime.question.ask.announcer.QuestionAnnouncer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.network.ServerSideConnectionEvent;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AskQuestionOnPlayerJoinEventHandlerTest {

    @Mock
    private QuestionAskManager questionAskManager;

    @Mock
    private QuestionAnnouncer questionAnnouncer;

    @InjectMocks
    private AskQuestionOnPlayerJoinEventHandler askQuestionOnPlayerJoinEventHandler;

    @Test
    void announceTheCurrentQuestion() {
        Mockito.when(questionAskManager.isQuestionHasBeenAsked()).thenReturn(true);
        Mockito.when(questionAskManager.getCurrentQuestion()).thenReturn(Optional.of(Mockito.mock(Question.class)));
        final ServerSideConnectionEvent.Join event = Mockito.mock(ServerSideConnectionEvent.Join.class);
        Mockito.when(event.player()).thenReturn(Mockito.mock(ServerPlayer.class));

        this.askQuestionOnPlayerJoinEventHandler.execute(event);

        Mockito.verify(questionAnnouncer).announce(Mockito.any(Question.class), Mockito.argThat(players -> players.size() == 1));
    }

    @Test
    void noQuestionInProgress() {
        Mockito.when(questionAskManager.isQuestionHasBeenAsked()).thenReturn(false);
        final ServerSideConnectionEvent.Join event = Mockito.mock(ServerSideConnectionEvent.Join.class);

        this.askQuestionOnPlayerJoinEventHandler.execute(event);

        Mockito.verify(questionAnnouncer, Mockito.never()).announce(Mockito.any(Question.class), Mockito.anyList());
    }

}