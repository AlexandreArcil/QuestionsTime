package fr.canardnocturne.questionstime.question.serializer;

import fr.canardnocturne.questionstime.question.component.Malus;
import fr.canardnocturne.questionstime.question.component.Prize;
import fr.canardnocturne.questionstime.question.type.Question;
import fr.canardnocturne.questionstime.question.type.Question.Types;
import fr.canardnocturne.questionstime.question.type.QuestionMulti;
import io.leangen.geantyref.TypeToken;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

public class QuestionSerializer implements TypeSerializer<Question> {

    @Override
    public Question deserialize(final Type type, final ConfigurationNode node) throws SerializationException {
        final Types questionType = Question.getType(node);
        if (questionType == Types.ERROR) {
            throw new SerializationException("The question " + node.key() + " contain one or several errors. " +
                    "Check if he contain the sections \"question\" and \"answer\" at least. ");
        }

        final String askedQuestion = node.node("question").getString();
        final String answer = node.node("answer").getString();
        final int timer = node.node("timer").getInt(-1);
        final int timeBetweenAnswer = node.node("time-between-answer").getInt(-1);
        final int weight = node.node("weight").getInt(1);
        final ConfigurationNode prizeNode = node.node("prize");
        final Prize prize = prizeNode.get(TypeToken.get(Prize.class));
        final ConfigurationNode malusNode = node.node("malus");
        final Malus malus = malusNode.get(TypeToken.get(Malus.class));

        final Question.QuestionBuilder questionBuilder;

        if (questionType == Types.MULTI) {
            final byte propositionAnswer = Byte.parseByte(answer);
            if (!StringUtils.isNumeric(answer) || propositionAnswer < 1) {
                throw new SerializationException("The question '" + askedQuestion + "' answer '" + answer + "' need to be a number between 1 and 128");
            }
            final ConfigurationNode propositionsNode = node.node("proposition");
            if (!(propositionsNode.isList()) || propositionsNode.childrenList().size() <= 1) {
                throw new SerializationException("The question '\"+askedQuestion+\"' need to have at least 2 propositions");
            }
            final List<String> propositions = propositionsNode.childrenList().stream().map(ConfigurationNode::getString).toList();
            questionBuilder = QuestionMulti.builder().setPropositions(propositions);
        } else {
            questionBuilder = Question.builder();
        }

        return questionBuilder.setAnswer(answer).setQuestion(askedQuestion).setPrize(prize)
                .setMalus(malus).setTimer(timer).setTimeBetweenAnswer(timeBetweenAnswer)
                .setWeight(weight).build();
    }

    @Override
    public void serialize(final Type type, final Question question, final ConfigurationNode node) throws SerializationException {
        if (question != null) {
            node.node("question").set(question.getQuestion());
            node.node("answer").set(question.getAnswer());
            node.node("timer").set(question.getTimer());
            node.node("time-between-answer").set(question.getTimeBetweenAnswer());
            node.node("weight").set(question.getWeight());
            if (question instanceof QuestionMulti) {
                node.node("proposition").set(((QuestionMulti) question).getPropositions());
            }

            final ConfigurationNode prizeNode = node.node("prize");
            final Optional<Prize> prizeOptional = question.getPrize();
            if (prizeOptional.isPresent()) {
                prizeNode.set(TypeToken.get(Prize.class), prizeOptional.get());
            }
            final ConfigurationNode malusNode = node.node("malus");
            final Optional<Malus> malusOptional = question.getMalus();
            if (malusOptional.isPresent()) {
                malusNode.set(TypeToken.get(Malus.class), malusOptional.get());
            }
        }
    }

}
