package fr.canardnocturne.questionstime.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NumberUtilsTest {

    @Test
    void toOrdinal() {
        assertEquals("1st", NumberUtils.toOrdinal(1));
        assertEquals("2nd", NumberUtils.toOrdinal(2));
        assertEquals("3rd", NumberUtils.toOrdinal(3));
        assertEquals("4th", NumberUtils.toOrdinal(4));
        assertEquals("11th", NumberUtils.toOrdinal(11));
        assertEquals("12th", NumberUtils.toOrdinal(12));
        assertEquals("13th", NumberUtils.toOrdinal(13));
        assertEquals("21st", NumberUtils.toOrdinal(21));
        assertEquals("22nd", NumberUtils.toOrdinal(22));
        assertEquals("23rd", NumberUtils.toOrdinal(23));
        assertEquals("101st", NumberUtils.toOrdinal(101));
        assertEquals("111th", NumberUtils.toOrdinal(111));
    }

}