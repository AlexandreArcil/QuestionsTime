package fr.canardnocturne.questionstime.command.set.question.tmp.qc2;

import org.spongepowered.api.command.parameter.Parameter;

import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class QuestionComponentComplexListString<B, T> extends QuestionComponentComplexList<B, T, String> {

    public QuestionComponentComplexListString(final String name, final String singular, final String plural,
                                              final String commandDescription,
                                              final BiConsumer<B, List<String>> setComponent,
                                              final Function<T, List<String>> getComponent) {
        super(name, singular, plural, commandDescription, setComponent, getComponent, Function.identity(), Function.identity());
    }

    public QuestionComponentComplexListString(final String name, final String singular, final String plural,
                                              final String commandDescription,
                                              final BiConsumer<B, List<String>> setComponent,
                                              final Function<T, List<String>> getComponent,
                                              final Parameter.Value<String> addParameter) {
        super(name, singular, plural, commandDescription, setComponent, getComponent, Function.identity(), Function.identity(), addParameter);
    }
}
