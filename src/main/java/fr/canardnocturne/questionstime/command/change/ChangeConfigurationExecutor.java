package fr.canardnocturne.questionstime.command.change;

import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;

public class ChangeConfigurationExecutor implements CommandExecutor {

    public static final Parameter.Value<Config> CONFIG = Parameter.enumValue(Config.class).key("config").build();
    public static final Parameter.Value<String> VALUE = Parameter.remainingJoinedStrings().key("value").build();

    private final ChangeConfiguration changeConfiguration;

    public ChangeConfigurationExecutor(final ChangeConfiguration changeConfiguration) {
        this.changeConfiguration = changeConfiguration;
    }

    @Override
    public CommandResult execute(final CommandContext context) throws CommandException {
        return CommandResult.success();
    }
}
