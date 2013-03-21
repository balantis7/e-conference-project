package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Alternative;
import model.Criterion;
import model.DecisionMaker;
import model.DecisionProject;
import model.Scale;

public class mysqlDecisionProjectDAO implements DecisionProjectDAO{
	
	public DecisionProject getProject(int projectID, String username)
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
		DecisionProject p=new DecisionProject();
		Criterion c;
		Alternative a;
		try{
			ps = con.prepareStatement("select * from decisionProjects where decisionProjectsID="+String.valueOf(projectID));
			rs=ps.executeQuery();
			rs.next();
			p.setDecisionProjectID(rs.getInt(1));
			p.setTitle(rs.getString(2));
			p.setDescription(rs.getString(3));
			p.setCreator(rs.getString(4));
			p.setUsersDone(rs.getBoolean(5));
			ps = con.prepareStatement("select * from criteria where decisionProjectID="+String.valueOf(projectID));
			rs=ps.executeQuery();
			while(rs.next())
			{
				c=new Criterion();
				c.setCriteriaID(rs.getInt(1));
				c.setDecisionProjectID(rs.getInt(2));
				c.setName(rs.getString(3));
				c.setIntervals(rs.getInt(4));
				c.setBest(rs.getDouble(5));
				c.setWorst(rs.getDouble(6));
				c.setQuantitative(rs.getBoolean(7));
				p.getCriteria().add(c);
			}
			for(int i=0; i<p.getCriteria().size(); i++)
			{
				if(!p.getCriteria().get(i).isQuantitative())
				{
					ps=con.prepareCall("select scale,value from scales where criteriaID="+String.valueOf(p.getCriteria().get(i).getCriteriaID())+" order by scalesID");
					rs=ps.executeQuery();
					Scale s=new Scale();
					while(rs.next())
					{
						s.setString(rs.getString(1));
						s.setValue(rs.getInt(2));
						p.getCriteria().get(i).getScale().add(s);
						s=new Scale();
					}
				}
			}
			ps = con.prepareStatement("select * from alternatives where decisionProjectsID="+String.valueOf(projectID));
			rs=ps.executeQuery();
			while(rs.next())
			{
				a=new Alternative();
				a.setId(rs.getInt(1));
				a.setProjectId(rs.getInt(2));
				a.setName(rs.getString(3));
				p.getAlternatives().add(a);
			}
			ps = con.prepareStatement("select username,done,unread,weight,utaDone from decisionmakers where decisionProjectsID="+String.valueOf(projectID));
			rs=ps.executeQuery();
			DecisionMaker m;
			while(rs.next())
			{
				m=new DecisionMaker();
				m.setUsername(rs.getString(1));
				m.setDecisionProjectID(p.getDecisionProjectID());
				m.setWeight(rs.getInt(4));
				m.setDone(rs.getBoolean(2));
				m.setUtaDone(rs.getBoolean(5));
				p.getDecisionMakers().add(m);
				if(rs.getString(1).equals(username))
				{
					p.setDone(rs.getBoolean(2));
					p.setUnread(rs.getBoolean(3));
					p.setUtaDone(rs.getBoolean(5));
					
				}
			}
			
			ps = con.prepareStatement("select * from satisfactionCriteria where decisionprojectID="+String.valueOf(projectID));
			rs=ps.executeQuery();
			while(rs.next())
			{
				c=new Criterion();
				c.setCriteriaID(rs.getInt(1));
				c.setDecisionProjectID(rs.getInt(2));
				c.setName(rs.getString(3));
				c.setBest(rs.getDouble(4));
				c.setWorst(rs.getDouble(5));
				p.getSatCriteria().add(c);
			}
			
			for(int i=0; i<p.getSatCriteria().size(); i++)
			{
				
				ps=con.prepareCall("select scale,value from satisfactionscales where satCritID="+String.valueOf(p.getSatCriteria().get(i).getCriteriaID())+" order by id");
				rs=ps.executeQuery();
				Scale s=new Scale();
				while(rs.next())
				{
					s.setString(rs.getString(1));
					s.setValue(rs.getInt(2));
					p.getSatCriteria().get(i).getScale().add(s);
					s=new Scale();
				}	
			}
			con.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return p;
		
	}
	
