package fr.canardnocturne.questionstime.question.type;

import fr.canardnocturne.questionstime.QuestionException;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class QuestionMulti extends Question {

    private final List<String> propositions;
    private final byte answer;

    private QuestionMulti(final QuestionMultiBuilder builder) {
        super(builder);
        this.propositions = builder.propositions;
        this.answer = builder.answer;
    }

    public List<String> getPropositions() {
        return propositions;
    }

    @Override
    public String getAnswer() {
        return String.valueOf(answer);
    }

    @Override
    public Types getType() {
        return Types.MULTI;
    }

    @Override
    public String toString() {
        return "QuestionMulti{" +
                "propositions=" + propositions +
                ", answer=" + answer +
                ", question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                ", prize=" + prize +
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

        private final List<String> propositions = new ArrayList<>();
        private byte answer;

        public QuestionMultiBuilder setPropositions(final List<String> propositions) {
            if (propositions == null || propositions.isEmpty())
                throw new IllegalArgumentException("The propositions are null or empty");
            this.propositions.addAll(propositions);
            return this;
        }

        @Override
        public QuestionMultiBuilder setAnswer(final String answer) {
            this.answer = Byte.parseByte(answer);
            return this;
        }

        @Override
        public QuestionMulti build() {
            if (StringUtils.isEmpty(this.question)) {
                throw new QuestionException("The question is not defined");
            }
            if (propositions.size() <= 1) {
                throw new QuestionException("The question need at least 2 propositions");
            }
            if (this.weight <= 0) {
                throw new QuestionException("weight must be greater or equal than 1");
            }
            if (this.answer <= 0) {
                throw new QuestionException("The answer need to be a number superior or equal of 1");
            }
            return new QuestionMulti(this);
        }
    }

}
