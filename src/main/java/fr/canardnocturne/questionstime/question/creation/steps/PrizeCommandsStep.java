package fr.canardnocturne.questionstime.question.creation.steps;

import fr.canardnocturne.questionstime.question.component.PrizeCommand;
import fr.canardnocturne.questionstime.question.creation.QuestionCreator;
import fr.canardnocturne.questionstime.question.serializer.PrizeCommandSerializer;
import fr.canardnocturne.questionstime.util.TextUtils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class PrizeCommandsStep implements CreationStep {

    public static final CreationStep INSTANCE = new PrizeCommandsStep();

    private static final String COMMAND_FORMAT = "[message];[command]";

    @Override
    public Component question() {
        return TextUtils.composed("Execute commands as prize with ", "/qtc " + COMMAND_FORMAT)
                .appendNewline().append(TextUtils.composed("The ", "message", " is displayed to the players when announcing the prizes"))
                .appendNewline().append(TextUtils.composed("And ", "command", " is executed on the player who find the answer, specified with ", "@winner"))
                .appendNewline().append(TextUtils.example("/qtc Teleport to the Dancing Room;tp @winner 0 0 0"))
                .appendNewline().append(TextUtils.normalWithPrefix("To go to the next step or skip this step, type "))
                .append(TextUtils.commandShortcut("confirm"));
    }

    @Override
    public boolean handle(Audience sender, String answer, QuestionCreator questionCreator) {
        if("confirm".equals(answer)) {
            return true;
        }

        try {
            PrizeCommand prizeCommand = PrizeCommandSerializer.deserialize(answer);
            questionCreator.addCommandPrize(prizeCommand);
            sender.sendMessage(TextUtils.normalWithPrefix("Added ")
                    .append(Component.text(prizeCommand.message(), NamedTextColor.BLUE, TextDecoration.UNDERLINED)
                            .hoverEvent(HoverEvent.showText(Component.text(prizeCommand.command()))))
                    .append(TextUtils.normal(" command as prize")));
        } catch (IllegalArgumentException e) {
            sender.sendMessage(TextUtils.composed("The command prize ", answer, " doesn't follow the syntax ", COMMAND_FORMAT));
        }
        return false;
    }

    @Override
    public boolean shouldSkip(QuestionCreator questionCreator) {
        return false;
    }

    @Override
    public CreationStep next(QuestionCreator questionCreator) {
        return PrizeMoneyAmountStep.INSTANCE;
    }
}
