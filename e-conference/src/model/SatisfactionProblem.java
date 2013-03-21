package model;

import java.util.List;

public class SatisfactionProblem {
	private int satisfactionProblemID;
	private int decisionProjectID;
	private List<Criterion> criteria;
	public int getSatisfactionProblemID() {
		return satisfactionProblemID;
	}
	public void setSatisfactionProblemID(int satisfactionProblemID) {
		this.satisfactionProblemID = satisfactionProblemID;
	}
	public int getDecisionProjectID() {
		return decisionProjectID;
	}
	public void setDecisionProjectID(int decisionProjectID) {
		this.decisionProjectID = decisionProjectID;
	}
	public List<Criterion> getCriteria() {
		return criteria;
	}
	public void setCriteria(List<Criterion> criteria) {
		this.criteria = criteria;
	}
	

}
