package Beans;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.*;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.TabChangeEvent; 
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

import UTA.UtaSolve;


import DAO.DAOFactory;
import DAO.DecisionProjectDAO;
import DAO.UserDAO;

@ManagedBean
@SessionScoped

public class ProjectBean implements Serializable{
	private DecisionProject project;
	private Integer projectid;
	private List<Criterion> criteria;
	private List<Alternative> alternatives;
	private Criterion criterion;
	private Criterion selectedCrit;
	private Integer doBasic=1;
	private Integer doCrit=0;
	private Integer doAlt=0;
	private Integer doUsers=0;
	private Integer finish=0;
	private Alternative alternative;
	private Alternative selectedAlt;
	private UserDataModel userList;
	private User[] selectedUsers;
	private List<User> dm;
	private Double[][] ratings;
	private String scaleString;
	private List<String> scales;
	private Integer[] ranks;
	private UtaSolve solve;

	
	
	public void init()
	{
		project=new DecisionProject();
		projectid=0;
		criteria=new ArrayList<Criterion>();
		alternatives=new ArrayList<Alternative>();
		criterion=new Criterion();
		selectedCrit=new Criterion();
		doBasic=1;
		doCrit=0;
		doAlt=0;
		doUsers=0;
		finish=0;
		alternative=new Alternative();
		selectedAlt=new Alternative();
		userList=new UserDataModel();
		selectedUsers=new User[50];
		dm=new ArrayList<User>();
		scales=new ArrayList<String>();
		solve = new UtaSolve();
	}
	
	public Integer[] getRanks() {
		return ranks;
	}

	public void setRanks(Integer[] ranks) {
		this.ranks = ranks;
	}

	public List<String> getScales() {
		return scales;
	}

	public void setScales(List<String> scales) {
		this.scales = scales;
	}

	public String getScaleString() {
		return scaleString;
	}

	public void setScaleString(String scaleString) {
		this.scaleString = scaleString;
	}

	public Double[][] getRatings() {
		return ratings;
	}

	public void setRatings(Double[][] ratings) {
		this.ratings = ratings;
	}

	public void setProject(DecisionProject p)
	{
		project=p;
	}
	
	public Integer getFinish() {
		return finish;
	}

	public void setFinish(Integer finish) {
		this.finish = finish;
	}

	public List<User> getDm() {
		return dm;
	}

	public void setDm(List<User> dm) {
		this.dm = dm;
	}

	public Integer getDoUsers() {
		return doUsers;
	}

	public void setDoUsers(Integer finish) {
		this.doUsers = finish;
	}

	public User[] getSelectedUsers() {
		return selectedUsers;
	}

	public void setSelectedUsers(User[] selectedUsers) {
		this.selectedUsers = selectedUsers;
	}

	public UserDataModel getUserList() {
		return userList;
	}

	public void setUserList(UserDataModel userList) {
		this.userList = userList;
	}

	public Alternative getSelectedAlt() {
		return selectedAlt;
	}

	public void setSelectedAlt(Alternative selecedAlt) {
		this.selectedAlt = selecedAlt;
	}

	public Alternative getAlternative() {
		return alternative;
	}

	public void setAlternative(Alternative alternative) {
		this.alternative = alternative;
	}

	public List<Alternative> getAlternatives() {
		return alternatives;
	}

	public void setAlternatives(List<Alternative> alternatives) {
		this.alternatives = alternatives;
	}
	public String getDoBasic() {
		return String.valueOf(doBasic);
	}

	public void setDoBasic(String doneBasic) {
		this.doBasic =Integer.valueOf(doneBasic);
	}
	
	public String getDoCrit() {
		return String.valueOf(doCrit);
	}

	public void setDoCrit(String doneCrit) {
		this.doCrit =Integer.valueOf(doneCrit);
	}
	
	public String getDoAlt() {
		return String.valueOf(doAlt);
	}

	public void setDoAlt(String doneAlt) {
		this.doCrit =Integer.valueOf(doneAlt);
	}

	public Criterion getSelectedCrit() {
		return selectedCrit;
	}

