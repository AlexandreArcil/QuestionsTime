package fr.canardnocturne.questionstime.question.creation;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CreateQuestionCommandTest {

    private MockedStatic<Parameter> parameterMock;
    private MockedStatic<CommandResult> commandResultMock;
    private CommandResult commandResult;

    @BeforeEach
    void setUp() {
        this.commandResultMock = Mockito.mockStatic(CommandResult.class);
        this.commandResult = Mockito.mock(CommandResult.class);
        this.commandResultMock.when(CommandResult::success).thenReturn(commandResult);
        this.parameterMock = Mockito.mockStatic(Parameter.class);
        final Parameter.Value.Builder<String> valueBuilderMock = Mockito.mock(Parameter.Value.Builder.class);
        Mockito.when(valueBuilderMock.key(Mockito.anyString())).thenReturn(valueBuilderMock);
        Mockito.when(valueBuilderMock.optional()).thenReturn(valueBuilderMock);
        final Parameter.Value<String> stepParam = Mockito.mock(Parameter.Value.class);
        Mockito.when(valueBuilderMock.build()).thenReturn(stepParam);
        this.parameterMock.when(Parameter::remainingJoinedStrings).thenReturn(valueBuilderMock);
    }

    @AfterEach
    void tearDown() {
        this.parameterMock.close();
        this.commandResultMock.close();
    }

    @Test
    void noArgumentsGiven() {
        final QuestionCreationManager manager = Mockito.mock(QuestionCreationManager.class);
        final CommandContext context = Mockito.mock(CommandContext.class);
        final CommandCause cause = Mockito.mock(CommandCause.class);
        final ServerPlayer player = Mockito.mock(ServerPlayer.class);

        Mockito.when(context.cause()).thenReturn(cause);
        Mockito.when(cause.root()).thenReturn(player);
        Mockito.when(context.one(Mockito.any(Parameter.Value.class))).thenReturn(Optional.empty());

        final CreateQuestionCommand command = new CreateQuestionCommand(manager);
        final CommandResult result = command.execute(context);

        Mockito.verify(manager).handlePlayerArguments(player, "");
        assertEquals(this.commandResult, result);
    }

    @Test
    void handlesArgs() {
        final QuestionCreationManager manager = Mockito.mock(QuestionCreationManager.class);
        final CommandContext context = Mockito.mock(CommandContext.class);
        final CommandCause cause = Mockito.mock(CommandCause.class);
        final ServerPlayer player = Mockito.mock(ServerPlayer.class);

        Mockito.when(context.cause()).thenReturn(cause);
        Mockito.when(cause.root()).thenReturn(player);
        Mockito.when(context.one(Mockito.any(Parameter.Value.class))).thenReturn(Optional.of("step args"));

        final CreateQuestionCommand command = new CreateQuestionCommand(manager);
        final CommandResult result = command.execute(context);

        Mockito.verify(manager).handlePlayerArguments(player, "step args");
        assertEquals(this.commandResult, result);
    }

}