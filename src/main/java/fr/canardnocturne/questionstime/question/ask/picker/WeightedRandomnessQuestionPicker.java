package fr.canardnocturne.questionstime.question.ask.picker;

import fr.canardnocturne.questionstime.question.Question;
import fr.canardnocturne.questionstime.question.ask.pool.QuestionPool;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.Random;

public class WeightedRandomnessQuestionPicker extends QuestionPicker {

    private final Logger logger;

    public WeightedRandomnessQuestionPicker(final QuestionPool questionPool, final Logger logger) {
        super(questionPool);
        this.logger = logger;
    }

    @Override
    public Question pick(final Collection<String> tags) {
        final Random rand = new Random();
        final Collection<Question> questions = this.questionPool.getAll().stream()
                .filter(question -> tags.isEmpty() || question.getTags().stream().anyMatch(tags::contains))
                .toList();
        if(questions.isEmpty()) {
            throw new IllegalArgumentException("No question found with tags: " + String.join(", ", tags));
        }
        Question chosenQuestion = null;

        final int totalWeight = questions.stream().mapToInt(Question::getWeight).sum();
        logger.debug("totalWeight: " + totalWeight);
        int weight = rand.nextInt(totalWeight);
        logger.debug("random weight: " + weight);
        for (final Question registeredQuestion : questions) {
            logger.debug("question weight: " + registeredQuestion.getWeight());
            weight -= registeredQuestion.getWeight();
            logger.debug("new weight value: " + weight);
            if (weight < 0) {
                chosenQuestion = registeredQuestion;
                logger.debug("question chosen: '" + registeredQuestion.getQuestion() + "'");
                break;
            }
        }
        if (chosenQuestion == null) {
            throw new IllegalStateException("No question chosen. It's not normal, please report with debug.log and config.conf files");
        }
        return chosenQuestion;
    }
}
