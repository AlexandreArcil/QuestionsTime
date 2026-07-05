package fr.canardnocturne.questionstime.message.component;

import fr.canardnocturne.questionstime.util.MiniMessageTest;
import net.kyori.adventure.text.Component;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ComponentAnswersTest {

    @Test
    void messageFormatCorrectForOneAnswer() {
        final ComponentAnswers componentAnswers = new ComponentAnswers("answers");
        final Set<String> answers = Set.of("Answer1");

        final Component result = componentAnswers.process(answers);

        assertEquals("Answer1", MiniMessageTest.NO_STYLE_COMPONENT.serialize(result));
    }

    @Test
    void messageFormatCorrectForTwoAnswers() {
        final ComponentAnswers componentAnswers = new ComponentAnswers("answers");
        final Set<String> answers = Set.of("Answer1", "Answer2");

        final Component result = componentAnswers.process(answers);

        final String serialize = MiniMessageTest.NO_STYLE_COMPONENT.serialize(result);
        assertTrue(serialize.matches("[\\w\\s]+ and [\\w\\s]+"));
        final String[] split = serialize.split(" and ");
        assertEquals(2, split.length);
        final Set<String> answers1 = new HashSet<>(Arrays.asList(split));
        assertTrue(answers1.contains("Answer1"));
        assertTrue(answers1.contains("Answer2"));
    }

    @Test
    void messageFormatCorrectForThreeAnswers() {
        final ComponentAnswers componentAnswers = new ComponentAnswers("answers");
        final Set<String> answers = Set.of("Answer1", "Answer2", "Answer3");

        final Component result = componentAnswers.process(answers);

        final String serialize = MiniMessageTest.NO_STYLE_COMPONENT.serialize(result);
        assertTrue(serialize.matches("[\\w\\s]+, [\\w\\s]+ and [\\w\\s]+"));
        final String[] split = serialize.split(", ");
        assertEquals(2, split.length);
        final String[] split2 = split[1].split(" and ");
        assertEquals(2, split2.length);
        final Set<String> answers1 = new HashSet<>(Arrays.asList(split[0], split2[0], split2[1]));
        assertTrue(answers1.contains("Answer1"));
        assertTrue(answers1.contains("Answer2"));
        assertTrue(answers1.contains("Answer3"));
    }

}