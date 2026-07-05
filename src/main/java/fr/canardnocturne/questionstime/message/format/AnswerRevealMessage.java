package fr.canardnocturne.questionstime.message.format;

import fr.canardnocturne.questionstime.message.component.set.SetAnswer;

public class AnswerRevealMessage extends MessageFormat<AnswerRevealMessage.Format> {

    public AnswerRevealMessage(final String section, final String message) {
        super(section, message);
    }

    @Override
    public AnswerRevealMessage.Format format() {
        return new AnswerRevealMessage.Format();
    }

    public class Format extends MessageFormat.Format implements SetAnswer<Format> {

    }
}
