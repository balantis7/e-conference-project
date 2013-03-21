/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DAO;

/**
 *
 * @author balantis7
 */
public abstract class DAOFactory
{
	public static final DAOFactory Factory=new mysqlDAOFactory();
	public abstract UserDAO getUserDAO();
	public abstract DecisionProjectDAO getDecisionProjectDAO();
    

    public static DAOFactory getDAOFactory()
    {

          return new mysqlDAOFactory();

    }

}
