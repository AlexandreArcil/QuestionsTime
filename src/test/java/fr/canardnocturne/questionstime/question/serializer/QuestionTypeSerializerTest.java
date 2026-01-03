package fr.canardnocturne.questionstime.question.serializer;

import fr.canardnocturne.questionstime.question.component.Malus;
import fr.canardnocturne.questionstime.question.component.Prize;
import fr.canardnocturne.questionstime.question.type.Question;
import fr.canardnocturne.questionstime.question.type.QuestionMulti;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.NodePath;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QuestionTypeSerializerTest {

    @Test
    void deserializeSimpleQuestion() throws SerializationException {
        final String questionText = "What does a duck make?";
        final int timer = 30;
        final int timeBetweenAnswer = 60;
        final int weight = 90;
        final String answerText = "quack";
        final Prize prize = Mockito.mock(Prize.class);
        final Malus malus = Mockito.mock(Malus.class);
        final ConfigurationNode node = Mockito.mock(ConfigurationNode.class);
        Mockito.when(node.node("question")).thenReturn(node);
        Mockito.when(node.node("answer")).thenReturn(node);
        Mockito.when(node.node("timer")).thenReturn(node);
        Mockito.when(node.node("time-between-answer")).thenReturn(node);
        Mockito.when(node.node("weight")).thenReturn(node);
        Mockito.when(node.node("prizes")).thenReturn(node);
        Mockito.when(node.node("malus")).thenReturn(node);
        Mockito.when(node.node("proposition")).thenReturn(node);
        Mockito.when(node.empty()).thenReturn(false, false, true);
        Mockito.when(node.getString()).thenReturn(questionText);
        Mockito.when(node.getList(Mockito.eq(String.class), Mockito.anyList())).thenReturn(List.of(answerText));
        Mockito.when(node.getInt(Mockito.anyInt())).thenReturn(timer, timeBetweenAnswer, weight);
        Mockito.when(node.getList(Mockito.eq(Prize.class), Mockito.anyList())).thenReturn(List.of(prize));
        Mockito.when(node.get(Malus.class)).thenReturn(malus);

        final QuestionTypeSerializer serializer = new QuestionTypeSerializer();
        final Question question = serializer.deserialize(Object.class, node);

        assertEquals(questionText, question.getQuestion());
        assertTrue(question.getAnswers().contains(answerText));
        assertEquals(timer, question.getTimer());
        assertEquals(timeBetweenAnswer, question.getTimeBetweenAnswer());
        assertEquals(weight, question.getWeight());
        assertTrue(question.getPrizes().get().contains(prize));
        assertEquals(malus, question.getMalus().get());
    }
    
    @Test
    void deserializeMultiQuestion() throws SerializationException {
        final String questionText = "What are the colors of the French flag?";
        final int timer = 45;
        final int timeBetweenAnswer = 30;
        final int weight = 50;
        final List<String> answers = List.of("1", "3", "4");
        final List<String> propositions = List.of("blue", "green", "white", "red", "yellow");
        final ConfigurationNode node = Mockito.mock(ConfigurationNode.class);
        Mockito.when(node.node("question")).thenReturn(node);
        Mockito.when(node.node("answer")).thenReturn(node);
        Mockito.when(node.node("timer")).thenReturn(node);
        Mockito.when(node.node("time-between-answer")).thenReturn(node);
        Mockito.when(node.node("weight")).thenReturn(node);
        Mockito.when(node.node("proposition")).thenReturn(node);
        Mockito.when(node.node("prizes")).thenReturn(node);
        Mockito.when(node.node("malus")).thenReturn(node);
        Mockito.when(node.empty()).thenReturn(false, false, false);
        Mockito.when(node.getString()).thenReturn(questionText);
        Mockito.when(node.getList(Mockito.eq(String.class), Mockito.anyList())).thenReturn(answers, propositions);
        Mockito.when(node.getInt(Mockito.anyInt())).thenReturn(timer, timeBetweenAnswer, weight);

        final QuestionTypeSerializer serializer = new QuestionTypeSerializer();
        final Question question = serializer.deserialize(Object.class, node);

        assertEquals(questionText, question.getQuestion());
        assertTrue(question.getAnswers().containsAll(answers));
        assertEquals(timer, question.getTimer());
        assertEquals(timeBetweenAnswer, question.getTimeBetweenAnswer());
        assertEquals(weight, question.getWeight());
        assertInstanceOf(QuestionMulti.class, question);
        final QuestionMulti multiQuestion = (QuestionMulti) question;
        assertTrue(multiQuestion.getPropositions().containsAll(propositions));
    }

    @Test
    void questionDoesNotHaveQuestion() {
        final ConfigurationNode node = Mockito.mock(ConfigurationNode.class);
        final NodePath path = Mockito.mock(NodePath.class);
        Mockito.when(path.toString()).thenReturn("questions.question1");
        Mockito.when(node.node("question")).thenReturn(node);
        Mockito.when(node.node("answer")).thenReturn(node);
        Mockito.when(node.key()).thenReturn("question1");
        Mockito.when(node.empty()).thenReturn(true);
        Mockito.when(node.path()).thenReturn(path);

        final QuestionTypeSerializer serializer = new QuestionTypeSerializer();
        final SerializationException exception = Assertions.assertThrows(SerializationException.class, () -> serializer.deserialize(Object.class, node));

        assertEquals("questions.question1 of type fr.canardnocturne.questionstime.question.type.Question: The question question1 contain one or several errors. Check if he contain the sections \"question\" and \"answer\" at least.", exception.getMessage());
    }

    @Test
    void answerIsEmpty() {
        final ConfigurationNode node = Mockito.mock(ConfigurationNode.class);
        Mockito.when(node.node("question")).thenReturn(node);
        Mockito.when(node.node("answer")).thenReturn(node);
        Mockito.when(node.node("proposition")).thenReturn(node);
        Mockito.when(node.node("timer")).thenReturn(node);
        Mockito.when(node.node("time-between-answer")).thenReturn(node);
        Mockito.when(node.node("weight")).thenReturn(node);
        Mockito.when(node.node("prizes")).thenReturn(node);
        Mockito.when(node.node("malus")).thenReturn(node);
        Mockito.when(node.getString()).thenReturn("");
        Mockito.when(node.empty()).thenReturn(false, false, true);

        final QuestionTypeSerializer serializer = new QuestionTypeSerializer();
        Assertions.assertThrows(SerializationException.class, () -> serializer.deserialize(Object.class, node));
    }

    @Test
    void serializeSimpleQuestion() throws SerializationException {
        final String questionText = "What does a duck make?";
        final int timer = 30;
        final int timeBetweenAnswer = 60;
        final int weight = 90;
        final Set<String> answers = Set.of("quack");
        final Prize prize = Mockito.mock(Prize.class);
        final Malus malus = Mockito.mock(Malus.class);
        final Question question = Question.builder().setQuestion(questionText).setAnswers(answers).setWeight(weight)
                .setPrizes(Set.of(prize)).setMalus(malus).setTimer(timer).setTimeBetweenAnswer(timeBetweenAnswer)
                .build();

        final ConfigurationNode rootNode = Mockito.mock(ConfigurationNode.class);
        final ConfigurationNode node = Mockito.mock(ConfigurationNode.class);
        Mockito.when(rootNode.node("question")).thenReturn(node);
        Mockito.when(rootNode.node("answer")).thenReturn(node);
        Mockito.when(rootNode.node("timer")).thenReturn(node);
        Mockito.when(rootNode.node("time-between-answer")).thenReturn(node);
        Mockito.when(rootNode.node("weight")).thenReturn(node);
        Mockito.when(rootNode.node("prizes")).thenReturn(node);
        Mockito.when(rootNode.node("malus")).thenReturn(node);

        final QuestionTypeSerializer serializer = new QuestionTypeSerializer();
        serializer.serialize(Object.class, question, rootNode);

        final ArgumentCaptor<Object> questionCaptor = ArgumentCaptor.forClass(Object.class);
        Mockito.verify(node, Mockito.times(5)).set(questionCaptor.capture());
        final List<Object> values = questionCaptor.getAllValues();
        assertEquals(questionText, values.getFirst());
        assertEquals(answers, values.get(1));
        assertEquals(timer, values.get(2));
        assertEquals(timeBetweenAnswer, values.get(3));
        assertEquals(weight, values.get(4));
        Mockito.verify(node).setList(Prize.class, List.of(prize));
        Mockito.verify(node).set(Malus.class, malus);
        Mockito.verify(node, Mockito.never()).node("proposition");
    }

    @Test
    void serializeMultiQuestion() throws SerializationException {
        final String questionText = "What are the colors of the French flag?";
        final int timer = 45;
        final int timeBetweenAnswer = 30;
        final int weight = 50;
        final List<String> answers = List.of("1", "3", "4");
        final LinkedHashSet<String> propositions = new LinkedHashSet<>(List.of("blue", "green", "white", "red", "yellow"));
        final Question question = QuestionMulti.builder().setQuestion(questionText).setAnswers(Set.copyOf(answers))
                .setPropositions(propositions).setTimer(timer).setTimeBetweenAnswer(timeBetweenAnswer)
                .setWeight(weight).build();
        final ConfigurationNode node = Mockito.mock(ConfigurationNode.class);
        Mockito.when(node.node("question")).thenReturn(node);
        Mockito.when(node.node("answer")).thenReturn(node);
        Mockito.when(node.node("timer")).thenReturn(node);
        Mockito.when(node.node("time-between-answer")).thenReturn(node);
        Mockito.when(node.node("weight")).thenReturn(node);
        Mockito.when(node.node("proposition")).thenReturn(node);

        final QuestionTypeSerializer serializer = new QuestionTypeSerializer();
        serializer.serialize(Object.class, question, node);

        final ArgumentCaptor<Object> questionCaptor = ArgumentCaptor.forClass(Object.class);
        Mockito.verify(node, Mockito.times(6)).set(questionCaptor.capture());
        final List<Object> values = questionCaptor.getAllValues();
        assertEquals(questionText, values.getFirst());
        assertEquals(new HashSet<>(answers), new HashSet<>((Collection) values.get(1)));
        assertEquals(timer, values.get(2));
        assertEquals(timeBetweenAnswer, values.get(3));
        assertEquals(weight, values.get(4));
        assertEquals(propositions, values.get(5));
    }

}