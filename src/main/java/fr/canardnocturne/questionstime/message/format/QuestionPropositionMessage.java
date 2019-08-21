package fr.canardnocturne.questionstime.message.format;

import fr.canardnocturne.questionstime.message.component.set.SetPosition;
import fr.canardnocturne.questionstime.message.component.set.SetProposition;

public class QuestionPropositionMessage extends MessageFormat<QuestionPropositionMessage.Format> {

    public QuestionPropositionMessage(final String section, final String message) {
        super(section, message);
    }

    @Override
    public Format format() {
        return this.new Format();
    }

    public class Format extends MessageFormat.Format implements SetPosition<Format>, SetProposition {

    }

}
