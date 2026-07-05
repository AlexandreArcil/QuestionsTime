package fr.canardnocturne.questionstime.message.component.set;

import fr.canardnocturne.questionstime.message.component.MessageComponents;
import fr.canardnocturne.questionstime.message.format.MessageFormat;

import java.util.Set;

public interface SetAnswers<T extends MessageFormat.Format> extends SetComponent {

    default T setAnswers(final Set<String> answers) {
        setComponent(MessageComponents.ANSWERS, answers);
        return (T) this;
    }
}
