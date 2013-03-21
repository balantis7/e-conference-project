package DAO;

import java.util.List;

import model.DecisionProject;

public interface DecisionProjectDAO {
	public DecisionProject getProject(int projectID, String username);
	public List<Integer> getUserProjects(String username);
	public DecisionProject createProject(DecisionProject project);
	public void storeSatCriteria(DecisionProject project);
	public void storeRate(String username, int critID, int projectID, int altID, double rate);
	public void storeOrder(String username, int projectID, Object alternativeID, double rank);
	public void setUserDone(String username, int projectID);
	public void setProjectRead(String username, int projectID);
	public void setUsersDone(int projectID);
	public void setUtaDone(String username, int projectID);
	public List<Double> getRates(String username, int projectID, int alternativeID);
	public double getRank(String username, int projectID, int alternativeID);
	public List<DecisionProject> getOwned(String username);
	public void storeMarginalValues(String username,int critID,double[] intervals,double[] values);
	public void storeTotalUtilities(String username, int altID, double value);
	public List[] getMarginalValues(int criteriaID, String username);
	public double getTotalUtilities(int alternativeID, String username);
}
