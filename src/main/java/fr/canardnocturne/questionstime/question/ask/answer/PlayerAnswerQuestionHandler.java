package fr.canardnocturne.questionstime.question.ask.answer;

import fr.canardnocturne.questionstime.QuestionsTime;
import fr.canardnocturne.questionstime.message.Messages;
import fr.canardnocturne.questionstime.question.type.Question;
import net.kyori.adventure.text.Component;
import org.apache.logging.log4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.plugin.PluginContainer;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class PlayerAnswerQuestionHandler implements AnswerHandler {

    private final Logger logger;
    private final PluginContainer plugin;
    private final Map<UUID, Long> playersAnswerCooldown;
    private final Question question;
    private final Game game;
    private final EconomyService economyService;

    public PlayerAnswerQuestionHandler(final Logger logger, final Question question, final Game game, final EconomyService economyService, final PluginContainer plugin) {
        this.logger = logger;
        this.plugin = plugin;
        this.playersAnswerCooldown = new HashMap<>();
        this.question = question;
        this.game = game;
        this.economyService = economyService;
    }

    @Override
    public boolean answer(final Player player, final String answer, final List<ServerPlayer> eligiblePlayers) {
        if (!this.canAnswer(player, eligiblePlayers)) return false;

        if (answer.equals(this.question.getAnswer())) {
            this.announceWinner(player, eligiblePlayers);
            this.givePrize(player);
            return true;
        } else {
            player.sendMessage(QuestionsTime.PREFIX.append(Messages.ANSWER_FALSE.format().setAnswer(answer).message()));
            this.giveCooldown(player);
            this.giveMalus(player);
            return false;
        }
    }

    private void announceWinner(final Player winner, final List<ServerPlayer> eligiblePlayers) {
//      Task.builder().execute(wait -> TODO why a delay has been set ?
        eligiblePlayers.forEach(player -> {
            if (player.uniqueId().equals(winner.uniqueId())) {
                player.sendMessage(QuestionsTime.PREFIX.append(Component.text(Messages.ANSWER_WIN.getMessage())));
            } else {
                player.sendMessage(QuestionsTime.PREFIX.append(Messages.ANSWER_WIN_ANNOUNCE.format().setPlayerName(winner).message()));
            }
        });
//      })).async().delay(500, TimeUnit.MILLISECONDS)
//          .submit(instance.getContainer().getInstance().get());
    }

    private void givePrize(final Player winner) {
        this.question.getPrize().ifPresent(prize -> {
            final Task givePrizeTask = Task.builder().execute(task -> {
                        if (prize.getItemStacks().length == 0 && economyService == null) {
                            return;
                        }
                        winner.sendMessage(QuestionsTime.PREFIX.append(Component.text(Messages.REWARD_ANNOUNCE.getMessage())));
                        if (prize.getItemStacks().length > 0) {
                            for (int i = 0; i < prize.getItemStacks().length; i++) {
                                final ItemStack item = prize.getItemStacks()[i];
                                winner.sendMessage(QuestionsTime.PREFIX.append(Messages.REWARD_PRIZE.format()
                                        .setQuantity(item.quantity())
                                        .setModId(item)
                                        .setItem(item)
                                        .message()));
                                winner.inventory().offer(prize.getItemStacks()[i].copy());
                            }
                        }
                        if (prize.getMoney() > 0 && economyService != null) {
                            winner.sendMessage(QuestionsTime.PREFIX.append(Messages.REWARD_MONEY.format()
                                    .setMoney(prize.getMoney())
                                    .setCurrency(this.economyService)
                                    .message()));
                            final Optional<UniqueAccount> account = this.economyService.findOrCreateAccount(winner.uniqueId());
                            if (account.isPresent()) {
                                account.get().deposit(this.economyService.defaultCurrency(), BigDecimal.valueOf(prize.getMoney()));
                            } else {
                                this.logger.error("The economy account for {} ({}) can't be found or created.", winner.name(), winner.uniqueId());
                            }
                        }
                    }).delay(3, TimeUnit.SECONDS)
                    .plugin(this.plugin)
                    .build();
            this.game.asyncScheduler().submit(givePrizeTask, "[QT]GiveWinnerPrize");
        });
    }

    private void giveCooldown(final Player loser) {
        if (question.isTimeBetweenAnswer()) {
            this.playersAnswerCooldown.put(loser.uniqueId(), System.currentTimeMillis() + (question.getTimeBetweenAnswer() * 1000L));
        }
    }

    private void giveMalus(final Player loser) {
        question.getMalus().ifPresent(malus -> {
            if (economyService != null) {
                loser.sendMessage(QuestionsTime.PREFIX.append(Messages.ANSWER_MALUS.format()
                        .setMoney(malus.getMoney())
                        .setCurrency(economyService).message()));
                final Optional<UniqueAccount> account = economyService.findOrCreateAccount(loser.uniqueId());
                if (account.isPresent()) {
                    account.get().withdraw(economyService.defaultCurrency(), BigDecimal.valueOf(malus.getMoney()));
                } else {
                    this.logger.error("The economy account for {} ({}) can't be found or created.", loser.name(), loser.uniqueId());
                }
            }
        });
    }

    private boolean canAnswer(final Player player, final List<ServerPlayer> eligiblePlayers) {
        if (eligiblePlayers.stream().noneMatch(eligiblePlayer -> eligiblePlayer.equals(player))) {
            return false;
        }
        if (this.playersAnswerCooldown.containsKey(player.uniqueId())) {
            final long time = this.playersAnswerCooldown.get(player.uniqueId());
            if (System.currentTimeMillis() > time)
                this.playersAnswerCooldown.remove(player.uniqueId());
            else {
                player.sendMessage(QuestionsTime.PREFIX.append(Messages.ANSWER_COOLDOWN.format()//
                        .setTimer((int) (this.playersAnswerCooldown.get(player.uniqueId()) - System.currentTimeMillis()) / 1000)
                        .message()));
                return false;
            }
        }
        return true;
    }

}
