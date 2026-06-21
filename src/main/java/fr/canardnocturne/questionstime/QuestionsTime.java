package fr.canardnocturne.questionstime;

import com.google.inject.Inject;
import fr.canardnocturne.questionstime.command.BaseCommandExecutor;
import fr.canardnocturne.questionstime.command.change.ChangeConfigurationExecutor;
import fr.canardnocturne.questionstime.command.change.QuestionModifier;
import fr.canardnocturne.questionstime.command.change.QuestionModifierImpl;
import fr.canardnocturne.questionstime.command.set.answers.SetQuestionAnswers;
import fr.canardnocturne.questionstime.command.set.answers.SetQuestionAnswersList;
import fr.canardnocturne.questionstime.command.set.SetQuestionExecutor;
import fr.canardnocturne.questionstime.command.set.SetQuestionTimeBetweenAnswer;
import fr.canardnocturne.questionstime.command.set.SetQuestionTimerExecutor;
import fr.canardnocturne.questionstime.command.set.SetQuestionWeightExecutor;
import fr.canardnocturne.questionstime.command.set.malus.SetQuestionMalusAnnounceExecutor;
import fr.canardnocturne.questionstime.command.set.malus.SetQuestionMalusCommandsExecutor;
import fr.canardnocturne.questionstime.command.set.malus.SetQuestionMalusCommandsListExecutor;
import fr.canardnocturne.questionstime.command.set.malus.SetQuestionMalusMoneyExecutor;
import fr.canardnocturne.questionstime.command.set.prize.SetQuestionPrizesCommandsExecutor;
import fr.canardnocturne.questionstime.command.set.prize.SetQuestionPrizesCommandsListExecutor;
import fr.canardnocturne.questionstime.command.set.prize.SetQuestionPrizesItemsExecutor;
import fr.canardnocturne.questionstime.command.set.prize.SetQuestionPrizesItemsListExecutor;
import fr.canardnocturne.questionstime.command.set.prize.SetQuestionPrizesMoneyExecutor;
import fr.canardnocturne.questionstime.command.set.propositions.SetQuestionPropositions;
import fr.canardnocturne.questionstime.command.set.propositions.SetQuestionPropositionsList;
import fr.canardnocturne.questionstime.config.QuestionTimeConfiguration;
import fr.canardnocturne.questionstime.config.loader.PluginConfigurationLoader;
import fr.canardnocturne.questionstime.config.loader.SafePluginConfigurationLoader;
import fr.canardnocturne.questionstime.config.serializer.ModeTypeSerializer;
import fr.canardnocturne.questionstime.config.upgrade.ConfigurationUpgrade;
import fr.canardnocturne.questionstime.config.upgrade.ConfigurationUpgradeException;
import fr.canardnocturne.questionstime.config.upgrade.ConfigurationUpgradeOrchestrator;
import fr.canardnocturne.questionstime.config.upgrade.update.FirstVersionConfigurationUpdate;
import fr.canardnocturne.questionstime.config.upgrade.update.NoVersionConfigurationUpdate;
import fr.canardnocturne.questionstime.config.verificator.SetDefaultWrongConfigurationValues;
import fr.canardnocturne.questionstime.config.verificator.VerifyConfigurationValues;
import fr.canardnocturne.questionstime.message.Messages;
import fr.canardnocturne.questionstime.message.SimpleMessage;
import fr.canardnocturne.questionstime.message.reader.HoconMessageReader;
import fr.canardnocturne.questionstime.message.reader.MessageReader;
import fr.canardnocturne.questionstime.message.updater.MessageUpdater;
import fr.canardnocturne.questionstime.message.updater.SafeMessageUpdater;
import fr.canardnocturne.questionstime.message.updater.config.AddMissingMessageConfiguration;
import fr.canardnocturne.questionstime.message.updater.config.MessageConfigurationUpdater;
import fr.canardnocturne.questionstime.question.ask.QuestionAskManager;
import fr.canardnocturne.questionstime.question.ask.announcer.QuestionAnnouncer;
import fr.canardnocturne.questionstime.question.ask.announcer.SimpleQuestionAnnouncer;
import fr.canardnocturne.questionstime.question.ask.answer.PlayerAnswerQuestionEventHandler;
import fr.canardnocturne.questionstime.question.ask.launcher.ManualAskQuestionCommand;
import fr.canardnocturne.questionstime.question.ask.launcher.QuestionLauncher;
import fr.canardnocturne.questionstime.question.ask.launcher.QuestionLauncherFactory;
import fr.canardnocturne.questionstime.question.ask.picker.QuestionPicker;
import fr.canardnocturne.questionstime.question.ask.picker.WeightedRandomnessQuestionPicker;
import fr.canardnocturne.questionstime.question.ask.pool.QuestionPool;
import fr.canardnocturne.questionstime.question.ask.pool.WeightSortedQuestionPool;
import fr.canardnocturne.questionstime.question.component.Malus;
import fr.canardnocturne.questionstime.question.component.Prize;
import fr.canardnocturne.questionstime.question.component.OutcomeCommand;
import fr.canardnocturne.questionstime.question.creation.CreateQuestionCommand;
import fr.canardnocturne.questionstime.question.creation.CreatorLeftServerEventHandler;
import fr.canardnocturne.questionstime.question.creation.QuestionCreationManager;
import fr.canardnocturne.questionstime.question.creation.orchestrator.StoppableQuestionCreationOrchestrator;
import fr.canardnocturne.questionstime.question.save.HoconQuestionRegister;
import fr.canardnocturne.questionstime.question.save.QuestionRegister;
import fr.canardnocturne.questionstime.question.serializer.MalusTypeSerializer;
import fr.canardnocturne.questionstime.question.serializer.OutcomeCommandTypeSerializer;
import fr.canardnocturne.questionstime.question.serializer.PrizeTypeSerializer;
import fr.canardnocturne.questionstime.question.serializer.QuestionSerializer;
import fr.canardnocturne.questionstime.question.Question;
import fr.canardnocturne.questionstime.util.TextUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.apache.logging.log4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.LoadedGameEvent;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.api.event.lifecycle.StartedEngineEvent;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.builtin.jvm.Plugin;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@Plugin("questionstime")
public class QuestionsTime {

