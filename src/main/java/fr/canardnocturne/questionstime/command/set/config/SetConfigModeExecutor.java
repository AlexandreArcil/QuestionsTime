package fr.canardnocturne.questionstime.command.set.config;

import fr.canardnocturne.questionstime.config.ConfigMutable;
import fr.canardnocturne.questionstime.config.QuestionTimeConfiguration;
import fr.canardnocturne.questionstime.config.save.PluginConfigurationSave;
import fr.canardnocturne.questionstime.question.ask.launcher.QuestionLauncher;
import fr.canardnocturne.questionstime.question.ask.launcher.QuestionLauncherFactory;
import fr.canardnocturne.questionstime.util.TextUtils;
import org.apache.logging.log4j.Logger;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;

import java.io.IOException;

public class SetConfigModeExecutor implements CommandExecutor {

    public static final Parameter.Value<QuestionTimeConfiguration.Mode> MODES = Parameter.enumValue(QuestionTimeConfiguration.Mode.class).key("modes").build();

    private final QuestionTimeConfiguration configuration;
    private final PluginConfigurationSave pluginConfigurationSave;
    private final QuestionLauncherFactory questionLauncherFactory;
    private final ConfigMutable<QuestionLauncher> questionLauncherConfig;
    private final Logger logger;

    public SetConfigModeExecutor(final QuestionTimeConfiguration configuration, final PluginConfigurationSave pluginConfigurationSave,
                                 final QuestionLauncherFactory questionLauncherFactory, final ConfigMutable<QuestionLauncher> questionLauncherConfig,
                                 final Logger logger) {
        this.configuration = configuration;
        this.pluginConfigurationSave = pluginConfigurationSave;
        this.questionLauncherFactory = questionLauncherFactory;
        this.questionLauncherConfig = questionLauncherConfig;
        this.logger = logger;
    }

    @Override
    public CommandResult execute(final CommandContext context) throws CommandException {
        final QuestionTimeConfiguration.Mode mode = context.requireOne(MODES);
        final QuestionTimeConfiguration.Mode previousValue = this.configuration.getMode();
        this.configuration.setMode(mode);
        try {
            this.pluginConfigurationSave.save(this.configuration);
            final QuestionLauncher previousQuestionLauncher = this.questionLauncherConfig.getValue();
            if(previousQuestionLauncher != null) {
                previousQuestionLauncher.stop();
            }
            final QuestionLauncher questionLauncher = this.questionLauncherFactory.create(mode);
            if(questionLauncher != null) {
                questionLauncher.start();
            }
            this.questionLauncherConfig.setValue(questionLauncher);
            context.sendMessage(TextUtils.composed("", "Mode", " set to ", mode.name().toLowerCase(), " !"));
            return CommandResult.success();
        } catch (final IOException e) {
            this.configuration.setMode(previousValue);
            this.logger.error(e);
            return CommandResult.error(TextUtils.errorWithPrefix("An error occurred while saving the configuration, see the log for more details."));
        }
    }

}
