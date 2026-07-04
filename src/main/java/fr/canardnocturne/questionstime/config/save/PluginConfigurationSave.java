package fr.canardnocturne.questionstime.config.save;

import fr.canardnocturne.questionstime.config.QuestionTimeConfiguration;

import java.io.IOException;

public interface PluginConfigurationSave {

    void save(final QuestionTimeConfiguration configuration) throws IOException;

}
