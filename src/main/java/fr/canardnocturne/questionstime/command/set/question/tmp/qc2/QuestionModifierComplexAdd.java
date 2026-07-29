package fr.canardnocturne.questionstime.command.set.question.tmp.qc2;

import fr.canardnocturne.questionstime.question.ask.pool.QuestionPool;
import fr.canardnocturne.questionstime.question.save.QuestionRegister;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;

public class QuestionModifierComplexAdd<B, T, W, V extends Collection<W>> extends QuestionModifierComplex<B, T, V> {

    private final QuestionComponentComplexCollection<B, T, W, V> questionComponent;

    public QuestionModifierComplexAdd(final QuestionRegister questionRegister, final QuestionPool questionPool,
                                      final QuestionComponentComplexCollection<B, T, W, V> questionComponent) {
        super(questionRegister, questionPool);
        this.questionComponent = questionComponent;
    }

    @Override
    protected void modify(final T type, final B builder, final V values) {
        final V collection = this.questionComponent.getComponent.apply(type);
        final int initialSize = collection.size();
        this.questionComponent.addAll(type, builder, values);
        final int expectedSize = initialSize + values.size();
        if (values.size() != expectedSize) {
            final String componentNameError = StringUtils.capitalize(values.size() == 1 ?
                    questionComponent.getSingular() : questionComponent.getPlural());
            final Collection<String> stringValues = values.stream().map(questionComponent.getValueUnmapper()).toList();
            throw new IllegalArgumentException(componentNameError + " '"
                    + String.join(", ", stringValues) + "' is/are already present in the question");
        }
    }
}
