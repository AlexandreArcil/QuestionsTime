package fr.canardnocturne.questionstime.config;

import fr.canardnocturne.questionstime.question.component.Malus;
import fr.canardnocturne.questionstime.question.component.Prize;
import fr.canardnocturne.questionstime.question.component.PrizeCommand;
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

import java.util.*;

@ConfigSerializable
public class QuestionTimeConfiguration {

    @Comment(Comments.VERSION)
    private int version = DefaultValues.VERSION;

    @Comment("The time in ticks between each questions. Used when 'mode' is set to 'fixed'")
    private int cooldown = DefaultValues.COOLDOWN;

    @Setting("mode")
    @Comment(Comments.MODE)
    private Mode mode = DefaultValues.MODE;

    @Comment("The min cooldown in ticks before a question can be asked. Used when 'mode' is set to 'interval'")
    private int minCooldown = DefaultValues.MIN_COOLDOWN;

    @Comment("The max cooldown in ticks before a question can be asked. Used when 'mode' is set to 'interval'")
    private int maxCooldown = DefaultValues.MAX_COOLDOWN;

    @Comment("If true, the answer given by a player remains personal and is not sent to the global chat. If false, every answer is sent to the global chat")
    private boolean personalAnswer = false;

    @Comment("The minimum of connected players required to ask a question")
    private int minConnected = DefaultValues.MIN_CONNECTED;

    private final Set<Question> questions = Set.of(Question.builder()
                    .setQuestion("Who created the QuestionsTime plugin ?")
                    .setAnswers(Set.of("CanardNocturne"))
                    .setWeight(5)
                    .setTimer(600)
                    .setPrize(new Prize(0, true, new ItemStack[]{ItemStack.builder()
                            .itemType(ItemTypes.SAND)
                            .quantity(64)
                            .add(Keys.CUSTOM_NAME, Component.text("Sandy", NamedTextColor.YELLOW))
                            .add(Keys.LORE, Arrays.asList(Component.text("It's just a sand block with a cool name"), Component.text("What did you expect")))
                            .build()}, null))
                    .setMalus(new Malus(100, true))
                    .build(),
            QuestionMulti.builder()
                    .setQuestion("Among these propositions, who created the QuestionsTime plugin ?")
                    .setPropositions(new LinkedHashSet<>(Arrays.asList("CanardNocturne", "Notch", "Pewdiepie", "Donald Trump", "Chicky the chicken")))
                    .setAnswers(Set.of("1"))
                    .setPrize(new Prize(500, true, null, new PrizeCommand[]{new PrizeCommand("A secret about the plugin's creator", "msg @winner Here is the secret about the plugin's creator: he's not a real duck")}))
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

    public Collection<Question> getQuestions() {
        return Collections.unmodifiableSet(questions);
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(final int version) {
        this.version = version;
    }

    public Mode getMode() {
        return mode;
    }

    @Override
    public String toString() {
        return "QuestionTimeConfiguration{" +
                "version=" + version +
                ", cooldown=" + cooldown +
                ", mode=" + mode +
                ", minCooldown=" + minCooldown +
                ", maxCooldown=" + maxCooldown +
                ", personalAnswer=" + personalAnswer +
                ", minConnected=" + minConnected +
                ", questions=" + questions +
                '}';
    }

    public enum Mode {
        FIXED,
        INTERVAL,
        MANUAL
    }

    public static class DefaultValues {
        public static final int VERSION = 1;
        public static final Mode MODE = Mode.FIXED;
        public static final int COOLDOWN = 36000;
        public static final int MIN_COOLDOWN = 36000;
        public static final int MAX_COOLDOWN = 72000;
        public static final int MIN_CONNECTED = 2;
    }

    public static class Comments {
        public static final String VERSION = "The version of the configuration file. Do not modify this value";
        public static final String MODE = """
            Define how the questions are asked:
            - fixed: a new question is asked after the cooldown
            - interval: a new question is asked between minCooldown and maxCooldown
            - manual: questions are asked only manually""";
    }

}
