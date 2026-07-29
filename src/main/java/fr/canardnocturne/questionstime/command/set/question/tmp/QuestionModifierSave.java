package fr.canardnocturne.questionstime.command.set.question.tmp;

import fr.canardnocturne.questionstime.QuestionException;
import fr.canardnocturne.questionstime.question.Question;
import fr.canardnocturne.questionstime.question.QuestionComponent;
import fr.canardnocturne.questionstime.question.ask.pool.QuestionPool;
import fr.canardnocturne.questionstime.question.modifier.QuestionModifier;
import fr.canardnocturne.questionstime.question.save.QuestionRegister;
import fr.canardnocturne.questionstime.util.TextUtils;
import net.kyori.adventure.text.Component;

import java.io.IOException;

public class QuestionModifierSave {

    private final QuestionModifier questionModifier;
    private final QuestionRegister questionRegister;
    private final QuestionPool questionPool;

    public QuestionModifierSave(final QuestionModifier questionModifier, final QuestionRegister questionRegister, final QuestionPool questionPool) {
        this.questionModifier = questionModifier;
        this.questionRegister = questionRegister;
        this.questionPool = questionPool;
    }

    Component test(Question question, QuestionComponent questionComponent, boolean value) {
        try {
            final Question modifiedQuestion = this.questionModifier.set(question, questionComponent, value);
            this.questionRegister.replace(question, modifiedQuestion);
            this.questionPool.replace(question, modifiedQuestion);
            if(value){
                return TextUtils.composed("The answer(s) will now be revealed at the end of the question !");
            } else {
                return TextUtils.composed("The answer(s) will no longer be revealed at the end of the question !");
            }
        } catch (final QuestionException | IllegalArgumentException e) {
            return TextUtils.errorWithPrefix(e.getMessage());
        } catch (final IOException e) {
            return TextUtils.errorWithPrefix("An error occurred while trying to save the question. See the log for details.");
        }
    }

}
