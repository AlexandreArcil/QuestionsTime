package fr.canardnocturne.questionstime.question.creation.steps;

import fr.canardnocturne.questionstime.question.creation.QuestionCreator;
import fr.canardnocturne.questionstime.util.TextUtils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.apache.commons.lang3.StringUtils;

public class QuestionStep implements CreationStep {

    public static final CreationStep INSTANCE = new QuestionStep();

    @Override
    public Component question() {
        return TextUtils.composed("What's the question ? Answer with ", "/qtc [question]");
    }

    @Override
    public boolean handle(final Audience player, final String answer, final QuestionCreator questionCreator) {
        if ("yes".equals(answer) && !StringUtils.isEmpty(questionCreator.getQuestion()))
            return true;
        else {
            questionCreator.setQuestion(answer);
            player.sendMessage(TextUtils.composed("Is ", answer, " correct ?")
                    .appendNewline().append(TextUtils.commandShortcut("yes"))
                    .append(TextUtils.normal(" or just repeat the command")));
            return false;
        }
    }

    @Override
    public boolean shouldSkip(final QuestionCreator questionCreator) {
        return false;
    }

    @Override
    public Step next(final QuestionCreator questionCreator) {
        return QuestionTypeStep.INSTANCE;
    }
}
