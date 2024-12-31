package fr.canardnocturne.questionstime.message.updater.config;

import java.nio.file.Path;
import java.util.Map;

public interface MessageConfigurationUpdater {

    void updateConfig(final Map<String, String> messagesRead, final Path messagesConfig);

}
