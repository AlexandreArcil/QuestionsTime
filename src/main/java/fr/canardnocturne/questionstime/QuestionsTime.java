package fr.canardnocturne.questionstime;

import com.google.inject.Inject;
import fr.canardnocturne.questionstime.command.BaseCommandExecutor;
import fr.canardnocturne.questionstime.command.set.config.SetConfigCooldownExecutor;
import fr.canardnocturne.questionstime.command.set.config.SetConfigMaximumCooldownExecutor;
import fr.canardnocturne.questionstime.command.set.config.SetConfigMinimumConnectedExecutor;
import fr.canardnocturne.questionstime.command.set.config.SetConfigMinimumCooldownExecutor;
import fr.canardnocturne.questionstime.command.set.config.SetConfigModeExecutor;
import fr.canardnocturne.questionstime.command.set.config.SetConfigPersonalAnswerExecutor;
import fr.canardnocturne.questionstime.command.set.question.SetQuestionRevealAnswerExecutor;
import fr.canardnocturne.questionstime.command.set.question.tags.SetQuestionAddTagsExecutor;
import fr.canardnocturne.questionstime.command.set.question.tags.SetQuestionRemoveTagsExecutor;
import fr.canardnocturne.questionstime.command.set.question.tags.SetQuestionTagsListExecutor;
import fr.canardnocturne.questionstime.config.ConfigField;
import fr.canardnocturne.questionstime.config.ConfigMutable;
import fr.canardnocturne.questionstime.config.save.HoconPluginConfigurationSave;
import fr.canardnocturne.questionstime.config.save.PluginConfigurationSave;
import fr.canardnocturne.questionstime.question.ask.announcer.AskQuestionOnPlayerJoinEventHandler;
import fr.canardnocturne.questionstime.question.modifier.QuestionModifier;
import fr.canardnocturne.questionstime.question.modifier.QuestionModifierImpl;
import fr.canardnocturne.questionstime.command.set.question.answers.SetQuestionAddAnswersExecutor;
import fr.canardnocturne.questionstime.command.set.question.answers.SetQuestionAnswersListExecutor;
import fr.canardnocturne.questionstime.command.set.question.SetQuestionExecutor;
import fr.canardnocturne.questionstime.command.set.question.SetQuestionTimeBetweenAnswerExecutor;
import fr.canardnocturne.questionstime.command.set.question.SetQuestionTimerExecutor;
import fr.canardnocturne.questionstime.command.set.question.SetQuestionWeightExecutor;
import fr.canardnocturne.questionstime.command.set.question.answers.SetQuestionRemoveAnswersExecutor;
import fr.canardnocturne.questionstime.command.set.question.malus.SetQuestionMalusAnnounceExecutor;
import fr.canardnocturne.questionstime.command.set.question.malus.commands.SetQuestionMalusAddCommandsExecutor;
import fr.canardnocturne.questionstime.command.set.question.malus.commands.SetQuestionMalusCommandsListExecutor;
import fr.canardnocturne.questionstime.command.set.question.malus.SetQuestionMalusMoneyExecutor;
import fr.canardnocturne.questionstime.command.set.question.malus.commands.SetQuestionMalusRemoveCommandsExecutor;
import fr.canardnocturne.questionstime.command.set.question.prize.commands.SetQuestionPrizesAddCommandsExecutor;
import fr.canardnocturne.questionstime.command.set.question.prize.commands.SetQuestionPrizesCommandsListExecutor;
import fr.canardnocturne.questionstime.command.set.question.prize.commands.SetQuestionPrizesRemoveCommandsExecutor;
import fr.canardnocturne.questionstime.command.set.question.prize.items.SetQuestionPrizesAddItemsExecutor;
import fr.canardnocturne.questionstime.command.set.question.prize.items.SetQuestionPrizesItemsListExecutor;
import fr.canardnocturne.questionstime.command.set.question.prize.SetQuestionPrizesMoneyExecutor;
import fr.canardnocturne.questionstime.command.set.question.prize.SetQuestionPrizesMoneyListExecutor;
import fr.canardnocturne.questionstime.command.set.question.prize.items.SetQuestionPrizesRemoveItemsExecutor;
import fr.canardnocturne.questionstime.command.set.question.propositions.SetQuestionAddPropositionsExecutor;
import fr.canardnocturne.questionstime.command.set.question.propositions.SetQuestionPropositionsListExecutor;
import fr.canardnocturne.questionstime.command.set.question.propositions.SetQuestionRemovePropositionsExecutor;
import fr.canardnocturne.questionstime.config.QuestionTimeConfiguration;
import fr.canardnocturne.questionstime.config.loader.PluginConfigurationLoader;
import fr.canardnocturne.questionstime.config.loader.SafePluginConfigurationLoader;
import fr.canardnocturne.questionstime.config.serializer.ModeTypeSerializer;
import fr.canardnocturne.questionstime.config.upgrade.ConfigurationUpgrade;
import fr.canardnocturne.questionstime.config.upgrade.ConfigurationUpgradeException;
import fr.canardnocturne.questionstime.config.upgrade.ConfigurationUpgradeOrchestrator;
import fr.canardnocturne.questionstime.config.upgrade.update.FirstVersionConfigurationUpdate;
import fr.canardnocturne.questionstime.config.upgrade.update.NoVersionConfigurationUpdate;
import fr.canardnocturne.questionstime.config.upgrade.update.SecondVersionConfigurationUpdate;
import fr.canardnocturne.questionstime.config.verificator.VerifyConfigurationValuesImpl;
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
import java.util.stream.Collectors;

