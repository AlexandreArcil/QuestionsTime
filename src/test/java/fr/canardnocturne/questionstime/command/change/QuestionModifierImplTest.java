package fr.canardnocturne.questionstime.command.change;

import fr.canardnocturne.questionstime.question.Question;
import fr.canardnocturne.questionstime.question.QuestionComponent;
import fr.canardnocturne.questionstime.question.component.Malus;
import fr.canardnocturne.questionstime.question.component.OutcomeCommand;
import fr.canardnocturne.questionstime.question.component.Prize;
import fr.canardnocturne.questionstime.question.modifier.QuestionModifier;
import fr.canardnocturne.questionstime.question.modifier.QuestionModifierImpl;
import fr.canardnocturne.questionstime.question.serializer.ItemStackSerializer;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class QuestionModifierImplTest {

    @Test
    void setQuestion() {
        final String newQuestion = "new question ?";
        final Question question = Question.builder().setQuestion("test ?").setAnswers(Set.of("wow")).setWeight(1).build();

        final QuestionModifier questionModifier = new QuestionModifierImpl();
        final Question modifiedQuestion = questionModifier.set(question, QuestionComponent.QUESTION, newQuestion);

        assertEquals(newQuestion, modifiedQuestion.getQuestion());
    }

    @Test
    void setWeight() {
        final int newWeight = 10;
        final Question question = Question.builder().setQuestion("test ?").setAnswers(Set.of("wow")).setWeight(1).build();

        final QuestionModifier questionModifier = new QuestionModifierImpl();
        final Question modifiedQuestion = questionModifier.set(question, QuestionComponent.WEIGHT, newWeight);

        assertEquals(newWeight, modifiedQuestion.getWeight());
    }

    @Test
    void setTimer() {
        final int newTimer = 10;
        final Question question = Question.builder().setQuestion("test ?").setAnswers(Set.of("wow")).setWeight(1).build();

        final QuestionModifier questionModifier = new QuestionModifierImpl();
        final Question modifiedQuestion = questionModifier.set(question, QuestionComponent.TIMER, newTimer);

        assertEquals(newTimer, modifiedQuestion.getTimer());
    }

    @Test
    void setMalusMoneyToInexistentMalus() {
        final int newMalusMoney = 10;
        final Question question = Question.builder().setQuestion("test ?").setAnswers(Set.of("wow")).setWeight(1).build();

        final QuestionModifier questionModifier = new QuestionModifierImpl();
        final Question modifiedQuestion = questionModifier.set(question, QuestionComponent.MALUS_MONEY, newMalusMoney);

        assertTrue(modifiedQuestion.getMalus().isPresent());
        assertEquals(newMalusMoney, modifiedQuestion.getMalus().get().getMoney());
    }

    @Test
    void setMalusMoneyToExistentMalus() {
        final int newMalusMoney = 10;
        final Question question = Question.builder().setQuestion("test ?").setAnswers(Set.of("wow"))
                .setMalus(new Malus(50, false, new OutcomeCommand[0])).setWeight(1).build();

        final QuestionModifier questionModifier = new QuestionModifierImpl();
        final Question modifiedQuestion = questionModifier.set(question, QuestionComponent.MALUS_MONEY, newMalusMoney);

        assertTrue(modifiedQuestion.getMalus().isPresent());
        assertEquals(newMalusMoney, modifiedQuestion.getMalus().get().getMoney());
    }

    @Test
    void setPrizeMoneyInexistentPrize() {
        final int newPrizeMoney = 10;
        final Question question = Question.builder().setQuestion("test ?").setAnswers(Set.of("wow")).setWeight(1).build();

        final QuestionModifier questionModifier = new QuestionModifierImpl();
        final Question modifiedQuestion = questionModifier.set(question, QuestionComponent.PRIZE_MONEY, 1, newPrizeMoney);

        assertEquals(newPrizeMoney, modifiedQuestion.getPrizes().getFirst().getMoney());
    }

    @Test
    void setPrizeMoneyExistentPrize() {
        final int newPrizeMoney = 10;
        final Question question = Question.builder().setQuestion("test ?").setAnswers(Set.of("wow")).setWeight(1)
                .setPrizes(Set.of(new Prize(50, false, new ItemStack[0], new OutcomeCommand[0], 1))).build();

        final QuestionModifier questionModifier = new QuestionModifierImpl();
        final Question modifiedQuestion = questionModifier.set(question, QuestionComponent.PRIZE_MONEY, 1, newPrizeMoney);

        assertEquals(newPrizeMoney, modifiedQuestion.getPrizes().getFirst().getMoney());
    }

    @Test
    void setPrizeMoneyRemovePrize() {
        final int newPrizeMoney = 0;
        final Question question = Question.builder().setQuestion("test ?").setAnswers(Set.of("wow")).setWeight(1)
                .setPrizes(Set.of(new Prize(50, false, new ItemStack[0], new OutcomeCommand[0], 1))).build();

        final QuestionModifier questionModifier = new QuestionModifierImpl();
        final Question modifiedQuestion = questionModifier.set(question, QuestionComponent.PRIZE_MONEY, 1, newPrizeMoney);

        assertTrue(modifiedQuestion.getPrizes().isEmpty());
    }

    @Test
    void setPrizeAnnounceInexistentPrize() {
        final boolean newPrizeAnnounce = true;
        final Question question = Question.builder().setQuestion("test ?").setAnswers(Set.of("wow")).setWeight(1).build();

        final QuestionModifier questionModifier = new QuestionModifierImpl();
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> questionModifier.set(question, QuestionComponent.PRIZE_ANNOUNCE, 1, newPrizeAnnounce));

        assertEquals("No prize with position 1 is present in the question", exception.getMessage());
    }

    @Test
    void setPrizeAnnounceExistentPrize() {
        final boolean newPrizeAnnounce = true;
        final Question question = Question.builder().setQuestion("test ?").setAnswers(Set.of("wow")).setWeight(1)
                .setPrizes(Set.of(new Prize(50, false, new ItemStack[0], new OutcomeCommand[0], 1))).build();

        final QuestionModifier questionModifier = new QuestionModifierImpl();
        final Question modifiedQuestion = questionModifier.set(question, QuestionComponent.PRIZE_ANNOUNCE, 1, newPrizeAnnounce);

        assertEquals(newPrizeAnnounce, modifiedQuestion.getPrizes().getFirst().isAnnounce());
    }

    @Test
    void addPrizeItemsInexistentPrize() {
        final String newPrizeItems = "stone";
        final Question question = Question.builder().setQuestion("test ?").setAnswers(Set.of("wow")).setWeight(1).build();

        try(final var itemStackSerializerMock = Mockito.mockStatic(ItemStackSerializer.class)) {
            itemStackSerializerMock.when(() -> ItemStackSerializer.fromString(newPrizeItems))
                    .thenReturn(Mockito.mock(ItemStack.class));

            final QuestionModifier questionModifier = new QuestionModifierImpl();
            final Question modifiedQuestion = questionModifier.add(question, QuestionComponent.PRIZE_ITEMS, 1, newPrizeItems);

            assertFalse(modifiedQuestion.getPrizes().isEmpty());
        }
    }

    @Test
    void addPrizeItemsExistentPrize() {
        final String newPrizeItems = "stone";
        final Question question = Question.builder().setQuestion("test ?").setAnswers(Set.of("wow")).setWeight(1)
                .setPrizes(Set.of(new Prize(50, false, new ItemStack[0], new OutcomeCommand[0], 1))).build();

        try(final var itemStackSerializerMock = Mockito.mockStatic(ItemStackSerializer.class)) {
            itemStackSerializerMock.when(() -> ItemStackSerializer.fromString(newPrizeItems))
                    .thenReturn(Mockito.mock(ItemStack.class));

            final QuestionModifier questionModifier = new QuestionModifierImpl();
            final Question modifiedQuestion = questionModifier.add(question, QuestionComponent.PRIZE_ITEMS, 1, newPrizeItems);

            assertFalse(modifiedQuestion.getPrizes().isEmpty());
        }
    }

    @Test
    void addPrizeCommandsInexistentPrize() {
        final String newCommand = "message;cmd";
        final Question question = Question.builder().setQuestion("test ?").setAnswers(Set.of("wow")).setWeight(1).build();

        final QuestionModifier questionModifier = new QuestionModifierImpl();
        final Question modifiedQuestion = questionModifier.add(question, QuestionComponent.PRIZE_COMMANDS, 1, newCommand);

        assertEquals(newCommand, modifiedQuestion.getPrizes().getFirst().getCommands()[0].toString());
    }

    @Test
    void addPrizeCommandsExistentPrize() {
        final String newCommand = "message;cmd";
        final Question question = Question.builder().setQuestion("test ?").setAnswers(Set.of("wow")).setWeight(1)
                .setPrizes(Set.of(new Prize(50, false, new ItemStack[0], new OutcomeCommand[0], 1))).build();

        final QuestionModifier questionModifier = new QuestionModifierImpl();
        final Question modifiedQuestion = questionModifier.add(question, QuestionComponent.PRIZE_COMMANDS, 1, newCommand);

        assertEquals(newCommand, modifiedQuestion.getPrizes().getFirst().getCommands()[0].toString());
    }

    @Test
    void setMalusAnnounceInexistentMalus() {
        final boolean newMalusAnnounce = true;
        final Question question = Question.builder().setQuestion("test ?").setAnswers(Set.of("wow")).setWeight(1).build();

        final QuestionModifier questionModifier = new QuestionModifierImpl();
        final Question modifiedQuestion = questionModifier.set(question, QuestionComponent.MALUS_ANNOUNCE, newMalusAnnounce);

        assertTrue(modifiedQuestion.getMalus().isPresent());
        assertEquals(newMalusAnnounce, modifiedQuestion.getMalus().get().isAnnounce());
    }

    @Test
    void setMalusAnnounceExistentMalus() {
        final boolean newMalusAnnounce = true;
        final Question question = Question.builder().setQuestion("test ?").setAnswers(Set.of("wow")).setWeight(1)
                .setMalus(new Malus(50, false, new OutcomeCommand[0])).build();

        final QuestionModifier questionModifier = new QuestionModifierImpl();
        final Question modifiedQuestion = questionModifier.set(question, QuestionComponent.MALUS_ANNOUNCE, newMalusAnnounce);

        assertTrue(modifiedQuestion.getMalus().isPresent());
        assertEquals(newMalusAnnounce, modifiedQuestion.getMalus().get().isAnnounce());
    }

    @Test
    void addAnswer() {
        final String newAnswer = "new answer";
        final Question question = Question.builder().setQuestion("test ?").setAnswers(Set.of("wow")).setWeight(1).build();

        final QuestionModifier questionModifier = new QuestionModifierImpl();
        final Question modifiedQuestion = questionModifier.add(question, QuestionComponent.ANSWERS, newAnswer);

        assertTrue(modifiedQuestion.getAnswers().contains(newAnswer));
    }

    @Test
    void addPropositions() {
        final String newPropositions = "proposition1;wow";
        final Question question = Question.builder().setQuestion("test ?").setAnswers(Set.of("wow")).setWeight(1).build();

        final QuestionModifier questionModifier = new QuestionModifierImpl();
        final Question modifiedQuestion = questionModifier.add(question, QuestionComponent.PROPOSITIONS, newPropositions);

        assertEquals(2, modifiedQuestion.getPropositions().size());
        assertTrue(modifiedQuestion.getPropositions().contains("proposition1"));
        assertTrue(modifiedQuestion.getPropositions().contains("wow"));
    }

    @Test
    void addMalusCommandInexistentMalus() {
        final String newMalusCommand = "malusCommand1;cmd";
        final Question question = Question.builder().setQuestion("test ?").setAnswers(Set.of("wow")).setWeight(1).build();

        final QuestionModifier questionModifier = new QuestionModifierImpl();
        final Question modifiedQuestion = questionModifier.add(question, QuestionComponent.MALUS_COMMANDS, newMalusCommand);

        assertTrue(modifiedQuestion.getMalus().isPresent());
        assertEquals(newMalusCommand, modifiedQuestion.getMalus().get().getCommands()[0].toString());
    }

    @Test
    void addMalusCommandsExistentMalus() {
        final String newMalusCommand = "malusCommand1;cmd";
        final Question question = Question.builder().setQuestion("test ?").setAnswers(Set.of("wow")).setWeight(1)
                .setMalus(new Malus(50, false, new OutcomeCommand[0])).build();

        final QuestionModifier questionModifier = new QuestionModifierImpl();
        final Question modifiedQuestion = questionModifier.add(question, QuestionComponent.MALUS_COMMANDS, newMalusCommand);

        assertTrue(modifiedQuestion.getMalus().isPresent());
        assertEquals(newMalusCommand, modifiedQuestion.getMalus().get().getCommands()[0].toString());
    }

    @Test
    void removeAnswer() {
        final String answerToRemove = "wow";
        final Question question = Question.builder().setQuestion("test ?").setAnswers(Set.of(answerToRemove, "test")).setWeight(1).build();

        final QuestionModifier questionModifier = new QuestionModifierImpl();
        final Question modifiedQuestion = questionModifier.remove(question, QuestionComponent.ANSWERS, answerToRemove);

        assertFalse(modifiedQuestion.getAnswers().contains(answerToRemove));
    }

    @Test
    void removeAnswerNotInQuestion() {
        final String answerToRemove = "wow";
        final Question question = Question.builder().setQuestion("test ?").setAnswers(Set.of("test")).setWeight(1).build();

        final QuestionModifier questionModifier = new QuestionModifierImpl();
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> questionModifier.remove(question, QuestionComponent.ANSWERS, answerToRemove));

        assertEquals("Answer 'wow' is not present in the question", exception.getMessage());
    }

    @Test
    void removePropositions() {
        final String propositionsToRemove = "proposition1;proposition2";
        final Question question = Question.builder().setQuestion("test ?").setAnswers(Set.of("proposition3")).setWeight(1)
                .setPropositions(List.of("proposition1", "proposition2", "proposition3", "proposition4")).build();

        final QuestionModifier questionModifier = new QuestionModifierImpl();
        final Question modifiedQuestion = questionModifier.remove(question, QuestionComponent.PROPOSITIONS, propositionsToRemove);

        assertFalse(modifiedQuestion.getPropositions().contains("proposition1"));
        assertFalse(modifiedQuestion.getPropositions().contains("proposition2"));
        assertTrue(modifiedQuestion.getPropositions().contains("proposition3"));
        assertTrue(modifiedQuestion.getPropositions().contains("proposition4"));
    }

    @Test
    void removePropositionsNotInQuestion() {
        final String propositionsToRemove = "proposition1;proposition2";
        final Question question = Question.builder().setQuestion("test ?").setAnswers(Set.of("proposition3")).setWeight(1)
                .setPropositions(List.of("proposition3", "proposition4")).build();

        final QuestionModifier questionModifier = new QuestionModifierImpl();
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> questionModifier.remove(question, QuestionComponent.PROPOSITIONS, propositionsToRemove));

        assertEquals("Proposition(s) 'proposition1, proposition2' is/are not present in the question", exception.getMessage());
    }

    @Test
    void removeMalusCommandsDeleteMalus() {
        final String malusCommand = "malusCommand;cmd";
        final Question question = Question.builder().setQuestion("test ?").setAnswers(Set.of("wow")).setWeight(1)
                .setMalus(new Malus(0, false, new OutcomeCommand[] {new OutcomeCommand("malusCommand", "cmd")})).build();

        final QuestionModifier questionModifier = new QuestionModifierImpl();
        final Question modifiedQuestion = questionModifier.remove(question, QuestionComponent.MALUS_COMMANDS, malusCommand);

        assertFalse(modifiedQuestion.getMalus().isPresent());
    }

    @Test
    void removeMalusCommands() {
        final String malusCommand = "malusCommand;cmd";
        final Question question = Question.builder().setQuestion("test ?").setAnswers(Set.of("wow")).setWeight(1)
                .setMalus(new Malus(50, false, new OutcomeCommand[] {new OutcomeCommand("malusCommand", "cmd")})).build();

        final QuestionModifier questionModifier = new QuestionModifierImpl();
        final Question modifiedQuestion = questionModifier.remove(question, QuestionComponent.MALUS_COMMANDS, malusCommand);

        assertTrue(modifiedQuestion.getMalus().isPresent());
        assertEquals(0, modifiedQuestion.getMalus().get().getCommands().length);
    }

    @Test
    void removeMalusCommandsInexistentMalus() {
        final String malusCommand = "malusCommand;cmd";
        final Question question = Question.builder().setQuestion("test ?").setAnswers(Set.of("wow")).setWeight(1).build();

        final QuestionModifier questionModifier = new QuestionModifierImpl();
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> questionModifier.remove(question, QuestionComponent.MALUS_COMMANDS, malusCommand));

        assertEquals("No malus is present in the question", exception.getMessage());
    }

    @Test
    void removeInexistentMalusCommand() {
        final String malusCommand = "malusCommand;cmd";
        final Question question = Question.builder().setQuestion("test ?").setAnswers(Set.of("wow")).setWeight(1)
                .setMalus(new Malus(50, false, new OutcomeCommand[0])).build();

        final QuestionModifier questionModifier = new QuestionModifierImpl();
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> questionModifier.remove(question, QuestionComponent.MALUS_COMMANDS, malusCommand));

        assertEquals("Command 'malusCommand;cmd' not found in malus", exception.getMessage());
    }

    @Test
    void removePrizeItems() {
        final String removePrizeItems = "stone";
        final ItemStack is = Mockito.mock(ItemStack.class);
        Mockito.when(is.equalTo(Mockito.any())).thenReturn(true);
        Mockito.when(is.copy()).thenReturn(is);
        final Question question = Question.builder().setQuestion("test ?").setAnswers(Set.of("wow")).setWeight(1)
                .setPrizes(Set.of(new Prize(50, false, new ItemStack[] {is}, new OutcomeCommand[0], 1))).build();

        try(final var itemStackSerializerMock = Mockito.mockStatic(ItemStackSerializer.class)) {
            itemStackSerializerMock.when(() -> ItemStackSerializer.fromString(removePrizeItems))
                    .thenReturn(Mockito.mock(ItemStack.class));

            final QuestionModifier questionModifier = new QuestionModifierImpl();
            final Question modifiedQuestion = questionModifier.remove(question, QuestionComponent.PRIZE_ITEMS, 1, removePrizeItems);

            assertEquals(1, modifiedQuestion.getPrizes().size());
            assertEquals(0, modifiedQuestion.getPrizes().getFirst().getItemStacks().length);
        }
    }

    @Test
    void removePrizeItemsRemovePrize() {
        final String removePrizeItems = "stone";
        final ItemStack is = Mockito.mock(ItemStack.class);
        Mockito.when(is.equalTo(Mockito.any())).thenReturn(true);
        Mockito.when(is.copy()).thenReturn(is);
        final Question question = Question.builder().setQuestion("test ?").setAnswers(Set.of("wow")).setWeight(1)
                .setPrizes(Set.of(new Prize(0, false, new ItemStack[] {is}, new OutcomeCommand[0], 1))).build();

        try(final var itemStackSerializerMock = Mockito.mockStatic(ItemStackSerializer.class)) {
            itemStackSerializerMock.when(() -> ItemStackSerializer.fromString(removePrizeItems))
                    .thenReturn(Mockito.mock(ItemStack.class));

            final QuestionModifier questionModifier = new QuestionModifierImpl();
            final Question modifiedQuestion = questionModifier.remove(question, QuestionComponent.PRIZE_ITEMS, 1, removePrizeItems);

            assertTrue(modifiedQuestion.getPrizes().isEmpty());
        }
    }

    @Test
    void removePrizeItemsInexistentPosition() {
        final String removePrizeItems = "stone";
        final ItemStack is = Mockito.mock(ItemStack.class);
        final Question question = Question.builder().setQuestion("test ?").setAnswers(Set.of("wow")).setWeight(1)
                .setPrizes(Set.of(new Prize(50, false, new ItemStack[] {is}, new OutcomeCommand[0], 1))).build();

        try(final var itemStackSerializerMock = Mockito.mockStatic(ItemStackSerializer.class)) {
            itemStackSerializerMock.when(() -> ItemStackSerializer.fromString(removePrizeItems))
                    .thenReturn(Mockito.mock(ItemStack.class));

            final QuestionModifier questionModifier = new QuestionModifierImpl();
            final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> questionModifier.remove(question, QuestionComponent.PRIZE_ITEMS, 2, removePrizeItems));

            assertEquals("Prize with position 2 not found", exception.getMessage());
        }
    }

    @Test
    void removeInexistentPrizeItems() {
        final String removePrizeItems = "stone";
        final ItemStack is = Mockito.mock(ItemStack.class);
        Mockito.when(is.equalTo(Mockito.any())).thenReturn(false);
        Mockito.when(is.copy()).thenReturn(is);
        final Question question = Question.builder().setQuestion("test ?").setAnswers(Set.of("wow")).setWeight(1)
                .setPrizes(Set.of(new Prize(50, false, new ItemStack[] {is}, new OutcomeCommand[0], 1))).build();

        try(final var itemStackSerializerMock = Mockito.mockStatic(ItemStackSerializer.class)) {
            itemStackSerializerMock.when(() -> ItemStackSerializer.fromString(removePrizeItems))
                    .thenReturn(Mockito.mock(ItemStack.class));

            final QuestionModifier questionModifier = new QuestionModifierImpl();
            final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> questionModifier.remove(question, QuestionComponent.PRIZE_ITEMS, 1, removePrizeItems));

            assertEquals("Item 'stone' not found in prize with position 1", exception.getMessage());
        }
    }

    @Test
    void removePrizeCommands() {
        final String removePrizeCommand = "message;cmd";
        final Question question = Question.builder().setQuestion("test ?").setAnswers(Set.of("wow")).setWeight(1)
                .setPrizes(Set.of(new Prize(50, false, new ItemStack[0], new OutcomeCommand[] {new OutcomeCommand("message", "cmd")}, 1))).build();

        final QuestionModifier questionModifier = new QuestionModifierImpl();
        final Question modifiedQuestion = questionModifier.remove(question, QuestionComponent.PRIZE_COMMANDS, 1, removePrizeCommand);

        assertEquals(1, modifiedQuestion.getPrizes().size());
        assertEquals(0, modifiedQuestion.getPrizes().getFirst().getCommands().length);
    }

    @Test
    void removePrizeCommandsRemovePrize() {
        final String removePrizeCommand = "message;cmd";
        final Question question = Question.builder().setQuestion("test ?").setAnswers(Set.of("wow")).setWeight(1)
                .setPrizes(Set.of(new Prize(0, false, new ItemStack[0], new OutcomeCommand[] {new OutcomeCommand("message", "cmd")}, 1))).build();

        final QuestionModifier questionModifier = new QuestionModifierImpl();
        final Question modifiedQuestion = questionModifier.remove(question, QuestionComponent.PRIZE_COMMANDS, 1, removePrizeCommand);

        assertTrue(modifiedQuestion.getPrizes().isEmpty());
    }

    @Test
    void removePrizeCommandsInexistentPosition() {
        final String removePrizeCommand = "message;cmd";
        final Question question = Question.builder().setQuestion("test ?").setAnswers(Set.of("wow")).setWeight(1)
                .setPrizes(Set.of(new Prize(50, false, new ItemStack[0], new OutcomeCommand[0], 1))).build();

        final QuestionModifier questionModifier = new QuestionModifierImpl();
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> questionModifier.remove(question, QuestionComponent.PRIZE_COMMANDS, 2, removePrizeCommand));

        assertEquals("Prize with position 2 not found", exception.getMessage());
    }

    @Test
    void removeInexistentPrizeCommands() {
        final String removePrizeCommand = "message;cmd";
        final Question question = Question.builder().setQuestion("test ?").setAnswers(Set.of("wow")).setWeight(1)
                .setPrizes(Set.of(new Prize(50, false, new ItemStack[0], new OutcomeCommand[0], 1))).build();

        final QuestionModifier questionModifier = new QuestionModifierImpl();
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> questionModifier.remove(question, QuestionComponent.PRIZE_COMMANDS, 1, removePrizeCommand));

        assertEquals("Command 'message;cmd' not found in prize with position 1", exception.getMessage());
    }

    //TODO finir de tester le dernier remove => set commande pour question terminer !

}