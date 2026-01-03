package fr.canardnocturne.questionstime.question.ask.answer;

import fr.canardnocturne.questionstime.question.ask.QuestionAskManager;
import net.kyori.adventure.text.Component;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.message.PlayerChatEvent;

import java.util.Optional;

class PlayerAnswerQuestionEventHandlerTest {

    @Test
    void playerAnswerIsProceeded() {
        final String answerText = "Coin coin";
        final QuestionAskManager questionAskManager = Mockito.mock(QuestionAskManager.class);
        Mockito.when(questionAskManager.isQuestionHasBeenAsked()).thenReturn(true);
        final PlayerChatEvent.Submit event = Mockito.mock(PlayerChatEvent.Submit.class);
        final ServerPlayer player = Mockito.mock(ServerPlayer.class);
        Mockito.when(event.player()).thenReturn(Optional.of(player));
        Mockito.when(event.originalMessage()).thenReturn(Component.text("qt>" + answerText));

        final PlayerAnswerQuestionEventHandler handler = new PlayerAnswerQuestionEventHandler(questionAskManager, false);
        handler.execute(event);

        Mockito.verify(questionAskManager).answer(player, answerText);
        Mockito.verify(event).setCancelled(false);
    }

    @Test
    void eventIsCancelledForPersonalAnswer() {
        final String answerText = "Coin coin";
        final QuestionAskManager questionAskManager = Mockito.mock(QuestionAskManager.class);
        Mockito.when(questionAskManager.isQuestionHasBeenAsked()).thenReturn(true);
        final PlayerChatEvent.Submit event = Mockito.mock(PlayerChatEvent.Submit.class);
        final ServerPlayer player = Mockito.mock(ServerPlayer.class);
        Mockito.when(event.player()).thenReturn(Optional.of(player));
        Mockito.when(event.originalMessage()).thenReturn(Component.text("qt>" + answerText));

        final PlayerAnswerQuestionEventHandler handler = new PlayerAnswerQuestionEventHandler(questionAskManager, true);
        handler.execute(event);

        Mockito.verify(questionAskManager).answer(player, answerText);
        Mockito.verify(event).setCancelled(true);
    }

    @Test
    void nonAnswerMessageIsIgnored() {
        final QuestionAskManager questionAskManager = Mockito.mock(QuestionAskManager.class);
        Mockito.when(questionAskManager.isQuestionHasBeenAsked()).thenReturn(true);
        final PlayerChatEvent.Submit event = Mockito.mock(PlayerChatEvent.Submit.class);
        final ServerPlayer player = Mockito.mock(ServerPlayer.class);
        Mockito.when(event.player()).thenReturn(Optional.of(player));
        Mockito.when(event.originalMessage()).thenReturn(Component.text("Coin coin"));

        final PlayerAnswerQuestionEventHandler handler = new PlayerAnswerQuestionEventHandler(questionAskManager, false);
        handler.execute(event);

        Mockito.verify(questionAskManager, Mockito.never()).answer(Mockito.any(), Mockito.anyString());
        Mockito.verify(event, Mockito.never()).setCancelled(Mockito.anyBoolean());
    }

    @Test
    void eventIsIgnoredWhenNoPlayer() {
        final QuestionAskManager questionAskManager = Mockito.mock(QuestionAskManager.class);
        Mockito.when(questionAskManager.isQuestionHasBeenAsked()).thenReturn(true);
        final PlayerChatEvent.Submit event = Mockito.mock(PlayerChatEvent.Submit.class);
        Mockito.when(event.player()).thenReturn(Optional.empty());
        Mockito.when(event.originalMessage()).thenReturn(Component.text("qt>Coin coin"));

        final PlayerAnswerQuestionEventHandler handler = new PlayerAnswerQuestionEventHandler(questionAskManager, false);
        handler.execute(event);

        Mockito.verify(questionAskManager, Mockito.never()).answer(Mockito.any(), Mockito.anyString());
        Mockito.verify(event, Mockito.never()).setCancelled(Mockito.anyBoolean());
    }

    @Test
    void eventIsIgnoredWhenNoQuestionAsked() {
        final QuestionAskManager questionAskManager = Mockito.mock(QuestionAskManager.class);
        Mockito.when(questionAskManager.isQuestionHasBeenAsked()).thenReturn(false);
        final PlayerChatEvent.Submit event = Mockito.mock(PlayerChatEvent.Submit.class);
        final ServerPlayer player = Mockito.mock(ServerPlayer.class);
        Mockito.when(event.player()).thenReturn(Optional.of(player));
        Mockito.when(event.originalMessage()).thenReturn(Component.text("qt>Coin coin"));

        final PlayerAnswerQuestionEventHandler handler = new PlayerAnswerQuestionEventHandler(questionAskManager, false);
        handler.execute(event);

        Mockito.verify(questionAskManager, Mockito.never()).answer(Mockito.any(), Mockito.anyString());
        Mockito.verify(event, Mockito.never()).setCancelled(Mockito.anyBoolean());
    }

}