	public void setSelectedCrit(Criterion selectedCrit) {
		this.selectedCrit = selectedCrit;
	}

	public ProjectBean()
	{
		project=new DecisionProject();
		projectid=0;
		criteria=new ArrayList<Criterion>();
		criterion=new Criterion();
		alternative=new Alternative();
		alternatives=new ArrayList<Alternative>();
		scales=new ArrayList<String>();
	}
	
	public List<Criterion> getCriteria() {
		return criteria;
	}

	public void setCriteria(List<Criterion> criteria) {
		this.criteria = criteria;
	}

	public Criterion getCriterion() {
		return criterion;
	}
	public void setCriterion(Criterion criterion) {
		this.criterion = criterion;
	}
	public DecisionProject getProject()
	{
		return project;
	}
	public void addCriterio()
	{
		criterion.setCriteriaID(criteria.size());
		criterion.setDecisionProjectID(projectid);
		if(!criterion.isQuantitative())
		{
			Scale s=new Scale();
			List<Scale> sl=new ArrayList<Scale>();
			for(int i=0;i<scales.size(); i++)
			{
				s.setString(scales.get(i));
				s.setValue(i);
				sl.add(s);
				s=new Scale();
			}
			criterion.setScale(sl);
			criterion.setIntervals(criterion.getScale().size());
			criterion.setBest((double) (criterion.getScale().size()-1));
			criterion.setWorst(0.0);
			scales= new ArrayList<String>();
		}
		criteria.add(criterion);
		criterion=new Criterion();
	}
	public void deleteCriterio()
	{
		criteria.remove(selectedCrit);
		selectedCrit=null;
	}
	public void addAlternative()
	{
		alternative.setId(alternatives.size());
		alternative.setProjectId(projectid);
		alternatives.add(alternative);
		alternative= new Alternative();
	}
	public void deleteAlternative()
	{
		alternatives.remove(selectedAlt);
		selectedAlt=null;
	}
	
	public void addScale()
	{
		scales.add(scaleString);
		scaleString="";
	}
	
	public void deleteScales()
	{
		if(criterion.isQuantitative())
		{
			scales=new ArrayList<String>();
		}
	}
	
	public void createProjectBasic()
	{
		FacesContext facesContext = FacesContext.getCurrentInstance();
		@SuppressWarnings("deprecation") //read data from userbean
		UserBean userbean=(UserBean) facesContext.getApplication().getVariableResolver().resolveVariable(facesContext, "userBean");
		project.setCreator(userbean.getUser().getUsername());
		UserDAO d=DAOFactory.Factory.getUserDAO();
		List<User> l=d.getAllUsers();
		userList=new UserDataModel(l);
		doBasic=0;
		doUsers=1;
	}
	
	public void criteriaDone()
	{
		project.setCriteria(criteria);
		doCrit=0;
		finish=1;
	}
	
	public void alternativesDone()
	{
		project.setAlternatives(alternatives);
		//prepare userList for next step
		doAlt=0;
		doCrit=1;
		
	}
	
	public void userListDone()
	{
		dm = Arrays.asList(selectedUsers);
		List<DecisionMaker> sel= new ArrayList<DecisionMaker>();
		DecisionMaker m;
		for(int i=0;i<dm.size();i++)
		{
			m=new DecisionMaker();
			m.setDecisionProjectID(project.getDecisionProjectID());
			m.setUsername(dm.get(i).getUsername());
			m.setWeight(dm.get(i).getCurProjectWeight());
			sel.add(m);
		}
		project.setDecisionMakers(sel);
		doUsers=0;
		doAlt=1;
	}
	
	public String finish()
	{
		DecisionProjectDAO d=DAOFactory.Factory.getDecisionProjectDAO();
		project=d.createProject(project);
		doBasic=1;
		doCrit=0;
		doAlt=0;
		doUsers=0;
		finish=0;
		criteria=new ArrayList<Criterion>();
		alternatives=new ArrayList<Alternative>();
		//selectedUsers=new User[5];
		dm=new ArrayList<User>();
		return "done";
	}
	