	public List<Integer> getUserProjects(String username)
	{
		Connection con=mysqlDAOFactory.createConnection();	 
		if(con==null)
			try {
				throw new SQLException("Can't get database connection");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		List<Integer> pr=new ArrayList<Integer>();
		PreparedStatement ps;
		try {
			ps = con.prepareStatement("select decisionProjectsID from decisionmakers where username="+"'"+username+"'");
			ResultSet rs=ps.executeQuery();
			while(rs.next()){
				pr.add(rs.getInt(1));
			}
			con.close();
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pr;
		
	}
	
	public DecisionProject createProject(DecisionProject project)
	{
		int projectid=0;
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
			ps = con.prepareStatement("insert into decisionProjects(title,description,creator) values('"+project.getTitle()+"','"+project.getDescription()+"','"+project.getCreator()+"')");
			ps.executeUpdate();
			ps=con.prepareStatement("select decisionProjectsID from decisionprojects where title='"+project.getTitle()+"'");
			ResultSet rs=ps.executeQuery();
			rs.next();
			projectid=rs.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		project.setDecisionProjectID(projectid);
		List<Criterion> l=project.getCriteria();
		ResultSet rs;
		for(int i=0;i<l.size();i++)
		{
			try {
				ps=con.prepareStatement("insert into criteria(decisionProjectID,name,intervals,best,worst,quantitative)values("+String.valueOf(projectid)+",'"+l.get(i).getName()+"',"+String.valueOf(l.get(i).getIntervals())+","+String.valueOf(l.get(i).getBest())+","+String.valueOf(l.get(i).getWorst())+","+l.get(i).isQuantitative()+")");
				ps.executeUpdate();
				ps=con.prepareStatement("select criteriaID from criteria where name='"+l.get(i).getName()+"' and decisionProjectID="+String.valueOf(projectid));
				rs=ps.executeQuery();
				rs.next();
				l.get(i).setCriteriaID(rs.getInt(1));
				if(!l.get(i).isQuantitative())
				{
					for(int j=0;j<l.get(i).getScale().size(); j++)
					{
						ps=con.prepareStatement("insert into scales(criteriaID,scale,value)values("+String.valueOf(l.get(i).getCriteriaID())+",'"+l.get(i).getScale().get(j).getString()+"',"+l.get(i).getScale().get(j).getValue()+")");
						ps.executeUpdate();
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		project.setCriteria(l);
		List<Alternative>ls=project.getAlternatives();
		for(int i=0;i<ls.size();i++)
		{
			try {
				ps=con.prepareStatement("insert into alternatives(decisionProjectsID,name)values("+String.valueOf(projectid)+",'"+ls.get(i).getName()+"')");
				ps.executeUpdate();
				ps=con.prepareStatement("select alternativesID from alternatives where name='"+ls.get(i).getName()+"' and decisionProjectsID="+String.valueOf(projectid));
				rs=ps.executeQuery();
				rs.next();
				ls.get(i).setId(rs.getInt(1));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		project.setAlternatives(ls);
		for(int i=0;i<project.getDecisionMakers().size();i++)
		{
			try {
				ps=con.prepareStatement("insert into decisionmakers(decisionProjectsID,username,weight,utaDone)values("+String.valueOf(projectid)+",'"+project.getDecisionMakers().get(i).getUsername()+"',"+String.valueOf(project.getDecisionMakers().get(i).getWeight())+","+project.getDecisionMakers().get(i).isUtaDone()+")");
				ps.executeUpdate();
				project.getDecisionMakers().get(i).setDecisionProjectID(projectid);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return project;
		
	}
	
	public void setUsersDone(int projectID)
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
			ps=con.prepareStatement("update decisionprojects set usersDone=true where decisionprojectsid="+projectID);
			ps.executeUpdate();
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void storeSatCriteria(DecisionProject project)
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
			for(int i=0;i<project.getSatCriteria().size();i++)
			{
				ps=con.prepareStatement("insert into satisfactionCriteria(decisionprojectID,name,best,worst) values("+String.valueOf(project.getDecisionProjectID())+",'"+project.getSatCriteria().get(i).getName()+"',"+String.valueOf(project.getSatCriteria().get(i).getBest())+",0)");
				ps.executeUpdate();
				ps=con.prepareStatement("select id from satisfactionCriteria where name='"+project.getSatCriteria().get(i).getName()+"' and decisionProjectID="+String.valueOf(project.getDecisionProjectID()));
				rs=ps.executeQuery();
				rs.next();
				project.getSatCriteria().get(i).setCriteriaID(rs.getInt(1));
				for(int j=0;j<project.getSatCriteria().get(i).getScale().size();j++)
				{
					ps=con.prepareStatement("insert into satisfactionscales(satCritID,scale,value) values("+project.getSatCriteria().get(i).getCriteriaID()+",'"+project.getSatCriteria().get(i).getScale().get(j).getString()+"',"+project.getSatCriteria().get(i).getScale().get(j).getValue()+")");
					ps.executeUpdate();
				}
			}
			con.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void storeRate(String username, int critID, int projectID, int altID, double rate)
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
			ps=con.prepareStatement("insert into rates(username,critID,projectID,altID,rate) values('"+username+"',"+String.valueOf(critID)+","+String.valueOf(projectID)+","+String.valueOf(altID)+","+String.valueOf(rate)+")");
			ps.executeUpdate();
			ps=con.prepareStatement("update decisionmakers set done=true , unread=false where username='"+username+"' and decisionProjectsID="+String.valueOf(projectID));
			ps.executeUpdate();
			con.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void storeOrder(String username, int projectID, Object alternativeID, double rank)
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
			ps=con.prepareStatement("insert into orders(username,projectID,alternativeID,rank) values('"+username+"',"+String.valueOf(projectID)+","+alternativeID.toString()+","+String.valueOf(rank)+")");
			ps.executeUpdate();
			ps=con.prepareStatement("update decisionmakers set done=true , unread=false where username='"+username+"' and decisionProjectsID="+String.valueOf(projectID));
			ps.executeUpdate();
			con.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setUserDone(String username, int projectID)
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
			ps=con.prepareStatement("update decisionmakers set done=true where username='"+username+"' and decisionProjectsID="+String.valueOf(projectID));
			ps.executeUpdate();
			con.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setProjectRead(String username, int projectID)
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
			ps=con.prepareStatement("update decisionmakers set unread=false where username='"+username+"' and decisionProjectsID="+String.valueOf(projectID));
			ps.executeUpdate();
			con.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setUtaDone(String username, int projectID)
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
			ps=con.prepareStatement("update decisionmakers set utaDone=true where username='"+username+"' and decisionProjectsID="+String.valueOf(projectID));
			ps.executeUpdate();
			con.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<Double> getRates(String username, int projectID, int alternativeID)
	{
		Connection con=mysqlDAOFactory.createConnection();
		List<Double> d=new ArrayList<Double>();
		 
		if(con==null)
			try {
				throw new SQLException("Can't get database connection");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		PreparedStatement ps;
		try {
			ps=con.prepareStatement("select rate from rates where username='"+username+"' and projectID="+String.valueOf(projectID)+" and altID="+String.valueOf(alternativeID));
			ResultSet rs=ps.executeQuery();
			while(rs.next())
			{
				d.add(rs.getDouble(1));
			}
			con.close();
			return d;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return d;
		}
	}
	
	public double getRank(String username, int projectID, int alternativeID)
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
			ps=con.prepareStatement("select rank from orders where username='"+username+"' and projectID="+String.valueOf(projectID)+" and alternativeID="+String.valueOf(alternativeID));
			ResultSet rs=ps.executeQuery();
			rs.next();
			double d=rs.getDouble(1);
			con.close();
			return d;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}
	
	public List<DecisionProject> getOwned(String username)
	{
		List<Integer> pr=new ArrayList<Integer>();
		List<DecisionProject> out=new ArrayList<DecisionProject>();
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
			ps=con.prepareStatement("select decisionProjectsID from decisionprojects where creator='"+username+"'");
			ResultSet rs=ps.executeQuery();
			while(rs.next())
			{
				pr.add(rs.getInt(1));
			}
			for(int i=0;i<pr.size();i++)
			{
				out.add(getProject(pr.get(i),""));//username only help fill the argument project.done
			}
			con.close();
			return out;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public void storeMarginalValues(String username,int critID,double[] intervals,double[] values)
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
			for(int i=0;i<intervals.length;i++)
			{
				ps=con.prepareStatement("insert into marginalValues(username,criteriaID,interv,value) values('"+username+"',"+critID+","+intervals[i]+","+values[i]+")");
				ps.executeUpdate();
			}
			con.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List[] getMarginalValues(int criteriaID, String username)
	{
		List[] out={new ArrayList<Double>(),new ArrayList<Double>()};
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
			ps=con.prepareStatement("select interv,value from marginalvalues where username='"+username+"' and criteriaID="+criteriaID);
			ResultSet rs=ps.executeQuery();
			while(rs.next())
			{
				out[0].add(rs.getInt(1));
				out[1].add(rs.getDouble(2));
			}
			con.close();
			return out; //out[0] contains list of intervals and out[1] contains the values
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
	
	public void storeTotalUtilities(String username, int altID, double value)
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
			
			ps=con.prepareStatement("insert into totalUtilities(username,alternativeID,utility) values('"+username+"',"+altID+","+value+")");
			ps.executeUpdate();
			con.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public double getTotalUtilities(int alternativeID, String username)
	{
		double out=0;
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
			ps=con.prepareStatement("select utility from totalutilities where username='"+username+"' and alternativeID="+alternativeID);
			ResultSet rs=ps.executeQuery();
			rs.next();
			
			out=rs.getDouble(1);
			
			con.close();
			return out; //out[0] contains list of intervals and out[1] contains the values
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}

}
