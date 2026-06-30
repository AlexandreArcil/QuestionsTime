package fr.canardnocturne.questionstime.question.modifier;

import fr.canardnocturne.questionstime.question.Question;
import fr.canardnocturne.questionstime.question.QuestionComponent;

public interface QuestionModifier {

    Question set(final Question question, final QuestionComponent component, final String value);

    Question set(final Question question, final QuestionComponent component, final int value);

    Question set(final Question question, final QuestionComponent component, final boolean value);

    Question set(final Question question, final QuestionComponent component, final int position, final int value);

    Question set(final Question question, final QuestionComponent component, final int position, final boolean value);

    Question add(final Question question, final QuestionComponent component, final String value);

    Question add(final Question question, final QuestionComponent component, final int position, final String value);

    Question remove(final Question question, final QuestionComponent component, final String value);

    Question remove(final Question question, final QuestionComponent component, final int position, final String value);

}
