package fr.canardnocturne.questionstime.question.ask.announcer;

import fr.canardnocturne.questionstime.QuestionsTime;
import fr.canardnocturne.questionstime.message.Messages;
import fr.canardnocturne.questionstime.question.component.Prize;
import fr.canardnocturne.questionstime.question.component.OutcomeCommand;
import fr.canardnocturne.questionstime.question.type.Question;
import fr.canardnocturne.questionstime.question.type.QuestionMulti;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.plugin.PluginContainer;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class SimpleQuestionAnnouncer implements QuestionAnnouncer {

    private final Game game;
    private final PluginContainer plugin;

    public SimpleQuestionAnnouncer(final Game game, final PluginContainer plugin) {
        this.game = game;
        this.plugin = plugin;
    }

    @Override
    public void announce(final Question question, final List<ServerPlayer> players) {
        final EconomyService economyService = Sponge.server().serviceProvider().provide(EconomyService.class).orElse(null);
        players.forEach(player ->
                player.sendMessage(QuestionsTime.PREFIX.append(Component.text(Messages.QUESTION_NEW.getMessage()))));

        final Task askQuestionTask = Task.builder().execute(task -> {
                    players.forEach(player -> {
                        player.sendMessage(QuestionsTime.PREFIX.append(Messages.QUESTION_ASK.format().setQuestion(question.getQuestion()).message()));

                        if (question.getType() == Question.Types.MULTI) {
                            final QuestionMulti qMulti = (QuestionMulti) question;
                            byte position = 1;
                            for (final String proposition : qMulti.getPropositions()) {
                                player.sendMessage(QuestionsTime.PREFIX.append(Messages.QUESTION_PROPOSITION.format()
                                        .setPosition(position)
                                        .setProposition(proposition)
                                        .message()));
                                position++;
                            }
                        }

                        question.getPrizes().ifPresent(prizes -> {
                            for (final Prize prize : prizes) {
                                if (prize.isAnnounce() && (prize.getItemStacks().length > 0 || prize.getCommands().length > 0 || economyService != null)) {
                                    if(prizes.size() == 1) {
                                        player.sendMessage(QuestionsTime.PREFIX.append(Component.text(Messages.PRIZE_ANNOUNCE.getMessage())));
                                    } else {
                                        player.sendMessage(QuestionsTime.PREFIX.append(Messages.PRIZE_ANNOUNCE_POSITION.format().setWinnerPosition(prize.getPosition()).message()));
                                    }
                                    for (final ItemStack is : prize.getItemStacks()) {
                                        player.sendMessage(QuestionsTime.PREFIX.append(Messages.PRIZE_ITEM.format()
                                                .setItem(is)
                                                .setModId(is)
                                                .setQuantity(is.quantity())
                                                .message()));
                                    }
                                    for (final OutcomeCommand command : prize.getCommands()) {
                                        player.sendMessage(QuestionsTime.PREFIX.append(Messages.OUTCOME_COMMAND.format()
                                                .setCommand(command).message()));
                                    }
                                    if (prize.getMoney() > 0 && economyService != null) {
                                        player.sendMessage(QuestionsTime.PREFIX.append(Messages.PRIZE_MONEY.format()
                                                .setMoney(prize.getMoney())
                                                .setCurrency(economyService)
                                                .message()));
                                    }
                                }
                            }

                        });

                        question.getMalus().ifPresent(malus -> {
                            if (malus.isAnnounce() && ((malus.getMoney() > 0 && economyService != null) || malus.getCommands().length > 0)) {
                                player.sendMessage(QuestionsTime.PREFIX.append(Component.text(Messages.MALUS_ANNOUNCE.getMessage())));
                                if(malus.getMoney() > 0 && economyService != null) {
                                    player.sendMessage(QuestionsTime.PREFIX.append(Messages.MALUS_MONEY.format()
                                            .setMoney(malus.getMoney())
                                            .setCurrency(economyService)
                                            .message()));
                                }
                                for (final OutcomeCommand command : malus.getCommands()) {
                                    player.sendMessage(QuestionsTime.PREFIX.append(Messages.OUTCOME_COMMAND.format()
                                            .setCommand(command).message()));
                                }
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