	public String finishAndSetSat()
	{
		DecisionProjectDAO d=DAOFactory.Factory.getDecisionProjectDAO();
		project=d.createProject(project);
		doBasic=1;
		doCrit=0;
		doAlt=0;
		doUsers=0;
		finish=0;
		criteria=new ArrayList<Criterion>();
		alternatives=new ArrayList<Alternative>();
		//selectedUsers=new User[5];
		dm=new ArrayList<User>();
		return "createSat";
	}
	public String setSatisfactionProblem()
	{
		doBasic=1;
		doCrit=0;
		doAlt=0;
		doUsers=0;
		finish=0;
		criteria=new ArrayList<Criterion>();
		alternatives=new ArrayList<Alternative>();
		//selectedUsers=new User[5];
		dm=new ArrayList<User>();
		return "createSat";
	}
	
	public String satCritDone()
	{
		project.setSatCriteria(criteria);
		DecisionProjectDAO d=DAOFactory.Factory.getDecisionProjectDAO();
		d.storeSatCriteria(project);
		doBasic=1;
		doCrit=0;
		doAlt=0;
		doUsers=0;
		finish=0;
		criteria=new ArrayList<Criterion>();
		return "workspace";
	}
	
	public String cancelProjectCreation()
	{
		doBasic=1;
		doCrit=0;
		doAlt=0;
		doUsers=0;
		finish=0;
		criteria=new ArrayList<Criterion>();
		alternatives=new ArrayList<Alternative>();
		//selectedUsers=new User[5];
		dm=new ArrayList<User>();
		project=new DecisionProject();
		return "workspace";
	}
	
	 public void onTabChange2(TabChangeEvent event) {  
		 project=(DecisionProject)event.getData();
	    } 
	 
	 public void onTabChange(TabChangeEvent event) {  
		 String title=event.getTab().getTitle();
		 if(title.equals("new")||title.equals("νέα"))
		 {
			 FacesContext facesContext = FacesContext.getCurrentInstance();
				@SuppressWarnings("deprecation") //read data from userbean
				UserBean userbean=(UserBean) facesContext.getApplication().getVariableResolver().resolveVariable(facesContext, "userBean");
				if(userbean.getProjectsNew().size()>0)
				{
					project=userbean.getProjectsNew().get(0);
				}
				else
				{
					project=new DecisionProject();
				}
		 }
		 else if(title.equals("pending")||title.equals("σε αναμονή"))
		 {
			 FacesContext facesContext = FacesContext.getCurrentInstance();
				@SuppressWarnings("deprecation") //read data from userbean
				UserBean userbean=(UserBean) facesContext.getApplication().getVariableResolver().resolveVariable(facesContext, "userBean");
				if(userbean.getProjectsPending().size()>0)
				{
					project=userbean.getProjectsPending().get(0);
				}
				else
				{
					project=new DecisionProject();
				}
		 }
		 else if(title.equals("finished")||title.equals("περατωμένα"))
		 {
			 FacesContext facesContext = FacesContext.getCurrentInstance();
				@SuppressWarnings("deprecation") //read data from userbean
				UserBean userbean=(UserBean) facesContext.getApplication().getVariableResolver().resolveVariable(facesContext, "userBean");
				if(userbean.getProjectsFinish().size()>0)
				{
					project=userbean.getProjectsFinish().get(0);
				}
				else
				{
					project=new DecisionProject();
				}
		 }
		 else
		 {
			 project=(DecisionProject)event.getData();
		 }
	    }
	 
	 public String viewRate()
	 {
		  Map<String,String> params =FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		  String pid = params.get("projectID");
		  DecisionProjectDAO d=DAOFactory.Factory.getDecisionProjectDAO();
		  FacesContext facesContext = FacesContext.getCurrentInstance();
		  @SuppressWarnings("deprecation") //read data from userbean
		  UserBean userbean=(UserBean) facesContext.getApplication().getVariableResolver().resolveVariable(facesContext, "userBean");
		  project=d.getProject(Integer.valueOf(pid), userbean.getUser().getUsername());
		  ratings=new Double[project.getAlternatives().size()][project.getCriteria().size()];
		  ranks=new Integer[project.getAlternatives().size()];
		  alternatives=project.getAlternatives();
		  userbean.getProjectsPending().addAll(userbean.getProjectsNew());
		  userbean.setProjectsNew(new ArrayList<DecisionProject>());
		  return "rate";
	 }
	 
