package fr.canardnocturne.questionstime.message.format;

import fr.canardnocturne.questionstime.message.component.set.SetCommand;

public class PrizeCommandMessage extends MessageFormat<PrizeCommandMessage.Format> {

    public PrizeCommandMessage(final String section, final String message) {
        super(section, message);
    }

    @Override
    public PrizeCommandMessage.Format format() {
        return new PrizeCommandMessage.Format();
    }

    public class Format extends MessageFormat.Format implements SetCommand<Format> {

    }
}
