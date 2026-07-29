package fr.canardnocturne.questionstime.command.set.question.tmp.qc2;

import fr.canardnocturne.questionstime.question.Question;
import fr.canardnocturne.questionstime.question.component.Malus;
import fr.canardnocturne.questionstime.question.component.OutcomeCommand;

import java.util.List;
import java.util.Set;

public class QuestionComponents {

    private static final QuestionComponentComplexBase<Question.QuestionBuilder, Question, Boolean> REVEAL_ANSWER =
            new QuestionComponentComplexQuestionBoolean("reveal_answer", "reveal answer", "Set whether the answers should be revealed", Question.QuestionBuilder::setRevealAnswer, Question::isRevealAnswer);
    private static final QuestionComponentComplexBase<Question.QuestionBuilder, Question, Integer> TIME_BETWEEN_ANSWER =
            new QuestionComponentComplexQuestionInteger("time_between_answer", "time between answer", "Set the time between answering questions", Question.QuestionBuilder::setTimeBetweenAnswer, Question::getTimeBetweenAnswer);
    private static final QuestionComponentComplexBase<Question.QuestionBuilder, Question, Integer> TIMER =
            new QuestionComponentComplexQuestionInteger("timer", "timer", "Set the timer for the question", Question.QuestionBuilder::setTimer, Question::getTimer);
    private static final QuestionComponentComplexBase<Question.QuestionBuilder, Question, Integer> WEIGHT =
            new QuestionComponentComplexQuestionInteger("weight", "weight", "Set the weight for the question", Question.QuestionBuilder::setWeight, Question::getWeight);
    private static final QuestionComponentComplexBase<Question.QuestionBuilder, Question, Set<String>> ANSWERS =
            new QuestionComponentComplexQuestionSetString("answers", "answer", "answers", "set the answers of the question", Question.QuestionBuilder::setAnswers, Question::getAnswers);
    private static final QuestionComponentComplexBase<Question.QuestionBuilder, Question, List<String>> PROPOSITIONS =
            new QuestionComponentComplexQuestionListString("propositions", "proposition", "propositions", "set the propositions of the question", Question.QuestionBuilder::setPropositions, Question::getPropositions);
    private static final QuestionComponentComplexBase<Question.QuestionBuilder, Question, Set<String>> TAGS =
            new QuestionComponentComplexQuestionSetString("tags", "tag", "tags", "set the tags of the question", Question.QuestionBuilder::setTags, Question::getTags);

    private static final QuestionComponentComplexBase<Malus.Builder, Malus, Boolean> ANNOUNCE =
            new QuestionComponentComplexMalusBoolean("announce", "announce", "Set whether the malus should be announced", Malus.Builder::setAnnounce, Malus::isAnnounce);
    private static final QuestionComponentComplexBase<Malus.Builder, Malus, Integer> MONEY =
            new QuestionComponentComplexMalusInteger("money", "money", "Set whether the malus should be announced", Malus.Builder::setMoney, Malus::getMoney);
//    private static final QuestionComponentComplexBase<Malus.Builder, Malus, OutcomeCommand[]> COMMANDS =
//            new QuestionComponentComplexMalusArray<OutcomeCommand>("commands", "command", "commands", "set the commands of the question", Malus.Builder::setCommands, Malus::getCommands, OutcomeCommand::fromString, OutcomeCommand::toString, OutcomeCommand.class);
}
