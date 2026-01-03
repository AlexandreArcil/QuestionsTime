package fr.canardnocturne.questionstime.config.upgrade.update;

import fr.canardnocturne.questionstime.question.component.Prize;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.List;

class FirstVersionConfigurationUpdateTest {

    @Test
    void movePrizeIntoPrizesNode() throws SerializationException {
        final CommentedConfigurationNode node = Mockito.mock(CommentedConfigurationNode.class);
        Mockito.when(node.node("questions")).thenReturn(node);
        final List<CommentedConfigurationNode> questionsNode = List.of(createQuestionNode(), createQuestionNode(),
                createQuestionNode());
        Mockito.when(node.childrenList()).thenReturn(questionsNode);

        final FirstVersionConfigurationUpdate configUpdate = new FirstVersionConfigurationUpdate();
        configUpdate.update(node);

        for (final CommentedConfigurationNode questionNode : questionsNode) {
            Mockito.verify(questionNode).removeChild("prize");
            Mockito.verify(questionNode.node("prizes")).setList(Mockito.eq(Prize.class), Mockito.anyList());
        }
    }

    @Test
    void noPrize() throws SerializationException {
        final CommentedConfigurationNode node = Mockito.mock(CommentedConfigurationNode.class);
        Mockito.when(node.node("questions")).thenReturn(node);
        final CommentedConfigurationNode questionNode = Mockito.mock(CommentedConfigurationNode.class);
        Mockito.when(questionNode.node("prize")).thenReturn(questionNode);
        Mockito.when(node.childrenList()).thenReturn(List.of(questionNode));

        final FirstVersionConfigurationUpdate configUpdate = new FirstVersionConfigurationUpdate();
        configUpdate.update(node);

        Mockito.verify(questionNode).removeChild("prize");
        Mockito.verify(questionNode, Mockito.never()).node("prizes");
    }

    @Test
    void versionCorrect() {
        final FirstVersionConfigurationUpdate configUpdate = new FirstVersionConfigurationUpdate();
        Assertions.assertEquals(1, configUpdate.getVersion());
    }

    private CommentedConfigurationNode createQuestionNode() throws SerializationException {
        final CommentedConfigurationNode questionNode = Mockito.mock(CommentedConfigurationNode.class);
        final CommentedConfigurationNode prizeNode = Mockito.mock(CommentedConfigurationNode.class);
        final CommentedConfigurationNode prizesNode = Mockito.mock(CommentedConfigurationNode.class);
        Mockito.when(questionNode.node("prize")).thenReturn(prizeNode);
        Mockito.when(prizeNode.get(Prize.class)).thenReturn(Prize.builder(0).build());
        Mockito.when(questionNode.node("prizes")).thenReturn(prizesNode);
        return questionNode;
    }

}