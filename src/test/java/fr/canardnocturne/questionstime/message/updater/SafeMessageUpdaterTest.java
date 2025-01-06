package fr.canardnocturne.questionstime.message.updater;

import fr.canardnocturne.questionstime.message.Messages;
import org.apache.logging.log4j.LogManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SafeMessageUpdaterTest {

    @Test
    public void test() {
        final String answerAnnounce = Messages.ANSWER_ANNOUNCE.getMessage(); //same message
        final String answerWinAnnounce = Messages.ANSWER_WIN_ANNOUNCE.getMessage(); //one more component
        final String malusMoney = Messages.MALUS_MONEY.getMessage(); //one component removed
        final String prizeAnnounce = "new prize announce message"; //normal message modified
        final String prizeMoney = "§9•{money} §r{currency} added text"; //format message modified
        final SafeMessageUpdater updater = new SafeMessageUpdater(LogManager.getLogger());
        final Map<String, String> messagesToUpdate = Map.of("section.doesnt.exist", "test",
                "answer.announce", answerAnnounce,
                "answer.win", "",
                "answer.win-announce", "§e§l{name}{oneMoreComponent} win !",
                "malus.money", "§4• -{money}",
                "prize.announce", prizeAnnounce,
                "prize.money", prizeMoney);
        updater.updateMessages(messagesToUpdate);

        Assertions.assertEquals(answerAnnounce, Messages.ANSWER_ANNOUNCE.getMessage());
        Assertions.assertEquals(answerWinAnnounce, Messages.ANSWER_WIN_ANNOUNCE.getMessage());
        Assertions.assertEquals(malusMoney, Messages.MALUS_MONEY.getMessage());
        Assertions.assertEquals(prizeAnnounce, Messages.PRIZE_ANNOUNCE.getMessage());
        Assertions.assertEquals(prizeMoney, Messages.PRIZE_MONEY.getMessage());
    }

}