	 public String storeRate()
	 {
		 FacesContext facesContext = FacesContext.getCurrentInstance();
		 @SuppressWarnings("deprecation") //read data from userbean
		 UserBean userbean=(UserBean) facesContext.getApplication().getVariableResolver().resolveVariable(facesContext, "userBean");
		 DecisionProjectDAO d=DAOFactory.Factory.getDecisionProjectDAO();
		 for(int i=0; i<project.getAlternatives().size(); i++)
		 {
			 for(int j=0; j<project.getCriteria().size(); j++)
			 {
				 d.storeRate(userbean.getUser().getUsername(), project.getCriteria().get(j).getCriteriaID(), project.getDecisionProjectID(), project.getAlternatives().get(i).getId(), ratings[i][j]);
			 }
			 d.storeOrder(userbean.getUser().getUsername(), project.getDecisionProjectID(), (Object)alternatives.get(i), i+1);
		 }
		 d.setUserDone(userbean.getUser().getUsername(), project.getDecisionProjectID());
		 userbean.getProjectsFinish().add(project);
		 userbean.getProjectsPending().remove(project);
		 for(int i=0; i<project.getAlternatives().size(); i++)
		 {
			 ranks[i]=(int)d.getRank(userbean.getUser().getUsername(), project.getDecisionProjectID(), project.getAlternatives().get(i).getId());
		 }
		
		 runUTA();
		
		 return "viewProjectRates";
	 }
	 
	 public String runUTA()
	 {
		 int[] intervals=new int[project.getCriteria().size()];
		 double[] best=new double[project.getCriteria().size()];
		 double[] worst=new double[project.getCriteria().size()];
		 for(int i=0;i<project.getCriteria().size();i++)
		 {
			 best[i]=project.getCriteria().get(i).getBest();
			 worst[i]=project.getCriteria().get(i).getWorst();
			 intervals[i]=project.getCriteria().get(i).getIntervals()-1;
		 }
		 solve= new UtaSolve();
		 solve.UtaSolveMethod(ratings, intervals, best, worst,ranks, 0.05);
		 double[] totalUtilities=solve.getSolution();
		 double[][] values =solve.getMarginalValues();
		 double[][] intervals_final=solve.getIntervals();
		 double[][] q=solve.getPartialUtilities();
		 DecisionProjectDAO d=DAOFactory.Factory.getDecisionProjectDAO();
		  FacesContext facesContext = FacesContext.getCurrentInstance();
		  @SuppressWarnings("deprecation") //read data from userbean
		  UserBean userbean=(UserBean) facesContext.getApplication().getVariableResolver().resolveVariable(facesContext, "userBean");
		 for(int i=0;i<values.length;i++)
		 {
			 d.storeMarginalValues(userbean.getUser().getUsername(), project.getCriteria().get(i).getCriteriaID(), intervals_final[i], values[i]);
		 }
		 d.setUtaDone(userbean.getUser().getUsername(), project.getDecisionProjectID());
		 project.setUtaDone(true);
		 for(int i=0;i<project.getAlternatives().size();i++)
			{
			 d.storeTotalUtilities(userbean.getUser().getUsername(), project.getAlternatives().get(i).getId(), totalUtilities[i]);
			}
		 viewUtaResults();

		 return "viewFunctions";
	 }
	 
