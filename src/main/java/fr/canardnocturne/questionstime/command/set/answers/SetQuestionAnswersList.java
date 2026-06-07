package fr.canardnocturne.questionstime.command.set.answers;

import fr.canardnocturne.questionstime.QuestionsTime;
import fr.canardnocturne.questionstime.question.Question;
import fr.canardnocturne.questionstime.util.TextUtils;
import net.kyori.adventure.text.Component;
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

public class SetQuestionAnswersList implements CommandExecutor {

    private final Parameter.Value<Question> specificQuestionParameter;

    public SetQuestionAnswersList(final Parameter.Value<Question> specificQuestionParameter) {
        this.specificQuestionParameter = specificQuestionParameter;
    }

    @Override
    public CommandResult execute(final CommandContext context) throws CommandException {
        final Question question = context.requireOne(specificQuestionParameter);
        final TextComponent.Builder message = Component.text().append(TextUtils.normalWithPrefix("Answers: ")).appendNewline();
        int position = 1;
        for (final String answer : question.getAnswers()) {
            message.append(QuestionsTime.PREFIX.append(Component.text("[X]", NamedTextColor.RED, TextDecoration.BOLD)
                            .clickEvent(ClickEvent.runCommand("/qtc set " + question.getQuestion() + " answers remove " + answer))
                            .hoverEvent(HoverEvent.showText(Component.text("Delete the answer '" + answer + "'"))))
                    .append(TextUtils.composedWithoutPrefix(" ", (position) + "] ", answer)))
                    .appendNewline();
            position++;
        }
        context.sendMessage(message.build());
        return CommandResult.success();
    }
}
