package fr.canardnocturne.questionstime.command.set.question.tmp.qc3.set;

import fr.canardnocturne.questionstime.command.set.question.tmp.qc3.section.QuestionSectionBase;
import fr.canardnocturne.questionstime.question.Question;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;

import java.util.Collection;

public class SetQuestionComponentListExecutor<B, T, V> implements CommandExecutor {

    private final Parameter.Value<Question> specificQuestionParameter;
    private final QuestionSectionBase<T, B, V> questionSectionBase;

    public SetQuestionComponentListExecutor(final Parameter.Value<Question> specificQuestionParameter,
                                            final QuestionSectionBase<T, B, V> questionSectionBase) {
        this.specificQuestionParameter = specificQuestionParameter;
        this.questionSectionBase = questionSectionBase;
    }

    @Override
    public CommandResult execute(final CommandContext context) throws CommandException {
        final Question question = context.requireOne(this.specificQuestionParameter);
        final Component message = this.questionSectionBase.display(question);
        context.sendMessage(message);
        return CommandResult.success();
    }

}
