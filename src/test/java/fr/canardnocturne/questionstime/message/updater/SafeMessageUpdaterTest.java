package fr.canardnocturne.questionstime.message.updater;

import fr.canardnocturne.questionstime.message.Messages;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Map;
import java.util.Set;

class SafeMessageUpdaterTest {

    @Test
    void sameNormalMessage() {
        final String answerAnnounce = Messages.ANSWER_ANNOUNCE.getMessage(); //same message
        final Logger logger = Mockito.mock(Logger.class);
        final SafeMessageUpdater updater = new SafeMessageUpdater(logger);
        final Map<String, String> messagesToUpdate = Map.of(Messages.ANSWER_ANNOUNCE.getSection(), answerAnnounce);
        updater.updateMessages(messagesToUpdate);

        Assertions.assertEquals(answerAnnounce, Messages.ANSWER_ANNOUNCE.getMessage());
    }

    @Test
    void normalMessageModified() {
        final String prizeAnnounce = "coin coin";
        final Logger logger = Mockito.mock(Logger.class);
        final SafeMessageUpdater updater = new SafeMessageUpdater(logger);
        final Map<String, String> messagesToUpdate = Map.of(Messages.PRIZE_ANNOUNCE.getSection(), prizeAnnounce);
        updater.updateMessages(messagesToUpdate);

        Assertions.assertEquals(prizeAnnounce, Messages.PRIZE_ANNOUNCE.getMessage());
    }

    @Test
    void normalMessageEmpty() {
        final String prizeAnnounce = StringUtils.EMPTY;
        final Logger logger = Mockito.mock(Logger.class);
        final SafeMessageUpdater updater = new SafeMessageUpdater(logger);
        final Map<String, String> messagesToUpdate = Map.of(Messages.PRIZE_ANNOUNCE.getSection(), prizeAnnounce);
        updater.updateMessages(messagesToUpdate);

        Assertions.assertNotEquals(prizeAnnounce, Messages.PRIZE_ANNOUNCE.getMessage());
    }

    @Test
    void sameFormatMessage() {
        final String prizeMoney = Messages.PRIZE_MONEY.getMessage();
        final Logger logger = Mockito.mock(Logger.class);
        final SafeMessageUpdater updater = new SafeMessageUpdater(logger);
        final Map<String, String> messagesToUpdate = Map.of(Messages.PRIZE_MONEY.getSection(), prizeMoney);
        updater.updateMessages(messagesToUpdate);

        Assertions.assertEquals(prizeMoney, Messages.PRIZE_MONEY.getMessage());
    }

    @Test
    void formatMessageModified() {
        final String prizeMoney = "§9•{money} §r{currency} coin coin";
        final Logger logger = Mockito.mock(Logger.class);
        final SafeMessageUpdater updater = new SafeMessageUpdater(logger);
        final Map<String, String> messagesToUpdate = Map.of(Messages.PRIZE_MONEY.getSection(), prizeMoney);
        updater.updateMessages(messagesToUpdate);

        Assertions.assertEquals(prizeMoney, Messages.PRIZE_MONEY.getMessage());
    }

    @Test
    void formatMessageExtraComponent() {
        final String prizeMoney = "§9•{money} §r{currency} coin coin {extra}";
        final Logger logger = Mockito.mock(Logger.class);
        final SafeMessageUpdater updater = new SafeMessageUpdater(logger);
        final Map<String, String> messagesToUpdate = Map.of(Messages.PRIZE_MONEY.getSection(), prizeMoney);
        updater.updateMessages(messagesToUpdate);

        Assertions.assertNotEquals(prizeMoney, Messages.PRIZE_MONEY.getMessage());
    }

    @Test
    void formatMessageMissingComponent() {
        final String prizeMoney = "§9•{money} coin coin";
        final Logger logger = Mockito.mock(Logger.class);
        final SafeMessageUpdater updater = new SafeMessageUpdater(logger);
        final Map<String, String> messagesToUpdate = Map.of(Messages.PRIZE_MONEY.getSection(), prizeMoney);
        updater.updateMessages(messagesToUpdate);

        Assertions.assertNotEquals(prizeMoney, Messages.PRIZE_MONEY.getMessage());
    }

    @Test
    void formatMessageEmpty() {
        final String prizeMoney = StringUtils.EMPTY;
        final Logger logger = Mockito.mock(Logger.class);
        final SafeMessageUpdater updater = new SafeMessageUpdater(logger);
        final Map<String, String> messagesToUpdate = Map.of(Messages.PRIZE_MONEY.getSection(), prizeMoney);
        updater.updateMessages(messagesToUpdate);

        Assertions.assertNotEquals(prizeMoney, Messages.PRIZE_MONEY.getMessage());
    }

    @Test
    void unknownMessageSection() {
        final Logger logger = Mockito.mock(Logger.class);
        final Map<String, String> messagesToUpdate = Mockito.mock(Map.class);
        final Set<Map.Entry<String, String>> entrySet = Mockito.mock(Set.class);
        final Map.Entry<String, String> entry = Mockito.mock(Map.Entry.class);
        Mockito.when(entry.getKey()).thenReturn("unknown.section");
        Mockito.when(entrySet.iterator()).thenReturn(Set.of(entry).iterator());
        Mockito.when(messagesToUpdate.entrySet()).thenReturn(entrySet);
        final SafeMessageUpdater updater = new SafeMessageUpdater(logger);
        updater.updateMessages(messagesToUpdate);

        Mockito.verify(entry, Mockito.never()).getValue();
    }

    @Test
    void multipleMessagesUpdate() {
        final String answerAnnounce = Messages.ANSWER_ANNOUNCE.getMessage();
        final String prizeMoney = Messages.PRIZE_MONEY.getMessage();
        final String modifiedAnswerWin = "coin coin";
        final String modifiedAnswerWinAnnounce = "§e§l{name} coin coin !";

        final Logger logger = Mockito.mock(Logger.class);
        final SafeMessageUpdater updater = new SafeMessageUpdater(logger);
        final Map<String, String> messagesToUpdate = Map.of(Messages.ANSWER_ANNOUNCE.getSection(), answerAnnounce,
                Messages.PRIZE_MONEY.getSection(), prizeMoney,
                Messages.ANSWER_WIN.getSection(), modifiedAnswerWin,
                Messages.ANSWER_WIN_ANNOUNCE.getSection(), modifiedAnswerWinAnnounce);

        updater.updateMessages(messagesToUpdate);

        Assertions.assertEquals(answerAnnounce, Messages.ANSWER_ANNOUNCE.getMessage());
        Assertions.assertEquals(prizeMoney, Messages.PRIZE_MONEY.getMessage());
        Assertions.assertEquals(modifiedAnswerWin, Messages.ANSWER_WIN.getMessage());
        Assertions.assertEquals(modifiedAnswerWinAnnounce, Messages.ANSWER_WIN_ANNOUNCE.getMessage());
    }

}