package fr.canardnocturne.questionstime.command.set.question.tmp.qc;

import fr.canardnocturne.questionstime.question.Question;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Collection;

public class QuestionModifierRemoveComplex {

    private final QuestionModifierComplex questionModifier;

    public QuestionModifierRemoveComplex(final QuestionModifierComplex questionModifier) {
        this.questionModifier = questionModifier;
    }

    Question remove(final Question question, final ListQuestionComponent questionComponent, final Collection<String> components) throws IOException {
        final Collection<String> modifiedComponents = questionComponent.getComponentCopy().apply(question);
        final int initialSize = modifiedComponents.size();
        modifiedComponents.removeAll(components);
        final int expectedSize = initialSize - components.size();
        if (components.size() != expectedSize) {
            final String componentNameError = StringUtils.capitalize(questionComponent.getSingular()) + "(s)";
            throw new IllegalArgumentException(componentNameError + " '"
                    + String.join(", ", components) + "' is/are not present in the question");
        }
        return this.questionModifier.set(question, questionComponent, modifiedComponents);
    }
}
