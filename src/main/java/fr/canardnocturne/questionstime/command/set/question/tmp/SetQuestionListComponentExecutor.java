package fr.canardnocturne.questionstime.command.set.question.tmp;

import fr.canardnocturne.questionstime.QuestionsTime;
import fr.canardnocturne.questionstime.question.Question;
import fr.canardnocturne.questionstime.question.QuestionComponent;
import fr.canardnocturne.questionstime.util.TextUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;

import java.util.Collection;
import java.util.function.Function;

public class SetQuestionListComponentExecutor implements CommandExecutor {

    private final Parameter.Value<Question> specificQuestionParameter;
    private final Function<Question, Collection<String>> componentExtractor;
    private final QuestionComponent questionComponent;
    private final boolean valueDoubleQuote;

    public SetQuestionListComponentExecutor(final Parameter.Value<Question> specificQuestionParameter,
                                            final Function<Question, Collection<String>> componentExtractor,
                                            final QuestionComponent questionComponent, final boolean valueDoubleQuote) {
        this.specificQuestionParameter = specificQuestionParameter;
        this.componentExtractor = componentExtractor;
        this.questionComponent = questionComponent;
        this.valueDoubleQuote = valueDoubleQuote;
    }

    @Override
    public CommandResult execute(final CommandContext context) throws CommandException {
        final Question question = context.requireOne(specificQuestionParameter);
        final Collection<String> componentValues = this.componentExtractor.apply(question);
        final String componentNamePlural = this.questionComponent.getPlural();
        final String componentName = this.questionComponent.name().toLowerCase();
        final TextComponent.Builder message = Component.text();
        if(componentValues.isEmpty()) {
            message.append(TextUtils.normalWithPrefix("There is no " + componentNamePlural + " for this question"));
        } else {
            message.append(TextUtils.normalWithPrefix(StringUtils.capitalize(componentNamePlural) + ": "))
                    .appendNewline()
                    .append(Component.join(JoinConfiguration.newlines(), componentValues.stream().map(componentValue -> {
                        final String valueComponentCommand = this.valueDoubleQuote ? "\"" + componentValue + "\"" : componentValue;
                        return QuestionsTime.PREFIX.append(Component.text("[X]", NamedTextColor.RED, TextDecoration.BOLD)
                                        .clickEvent(ClickEvent.runCommand("/qt set question \"" + question.getQuestion() + "\" " + componentNamePlural + " remove " + valueComponentCommand))
                                        .hoverEvent(HoverEvent.showText(Component.text("Delete the " + this.questionComponent.getSingular() + " '" + componentValue + "'"))))
                                .append(TextUtils.composedWithoutPrefix(" ", componentValue));
                    }
            ).toList()));
            message.appendNewline().append(TextUtils.normalWithPrefix("You can add " + componentNamePlural + " with the command ")
                    .append(TextUtils.commandSuggestion("set question \"" + question.getQuestion() + "\" " + componentNamePlural + " add "
                            + componentName + "1 " + componentName + "2 " + componentName + "3...")));
        }
        context.sendMessage(message.build());
        return CommandResult.success();
    }
}
