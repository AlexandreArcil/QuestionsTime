package fr.canardnocturne.questionstime.command.set.question.tags;

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

public class SetQuestionTagsListExecutor implements CommandExecutor {

    private final Parameter.Value<Question> specificQuestionParameter;

    public SetQuestionTagsListExecutor(final Parameter.Value<Question> specificQuestionParameter) {
        this.specificQuestionParameter = specificQuestionParameter;
    }

    @Override
    public CommandResult execute(CommandContext context) throws CommandException {
        final Question question = context.requireOne(specificQuestionParameter);
        final TextComponent.Builder message = Component.text();
        if(question.getTags().isEmpty()) {
            message.append(TextUtils.normalWithPrefix("There are no tags for this question."));
        } else {
            message.append(TextUtils.normalWithPrefix("Tags: "))
                    .appendNewline()
                    .append(Component.join(JoinConfiguration.newlines(), question.getTags().stream().map(tag ->
                            QuestionsTime.PREFIX.append(Component.text("[X]", NamedTextColor.RED, TextDecoration.BOLD)
                                            .clickEvent(ClickEvent.runCommand("/qt set question \"" + question.getQuestion() + "\" tags remove " + tag))
                                            .hoverEvent(HoverEvent.showText(Component.text("Delete the tag '" + tag + "'"))))
                                    .append(TextUtils.composedWithoutPrefix(" ", tag))
                    ).toList()));
        }
        message.appendNewline()
                .append(TextUtils.normalWithPrefix("You can add tags with the command "))
                .append(TextUtils.commandSuggestion("set question \"" + question.getQuestion() + "\" tags add tag1[;tag2;...]"));
        context.sendMessage(message.build());
        return CommandResult.success();
    }

}
