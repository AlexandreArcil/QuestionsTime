package fr.canardnocturne.questionstime.question.creation.steps;

import fr.canardnocturne.questionstime.question.creation.QuestionCreator;
import fr.canardnocturne.questionstime.question.type.Question;
import fr.canardnocturne.questionstime.util.TextUtils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

public class QuestionTypeStep implements CreationStep {

    public static CreationStep INSTANCE = new QuestionTypeStep();

    private static final String SIMPLE = "simple";
    private static final String PROPOSITION = "proposition";

    @Override
    public Component question() {
        return TextUtils.normalWithPrefix("What's the question's type ? Answer with ")
                .append(TextUtils.commandShortcut(SIMPLE))
                .append(TextUtils.normal(" or "))
                .append(TextUtils.commandShortcut(PROPOSITION))
                .append(TextUtils.normal(""));
    }

    @Override
    public boolean handle(final Audience sender, final String answer, final QuestionCreator questionCreator) {
        return switch (answer) {
            case SIMPLE -> {
                questionCreator.setQuestionType(Question.Types.SIMPLE);
                yield true;
            }
            case PROPOSITION -> {
                questionCreator.setQuestionType(Question.Types.MULTI);
                yield true;
            }
            default -> {
                sender.sendMessage(TextUtils.composed("The question's type can only be ", SIMPLE, " or ", PROPOSITION, ", not ", answer));
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
        return switch (questionCreator.getQuestionType()) {
            case SIMPLE -> SimpleQuestionAnswerStep.INSTANCE;
            case MULTI -> QuestionPropositionStep.INSTANCE;
            default -> throw new IllegalArgumentException("Question's type should be defined");
        };
    }
}
