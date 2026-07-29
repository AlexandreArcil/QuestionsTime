package fr.canardnocturne.questionstime.command.set.question.tmp.qc2;

import org.spongepowered.api.command.parameter.Parameter;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class QuestionComponentComplexSetString<B, T> extends QuestionComponentComplexSet<B, T, String> {

    public QuestionComponentComplexSetString(final String name, final String singular, final String plural,
                                             final String commandDescription,
                                             final BiConsumer<B, Set<String>> setComponent,
                                             final Function<T, Set<String>> getComponent) {
        super(name, singular, plural, commandDescription, setComponent, getComponent, Function.identity(), Function.identity());
    }

    public QuestionComponentComplexSetString(final String name, final String singular, final String plural,
                                             final String commandDescription,
                                             final BiConsumer<B, Set<String>> setComponent,
                                             final Function<T, Set<String>> getComponent,
                                             final Parameter.Value<String> addParameter) {
        super(name, singular, plural, commandDescription, setComponent, getComponent, Function.identity(), Function.identity(), addParameter);
    }
}
