package fr.canardnocturne.questionstime.command.set.question;

import org.spongepowered.api.command.parameter.Parameter;

import java.util.Optional;

public class CharacterParameter {

    public static Parameter.Value<String> create(final String key, final char separator) {
        return Parameter.builder(String.class)
                .addParser((parameterKey, reader, context) -> {
                    final StringBuilder stringBuilder = new StringBuilder();
                    char parsed;
                    do {
                        parsed = reader.parseChar();
                        if(parsed != separator && !Character.isWhitespace(parsed)) {
                            stringBuilder.append(parsed);
                        }
                    } while (parsed != separator && reader.canRead());
                    final String input = stringBuilder.toString();
                    return Optional.of(input);
                })
                .key(key)
                .consumeAllRemaining()
                .build();
    }

}
