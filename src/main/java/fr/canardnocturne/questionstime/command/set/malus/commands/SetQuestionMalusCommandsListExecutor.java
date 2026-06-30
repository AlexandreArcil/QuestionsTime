package fr.canardnocturne.questionstime.command.set.malus.commands;

import fr.canardnocturne.questionstime.QuestionsTime;
import fr.canardnocturne.questionstime.question.Question;
import fr.canardnocturne.questionstime.util.TextUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;

import java.util.stream.Stream;

public class SetQuestionMalusCommandsListExecutor implements CommandExecutor {

    private final Parameter.Value<Question> specificQuestionParameter;

    public SetQuestionMalusCommandsListExecutor(final Parameter.Value<Question> specificQuestionParameter) {
        this.specificQuestionParameter = specificQuestionParameter;
    }

    @Override
    public CommandResult execute(final CommandContext context) throws CommandException {
        final Question question = context.requireOne(specificQuestionParameter);
        final TextComponent.Builder message = Component.text();
        if (question.getMalus().isEmpty() || question.getMalus().get().getCommands().length == 0) {
            message.append(TextUtils.normalWithPrefix("No commands"));
        } else {
            message.append(TextUtils.normalWithPrefix("Malus commands: ")).appendNewline()
            .append(Component.join(JoinConfiguration.newlines(), Stream.of(question.getMalus().get().getCommands()).map(command ->
                    QuestionsTime.PREFIX.append(Component.text("[X]", NamedTextColor.RED, TextDecoration.BOLD)
                                    .clickEvent(ClickEvent.runCommand("/qt set question \"" + question.getQuestion() + "\" malus commands remove " + command.toString()))
                                    .hoverEvent(HoverEvent.showText(Component.text("Delete the command"))))
                            .appendSpace()
                            .append(command.format())).toList()));
        }
        message.appendNewline()
                        .append(TextUtils.normalWithPrefix("You can add a command with the command "))
                        .append(TextUtils.commandSuggestion("set question \"" + question.getQuestion() + "\" malus commands add message;command"));
        context.sendMessage(message.build());
        return CommandResult.success();
    }
}
