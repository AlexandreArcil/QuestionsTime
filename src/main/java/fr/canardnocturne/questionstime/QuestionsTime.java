package fr.canardnocturne.questionstime;

import com.google.inject.Inject;
import fr.canardnocturne.questionstime.config.QuestionTimeConfiguration;
import fr.canardnocturne.questionstime.config.loader.PluginConfigurationLoader;
import fr.canardnocturne.questionstime.config.loader.SafePluginConfigurationLoader;
import fr.canardnocturne.questionstime.config.verificator.SetDefaultWrongConfigurationValues;
import fr.canardnocturne.questionstime.config.verificator.VerifyConfigurationValues;
import fr.canardnocturne.questionstime.message.Messages;
import fr.canardnocturne.questionstime.message.SimpleMessage;
import fr.canardnocturne.questionstime.message.reader.HoconMessageReader;
import fr.canardnocturne.questionstime.message.reader.MessageReader;
import fr.canardnocturne.questionstime.message.updater.MessageUpdater;
import fr.canardnocturne.questionstime.message.updater.SafeMessageUpdater;
import fr.canardnocturne.questionstime.question.ask.QuestionAskManager;
import fr.canardnocturne.questionstime.question.ask.announcer.QuestionAnnouncer;
import fr.canardnocturne.questionstime.question.ask.announcer.SimpleQuestionAnnouncer;
import fr.canardnocturne.questionstime.question.ask.answer.PlayerAnswerQuestionEventHandler;
import fr.canardnocturne.questionstime.question.ask.launcher.QuestionLauncher;
import fr.canardnocturne.questionstime.question.ask.launcher.QuestionLauncherFactory;
import fr.canardnocturne.questionstime.question.ask.picker.QuestionPicker;
import fr.canardnocturne.questionstime.question.ask.picker.WeightedRandomnessQuestionPicker;
import fr.canardnocturne.questionstime.question.ask.pool.QuestionPool;
import fr.canardnocturne.questionstime.question.ask.pool.WeightSortedQuestionPool;
import fr.canardnocturne.questionstime.question.component.Malus;
import fr.canardnocturne.questionstime.question.component.Prize;
import fr.canardnocturne.questionstime.question.creation.CreateQuestionCommand;
import fr.canardnocturne.questionstime.question.creation.CreatorLeftServerEventHandler;
import fr.canardnocturne.questionstime.question.creation.QuestionCreationManager;
import fr.canardnocturne.questionstime.question.save.HoconQuestionRegister;
import fr.canardnocturne.questionstime.question.save.QuestionRegister;
import fr.canardnocturne.questionstime.question.serializer.MalusSerializer;
import fr.canardnocturne.questionstime.question.serializer.PrizeSerializer;
import fr.canardnocturne.questionstime.question.serializer.QuestionSerializer;
import fr.canardnocturne.questionstime.question.type.Question;
import io.leangen.geantyref.TypeToken;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.apache.logging.log4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.LoadedGameEvent;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.api.event.lifecycle.StartedEngineEvent;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.builtin.jvm.Plugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

@Plugin("questionstime")
public class QuestionsTime {

    private static QuestionsTime instance;

    public static final TextComponent PREFIX = Component.text("[", NamedTextColor.AQUA)
            .append(Component.text("QT", NamedTextColor.YELLOW))
            .append(Component.text("] ", NamedTextColor.AQUA));

    private final Logger logger;
    private final Game game;
    private final PluginContainer plugin;
    private final Path pluginFolder;
    private final MessageReader messageReader;
    private final MessageUpdater messageUpdater;
    private final PluginConfigurationLoader pluginConfigurationLoader;
    private final VerifyConfigurationValues verifyConfigurationValues;

    private EconomyService economy;
    private QuestionTimeConfiguration pluginConfig;
    private QuestionCreationManager questionCreationManager;
    private QuestionLauncher questionLauncher;
    private QuestionPool questionPool;

    @Inject
    public QuestionsTime(final Logger logger, final Game game, @ConfigDir(sharedRoot = false) final Path pluginFolder, final PluginContainer pluginContainer) {
        this.logger = logger;
        this.game = game;
        this.pluginFolder = pluginFolder;
        this.plugin = pluginContainer;
        this.messageReader = new HoconMessageReader(logger);
        this.messageUpdater = new SafeMessageUpdater(logger);
        this.pluginConfigurationLoader = new SafePluginConfigurationLoader(logger);
        this.verifyConfigurationValues = new SetDefaultWrongConfigurationValues(logger);
    }

    @Listener
    public void onServerStarted(final StartedEngineEvent<Server> event) {
        instance = this;
        Sponge.server().serviceProvider().provide(EconomyService.class).ifPresent(economyService -> this.economy = economyService);

        final QuestionPicker questionPicker = new WeightedRandomnessQuestionPicker(this.questionPool, this.logger);
        final QuestionAnnouncer questionAnnouncer = new SimpleQuestionAnnouncer(this.game, this.economy, this.plugin);
        final QuestionAskManager questionAskManager = new QuestionAskManager(questionPicker, questionAnnouncer, this.questionCreationManager, this.game, this.economy, this.plugin, this.logger, this.pluginConfig.getMinConnected());
        try {
            this.questionLauncher = QuestionLauncherFactory.create(this.pluginConfig, this.plugin, this.game, questionAskManager);
            questionAskManager.setQuestionLauncher(questionLauncher);
        } catch (final IllegalStateException e) {
            this.logger.error(e.getMessage(), e);
        }

        Sponge.eventManager().registerListeners(this.plugin, new PlayerAnswerQuestionEventHandler(questionAskManager, this.pluginConfig.isPersonalAnswer()));
        Sponge.eventManager().registerListeners(this.plugin, new CreatorLeftServerEventHandler(this.questionCreationManager));
    }

