package fr.canardnocturne.questionstime.message.component.set;

import fr.canardnocturne.questionstime.message.component.MessageComponents;
import fr.canardnocturne.questionstime.message.format.MessageFormat;

public interface SetQuestion<T extends MessageFormat.Format> extends SetComponent {

    default T setQuestion(final String question) {
        setComponent(MessageComponents.QUESTION, question);
        return (T) this;
    }

}