	 public String showRates()
	 {
		 Map<String,String> params =FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		 String pid = params.get("projectID");
		 FacesContext facesContext = FacesContext.getCurrentInstance();
		 @SuppressWarnings("deprecation") //read data from userbean
		 UserBean userbean=(UserBean) facesContext.getApplication().getVariableResolver().resolveVariable(facesContext, "userBean");
		 DecisionProjectDAO d=DAOFactory.Factory.getDecisionProjectDAO();
		 project=d.getProject(Integer.valueOf(pid), userbean.getUser().getUsername());
		 List<Double> r;
		 ratings=new Double[project.getAlternatives().size()][project.getCriteria().size()];
		 ranks=new Integer[project.getAlternatives().size()];
		 for(int i=0; i<project.getAlternatives().size(); i++)
		 {
			 r=d.getRates(userbean.getUser().getUsername(), project.getDecisionProjectID(), project.getAlternatives().get(i).getId());
			 ranks[i]=(int)d.getRank(userbean.getUser().getUsername(), project.getDecisionProjectID(), project.getAlternatives().get(i).getId());
			 for(int j=0; j<project.getCriteria().size(); j++)
			 {
				 ratings[i][j]=r.get(j);
			 }
		 }
			 
		 return "viewProjectRates";
	 }
	 
	 public String viewUtaResults()
	 {
		 List[] l;
		 double[][] values;
		 double[][] intervals_final;
		 values=new double[project.getCriteria().size()][];
		 intervals_final=new double[project.getCriteria().size()][];
		 DecisionProjectDAO d=DAOFactory.Factory.getDecisionProjectDAO();
		 FacesContext facesContext = FacesContext.getCurrentInstance();
		 @SuppressWarnings("deprecation") //read data from userbean
		 UserBean userbean=(UserBean) facesContext.getApplication().getVariableResolver().resolveVariable(facesContext, "userBean");
		 double max;
		 double t1,t2;
		 for(int i=0;i<project.getCriteria().size();i++)
		 {
			 l=d.getMarginalValues(project.getCriteria().get(i).getCriteriaID() ,userbean.getUser().getUsername());
			 values[i]=new double[l[0].size()];
			 intervals_final[i]=new double[l[0].size()];
			 max=Collections.max(l[1]);
			 project.getCriteria().get(i).setWeight(roundThreeDecimals(max)*100);
			 for(int j=0;j<l[0].size();j++)
			 {
				 intervals_final[i][j]=(Integer)l[0].get(j);
				 t1=(Double)l[1].get(j);
				 t2=t1/max;
				 if(t1==0 && max==0)
					 values[i][j]=0;
				 else
					 values[i][j]=roundThreeDecimals(t2);
			 }
		 }
		 double[] totalUtilities=new double[project.getAlternatives().size()];
		 for(int i=0; i<project.getAlternatives().size(); i++)
		 {
			 totalUtilities[i]=roundThreeDecimals(d.getTotalUtilities(project.getAlternatives().get(i).getId(), userbean.getUser().getUsername()));
		 }
		 ChartSeries u;
		 
		 for(int i=0;i<values.length;i++)
		 {
			 u=new ChartSeries();
			 for(int j=0;j<values[i].length;j++)
			 {
				 if(values[i][j]>=0)
				 {
					 u.set(intervals_final[i][j],values[i][j]);
				 }
				 	
			 }
			 project.getCriteria().get(i).setGraph(new CartesianChartModel());
			 project.getCriteria().get(i).getGraph().addSeries(u);
		 }
		 for(int i=0;i<project.getAlternatives().size();i++)
			{
				project.getAlternatives().get(i).setUtility(roundThreeDecimals(totalUtilities[i]));
			}
		 return "viewFunctions";
	 }
	 
	 public String aggregatePreferences()
	 {
		 Map<String,String> params =FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		 String pid = params.get("projectID");
		 DecisionProjectDAO d=DAOFactory.Factory.getDecisionProjectDAO();
		 FacesContext facesContext = FacesContext.getCurrentInstance();
		 @SuppressWarnings("deprecation") //read data from userbean
		 UserBean userbean=(UserBean) facesContext.getApplication().getVariableResolver().resolveVariable(facesContext, "userBean");
		 project=d.getProject(Integer.valueOf(pid), userbean.getUser().getUsername());
		 
		 return "";
	 }
	 private double roundThreeDecimals(double d) {
         DecimalFormat twoDForm = new DecimalFormat("#.###");
     return Double.valueOf(twoDForm.format(d));
}
	 

}
