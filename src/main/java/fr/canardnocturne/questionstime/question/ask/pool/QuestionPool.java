package fr.canardnocturne.questionstime.question.ask.pool;

import fr.canardnocturne.questionstime.question.type.Question;

import java.util.Collection;
import java.util.Optional;

public interface QuestionPool {

    void add(final Question question);

    Optional<Question> get(final String question);

    Collection<Question> getAll();

}
