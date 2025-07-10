package fr.canardnocturne.questionstime.question.creation;

import fr.canardnocturne.questionstime.QuestionsTime;
import fr.canardnocturne.questionstime.question.ask.pool.QuestionPool;
import fr.canardnocturne.questionstime.question.creation.orchestrator.QuestionCreationOrchestrator;
import fr.canardnocturne.questionstime.question.creation.orchestrator.StoppableQuestionCreationOrchestrator;
import fr.canardnocturne.questionstime.question.save.QuestionRegister;
import fr.canardnocturne.questionstime.question.type.Question;
import fr.canardnocturne.questionstime.util.TextUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;
import org.spongepowered.api.entity.living.player.Player;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class QuestionCreationManager {

    private final Map<UUID, QuestionCreationOrchestrator> questionCreators;
    private final QuestionPool questionPool;
    private final QuestionRegister questionRegister;
    private final Logger logger;

    public QuestionCreationManager(final QuestionPool questionPool, final QuestionRegister questionRegister, final Logger logger) {
        this.questionPool = questionPool;
        this.questionRegister = questionRegister;
        this.logger = logger;
        this.questionCreators = new HashMap<>();
    }

    public void handlePlayerArguments(final Player player, final String args) {
        final UUID uuid = player.uniqueId();
        if (!this.questionCreators.containsKey(uuid)) {
            this.sendStartMessages(player);
            this.startQuestionCreation(player);
        } else if (!StringUtils.isEmpty(args)) {
            final QuestionCreationOrchestrator orchestrator = this.questionCreators.get(uuid);
            orchestrator.handle(args);
            if (orchestrator.isFinished()) {
                if (orchestrator.getStatus() == QuestionCreationOrchestrator.Status.FINISHED_SUCCESS) {
                    this.onQuestionCreatedSuccessfully(player);
                } else if (orchestrator.getStatus() == QuestionCreationOrchestrator.Status.FINISHED_STOPPED) {
                    this.onQuestionCreationStopped(player);
                } else {
                    throw new IllegalArgumentException("Status " + orchestrator.getStatus() + " not handled");
                }
                this.questionCreators.remove(uuid);
            }
        } else {
            player.sendMessage(TextUtils.errorWithPrefix("Your answer can not be empty"));
        }
    }

    private void startQuestionCreation(final Player player) {
        final QuestionCreationOrchestrator orchestrator = new StoppableQuestionCreationOrchestrator(player);
        this.questionCreators.put(player.uniqueId(), orchestrator);
        orchestrator.start();
    }

    private void onQuestionCreatedSuccessfully(final Player player) {
        final Question question = this.questionCreators.get(player.uniqueId()).getQuestionCreator().build();
        try {
            this.questionRegister.register(question);
            this.questionPool.add(question);
            player.sendMessage(TextUtils.normalWithPrefix("Question created and registered in the config file!")
                    .appendNewline().append(TextUtils.normalWithPrefix("The question can be asked by the plugin at any time")));
        } catch (final IOException e) {
            player.sendMessage(TextUtils.errorWithPrefix("An error occurred when saving the question, see the latest.log. The question has been logged in debug.log"));
        }
    }

    private void onQuestionCreationStopped(final Player player) {
        player.sendMessage(TextUtils.normalWithPrefix("Question creation stopped"));
    }

    public void onPlayerDisconnect(final UUID uuid, final String name) {
        if (this.questionCreators.containsKey(uuid)) {
            this.questionCreators.remove(uuid);
            this.logger.warn("Player {} ({}) was creating a question. Its progress has been deleted.", name, uuid);
        }
    }

    public boolean isCreator(final UUID uuid) {
        return this.questionCreators.containsKey(uuid);
    }

    private void sendStartMessages(final Player player) {
        player.sendMessage(QuestionsTime.PREFIX.append(Component.text("--------Question Creator--------", NamedTextColor.DARK_GREEN, TextDecoration.BOLD))
                .appendNewline().append(TextUtils.normalWithPrefix("A few things before starting :"))
                .appendNewline().append(TextUtils.composed("• Command arguments with placeholders like ", "[text]", " are mandatory and must be replaced without ", "[]"))
                .appendNewline().append(TextUtils.normalWithPrefix("• Sometimes, you need to confirm, I know it can be annoying but it's to avoid mistakes"))
                .appendNewline().append(TextUtils.normalWithPrefix("• You can stop whenever you want by typing : ")).append(TextUtils.commandShortcut("stop"))
                .appendNewline().append(TextUtils.normalWithPrefix("• If you leave the server while you were creating a question, you will lose your progress"))
                .appendNewline());
    }


}
