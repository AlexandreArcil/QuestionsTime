package fr.canardnocturne.questionstime.util;

public class NumberUtils {

    private NumberUtils() {}

    /**
     * Convert a number to ordinal (1st, 2nd...).
     * Code nearly copy/paste from (<a href="https://stackoverflow.com/a/27204037">klamann</a>)
     * @param position the number to convert
     * @return the ordinal representation of the number
     */
    public static String toOrdinal(final int position) {
        final int mod100 = position % 100;
        final int mod10 = position % 10;
        if(mod10 == 1 && mod100 != 11) {
            return position + "st";
        } else if(mod10 == 2 && mod100 != 12) {
            return position + "nd";
        } else if(mod10 == 3 && mod100 != 13) {
            return position + "rd";
        } else {
            return position + "th";
        }
    }

}
