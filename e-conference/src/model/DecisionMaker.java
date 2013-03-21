package model;

public class DecisionMaker {
	private String username;
	private int decisionProjectID;
	private int weight;
	private boolean done;
	private boolean utaDone;
	
	public DecisionMaker()
	{
		weight=1;
	}
	
	public boolean isUtaDone() {
		return utaDone;
	}

	public void setUtaDone(boolean utaDone) {
		this.utaDone = utaDone;
	}



	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getDecisionProjectID() {
		return decisionProjectID;
	}
	public void setDecisionProjectID(int decisionProjectID) {
		this.decisionProjectID = decisionProjectID;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	
	

}
