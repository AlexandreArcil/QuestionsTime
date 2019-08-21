package fr.canardnocturne.questionstime.question.ask.pool;

import fr.canardnocturne.questionstime.question.type.Question;

import java.util.*;

public class WeightSortedQuestionPool implements QuestionPool {

    private final Set<Question> weightTree;

    public WeightSortedQuestionPool(final Collection<Question> questions) {
        this.weightTree = new TreeSet<>(Comparator.comparingInt(Question::getWeight));
        this.weightTree.addAll(questions);
    }

    @Override
    public void add(final Question question) {
        this.weightTree.add(question);
    }

    @Override
    public Collection<Question> getAll() {
        return Collections.unmodifiableSet(weightTree);
    }
}
