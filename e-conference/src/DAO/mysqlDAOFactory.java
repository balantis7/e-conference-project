/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DAO;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author balantis7
 */
public class mysqlDAOFactory extends DAOFactory {
    public static final String DRIVER= "com.mysql.jdbc.Driver";
	//public static final String DRIVER="org.gjt.mm.mysql.Driver";
    public static final String DBURL= "jdbc:mysql://localhost:3306/e-conferencedb";
    public static final String username="root";
    public static final String password="";

    public static Connection createConnection()
    {
        Connection conn=null;
        try {
            // Use DRIVER and DBURL to create a connection // Recommend connection pool implementation/usage
            Class.forName(DRIVER);
            try {

                conn = DriverManager.getConnection(DBURL, username,password);
            } catch (SQLException ex) {
                Logger.getLogger(mysqlDAOFactory.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(mysqlDAOFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conn;
    }
    
    public UserDAO getUserDAO()
    {
        return new mysqlUserDAO();
    }
    
    public DecisionProjectDAO getDecisionProjectDAO()
    {
        return new mysqlDecisionProjectDAO();
    }

}
