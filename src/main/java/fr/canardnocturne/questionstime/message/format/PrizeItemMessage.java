package fr.canardnocturne.questionstime.message.format;

import fr.canardnocturne.questionstime.message.component.set.SetItem;
import fr.canardnocturne.questionstime.message.component.set.SetModId;
import fr.canardnocturne.questionstime.message.component.set.SetQuantity;

public class PrizeItemMessage extends MessageFormat<PrizeItemMessage.Format> {

    public PrizeItemMessage(final String section, final String message) {
        super(section, message);
    }

    @Override
    public Format format() {
        return new PrizeItemMessage.Format();
    }

    public class Format extends MessageFormat.Format implements SetQuantity<Format>, SetModId<Format>, SetItem<Format> {

    }
}
