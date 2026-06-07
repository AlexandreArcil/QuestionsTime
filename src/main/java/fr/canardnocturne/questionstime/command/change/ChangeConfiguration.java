package fr.canardnocturne.questionstime.command.change;

import fr.canardnocturne.questionstime.config.QuestionTimeConfiguration;

public interface ChangeConfiguration {

    void change(QuestionTimeConfiguration configuration, Config config, String value);

}
