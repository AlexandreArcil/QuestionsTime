package fr.canardnocturne.questionstime.command.set.question.tmp.qc;

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
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;

import java.util.Collection;
import java.util.Map;

public class SetQuestionListComponentComplexAtPositionExecutor implements CommandExecutor {

    private final Parameter.Value<Question> specificQuestionParameter;
    private final Parameter.Value<Integer> positionParameter;
    private final ListQuestionPositionComponent questionComponent;

    public SetQuestionListComponentComplexAtPositionExecutor(final Parameter.Value<Question> specificQuestionParameter,
                                                             final Parameter.Value<Integer> positionParameter,
                                                             final ListQuestionPositionComponent questionComponent) {
        this.specificQuestionParameter = specificQuestionParameter;
        this.positionParameter = positionParameter;
        this.questionComponent = questionComponent;
    }

    @Override
    public CommandResult execute(final CommandContext context) throws CommandException {
        final Question question = context.requireOne(specificQuestionParameter);
        final int position = context.requireOne(this.positionParameter);
        final Map<Integer, Collection<String>> componentValues = this.questionComponent.getComponentExtractor().apply(question);
        final String componentNamePlural = this.questionComponent.getPlural();
        final String componentName = this.questionComponent.getName();
        final TextComponent.Builder message = Component.text();
        if(componentValues.isEmpty()) {
            message.append(TextUtils.normalWithPrefix("There is no " + componentNamePlural + " for this question"));
        } else {
            message.append(TextUtils.normalWithPrefix(StringUtils.capitalize(componentNamePlural) + ": "))
                    .appendNewline()
                    .append(Component.join(JoinConfiguration.newlines(), componentValues.entrySet().stream().map(positionComponentValues -> {
                                final TextComponent.Builder componentMessage = Component.text().append(TextUtils.normalWithPrefix("Position " + positionComponentValues.getKey() + ":"))
                                        .appendNewline();
                                if (positionComponentValues.getValue().isEmpty()) {
                                    componentMessage.append(TextUtils.normalWithPrefix("  No " + this.questionComponent.getPlural()));
                                } else {
                                    componentMessage.append(Component.join(JoinConfiguration.newlines(), positionComponentValues.getValue().stream().map(componentValue -> QuestionsTime.PREFIX.append(Component.text("[X]", NamedTextColor.RED, TextDecoration.BOLD)
                                                    .clickEvent(ClickEvent.runCommand("/qt set question \"" + question.getQuestion() + "\" " + componentNamePlural + " remove " + positionComponentValues.getKey() + " " + "\"" + componentValue + "\""))
                                                    .hoverEvent(HoverEvent.showText(Component.text("Delete the " + this.questionComponent.getSingular() + " '" + componentValue + "'"))))
                                            .append(TextUtils.composedWithoutPrefix(" ", componentValue))).toList()));
                                }
                                return componentMessage.build();
                            }).toList()));
            message.appendNewline().append(TextUtils.normalWithPrefix("You can add " + componentNamePlural + " with the command ")
                    .append(TextUtils.commandSuggestion("set question \"" + question.getQuestion() + "\" " + componentNamePlural + " add <position> \""
                            + componentName + "1\" \"" + componentName + "2\" \"" + componentName + "3\"...")));
        }
        context.sendMessage(message.build());
        return CommandResult.success();
    }
}
