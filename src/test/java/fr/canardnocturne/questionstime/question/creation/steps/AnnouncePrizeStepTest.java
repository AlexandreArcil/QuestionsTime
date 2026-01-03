package fr.canardnocturne.questionstime.question.creation.steps;

import fr.canardnocturne.questionstime.question.creation.QuestionCreator;
import fr.canardnocturne.questionstime.util.MiniMessageTest;
import net.kyori.adventure.audience.Audience;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class AnnouncePrizeStepTest {
    
    @Mock
    Audience audience;
    
    @Mock
    QuestionCreator qc;

    @Test
    void questionDefined() {
        assertNotNull(AnnouncePrizeStep.INSTANCE.question());
    }

    @Test
    void handleYes() {
        final boolean result = AnnouncePrizeStep.INSTANCE.handle(audience, "yes", qc);

        assertTrue(result);
        Mockito.verify(qc).setAnnouncePrize(true);
    }

    @Test
    void handleNo() {
        final boolean result = AnnouncePrizeStep.INSTANCE.handle(audience, "no", qc);

        assertTrue(result);
        Mockito.verify(qc).setAnnouncePrize(false);
    }

    @Test
    void handleInvalid() {
        final boolean result = AnnouncePrizeStep.INSTANCE.handle(audience, "coin", qc);

        assertFalse(result);
        Mockito.verifyNoInteractions(qc);
        Mockito.verify(audience).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).contains("The answer can only be yes OR no, not coin")));
    }

    @Test
    void shouldSkipWhenNoPrizes() {
        Mockito.when(qc.getMoneyPrize()).thenReturn(Collections.emptyMap());
        Mockito.when(qc.getItemsPrize()).thenReturn(Collections.emptyMap());
        Mockito.when(qc.getCommandsPrize()).thenReturn(Collections.emptyMap());

        final boolean result = AnnouncePrizeStep.INSTANCE.shouldSkip(qc);

        assertTrue(result);
    }

    @Test
    void shouldNotSkipWhenHasMoneyPrizes() {
        Mockito.when(qc.getMoneyPrize()).thenReturn(Map.of(1, 100));

        final boolean result = AnnouncePrizeStep.INSTANCE.shouldSkip(qc);

        assertFalse(result);
    }

    @Test
    void shouldNotSkipWhenHasItemPrizes() {
        Mockito.when(qc.getMoneyPrize()).thenReturn(Collections.emptyMap());
        Mockito.when(qc.getItemsPrize()).thenReturn(Map.of(1, Collections.emptyList()));

        final boolean result = AnnouncePrizeStep.INSTANCE.shouldSkip(qc);

        assertFalse(result);
    }

    @Test
    void shouldNotSkipWhenHasCommandPrizes() {
        Mockito.when(qc.getMoneyPrize()).thenReturn(Collections.emptyMap());
        Mockito.when(qc.getItemsPrize()).thenReturn(Collections.emptyMap());
        Mockito.when(qc.getCommandsPrize()).thenReturn(Map.of(1, Collections.emptyList()));

        final boolean result = AnnouncePrizeStep.INSTANCE.shouldSkip(qc);

        assertFalse(result);
    }

    @Test
    void nextStepIsDefined() {
        final Step nextStep = AnnouncePrizeStep.INSTANCE.next(qc);

        assertNotNull(nextStep);
    }

}