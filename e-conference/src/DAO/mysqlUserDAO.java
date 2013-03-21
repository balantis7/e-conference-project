package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.User;

public class mysqlUserDAO implements UserDAO{
	public User getUser(String username, String password)
	{
		User user=new User();
		Connection con=mysqlDAOFactory.createConnection();
		 
		if(con==null)
			try {
				throw new SQLException("Can't get database connection");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		PreparedStatement ps;
		try {
			ps = con.prepareStatement("select * from users where username="+"'"+username+"' and password='"+password+"'");
			ResultSet rs=ps.executeQuery();
			user=new User();
			while(rs.next()){
				user.setuID(rs.getInt(1));
				user.setUsername(rs.getString(2));
				user.setPassword(rs.getString(3));
				user.setEmail(rs.getString(4));
				user.setName(rs.getString(5));
				user.setSurname(rs.getString(6));
				user.setAdmin(rs.getBoolean(7));
				user.setAbout(rs.getString(8));
			}
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;
	}
	
	public String getUsername(String email)
	{
		Connection con=mysqlDAOFactory.createConnection();
		if(con==null)
			try {
				throw new SQLException("Can't get database connection");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		PreparedStatement ps;
		ResultSet rs;
		try {
			ps = con.prepareStatement("select username from users where email="+"'"+email+"'");
			rs=ps.executeQuery();
			if(rs.next())
				return rs.getString(1);
			else
				return null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public void createUser(User u)
	{
		Connection con=mysqlDAOFactory.createConnection();
		if(con==null)
			try {
				throw new SQLException("Can't get database connection");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		PreparedStatement ps;
		try {
			ps = con.prepareStatement("insert into users (username,password,email,name,surname,about) values ("+"'"+u.getUsername()+"','"+u.getPassword()+"','"+u.getEmail()+"','"+u.getName()+"','"+u.getSurname()+"','"+u.getAbout()+"')");
			ps.executeUpdate();
			con.close(); 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public boolean validateMail(String mail)
	{
		Connection con=mysqlDAOFactory.createConnection();
		if(con==null)
			try {
				throw new SQLException("Can't get database connection");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		PreparedStatement ps;
		ResultSet rs;
		try {
			ps = con.prepareStatement("select email from users where email="+"'"+mail+"'");
			rs=ps.executeQuery();
			if(rs.next())
			{
				con.close();
				return true;
			}
			else
			{
				con.close();
				return false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return true;
		}
	}
	
	public boolean validateUsername(String username)
	{
		Connection con=mysqlDAOFactory.createConnection();
		if(con==null)
			try {
				throw new SQLException("Can't get database connection");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		PreparedStatement ps;
		ResultSet rs;
		try {
			ps = con.prepareStatement("select username from users where username="+"'"+username+"'");
			rs=ps.executeQuery();
			if(rs.next())
			{
				con.close();
				return true;
			}
			else
			{
				con.close();
				return false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return true;
		}
	}
	
	public List<User> getAllUsers()
	{
		List<User> l=new ArrayList<User>();
		Connection con=mysqlDAOFactory.createConnection();
		 
		if(con==null)
			try {
				throw new SQLException("Can't get database connection");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		PreparedStatement ps;
		try {
			ps = con.prepareStatement("select * from users ");
			ResultSet rs=ps.executeQuery();
			User u=new User();
			while(rs.next()){		
				u.setuID(rs.getInt(1));
				u.setUsername(rs.getString(2));
				u.setPassword(rs.getString(3));
				u.setEmail(rs.getString(4));
				u.setName(rs.getString(5));
				u.setSurname(rs.getString(6));
				u.setAdmin(rs.getBoolean(7));
				u.setAbout(rs.getString(8));
				l.add(u);
				u=new User();
			}
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return l;
	}
	
	public void editUsername(String email, String username)
	{
		Connection con=mysqlDAOFactory.createConnection();
		 
		if(con==null)
			try {
				throw new SQLException("Can't get database connection");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		PreparedStatement ps;
		try {
			ps = con.prepareStatement("update users set username='"+username+"' where email='"+email+"'");
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void editName(String email, String name)
	{
		Connection con=mysqlDAOFactory.createConnection();
		 
		if(con==null)
			try {
				throw new SQLException("Can't get database connection");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		PreparedStatement ps;
		try {
			ps = con.prepareStatement("update users set name='"+name+"' where email='"+email+"'");
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void editSurname(String email, String surname)
	{
		Connection con=mysqlDAOFactory.createConnection();
		 
		if(con==null)
			try {
				throw new SQLException("Can't get database connection");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		PreparedStatement ps;
		try {
			ps = con.prepareStatement("update users set surname='"+surname+"' where email='"+email+"'");
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void editAbout(String email, String about)
	{
		Connection con=mysqlDAOFactory.createConnection();
		 
		if(con==null)
			try {
				throw new SQLException("Can't get database connection");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		PreparedStatement ps;
		try {
			ps = con.prepareStatement("update users set about='"+about+"' where email='"+email+"'");
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void changeAdmin(String username,boolean admin)
	{
		Connection con=mysqlDAOFactory.createConnection();
		 
		if(con==null)
			try {
				throw new SQLException("Can't get database connection");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		PreparedStatement ps;
		try {
			ps = con.prepareStatement("update users set admin="+admin+" where username='"+username+"'");
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void changePassword(String newpass, String username)
	{
		Connection con=mysqlDAOFactory.createConnection();
		 
		if(con==null)
			try {
				throw new SQLException("Can't get database connection");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		PreparedStatement ps;
		try {
			ps = con.prepareStatement("update users set password='"+newpass+"' where username='"+username+"'");
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
