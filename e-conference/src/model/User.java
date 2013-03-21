package model;

public class User {
	private String username;
	private String password;
	private int uID;
	private String email;
	private boolean Admin;
	private String name;
	private String surname;
	private String about;
	private boolean notLoggedIn=true;
	private int curProjectWeight;
	
	public User() {
		username="";
		password="";
		uID=-1;
		email="";
		Admin=false;
		name="";
		surname="";
		about=""; 
		notLoggedIn=true;
		curProjectWeight=1;
	}
	public User(String u, String p, String e, int a, String n, String s, String ab)
	{
		username=u;
		password=p;
		email=e;
		Admin=false;
		name=n;
		surname=s;
		about=ab;
		notLoggedIn=true;
		curProjectWeight=1;
	}
	
	
	public int getCurProjectWeight() {
		return curProjectWeight;
	}
	public void setCurProjectWeight(int curProjectWeight) {
		this.curProjectWeight = curProjectWeight;
	}
	public boolean isNotLoggedIn() {
		return notLoggedIn;
	}
	public void setNotLoggedIn(boolean logggedIn) {
		this.notLoggedIn = logggedIn;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getuID() {
		return uID;
	}
	public void setuID(int uID) {
		this.uID = uID;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getAbout() {
		return about;
	}
	public void setAbout(String about) {
		this.about = about;
	}
	public boolean getAdmin() {
		return Admin;
	}
	public void setAdmin(boolean admin) {
		Admin = admin;
	}
	

}
