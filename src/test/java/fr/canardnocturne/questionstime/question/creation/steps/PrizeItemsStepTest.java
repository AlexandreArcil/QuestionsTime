package fr.canardnocturne.questionstime.question.creation.steps;

import fr.canardnocturne.questionstime.question.creation.QuestionCreator;
import fr.canardnocturne.questionstime.question.serializer.ItemStackSerializer;
import fr.canardnocturne.questionstime.util.MiniMessageTest;
import fr.canardnocturne.questionstime.util.TextUtils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class PrizeItemsStepTest {
    
    @Mock
    private Audience audience;
    
    @Mock
    private QuestionCreator qc;

    @Test
    void questionDefined() {
        assertNotNull(PrizeItemsStep.INSTANCE.question());
    }

    @Test
    void addItemPrize() {
        try(final MockedStatic<ItemStackSerializer> itemStackSerializerMock = org.mockito.Mockito.mockStatic(ItemStackSerializer.class);
            final MockedStatic<TextUtils> textUtilsMock = Mockito.mockStatic(TextUtils.class)) {
            final String itemString = "minecraft:stone;5;Old Stone;Emits a low light";
            final ItemStack itemStack = Mockito.mock(ItemStack.class);
            itemStackSerializerMock.when(() -> ItemStackSerializer.fromString(itemString)).thenReturn(itemStack);
            this.mockTestUtilsFunctions(textUtilsMock);

            PrizeItemsStep.INSTANCE.handle(audience, "add " + itemString + ";2", qc);

            Mockito.verify(qc).addItemPrize(2, itemStack);
        }
    }

    @Test
    void addItemPrizeToInvalidPosition() {
        try(final MockedStatic<ItemStackSerializer> itemStackSerializerMock = org.mockito.Mockito.mockStatic(ItemStackSerializer.class);
            final MockedStatic<TextUtils> textutilsMock = Mockito.mockStatic(TextUtils.class) ) {
            final String itemString = "minecraft:stone;5;Old Stone;Emits a low light;-1";
            this.mockTestUtilsFunctions(textutilsMock);
            textutilsMock.when(() -> TextUtils.extractPositionFromAnswer(Mockito.anyString())).thenReturn(new TextUtils.AnswerPosition("", -1));

            PrizeItemsStep.INSTANCE.handle(audience, "add " + itemString, qc);

            Mockito.verify(audience).sendMessage(Mockito.any());
            Mockito.verifyNoInteractions(qc);
            itemStackSerializerMock.verifyNoInteractions();
        }
    }

    @Test
    void addItemPrizeNoItem() {
        PrizeItemsStep.INSTANCE.handle(audience, "add", qc);

        Mockito.verify(audience).sendMessage(Mockito.any());
        Mockito.verifyNoInteractions(qc);
    }

    @Test
    void listItemPrizes() {
        try(final MockedStatic<TextUtils> textutilsMock = Mockito.mockStatic(TextUtils.class);
                final MockedStatic<ItemStackSerializer> itemStackSerializerMock = Mockito.mockStatic(ItemStackSerializer.class)) {
            Mockito.when(qc.getItemsPrize()).thenReturn(Map.of(1, List.of(Mockito.mock(ItemStack.class), Mockito.mock(ItemStack.class)),
                    2, List.of(Mockito.mock(ItemStack.class))));
            this.mockTestUtilsFunctions(textutilsMock);

            itemStackSerializerMock.when(() -> ItemStackSerializer.fromItemStack(Mockito.any(ItemStack.class))).thenReturn("is");

            PrizeItemsStep.INSTANCE.handle(audience, "list", qc);

            Mockito.verify(audience).sendMessage(Mockito.any(Component.class));
        }
    }

    @Test
    void listItemPrizesEmpty() {
        try(final MockedStatic<TextUtils> textutilsMock = Mockito.mockStatic(TextUtils.class);
            final MockedStatic<ItemStackSerializer> itemStackSerializerMock = Mockito.mockStatic(ItemStackSerializer.class)) {
            Mockito.when(qc.getItemsPrize()).thenReturn(Map.of());
            this.mockTestUtilsFunctions(textutilsMock);

            PrizeItemsStep.INSTANCE.handle(audience, "list", qc);

            Mockito.verify(audience).sendMessage(Mockito.argThat(
                    component -> MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).endsWith("No item prizes added yet.")));
            itemStackSerializerMock.verifyNoInteractions();
        }
    }

    @Test
    void removeItemPrize() {
        try(final MockedStatic<ItemStackSerializer> itemStackSerializerMock = org.mockito.Mockito.mockStatic(ItemStackSerializer.class);
            final MockedStatic<TextUtils> textutilsMock = Mockito.mockStatic(TextUtils.class)) {
            final String itemString = "minecraft:stone;5;Old Stone;Emits a low light";
            final ItemStack itemStack = Mockito.mock(ItemStack.class);
            Mockito.when(qc.removeItemPrize(2, itemStack)).thenReturn(true);
            itemStackSerializerMock.when(() -> ItemStackSerializer.fromString(itemString)).thenReturn(itemStack);
            this.mockTestUtilsFunctions(textutilsMock);

            PrizeItemsStep.INSTANCE.handle(audience, "del " + itemString + ";2", qc);

            Mockito.verify(audience).sendMessage(Mockito.argThat(
                    component -> MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).contains("Removed item prize")));
        }
    }

    @Test
    void removeItemPrizeNotFound() {
        try(final MockedStatic<ItemStackSerializer> itemStackSerializerMock = org.mockito.Mockito.mockStatic(ItemStackSerializer.class);
        final MockedStatic<TextUtils> textutilsMock = Mockito.mockStatic(TextUtils.class)) {
            final String itemString = "minecraft:stone;5;Old Stone;Emits a low light";
            final ItemStack itemStack = Mockito.mock(ItemStack.class);
            Mockito.when(qc.removeItemPrize(2, itemStack)).thenReturn(false);
            itemStackSerializerMock.when(() -> ItemStackSerializer.fromString(itemString)).thenReturn(itemStack);
            this.mockTestUtilsFunctions(textutilsMock);

            PrizeItemsStep.INSTANCE.handle(audience, "del " + itemString + ";2", qc);

            Mockito.verify(audience).sendMessage(Mockito.argThat(
                    component -> MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).endsWith("winner not found.")));
        }
    }

    @Test
    void removeItemPrizeIncorrectPosition() {
        try(final MockedStatic<ItemStackSerializer> itemStackSerializerMock = org.mockito.Mockito.mockStatic(ItemStackSerializer.class);
            final MockedStatic<TextUtils> textutilsMock = Mockito.mockStatic(TextUtils.class)) {
            final String itemString = "minecraft:stone;5;Old Stone;Emits a low light";
            this.mockTestUtilsFunctions(textutilsMock);
            textutilsMock.when(() -> TextUtils.extractPositionFromAnswer(Mockito.anyString())).thenReturn(new TextUtils.AnswerPosition("", -1));

            PrizeItemsStep.INSTANCE.handle(audience, "del " + itemString + ";-1", qc);

            Mockito.verify(audience).sendMessage(Mockito.argThat(
                    component -> MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).endsWith("is not a positive number")));
            itemStackSerializerMock.verifyNoInteractions();
        }
    }

    @Test
    void removeItemPrizeNoItem() {
        PrizeItemsStep.INSTANCE.handle(audience, "del", qc);

        Mockito.verify(audience).sendMessage(Mockito.argThat(
                component -> MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).endsWith("You must provide an item to remove")));
    }

    @Test
    void commandNotFound() {
        PrizeItemsStep.INSTANCE.handle(audience, "coin", qc);

        Mockito.verify(audience).sendMessage(Mockito.argThat(
                component -> MiniMessageTest.NO_STYLE_COMPONENT.serialize(component).endsWith("Unknown command: coin")));
    }

    @Test
    void confirmCommand() {
        final boolean result = PrizeItemsStep.INSTANCE.handle(audience, "confirm", qc);

        assertTrue(result);
    }

    @Test
    void shouldNotSkipStep() {
        assertFalse(PrizeItemsStep.INSTANCE.shouldSkip(Mockito.mock(QuestionCreator.class)));
    }

    @Test
    void nextStepDefined() {
        assertNotNull(PrizeItemsStep.INSTANCE.next(Mockito.mock(QuestionCreator.class)));
    }

    private void mockTestUtilsFunctions(final MockedStatic<TextUtils> textUtilsMock) {
        textUtilsMock.when(() -> TextUtils.displayItem(Mockito.any(ItemStack.class))).thenReturn(Component.text("is"));
        textUtilsMock.when(() -> TextUtils.normalWithPrefix(Mockito.anyString())).thenCallRealMethod();
        textUtilsMock.when(() -> TextUtils.special(Mockito.anyString())).thenCallRealMethod();
        textUtilsMock.when(() -> TextUtils.specialWithPrefix(Mockito.anyString())).thenCallRealMethod();
        textUtilsMock.when(() -> TextUtils.normal(Mockito.anyString())).thenCallRealMethod();
        textUtilsMock.when(() -> TextUtils.extractPositionFromAnswer(Mockito.anyString())).thenCallRealMethod();
        textUtilsMock.when(() -> TextUtils.composedWithoutPrefix(Mockito.any(String[].class))).thenCallRealMethod();
    }

}