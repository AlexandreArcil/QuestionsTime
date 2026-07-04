package fr.canardnocturne.questionstime.command.set.config;

import fr.canardnocturne.questionstime.config.ConfigMutable;
import fr.canardnocturne.questionstime.config.QuestionTimeConfiguration;
import fr.canardnocturne.questionstime.config.save.PluginConfigurationSave;
import fr.canardnocturne.questionstime.util.TextUtils;
import org.apache.logging.log4j.Logger;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;

import java.io.IOException;

public class SetConfigPersonalAnswerExecutor implements CommandExecutor {

    public static final Parameter.Value<Boolean> PERSONAL_ANSWER = Parameter.bool().key("personal_answer").build();

    private final QuestionTimeConfiguration configuration;
    private final PluginConfigurationSave pluginConfigurationSave;
    private final ConfigMutable<Boolean> personalAnswerConfig;
    private final Logger logger;

    public SetConfigPersonalAnswerExecutor(final QuestionTimeConfiguration configuration, final PluginConfigurationSave pluginConfigurationSave,
                                           final ConfigMutable<Boolean> personalAnswerConfig, final Logger logger) {
        this.configuration = configuration;
        this.pluginConfigurationSave = pluginConfigurationSave;
        this.personalAnswerConfig = personalAnswerConfig;
        this.logger = logger;
    }

    @Override
    public CommandResult execute(final CommandContext context) throws CommandException {
        final boolean personalAnswer = context.requireOne(PERSONAL_ANSWER);
        final boolean previousValue = this.configuration.isPersonalAnswer();
        this.configuration.setPersonalAnswer(personalAnswer);
        try {
            this.pluginConfigurationSave.save(this.configuration);
            this.personalAnswerConfig.setValue(personalAnswer);
            context.sendMessage(TextUtils.composed("", "Personal answer", " set to ", String.valueOf(personalAnswer), " !"));
            return CommandResult.success();
        } catch (final IOException e) {
            this.configuration.setPersonalAnswer(previousValue);
            this.logger.error(e);
            return CommandResult.error(TextUtils.errorWithPrefix("An error occurred while saving the configuration, see the log for more details."));
        }
    }

}
