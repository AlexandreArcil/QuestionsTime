package fr.canardnocturne.questionstime.question.creation;

import fr.canardnocturne.questionstime.question.component.OutcomeCommand;
import fr.canardnocturne.questionstime.question.type.Question;
import io.leangen.geantyref.TypeToken;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.spongepowered.api.Game;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.Key;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackComparators;
import org.spongepowered.api.registry.BuilderProvider;
import org.spongepowered.api.registry.FactoryProvider;

import java.time.LocalTime;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QuestionCreatorTest {

    @Test
    void addItemPrizeFirstTime() {
        final ItemStack is = Mockito.mock(ItemStack.class);

        final QuestionCreator creator = new QuestionCreator();
        creator.addItemPrize(1, is);

        assertTrue(creator.getItemsPrize().containsKey(1));
        assertEquals(1, creator.getItemsPrize().get(1).size());
        assertEquals(is, creator.getItemsPrize().get(1).getFirst());
    }

    @Test
    void addItemPrizeWithSameItemStack() {
        final ItemStack is1 = Mockito.mock(ItemStack.class);
        final ItemStack is2 = Mockito.mock(ItemStack.class);
        Mockito.when(is1.equalTo(is2)).thenReturn(true);
        Mockito.when(is1.quantity()).thenReturn(2);
        Mockito.when(is2.quantity()).thenReturn(3);

        final QuestionCreator creator = new QuestionCreator();
        creator.addItemPrize(1, is1);
        creator.addItemPrize(1, is2);

        assertTrue(creator.getItemsPrize().containsKey(1));
        assertEquals(1, creator.getItemsPrize().get(1).size());
        assertEquals(is1, creator.getItemsPrize().get(1).getFirst());
        Mockito.verify(is1).setQuantity(5);
    }

    @Test
    void addItemPrizeWithDifferentItemStack() {
        final ItemStack is1 = Mockito.mock(ItemStack.class);
        final ItemStack is2 = Mockito.mock(ItemStack.class);
        Mockito.when(is1.equalTo(is2)).thenReturn(false);

        final QuestionCreator creator = new QuestionCreator();
        creator.addItemPrize(1, is1);
        creator.addItemPrize(1, is2);

        assertTrue(creator.getItemsPrize().containsKey(1));
        assertEquals(2, creator.getItemsPrize().get(1).size());
        assertTrue(creator.getItemsPrize().get(1).contains(is1));
        assertTrue(creator.getItemsPrize().get(1).contains(is2));
    }

    @Test
    void addItemPrizeDifferentPositions() {
        final ItemStack is1 = Mockito.mock(ItemStack.class);
        final ItemStack is2 = Mockito.mock(ItemStack.class);

        final QuestionCreator creator = new QuestionCreator();
        creator.addItemPrize(1, is1);
        creator.addItemPrize(2, is2);

        assertTrue(creator.getItemsPrize().containsKey(1));
        assertTrue(creator.getItemsPrize().containsKey(2));
        assertEquals(1, creator.getItemsPrize().get(1).size());
        assertEquals(1, creator.getItemsPrize().get(2).size());
        assertEquals(is1, creator.getItemsPrize().get(1).getFirst());
        assertEquals(is2, creator.getItemsPrize().get(2).getFirst());
    }

    @Test
    void addCommandPrizes() {
        final OutcomeCommand cmd1 = Mockito.mock(OutcomeCommand.class);
        final OutcomeCommand cmd2 = Mockito.mock(OutcomeCommand.class);
        final OutcomeCommand cmd3 = Mockito.mock(OutcomeCommand.class);

        final QuestionCreator creator = new QuestionCreator();
        creator.addCommandPrize(1, cmd1);
        creator.addCommandPrize(1, cmd2);
        creator.addCommandPrize(2, cmd3);

        assertTrue(creator.getCommandsPrize().containsKey(1));
        assertTrue(creator.getCommandsPrize().containsKey(2));
        assertEquals(2, creator.getCommandsPrize().get(1).size());
        assertEquals(1, creator.getCommandsPrize().get(2).size());
        assertEquals(cmd1, creator.getCommandsPrize().get(1).getFirst());
        assertEquals(cmd2, creator.getCommandsPrize().get(1).get(1));
        assertEquals(cmd3, creator.getCommandsPrize().get(2).getFirst());
    }

    @Test
    void setTimeBetweenAnswer() {
        final QuestionCreator creator = new QuestionCreator();
        creator.setTimeBetweenAnswer(LocalTime.of(5, 5, 5));
        assertEquals(366100, creator.getTimeBetweenAnswer());
    }

    /*@Test
    void setDuration() {
        final QuestionCreator creator = new QuestionCreator();
        creator.setDuration(LocalTime.of(5, 5, 5));
        assertEquals(18305, creator.getDuration());
    }*/

    @Test
    void setMoneyPrizes() {
        final QuestionCreator creator = new QuestionCreator();
        creator.setMoneyPrize(1, 500);
        creator.setMoneyPrize(1, 50);
        creator.setMoneyPrize(2, 5);

        assertTrue(creator.getMoneyPrize().containsKey(1));
        assertTrue(creator.getMoneyPrize().containsKey(2));
        assertEquals(50, creator.getMoneyPrize().get(1));
        assertEquals(5, creator.getMoneyPrize().get(2));
    }

    @Test
    void removeItemPrize() {
        try(final MockedStatic<Sponge> spongeMock = Mockito.mockStatic(Sponge.class);
            final MockedStatic<ResourceKey> resourceKeyMock = Mockito.mockStatic(ResourceKey.class)) {
            final ItemStack is1 = Mockito.mock(ItemStack.class);
            final ItemStack is2 = Mockito.mock(ItemStack.class);
            final ItemStack is3 = Mockito.mock(ItemStack.class);
            final Game game = Mockito.mock(Game.class);
            final FactoryProvider factoryProvider = Mockito.mock(FactoryProvider.class);
            final ItemStackComparators.Factory isComparatorsFactory = Mockito.mock(ItemStackComparators.Factory.class);
            final Supplier<Comparator<ItemStack>> comparatorSupplier = Mockito.mock(Supplier.class);
            final Comparator<ItemStack> isComparator = Mockito.mock(Comparator.class);
            Mockito.when(isComparator.compare(is1, is1)).thenReturn(0);
            Mockito.when(isComparator.compare(is1, is2)).thenReturn(1);
            Mockito.when(isComparator.compare(is2, is1)).thenReturn(1);
            Mockito.when(comparatorSupplier.get()).thenReturn(isComparator);
            Mockito.when(isComparatorsFactory.byType()).thenReturn(isComparatorsFactory);
            Mockito.when(isComparatorsFactory.bySize()).thenReturn(isComparatorsFactory);
            Mockito.when(isComparatorsFactory.byDurability()).thenReturn(isComparatorsFactory);
            Mockito.when(isComparatorsFactory.byData()).thenReturn(isComparatorsFactory);
            Mockito.when(isComparatorsFactory.asSupplier()).thenReturn(comparatorSupplier);
            Mockito.when(game.factoryProvider()).thenReturn(factoryProvider);
            Mockito.when(factoryProvider.provide(ItemStackComparators.Factory.class)).thenReturn(isComparatorsFactory);
            spongeMock.when(Sponge::game).thenReturn(game);
            final ResourceKey resourceKey = Mockito.mock(ResourceKey.class);
            final BuilderProvider builderProvider = Mockito.mock(BuilderProvider.class);
            final Key.Builder keyBuilder = Mockito.mock(Key.Builder.class);
            Mockito.when(keyBuilder.key(Mockito.any())).thenReturn(keyBuilder);
            Mockito.when(keyBuilder.elementType(Mockito.any(Class.class))).thenReturn(keyBuilder);
            Mockito.when(keyBuilder.elementType(Mockito.any(TypeToken.class))).thenReturn(keyBuilder);
            Mockito.when(keyBuilder.listElementType(Mockito.any(Class.class))).thenReturn(keyBuilder);
            Mockito.when(keyBuilder.listElementType(Mockito.any(TypeToken.class))).thenReturn(keyBuilder);
            Mockito.when(keyBuilder.setElementType(Mockito.any(Class.class))).thenReturn(keyBuilder);
            Mockito.when(keyBuilder.setElementType(Mockito.any(TypeToken.class))).thenReturn(keyBuilder);
            Mockito.when(keyBuilder.mapElementType(Mockito.any(Class.class), Mockito.any(Class.class))).thenReturn(keyBuilder);
            Mockito.when(keyBuilder.mapElementType(Mockito.any(TypeToken.class), Mockito.any(TypeToken.class))).thenReturn(keyBuilder);
            Mockito.when(keyBuilder.weightedCollectionElementType(Mockito.any(Class.class))).thenReturn(keyBuilder);
            Mockito.when(keyBuilder.weightedCollectionElementType(Mockito.any(TypeToken.class))).thenReturn(keyBuilder);
            final Key key = Mockito.mock(Key.class);
            Mockito.when(keyBuilder.build()).thenReturn(key);
            Mockito.when(builderProvider.provide(Mockito.any())).thenReturn(keyBuilder);
            Mockito.when(game.builderProvider()).thenReturn(builderProvider);
            resourceKeyMock.when(() -> ResourceKey.sponge(Mockito.anyString())).thenReturn(resourceKey);
            Mockito.when(is1.get(Keys.CUSTOM_NAME)).thenReturn(Optional.empty());
            Mockito.when(is1.get(Keys.LORE)).thenReturn(Optional.empty());

            final QuestionCreator creator = new QuestionCreator();
            creator.addItemPrize(1, is1);
            creator.addItemPrize(1, is2);
            creator.addItemPrize(2, is3);

            creator.removeItemPrize(1, is1);

            assertTrue(creator.getItemsPrize().containsKey(1));
            assertEquals(1, creator.getItemsPrize().get(1).size());
            assertEquals(is2, creator.getItemsPrize().get(1).getFirst());
            assertTrue(creator.getItemsPrize().containsKey(2));
            assertEquals(1, creator.getItemsPrize().get(2).size());
            assertEquals(is3, creator.getItemsPrize().get(2).getFirst());
        }
    }

    @Test
    void removeCommandPrize() {
        final QuestionCreator creator = new QuestionCreator();
        final OutcomeCommand cmd1 = Mockito.mock(OutcomeCommand.class);
        final OutcomeCommand cmd2 = Mockito.mock(OutcomeCommand.class);
        creator.addCommandPrize(1, cmd1);
        creator.addCommandPrize(1, cmd2);

        creator.removeCommandPrize(1, cmd1);

        assertTrue(creator.getCommandsPrize().containsKey(1));
        assertEquals(1, creator.getCommandsPrize().get(1).size());
        assertEquals(cmd2, creator.getCommandsPrize().get(1).getFirst());
    }

    @Test
    void buildSimpleQuestion() {
        final String questionAsked = "What sound does a duck make?";
        final QuestionCreator creator = new QuestionCreator();
        creator.setQuestionType(Question.Types.SIMPLE);
        creator.setQuestion(questionAsked);
        creator.getAnswers().add("Y");
        creator.getAnswers().add("Z");
        creator.addItemPrize(1, Mockito.mock(ItemStack.class));
        creator.addItemPrize(2, Mockito.mock(ItemStack.class));
        creator.addCommandPrize(1, Mockito.mock(OutcomeCommand.class));
        creator.addCommandPrize(3, Mockito.mock(OutcomeCommand.class));
        creator.setMoneyMalus(50);
        creator.setAnnounceMalus(true);
        creator.getCommandsMalus().add(Mockito.mock(OutcomeCommand.class));
        creator.getCommandsMalus().add(Mockito.mock(OutcomeCommand.class));
        creator.setTimeBetweenAnswer(LocalTime.of(0, 1, 0));
        creator.setDuration(LocalTime.of(0, 0, 30));
        creator.setWeight(10);

        final var question = creator.build();

        assertEquals(questionAsked, question.getQuestion());
        assertEquals(Question.Types.SIMPLE, question.getType());
        assertEquals(2, question.getAnswers().size());
        assertEquals(3, question.getPrizes().get().size());
        assertEquals(2, question.getMalus().get().getCommands().length);
        assertEquals(50, question.getMalus().get().getMoney());
        assertTrue(question.getMalus().get().isAnnounce());
        assertEquals(1200, question.getTimeBetweenAnswer());
        assertEquals(600, question.getTimer());
        assertEquals(10, question.getWeight());
    }

    @Test
    void buildPropositionQuestion() {
        final String questionAsked = "What sound does a duck make?";
        final QuestionCreator creator = new QuestionCreator();
        creator.setQuestionType(Question.Types.MULTI);
        creator.setQuestion(questionAsked);
        creator.getPropositions().add("Y");
        creator.getPropositions().add("Z");
        creator.getAnswers().add("Y");
        creator.addItemPrize(1, Mockito.mock(ItemStack.class));
        creator.addItemPrize(2, Mockito.mock(ItemStack.class));
        creator.addCommandPrize(1, Mockito.mock(OutcomeCommand.class));
        creator.addCommandPrize(3, Mockito.mock(OutcomeCommand.class));
        creator.setMoneyMalus(50);
        creator.setAnnounceMalus(true);
        creator.getCommandsMalus().add(Mockito.mock(OutcomeCommand.class));
        creator.getCommandsMalus().add(Mockito.mock(OutcomeCommand.class));
        creator.setTimeBetweenAnswer(LocalTime.of(0, 1, 0));
        creator.setDuration(LocalTime.of(0, 0, 30));
        creator.setWeight(10);

        final var question = creator.build();

        assertEquals(questionAsked, question.getQuestion());
        assertEquals(Question.Types.MULTI, question.getType());
        assertEquals(1, question.getAnswers().size());
        assertEquals(3, question.getPrizes().get().size());
        assertEquals(2, question.getMalus().get().getCommands().length);
        assertEquals(50, question.getMalus().get().getMoney());
        assertTrue(question.getMalus().get().isAnnounce());
        assertEquals(1200, question.getTimeBetweenAnswer());
        assertEquals(600, question.getTimer());
        assertEquals(10, question.getWeight());
    }

}