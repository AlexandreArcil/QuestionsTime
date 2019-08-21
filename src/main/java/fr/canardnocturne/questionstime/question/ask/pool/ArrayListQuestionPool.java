package fr.canardnocturne.questionstime.question.ask.pool;

import fr.canardnocturne.questionstime.question.type.Question;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

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
    public Collection<Question> getAll() {
        return Collections.unmodifiableList(questions);
    }
}