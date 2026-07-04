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

public class SetConfigCooldownExecutor implements CommandExecutor {

    public static final Parameter.Value<Integer> COOLDOWN = Parameter.integerNumber().key("cooldown").build();

    private final VerifyConfigurationValues verifyConfigurationValues;
    private final QuestionTimeConfiguration configuration;
    private final PluginConfigurationSave pluginConfigurationSave;
    private final ConfigMutable<Integer> cooldownConfig;
    private final Logger logger;

    public SetConfigCooldownExecutor(final VerifyConfigurationValues verifyConfigurationValues, final QuestionTimeConfiguration configuration,
                                     final PluginConfigurationSave pluginConfigurationSave, final ConfigMutable<Integer> cooldownConfig,
                                     final Logger logger) {
        this.verifyConfigurationValues = verifyConfigurationValues;
        this.configuration = configuration;
        this.pluginConfigurationSave = pluginConfigurationSave;
        this.cooldownConfig = cooldownConfig;
        this.logger = logger;
    }

    @Override
    public CommandResult execute(final CommandContext context) throws CommandException {
        final Integer cooldown = context.requireOne(COOLDOWN);
        final int previousValue = this.configuration.getCooldown();
        this.configuration.setCooldown(cooldown);
        final VerifyConfigurationValues.Result verificationResult = this.verifyConfigurationValues.verify(this.configuration);
        if(verificationResult.isSuccess()) {
            try {
                this.pluginConfigurationSave.save(this.configuration);
                this.cooldownConfig.setValue(cooldown);
                context.sendMessage(TextUtils.composed("", "Cooldown", " set to ", String.valueOf(cooldown), " ticks !"));
                return CommandResult.success();
            } catch (final IOException e) {
                this.configuration.setCooldown(previousValue);
                this.logger.error(e);
                return CommandResult.error(TextUtils.errorWithPrefix("An error occurred while saving the configuration, see the log for more details."));
            }
        } else {
            this.configuration.setCooldown(previousValue);
            return CommandResult.error(QuestionsTime.PREFIX.append(Component.join(JoinConfiguration.commas(true),
                    verificationResult.getWrongValues().get(ConfigField.COOLDOWN).stream()
                            .map(errorReason -> Component.text("The value must not be " + errorReason, NamedTextColor.RED)).toList())));
        }
    }
}
