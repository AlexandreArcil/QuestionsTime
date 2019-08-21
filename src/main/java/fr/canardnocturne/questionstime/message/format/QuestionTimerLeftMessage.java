package fr.canardnocturne.questionstime.message.format;

import fr.canardnocturne.questionstime.message.component.set.SetTimer;

public class QuestionTimerLeftMessage extends MessageFormat<QuestionTimerLeftMessage.Format> {

    public QuestionTimerLeftMessage(final String section, final String message) {
        super(section, message);
    }

    @Override
    public QuestionTimerLeftMessage.Format format() {
        return new QuestionTimerLeftMessage.Format();
    }

    public class Format extends MessageFormat.Format implements SetTimer {

    }
}
