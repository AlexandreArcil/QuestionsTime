package fr.canardnocturne.questionstime.question.ask.answer;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import java.util.List;

public interface AnswerHandler {

    boolean answer(Player player, String answer, List<ServerPlayer> eligiblePlayers);

}
