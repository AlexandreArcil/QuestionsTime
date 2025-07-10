package fr.canardnocturne.questionstime.config.update;

import fr.canardnocturne.questionstime.question.component.Prize;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.List;

public class FirstVersionConfigurationUpdate implements ConfigurationUpdater {

    @Override
    public void update(final CommentedConfigurationNode configNodeRoot) throws SerializationException {
        final List<CommentedConfigurationNode> questions = configNodeRoot.node("questions").childrenList();
        for (final CommentedConfigurationNode question : questions) {
            final Prize prize = question.node("prize").get(Prize.class);
            if(prize != null) {
                question.node("prizes").setList(Prize.class, List.of(prize));
            }
            question.removeChild("prize");
        }
    }

    @Override
    public int getVersion() {
        return 1;
    }
}
