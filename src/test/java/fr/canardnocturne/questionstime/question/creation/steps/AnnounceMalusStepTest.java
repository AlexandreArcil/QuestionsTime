package fr.canardnocturne.questionstime.question.creation.steps;

import fr.canardnocturne.questionstime.question.component.Malus;
import fr.canardnocturne.questionstime.question.component.OutcomeCommand;
import fr.canardnocturne.questionstime.question.creation.QuestionCreator;
import fr.canardnocturne.questionstime.util.MiniMessageTest;
import net.kyori.adventure.audience.Audience;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class AnnounceMalusStepTest {

    @Mock
    Audience sender;

    QuestionCreator questionCreator;

    @BeforeEach
    void setUp() {
        questionCreator = new QuestionCreator();
        questionCreator.setQuestion("question");
        questionCreator.getAnswers().add("answer");
    }

    @Test
    void questionNonNull() {
        assertNotNull(AnnounceMalusStep.INSTANCE.question());
    }

    @Test
    void announceMalus() {
        assertTrue(AnnounceMalusStep.INSTANCE.handle(sender, "yes", questionCreator));
        assertTrue(questionCreator.build().getMalus().stream().allMatch(Malus::isAnnounce));
    }

    @Test
    void MalusNotAnnounced() {
        assertTrue(AnnounceMalusStep.INSTANCE.handle(sender, "no", questionCreator));
        assertTrue(questionCreator.build().getMalus().stream().noneMatch(Malus::isAnnounce));
    }

    @Test
    void invalidAnswer() {
        assertFalse(AnnounceMalusStep.INSTANCE.handle(sender, "invalid", questionCreator));
        Mockito.verify(sender).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).contains("The answer can only be yes or no, not invalid")));
    }

    @Test
    void shouldSkipWhenNoMalus() {
        assertTrue(AnnounceMalusStep.INSTANCE.shouldSkip(questionCreator));
    }

    @Test
    void shouldNotSkipWhenMalusMoneyExists() {
        questionCreator.setMoneyMalus(50);
        assertFalse(AnnounceMalusStep.INSTANCE.shouldSkip(questionCreator));
    }

    @Test
    void shouldNotSkipWhenMalusCommandsExists() {
        questionCreator.getCommandsMalus().add(new OutcomeCommand("message", "command"));
        assertFalse(AnnounceMalusStep.INSTANCE.shouldSkip(questionCreator));
    }

}