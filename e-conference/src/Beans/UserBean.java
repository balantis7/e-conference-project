package Beans;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.*;
import javax.faces.component.*;
import javax.faces.event.ValueChangeEvent;
import javax.faces.validator.*;
import javax.faces.application.*;

import DAO.DAOFactory;
import DAO.DecisionProjectDAO;
import DAO.UserDAO;
import DAO.encrypt;


import model.*;

@ManagedBean
@SessionScoped
public class UserBean implements Serializable {
		
	private User user=new User();
	private List<DecisionProject> projectsPending=new ArrayList<DecisionProject>();
	private List<DecisionProject> projectsFinish=new ArrayList<DecisionProject>();
	private List<DecisionProject> projectsNew=new ArrayList<DecisionProject>();
	private List<DecisionProject> ownedProjects=new ArrayList<DecisionProject>();
	private List<DecisionMaker> selectedDms=new ArrayList<DecisionMaker>();
	private List<User> usersList=new ArrayList<User>();
	private String pass;
	private FacesMessage passError;
	private int loginError=0;
	private int tabIndex=0;
	private boolean retrievePass=false;
	private String email;
	private String message="";

	
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isRetrievePass() {
		return retrievePass;
	}

	public void setRetrievePass(boolean retrievePass) {
		this.retrievePass = retrievePass;
	}

	public List<DecisionMaker> getSelectedDms() {
		return selectedDms;
	}

	public void setSelectedDms(List<DecisionMaker> selectedDms) {
		this.selectedDms = selectedDms;
	}

	public List<DecisionProject> getOwnedProjects() {
		return ownedProjects;
	}

	public void setOwnedProjects(List<DecisionProject> ownedProjects) {
		this.ownedProjects = ownedProjects;
	}

	public List<User> getUsersList() {
		return usersList;
	}

	public void setUsersList(List<User> usersList) {
		this.usersList = usersList;
	}
	public int getTabIndex() {
		return tabIndex;
	}

	public void setTabIndex(int tabIndex) {
		this.tabIndex = tabIndex;
	}

	public List<DecisionProject> getProjectsFinish() {
		return projectsFinish;
	}

	public void setProjectsFinish(List<DecisionProject> projectsFinish) {
		this.projectsFinish = projectsFinish;
	}

	public List<DecisionProject> getProjectsNew() {
		return projectsNew;
	}

	public void setProjectsNew(List<DecisionProject> projectsNew) {
		this.projectsNew = projectsNew;
	}

	public List<DecisionProject> getProjectsPending() {
		return projectsPending;
	}

	public void setProjectsPending(List<DecisionProject> projects) {
		this.projectsPending = projects;
	}

	public int getLoginError() {
		return loginError;
	}

	public void setLoginError(int loginError) {
		this.loginError = loginError;
	}

//	public String getPassError() {
//		return passError;
//	}
//
//	public void setPrintError(String printError) {
//		this.passError = printError;
//	}
	public String getPass() {
		return pass;
	}

	public void setPass(String pass){
		if(pass.equals(this.user.getPassword()))
		{
			this.pass=pass;
		}
		else
		{
			Locale loc=FacesContext.getCurrentInstance().getViewRoot().getLocale();
			if(loc.getDisplayLanguage().equals("English"))
			{
				passError=new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", "Password and repeat password fields do not match.");
				FacesContext.getCurrentInstance().addMessage(null, passError);
			}
			else
			{
				passError=new FacesMessage(FacesMessage.SEVERITY_ERROR,"Σφάλμα", "Τα πεδία κωδικός και επανάληψη κωδικού πρέπει να είναι ίδια.");
				FacesContext.getCurrentInstance().addMessage(null, passError);
			}
		}
		
	}
	public String showSignUp()
	{
		Iterator<FacesMessage> msgIterator = FacesContext.getCurrentInstance().getMessages();
	    while(msgIterator.hasNext())
	    {
	        msgIterator.next();
	        msgIterator.remove();
	    }
		//List<FacesMessage> m=FacesContext.getCurrentInstance().getMessageList();
		//m.clear();
		return "signup";
	}
	public String validateUser() {			
		encrypt Encrypt=new encrypt();
		UserDAO d=DAOFactory.Factory.getUserDAO();
	    String password=Encrypt.Doencrypt(user.getPassword());
	    user=d.getUser(user.getUsername(), password);		
		if(!user.getUsername().equals(""))
		{
			loginError=0;
			user.setNotLoggedIn(false);
			getProjects();
			FacesContext facesContext = FacesContext.getCurrentInstance();
			@SuppressWarnings("deprecation") //read data from userbean
			ProjectBean projectBean=(ProjectBean) facesContext.getApplication().getVariableResolver().resolveVariable(facesContext, "projectBean");
			if(!projectsNew.isEmpty())
			{
				projectBean.setProject(projectsNew.get(0));
			}
			return "success";
		}
		else
		{
			Locale loc=FacesContext.getCurrentInstance().getViewRoot().getLocale();
			if(loc.getDisplayLanguage().equals("English"))
			{
				passError=new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", "Username or password are wrong.");
				FacesContext.getCurrentInstance().addMessage(null, passError);
			}
			else
			{
				passError=new FacesMessage(FacesMessage.SEVERITY_ERROR,"Σφάλμα", "Το όνομα χρήστη ή ο κωδικός είναι λανθασμένα.");
				FacesContext.getCurrentInstance().addMessage(null, passError);
			}
			return "loginError";
		}
	}
	
