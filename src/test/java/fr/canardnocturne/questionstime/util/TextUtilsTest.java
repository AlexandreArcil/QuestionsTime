package fr.canardnocturne.questionstime.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TextUtilsTest {

    @Test
    void onlyAnswer() {
        final TextUtils.AnswerPosition answerPosition = TextUtils.extractPositionFromAnswer("coin;coin;test");

        assertEquals("coin;coin;test", answerPosition.answer());
        assertEquals(1, answerPosition.position());
    }

    @Test
    void answerWithPosition() {
        final TextUtils.AnswerPosition answerPosition = TextUtils.extractPositionFromAnswer("coin;coin;test;5");

        assertEquals("coin;coin;test", answerPosition.answer());
        assertEquals(5, answerPosition.position());
    }

    @Test
    void answerWithInvalidPosition() {
        final TextUtils.AnswerPosition answerPosition = TextUtils.extractPositionFromAnswer("test;5;coin");

        assertEquals("test;5;coin", answerPosition.answer());
        assertEquals(1, answerPosition.position());
    }

}