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
        HoconMessageReader reader = new HoconMessageReader(LogManager.getLogger());
        Path path = Path.of("invalid_file.conf");
        Map<String, String> result = reader.readMessages(path);
        Assertions.assertEquals(Collections.emptyMap(), result);
    }

    @Test
    public void testVisitor() throws URISyntaxException {
        HoconMessageReader reader = new HoconMessageReader(LogManager.getLogger());
        Path path = Path.of(this.getClass().getClassLoader().getResource("./message.conf").toURI());
        Map<String, String> result = reader.readMessages(path);
        Assertions.assertEquals(Map.of("test.test", "test"), result);
    }

}
