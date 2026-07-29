package fr.canardnocturne.questionstime.command.set.question.tmp.qc3;

import fr.canardnocturne.questionstime.command.set.question.tmp.qc3.component.QuestionComponentBoolean;
import fr.canardnocturne.questionstime.command.set.question.tmp.qc3.component.QuestionComponentInteger;
import fr.canardnocturne.questionstime.command.set.question.tmp.qc3.component.QuestionComponentListString;
import fr.canardnocturne.questionstime.command.set.question.tmp.qc3.component.QuestionComponentSetItemStack;
import fr.canardnocturne.questionstime.command.set.question.tmp.qc3.component.QuestionComponentSetOutcomeCommands;
import fr.canardnocturne.questionstime.command.set.question.tmp.qc3.component.QuestionComponentSetString;
import fr.canardnocturne.questionstime.command.set.question.tmp.qc3.component.QuestionComponentString;
import fr.canardnocturne.questionstime.command.set.question.tmp.qc3.section.QuestionSectionBase;
import fr.canardnocturne.questionstime.command.set.question.tmp.qc3.section.QuestionSectionMalus;
import fr.canardnocturne.questionstime.command.set.question.tmp.qc3.section.QuestionSectionPrize;
import fr.canardnocturne.questionstime.command.set.question.tmp.qc3.section.QuestionSectionQuestion;
import fr.canardnocturne.questionstime.command.set.question.tmp.qc3.set.creator.SetCommandCreator;
import fr.canardnocturne.questionstime.command.set.question.tmp.qc3.set.creator.SetCommandCreatorBase;
import fr.canardnocturne.questionstime.command.set.question.tmp.qc3.set.creator.SetCommandCreatorCollection;
import fr.canardnocturne.questionstime.command.set.question.tmp.qc3.set.creator.SetCommandCreatorLuckPermsCollection;
import fr.canardnocturne.questionstime.command.set.question.tmp.qc3.set.creator.SetCommandCreatorPrizeBase;
import fr.canardnocturne.questionstime.command.set.question.tmp.qc3.set.creator.SetCommandCreatorPrizeCollection;
import fr.canardnocturne.questionstime.question.Question;
import fr.canardnocturne.questionstime.question.component.Malus;
import fr.canardnocturne.questionstime.question.component.OutcomeCommand;
import fr.canardnocturne.questionstime.question.component.Prize;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class QuestionComponents {

    private static final Set<QuestionSectionBase<?, ?, ?>> SECTIONS;
    private static final Set<SetCommandCreator<?, ?, ?>> COMMAND_CREATORS;

    static {
        SECTIONS = new HashSet<>();
        COMMAND_CREATORS = new HashSet<>();

        // Question
        final QuestionComponentString questionComponent = new QuestionComponentString("question", "question");
        final QuestionSectionQuestion<String> questionQuestionSection = new QuestionSectionQuestion<>(Question.QuestionBuilder::setQuestion, Question::getQuestion, questionComponent);
        register(questionQuestionSection, new SetCommandCreatorBase<>(questionQuestionSection, questionComponent));
        final QuestionComponentInteger timerComponent = new QuestionComponentInteger("timer", "timer");
        final QuestionSectionQuestion<Integer> timerQuestionSection = new QuestionSectionQuestion<>(Question.QuestionBuilder::setTimer, Question::getTimer, timerComponent);
        register(timerQuestionSection, new SetCommandCreatorBase<>(timerQuestionSection, timerComponent));
        final QuestionComponentSetString tagsComponent = new QuestionComponentSetString("tags", "tag", "tags");
        final QuestionSectionQuestion<Set<String>> tagsQuestionSection = new QuestionSectionQuestion<>(Question.QuestionBuilder::setTags, Question::getTags, tagsComponent);
        register(tagsQuestionSection, new SetCommandCreatorCollection<>(tagsQuestionSection, tagsComponent));
        final QuestionComponentListString propositionsComponent = new QuestionComponentListString("proposition", "proposition", "propositions");
        final QuestionSectionQuestion<List<String>> propositionsQuestionSection = new QuestionSectionQuestion<>(Question.QuestionBuilder::setPropositions, Question::getPropositions, propositionsComponent);
        register(propositionsQuestionSection, new SetCommandCreatorCollection<>(propositionsQuestionSection, propositionsComponent));
        final QuestionComponentSetString answersComponent = new QuestionComponentSetString("answer", "answer", "answers");
        final QuestionSectionQuestion<Set<String>> answersQuestionSection = new QuestionSectionQuestion<>(Question.QuestionBuilder::setAnswers, Question::getAnswers, answersComponent);
        register(answersQuestionSection, new SetCommandCreatorCollection<>(answersQuestionSection, answersComponent));
        final QuestionComponentInteger timeBetweenAnswerComponent = new QuestionComponentInteger("time-between-answer", "time between answer");
        final QuestionSectionQuestion<Integer> timeBetweenAnswerQuestionSection = new QuestionSectionQuestion<>(Question.QuestionBuilder::setTimeBetweenAnswer, Question::getTimeBetweenAnswer, timeBetweenAnswerComponent);
        register(timeBetweenAnswerQuestionSection, new SetCommandCreatorBase<>(timeBetweenAnswerQuestionSection, timeBetweenAnswerComponent));
        final QuestionComponentInteger weightComponent = new QuestionComponentInteger("weight", "weight");
        final QuestionSectionQuestion<Integer> weightQuestionSection = new QuestionSectionQuestion<>(Question.QuestionBuilder::setWeight, Question::getWeight, weightComponent);
        register(weightQuestionSection, new SetCommandCreatorBase<>(weightQuestionSection, weightComponent));
        final QuestionComponentBoolean revealAnswerComponent = new QuestionComponentBoolean("reveal-answer", "reveal answer");
        final QuestionSectionQuestion<Boolean> revealAnswerQuestionSection = new QuestionSectionQuestion<>(Question.QuestionBuilder::setRevealAnswer, Question::isRevealAnswer, revealAnswerComponent);
        register(revealAnswerQuestionSection, new SetCommandCreatorBase<>(revealAnswerQuestionSection, revealAnswerComponent));
        final QuestionComponentSetString includePermissionsComponent = new QuestionComponentSetString("include-permissions", "include permission", "include permissions");
        final QuestionSectionQuestion<Set<String>> includePermissionsQuestionSection = new QuestionSectionQuestion<>(Question.QuestionBuilder::setIncludePermissions, Question::getIncludePermissions, includePermissionsComponent);
        register(includePermissionsQuestionSection, new SetCommandCreatorLuckPermsCollection<>(includePermissionsQuestionSection, includePermissionsComponent));
        final QuestionComponentSetString excludePermissionsComponent = new QuestionComponentSetString("exclude-permissions", "exclude permission", "exclude permissions");
        final QuestionSectionQuestion<Set<String>> excludePermissionsQuestionSection = new QuestionSectionQuestion<>(Question.QuestionBuilder::setExcludePermissions, Question::getExcludePermissions, excludePermissionsComponent);
        register(excludePermissionsQuestionSection, new SetCommandCreatorLuckPermsCollection<>(excludePermissionsQuestionSection, excludePermissionsComponent));

        // Malus
        final QuestionComponentBoolean announceComponent = new QuestionComponentBoolean("announce", "malus announce");
        final QuestionSectionMalus<Boolean> announceMalusSection = new QuestionSectionMalus<>(Malus.Builder::setAnnounce, Malus::isAnnounce, announceComponent);
        register(announceMalusSection, new SetCommandCreatorBase<>(announceMalusSection, announceComponent));
        final QuestionComponentInteger moneyMalusComponent = new QuestionComponentInteger("money", "malus money");
        final QuestionSectionMalus<Integer> moneyMalusSection = new QuestionSectionMalus<>(Malus.Builder::setMoney, Malus::getMoney, moneyMalusComponent);
        register(moneyMalusSection, new SetCommandCreatorBase<>(moneyMalusSection, moneyMalusComponent));
        final QuestionComponentSetOutcomeCommands commandsMalusComponent = new QuestionComponentSetOutcomeCommands("commands", "malus command", " malus commands");
        final QuestionSectionMalus<Set<OutcomeCommand>> commandsMalusSection = new QuestionSectionMalus<>(Malus.Builder::setCommands, Malus::getCommands, commandsMalusComponent);
        register(commandsMalusSection, new SetCommandCreatorCollection<>(commandsMalusSection, commandsMalusComponent));

        // Prize
        final QuestionComponentSetItemStack prizeItemsComponent = new QuestionComponentSetItemStack("items", "prize item", "prize items");
        final QuestionSectionPrize<Set<ItemStack>> prizeItemsSection = new QuestionSectionPrize<>(Prize.Builder::setItems, Prize::getItemStacks, prizeItemsComponent);
        register(prizeItemsSection, new SetCommandCreatorPrizeCollection<>(prizeItemsSection, prizeItemsComponent));
        final QuestionComponentInteger moneyPrizeComponent = new QuestionComponentInteger("money", "prize money");
        final QuestionSectionPrize<Integer> moneyPrizeSection = new QuestionSectionPrize<>(Prize.Builder::setMoney, Prize::getMoney, moneyPrizeComponent);
        register(moneyPrizeSection, new SetCommandCreatorPrizeBase<>(moneyPrizeSection, moneyPrizeComponent));
        final QuestionComponentBoolean announcePrizeComponent = new QuestionComponentBoolean("announce", "prize announce");
        final QuestionSectionPrize<Boolean> announcePrizeSection = new QuestionSectionPrize<>(Prize.Builder::setAnnounce, Prize::isAnnounce, announcePrizeComponent);
        register(announcePrizeSection, new SetCommandCreatorPrizeBase<>(announcePrizeSection, announcePrizeComponent));
        final QuestionComponentSetOutcomeCommands commandsPrizeComponent = new QuestionComponentSetOutcomeCommands("commands", "prize command", "prize commands");
        final QuestionSectionPrize<Set<OutcomeCommand>> commandsPrizeSection = new QuestionSectionPrize<>(Prize.Builder::setCommands, Prize::getCommands, commandsPrizeComponent);
        register(commandsPrizeSection, new SetCommandCreatorPrizeCollection<>(commandsPrizeSection, commandsPrizeComponent));
    }

    private static <T, B, V> void register(final QuestionSectionBase<T, B, V> section, final SetCommandCreator<T, B, V> creator) {
        SECTIONS.add(section);
        COMMAND_CREATORS.add(creator);
    }

    public static Set<QuestionSectionBase<?, ?, ?>> getSections() {
        return Set.copyOf(SECTIONS);
    }

    public static Set<SetCommandCreator<?, ?, ?>> getCommandCreators() {
        return Set.copyOf(COMMAND_CREATORS);
    }
}
