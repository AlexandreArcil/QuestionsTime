package fr.canardnocturne.questionstime.question.creation.steps;

import fr.canardnocturne.questionstime.question.creation.QuestionCreator;
import fr.canardnocturne.questionstime.util.MiniMessageTest;
import net.kyori.adventure.text.Component;
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
class MissingPrizePositionTest {
    
    @Mock
    private QuestionCreator questionCreator;

    @Test
    void verifyNoGaps() {
        Mockito.when(questionCreator.getMoneyPrize()).thenReturn(Map.of(1, 100, 2, 200));
        Mockito.when(questionCreator.getItemsPrize()).thenReturn(Map.of(3, Collections.emptyList()));
        Mockito.when(questionCreator.getCommandsPrize()).thenReturn(Map.of(4, Collections.emptyList()));

        final boolean verified = MissingPrizePosition.INSTANCE.verify(questionCreator);

        assertTrue(verified);
    }

    @Test
    void verifyMoneyPrizeGap() {
        Mockito.when(questionCreator.getMoneyPrize()).thenReturn(Map.of(1, 100, 3, 200));
        Mockito.when(questionCreator.getItemsPrize()).thenReturn(Map.of());
        Mockito.when(questionCreator.getCommandsPrize()).thenReturn(Map.of());

        final boolean verified = MissingPrizePosition.INSTANCE.verify(questionCreator);

        assertFalse(verified);
    }

    @Test
    void verifyItemsPrizeGap() {
        Mockito.when(questionCreator.getMoneyPrize()).thenReturn(Map.of(1, 100));
        Mockito.when(questionCreator.getItemsPrize()).thenReturn(Map.of(3, Collections.emptyList()));
        Mockito.when(questionCreator.getCommandsPrize()).thenReturn(Map.of());

        final boolean verified = MissingPrizePosition.INSTANCE.verify(questionCreator);

        assertFalse(verified);
    }

    @Test
    void verifyCommandsPrizeGap() {
        Mockito.when(questionCreator.getMoneyPrize()).thenReturn(Map.of(1, 100));
        Mockito.when(questionCreator.getItemsPrize()).thenReturn(Map.of());
        Mockito.when(questionCreator.getCommandsPrize()).thenReturn(Map.of(3, Collections.emptyList()));

        final boolean verified = MissingPrizePosition.INSTANCE.verify(questionCreator);

        assertFalse(verified);
    }

    @Test
    void mistakeMessageDefinedOneBigGap() {
        Mockito.when(questionCreator.getMoneyPrize()).thenReturn(Map.of(1, 100, 5, 200));
        Mockito.when(questionCreator.getItemsPrize()).thenReturn(Map.of());
        Mockito.when(questionCreator.getCommandsPrize()).thenReturn(Map.of());

        final Component mistake = MissingPrizePosition.INSTANCE.mistake(questionCreator);

        assertTrue(MiniMessageTest.NO_STYLE_COMPONENT.serialize(mistake).endsWith("Winner positions 2, 3 and 4 are missing."));
    }

    @Test
    void mistakeMessageDefinedMultipleGaps() {
        Mockito.when(questionCreator.getMoneyPrize()).thenReturn(Map.of(1, 100));
        Mockito.when(questionCreator.getItemsPrize()).thenReturn(Map.of(4, Collections.emptyList()));
        Mockito.when(questionCreator.getCommandsPrize()).thenReturn(Map.of(6, Collections.emptyList()));

        final Component mistake = MissingPrizePosition.INSTANCE.mistake(questionCreator);

        assertTrue(MiniMessageTest.NO_STYLE_COMPONENT.serialize(mistake).endsWith("Winner positions 2, 3 and 5 are missing."));
    }

    @Test
    void mistakeMessageDefinedSingleGap() {
        Mockito.when(questionCreator.getMoneyPrize()).thenReturn(Map.of(1, 100, 3, 200));
        Mockito.when(questionCreator.getItemsPrize()).thenReturn(Map.of());
        Mockito.when(questionCreator.getCommandsPrize()).thenReturn(Map.of());

        final Component mistake = MissingPrizePosition.INSTANCE.mistake(questionCreator);

        assertTrue(MiniMessageTest.NO_STYLE_COMPONENT.serialize(mistake).endsWith("Winner position 2 is missing."));
    }

    @Test
    void nextStepDefined() {
        assertNotNull(MissingPrizePosition.INSTANCE.next(null));
    }

    @Test
    void returnedToDefined() {
        assertNotNull(MissingPrizePosition.INSTANCE.returnTo());
    }

}