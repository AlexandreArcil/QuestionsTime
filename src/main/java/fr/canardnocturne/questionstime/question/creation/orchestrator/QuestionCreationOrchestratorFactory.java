package fr.canardnocturne.questionstime.question.creation.orchestrator;

import org.spongepowered.api.entity.living.player.Player;

public interface QuestionCreationOrchestratorFactory {

    QuestionCreationOrchestrator create(Player player);

}
