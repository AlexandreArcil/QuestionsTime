package fr.canardnocturne.questionstime.question.creation.steps;

import fr.canardnocturne.questionstime.question.component.OutcomeCommand;
import fr.canardnocturne.questionstime.question.creation.QuestionCreator;
import fr.canardnocturne.questionstime.question.serializer.OutcomeCommandSerializer;
import fr.canardnocturne.questionstime.util.NumberUtils;
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
import java.util.Map;

public class PrizeCommandsStep implements CreationStep {

    public static final CreationStep INSTANCE = new PrizeCommandsStep();

    private static final String COMMAND_FORMAT = "[message];[command];{position}";

    @Override
    public Component question() {
        return TextUtils.composed("Execute commands as prize with ", "/qtc add " + COMMAND_FORMAT)
                .appendNewline().append(TextUtils.composed("List prizes with ")).append(TextUtils.commandShortcut("list"))
                .appendNewline().append(TextUtils.normalWithPrefix("Remove prizes with ")).append(TextUtils.commandShortcut("list"))
                .append(TextUtils.composedWithoutPrefix(" then clicking on the ", "[X]", " icon"))
                .appendNewline().append(TextUtils.composed("The ", "message", " is displayed to the players when announcing the prizes"))
                .appendNewline().append(TextUtils.composed("The ", "command", " is executed on the player who find the answer, specified with ", "@winner"))
                .appendNewline().append(TextUtils.composed("And ", "position", " is the winner position, default is the first"))
                .appendNewline().append(TextUtils.example("/qtc Teleport to the Dancing Room;tp @winner 0 0 0"))
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
                    this.addCommandPrize(sender, questionCreator, args[1]);
                }
                break;
            case "list":
                this.listCommandPrizes(questionCreator, sender);
                break;
            case "del":
                if (args.length < 2) {
                    sender.sendMessage(TextUtils.normalWithPrefix("You must provide a command to remove"));
                } else {
                    this.removeCommandPrize(sender, questionCreator, args[1]);
                }
                break;
            default:
                sender.sendMessage(TextUtils.normalWithPrefix("Unknown command: " + args[0]));
                break;
        }
        return false;
    }

    private void addCommandPrize(final Audience sender, final QuestionCreator questionCreator, final String answer) {
        try {
            final TextUtils.AnswerPosition answerPosition = TextUtils.extractPositionFromAnswer(answer);
            final int position = answerPosition.position();
            if(position <= 0) {
                sender.sendMessage(TextUtils.specialWithPrefix(String.valueOf(position))
                        .append(TextUtils.normal(" is not a positive number")));
                return;
            }

            final OutcomeCommand prizeCommand = OutcomeCommandSerializer.deserialize(answerPosition.answer());
            questionCreator.addCommandPrize(position, prizeCommand);
            sender.sendMessage(TextUtils.normalWithPrefix("Added ")
                    .append(prizeCommand.format())
                    .append(TextUtils.composedWithoutPrefix(" command as prize for the ", NumberUtils.toOrdinal(position), " winner")));
        } catch (final IllegalArgumentException e) {
            sender.sendMessage(TextUtils.composed("The command prize ", answer, " doesn't follow the syntax ", COMMAND_FORMAT));
        }
    }

    private void listCommandPrizes(final QuestionCreator questionCreator, final Audience sender) {
        final Map<Integer, List<OutcomeCommand>> commandsPrize = questionCreator.getCommandsPrize();
        if (commandsPrize.isEmpty()) {
            sender.sendMessage(TextUtils.normalWithPrefix("No command prizes added yet."));
            return;
        }
        final TextComponent.Builder listCommandPrizesBuilder = Component.text().append(TextUtils.normalWithPrefix("Command prizes:")).appendNewline();
        listCommandPrizesBuilder.append(Component.join(JoinConfiguration.newlines(), commandsPrize.entrySet().stream().map(entry -> {
            final int position = entry.getKey();
            final List<OutcomeCommand> commands = entry.getValue();
            return Component.text()
                    .append(TextUtils.specialWithPrefix(NumberUtils.toOrdinal(position)))
                    .append(TextUtils.normal(": "))
                    .appendNewline()
                    .append(Component.join(JoinConfiguration.newlines(),
                            commands.stream().map(prizeCommand ->
                                TextUtils.normalWithPrefix("- ")
                                    .append(prizeCommand.format())
                                    .appendSpace()
                                    .append(Component.text("[X]", NamedTextColor.RED, TextDecoration.BOLD)
                                            .hoverEvent(HoverEvent.showText(TextUtils.normal("Click to remove this command prize")))
                                            .clickEvent(ClickEvent.runCommand("/qtc del " + OutcomeCommandSerializer.serialize(prizeCommand) + ";" + position))))
                                    .toList()))
                    .build();
        }).toList()));
        sender.sendMessage(listCommandPrizesBuilder.build());
    }

    private void removeCommandPrize(final Audience sender, final QuestionCreator questionCreator, final String answer) {
        try {
            final TextUtils.AnswerPosition answerPosition = TextUtils.extractPositionFromAnswer(answer);
            final int position = answerPosition.position();
            if(position <= 0) {
                sender.sendMessage(TextUtils.specialWithPrefix(String.valueOf(position))
                        .append(TextUtils.normal(" is not a positive number")));
                return;
            }

            final OutcomeCommand prizeCommand = OutcomeCommandSerializer.deserialize(answerPosition.answer());
            final boolean removed = questionCreator.removeCommandPrize(position, prizeCommand);
            if (!removed) {
                sender.sendMessage(TextUtils.normalWithPrefix("No command prize ")
                        .append(prizeCommand.format())
                        .append(TextUtils.normal(" found for the position "))
                        .append(TextUtils.special(String.valueOf(position))));
            } else {
                sender.sendMessage(TextUtils.normalWithPrefix("Removed command prize ")
                        .append(prizeCommand.format())
                        .append(TextUtils.composedWithoutPrefix(" for the ", NumberUtils.toOrdinal(position), " winner.")));
            }
        } catch (final IllegalArgumentException e) {
            sender.sendMessage(TextUtils.composed("The command prize ", answer, " doesn't follow the syntax ", COMMAND_FORMAT));
        }
    }

    @Override
    public boolean shouldSkip(final QuestionCreator questionCreator) {
        return false;
    }

    @Override
    public Step next(final QuestionCreator questionCreator) {
        return PrizeMoneyAmountStep.INSTANCE;
    }
}
