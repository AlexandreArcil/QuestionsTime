package fr.canardnocturne.questionstime.question;

import fr.canardnocturne.questionstime.QuestionException;
import fr.canardnocturne.questionstime.question.component.Malus;
import fr.canardnocturne.questionstime.question.component.Prize;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class Question {

    private final String question;
    private final List<String> propositions;
    private final Set<String> answers;
    private final SortedSet<Prize> prizes;
    private final Malus malus;
    private final int timer;
    private final int timeBetweenAnswer;
    private final int weight;

    private Question(final QuestionBuilder builder) {
        this.question = builder.question;
        this.answers = Collections.unmodifiableSet(builder.answers);
        this.timer = builder.timer;
        this.prizes = Collections.unmodifiableSortedSet(builder.prizes);
        this.malus = builder.malus;
        this.timeBetweenAnswer = builder.timeBetweenAnswer;
        this.weight = builder.weight;
        this.propositions = Collections.unmodifiableList(builder.propositions);
    }

    public SortedSet<Prize> getPrizes() {
        return this.prizes;
    }

    public Set<String> getAnswers() {
        return answers;
    }

    public List<String> getPropositions() {
        return propositions;
    }

    public String getQuestion() {
        return this.question;
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

    public static QuestionBuilder builder() {
        return new QuestionBuilder();
    }

    public QuestionBuilder toBuilder() {
        return new QuestionBuilder(this);
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof final Question question1)) return false;
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
                ", answers='" + answers + '\'' +
                ", prize=" + prizes +
                ", malus=" + malus +
                ", timer=" + timer +
                ", timeBetweenAnswer=" + timeBetweenAnswer +
                ", weight=" + weight +
                '}';
    }

    public static class QuestionBuilder {

        private String question;
        private final List<String> propositions;
        private final Set<String> answers;
        private final SortedSet<Prize> prizes;
        private Malus malus;
        private int timer;
        private int timeBetweenAnswer;
        private int weight;

        private QuestionBuilder() {
            this.propositions = new ArrayList<>();
            this.answers = new HashSet<>();
            this.prizes = new TreeSet<>(Comparator.comparingInt(Prize::getPosition));
        }

        private QuestionBuilder(final Question question) {
            this();
            this.question = question.question;
            this.propositions.addAll(question.propositions);
            this.answers.addAll(question.answers);
            for (final Prize prize : question.prizes) {
                final Prize prizeClone = new Prize(prize);
                this.prizes.add(prizeClone);
            }
            if(question.malus != null) {
                this.malus = new Malus(question.malus);
            }
            this.timer = question.timer;
            this.timeBetweenAnswer = question.timeBetweenAnswer;
            this.weight = question.weight;
        }

        public QuestionBuilder setQuestion(final String question) {
            this.question = question;
            return this;
        }

        public QuestionBuilder setPropositions(final List<String> propositions) {
            this.propositions.clear();
            this.propositions.addAll(propositions);
            return this;
        }

        public QuestionBuilder setAnswers(final Set<String> answers) {
            this.answers.clear();
            this.answers.addAll(answers);
            return this;
        }

        public QuestionBuilder setMalus(final Malus malus) {
            this.malus = malus;
            return this;
        }

        public QuestionBuilder setPrizes(final Set<Prize> prizes) {
            this.prizes.clear();
            this.prizes.addAll(prizes);
            return this;
        }

        public QuestionBuilder setTimeBetweenAnswer(final int timeBetweenAnswer) {
            this.timeBetweenAnswer = timeBetweenAnswer;
            return this;
        }

        public QuestionBuilder setTimer(final int timer) {
            this.timer = timer;
            return this;
        }

        public QuestionBuilder setWeight(final int weight) {
            this.weight = weight;
            return this;
        }

        public Question build() {
            if (StringUtils.isEmpty(this.question)) {
                throw new QuestionException("The question is not defined");
            }
            if (this.answers.isEmpty()) {
                throw new QuestionException("The answer is not defined");
            }
            final Set<String> answersUnique = new HashSet<>(this.answers);
            if(answersUnique.size() != this.answers.size()) {
                throw new QuestionException("The answers are not unique");
            }
            if (this.weight <= 0) {
                throw new QuestionException("Weight must be greater or equal than 1");
            }
            if(!this.propositions.isEmpty()) {
                if (this.propositions.size() == 1) {
                    throw new QuestionException("The question need at least 2 propositions");
                }
                if(this.propositions.size() > 128) {
                    throw new QuestionException("The question need at most 128 propositions");
                }
                final List<String> answersNotProposition = this.answers.stream().filter(answer -> !propositions.contains(answer)).toList();
                if (!answersNotProposition.isEmpty()) {
                    throw new QuestionException("The question answers '" + answersNotProposition + "' need to be a proposition");
                }
            }
            //check that the positions of the prizes doesn't have "hole"
            if(!this.prizes.isEmpty()) {
                this.prizes.stream().map(Prize::getPosition).reduce((previous, current) -> {
                    final int next = previous + 1;
                    if (next != current) {
                        throw new QuestionException("The position prize " + next + " is missing");
                    }
                    return current;
                });
            }
            return new Question(this);
        }

    }


}
