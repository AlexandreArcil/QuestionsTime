package fr.canardnocturne.questionstime.message.format;

import fr.canardnocturne.questionstime.message.component.set.SetItem;
import fr.canardnocturne.questionstime.message.component.set.SetModId;
import fr.canardnocturne.questionstime.message.component.set.SetQuantity;

public class RewardPrizeMessage extends MessageFormat<RewardPrizeMessage.Format> {

    public RewardPrizeMessage(final String section, final String message) {
        super(section, message);
    }

    @Override
    public Format format() {
        return new Format();
    }

    public class Format extends MessageFormat.Format implements SetQuantity<Format>, SetModId<Format>, SetItem<Format> {

    }
}
