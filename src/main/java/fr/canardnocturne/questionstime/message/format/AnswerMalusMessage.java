package fr.canardnocturne.questionstime.message.format;

import fr.canardnocturne.questionstime.message.component.set.SetCurrency;
import fr.canardnocturne.questionstime.message.component.set.SetMoney;

public class AnswerMalusMessage extends MessageFormat<AnswerMalusMessage.Format> {

    public AnswerMalusMessage(final String section, final String message) {
        super(section, message);
    }

    @Override
    public Format format() {
        return new Format();
    }

    public class Format extends MessageFormat.Format implements SetMoney<Format>, SetCurrency<Format> {

    }
}
