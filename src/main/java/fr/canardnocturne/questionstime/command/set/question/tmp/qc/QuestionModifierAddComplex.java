package fr.canardnocturne.questionstime.command.set.question.tmp.qc;

import fr.canardnocturne.questionstime.question.Question;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Collection;

public class QuestionModifierAddComplex {

    private final QuestionModifierComplex questionModifier;

    public QuestionModifierAddComplex(final QuestionModifierComplex questionModifier) {
        this.questionModifier = questionModifier;
    }

    Question add(final Question question, final ListQuestionComponent questionComponent, final Collection<String> components) throws IOException {
        final Collection<String> modifiedComponents = questionComponent.getComponentCopy().apply(question);
        final int initialSize = modifiedComponents.size();
        modifiedComponents.addAll(components);
        final int expectedSize = initialSize + components.size();
        if (components.size() != expectedSize) {
            final String componentNameError = StringUtils.capitalize(questionComponent.getSingular()) + "(s)";
            throw new IllegalArgumentException(componentNameError + " '"
                    + String.join(", ", components) + "' is/are already present in the question");
        }
        return this.questionModifier.set(question, questionComponent, modifiedComponents);
    }
}
