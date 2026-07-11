package fr.canardnocturne.questionstime.question.creation.steps;

import fr.canardnocturne.questionstime.question.creation.QuestionCreator;
import fr.canardnocturne.questionstime.util.TextUtils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

public class RevealAnswerStep implements CreationStep {

    public static final CreationStep INSTANCE = new RevealAnswerStep();

    private static final String YES = "yes";
    private static final String NO = "no";

    @Override
    public Component question() {
        return TextUtils.normalWithPrefix("Should the answer(s) be revealed at the end of the question ?")
                .appendNewline()
                .append(TextUtils.normalWithPrefix("Answer with "))
                .append(TextUtils.commandShortcut(YES))
                .append(TextUtils.normal(" or "))
                .append(TextUtils.commandShortcut(NO));
    }

    @Override
    public boolean handle(final Audience sender, final String answer, final QuestionCreator questionCreator) {
        return switch (answer) {
            case YES -> {
                questionCreator.setRevealAnswer(true);
                yield true;
            }
            case NO -> {
                questionCreator.setRevealAnswer(false);
                yield true;
            }
            default -> {
                sender.sendMessage(TextUtils.composed("The answer can only be ", YES, " or ", NO, ", not ", answer));
                yield false;
            }
        };
    }

    @Override
    public boolean shouldSkip(final QuestionCreator questionCreator) {
        return false;
    }

    @Override
    public Step next(final QuestionCreator questionCreator) {
        return TagsStep.INSTANCE;
    }
}
