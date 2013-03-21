package model;

import java.util.ArrayList;
import java.util.List;

public class DecisionProject{
	private String title;
	private String description;
	private List<Criterion> criteria;
	private List<Alternative> alternatives;
	private String creator;	//creator username
	private List<DecisionMaker> decisionMakers;	//decision makers usernames
	private int decisionProjectID;
	private boolean done=false;
	private boolean unread=true;
	private boolean utaDone=false;
	private boolean usersDone=false;
	private List<Criterion> satCriteria;
	
	
	

	public DecisionProject()
	{
		title="";
		description="";
		criteria=new ArrayList<Criterion>();
		alternatives=new ArrayList<Alternative>();
		satCriteria=new ArrayList<Criterion>();
		creator="";
		decisionMakers=new ArrayList<DecisionMaker>();
	}
	
	
	public boolean isUtaDone() {
		return utaDone;
	}


	public void setUtaDone(boolean utaDone) {
		this.utaDone = utaDone;
	}


	public boolean isUsersDone() {
		return usersDone;
	}


	public void setUsersDone(boolean readyToJoin) {
		this.usersDone = readyToJoin;
	}


	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public boolean isUnread() {
		return unread;
	}

	public void setUnread(boolean unread) {
		this.unread = unread;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<Criterion> getCriteria() {
		return criteria;
	}
	public void setCriteria(List<Criterion> criteria) {
		this.criteria = criteria;
	}
	public List<Alternative> getAlternatives() {
		return alternatives;
	}
	public void setAlternatives(List<Alternative> alternatives) {
		this.alternatives = alternatives;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public List<DecisionMaker> getDecisionMakers() {
		return decisionMakers;
	}
	public void setDecisionMakers(List<DecisionMaker> decisionMakers) {
		this.decisionMakers = decisionMakers;
	}
	public int getDecisionProjectID() {
		return decisionProjectID;
	}
	public void setDecisionProjectID(int decisionProjectID) {
		this.decisionProjectID = decisionProjectID;
	}


	public List<Criterion> getSatCriteria() {
		return satCriteria;
	}


	public void setSatCriteria(List<Criterion> satCriteria) {
		this.satCriteria = satCriteria;
	}


}
