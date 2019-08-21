package fr.canardnocturne.questionstime.question.creation;

import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ServerSideConnectionEvent;
import org.spongepowered.api.profile.GameProfile;

import java.util.Optional;
import java.util.UUID;

public class CreatorLeftServerEventHandler {

    private final QuestionCreationManager questionCreationManager;

    public CreatorLeftServerEventHandler(final QuestionCreationManager questionCreationManager) {
        this.questionCreationManager = questionCreationManager;
    }

    @Listener
    public void onPlayerLeftServer(final ServerSideConnectionEvent.Disconnect event) {
        final Optional<GameProfile> profile = event.profile();
        if (profile.isPresent()) {
            final GameProfile gameProfile = profile.get();
            final UUID uuid = gameProfile.uuid();
            final String playerName = gameProfile.name().orElse("?"); //If the player was creating a question, the profile should have a player name
            this.questionCreationManager.onPlayerDisconnect(uuid, playerName);
        }
    }

}
