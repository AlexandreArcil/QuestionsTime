package fr.canardnocturne.questionstime.question.ask.pool;

import fr.canardnocturne.questionstime.question.type.Question;

import java.util.*;

public class WeightSortedQuestionPool implements QuestionPool {

    private final Set<Question> weightTree;

    public WeightSortedQuestionPool(final Collection<Question> questions) {
        this.weightTree = new TreeSet<>(Comparator.comparingInt(Question::getWeight)
                .thenComparing(Question::getQuestion));
        this.weightTree.addAll(questions);
    }

    @Override
    public void add(final Question question) {
        this.weightTree.add(question);
    }

    @Override
    public Optional<Question> get(final String question) {
        return this.weightTree.stream()
                .filter(registeredQuestion -> registeredQuestion.getQuestion().equals(question))
                .findFirst();
    }

    @Override
    public Collection<Question> getAll() {
        return Collections.unmodifiableSet(weightTree);
    }
}
