package fr.canardnocturne.questionstime.message.reader;

import org.apache.logging.log4j.LogManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;

public class HoconMessageReaderTest {

    @Test
    public void testInvalidFile() {
        final HoconMessageReader reader = new HoconMessageReader(LogManager.getLogger());
        final Path path = Path.of("invalid_file.conf");
        final Map<String, String> result = reader.readMessages(path);
        Assertions.assertEquals(Collections.emptyMap(), result);
    }

    @Test
    public void testVisitor() throws URISyntaxException {
        final HoconMessageReader reader = new HoconMessageReader(LogManager.getLogger());
        final Path path = Path.of(this.getClass().getClassLoader().getResource("./message.conf").toURI());
        final Map<String, String> result = reader.readMessages(path);
        Assertions.assertEquals(Map.of("test.test", "test"), result);
    }

}
