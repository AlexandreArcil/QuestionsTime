package fr.canardnocturne.questionstime.command.set.question.tmp.qc3.set;

import fr.canardnocturne.questionstime.command.set.question.tmp.qc3.component.QuestionComponentCollection;
import fr.canardnocturne.questionstime.command.set.question.tmp.qc3.section.QuestionSectionBase;
import fr.canardnocturne.questionstime.command.set.question.tmp.qc3.section.transform.SectionTransform;
import fr.canardnocturne.questionstime.question.Question;
import fr.canardnocturne.questionstime.util.TextUtils;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.command.CommandCompletion;
import org.spongepowered.api.command.parameter.Parameter;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class QuestionRemoveComponentParameter {

    public static <T, B, W, V extends Collection<W>> Parameter.Value<String> create(final Parameter.Value<Question> questionParam,
                                                                              final QuestionSectionBase<T, B, V> section,
                                                                              final QuestionComponentCollection<W, V> componentCollection) {
        return Parameter.builder(String.class)
                .addParser((parameterKey, reader, context) -> {
                    final Question question = context.requireOne(questionParam);
                    final String input = reader.parseString();
                    final SectionTransform<B, T> transform = section.createTransform(context, question);
                    final V values = section.getGetComponent().apply(transform.getType());
                    final Collection<String> mappedValues = values.stream().map(componentCollection.getValueUnmapper()).toList();
                    if (mappedValues.contains(input)) {
                        return Optional.ofNullable(input);
                    } else {
                        throw reader.createException(Component.text(input + " is not a valid choice!"));
                    }
                })
                .completer((context, input) ->
                        context.one(questionParam)
                                .map(question -> section.createTransform(context, question))
                                .map(transform -> section.getGetComponent().apply(transform.getType()).stream()
                                        .map(componentCollection.getValueUnmapper())
                                        .filter(component -> component.startsWith(input))
                                        .map(component -> TextUtils.shouldBeDoubleQuote(component)
                                                ? "\"" + component + "\"" : component)
                                        .map(CommandCompletion::of)
                                        .toList())
                                .orElse(List.of()))
                .key("remove-component")
                .consumeAllRemaining()
                .build();
    }

    /*public static <W, V extends Collection<W>> Parameter.Value<W> createMalus(final Parameter.Value<Question> questionParam,
                                                                              final QuestionComponentComplexCollection<Malus.Builder, Malus, W, V> componentCollection,
                                                                                   final Class<W> componentType) {
        return Parameter.builder(componentType)
                .addParser((parameterKey, reader, context) -> {
                    final Question question = context.requireOne(questionParam);
                    final String input = reader.parseString();
                    final MalusComponentTransform componentTransform = new MalusComponentTransform(question);
                    final V values = componentCollection.getComponent.apply(componentTransform.getType());
                    final Optional<W> selectedValue = values.stream()
                            .filter(value -> componentCollection.getValueUnmapper().apply(value).equals(input))
                            .findFirst();
                    if(selectedValue.isPresent()) {
                        return selectedValue;
                    } else {
                        throw reader.createException(Component.text(input + " is not a valid choice!"));
                    }
                })
                .completer((context, input) ->
                        context.one(questionParam)
                                .map(question -> new MalusComponentTransform(question).getType())
                                .map(malus -> componentCollection.getComponent.apply(malus).stream()
                                        .map(componentCollection.getValueUnmapper())
                                        .filter(component -> component.startsWith(input))
                                        .map(component -> TextUtils.shouldBeDoubleQuote(component)
                                                ? "\"" + component + "\"" : component)
                                        .map(CommandCompletion::of)
                                        .toList())
                                .orElse(List.of()))
                .key("remove-component")
                .consumeAllRemaining()
                .build();
    }

    public static <W, V extends Collection<W>> Parameter.Value<W> createPrize(final Parameter.Value<Question> questionParam,
                                                                              final QuestionComponentComplexCollection<Prize.Builder, Prize, W, V> componentCollection,
                                                                                   final Class<W> componentType) {
        final Parameter.Value<Integer> positionParam = Parameter.integerNumber().key("position").build();
        return Parameter.builder(componentType)
                .addParser((parameterKey, reader, context) -> {
                    final Question question = context.requireOne(questionParam);
                    final int position = context.requireOne(positionParam);
                    final String input = reader.parseString();
                    final PrizeTransform componentTransform = new PrizeTransform(question, position);
                    final V values = componentCollection.getComponent.apply(componentTransform.getType());
                    final Optional<W> selectedValue = values.stream()
                            .filter(value -> componentCollection.getValueUnmapper().apply(value).equals(input))
                            .findFirst();
                    if(selectedValue.isPresent()) {
                        return selectedValue;
                    } else {
                        throw reader.createException(Component.text(input + " is not a valid choice!"));
                    }
                })
                .completer((context, input) -> {
                            final Question question = context.requireOne(questionParam);
                            final int position = context.requireOne(positionParam);
                            final PrizeTransform componentTransform = new PrizeTransform(question, position);
                            final V values = componentCollection.getComponent.apply(componentTransform.getType());
                            return values.stream()
                                    .map(componentCollection.getValueUnmapper())
                                    .filter(component -> component.startsWith(input))
                                    .map(component -> TextUtils.shouldBeDoubleQuote(component)
                                            ? "\"" + component + "\"" : component)
                                    .map(CommandCompletion::of)
                                    .toList();
                        })
                .key("remove-component")
                .consumeAllRemaining()
                .build();
    }*/

}
