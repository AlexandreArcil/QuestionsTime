package fr.canardnocturne.questionstime.message.format;

import fr.canardnocturne.questionstime.message.component.set.SetAnswers;

public class AnswersRevealMessage extends MessageFormat<AnswersRevealMessage.Format> {

    public AnswersRevealMessage(final String section, final String message) {
        super(section, message);
    }

    @Override
    public AnswersRevealMessage.Format format() {
        return new AnswersRevealMessage.Format();
    }

    public class Format extends MessageFormat.Format implements SetAnswers<Format> {

    }
}
