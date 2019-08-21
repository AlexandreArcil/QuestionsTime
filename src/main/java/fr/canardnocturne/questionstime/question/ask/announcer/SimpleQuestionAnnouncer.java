package fr.canardnocturne.questionstime.question.ask.announcer;

import fr.canardnocturne.questionstime.QuestionsTime;
import fr.canardnocturne.questionstime.message.Messages;
import fr.canardnocturne.questionstime.question.type.Question;
import fr.canardnocturne.questionstime.question.type.QuestionMulti;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.Game;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.plugin.PluginContainer;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class SimpleQuestionAnnouncer implements QuestionAnnouncer {

    private final Game game;
    private final EconomyService economyService;
    private final PluginContainer plugin;

    public SimpleQuestionAnnouncer(final Game game, final EconomyService economyService, final PluginContainer plugin) {
        this.game = game;
        this.economyService = economyService;
        this.plugin = plugin;
    }

    @Override
    public void announce(final Question question, final List<ServerPlayer> players) {
        players.forEach(player -> {
            player.sendMessage(QuestionsTime.PREFIX.append(Component.text(Messages.QUESTION_NEW.getMessage())));
        });

        final Task askQuestionTask = Task.builder().execute(task -> {
                    players.forEach(player -> {
                        player.sendMessage(QuestionsTime.PREFIX.append(Messages.QUESTION_ASK.format().setQuestion(question.getQuestion()).message()));

                        if (question.getType() == Question.Types.MULTI) {
                            final QuestionMulti qMulti = (QuestionMulti) question;
                            for (int i = 0; i < qMulti.getPropositions().size(); i++) {
                                player.sendMessage(QuestionsTime.PREFIX.append(Messages.QUESTION_PROPOSITION.format()
                                        .setPosition((byte) (i + 1))
                                        .setProposition(qMulti.getPropositions().get(i))
                                        .message()));
                            }
                        }

                        question.getPrize().ifPresent(prize -> {
                            if (prize.isAnnounce() && economyService != null) {
                                player.sendMessage(QuestionsTime.PREFIX.append(Component.text(Messages.PRIZE_ANNOUNCE.getMessage())));
                                if (prize.getItemStacks().length > 0) {
                                    for (int i = 0; i < prize.getItemStacks().length; i++) {
                                        final ItemStack is = prize.getItemStacks()[i];
                                        if (!is.type().equals(ItemTypes.AIR))
                                            player.sendMessage(QuestionsTime.PREFIX.append(Messages.PRIZE_ITEM.format()
                                                    .setItem(is)
                                                    .setModId(is)
                                                    .setQuantity(is.quantity())
                                                    .message()));
                                    }
                                }
                                if (prize.getMoney() > 0) {
                                    player.sendMessage(QuestionsTime.PREFIX.append(Messages.PRIZE_MONEY.format()
                                            .setMoney(prize.getMoney())
                                            .setCurrency(economyService)
                                            .message()));
                                }
                            }
                        });

                        question.getMalus().ifPresent(malus -> {
                            if (malus.isAnnounce() && malus.getMoney() > 0 && economyService != null) {
                                player.sendMessage(QuestionsTime.PREFIX.append(Component.text(Messages.MALUS_ANNOUNCE.getMessage())));
                                player.sendMessage(QuestionsTime.PREFIX.append(Messages.MALUS_MONEY.format()
                                        .setMoney(malus.getMoney())
                                        .setCurrency(economyService)
                                        .message()));
                            }
                        });

                        player.sendMessage(QuestionsTime.PREFIX.append(Component.text(Messages.ANSWER_ANNOUNCE.getMessage())));
                        if (question.isTimed()) {
                            player.sendMessage(QuestionsTime.PREFIX.append(Messages.QUESTION_TIMER_END.format()
                                    .setTimer(question.getTimer()).message()));
                        } else {
                            player.sendMessage(QuestionsTime.PREFIX.append(Component.text(Messages.QUESTION_END.getMessage())));
                        }
                    });
                }).delay(3, TimeUnit.SECONDS)
                .plugin(this.plugin)
                .build();
        this.game.asyncScheduler().submit(askQuestionTask, "[QT]AskQuestion");
    }

}
