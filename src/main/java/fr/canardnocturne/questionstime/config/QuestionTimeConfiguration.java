package fr.canardnocturne.questionstime.config;

import fr.canardnocturne.questionstime.question.component.Malus;
import fr.canardnocturne.questionstime.question.component.Prize;
import fr.canardnocturne.questionstime.question.type.Question;
import fr.canardnocturne.questionstime.question.type.QuestionMulti;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ConfigSerializable
public class QuestionTimeConfiguration {

    @Comment("The time in ticks between each questions")
    private int cooldown = DefaultValues.COOLDOWN;

    @Setting("randomTime")
    @Comment("If true, he will say a new question between minCooldown and maxCooldown, if false, he will say a new question after the cooldown")
    private boolean isRandom = false;

    @Comment("The min cooldown in ticks before a question can be asked")
    private int minCooldown = DefaultValues.MIN_COOLDOWN;

    @Comment("The max cooldown in ticks before a question can be asked")
    private int maxCooldown = DefaultValues.MAX_COOLDOWN;

    @Comment("If true, the answer given by a player remains personal and is not sent to the global chat. If false, every answer is sent to the global chat")
    private boolean personalAnswer = false;

    @Comment("The minimum of connected  players required to ask a question")
    private int minConnected = DefaultValues.MIN_CONNECTED;

    private final List<Question> questions = Arrays.asList(Question.builder()
                    .setQuestion("Who created the QuestionsTime plugin ?")
                    .setAnswer("CanardNocturne")
                    .setWeight(5)
                    .setTimer(600)
                    .setPrize(new Prize(0, true, new ItemStack[]{ItemStack.builder()
                            .itemType(ItemTypes.SAND)
                            .quantity(64)
                            .add(Keys.CUSTOM_NAME, Component.text("Sandy", NamedTextColor.YELLOW))
                            .add(Keys.LORE, Arrays.asList(Component.text("It's just a sand block with a cool name"), Component.text("What did you expect")))
                            .build()}))
                    .setMalus(new Malus(100, true))
                    .build(),
            QuestionMulti.builder()
                    .setQuestion("Who created the QuestionsTime plugin ?")
                    .setPropositions(Arrays.asList("CanardNocturne", "Notch", "Pewdiepie", "Donald Trump", "Chicky the chicken"))
                    .setAnswer("1")
                    .setPrize(new Prize(500, true, null))
                    .setTimer(1800)
                    .setTimeBetweenAnswer(300)
                    .setWeight(2)
                    .build()
    );

    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(final int cooldown) {
        this.cooldown = cooldown;
    }

    public int getMaxCooldown() {
        return maxCooldown;
    }

    public void setMaxCooldown(final int maxCooldown) {
        this.maxCooldown = maxCooldown;
    }

    public int getMinConnected() {
        return minConnected;
    }

    public void setMinConnected(final int minConnected) {
        this.minConnected = minConnected;
    }

    public int getMinCooldown() {
        return minCooldown;
    }

    public void setMinCooldown(final int minCooldown) {
        this.minCooldown = minCooldown;
    }

    public boolean isPersonalAnswer() {
        return personalAnswer;
    }

    public boolean isRandom() {
        return isRandom;
    }

    public List<Question> getQuestions() {
        return new ArrayList<>(questions);
    }

    @Override
    public String toString() {
        return "QuestionTimeConfiguration{" +
                "cooldown=" + cooldown +
                ", isRandom=" + isRandom +
                ", minCooldown=" + minCooldown +
                ", maxCooldown=" + maxCooldown +
                ", personalAnswer=" + personalAnswer +
                ", minConnected=" + minConnected +
                ", questions=" + questions +
                '}';
    }

    public static class DefaultValues {
        public static final int COOLDOWN = 36000;
        public static final int MIN_COOLDOWN = 36000;
        public static final int MAX_COOLDOWN = 72000;
        public static final int MIN_CONNECTED = 2;
    }

}
