package fr.canardnocturne.questionstime.command.set.config;

import fr.canardnocturne.questionstime.QuestionsTime;
import fr.canardnocturne.questionstime.config.ConfigField;
import fr.canardnocturne.questionstime.config.ConfigMutable;
import fr.canardnocturne.questionstime.config.QuestionTimeConfiguration;
import fr.canardnocturne.questionstime.config.save.PluginConfigurationSave;
import fr.canardnocturne.questionstime.config.verificator.VerifyConfigurationValues;
import fr.canardnocturne.questionstime.util.TextUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.NamedTextColor;
import org.apache.logging.log4j.Logger;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;

import java.io.IOException;

public class SetConfigMinimumConnectedExecutor implements CommandExecutor {

    public static final Parameter.Value<Integer> MINIMUM_CONNECTED = Parameter.integerNumber().key("minimum_connected").build();

    private final VerifyConfigurationValues verifyConfigurationValues;
    private final QuestionTimeConfiguration configuration;
    private final PluginConfigurationSave pluginConfigurationSave;
    private final ConfigMutable<Integer> minConnectedConfig;
    private final Logger logger;

    public SetConfigMinimumConnectedExecutor(final VerifyConfigurationValues verifyConfigurationValues, final QuestionTimeConfiguration configuration,
                                             final PluginConfigurationSave pluginConfigurationSave, final ConfigMutable<Integer> minConnectedConfig,
                                             final Logger logger) {
        this.verifyConfigurationValues = verifyConfigurationValues;
        this.configuration = configuration;
        this.pluginConfigurationSave = pluginConfigurationSave;
        this.minConnectedConfig = minConnectedConfig;
        this.logger = logger;
    }

    @Override
    public CommandResult execute(final CommandContext context) throws CommandException {
        final Integer minimumConnected = context.requireOne(MINIMUM_CONNECTED);
        final int previousValue = this.configuration.getMinConnected();
        this.configuration.setMinConnected(minimumConnected);
        final VerifyConfigurationValues.Result verificationResult = this.verifyConfigurationValues.verify(this.configuration);
        if(verificationResult.isSuccess()) {
            try {
                pluginConfigurationSave.save(this.configuration);
                this.minConnectedConfig.setValue(minimumConnected);
                context.sendMessage(TextUtils.composed("", "Minimum connected", " set to ", String.valueOf(minimumConnected), " !"));
                return CommandResult.success();
            } catch (final IOException e) {
                this.configuration.setMinConnected(previousValue);
                this.logger.error(e);
                return CommandResult.error(TextUtils.errorWithPrefix("An error occurred while saving the configuration, see the log for more details."));
            }
        } else {
            this.configuration.setMinConnected(previousValue);
            return CommandResult.error(QuestionsTime.PREFIX.append(Component.join(JoinConfiguration.commas(true),
                    verificationResult.getWrongValues().get(ConfigField.MINIMUM_CONNECTED).stream()
                            .map(errorReason -> Component.text("The value must not be " + errorReason, NamedTextColor.RED)).toList())));
        }
    }
}
