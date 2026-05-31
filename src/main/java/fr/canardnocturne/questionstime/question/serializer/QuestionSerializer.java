package fr.canardnocturne.questionstime.question.serializer;

import fr.canardnocturne.questionstime.question.component.Malus;
import fr.canardnocturne.questionstime.question.component.Prize;
import fr.canardnocturne.questionstime.question.Question;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.SequencedSet;
import java.util.Set;
import java.util.SortedSet;

public class QuestionSerializer implements TypeSerializer<Question> {

    @Override
    public Question deserialize(final Type type, final ConfigurationNode node) throws SerializationException {
        if (this.isInvalid(node)) {
            throw new SerializationException(node, Question.class, "The question " + node.key() + " contain one or several errors. " +
                    "Check if he contain the sections \"question\" and \"answer\" at least.");
        }

        final String askedQuestion = node.node("question").getString();
        final Set<String> answers = new HashSet<>(node.node("answer").getList(String.class, Collections.emptyList()));
        final int timer = node.node("timer").getInt(-1);
        final int timeBetweenAnswer = node.node("time-between-answer").getInt(-1);
        final int weight = node.node("weight").getInt(1);
        final ConfigurationNode prizeNode = node.node("prizes");
        final Set<Prize> prizes = new HashSet<>(prizeNode.getList(Prize.class, Collections.emptyList()));
        final ConfigurationNode malusNode = node.node("malus");
        final Malus malus = malusNode.get(Malus.class);
        final SequencedSet<String> propositions = new LinkedHashSet<>(node.node("proposition").getList(String.class, Collections.emptyList()));

        final Question.QuestionBuilder questionBuilder = Question.builder();
        try {
            return questionBuilder.setAnswers(answers).setPropositions(propositions).setQuestion(askedQuestion).setPrizes(prizes)
                    .setMalus(malus).setTimer(timer).setTimeBetweenAnswer(timeBetweenAnswer)
                    .setWeight(weight).build();
        } catch (final Exception e) {
            throw new SerializationException(e);
        }
    }

    @Override
    public void serialize(final Type type, final Question question, final ConfigurationNode node) throws SerializationException {
        if (question != null) {
            node.node("question").set(question.getQuestion());
            node.node("answer").set(question.getAnswers());
            node.node("timer").set(question.getTimer());
            node.node("time-between-answer").set(question.getTimeBetweenAnswer());
            node.node("weight").set(question.getWeight());
            if(!question.getPropositions().isEmpty()) {
                node.node("proposition").setList(String.class, new ArrayList<>(question.getPropositions()));
            }

            final ConfigurationNode prizeNode = node.node("prizes");
            if (!question.getPrizes().isEmpty()) {
                prizeNode.setList(Prize.class, new ArrayList<>(question.getPrizes()));
            }
            final ConfigurationNode malusNode = node.node("malus");
            final Optional<Malus> malusOptional = question.getMalus();
            if (malusOptional.isPresent()) {
                malusNode.set(Malus.class, malusOptional.get());
            }
        }
    }

    private boolean isInvalid(final ConfigurationNode node) {
        return node.node("question").empty() || node.node("answer").empty();
    }

}
