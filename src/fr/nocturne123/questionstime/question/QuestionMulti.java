package fr.nocturne123.questionstime.question;

import java.util.Optional;

import fr.nocturne123.questionstime.Malus;
import fr.nocturne123.questionstime.Prize;
import ninja.leaping.configurate.ConfigurationNode;

public class QuestionMulti extends Question {

	private String[] propositions;
	private byte answer;
	
	public QuestionMulti(String question, Optional<ConfigurationNode> prizeNode, String[] propositions, byte answer, 
			Optional<ConfigurationNode> malusNode) {
		super(question, prizeNode, String.valueOf(answer), malusNode);
		this.propositions = propositions;
		this.answer = answer;
	}
	
	public QuestionMulti(String question, Prize prize, String[] propositions, byte answer, Malus malus) {
		super(question, prize, String.valueOf(answer), malus);
		this.propositions = propositions;
		this.answer = answer;
	}
	
	public String[] getPropositions() {
		return propositions;
	}
	
	@Override
	public String getAnswer() {
		return String.valueOf(answer);
	}
	
	@Override
	public Types getType() {
		return Types.MULTI;
	}

}
