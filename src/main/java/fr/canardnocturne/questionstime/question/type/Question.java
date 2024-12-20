package fr.canardnocturne.questionstime.question.type;

import fr.canardnocturne.questionstime.QuestionException;
import fr.canardnocturne.questionstime.question.component.Malus;
import fr.canardnocturne.questionstime.question.component.Prize;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.configurate.ConfigurationNode;

import java.util.Objects;
import java.util.Optional;

public class Question {

    protected final String question;
    protected final String answer;
    protected final Prize prize;
    protected final Malus malus;
    protected final int timer;
    protected final int timeBetweenAnswer;
    protected final int weight;

    protected Question(final QuestionBuilder builder) {
        this.question = builder.question;
        this.answer = builder.answer;
        this.timer = builder.timer;
        this.prize = builder.prize;
        this.malus = builder.malus;
        this.timeBetweenAnswer = builder.timeBetweenAnswer;
        this.weight = builder.weight;
    }

    public static QuestionBuilder builder() {
        return new QuestionBuilder();
    }

    public Optional<Prize> getPrize() {
        return Optional.ofNullable(this.prize);
    }

    public static Types getType(final ConfigurationNode questionNode) {
        if (!questionNode.node("question").empty() && !questionNode.node("answer").empty()) {
            if (!questionNode.node("proposition").empty())
                return Types.MULTI;
            return Types.SIMPLE;
        }
        return Types.ERROR;
    }

    public String getAnswer() {
        return answer;
    }

    public String getQuestion() {
        return this.question;
    }

    public Types getType() {
        return Types.SIMPLE;
    }

    public Optional<Malus> getMalus() {
        return Optional.ofNullable(malus);
    }

    public int getTimer() {
        return this.timer;
    }

    public boolean isTimed() {
        return timer > 0;
    }

    public boolean isTimeBetweenAnswer() {
        return this.timeBetweenAnswer > 0;
    }

    public int getTimeBetweenAnswer() {
        return timeBetweenAnswer;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        final Question question1 = (Question) o;
        return Objects.equals(question, question1.question);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(question);
    }

    @Override
    public String toString() {
        return "Question{" +
                "question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                ", prize=" + prize +
                ", malus=" + malus +
                ", timer=" + timer +
                ", timeBetweenAnswer=" + timeBetweenAnswer +
                ", weight=" + weight +
                '}';
    }

    public enum Types {
        SIMPLE, MULTI, ERROR
    }

    public static class QuestionBuilder<T extends QuestionBuilder<T>> {

        protected String question;
        protected String answer;
        protected Prize prize;
        protected Malus malus;
        protected int timer;
        protected int timeBetweenAnswer;
        protected int weight;

        public T setQuestion(final String question) {
            this.question = question;
            return (T) this;
        }

        public T setAnswer(final String answer) {
            this.answer = answer;
            return (T) this;
        }

        public T setMalus(final Malus malus) {
            this.malus = malus;
            return (T) this;
        }

        public T setPrize(final Prize prize) {
            this.prize = prize;
            return (T) this;
        }

        public T setTimeBetweenAnswer(final int timeBetweenAnswer) {
            this.timeBetweenAnswer = timeBetweenAnswer;
            return (T) this;
        }

        public T setTimer(final int timer) {
            this.timer = timer;
            return (T) this;
        }

        public T setWeight(final int weight) {
            this.weight = weight;
            return (T) this;
        }

        public Question build() {
            if (StringUtils.isEmpty(this.question)) {
                throw new QuestionException("The question is not defined");
            }
            if (StringUtils.isEmpty(this.answer)) {
                throw new QuestionException("The answer is not defined");
            }
            if (this.weight <= 0) {
                throw new QuestionException("weight must be greater or equal than 1");
            }
            return new Question(this);
        }

    }


}
