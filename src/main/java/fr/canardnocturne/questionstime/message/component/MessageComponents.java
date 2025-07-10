package fr.canardnocturne.questionstime.message.component;

public class MessageComponents {

    public static final ComponentPlayersName PLAYERS_NAME = new ComponentPlayersName("name");
    public static final ComponentDefault<String> QUESTION = new ComponentDefault<>("question");
    public static final ComponentDefault<Byte> POSITION = new ComponentDefault<>("position");
    public static final ComponentDefault<String> PROPOSITION = new ComponentDefault<>("proposition");
    public static final ComponentTimer TIMER = new ComponentTimer("timer");
    public static final ComponentDefault<Integer> MONEY = new ComponentDefault<>("money");
    public static final ComponentCurrency CURRENCY = new ComponentCurrency("currency");
    public static final ComponentDefault<Integer> QUANTITY = new ComponentDefault<>("quantity");
    public static final ComponentModID MOD_ID = new ComponentModID("modid");
    public static final ComponentItem ITEM = new ComponentItem("item");
    public static final ComponentDefault<String> ANSWER = new ComponentDefault<>("answer");
    public static final ComponentCommand COMMAND = new ComponentCommand("command");
    public static final ComponentWinnerPosition WINNER_POSITION = new ComponentWinnerPosition("position");

}
