package fr.canardnocturne.questionstime.command.set.question;

import fr.canardnocturne.questionstime.question.Question;
import net.kyori.adventure.text.Component;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.api.command.CommandCompletion;
import org.spongepowered.api.command.parameter.Parameter;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class QuestionComponentParameter {

    public static Parameter.Value<String> create(final String key, final Parameter.Value<Question> questionParam,
                                                 final Function<Question, Collection<String>> componentFunction) {
        return Parameter.builder(String.class)
                .addParser((parameterKey, reader, context) -> {
                    final Question question = context.requireOne(questionParam);
                    final String input = reader.parseString();
                    if (componentFunction.apply(question).contains(input)) {
                        return Optional.ofNullable(input);
                    } else {
                        throw reader.createException(Component.text(input + " is not a valid choice!"));
                    }
                })
                .completer((context, input) ->
                        context.one(questionParam)
                                .map(question -> componentFunction.apply(question).stream()
                                        .filter(component -> component.startsWith(input))
                                        .map(component -> StringUtils.containsAny(component, " ", ";", ":")
                                                ? "\"" + component + "\"" : component)
                                        .map(CommandCompletion::of)
                                        .toList())
                                .orElse(List.of()))
                .key(key)
                .consumeAllRemaining()
                .build();
    }

}
