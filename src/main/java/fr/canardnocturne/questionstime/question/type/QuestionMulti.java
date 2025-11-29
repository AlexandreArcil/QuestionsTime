package fr.canardnocturne.questionstime.question.type;

import fr.canardnocturne.questionstime.QuestionException;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashSet;

public class QuestionMulti extends Question {

    private final LinkedHashSet<String> propositions;

    private QuestionMulti(final QuestionMultiBuilder builder) {
        super(builder);
        this.propositions = builder.propositions;
    }

    public LinkedHashSet<String> getPropositions() {
        return propositions;
    }

    @Override
    public Types getType() {
        return Types.MULTI;
    }

    @Override
    public String toString() {
        return "QuestionMulti{" +
                "propositions=" + propositions +
                ", question='" + question + '\'' +
                ", answers=" + answers +
                ", prize=" + prizes +
                ", malus=" + malus +
                ", timer=" + timer +
                ", timeBetweenAnswer=" + timeBetweenAnswer +
                ", weight=" + weight +
                '}';
    }

    public static QuestionMultiBuilder builder() {
        return new QuestionMultiBuilder();
    }

    public static class QuestionMultiBuilder extends QuestionBuilder<QuestionMultiBuilder> {

        private LinkedHashSet<String> propositions;

        public QuestionMultiBuilder setPropositions(final LinkedHashSet<String> propositions) {
            this.propositions = propositions;
            return this;
        }

        @Override
        public QuestionMulti build() {
            super.build();
            if (this.propositions == null || this.propositions.size() <= 1) {
                throw new QuestionException("The question need at least 2 propositions");
            }
            if(this.propositions.size() > 128) {
                throw new QuestionException("The question need at most 128 propositions");
            }
            for (final String answer : this.answers) {
                if (!StringUtils.isNumeric(answer)) {
                    throw new QuestionException("The question answer '" + answer + "' needs to be a number between 1 and " + this.propositions.size());
                }
                final byte propositionAnswer = Byte.parseByte(answer);
                if (propositionAnswer < 1 || propositionAnswer > this.propositions.size()) {
                    throw new QuestionException("The question answer '" + answer + "' needs to be a number between 1 and " + this.propositions.size());
                }
            }
            return new QuestionMulti(this);
        }
    }

}
