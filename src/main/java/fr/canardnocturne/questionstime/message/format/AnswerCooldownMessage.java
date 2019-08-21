package fr.canardnocturne.questionstime.message.format;

import fr.canardnocturne.questionstime.message.component.set.SetTimer;

public class AnswerCooldownMessage extends MessageFormat<AnswerCooldownMessage.Format> {

    public AnswerCooldownMessage(final String section, final String message) {
        super(section, message);
    }

    @Override
    public Format format() {
        return new Format();
    }

    public class Format extends MessageFormat.Format implements SetTimer<Format> {

    }
}
