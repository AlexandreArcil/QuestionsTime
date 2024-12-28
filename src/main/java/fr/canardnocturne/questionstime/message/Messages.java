package fr.canardnocturne.questionstime.message;

import fr.canardnocturne.questionstime.message.format.*;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Messages {

    private static final Map<String, SimpleMessage> messages = new HashMap<>();

    public static final SimpleMessage QUESTION_NEW = register(new SimpleMessage("question.new", "§eIt's Question Time !"));
    public static final MessageFormat<AskQuestionMessage.Format> QUESTION_ASK = register(new AskQuestionMessage("question.ask", "§e§l{question}"));
    public static final MessageFormat<QuestionPropositionMessage.Format> QUESTION_PROPOSITION = register(new QuestionPropositionMessage("question.proposition", "§b•{position}] {proposition}"));
    public static final SimpleMessage QUESTION_END = register(new SimpleMessage("question.end", "§eMay the best win !"));
    public static final MessageFormat<QuestionTimerEndMessage.Format> QUESTION_TIMER_END = register(new QuestionTimerEndMessage("question.timer.end", "§eYou have §9§l{timer}§r§e to answer ! May the best win !"));
    public static final MessageFormat<QuestionTimerLeftMessage.Format> QUESTION_TIMER_LEFT = register(new QuestionTimerLeftMessage("question.timer.left", "§eYou have §9§l{timer}§r§e to answer !"));
    public static final SimpleMessage QUESTION_TIMER_OUT = register(new SimpleMessage("question.timer.out", "§cNobody have found the answer, maybe a next time"));
    public static final SimpleMessage PRIZE_ANNOUNCE = register(new SimpleMessage("prize.announce", "§eThe winner win :"));
    public static final MessageFormat<PrizeMoneyMessage.Format> PRIZE_MONEY = register(new PrizeMoneyMessage("prize.money", "§9•{money} §r{currency}"));
    public static final MessageFormat<PrizeItemMessage.Format> PRIZE_ITEM = register(new PrizeItemMessage("prize.item", "§9• {quantity} * {modid}§f{item}"));
    public static final MessageFormat<PrizeCommandMessage.Format> PRIZE_COMMAND = register(new PrizeCommandMessage("prize.command", "§9• {command}"));
    public static final SimpleMessage MALUS_ANNOUNCE = register(new SimpleMessage("malus.announce", "§cBut a wrong answer :"));
    public static final MessageFormat<MalusMoneyMessage.Format> MALUS_MONEY = register(new MalusMoneyMessage("malus.money", "§4• -{money} §r{currency}"));
    public static final SimpleMessage ANSWER_ANNOUNCE = register(new SimpleMessage("answer.announce", "§eAnswer with : \"§bqt>answer§e\""));
    public static final SimpleMessage ANSWER_WIN = register(new SimpleMessage("answer.win", "§e§lYou win !"));
    public static final MessageFormat<AnswerWinAnnounceMessage.Format> ANSWER_WIN_ANNOUNCE = register(new AnswerWinAnnounceMessage("answer.win-announce", "§e§l{name} win !"));
    public static final MessageFormat<AnswerFalseMessage.Format> ANSWER_FALSE = register(new AnswerFalseMessage("answer.false", "§e§l{answer} §cisn't the right answer :("));
    public static final MessageFormat<AnswerMalusMessage.Format> ANSWER_MALUS = register(new AnswerMalusMessage("answer.malus", "§cYou lose §4{money} §r{currency}"));
    public static final MessageFormat<AnswerCooldownMessage.Format> ANSWER_COOLDOWN = register(new AnswerCooldownMessage("answer.cooldown", "§cYou have to wait {timer} §cto suggest an another answer"));
    public static final SimpleMessage REWARD_ANNOUNCE = register(new SimpleMessage("reward.announce", "§e§lHere's your reward :"));
    public static final MessageFormat<RewardPrizeMessage.Format> REWARD_PRIZE = register(new RewardPrizeMessage("reward.prize", "§9• {quantity} * {modid}§f{item}"));
    public static final MessageFormat<RewardMoneyMessage.Format> REWARD_MONEY = register(new RewardMoneyMessage("reward.money", "§9•{money} §r{currency}"));

    private static <T extends SimpleMessage> T register(final T message) {
        messages.put(message.getSection(), message);
        return message;
    }

    public static SimpleMessage getMessage(final String section) {
        return messages.get(section);
    }

    public static int registeredMessagesCount() {
        return messages.size();
    }

    public static Collection<SimpleMessage> getAll() {
        return Collections.unmodifiableCollection(messages.values());
    }

}
