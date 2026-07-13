package fr.canardnocturne.questionstime.command.set.question.permissions.include;

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

public class SetQuestionIncludePermissionsListExecutor implements CommandExecutor {

    private final Parameter.Value<Question> specificQuestionParameter;

    public SetQuestionIncludePermissionsListExecutor(final Parameter.Value<Question> specificQuestionParameter) {
        this.specificQuestionParameter = specificQuestionParameter;
    }

    @Override
    public CommandResult execute(final CommandContext context) throws CommandException {
        final Question question = context.requireOne(specificQuestionParameter);
        final TextComponent.Builder message = Component.text();
        if(question.getIncludePermissions().isEmpty()) {
            message.append(TextUtils.normalWithPrefix("There are no include permissions for this question."));
        } else {
            message.append(TextUtils.normalWithPrefix("Include permissions: "))
                    .appendNewline()
                    .append(Component.join(JoinConfiguration.newlines(), question.getIncludePermissions().stream().map(permission ->
                            QuestionsTime.PREFIX.append(Component.text("[X]", NamedTextColor.RED, TextDecoration.BOLD)
                                            .clickEvent(ClickEvent.runCommand("/qt set question \"" + question.getQuestion() + "\" include_permissions remove " + permission))
                                            .hoverEvent(HoverEvent.showText(Component.text("Delete the include permission '" + permission + "'"))))
                                    .append(TextUtils.composedWithoutPrefix(" ", permission))
                    ).toList()));
        }
        message.appendNewline()
                .append(TextUtils.normalWithPrefix("You can add include permissions with the command "))
                .append(TextUtils.commandSuggestion("set question \"" + question.getQuestion() + "\" include_permissions add permission1[ permission2 permission3...]"));
        context.sendMessage(message.build());
        return CommandResult.success();
    }

}
