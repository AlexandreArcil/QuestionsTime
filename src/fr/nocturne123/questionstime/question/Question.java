package fr.nocturne123.questionstime.question;

import java.util.Optional;

import fr.nocturne123.questionstime.Malus;
import fr.nocturne123.questionstime.Prize;
import ninja.leaping.configurate.ConfigurationNode;

public class Question {

	private String question;
	private String answer;
	private Prize prize;
	private Malus malus;
	
	public Question(String question, Optional<ConfigurationNode> prizeNode, String answer, Optional<ConfigurationNode> malusNode) {
		this.question = question;
		this.answer = answer;
		this.prize = new Prize(prizeNode);
		this.malus = new Malus(malusNode);
	}
	
	public Question(String question, Prize prize, String answer, Malus malus) {
		this.question = question;
		this.answer = answer;
		this.prize = prize;
		this.malus = malus;
	}
	
	public Prize getPrize() {
		return this.prize;
	}
	
	public static Types getType(ConfigurationNode questionnode) {
		if(questionnode.getNode("question").getValue() != null && questionnode.getNode("answer").getValue() != null) {
			if(questionnode.getNode("proposition1").getValue() != null)
				return Types.MULTI;
			return Types.SIMPLE;
		}
		return Types.ERROR;
	}

	public String getAnswer() {
		return answer;
	}
	
	public String getQuestion() {
		return this.question;
	}
	
	public Types getType() {
		return Types.SIMPLE;
	}
	
	public Malus getMalus() {
		return malus;
	}
	
	public enum Types {
		SIMPLE, MULTI, ERROR
	}

	
}
