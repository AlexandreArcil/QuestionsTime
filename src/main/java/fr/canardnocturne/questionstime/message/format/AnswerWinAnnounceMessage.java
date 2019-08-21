package fr.canardnocturne.questionstime.message.format;

import fr.canardnocturne.questionstime.message.component.set.SetPlayerName;

public class AnswerWinAnnounceMessage extends MessageFormat<AnswerWinAnnounceMessage.Format> {

    public AnswerWinAnnounceMessage(final String section, final String message) {
        super(section, message);
    }

    @Override
    public Format format() {
        return new Format();
    }

    public class Format extends MessageFormat.Format implements SetPlayerName {

    }
}
