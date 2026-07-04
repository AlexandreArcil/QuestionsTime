package fr.canardnocturne.questionstime.command.set.question.propositions;

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

public class SetQuestionPropositionsListExecutor implements CommandExecutor {

    private final Parameter.Value<Question> specificQuestionParameter;

    public SetQuestionPropositionsListExecutor(final Parameter.Value<Question> specificQuestionParameter) {
        this.specificQuestionParameter = specificQuestionParameter;
    }

    @Override
    public CommandResult execute(final CommandContext context) throws CommandException {
        final Question question = context.requireOne(specificQuestionParameter);
        final TextComponent.Builder message = Component.text();
        if(question.getPropositions().isEmpty()) {
            message.append(TextUtils.normalWithPrefix("There are no propositions for this question."));
        } else {
            message.append(TextUtils.normalWithPrefix("Propositions: "))
                    .appendNewline()
                    .append(Component.join(JoinConfiguration.newlines(), question.getPropositions().stream().map(proposition ->
                    QuestionsTime.PREFIX.append(Component.text("[X]", NamedTextColor.RED, TextDecoration.BOLD)
                                    .clickEvent(ClickEvent.runCommand("/qt set question \"" + question.getQuestion() + "\" propositions remove " + proposition))
                                    .hoverEvent(HoverEvent.showText(Component.text("Delete the proposition '" + proposition + "'"))))
                            .append(TextUtils.composedWithoutPrefix(" ", proposition))
            ).toList()));
        }
        message.appendNewline()
                .append(TextUtils.normalWithPrefix("You can add propositions with the command "))
                .append(TextUtils.commandSuggestion("set question \"" + question.getQuestion() + "\" propositions add proposition1[;proposition2;...]"));
        context.sendMessage(message.build());
        return CommandResult.success();
    }
}
