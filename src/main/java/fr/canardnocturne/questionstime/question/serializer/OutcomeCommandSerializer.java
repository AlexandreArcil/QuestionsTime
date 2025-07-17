package fr.canardnocturne.questionstime.question.serializer;

import fr.canardnocturne.questionstime.question.component.OutcomeCommand;
import org.apache.commons.lang3.StringUtils;

public class OutcomeCommandSerializer {

    private OutcomeCommandSerializer() {}

    /**
     * Convert a String that follow the syntax "[message];[command]" to a OutcomeCommand
     * @param outcomeCommand The String to convert
     * @return A OutcomeCommand representation of the String
     * @throws IllegalArgumentException if the String doesn't contain a message and a command
     */
    public static OutcomeCommand deserialize(final String outcomeCommand) {
        final String[] commandParts = outcomeCommand.split(";", 2);
        if(commandParts.length >= 2 && StringUtils.isNotBlank(commandParts[0]) && StringUtils.isNotBlank(commandParts[1])) {
            final String command = commandParts[1];
            final String fixedCommand = command.startsWith("/") ? command.substring(1) : command;
            return new OutcomeCommand(commandParts[0], fixedCommand);
        } else {
            throw new IllegalArgumentException("The outcome command '"+outcomeCommand+"' doesn't have a message or a command");
        }
    }

    /**
     * Convert a OutcomeCommand to a String following the syntax "[message];[command]"
     * @param outcomeCommand The OutcomeCommand to convert
     * @return A String representation of the OutcomeCommand
     */
    public static String serialize(final OutcomeCommand outcomeCommand) {
        return outcomeCommand.message() + ";" + outcomeCommand.command();
    }

}