    public static final TextComponent PREFIX = Component.text("[", NamedTextColor.AQUA)
            .append(Component.text("QT", NamedTextColor.YELLOW))
            .append(Component.text("] ", NamedTextColor.AQUA));

    private final Logger logger;
    private final Game game;
    private final PluginContainer plugin;
    private final Path pluginFolder;
    private final MessageReader messageReader;
    private final MessageUpdater messageUpdater;
    private final MessageConfigurationUpdater messageConfigurationUpdater;
    private final PluginConfigurationLoader pluginConfigurationLoader;
    private final ConfigurationUpgrade configurationUpgrade;
    private final VerifyConfigurationValues verifyConfigurationValues;

    private QuestionTimeConfiguration pluginConfig;
    private QuestionCreationManager questionCreationManager;
    private QuestionLauncher questionLauncher;
    private QuestionPool questionPool;
    private QuestionAskManager questionAskManager;

    @Inject
    public QuestionsTime(final Logger logger, final Game game, @ConfigDir(sharedRoot = false) final Path pluginFolder, final PluginContainer pluginContainer) {
        this.logger = logger;
        this.game = game;
        this.pluginFolder = pluginFolder;
        this.plugin = pluginContainer;
        this.messageReader = new HoconMessageReader(logger);
        this.messageUpdater = new SafeMessageUpdater(logger);
        this.messageConfigurationUpdater = new AddMissingMessageConfiguration(logger);
        this.pluginConfigurationLoader = new SafePluginConfigurationLoader(logger);
        this.configurationUpgrade = new ConfigurationUpgradeOrchestrator(List.of(new NoVersionConfigurationUpdate(), new FirstVersionConfigurationUpdate(), new SecondVersionConfigurationUpdate()), logger);
        this.verifyConfigurationValues = new SetDefaultWrongConfigurationValues(logger);
    }

    @Listener
    public void onServerStarted(final StartedEngineEvent<Server> event) {
        Sponge.eventManager()
                .registerListeners(this.plugin, new PlayerAnswerQuestionEventHandler(this.questionAskManager, this.pluginConfig.isPersonalAnswer()), MethodHandles.lookup())
                .registerListeners(this.plugin, new CreatorLeftServerEventHandler(this.questionCreationManager), MethodHandles.lookup());
    }

