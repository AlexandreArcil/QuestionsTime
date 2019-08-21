package fr.canardnocturne.questionstime.question.ask.pool;

import fr.canardnocturne.questionstime.question.type.Question;

import java.util.Collection;

public interface QuestionPool {

    void add(final Question question);

    Collection<Question> getAll();

}
