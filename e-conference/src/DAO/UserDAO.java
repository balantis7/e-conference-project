package DAO;

import java.util.List;

import model.User;

public interface UserDAO {
	public User getUser(String username, String password);
	public String getUsername(String email);
	public void createUser(User u);
	public boolean validateMail(String mail);
	public boolean validateUsername(String username);
	public List<User> getAllUsers();
	public void editUsername(String email, String username);
	public void editName(String email, String name);
	public void editSurname(String email, String surname);
	public void editAbout(String email, String about);
	public void changeAdmin(String username, boolean admin);
	public void changePassword(String newpass,String username);


}
