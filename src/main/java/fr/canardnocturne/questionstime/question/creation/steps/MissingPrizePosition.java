package fr.canardnocturne.questionstime.question.creation.steps;

import fr.canardnocturne.questionstime.question.creation.QuestionCreator;
import fr.canardnocturne.questionstime.util.TextUtils;
import net.kyori.adventure.text.Component;

import java.util.*;

public class MissingPrizePosition implements VerifyStep {

    public final static MissingPrizePosition INSTANCE = new MissingPrizePosition();

    @Override
    public boolean verify(final QuestionCreator questionCreator) {
        return this.containsNoPrizePositionGap(questionCreator);
    }

    private boolean containsNoPrizePositionGap(final QuestionCreator questionCreator) {
        return this.prizesGaps(questionCreator).isEmpty();
    }

    private SortedSet<Integer> prizesGaps(final QuestionCreator questionCreator) {
        final SortedSet<Integer> positions = new TreeSet<>();
        positions.addAll(questionCreator.getMoneyPrize().keySet());
        positions.addAll(questionCreator.getItemsPrize().keySet());
        positions.addAll(questionCreator.getCommandsPrize().keySet());
        return this.gaps(positions);
    }

    private SortedSet<Integer> gaps(final SortedSet<Integer> positions) {
        if (positions.isEmpty()) {
            return new TreeSet<>();
        }
        final SortedSet<Integer> gaps = new TreeSet<>();
        int previous = 0;
        for (final int current : positions) {
            if (current - previous > 1) {
                for (int i = previous + 1; i < current; i++) {
                    gaps.add(i);
                }
            }
            previous = current;
        }
        return gaps;
    }

    @Override
    public Component mistake(final QuestionCreator questionCreator) {
        final SortedSet<Integer> gaps = this.prizesGaps(questionCreator);
        final String gapsText = this.gapsText(gaps);
        final String mistakeGapsLabel = gaps.size() == 1 ? "position " + gapsText + " is": "positions " + gapsText + " are";
        return TextUtils.errorWithPrefix("There is a gap in the prize positions! Winner " + mistakeGapsLabel + " missing.");
    }

    private String gapsText(final SortedSet<Integer> gaps) {
        if (gaps.size() == 1) {
            return String.valueOf(gaps.first());
        }
        final StringBuilder sb = new StringBuilder();
        final Iterator<Integer> gapsIter = gaps.iterator();
        while (gapsIter.hasNext()) {
            sb.append(gapsIter.next());
            if (gapsIter.hasNext()) {
                sb.append(", ");
            }
        }
        sb.insert(sb.lastIndexOf(","), " and");
        return sb.toString();
    }

    @Override
    public Step next(final QuestionCreator questionCreator) {
        return AnnouncePrizeStep.INSTANCE;
    }

    @Override
    public Step returnTo() {
        return PrizeItemsStep.INSTANCE;
    }
}
