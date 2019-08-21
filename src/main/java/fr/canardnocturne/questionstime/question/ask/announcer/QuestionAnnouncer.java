package fr.canardnocturne.questionstime.question.ask.announcer;

import fr.canardnocturne.questionstime.question.type.Question;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import java.util.List;

public interface QuestionAnnouncer {

    void announce(final Question question, final List<ServerPlayer> players);

}