    @Listener
    public void registerCommands(final RegisterCommandEvent<Command.Parameterized> event) {
        // Can't put this part of code in ConstructPluginEvent because ItemStackSerializer#fromString throw en error
        // with RegistryHolder#registry(RegistryTypes.ITEM_TYPE) as this registry doesn't seem to exist yet
        final Path configFile = this.pluginFolder.resolve("config.conf");
        final ConfigurationLoader<CommentedConfigurationNode> configLoader = HoconConfigurationLoader.builder()
                .defaultOptions(ConfigurationOptions.defaults()
                        .serializers(ConfigurationOptions.defaults().serializers().childBuilder()
                                .register(TypeToken.get(Question.class), new QuestionSerializer())
                                .register(TypeToken.get(Prize.class), new PrizeSerializer(this.logger))
                                .register(TypeToken.get(Malus.class), new MalusSerializer())
                                .build())).path(configFile)
                .build();
        final QuestionRegister questionRegister = new HoconQuestionRegister(configLoader, logger);
        try {
            if (Files.notExists(this.pluginFolder)) {
                Files.createDirectories(this.pluginFolder);
            }
            this.createConfigFile(configFile, configLoader);
            this.loadConfig(configLoader);

            final Path messagesConfigPath = pluginFolder.resolve("message.conf");
            this.createMessagesFile(messagesConfigPath);
            this.loadMessages(messagesConfigPath);
        } catch (final IOException e) {
            this.logger.error("Unable to create the plugin folder", e);
        }

        this.questionPool = new WeightSortedQuestionPool(this.pluginConfig.getQuestions());
        this.questionCreationManager = new QuestionCreationManager(this, this.questionPool, questionRegister);

        final Command.Parameterized commandQTBase = Command.builder()
                .shortDescription(Component.text("Create a question").color(NamedTextColor.YELLOW))
                .permission("questionstime.command.create")
                .executionRequirements(commandCause -> commandCause.root() instanceof ServerPlayer)
                .addParameter(CreateQuestionCommand.STEP_ARG)
                .executor(new CreateQuestionCommand(this.questionCreationManager))
                .build();
        event.register(this.plugin, commandQTBase, "questionstimecreator", "qtc");
    }

    @Listener
    public void onGameLoaded(final LoadedGameEvent event) {
        if (this.questionLauncher != null) {
            if (!this.questionPool.getAll().isEmpty()) {
                this.questionLauncher.start();
            } else {
                this.logger.warn("No questions registered, did you add questions in the config.conf file ?");
            }
        }
    }

    private void createConfigFile(final Path configFile, final ConfigurationLoader<CommentedConfigurationNode> configLoader) {
        if (Files.notExists(configFile)) {
            try {
                this.logger.info("Creating config.conf...");
                final CommentedConfigurationNode root = configLoader.load();
                root.set(QuestionTimeConfiguration.class, new QuestionTimeConfiguration());
                configLoader.save(root);
                this.logger.info("config.conf created with default values!");
            } catch (final SerializationException e) {
                this.logger.error("A problem occurred when saving the default values of the config.conf file", e);
            } catch (final IOException e) {
                this.logger.error("A problem occurred when creating or loading the config.conf file", e);
            }
        }
    }

    private void loadConfig(final ConfigurationLoader<CommentedConfigurationNode> configLoader) {
        this.logger.info("Loading configurations from config.conf...");
        final QuestionTimeConfiguration questionTimeConfiguration = this.pluginConfigurationLoader.load(configLoader);
        this.verifyConfigurationValues.verify(questionTimeConfiguration);
        this.pluginConfig = questionTimeConfiguration;
        this.logger.info("Configuration loaded with {} questions", String.valueOf(questionTimeConfiguration.getQuestions().size()));
    }

    private void createMessagesFile(final Path messagesFile) {
        if (Files.notExists(messagesFile)) {
            this.logger.info("Creating messages.conf...");
            try {
                final HoconConfigurationLoader loader = HoconConfigurationLoader.builder().path(messagesFile).build();
                final CommentedConfigurationNode root = loader.load();
                for (final SimpleMessage message : Messages.getAll()) {
                    root.node(message.getSection()).set(message.getMessage());
                }
                loader.save(root);
                this.logger.info("messages.conf created with the default values!");
            } catch (final SerializationException e) {
                this.logger.error("A problem occurred when saving the default values of the messages.conf file", e);
            } catch (final IOException e) {
                this.logger.error("A problem occurred when creating or loading the messages.conf file", e);
            }
        }
    }

    private void loadMessages(final Path messagesConfig) {
        if (Files.exists(messagesConfig)) {
            this.logger.info("Loading messages from messages.conf...");
            final Map<String, String> readMessages = this.messageReader.readMessages(messagesConfig);
            this.messageUpdater.updateMessages(readMessages);
        }
    }

    public Logger getLogger() {
        return logger;
    }

    public static QuestionsTime getInstance() {
        return instance;
    }

    public Optional<EconomyService> getEconomy() {
        return Optional.ofNullable(economy);
    }

}