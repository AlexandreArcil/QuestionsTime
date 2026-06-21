package fr.canardnocturne.questionstime.config.upgrade.update;

import fr.canardnocturne.questionstime.config.upgrade.ConfigurationUpgradeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.Collections;
import java.util.List;

class SecondVersionConfigurationUpdateTest {

    @Test
    void replaceOldValues() throws SerializationException, ConfigurationUpgradeException {
        final CommentedConfigurationNode node = Mockito.mock(CommentedConfigurationNode.class);
        Mockito.when(node.node("questions")).thenReturn(node);
        Mockito.when(node.childrenList()).thenReturn(List.of(node));
        Mockito.when(node.node("answer")).thenReturn(node);
        Mockito.when(node.hasChild("proposition")).thenReturn(true);
        Mockito.when(node.node("proposition")).thenReturn(node);
        Mockito.when(node.getList(String.class, Collections.emptyList())).thenReturn(List.of("1"), List.of("proposition1", "proposition2"));

        final SecondVersionConfigurationUpdate configUpdate = new SecondVersionConfigurationUpdate();
        configUpdate.update(node);

        Mockito.verify(node).setList(String.class, List.of("proposition1"));
    }

    @Test
    void noQuestion() throws SerializationException, ConfigurationUpgradeException {
        final CommentedConfigurationNode node = Mockito.mock(CommentedConfigurationNode.class);
        Mockito.when(node.node("questions")).thenReturn(node);
        Mockito.when(node.childrenList()).thenReturn(Collections.emptyList());

        final SecondVersionConfigurationUpdate configUpdate = new SecondVersionConfigurationUpdate();
        configUpdate.update(node);

        Mockito.verify(node, Mockito.never()).setList(Mockito.eq(String.class), Mockito.anyList());
    }

    @Test
    void noAnswer() throws SerializationException, ConfigurationUpgradeException {
        final CommentedConfigurationNode node = Mockito.mock(CommentedConfigurationNode.class);
        Mockito.when(node.node("questions")).thenReturn(node);
        Mockito.when(node.childrenList()).thenReturn(List.of(node));
        Mockito.when(node.node("answer")).thenReturn(node);
        Mockito.when(node.getList(String.class, Collections.emptyList())).thenReturn(Collections.emptyList());

        final SecondVersionConfigurationUpdate configUpdate = new SecondVersionConfigurationUpdate();
        configUpdate.update(node);

        Mockito.verify(node, Mockito.never()).setList(Mockito.eq(String.class), Mockito.anyList());
    }

    @Test
    void noProposition() throws SerializationException, ConfigurationUpgradeException {
        final CommentedConfigurationNode node = Mockito.mock(CommentedConfigurationNode.class);
        Mockito.when(node.node("questions")).thenReturn(node);
        Mockito.when(node.childrenList()).thenReturn(List.of(node));
        Mockito.when(node.node("answer")).thenReturn(node);
        Mockito.when(node.getList(String.class, Collections.emptyList())).thenReturn(List.of("1"));
        Mockito.when(node.hasChild("proposition")).thenReturn(false);

        final SecondVersionConfigurationUpdate configUpdate = new SecondVersionConfigurationUpdate();
        configUpdate.update(node);

        Mockito.verify(node, Mockito.never()).setList(Mockito.eq(String.class), Mockito.anyList());
    }

    @Test
    void propositionEmpty() throws SerializationException {
        final CommentedConfigurationNode node = Mockito.mock(CommentedConfigurationNode.class);
        Mockito.when(node.node("questions")).thenReturn(node);
        Mockito.when(node.childrenList()).thenReturn(List.of(node));
        Mockito.when(node.node("answer")).thenReturn(node);
        Mockito.when(node.getList(String.class, Collections.emptyList())).thenReturn(List.of("1"), Collections.emptyList());
        Mockito.when(node.hasChild("proposition")).thenReturn(true);
        Mockito.when(node.node("proposition")).thenReturn(node);

        final SecondVersionConfigurationUpdate configUpdate = new SecondVersionConfigurationUpdate();
        ConfigurationUpgradeException exception = Assertions.assertThrows(ConfigurationUpgradeException.class, () -> configUpdate.update(node));

        Assertions.assertEquals("'proposition' should not be empty", exception.getMessage());
        Mockito.verify(node, Mockito.never()).setList(Mockito.eq(String.class), Mockito.anyList());
    }

    @Test
    void answerNotNumber() throws SerializationException {
        final CommentedConfigurationNode node = Mockito.mock(CommentedConfigurationNode.class);
        Mockito.when(node.node("questions")).thenReturn(node);
        Mockito.when(node.childrenList()).thenReturn(List.of(node));
        Mockito.when(node.node("answer")).thenReturn(node);
        Mockito.when(node.getList(String.class, Collections.emptyList())).thenReturn(List.of("a"), List.of("proposition1"));
        Mockito.when(node.hasChild("proposition")).thenReturn(true);
        Mockito.when(node.node("proposition")).thenReturn(node);

        final SecondVersionConfigurationUpdate configUpdate = new SecondVersionConfigurationUpdate();
        ConfigurationUpgradeException exception = Assertions.assertThrows(ConfigurationUpgradeException.class, () -> configUpdate.update(node));

        Assertions.assertEquals("The answer 'a' is not a number", exception.getMessage());
        Mockito.verify(node, Mockito.never()).setList(Mockito.eq(String.class), Mockito.anyList());
    }

    @Test
    void answerNumberHigherPropositions() throws SerializationException {
        final CommentedConfigurationNode node = Mockito.mock(CommentedConfigurationNode.class);
        Mockito.when(node.node("questions")).thenReturn(node);
        Mockito.when(node.childrenList()).thenReturn(List.of(node));
        Mockito.when(node.node("answer")).thenReturn(node);
        Mockito.when(node.getList(String.class, Collections.emptyList())).thenReturn(List.of("5"), List.of("proposition1", "proposition2"));
        Mockito.when(node.hasChild("proposition")).thenReturn(true);
        Mockito.when(node.node("proposition")).thenReturn(node);

        final SecondVersionConfigurationUpdate configUpdate = new SecondVersionConfigurationUpdate();
        ConfigurationUpgradeException exception = Assertions.assertThrows(ConfigurationUpgradeException.class, () -> configUpdate.update(node));

        Assertions.assertEquals("The answer '5' must be a number between 1 and 2", exception.getMessage());
        Mockito.verify(node, Mockito.never()).setList(Mockito.eq(String.class), Mockito.anyList());
    }

}