	public User getUser()
	{
		return user;
	}
	
	public void validateUsername(FacesContext context, UIComponent componentToValidate, Object value) throws ValidatorException, SQLException
	{
		UserDAO d=DAOFactory.Factory.getUserDAO();
		if(d.validateUsername((String)value))
		{
			Locale loc=FacesContext.getCurrentInstance().getViewRoot().getLocale();
			FacesMessage message;
			if(loc.getDisplayLanguage().equals("English"))
			{
				message=new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error","Username already exists. Try another.");
			}
			else
			{
				message=new FacesMessage(FacesMessage.SEVERITY_ERROR,"Σφάλμα","Το όνομα χρήστη υπάρχει ήδη. Δοκίμαστε ένα άλλο.");
			}
			throw new ValidatorException(message);
		}
	}
	
	public void validateEmail(FacesContext context, UIComponent componentToValidate, Object value) throws ValidatorException
	{
		UserDAO d=DAOFactory.Factory.getUserDAO();
		if(d.validateMail((String)value))
		{
			Locale loc=FacesContext.getCurrentInstance().getViewRoot().getLocale();
			FacesMessage message;
			if(loc.getDisplayLanguage().equals("English"))
			{
				message=new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error","Email already exists. Try another.");
			}
			else
			{
				message=new FacesMessage(FacesMessage.SEVERITY_ERROR,"Σφάλμα","Το email υπάρχει ήδη. Δοκιμάστε ένα άλλο.");
			}
			throw new ValidatorException(message);
		}
	}
	
	
	public String createUser() 
	{
		encrypt Encrypt=new encrypt();
		this.user.setPassword(Encrypt.Doencrypt(this.user.getPassword()));
		UserDAO d=DAOFactory.Factory.getUserDAO();
		d.createUser(user);
		loginError=0;
		user.setNotLoggedIn(false);
		getProjects();
		FacesContext facesContext = FacesContext.getCurrentInstance();
		@SuppressWarnings("deprecation") //read data from userbean
		ProjectBean projectBean=(ProjectBean) facesContext.getApplication().getVariableResolver().resolveVariable(facesContext, "projectBean");
		if(!projectsNew.isEmpty())
		{
			projectBean.setProject(projectsNew.get(0));
		}
		return "success";
	}
	
	public String logout()
	{
		this.user=new User();
		projectsFinish=new ArrayList<DecisionProject>();
		projectsPending=new ArrayList<DecisionProject>();
		projectsNew=new ArrayList<DecisionProject>();
		usersList=new ArrayList<User>();
		this.pass="";
		FacesContext facesContext = FacesContext.getCurrentInstance();
		@SuppressWarnings("deprecation") //read data from projectbean
		ProjectBean projectbean=(ProjectBean) facesContext.getApplication().getVariableResolver().resolveVariable(facesContext, "projectBean");
		projectbean.init();
		return "workspace";
		
	}
	
	public List<User> getAllUsers()
	{
		List<User> l=new ArrayList<User>();
		UserDAO d=DAOFactory.Factory.getUserDAO();
		l=d.getAllUsers();
		return l;
		
	}
	
	public String showProfile()
	{
		retrievePass=true;
		return "profile";
	}
	
	public void getProjects()
	{
		DecisionProjectDAO d=DAOFactory.getDAOFactory().getDecisionProjectDAO();
		List<Integer> projectsID;
		projectsID=d.getUserProjects(user.getUsername());
		DecisionProject p;
		for(int i=0;i<projectsID.size(); i++)
		{
			p=d.getProject(projectsID.get(i), user.getUsername());
			if(p.isDone())
			{
				projectsFinish.add(p);
			}
			else
			{
				projectsPending.add(p);
			}
			if(p.isUnread())
			{
				projectsNew.add(p);
			}
		}
	}
	
