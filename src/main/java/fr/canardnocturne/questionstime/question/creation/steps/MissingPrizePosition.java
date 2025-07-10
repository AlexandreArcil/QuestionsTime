package fr.canardnocturne.questionstime.question.creation.steps;

import fr.canardnocturne.questionstime.question.creation.QuestionCreator;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

public class MissingPrizePosition implements VerifyStep {
    //TODO deporter logique de l'orchestrator dans VerifyStep et CreationStep qui deviendront des petit orchestrator ?
    public final static MissingPrizePosition INSTANCE = new MissingPrizePosition();

    @Override
    public boolean verify(QuestionCreator questionCreator) {
        return false;
    }

    @Override
    public Step next(final QuestionCreator questionCreator) {
        return AnnouncePrizeStep.INSTANCE;
    }

    @Override
    public Step returnTo() {
        return PrizeItemsStep.INSTANCE;
    }
}
