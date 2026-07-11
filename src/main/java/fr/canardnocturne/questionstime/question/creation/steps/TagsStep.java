package fr.canardnocturne.questionstime.question.creation.steps;

import fr.canardnocturne.questionstime.QuestionsTime;
import fr.canardnocturne.questionstime.question.creation.QuestionCreator;
import fr.canardnocturne.questionstime.util.TextUtils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TagsStep implements CreationStep {

    public static final CreationStep INSTANCE = new TagsStep();

    @Override
    public Component question() {
        return TextUtils.composed("Add tags with ", "/qtc add tag1[;tag2;...]")
                .appendNewline()
                .append(TextUtils.example("/qtc add easy; no_prize; Zelda"))
                .appendNewline()
                .append(TextUtils.composed("Remove tags with ", "/qtc remove tag1[;tag2;...]"))
                .appendNewline()
                .append(TextUtils.example("/qtc remove easy; no_prize"))
                .appendNewline()
                .append(TextUtils.composed("List tags with ", "/qtc list"))
                .appendNewline()
                .append(TextUtils.normalWithPrefix("To go to the next step or skip this step, type "))
                .append(TextUtils.commandShortcut("confirm"));
    }

    @Override
    public boolean handle(final Audience sender, final String answer, final QuestionCreator questionCreator) {
        if(answer.equalsIgnoreCase("confirm")) {
            return true;
        }
        final String[] answers = answer.split(" ", 2);
        switch (answers[0].toLowerCase()) {
            case "list":
                this.listTags(questionCreator, sender);
                break;
            case "add":
                if (answers.length < 2) {
                    sender.sendMessage(TextUtils.composed("Command ", "add", " needs to be followed by tags"));
                } else {
                    this.addTags(sender, answers[1], questionCreator);
                }
                break;
            case "remove":
                if (answers.length < 2) {
                    sender.sendMessage(TextUtils.composed("Command ", "remove", " needs to be followed by tags"));
                } else {
                    this.removeTags(sender, answers[1], questionCreator);
                }
                break;
            default:
                sender.sendMessage(TextUtils.normalWithPrefix("Unknown command. Use add, remove or list"));
        }
        return false;
    }

    private void listTags(final QuestionCreator questionCreator, final Audience sender) {
        if(questionCreator.getTags().isEmpty()) {
            sender.sendMessage(TextUtils.composed("No tags have been added yet"));
        } else {
            final Component message = TextUtils.normalWithPrefix("Tags: ")
                    .appendNewline()
                    .append(Component.join(JoinConfiguration.newlines(), questionCreator.getTags().stream().map(tag ->
                            QuestionsTime.PREFIX.append(Component.text("[X]", NamedTextColor.RED, TextDecoration.BOLD)
                                            .clickEvent(ClickEvent.runCommand("/qtc remove " + tag))
                                            .hoverEvent(HoverEvent.showText(Component.text("Delete the tag '" + tag + "'"))))
                                    .append(TextUtils.composedWithoutPrefix(" ", tag))
                    ).toList()));
            sender.sendMessage(message);
        }
    }

    private void addTags(final Audience sender, final String answer, final QuestionCreator questionCreator) {
        final Set<String> tags = Stream.of(answer.split(";")).map(String::trim).collect(Collectors.toSet());
        final Set<String> existingTags = new HashSet<>(questionCreator.getTags());
        questionCreator.addTags(tags);
        final Set<String> newTags = questionCreator.getTags().stream().filter(tag -> !existingTags.contains(tag)).collect(Collectors.toSet());
        if(newTags.isEmpty()) {
            sender.sendMessage(TextUtils.composed("No new tags to add"));
        } else if(newTags.size() == 1) {
            sender.sendMessage(TextUtils.composed("Tag added: ", newTags.iterator().next()));
        } else {
            sender.sendMessage(TextUtils.composed("Tags added: ", String.join(", ", newTags)));
        }
    }

    private void removeTags(final Audience sender, final String answer, final QuestionCreator questionCreator) {
        final Set<String> tags = Stream.of(answer.split(";")).map(String::trim).collect(Collectors.toSet());
        final Set<String> existingTags = new HashSet<>(questionCreator.getTags());
        questionCreator.removeTags(tags);
        final Set<String> removedTags = existingTags.stream().filter(tag -> !questionCreator.getTags().contains(tag)).collect(Collectors.toSet());
        if(removedTags.isEmpty()) {
            sender.sendMessage(TextUtils.composed("No tags removed"));
        } else if(removedTags.size() == 1) {
            sender.sendMessage(TextUtils.composed("Tag removed: ", removedTags.iterator().next()));
        } else {
            sender.sendMessage(TextUtils.composed("Tags removed: ", String.join(", ", removedTags)));
        }
    }

    @Override
    public boolean shouldSkip(final QuestionCreator questionCreator) {
        return false;
    }

    @Override
    public Step next(final QuestionCreator questionCreator) {
        return null;
    }
}
