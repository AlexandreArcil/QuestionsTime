package fr.canardnocturne.questionstime.question.creation;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.spongepowered.api.event.network.ServerSideConnectionEvent;
import org.spongepowered.api.profile.GameProfile;

import java.util.Optional;
import java.util.UUID;

class CreatorLeftServerEventHandlerTest {

    @Test
    void connectedPlayerDisconnect() {
        final QuestionCreationManager manager = Mockito.mock(QuestionCreationManager.class);
        final ServerSideConnectionEvent.Disconnect event = Mockito.mock(ServerSideConnectionEvent.Disconnect.class);
        final GameProfile profile = Mockito.mock(GameProfile.class);
        final UUID uuid = UUID.randomUUID();
        final String playerName = "CanardNocturne";
        Mockito.when(event.profile()).thenReturn(Optional.of(profile));
        Mockito.when(profile.uuid()).thenReturn(uuid);
        Mockito.when(profile.name()).thenReturn(Optional.of(playerName));

        final CreatorLeftServerEventHandler handler = new CreatorLeftServerEventHandler(manager);
        handler.onPlayerLeftServer(event);

        Mockito.verify(manager).onPlayerDisconnect(uuid, playerName);
    }

    @Test
    void profileNoName() {
        final QuestionCreationManager manager = Mockito.mock(QuestionCreationManager.class);
        final ServerSideConnectionEvent.Disconnect event = Mockito.mock(ServerSideConnectionEvent.Disconnect.class);
        final GameProfile profile = Mockito.mock(GameProfile.class);
        final UUID uuid = UUID.randomUUID();
        Mockito.when(event.profile()).thenReturn(Optional.of(profile));
        Mockito.when(profile.uuid()).thenReturn(uuid);

        final CreatorLeftServerEventHandler handler = new CreatorLeftServerEventHandler(manager);
        handler.onPlayerLeftServer(event);

        Mockito.verify(manager).onPlayerDisconnect(uuid, "?");
    }

    @Test
    void noProfile() {
        final QuestionCreationManager manager = Mockito.mock(QuestionCreationManager.class);
        final ServerSideConnectionEvent.Disconnect event = Mockito.mock(ServerSideConnectionEvent.Disconnect.class);
        Mockito.when(event.profile()).thenReturn(Optional.empty());

        final CreatorLeftServerEventHandler handler = new CreatorLeftServerEventHandler(manager);
        handler.onPlayerLeftServer(event);

        Mockito.verifyNoInteractions(manager);
    }


}