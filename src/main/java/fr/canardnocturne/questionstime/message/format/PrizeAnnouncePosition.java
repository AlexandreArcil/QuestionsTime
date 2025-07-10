package fr.canardnocturne.questionstime.message.format;

import fr.canardnocturne.questionstime.message.component.set.SetWinnerPosition;

public class PrizeAnnouncePosition extends MessageFormat<PrizeAnnouncePosition.Format>{

    public PrizeAnnouncePosition(final String section, final String message) {
        super(section, message);
    }

    @Override
    public PrizeAnnouncePosition.Format format() {
        return new Format();
    }

    public class Format extends MessageFormat.Format implements SetWinnerPosition<Format> {

    }

}
