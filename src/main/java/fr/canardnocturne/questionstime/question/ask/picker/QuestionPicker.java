package fr.canardnocturne.questionstime.question.ask.picker;

import fr.canardnocturne.questionstime.question.ask.pool.QuestionPool;
import fr.canardnocturne.questionstime.question.Question;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import java.util.Collection;

public abstract class QuestionPicker {

    protected final QuestionPool questionPool;

    protected QuestionPicker(final QuestionPool questionPool) {
        this.questionPool = questionPool;
    }

    public abstract Question pick(Collection<String> tags, Collection<ServerPlayer> players, int minimumEligiblePlayers);

}
