package fr.canardnocturne.questionstime.question.ask.launcher;

import fr.canardnocturne.questionstime.question.ask.QuestionAskManager;
import fr.canardnocturne.questionstime.question.Question;
import net.kyori.adventure.text.Component;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ManualAskQuestionCommandTest {

    private MockedStatic<Parameter> parameterMock;
    private MockedStatic<CommandResult> commandResultMock;
    private CommandResult commandResult;
    private CommandResult commandResultError;
    private QuestionAskManager askManager;
    private QuestionLauncher questionLauncher;
    private Parameter.Value<Question> specificQuestionParam;
    private Parameter.Value<String> tagsParam;
    private ManualAskQuestionCommand manualAskQuestionCommand;

    @BeforeEach
    void setUp() {
        this.commandResultMock = Mockito.mockStatic(CommandResult.class);
        this.commandResult = Mockito.mock(CommandResult.class);
        this.commandResultError = Mockito.mock(CommandResult.class);
        this.commandResultMock.when(CommandResult::success).thenReturn(commandResult);
        this.commandResultMock.when(() -> CommandResult.error(Mockito.any(Component.class))).thenReturn(commandResultError);
        this.parameterMock = Mockito.mockStatic(Parameter.class);
        final Parameter.Value.Builder<String> valueBuilderMock = Mockito.mock(Parameter.Value.Builder.class);
        Mockito.when(valueBuilderMock.key(Mockito.anyString())).thenReturn(valueBuilderMock);
        final Parameter.Value<String> randomQuestionParam = Mockito.mock(Parameter.Value.class);
        Mockito.when(valueBuilderMock.build()).thenReturn(randomQuestionParam);
        this.parameterMock.when(() -> Parameter.choices(Mockito.anyString())).thenReturn(valueBuilderMock);

        this.askManager = Mockito.mock(QuestionAskManager.class);
        this.questionLauncher = Mockito.mock(QuestionLauncher.class);
        this.specificQuestionParam = Mockito.mock(Parameter.Value.class);
        this.tagsParam = Mockito.mock(Parameter.Value.class);
        final Logger logger = Mockito.mock(Logger.class);
        this.manualAskQuestionCommand = new ManualAskQuestionCommand(askManager, questionLauncher, specificQuestionParam, tagsParam, logger);
    }

    @AfterEach
    void tearDown() {
        this.parameterMock.close();
        this.commandResultMock.close();
    }

    @Test
    void askRandomQuestion() throws CommandException {
        Mockito.when(this.askManager.isQuestionHasBeenAsked()).thenReturn(false);
        Mockito.when(this.askManager.enoughEligiblePlayers()).thenReturn(true);
        final CommandContext context = Mockito.mock(CommandContext.class);
        Mockito.when(context.hasAny(Mockito.any(Parameter.Value.class))).thenReturn(true);
        Mockito.when(context.friendlyIdentifier()).thenReturn(Optional.of("CanardNocturne"));
        Mockito.when(context.all(this.tagsParam)).thenReturn(Collections.emptyList());

        final CommandResult cmdResult = this.manualAskQuestionCommand.execute(context);

        assertEquals(this.commandResult, cmdResult);
        Mockito.verify(this.askManager).askRandomQuestion(Mockito.anyCollection());
        Mockito.verify(this.questionLauncher).stop();
    }

    @Test
    void askRandomQuestionWithTags() throws CommandException {
        Mockito.when(this.askManager.isQuestionHasBeenAsked()).thenReturn(false);
        Mockito.when(this.askManager.enoughEligiblePlayers()).thenReturn(true);
        final CommandContext context = Mockito.mock(CommandContext.class);
        Mockito.when(context.hasAny(Mockito.any(Parameter.Value.class))).thenReturn(true);
        Mockito.when(context.friendlyIdentifier()).thenReturn(Optional.of("CanardNocturne"));
        final Collection<String> tags = List.of("tag1", "tag2");
        Mockito.doReturn(tags).when(context).all(this.tagsParam);

        final CommandResult cmdResult = this.manualAskQuestionCommand.execute(context);

        assertEquals(this.commandResult, cmdResult);
        Mockito.verify(this.askManager).askRandomQuestion(Mockito.argThat(tags::containsAll));
        Mockito.verify(this.questionLauncher).stop();
    }

    @Test
    void askRandomQuestionWithUnknowTags() throws CommandException {
        Mockito.when(this.askManager.isQuestionHasBeenAsked()).thenReturn(false);
        Mockito.when(this.askManager.enoughEligiblePlayers()).thenReturn(true);
        final CommandContext context = Mockito.mock(CommandContext.class);
        Mockito.when(context.hasAny(Mockito.any(Parameter.Value.class))).thenReturn(true);
        Mockito.when(context.friendlyIdentifier()).thenReturn(Optional.of("CanardNocturne"));
        final Collection<String> tags = List.of("tag1");
        Mockito.doReturn(tags).when(context).all(this.tagsParam);
        Mockito.doThrow(new IllegalArgumentException("No question found with the specified tags")).when(this.askManager).askRandomQuestion(Mockito.anyCollection());

        final CommandResult cmdResult = this.manualAskQuestionCommand.execute(context);

        assertEquals(this.commandResultError, cmdResult);
        Mockito.verify(this.askManager).askRandomQuestion(Mockito.argThat(tags::containsAll));
        Mockito.verify(this.questionLauncher).stop();
    }

    @Test
    void askSpecificQuestion() throws CommandException {
        Mockito.when(askManager.isQuestionHasBeenAsked()).thenReturn(false);
        Mockito.when(askManager.enoughEligiblePlayers()).thenReturn(true);
        final CommandContext context = Mockito.mock(CommandContext.class);
        Mockito.when(context.hasAny(Mockito.any(Parameter.Value.class))).thenReturn(false);
        final Question question = Mockito.mock(Question.class);
        Mockito.when(context.requireOne(this.specificQuestionParam)).thenReturn(question);
        Mockito.when(question.getQuestion()).thenReturn("Is CanardNocturne a duck?");
        Mockito.when(context.friendlyIdentifier()).thenReturn(Optional.of("CanardNocturne"));

        final CommandResult cmdResult = manualAskQuestionCommand.execute(context);

        assertEquals(this.commandResult, cmdResult);
        Mockito.verify(askManager).askQuestion(question);
        Mockito.verify(this.questionLauncher).stop();
    }

    @Test
    void notEnoughEligiblePlayers() throws CommandException {
        Mockito.when(askManager.isQuestionHasBeenAsked()).thenReturn(false);
        Mockito.when(askManager.enoughEligiblePlayers()).thenReturn(false);
        final CommandContext context = Mockito.mock(CommandContext.class);
        final CommandResult errorResult = Mockito.mock(CommandResult.class);
        this.commandResultMock.when(() -> CommandResult.error(Mockito.any(Component.class))).thenReturn(errorResult);

        final CommandResult cmdResult = this.manualAskQuestionCommand.execute(context);

        assertEquals(errorResult, cmdResult);
        Mockito.verify(askManager, Mockito.never()).askRandomQuestion(Mockito.anyCollection());
        Mockito.verify(askManager, Mockito.never()).askQuestion(Mockito.any());
    }

    @Test
    void questionAlreadyAsked() throws CommandException {
        Mockito.when(askManager.isQuestionHasBeenAsked()).thenReturn(true);
        final CommandContext context = Mockito.mock(CommandContext.class);
        final CommandResult errorResult = Mockito.mock(CommandResult.class);
        this.commandResultMock.when(() -> CommandResult.error(Mockito.any(Component.class))).thenReturn(errorResult);

        final CommandResult cmdResult = this.manualAskQuestionCommand.execute(context);

        assertEquals(errorResult, cmdResult);
        Mockito.verify(askManager, Mockito.never()).askRandomQuestion(Mockito.anyCollection());
        Mockito.verify(askManager, Mockito.never()).askQuestion(Mockito.any());
    }

}