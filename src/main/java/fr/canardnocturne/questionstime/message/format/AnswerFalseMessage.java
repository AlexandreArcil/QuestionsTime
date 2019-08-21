package fr.canardnocturne.questionstime.message.format;

import fr.canardnocturne.questionstime.message.component.set.SetAnswer;

public class AnswerFalseMessage extends MessageFormat<AnswerFalseMessage.Format> {

    public AnswerFalseMessage(final String section, final String message) {
        super(section, message);
    }

    @Override
    public Format format() {
        return new Format();
    }

    public class Format extends MessageFormat.Format implements SetAnswer {

    }
}