@Plugin("questionstime")
public class QuestionsTime {

    public static final Component PREFIX = Component.text("[", NamedTextColor.AQUA)
            .append(Component.text("QT", NamedTextColor.YELLOW))
            .append(Component.text("]", NamedTextColor.AQUA))
            .appendSpace();

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

    private QuestionLauncher questionLauncher;
    private QuestionPool questionPool;
    private PlayerAnswerQuestionEventHandler playerAnswerQuestionEventHandler;
    private CreatorLeftServerEventHandler creatorLeftServerEventHandler;
    private AskQuestionOnPlayerJoinEventHandler askQuestionOnPlayerJoinEventHandler;

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
        this.verifyConfigurationValues = new VerifyConfigurationValuesImpl();
    }

    @Listener
    public void onServerStarted(final StartedEngineEvent<Server> event) {
        Sponge.eventManager()
                .registerListeners(this.plugin, this.playerAnswerQuestionEventHandler, MethodHandles.lookup())
                .registerListeners(this.plugin, this.creatorLeftServerEventHandler, MethodHandles.lookup())
                .registerListeners(this.plugin, this.askQuestionOnPlayerJoinEventHandler, MethodHandles.lookup());
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
        final PluginConfigurationSave pluginConfigurationSave = new HoconPluginConfigurationSave(configLoader, logger);
        QuestionTimeConfiguration pluginConfig;
        try {
            if (Files.notExists(this.pluginFolder)) {
                Files.createDirectories(this.pluginFolder);
            }
            this.createConfigFile(configFile, configLoader);
            pluginConfig = this.loadConfig(configLoader);

            final Path messagesConfigPath = pluginFolder.resolve("message.conf");
            this.createMessagesFile(messagesConfigPath);
            this.loadMessages(messagesConfigPath);
        } catch (final IOException e) {
            this.logger.error("Unable to create the plugin folder", e);
            pluginConfig = new QuestionTimeConfiguration();
        }

        this.questionPool = new WeightSortedQuestionPool(pluginConfig.getQuestions());
        final QuestionCreationManager questionCreationManager = new QuestionCreationManager(new StoppableQuestionCreationOrchestrator.StoppableQuestionCreationOrchestratorFactory(),
                this.questionPool, questionRegister, this.logger);
        this.creatorLeftServerEventHandler = new CreatorLeftServerEventHandler(questionCreationManager);

        final ConfigMutable<Integer> cooldownConfig = new ConfigMutable<>(pluginConfig.getCooldown());
        final ConfigMutable<Integer> minimumCooldownConfig = new ConfigMutable<>(pluginConfig.getMinCooldown());
        final ConfigMutable<Integer> maximumCooldownConfig = new ConfigMutable<>(pluginConfig.getMaxCooldown());
        final ConfigMutable<Integer> minConnectedConfig = new ConfigMutable<>(pluginConfig.getMinConnected());
        final ConfigMutable<Boolean> personalAnswerConfig = new ConfigMutable<>(pluginConfig.isPersonalAnswer());
        final ConfigMutable<QuestionLauncher> questionLauncherConfig = new ConfigMutable<>(null);

        final QuestionPicker questionPicker = new WeightedRandomnessQuestionPicker(this.questionPool, this.logger);
        final QuestionAnnouncer questionAnnouncer = new SimpleQuestionAnnouncer(this.game, this.plugin);
        final QuestionAskManager questionAskManager = new QuestionAskManager(questionPicker, questionAnnouncer, questionCreationManager, this.game, this.plugin, this.logger, minConnectedConfig, questionLauncherConfig);
        final QuestionLauncherFactory questionLauncherFactory = new QuestionLauncherFactory(this.plugin, this.game, questionAskManager, cooldownConfig, minimumCooldownConfig, maximumCooldownConfig);
        this.playerAnswerQuestionEventHandler = new PlayerAnswerQuestionEventHandler(questionAskManager, personalAnswerConfig);
        this.askQuestionOnPlayerJoinEventHandler = new AskQuestionOnPlayerJoinEventHandler(questionAskManager, questionAnnouncer);
        try {
            this.questionLauncher = questionLauncherFactory.create(pluginConfig.getMode());
            questionLauncherConfig.setValue(this.questionLauncher);
        } catch (final IllegalStateException e) {
            this.logger.error(e.getMessage(), e);
        }

        final Command.Parameterized commandQTCreator = Command.builder()
                .shortDescription(Component.text("Create a question").color(NamedTextColor.YELLOW))
                .permission("questionstime.command.create")
                .executionRequirements(commandCause -> commandCause.root() instanceof ServerPlayer)
                .addParameter(CreateQuestionCommand.STEP_ARG)
                .executor(new CreateQuestionCommand(questionCreationManager))
                .build();
        event.register(this.plugin, commandQTCreator, "questionstimecreator", "qtc");

        //For the Optional#get: As specified in the doc, the function should take as argument a choice which is from the collection of questions, so I assume that it should always get a question from a player choice
        final Parameter.Value<Question> specificQuestionParameter = Parameter.choices(Question.class, s -> this.questionPool.get(s).get(), () -> this.questionPool.getAll().stream().map(Question::getQuestion).toList()).key("question").build();
        final Parameter.Value<String> tagsParameter = Parameter.choices(String.class,
                s -> s,
                () -> this.questionPool.getAll().stream().flatMap(question -> question.getTags().stream()).collect(Collectors.toSet()))
                .key("tags").optional().consumeAllRemaining().build();
        final Parameter questionParameter = Parameter.firstOf(ManualAskQuestionCommand.RANDOM_QUESTION_ARG, specificQuestionParameter);
        final Parameter questionParameter = Parameter.firstOf(Parameter.seq(ManualAskQuestionCommand.RANDOM_QUESTION_ARG, tagsParameter),specificQuestionParameter);
        final Command.Parameterized commandQTAskQuestion = Command.builder()
                .shortDescription(Component.text("Ask a question").color(NamedTextColor.YELLOW))
                .permission("questionstime.command.ask")
                .addParameter(questionParameter)
                .executor(new ManualAskQuestionCommand(questionAskManager, this.questionLauncher, specificQuestionParameter, tagsParameter, this.logger))
                .build();

        final QuestionModifier questionModifier = new QuestionModifierImpl();

        final Command.Parameterized commandQTSetQuestionMalusAnnounce = Command.builder()
                .shortDescription(Component.text("Set the malus announce for a question").color(NamedTextColor.YELLOW))
                .addParameters(SetQuestionMalusAnnounceExecutor.VALUE)
                .executor(new SetQuestionMalusAnnounceExecutor(specificQuestionParameter, questionModifier, questionPool, questionRegister))
                .build();

        final Command.Parameterized commandQTSetQuestionMalusCommandsList = Command.builder()
                .shortDescription(Component.text("List the malus commands for a question").color(NamedTextColor.YELLOW))
                .executor(new SetQuestionMalusCommandsListExecutor(specificQuestionParameter))
                .build();

        final Command.Parameterized commandQTSetQuestionMalusAddCommands = Command.builder()
                .shortDescription(Component.text("Add a malus command for a question").color(NamedTextColor.YELLOW))
                .addParameters(SetQuestionMalusAddCommandsExecutor.COMMAND)
                .executor(new SetQuestionMalusAddCommandsExecutor(specificQuestionParameter, questionModifier, questionPool, questionRegister))
                .build();

        final Command.Parameterized commandQTSetQuestionMalusRemoveCommands = Command.builder()
                .shortDescription(Component.text("Remove a malus command for a question").color(NamedTextColor.YELLOW))
                .addParameters(SetQuestionMalusRemoveCommandsExecutor.COMMAND)
                .executor(new SetQuestionMalusRemoveCommandsExecutor(specificQuestionParameter, questionModifier, questionPool, questionRegister))
                .build();

        final Command.Parameterized commandQTSetQuestionMalusCommands = Command.builder()
                .shortDescription(Component.text("Set the question malus commands").color(NamedTextColor.YELLOW))
                .addChild(commandQTSetQuestionMalusCommandsList, "list")
                .addChild(commandQTSetQuestionMalusAddCommands, "add")
                .addChild(commandQTSetQuestionMalusRemoveCommands, "remove")
                .build();

        final Command.Parameterized commandQTSetQuestionMalusMoney = Command.builder()
                .shortDescription(Component.text("Set the malus money for a question").color(NamedTextColor.YELLOW))
                .addParameters(SetQuestionMalusMoneyExecutor.MONEY)
                .executor(new SetQuestionMalusMoneyExecutor(specificQuestionParameter, questionModifier, questionPool, questionRegister))
                .build();

        final Command.Parameterized commandQTSetMalus = Command.builder()
                .shortDescription(Component.text("Set the malus for a question").color(NamedTextColor.YELLOW))
                .addChild(commandQTSetQuestionMalusAnnounce, "announce")
                .addChild(commandQTSetQuestionMalusCommands, "commands")
                .addChild(commandQTSetQuestionMalusMoney, "money")
                .build();

        final Command.Parameterized commandQTSetQuestionPrizesMoneySet = Command.builder()
                .shortDescription(Component.text("Set the prizes money for a question").color(NamedTextColor.YELLOW))
                .addParameters(SetQuestionPrizesMoneyExecutor.POSITION, SetQuestionPrizesMoneyExecutor.AMOUNT)
                .executor(new SetQuestionPrizesMoneyExecutor(specificQuestionParameter, questionModifier, questionPool, questionRegister))
                .build();

        final Command.Parameterized commandQTSetQuestionPrizesMoneyList = Command.builder()
                .shortDescription(Component.text("List the prizes money for a question").color(NamedTextColor.YELLOW))
                .executor(new SetQuestionPrizesMoneyListExecutor(specificQuestionParameter))
                .build();

        final Command.Parameterized commandQTSetQuestionPrizesMoney = Command.builder()
                .shortDescription(Component.text("Set or list the prizes money for a question").color(NamedTextColor.YELLOW))
                .addChild(commandQTSetQuestionPrizesMoneySet, "set")
                .addChild(commandQTSetQuestionPrizesMoneyList, "list")
                .build();

        final Command.Parameterized commandQTSetQuestionPrizesCommandsList = Command.builder()
                .shortDescription(Component.text("List the question commands prizes").color(NamedTextColor.YELLOW))
                .executor(new SetQuestionPrizesCommandsListExecutor(specificQuestionParameter))
                .build();

        final Command.Parameterized commandQTSetQuestionPrizesAddCommands = Command.builder()
                .shortDescription(Component.text("Add a command prize to the question").color(NamedTextColor.YELLOW))
                .addParameters(SetQuestionPrizesAddCommandsExecutor.POSITION, SetQuestionPrizesAddCommandsExecutor.COMMAND)
                .executor(new SetQuestionPrizesAddCommandsExecutor(specificQuestionParameter, questionModifier, questionPool, questionRegister))
                .build();

        final Command.Parameterized commandQTSetQuestionPrizesRemoveCommands = Command.builder()
                .shortDescription(Component.text("Remove a command prize from the question").color(NamedTextColor.YELLOW))
                .addParameters(SetQuestionPrizesRemoveCommandsExecutor.POSITION, SetQuestionPrizesRemoveCommandsExecutor.COMMAND)
                .executor(new SetQuestionPrizesRemoveCommandsExecutor(specificQuestionParameter, questionModifier, questionPool, questionRegister))
                .build();

        final Command.Parameterized commandQTSetQuestionPrizesCommands = Command.builder()
                .shortDescription(Component.text("Set the question prizes commands").color(NamedTextColor.YELLOW))
                .addChild(commandQTSetQuestionPrizesCommandsList, "list")
                .addChild(commandQTSetQuestionPrizesAddCommands, "add")
                .addChild(commandQTSetQuestionPrizesRemoveCommands, "remove")
                .build();

        final Command.Parameterized commandQTSetQuestionPrizesItemsList = Command.builder()
                .shortDescription(Component.text("List the question items prizes").color(NamedTextColor.YELLOW))
                .executor(new SetQuestionPrizesItemsListExecutor(specificQuestionParameter))
                .build();

        final Command.Parameterized commandQTSetQuestionPrizesAddItems = Command.builder()
                .shortDescription(Component.text("Add an item prize to the question").color(NamedTextColor.YELLOW))
                .addParameters(SetQuestionPrizesAddItemsExecutor.POSITION, SetQuestionPrizesAddItemsExecutor.ITEM)
                .executor(new SetQuestionPrizesAddItemsExecutor(specificQuestionParameter, questionModifier, questionPool, questionRegister))
                .build();

        final Command.Parameterized commandQTSetQuestionPrizesRemoveItems = Command.builder()
                .shortDescription(Component.text("Remove an item prize from the question").color(NamedTextColor.YELLOW))
                .addParameters(SetQuestionPrizesRemoveItemsExecutor.POSITION, SetQuestionPrizesRemoveItemsExecutor.ITEM)
                .executor(new SetQuestionPrizesRemoveItemsExecutor(specificQuestionParameter, questionModifier, questionPool, questionRegister))
                .build();

        final Command.Parameterized commandQTSetQuestionPrizesItems = Command.builder()
                .shortDescription(Component.text("Set the question prizes items").color(NamedTextColor.YELLOW))
                .addChild(commandQTSetQuestionPrizesItemsList, "list")
                .addChild(commandQTSetQuestionPrizesAddItems, "add")
                .addChild(commandQTSetQuestionPrizesRemoveItems, "remove")
                .build();

        final Command.Parameterized commandQTSetQuestionPrizes = Command.builder()
                .shortDescription(Component.text("Set the prizes for a question").color(NamedTextColor.YELLOW))
                .addChild(commandQTSetQuestionPrizesMoney, "money")
                .addChild(commandQTSetQuestionPrizesCommands, "commands")
                .addChild(commandQTSetQuestionPrizesItems, "items")
                .build();

        final Command.Parameterized commandQTSetWeight = Command.builder()
                .shortDescription(Component.text("Set the weight of a question").color(NamedTextColor.YELLOW))
                .addParameters(SetQuestionWeightExecutor.WEIGHT)
                .executor(new SetQuestionWeightExecutor(specificQuestionParameter, questionModifier, questionPool, questionRegister))
                .build();

        final Command.Parameterized commandQTSetTimer = Command.builder()
                .shortDescription(Component.text("Set the timer of a question").color(NamedTextColor.YELLOW))
                .addParameters(SetQuestionTimerExecutor.TIMER)
                .executor(new SetQuestionTimerExecutor(specificQuestionParameter, questionModifier, questionPool, questionRegister))
                .build();

        final Command.Parameterized commandQTSetTimeBetweenAnswer = Command.builder()
                .shortDescription(Component.text("Set the time between answers of a question").color(NamedTextColor.YELLOW))
                .addParameters(SetQuestionTimeBetweenAnswerExecutor.TIME_BETWEEN_ANSWER)
                .executor(new SetQuestionTimeBetweenAnswerExecutor(specificQuestionParameter, questionModifier, questionPool, questionRegister))
                .build();

        final Command.Parameterized commandQTSetQuestionQuestion = Command.builder()
                .shortDescription(Component.text("Set the question").color(NamedTextColor.YELLOW))
                .addParameters(SetQuestionExecutor.QUESTION)
                .executor(new SetQuestionExecutor(specificQuestionParameter, questionModifier, questionPool, questionRegister))
                .build();

        final Command.Parameterized commandQTSetQuestionAnswersList = Command.builder()
                .shortDescription(Component.text("List the question answers").color(NamedTextColor.YELLOW))
                .executor(new SetQuestionAnswersListExecutor(specificQuestionParameter))
                .build();

        final Command.Parameterized commandQTSetQuestionAddAnswers = Command.builder()
                .shortDescription(Component.text("Add an answer to the question").color(NamedTextColor.YELLOW))
                .addParameters(SetQuestionAddAnswersExecutor.ANSWER)
                .executor(new SetQuestionAddAnswersExecutor(specificQuestionParameter, questionModifier, questionPool, questionRegister))
                .build();

        final Command.Parameterized commandQTSetQuestionRemoveAnswers = Command.builder()
                .shortDescription(Component.text("Remove an answer from the question").color(NamedTextColor.YELLOW))
                .addParameters(SetQuestionRemoveAnswersExecutor.ANSWER)
                .executor(new SetQuestionRemoveAnswersExecutor(specificQuestionParameter, questionModifier, questionPool, questionRegister))
                .build();

        final Command.Parameterized commandQTSetQuestionAnswers = Command.builder()
                .shortDescription(Component.text("Set the question answers").color(NamedTextColor.YELLOW))
                .addChild(commandQTSetQuestionAnswersList, "list")
                .addChild(commandQTSetQuestionAddAnswers, "add")
                .addChild(commandQTSetQuestionRemoveAnswers, "remove")
                .build();

        final Command.Parameterized commandQTSetQuestionPropositionsList = Command.builder()
                .shortDescription(Component.text("List the question propositions").color(NamedTextColor.YELLOW))
                .executor(new SetQuestionPropositionsListExecutor(specificQuestionParameter))
                .build();

        final Command.Parameterized commandQTSetQuestionAddPropositions = Command.builder()
                .shortDescription(Component.text("Add a proposition to the question").color(NamedTextColor.YELLOW))
                .addParameters(SetQuestionAddPropositionsExecutor.PROPOSITION)
                .executor(new SetQuestionAddPropositionsExecutor(specificQuestionParameter, questionModifier, questionPool, questionRegister))
                .build();

        final Command.Parameterized commandQTSetQuestionRemovePropositions = Command.builder()
                .shortDescription(Component.text("Remove a proposition from the question").color(NamedTextColor.YELLOW))
                .addParameters(SetQuestionRemovePropositionsExecutor.PROPOSITION)
                .executor(new SetQuestionRemovePropositionsExecutor(specificQuestionParameter, questionModifier, questionPool, questionRegister))
                .build();

        final Command.Parameterized commandQTSetQuestionPropositions = Command.builder()
                .shortDescription(Component.text("Set the question propositions").color(NamedTextColor.YELLOW))
                .addChild(commandQTSetQuestionPropositionsList, "list")
                .addChild(commandQTSetQuestionAddPropositions, "add")
                .addChild(commandQTSetQuestionRemovePropositions, "remove")
                .build();

        final Command.Parameterized commandQTSetQuestionRevealAnswer = Command.builder()
                .shortDescription(Component.text("Set if the answer should be revealed at the end of the question").color(NamedTextColor.YELLOW))
                .addParameters(SetQuestionRevealAnswerExecutor.REVEAL_ANSWER)
                .executor(new SetQuestionRevealAnswerExecutor(specificQuestionParameter, questionModifier, questionPool, questionRegister))
                .build();

        final Command.Parameterized commandQTSetQuestionTagsList = Command.builder()
                .shortDescription(Component.text("List the question tags").color(NamedTextColor.YELLOW))
                .executor(new SetQuestionTagsListExecutor(specificQuestionParameter))
                .build();

        final Command.Parameterized commandQTSetQuestionAddTags = Command.builder()
                .shortDescription(Component.text("Add tags to the question").color(NamedTextColor.YELLOW))
                .addParameters(SetQuestionAddTagsExecutor.TAGS)
                .executor(new SetQuestionAddTagsExecutor(specificQuestionParameter, questionModifier, questionPool, questionRegister))
                .build();

        final Command.Parameterized commandQTSetQuestionRemoveTags = Command.builder()
                .shortDescription(Component.text("Remove tags from the question").color(NamedTextColor.YELLOW))
                .addParameters(SetQuestionRemoveTagsExecutor.TAGS)
                .executor(new SetQuestionRemoveTagsExecutor(specificQuestionParameter, questionModifier, questionPool, questionRegister))
                .build();

        final Command.Parameterized commandQTSetQuestionTags = Command.builder()
                .shortDescription(Component.text("Set the question tags").color(NamedTextColor.YELLOW))
                .addChild(commandQTSetQuestionTagsList, "list")
                .addChild(commandQTSetQuestionAddTags, "add")
                .addChild(commandQTSetQuestionRemoveTags, "remove")
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
                        Parameter.subcommand(commandQTSetTimeBetweenAnswer, "time_between_answer"),
                        Parameter.subcommand(commandQTSetQuestionRevealAnswer, "reveal_answer"),
                        Parameter.subcommand(commandQTSetQuestionTags, "tags")))
                .executor(context -> CommandResult.error(TextUtils.errorWithPrefix("Select a question")))
                .build();

        final Command.Parameterized commandQTSetConfigMaximumCooldown = Command.builder()
                .shortDescription(Component.text("Set the maximum cooldown").color(NamedTextColor.YELLOW))
                .addParameters(SetConfigMaximumCooldownExecutor.MAXIMUM_COOLDOWN)
                .executor(new SetConfigMaximumCooldownExecutor(verifyConfigurationValues, pluginConfig, pluginConfigurationSave, maximumCooldownConfig, logger))
                .build();

        final Command.Parameterized commandQTSetConfigMinimumCooldown = Command.builder()
                .shortDescription(Component.text("Set the minimum cooldown").color(NamedTextColor.YELLOW))
                .addParameters(SetConfigMinimumCooldownExecutor.MINIMUM_COOLDOWN)
                .executor(new SetConfigMinimumCooldownExecutor(verifyConfigurationValues, pluginConfig, pluginConfigurationSave, minimumCooldownConfig, logger))
                .build();

        final Command.Parameterized commandQTSetConfigCooldown = Command.builder()
                .shortDescription(Component.text("Set the cooldown").color(NamedTextColor.YELLOW))
                .addParameters(SetConfigCooldownExecutor.COOLDOWN)
                .executor(new SetConfigCooldownExecutor(verifyConfigurationValues, pluginConfig, pluginConfigurationSave, cooldownConfig, logger))
                .build();

        final Command.Parameterized commandQTSetPersonalAnswer = Command.builder()
                .shortDescription(Component.text("Set the personal answer").color(NamedTextColor.YELLOW))
                .addParameters(SetConfigPersonalAnswerExecutor.PERSONAL_ANSWER)
                .executor(new SetConfigPersonalAnswerExecutor(pluginConfig, pluginConfigurationSave, personalAnswerConfig, logger))
                .build();

        final Command.Parameterized commandQTSetMinimumConnected = Command.builder()
                .shortDescription(Component.text("Set the minimum connected players").color(NamedTextColor.YELLOW))
                .addParameters(SetConfigMinimumConnectedExecutor.MINIMUM_CONNECTED)
                .executor(new SetConfigMinimumConnectedExecutor(verifyConfigurationValues, pluginConfig, pluginConfigurationSave, minConnectedConfig, logger))
                .build();

        final Command.Parameterized commandQTSetMode = Command.builder()
                .shortDescription(Component.text("Set the mode").color(NamedTextColor.YELLOW))
                .addParameters(SetConfigModeExecutor.MODES)
                .executor(new SetConfigModeExecutor(pluginConfig, pluginConfigurationSave, questionLauncherFactory, questionLauncherConfig, logger))
                .build();

        final Command.Parameterized commandQTSetConfig = Command.builder()
                .shortDescription(Component.text("Set a configuration value").color(NamedTextColor.YELLOW))
                .permission("questionstime.command.set")
                .addChild(commandQTSetConfigMinimumCooldown, "minimum_cooldown")
                .addChild(commandQTSetConfigMaximumCooldown, "maximum_cooldown")
                .addChild(commandQTSetConfigCooldown, "cooldown")
                .addChild(commandQTSetPersonalAnswer, "personal_answer")
                .addChild(commandQTSetMinimumConnected, "minimum_connected")
                .addChild(commandQTSetMode, "mode")
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

    private QuestionTimeConfiguration loadConfig(final ConfigurationLoader<CommentedConfigurationNode> configLoader) {
        this.logger.info("Loading configurations from config.conf...");
        try {
            this.configurationUpgrade.upgrade(configLoader);
            final QuestionTimeConfiguration questionTimeConfiguration = pluginConfigurationLoader.load(configLoader);
            final VerifyConfigurationValues.Result verificationResult = this.verifyConfigurationValues.verify(questionTimeConfiguration);
            if(!verificationResult.isSuccess()) {
                this.setConfigDefaultValue(verificationResult, questionTimeConfiguration);
            }
            this.logger.info("Configuration loaded with {} questions", String.valueOf(questionTimeConfiguration.getQuestions().size()));
            return questionTimeConfiguration;
        } catch (final ConfigurationUpgradeException e) {
            this.logger.error("A problem occurred when upgrading the config.conf file, default configuration will be used", e);
            return new QuestionTimeConfiguration();
        }
    }

    private void setConfigDefaultValue(final VerifyConfigurationValues.Result verificationResult,
                                       final QuestionTimeConfiguration questionTimeConfiguration) {
        for (final Map.Entry<ConfigField, List<String>> entry : verificationResult.getWrongValues().entrySet()) {
            final ConfigField configField = entry.getKey();
            for (final String errorReason : entry.getValue()) {
                switch (configField) {
                    case MINIMUM_CONNECTED -> {
                        questionTimeConfiguration.setMinConnected(QuestionTimeConfiguration.DefaultValues.MIN_CONNECTED);
                        this.logger.warn("The config '{}' value is {}. The default value {} will be used instead", ConfigField.MIN_COOLDOWN.getName(), errorReason, QuestionTimeConfiguration.DefaultValues.MIN_CONNECTED);
                    }
                    case MIN_COOLDOWN -> {
                        questionTimeConfiguration.setMinCooldown(QuestionTimeConfiguration.DefaultValues.MIN_COOLDOWN);
                        this.logger.warn("The config '{}' value is {}. The default value {} will be used instead", ConfigField.MIN_COOLDOWN.getName(), errorReason, QuestionTimeConfiguration.DefaultValues.MIN_COOLDOWN);
                    }
                    case MAX_COOLDOWN -> {
                        questionTimeConfiguration.setMaxCooldown(QuestionTimeConfiguration.DefaultValues.MAX_COOLDOWN);
                        this.logger.warn("The config '{}' value is {}. The default value {} will be used instead", ConfigField.MAX_COOLDOWN.getName(), errorReason, QuestionTimeConfiguration.DefaultValues.MAX_COOLDOWN);
                    }
                    case VERSION -> {
                        questionTimeConfiguration.setVersion(QuestionTimeConfiguration.DefaultValues.VERSION);
                        this.logger.warn("The config '{}' value is {}. The default value {} will be used instead", ConfigField.VERSION.getName(), errorReason, QuestionTimeConfiguration.DefaultValues.VERSION);
                    }
                }
            }
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
