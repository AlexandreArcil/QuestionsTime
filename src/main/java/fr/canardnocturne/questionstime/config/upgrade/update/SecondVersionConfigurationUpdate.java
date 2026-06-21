package fr.canardnocturne.questionstime.config.upgrade.update;

import fr.canardnocturne.questionstime.config.upgrade.ConfigurationUpgradeException;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SecondVersionConfigurationUpdate implements ConfigurationUpdater {

    @Override
    public void update(final CommentedConfigurationNode configNodeRoot) throws SerializationException, ConfigurationUpgradeException {
        final List<CommentedConfigurationNode> questions = configNodeRoot.node("questions").childrenList();
        for (final CommentedConfigurationNode question : questions) {
            final List<String> answers = question.node("answer").getList(String.class, Collections.emptyList());
            if(!answers.isEmpty() && question.hasChild("proposition")) {
                final List<String> propositions = question.node("proposition").getList(String.class, Collections.emptyList());
                if(propositions.isEmpty()) {
                    throw new ConfigurationUpgradeException("'proposition' should not be empty");
                }
                final List<String> newAnswers = new ArrayList<>();
                for (final String answer : answers) {
                    final int index = parseInt(answer);
                    if(index >= 1 && index <= propositions.size()) {
                        newAnswers.add(propositions.get(index - 1));
                    } else {
                        throw new ConfigurationUpgradeException("The answer '" + answer + "' must be a number between 1 and " + (propositions.size()));
                    }
                }
                question.node("answer").setList(String.class, newAnswers);
            }
        }
    }

    private int parseInt(final String answer) throws ConfigurationUpgradeException {
        try {
            return Integer.parseInt(answer);
        }  catch (final NumberFormatException e) {
            throw new ConfigurationUpgradeException("The answer '" + answer + "' is not a number");
        }
    }

    @Override
    public int getVersion() {
        return 2;
    }
}
