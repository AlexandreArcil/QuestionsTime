package fr.canardnocturne.questionstime.question.serializer;

import fr.canardnocturne.questionstime.question.component.PrizeCommand;
import org.apache.commons.lang3.StringUtils;

public class PrizeCommandSerializer {

    private PrizeCommandSerializer() {}

    /**
     * Convert a String that follow the syntax "[message];[command]" to a PrizeCommand
     * @param prizeCommand The String to convert
     * @return A PrizeCommand representation of the String
     * @throws IllegalArgumentException if the String doesn't contain a message and a command
     */
    public static PrizeCommand deserialize(String prizeCommand) {
        final String[] commandParts = prizeCommand.split(";", 2);
        if(commandParts.length >= 2 && StringUtils.isNotBlank(commandParts[0]) && StringUtils.isNotBlank(commandParts[1])) {
            String command = commandParts[1];
            String fixedCommand = command.startsWith("/") ? command.substring(1) : command;
            return new PrizeCommand(commandParts[0], fixedCommand);
        } else {
            throw new IllegalArgumentException("The command prize '"+prizeCommand+"' doesn't have a message or a command");
        }
    }

    /**
     * Convert a PrizeCommand to a String following the syntax "[message];[command]"
     * @param prizeCommand The PrizeCommand to convert
     * @return A String representation of the PrizeCommand
     */
    public static String serialize(PrizeCommand prizeCommand) {
        return prizeCommand.message() + ";" + prizeCommand.command();
    }

}
