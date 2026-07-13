package fr.canardnocturne.questionstime.question.serializer;

import fr.canardnocturne.questionstime.question.Question;
import fr.canardnocturne.questionstime.question.component.Malus;
import fr.canardnocturne.questionstime.question.component.Prize;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class QuestionSerializer implements TypeSerializer<Question> {

    @Override
    public Question deserialize(final Type type, final ConfigurationNode node) throws SerializationException {
        if (this.isInvalid(node)) {
            throw new SerializationException(node, Question.class, "The question " + node.key() + " contain one or several errors. " +
                    "Check if it contains the sections \"question\" and \"answer\" at least.");
        }

        final String askedQuestion = node.node("question").getString();
        final Set<String> answers = new HashSet<>(node.node("answer").getList(String.class, Collections.emptyList()));
        final int timer = node.node("timer").getInt(-1);
        final int timeBetweenAnswer = node.node("time-between-answer").getInt(-1);
        final int weight = node.node("weight").getInt(1);
        final boolean revealAnswer = node.node("reveal-answer").getBoolean(false);
        final ConfigurationNode prizeNode = node.node("prizes");
        final Set<Prize> prizes = new HashSet<>(prizeNode.getList(Prize.class, Collections.emptyList()));
        final ConfigurationNode malusNode = node.node("malus");
        final Malus malus = malusNode.get(Malus.class);
        final List<String> propositions = node.node("proposition").getList(String.class, Collections.emptyList());
        final Set<String> tags = new HashSet<>(node.node("tags").getList(String.class, Collections.emptyList()));
        final Set<String> includePermissions = new HashSet<>(node.node("include-permissions").getList(String.class, Collections.emptyList()));
        final Set<String> excludePermissions = new HashSet<>(node.node("exclude-permissions").getList(String.class, Collections.emptyList()));

        final Question.QuestionBuilder questionBuilder = Question.builder();
        try {
            return questionBuilder.setAnswers(answers).setPropositions(propositions).setQuestion(askedQuestion).setPrizes(prizes)
                    .setMalus(malus).setTimer(timer).setTimeBetweenAnswer(timeBetweenAnswer)
                    .setWeight(weight).setRevealAnswer(revealAnswer).setTags(tags).setIncludePermissions(includePermissions)
                    .setExcludePermissions(excludePermissions).build();
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
            node.node("reveal-answer").set(question.isRevealAnswer());
            if(!question.getPropositions().isEmpty()) {
                node.node("proposition").setList(String.class, new ArrayList<>(question.getPropositions()));
            }
            if (!question.getTags().isEmpty()) {
                node.node("tags").setList(String.class, new ArrayList<>(question.getTags()));
            }
            if(!question.getIncludePermissions().isEmpty()) {
                node.node("include-permissions").setList(String.class, new ArrayList<>(question.getIncludePermissions()));
            }
            if(!question.getExcludePermissions().isEmpty()) {
                node.node("exclude-permissions").setList(String.class, new ArrayList<>(question.getExcludePermissions()));
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
