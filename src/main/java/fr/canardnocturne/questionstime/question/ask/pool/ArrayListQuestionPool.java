package fr.canardnocturne.questionstime.question.ask.pool;

import fr.canardnocturne.questionstime.question.Question;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ArrayListQuestionPool implements QuestionPool {

    private final List<Question> questions;

    public ArrayListQuestionPool(final List<Question> questions) {
        this.questions = questions;
    }

    @Override
    public void add(final Question question) {
        this.questions.add(question);
    }

    @Override
    public void replace(final Question question, final Question newQuestion) {
        final int index = this.questions.indexOf(question);
        if (index == -1) {
            throw new IllegalArgumentException("The question is not in the pool !");
        }
        this.questions.set(index, newQuestion);
    }

    @Override
    public Optional<Question> get(final String question) {
        return this.questions.stream()
                .filter(registeredQuestion -> registeredQuestion.getQuestion().equals(question))
                .findFirst();
    }

    @Override
    public Collection<Question> getAll() {
        return Collections.unmodifiableList(questions);
    }
}
