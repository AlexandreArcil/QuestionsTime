package fr.canardnocturne.questionstime.config.upgrade.update;

import fr.canardnocturne.questionstime.config.QuestionTimeConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.stream.Stream;

class NoVersionConfigurationUpdateTest {

    @ParameterizedTest
    @MethodSource("values")
    void replaceOldMode(final boolean randomTime, final QuestionTimeConfiguration.Mode mode) throws SerializationException {
        final CommentedConfigurationNode node = Mockito.mock(CommentedConfigurationNode.class);
        Mockito.when(node.node("mode")).thenReturn(node);
        Mockito.when(node.set(mode)).thenReturn(node);
        final CommentedConfigurationNode randomTimeNode = Mockito.mock(CommentedConfigurationNode.class);
        Mockito.when(node.node("randomTime")).thenReturn(randomTimeNode);
        Mockito.when(randomTimeNode.virtual()).thenReturn(false);
        Mockito.when(randomTimeNode.getBoolean(Mockito.anyBoolean())).thenReturn(randomTime);

        final NoVersionConfigurationUpdate configUpdate = new NoVersionConfigurationUpdate();
        configUpdate.update(node);

        Mockito.verify(node).removeChild("randomTime");
        Mockito.verify(node).comment(QuestionTimeConfiguration.Comments.MODE);
    }

    @Test
    void randomTimeFieldNotDefined() throws SerializationException {
        final CommentedConfigurationNode node = Mockito.mock(CommentedConfigurationNode.class);
        Mockito.when(node.node("mode")).thenReturn(node);
        Mockito.when(node.set(QuestionTimeConfiguration.Mode.FIXED)).thenReturn(node);
        final CommentedConfigurationNode randomTimeNode = Mockito.mock(CommentedConfigurationNode.class);
        Mockito.when(node.node("randomTime")).thenReturn(randomTimeNode);
        Mockito.when(randomTimeNode.virtual()).thenReturn(true);

        final NoVersionConfigurationUpdate configUpdate = new NoVersionConfigurationUpdate();
        configUpdate.update(node);

        Mockito.verify(node, Mockito.never()).removeChild("randomTime");
        Mockito.verify(node).comment(QuestionTimeConfiguration.Comments.MODE);
    }

    @Test
    void versionCorrect() {
        final NoVersionConfigurationUpdate configUpdate = new NoVersionConfigurationUpdate();
        Assertions.assertEquals(0, configUpdate.getVersion());
    }

    static Stream<Arguments> values() {
        return Stream.of(
                Arguments.of(true, QuestionTimeConfiguration.Mode.INTERVAL),
                Arguments.of(false, QuestionTimeConfiguration.Mode.FIXED)
        );
    }

}