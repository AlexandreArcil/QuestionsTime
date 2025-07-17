package fr.canardnocturne.questionstime.question.creation.steps;

import fr.canardnocturne.questionstime.question.component.OutcomeCommand;
import fr.canardnocturne.questionstime.question.creation.QuestionCreator;
import fr.canardnocturne.questionstime.question.serializer.OutcomeCommandSerializer;
import fr.canardnocturne.questionstime.util.TextUtils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.List;

public class MalusCommandsStep implements CreationStep {

    public static MalusCommandsStep INSTANCE = new MalusCommandsStep();

    private final static String COMMAND_FORMAT = "[message];[command]";

    @Override
    public Component question() {
        return TextUtils.composed("Execute commands as malus with ", "/qtc add " + COMMAND_FORMAT)
                .appendNewline().append(TextUtils.composed("List commands with ")).append(TextUtils.commandShortcut("list"))
                .appendNewline().append(TextUtils.normalWithPrefix("Remove commands with ")).append(TextUtils.commandShortcut("list"))
                .append(TextUtils.composedWithoutPrefix(" then clicking on the ", "[X]", " icon"))
                .appendNewline().append(TextUtils.composed("The ", "message", " is displayed to the players when announcing the malus"))
                .appendNewline().append(TextUtils.composed("The ", "command", " is executed on the player who give a wrong answer, specified with ", "@loser"))
                .appendNewline().append(TextUtils.example("/qtc add Teleport to the Hell;tp @loser 0 -64 0"))
                .appendNewline().append(TextUtils.normalWithPrefix("To go to the next step or skip this step, type "))
                .append(TextUtils.commandShortcut("confirm"));
    }

    @Override
    public boolean handle(final Audience sender, final String answer, final QuestionCreator questionCreator) {
        if("confirm".equals(answer)) {
            return true;
        }

        final String[] args = answer.split(" ", 2);
        switch (args[0]) {
            case "add":
                if (args.length < 2) {
                    sender.sendMessage(TextUtils.normalWithPrefix("You must provide a command to add"));
                } else {
                    this.addCommandMalus(sender, questionCreator, args[1]);
                }
                break;
            case "list":
                this.listCommandMalus(questionCreator, sender);
                break;
            case "del":
                if (args.length < 2) {
                    sender.sendMessage(TextUtils.normalWithPrefix("You must provide a command to remove"));
                } else {
                    this.removeCommandMalus(sender, questionCreator, args[1]);
                }
                break;
            default:
                sender.sendMessage(TextUtils.normalWithPrefix("Unknown command: " + args[0]));
                break;
        }
        return false;
    }

    private void addCommandMalus(final Audience sender, final QuestionCreator questionCreator, final String answer) {
        try {
            final OutcomeCommand outcomeCommand = OutcomeCommandSerializer.deserialize(answer);
            questionCreator.getCommandsMalus().add(outcomeCommand);
            sender.sendMessage(TextUtils.normalWithPrefix("Added ")
                    .append(outcomeCommand.format())
                    .append(TextUtils.composedWithoutPrefix(" command as malus")));
        } catch (final IllegalArgumentException e) {
            sender.sendMessage(TextUtils.composed("The command malus ", answer, " doesn't follow the syntax ", COMMAND_FORMAT));
        }
    }

    private void listCommandMalus(final QuestionCreator questionCreator, final Audience sender) {
        final List<OutcomeCommand> commandsMalus = questionCreator.getCommandsMalus();
        if (commandsMalus.isEmpty()) {
            sender.sendMessage(TextUtils.normalWithPrefix("No command malus added yet."));
            return;
        }
        final TextComponent.Builder listCommandMalusBuilder = Component.text().append(TextUtils.normalWithPrefix("Command malus:")).appendNewline();
        listCommandMalusBuilder.append(Component.join(JoinConfiguration.newlines(), commandsMalus.stream().map(outcomeCommand -> 
                TextUtils.normalWithPrefix("- ")
                   .append(outcomeCommand.format())
                   .appendSpace()
                   .append(Component.text("[X]", NamedTextColor.RED, TextDecoration.BOLD)
                   .hoverEvent(HoverEvent.showText(TextUtils.normal("Click to remove this command malus")))
                   .clickEvent(ClickEvent.runCommand("/qtc del " + OutcomeCommandSerializer.serialize(outcomeCommand))))).toList()));
        sender.sendMessage(listCommandMalusBuilder.build());
    }

    private void removeCommandMalus(final Audience sender, final QuestionCreator questionCreator, final String answer) {
        try {
            final OutcomeCommand outcomeCommand = OutcomeCommandSerializer.deserialize(answer);
            final boolean removed = questionCreator.getCommandsMalus().remove(outcomeCommand);
            if (removed) {
                sender.sendMessage(TextUtils.normalWithPrefix("Removed command prize ")
                        .append(outcomeCommand.format()));
            } else {
                sender.sendMessage(TextUtils.normalWithPrefix("No command malus ")
                        .append(outcomeCommand.format()));
            }
        } catch (final IllegalArgumentException e) {
            sender.sendMessage(TextUtils.composed("The command malus ", answer, " doesn't follow the syntax ", COMMAND_FORMAT));
        }
    }

    @Override
    public boolean shouldSkip(final QuestionCreator questionCreator) {
        return false;
    }

    @Override
    public Step next(final QuestionCreator questionCreator) {
        return AnnounceMalusStep.INSTANCE;
    }
}
