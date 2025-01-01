package fr.canardnocturne.questionstime.question.creation.steps;

import fr.canardnocturne.questionstime.question.creation.QuestionCreator;
import fr.canardnocturne.questionstime.util.TextUtils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.apache.commons.lang3.StringUtils;

public class SimpleQuestionAnswerStep implements CreationStep {

    public static final CreationStep INSTANCE = new SimpleQuestionAnswerStep();

    @Override
    public Component question() {
        return TextUtils.composed("What's the answer to the question ? Answer with ", "/qtc [answer]", "");
    }

    @Override
    public boolean handle(final Audience sender, final String answer, final QuestionCreator questionCreator) {
        if ("yes".equals(answer) && !StringUtils.isEmpty(questionCreator.getAnswer())) {
            return true;
        }
        questionCreator.setAnswer(answer);
        sender.sendMessage(TextUtils.composed("Is the answer ", answer, " correct ?")
                .appendNewline().append(TextUtils.commandShortcut("yes"))
                .append(TextUtils.normal(" or just repeat the command")));
        return false;
    }

    @Override
    public boolean shouldSkip(final QuestionCreator questionCreator) {
        return false;
    }

    @Override
    public CreationStep next(final QuestionCreator questionCreator) {
        return PrizeItemsStep.INSTANCE;
    }
}
