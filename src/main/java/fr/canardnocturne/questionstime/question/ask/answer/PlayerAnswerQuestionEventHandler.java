package fr.canardnocturne.questionstime.question.ask.answer;

import fr.canardnocturne.questionstime.question.ask.QuestionAskManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.message.PlayerChatEvent;

public class PlayerAnswerQuestionEventHandler {

    private final QuestionAskManager questionAskManager;
    private final boolean isPersonalAnswer;

    public PlayerAnswerQuestionEventHandler(final QuestionAskManager questionAskManager, final boolean isPersonalAnswer) {
        this.questionAskManager = questionAskManager;
        this.isPersonalAnswer = isPersonalAnswer;
    }

    @Listener
    public void execute(final PlayerChatEvent.Submit event) {
        if (this.questionAskManager.isQuestionHasBeenAsked()) {
            if (event.player().isPresent()) {
                final ServerPlayer player = event.player().get();
                final String originalMessage = MiniMessage.miniMessage().serialize(event.originalMessage());
                if (originalMessage.startsWith("qt>")) {
                    final String answer = originalMessage.substring(3).stripLeading();
                    this.questionAskManager.answer(player, answer);
                    event.setCancelled(this.isPersonalAnswer);
                }
            }
        }
    }
}
