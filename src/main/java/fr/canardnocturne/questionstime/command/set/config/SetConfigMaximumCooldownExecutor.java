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

public class SetConfigMaximumCooldownExecutor implements CommandExecutor {

    public static final Parameter.Value<Integer> MAXIMUM_COOLDOWN = Parameter.integerNumber().key("maximum_cooldown").build();

    private final VerifyConfigurationValues verifyConfigurationValues;
    private final QuestionTimeConfiguration configuration;
    private final PluginConfigurationSave pluginConfigurationSave;
    private final ConfigMutable<Integer> maxCooldownConfig;
    private final Logger logger;

    public SetConfigMaximumCooldownExecutor(final VerifyConfigurationValues verifyConfigurationValues, final QuestionTimeConfiguration configuration,
                                            final PluginConfigurationSave pluginConfigurationSave, final ConfigMutable<Integer> maxCooldownConfig,
                                            final Logger logger) {
        this.verifyConfigurationValues = verifyConfigurationValues;
        this.configuration = configuration;
        this.pluginConfigurationSave = pluginConfigurationSave;
        this.maxCooldownConfig = maxCooldownConfig;
        this.logger = logger;
    }

    @Override
    public CommandResult execute(final CommandContext context) throws CommandException {
        final Integer cooldown = context.requireOne(MAXIMUM_COOLDOWN);
        final int previousValue = this.configuration.getMaxCooldown();
        this.configuration.setMaxCooldown(cooldown);
        final VerifyConfigurationValues.Result verificationResult = this.verifyConfigurationValues.verify(this.configuration);
        if(verificationResult.isSuccess()) {
            try {
                pluginConfigurationSave.save(this.configuration);
                this.maxCooldownConfig.setValue(cooldown);
                context.sendMessage(TextUtils.composed("", "Maximum cooldown", " set to ", String.valueOf(cooldown), " ticks !"));
                return CommandResult.success();
            } catch (final IOException e) {
                this.configuration.setMaxCooldown(previousValue);
                this.logger.error(e);
                return CommandResult.error(TextUtils.errorWithPrefix("An error occurred while saving the configuration, see the log for more details."));
            }
        } else {
            this.configuration.setMaxCooldown(previousValue);
            return CommandResult.error(QuestionsTime.PREFIX.append(Component.join(JoinConfiguration.commas(true),
                    verificationResult.getWrongValues().get(ConfigField.MAX_COOLDOWN).stream()
                            .map(errorReason -> Component.text("The value must not be " + errorReason, NamedTextColor.RED)).toList())));
        }
    }
}
