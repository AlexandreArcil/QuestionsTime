package fr.canardnocturne.questionstime.question.creation.steps;

import fr.canardnocturne.questionstime.question.creation.QuestionCreator;
import fr.canardnocturne.questionstime.util.TextUtils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

public class StopQuestionCreationStep implements CreationStep {

    public static final StopQuestionCreationStep INSTANCE = new StopQuestionCreationStep();

    private static final String YES = "yes";
    private static final String NO = "no";

    @Override
    public Component question() {
        return TextUtils.normalWithPrefix("Do you really want to stop the creation of the question ?")
                .appendNewline().append(TextUtils.normal("Answer with "))
                .append(TextUtils.commandShortcut("yes"))
                .append(TextUtils.normal(" or "))
                .append(TextUtils.commandShortcut("no"));
    }

    @Override
    public boolean handle(final Audience sender, final String answer, final QuestionCreator questionCreator) {
        return switch (answer) {
            case YES -> {
                questionCreator.setStopped();
                yield true;
            }
            case NO -> true;
            default -> {
                sender.sendMessage(TextUtils.composed("The answer need to be ", "yes OR no"));
                yield false;
            }
        };
    }

    @Override
    public boolean shouldSkip(final QuestionCreator questionCreator) {
        return false;
    }

    @Override
    public CreationStep next(final QuestionCreator questionCreator) {
        return null;
    }
}
