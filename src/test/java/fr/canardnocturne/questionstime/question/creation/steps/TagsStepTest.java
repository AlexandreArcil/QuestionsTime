package fr.canardnocturne.questionstime.question.creation.steps;

import fr.canardnocturne.questionstime.question.creation.QuestionCreator;
import fr.canardnocturne.questionstime.util.MiniMessageTest;
import net.kyori.adventure.audience.Audience;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TagsStepTest {

    @Test
    void questionDefined() {
        assertNotNull(TagsStep.INSTANCE.question());
    }

    @Test
    void addTags() {
        final String command = "add tag1; tag2";
        final QuestionCreator questionCreator = new QuestionCreator();
        final Audience audience = Mockito.mock(Audience.class);

        final boolean finished = TagsStep.INSTANCE.handle(audience, command, questionCreator);

        assertFalse(finished);
        assertEquals(2, questionCreator.getTags().size());
        assertTrue(questionCreator.getTags().contains("tag1"));
        assertTrue(questionCreator.getTags().contains("tag2"));
        Mockito.verify(audience).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).contains("Tags added: tag1, tag2")));
    }

    @Test
    void addOneTag() {
        final String command = "add tag1";
        final QuestionCreator questionCreator = new QuestionCreator();
        final Audience audience = Mockito.mock(Audience.class);

        final boolean finished = TagsStep.INSTANCE.handle(audience, command, questionCreator);

        assertFalse(finished);
        assertEquals(1, questionCreator.getTags().size());
        assertTrue(questionCreator.getTags().contains("tag1"));
        Mockito.verify(audience).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).contains("Tag added: tag1")));
    }

    @Test
    void noTagsAdded() {
        final String command = "add tag1";
        final QuestionCreator questionCreator = new QuestionCreator();
        questionCreator.getTags().add("tag1");
        final Audience audience = Mockito.mock(Audience.class);

        final boolean finished = TagsStep.INSTANCE.handle(audience, command, questionCreator);

        assertFalse(finished);
        assertEquals(1, questionCreator.getTags().size());
        assertTrue(questionCreator.getTags().contains("tag1"));
        Mockito.verify(audience).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).contains("No new tags to add")));
    }

    @Test
    void missingTagForAddCommand() {
        final String command = "add";
        final QuestionCreator questionCreator = new QuestionCreator();
        final Audience audience = Mockito.mock(Audience.class);

        final boolean finished = TagsStep.INSTANCE.handle(audience, command, questionCreator);

        assertFalse(finished);
        Mockito.verify(audience).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).contains("Command add needs to be followed by tags")));
    }

    @Test
    void removeTags() {
        final String command = "remove tag1; tag2";
        final QuestionCreator questionCreator = new QuestionCreator();
        questionCreator.getTags().addAll(List.of("tag1", "tag2", "tag3"));
        final Audience audience = Mockito.mock(Audience.class);

        final boolean finished = TagsStep.INSTANCE.handle(audience, command, questionCreator);

        assertFalse(finished);
        assertEquals(1, questionCreator.getTags().size());
        assertTrue(questionCreator.getTags().contains("tag3"));
        Mockito.verify(audience).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).contains("Tags removed: tag1, tag2")));
    }

    @Test
    void removeOneTag() {
        final String command = "remove tag1";
        final QuestionCreator questionCreator = new QuestionCreator();
        questionCreator.getTags().add("tag1");
        final Audience audience = Mockito.mock(Audience.class);

        final boolean finished = TagsStep.INSTANCE.handle(audience, command, questionCreator);

        assertFalse(finished);
        assertTrue(questionCreator.getTags().isEmpty());
        Mockito.verify(audience).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).contains("Tag removed: tag1")));
    }

    @Test
    void noTagsRemoved() {
        final String command = "remove tag1";
        final QuestionCreator questionCreator = new QuestionCreator();
        questionCreator.getTags().add("tag2");
        final Audience audience = Mockito.mock(Audience.class);

        final boolean finished = TagsStep.INSTANCE.handle(audience, command, questionCreator);

        assertFalse(finished);
        assertEquals(1, questionCreator.getTags().size());
        assertTrue(questionCreator.getTags().contains("tag2"));
        Mockito.verify(audience).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).contains("No tags removed")));
    }

    @Test
    void missingTagForRemoveCommand() {
        final String command = "remove";
        final QuestionCreator questionCreator = new QuestionCreator();
        final Audience audience = Mockito.mock(Audience.class);

        final boolean finished = TagsStep.INSTANCE.handle(audience, command, questionCreator);

        assertFalse(finished);
        Mockito.verify(audience).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).contains("Command remove needs to be followed by tags")));
    }
    
    @Test
    void listTags() {
        final String command = "list";
        final QuestionCreator questionCreator = new QuestionCreator();
        questionCreator.getTags().addAll(List.of("tag1", "tag2", "tag3"));
        final Audience audience = Mockito.mock(Audience.class);

        final boolean finished = TagsStep.INSTANCE.handle(audience, command, questionCreator);

        assertFalse(finished);
        Mockito.verify(audience).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.containsAll(component, "Tags: ", "[X] tag1", "[X] tag2", "[X] tag3")));
    }

    @Test
    void listNoTags() {
        final String command = "list";
        final QuestionCreator questionCreator = new QuestionCreator();
        final Audience audience = Mockito.mock(Audience.class);

        final boolean finished = TagsStep.INSTANCE.handle(audience, command, questionCreator);

        assertFalse(finished);
        Mockito.verify(audience).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).contains("No tags have been added yet")));
    }

    @Test
    void unknownCommand() {
        final String command = "unknownCommand";
        final QuestionCreator questionCreator = new QuestionCreator();
        final Audience audience = Mockito.mock(Audience.class);

        final boolean finished = TagsStep.INSTANCE.handle(audience, command, questionCreator);

        assertFalse(finished);
        Mockito.verify(audience).sendMessage(Mockito.argThat(component ->
                MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).contains("Unknown command. Use add, remove or list")));
    }

    @Test
    void confirmCommand() {
        final String command = "confirm";
        final QuestionCreator questionCreator = new QuestionCreator();
        final Audience audience = Mockito.mock(Audience.class);

        final boolean finished = TagsStep.INSTANCE.handle(audience, command, questionCreator);

        assertTrue(finished);
    }

    @Test
    void shouldNotSkip() {
        assertFalse(TagsStep.INSTANCE.shouldSkip(null));
    }

    @Test
    void lastStep() {
        assertNull(TagsStep.INSTANCE.next(null));
    }

}