package fr.canardnocturne.questionstime.question.save;

import fr.canardnocturne.questionstime.question.type.Question;

import java.io.IOException;

public interface QuestionRegister {

    void register(Question question) throws IOException;

}
