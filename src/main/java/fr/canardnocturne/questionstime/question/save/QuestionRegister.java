package fr.canardnocturne.questionstime.question.save;

import fr.canardnocturne.questionstime.question.Question;

import java.io.IOException;

public interface QuestionRegister {

    void register(Question question) throws IOException;

    void update(Question question) throws IOException;

}
