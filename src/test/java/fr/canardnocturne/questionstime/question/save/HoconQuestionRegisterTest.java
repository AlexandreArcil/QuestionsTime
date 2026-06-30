package fr.canardnocturne.questionstime.question.save;

import fr.canardnocturne.questionstime.question.Question;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.serialize.SerializationException;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class HoconQuestionRegisterTest {

    @Mock
    private ConfigurationLoader<CommentedConfigurationNode> configLoader;

    @Mock
    private Logger logger;

    @InjectMocks
    private HoconQuestionRegister register;

    @Test
    void questionRegistered() throws IOException {
        final CommentedConfigurationNode rootNode = Mockito.mock(CommentedConfigurationNode.class);
        final CommentedConfigurationNode questionsNode = Mockito.mock(CommentedConfigurationNode.class);
        Mockito.when(this.configLoader.load()).thenReturn(rootNode);
        Mockito.when(rootNode.node("questions")).thenReturn(questionsNode);
        final Question question = Mockito.mock(Question.class);

        this.register.register(question);

        Mockito.verify(questionsNode).getList(Question.class);
        Mockito.verify(questionsNode).setList(Mockito.eq(Question.class), Mockito.argThat(questions -> questions.contains(question)));
        Mockito.verify(this.configLoader).save(rootNode);
    }

    @Test
    void questionRegisterSerializationException() throws IOException {
        final CommentedConfigurationNode rootNode = Mockito.mock(CommentedConfigurationNode.class);
        final CommentedConfigurationNode questionsNode = Mockito.mock(CommentedConfigurationNode.class);
        Mockito.when(this.configLoader.load()).thenReturn(rootNode);
        Mockito.when(rootNode.node("questions")).thenReturn(questionsNode);
        final Question question = Mockito.mock(Question.class);
        Mockito.when(questionsNode.getList(Question.class)).thenThrow(new SerializationException("Serialization error"));

        Assertions.assertThrows(SerializationException.class, () -> this.register.register(question));

        Mockito.verify(this.logger).error(Mockito.contains("not registered because an error occurred when serializing it"), Mockito.any(SerializationException.class));
    }

    @Test
    void questionRegisterConfigurateException() throws IOException {
        final CommentedConfigurationNode rootNode = Mockito.mock(CommentedConfigurationNode.class);
        final CommentedConfigurationNode questionsNode = Mockito.mock(CommentedConfigurationNode.class);
        Mockito.when(this.configLoader.load()).thenReturn(rootNode);
        Mockito.when(rootNode.node("questions")).thenReturn(questionsNode);
        final Question question = Mockito.mock(Question.class);
        Mockito.doThrow(new ConfigurateException("IO error")).when(this.configLoader).save(rootNode);

        Assertions.assertThrows(ConfigurateException.class, () -> this.register.register(question));

        Mockito.verify(this.logger).error(Mockito.contains("not registered because an error occurred when saving it to the config file"), Mockito.any(IOException.class));
    }

    @Test
    void loadIOException() throws IOException {
        Mockito.when(this.configLoader.load()).thenThrow(new ConfigurateException("IO error"));

        Assertions.assertThrows(IllegalStateException.class, () -> this.register.register(Mockito.mock(Question.class)));

        Mockito.verify(this.logger).error(Mockito.contains("Unable to load the config file"), Mockito.any(IOException.class));
    }

    @Test
    void replaceQuestion() throws IOException {
        final CommentedConfigurationNode rootNode = Mockito.mock(CommentedConfigurationNode.class);
        final CommentedConfigurationNode questionsNode = Mockito.mock(CommentedConfigurationNode.class);
        Mockito.when(this.configLoader.load()).thenReturn(rootNode);
        Mockito.when(rootNode.node("questions")).thenReturn(questionsNode);
        final Question oldQuestion = Question.builder().setQuestion("Old question").setAnswers(Set.of("answer")).setWeight(1).build();
        Mockito.when(questionsNode.getList(Question.class)).thenReturn(List.of(oldQuestion));
        final Question newQuestion = Question.builder().setQuestion("New question").setAnswers(Set.of("new answer")).setWeight(1).build();

        this.register.replace(oldQuestion, newQuestion);

        Mockito.verify(questionsNode).setList(Mockito.eq(Question.class), Mockito.argThat(questions ->
                questions != null && questions.contains(newQuestion) && !questions.contains(oldQuestion)));
        Mockito.verify(this.configLoader).save(rootNode);
    }

    @Test
    void replaceQuestionNotInList() throws IOException {
        final CommentedConfigurationNode rootNode = Mockito.mock(CommentedConfigurationNode.class);
        final CommentedConfigurationNode questionsNode = Mockito.mock(CommentedConfigurationNode.class);
        Mockito.when(this.configLoader.load()).thenReturn(rootNode);
        Mockito.when(rootNode.node("questions")).thenReturn(questionsNode);
        final Question oldQuestion = Question.builder().setQuestion("Old question").setAnswers(Set.of("answer")).setWeight(1).build();
        Mockito.when(questionsNode.getList(Question.class)).thenReturn(List.of());
        final Question newQuestion = Question.builder().setQuestion("New question").setAnswers(Set.of("new answer")).setWeight(1).build();

        this.register.replace(oldQuestion, newQuestion);

        Mockito.verify(questionsNode).setList(Mockito.eq(Question.class), Mockito.argThat(questions ->
                questions != null && questions.contains(newQuestion) && !questions.contains(oldQuestion)));
        Mockito.verify(this.configLoader).save(rootNode);
    }

    @Test
    void replaceQuestionSerializationException() throws IOException {
        final CommentedConfigurationNode rootNode = Mockito.mock(CommentedConfigurationNode.class);
        final CommentedConfigurationNode questionsNode = Mockito.mock(CommentedConfigurationNode.class);
        Mockito.when(this.configLoader.load()).thenReturn(rootNode);
        Mockito.when(rootNode.node("questions")).thenReturn(questionsNode);
        final Question question = Mockito.mock(Question.class);
        Mockito.when(questionsNode.getList(Question.class)).thenThrow(new SerializationException("Serialization error"));

        Assertions.assertThrows(SerializationException.class, () -> this.register.replace(question, question));

        Mockito.verify(this.logger).error(Mockito.contains("not replaced because an error occurred when serializing it"), Mockito.any(SerializationException.class));
    }

    @Test
    void replaceQuestionConfigurateException() throws IOException {
        final CommentedConfigurationNode rootNode = Mockito.mock(CommentedConfigurationNode.class);
        final CommentedConfigurationNode questionsNode = Mockito.mock(CommentedConfigurationNode.class);
        Mockito.when(this.configLoader.load()).thenReturn(rootNode);
        Mockito.when(rootNode.node("questions")).thenReturn(questionsNode);
        final Question question = Mockito.mock(Question.class);
        Mockito.doThrow(new ConfigurateException("IO error")).when(this.configLoader).save(rootNode);

        Assertions.assertThrows(ConfigurateException.class, () -> this.register.replace(question, question));

        Mockito.verify(this.logger).error(Mockito.contains("not replaced because an error occurred when saving it to the config file"), Mockito.any(IOException.class));
    }

}