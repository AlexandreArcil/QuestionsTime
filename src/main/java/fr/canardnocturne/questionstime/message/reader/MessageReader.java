package fr.canardnocturne.questionstime.message.reader;

import java.nio.file.Path;
import java.util.Map;

public interface MessageReader {

    Map<String, String> readMessages(Path path);

}
