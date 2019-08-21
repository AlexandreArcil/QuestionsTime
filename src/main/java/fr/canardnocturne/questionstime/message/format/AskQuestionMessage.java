package fr.canardnocturne.questionstime.message.format;

import fr.canardnocturne.questionstime.message.component.set.SetQuestion;

public class AskQuestionMessage extends MessageFormat<AskQuestionMessage.Format> {

    public AskQuestionMessage(final String section, final String message) {
        super(section, message);
    }

    @Override
    public AskQuestionMessage.Format format() {
        return this.new Format();
    }

    public class Format extends MessageFormat.Format implements SetQuestion {

    }

}
