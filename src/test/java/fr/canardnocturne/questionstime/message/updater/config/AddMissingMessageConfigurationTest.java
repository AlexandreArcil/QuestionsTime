package fr.canardnocturne.questionstime.message.updater.config;

import fr.canardnocturne.questionstime.message.Messages;
import fr.canardnocturne.questionstime.message.SimpleMessage;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.CleanupMode;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

class AddMissingMessageConfigurationTest {

    @Test
    void allMessagesAreAdded(@TempDir(cleanup = CleanupMode.NEVER) final Path tmpDir) throws IOException {
        final Map<String , String> messagesRead = Collections.emptyMap();
        final Path path = tmpDir.resolve("empty_messages.conf");

        final Logger logger = org.mockito.Mockito.mock(Logger.class);
        final AddMissingMessageConfiguration updater = new AddMissingMessageConfiguration(logger);
        updater.updateConfig(messagesRead, path);

        final int messageWrittenCount = Files.readAllLines(path).size();
        Assertions.assertEquals(Messages.registeredMessagesCount(), messageWrittenCount);
    }

    @Test
    void onlyMissingMessagesAreAdded(@TempDir(cleanup = CleanupMode.NEVER) final Path tmpDir) throws IOException {
        final Map<String , String> messagesRead = Messages.getAll().stream()
                .collect(Collectors.toMap(SimpleMessage::getSection, SimpleMessage::getMessage));
        messagesRead.remove(Messages.ANSWER_ANNOUNCE.getSection());
        final Path path = tmpDir.resolve("partial_messages.conf");
        Files.write(path, messagesRead.entrySet().stream()
                .map(entry -> "\"" + entry.getKey() + "\"=\"" + entry.getValue() + "\"")
                .collect(Collectors.toList()));

        final Logger logger = org.mockito.Mockito.mock(Logger.class);
        final AddMissingMessageConfiguration updater = new AddMissingMessageConfiguration(logger);
        updater.updateConfig(messagesRead, path);

        final int messageWrittenCount = Files.readAllLines(path).size();
        Assertions.assertEquals(Messages.registeredMessagesCount(), messageWrittenCount);
    }

}