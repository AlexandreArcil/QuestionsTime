package fr.canardnocturne.questionstime.question.save;

import fr.canardnocturne.questionstime.question.type.Question;
import org.apache.logging.log4j.Logger;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.serialize.SerializationException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class HoconQuestionRegister implements QuestionRegister {

    private final ConfigurationLoader<CommentedConfigurationNode> configLoader;
    private final Logger logger;

    private CommentedConfigurationNode questionsNode;

    public HoconQuestionRegister(final ConfigurationLoader<CommentedConfigurationNode> configLoader, final Logger logger) {
        this.configLoader = configLoader;
        this.logger = logger;
    }

    @Override
    public void register(final Question question) throws IOException {
        final CommentedConfigurationNode rootNode = this.getQuestionsNode();
        try {
            final CommentedConfigurationNode questionsNode = rootNode.node("questions");
            final List<Question> questionsInNode = Optional.ofNullable(questionsNode.getList(Question.class)).orElse(Collections.emptyList());
            final List<Question> questions = new ArrayList<>(questionsInNode);
            questions.add(question);
            questionsNode.setList(Question.class, questions);
            this.configLoader.save(rootNode);
        } catch (final SerializationException e) {
            logger.error("Question '" + question.getQuestion() + "' not registered because an error occurred when serializing it", e);
            logger.debug(question);
            throw e;
        } catch (final ConfigurateException e) {
            logger.error("Question '" + question.getQuestion() + "' not registered because an error occurred when saving it to the config file", e);
            logger.debug(question);
            throw e;
        }
    }

    private CommentedConfigurationNode getQuestionsNode() {
        if (this.questionsNode == null) {
            try {
                this.questionsNode = this.configLoader.load();
                return this.configLoader.load();
            } catch (final IOException e) {
                logger.error("Unable to load the config file ", e);
                throw new IllegalStateException(e);
            }
        }
        return this.questionsNode;
    }
}