    @Listener
    public void registerCommands(final RegisterCommandEvent<Command.Parameterized> event) {
        // Can't put this part of code in ConstructPluginEvent because ItemStackSerializer#fromString throw en error
        // with RegistryHolder#registry(RegistryTypes.ITEM_TYPE) as this registry doesn't seem to exist yet
        final Path configFile = this.pluginFolder.resolve("config.conf");
        final ConfigurationLoader<CommentedConfigurationNode> configLoader = HoconConfigurationLoader.builder()
                .defaultOptions(ConfigurationOptions.defaults()
                        .serializers(ConfigurationOptions.defaults().serializers().childBuilder()
                                .register(Question.class, new QuestionSerializer())
                                .register(Prize.class, new PrizeTypeSerializer())
                                .register(Malus.class, new MalusTypeSerializer())
                                .register(OutcomeCommand.class, new OutcomeCommandTypeSerializer())
                                .register(QuestionTimeConfiguration.Mode.class, new ModeTypeSerializer())
                                .build()))
                .path(configFile)
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
        this.questionCreationManager = new QuestionCreationManager(new StoppableQuestionCreationOrchestrator.StoppableQuestionCreationOrchestratorFactory(),
                this.questionPool, questionRegister, this.logger);

        final QuestionPicker questionPicker = new WeightedRandomnessQuestionPicker(this.questionPool, this.logger);
        final QuestionAnnouncer questionAnnouncer = new SimpleQuestionAnnouncer(this.game, this.plugin);
        this.questionAskManager = new QuestionAskManager(questionPicker, questionAnnouncer, this.questionCreationManager, this.game, this.plugin, this.logger, this.pluginConfig.getMinConnected());
        try {
            this.questionLauncher = QuestionLauncherFactory.create(this.pluginConfig, this.plugin, this.game, this.questionAskManager);
            this.questionAskManager.setQuestionLauncher(questionLauncher);
        } catch (final IllegalStateException e) {
            this.logger.error(e.getMessage(), e);
        }

        final Command.Parameterized commandQTCreator = Command.builder()
                .shortDescription(Component.text("Create a question").color(NamedTextColor.YELLOW))
                .permission("questionstime.command.create")
                .executionRequirements(commandCause -> commandCause.root() instanceof ServerPlayer)
                .addParameter(CreateQuestionCommand.STEP_ARG)
                .executor(new CreateQuestionCommand(this.questionCreationManager))
                .build();
        event.register(this.plugin, commandQTCreator, "questionstimecreator", "qtc");

        //For the Optional#get: As specified in the doc, the function should take as argument a choice which is from the collection of questions, so I assume that it should always get a question from a player choice
        final Parameter.Value<Question> specificQuestionParameter = Parameter.choices(Question.class, s -> this.questionPool.get(s).get(), () -> this.questionPool.getAll().stream().map(Question::getQuestion).toList()).key("question").build();
        final Parameter questionParameter = Parameter.firstOf(ManualAskQuestionCommand.RANDOM_QUESTION_ARG, specificQuestionParameter);
        final Command.Parameterized commandQTAskQuestion = Command.builder()
                .shortDescription(Component.text("Ask a question").color(NamedTextColor.YELLOW))
                .permission("questionstime.command.ask")
                .addParameter(questionParameter)
                .executor(new ManualAskQuestionCommand(this.questionAskManager, this.questionLauncher, specificQuestionParameter, this.logger))
                .build();

//        final ChangeQuestionComponentExecutor changeQuestionComponentExecutor = new ChangeQuestionComponentExecutor(
//                new QuestionModifierImpl(this.questionPool), questionRegister, specificQuestionParameter);
        final QuestionModifier questionModifier = new QuestionModifierImpl();

        final Command.Parameterized commandQTSetQuestionMalusAnnounce = Command.builder()
                .shortDescription(Component.text("Set the malus announce for a question").color(NamedTextColor.YELLOW))
                .addParameters(SetQuestionMalusAnnounceExecutor.VALUE)
                .executor(new SetQuestionMalusAnnounceExecutor(specificQuestionParameter, questionModifier, questionPool, questionRegister))
                .build();

        final Command.Parameterized commandQTSetQuestionMalusCommandsList = Command.builder()
                .shortDescription(Component.text("List the malus commands for a question").color(NamedTextColor.YELLOW))
                .executor(new SetQuestionMalusCommandsListExecutor())
                .build();

        final Command.Parameterized commandQTSetQuestionMalusCommands = Command.builder()
                .shortDescription(Component.text("Set the question malus commands").color(NamedTextColor.YELLOW))
                .addParameters(Parameter.firstOf(Parameter.subcommand(commandQTSetQuestionMalusCommandsList, "list"),
                        SetQuestionMalusCommandsExecutor.ACTIONS))
                .executor(new SetQuestionMalusCommandsExecutor())
                .build();

        final Command.Parameterized commandQTSetQuestionMalusMoney = Command.builder()
                .shortDescription(Component.text("Set the malus money for a question").color(NamedTextColor.YELLOW))
                .executor(new SetQuestionMalusMoneyExecutor())
                .build();

        final Command.Parameterized commandQTSetMalus = Command.builder()
                .shortDescription(Component.text("Set the malus for a question").color(NamedTextColor.YELLOW))
                .addChild(commandQTSetQuestionMalusAnnounce, "announce")
                .addChild(commandQTSetQuestionMalusCommands, "commands")
                .addChild(commandQTSetQuestionMalusMoney, "money")
                .build();

        final Command.Parameterized commandQTSetQuestionPrizesMoney = Command.builder()
                .shortDescription(Component.text("Set the prizes money for a question").color(NamedTextColor.YELLOW))
                .addParameters(SetQuestionPrizesMoneyExecutor.POSITION, SetQuestionPrizesMoneyExecutor.AMOUNT)
                .executor(new SetQuestionPrizesMoneyExecutor())
                .build();

        final Command.Parameterized commandQTSetQuestionPrizesCommandsList = Command.builder()
                .shortDescription(Component.text("List the question commands prizes").color(NamedTextColor.YELLOW))
                .executor(new SetQuestionPrizesCommandsListExecutor())
                .build();

        final Command.Parameterized commandQTSetQuestionPrizesItemsList = Command.builder()
                .shortDescription(Component.text("List the question items prizes").color(NamedTextColor.YELLOW))
                .executor(new SetQuestionPrizesItemsListExecutor())
                .build();

        final Command.Parameterized commandQTSetQuestionPrizesCommands = Command.builder()
                .shortDescription(Component.text("Set the question prizes commands").color(NamedTextColor.YELLOW))
                .addParameters(Parameter.firstOf(Parameter.subcommand(commandQTSetQuestionPrizesCommandsList, "list"),
                        SetQuestionPrizesCommandsExecutor.ACTIONS))
                .executor(new SetQuestionPrizesCommandsExecutor())
                .build();

        final Command.Parameterized commandQTSetQuestionPrizesItems = Command.builder()
                .shortDescription(Component.text("Set the question prizes items").color(NamedTextColor.YELLOW))
                .addParameters(Parameter.firstOf(Parameter.subcommand(commandQTSetQuestionPrizesItemsList, "list"),
                        SetQuestionPrizesItemsExecutor.ACTIONS))
                .executor(new SetQuestionPrizesItemsExecutor())
                .build();

        final Command.Parameterized commandQTSetQuestionPrizes = Command.builder()
                .shortDescription(Component.text("Set the prizes for a question").color(NamedTextColor.YELLOW))
//                .executor(changeQuestionComponentExecutor)
                .addChild(commandQTSetQuestionPrizesMoney, "money")
                .addChild(commandQTSetQuestionPrizesCommands, "commands")
                .addChild(commandQTSetQuestionPrizesItems, "items")
                .build();

        final Command.Parameterized commandQTSetWeight = Command.builder()
                .shortDescription(Component.text("Set the weight of a question").color(NamedTextColor.YELLOW))
                .addParameters(SetQuestionWeightExecutor.WEIGHT)
                .executor(new SetQuestionWeightExecutor())
                .build();

        final Command.Parameterized commandQTSetTimer = Command.builder()
                .shortDescription(Component.text("Set the timer of a question").color(NamedTextColor.YELLOW))
                .addParameters(SetQuestionTimerExecutor.TIMER)
                .executor(new SetQuestionTimerExecutor())
                .build();

        final Command.Parameterized commandQTSetTimeBetweenAnswer = Command.builder()
                .shortDescription(Component.text("Set the time between answers of a question").color(NamedTextColor.YELLOW))
                .addParameters(SetQuestionTimeBetweenAnswer.TIME_BETWEEN_ANSWER)
                .executor(new SetQuestionTimeBetweenAnswer())
                .build();

        final Command.Parameterized commandQTSetQuestionQuestion = Command.builder()
                .shortDescription(Component.text("Set the question").color(NamedTextColor.YELLOW))
                .addParameters(SetQuestionExecutor.QUESTION)
                .executor(new SetQuestionExecutor())
                .build();

        final Command.Parameterized commandQTSetQuestionAnswersList = Command.builder()
                .shortDescription(Component.text("List the question answers").color(NamedTextColor.YELLOW))
                .executor(new SetQuestionAnswersList(specificQuestionParameter))
                .build();

        final Command.Parameterized commandQTSetQuestionAnswers = Command.builder()
                .shortDescription(Component.text("Set the question answers").color(NamedTextColor.YELLOW))
                .addParameters(Parameter.firstOf(Parameter.subcommand(commandQTSetQuestionAnswersList, "list"), SetQuestionAnswers.ACTIONS))
                .executor(new SetQuestionAnswers(specificQuestionParameter, questionModifier, questionPool, questionRegister))
                .build();

        final Command.Parameterized commandQTSetQuestionPropositionsList = Command.builder()
                .shortDescription(Component.text("List the question propositions").color(NamedTextColor.YELLOW))
                .executor(new SetQuestionPropositionsList())
                .build();

        final Command.Parameterized commandQTSetQuestionPropositions = Command.builder()
                .shortDescription(Component.text("Set the question propositions").color(NamedTextColor.YELLOW))
                .addParameters(Parameter.firstOf(Parameter.subcommand(commandQTSetQuestionPropositionsList, "list"), SetQuestionAnswers.ACTIONS))
                .executor(new SetQuestionPropositions())
                .build();

        final Command.Parameterized commandQTSetQuestion = Command.builder()
                .shortDescription(Component.text("Change a value of a question").color(NamedTextColor.YELLOW))
                .permission("questionstime.command.set")
                .addParameters(specificQuestionParameter, Parameter.firstOf(
                        Parameter.subcommand(commandQTSetQuestionAnswers, "answers"),
                        Parameter.subcommand(commandQTSetQuestionPropositions,"propositions"),
                        Parameter.subcommand(commandQTSetMalus, "malus"),
                        Parameter.subcommand(commandQTSetQuestionPrizes, "prizes"),
                        Parameter.subcommand(commandQTSetQuestionQuestion, "question"),
                        Parameter.subcommand(commandQTSetTimer, "timer"),
                        Parameter.subcommand(commandQTSetWeight, "weight"),
                        Parameter.subcommand(commandQTSetTimeBetweenAnswer, "time_between_answer")))
                .executor(context -> CommandResult.error(TextUtils.errorWithPrefix("Select a question")))
                .build();

        final Command.Parameterized commandQTSetConfig = Command.builder()
                .shortDescription(Component.text("Change a configuration value").color(NamedTextColor.YELLOW))
                .permission("questionstime.command.set")
                .addParameters(ChangeConfigurationExecutor.CONFIG, ChangeConfigurationExecutor.VALUE)
                .executor(new ChangeConfigurationExecutor(null))
                .build();

        final Command.Parameterized commandQTSet = Command.builder()
                .shortDescription(Component.text("Change a configuration or question value").color(NamedTextColor.YELLOW))
                .permission("questionstime.command.set")
                .addChild(commandQTSetQuestion, "question")
                .addChild(commandQTSetConfig, "config")
                .build();

        final Command.Parameterized commandQTBase = Command.builder()
                .shortDescription(Component.text("List of all subcommands").color(NamedTextColor.YELLOW))
                .permission("questionstime.command.base")
                .executor(new BaseCommandExecutor())
                .addChild(commandQTAskQuestion, "ask")
                .addChild(commandQTSet, "set")
                .build();

        //multiple childs: mis au meme niveau que les questions
        //parameter.subcommands: on peut en mettre qu'un
        event.register(this.plugin, commandQTBase, "questionstime", "qt");
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
        try {
            this.configurationUpgrade.upgrade(configLoader);
            final QuestionTimeConfiguration questionTimeConfiguration = this.pluginConfigurationLoader.load(configLoader);
            this.verifyConfigurationValues.verify(questionTimeConfiguration);
            this.pluginConfig = questionTimeConfiguration;
            this.logger.info("Configuration loaded with {} questions", String.valueOf(questionTimeConfiguration.getQuestions().size()));
        } catch (final ConfigurationUpgradeException e) {
            this.logger.error("A problem occurred when upgrading the config.conf file, default configuration will be used", e);
            this.pluginConfig = new QuestionTimeConfiguration();
        }
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
            if(readMessages.size() < Messages.registeredMessagesCount()) {
                this.messageConfigurationUpdater.updateConfig(readMessages, messagesConfig);
            }
        }
    }

}
