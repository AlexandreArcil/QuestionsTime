package fr.canardnocturne.questionstime.message.format;

import fr.canardnocturne.questionstime.message.component.set.SetCommand;

public class OutcomeCommandMessage extends MessageFormat<OutcomeCommandMessage.Format> {

    public OutcomeCommandMessage(final String section, final String message) {
        super(section, message);
    }

    @Override
    public OutcomeCommandMessage.Format format() {
        return new OutcomeCommandMessage.Format();
    }

    public class Format extends MessageFormat.Format implements SetCommand<Format> {

    }
}