	public void editUsername()
	{
		UserDAO d=DAOFactory.Factory.getUserDAO();
		d.editUsername(user.getEmail(), user.getUsername());
	}
	//test name
	public void editName()
	{
		UserDAO d=DAOFactory.Factory.getUserDAO();
		d.editName(user.getEmail(), user.getName());
	}
	
	public void editSurname()
	{
		UserDAO d=DAOFactory.Factory.getUserDAO();
		d.editSurname(user.getEmail(), user.getSurname());
	}
	
	public void editAbout()
	{
		UserDAO d=DAOFactory.Factory.getUserDAO();
		d.editAbout(user.getEmail(), user.getAbout());
	}
	
	public String forgetPass()
	{
		retrievePass=true;
		return "login";
	}
	
	public void retrievePass()
	{
		Locale loc=FacesContext.getCurrentInstance().getViewRoot().getLocale();
		String un;
		UserDAO d=DAOFactory.Factory.getUserDAO();
		un=d.getUsername(email);
		if(un!=null)
		{
			Random generator = new Random();
	         int num,p=0;
	         for(int i=1; i<7; i++)
	         {
	        	 while((num=generator.nextInt(10))==0)
	             {

	             }
	             Math.pow(10,i);
	             p=p+(int)Math.pow(10,i)*i*num;
	          }
	          while((num=generator.nextInt(10))==0)
	          {

	          }
	          p=p+num;

	          String pass=String.valueOf(p);
	          //encryption
	          encrypt Encrypt=new encrypt();
	          String passw=Encrypt.Doencrypt(pass);
			d.changePassword(passw, un);
			Mailer m=new Mailer();
            try {
                m.sendMail(email,un,pass);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            retrievePass=false;
            if(loc.getDisplayLanguage().equals("English"))
            {
            	message="Your new code was sent to your email.";
            }
            else
            {
            	message="Ο νέος σας κωδικός εστάλει στο email σας";
            }
            
		}
		else
		{
			if(loc.getDisplayLanguage().equals("English"))
			{
				message="The email address you gave does not exist in our database.";
			}
			else
			{
				message="Η διεύθυνση email που δώσατε δεν υπάρχει στην βάση δεδομένων μας.";
			}		
		}	
	}
	
	public String viewProjects()
	 {
		 Map<String,String> params =FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		 String index = params.get("tabNumber");
		 if(index!=null)
		 {
			 tabIndex=Integer.valueOf(index);
		 }
		 else
		 {
			 tabIndex=0;
		 }
		 DecisionProjectDAO d=DAOFactory.Factory.getDecisionProjectDAO();
		 for(int i=0;i<projectsNew.size(); i++)
		 {
			 d.setProjectRead(user.getUsername(), projectsNew.get(i).getDecisionProjectID() );
		 }
		 return "myprojects";
	 }
	
	public String viewUserList()
	{
		UserDAO d=DAOFactory.Factory.getUserDAO();
		usersList=d.getAllUsers();
		return "viewUserList";
	}
	
	public void changeAdmin()//ValueChangeEvent vce
	{
		UserDAO d=DAOFactory.Factory.getUserDAO();
		Map<String,String> params =FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String username = params.get("username");
		Iterator<User> it=usersList.iterator();
		User u;
		boolean admin=false;
		while(it.hasNext())
		{
			u=it.next();
			if(u.getUsername().equals(username))
			{
				admin=u.getAdmin();
				break;
			}
		}
		d.changeAdmin(username, admin);
	}
	
	public String viewOwnedProjects()
	{
		DecisionProjectDAO d=DAOFactory.Factory.getDecisionProjectDAO();
		ownedProjects=d.getOwned(user.getUsername());
		int count=0;
		for(int i=0;i<ownedProjects.size();i++)
		{
			if(!ownedProjects.get(i).isUsersDone())
			{
				count=0;
				for(int j=0;j<ownedProjects.get(i).getDecisionMakers().size();j++)
				{
					if(ownedProjects.get(i).getDecisionMakers().get(j).isDone())
					{
						count++;
					}
				}
				if(count==ownedProjects.get(i).getDecisionMakers().size())
				{
					ownedProjects.get(i).setUsersDone(true);
					d.setUsersDone(ownedProjects.get(i).getDecisionProjectID());
				}
			}
		}
		return "viewOwnedProject";
	}
	
}
