package fr.canardnocturne.questionstime.command.set.question.tmp.qc2;

import fr.canardnocturne.questionstime.QuestionsTime;
import fr.canardnocturne.questionstime.util.TextUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;

import java.util.Arrays;

public class SetQuestionListArrayComponentComplexExecutor<B, T, V> {

    protected final QuestionComponentComplexArray<B, T, V> questionComponent;

    public SetQuestionListArrayComponentComplexExecutor(final QuestionComponentComplexArray<B, T, V> questionComponent) {
        this.questionComponent = questionComponent;
    }

    public CommandResult execute(final CommandContext context, final String question, final V[] componentValues) throws CommandException {
        final String componentNamePlural = this.questionComponent.getPlural();
        final String componentName = this.questionComponent.getSingular();
        final TextComponent.Builder message = Component.text();
        if(componentValues.length == 0) {
            message.append(TextUtils.normalWithPrefix("There is no " + componentNamePlural + " for this question"));
        } else {
            message.append(TextUtils.normalWithPrefix(StringUtils.capitalize(componentNamePlural) + ": "))
                    .appendNewline()
                    .append(Component.join(JoinConfiguration.newlines(), Arrays.stream(componentValues).map(componentValue -> {
                                final String value = this.questionComponent.getValueUnmapper().apply(componentValue);
                                final String valueComponentCommand = TextUtils.shouldBeDoubleQuote(value) ? "\"" + value + "\"" : value;
                                return QuestionsTime.PREFIX.append(Component.text("[X]", NamedTextColor.RED, TextDecoration.BOLD)
                                                .clickEvent(ClickEvent.runCommand("/qt set question \"" + question + "\" " + componentNamePlural + " remove " + valueComponentCommand))
                                                .hoverEvent(HoverEvent.showText(Component.text("Delete the " + this.questionComponent.getSingular() + " '" + value + "'"))))
                                        .append(TextUtils.composedWithoutPrefix(" ", value));
                            }
                    ).toList()));
            message.appendNewline().append(TextUtils.normalWithPrefix("You can add " + componentNamePlural + " with the command ")
                    .append(TextUtils.commandSuggestion("set question \"" + question + "\" " + componentNamePlural + " add "
                            + componentName + "1 " + componentName + "2 " + componentName + "3...")));
        }
        context.sendMessage(message.build());
        return CommandResult.success();
    }

}
