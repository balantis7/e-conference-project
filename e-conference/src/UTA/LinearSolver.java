package UTA;

import java.util.ArrayList;
import java.util.Collection;


import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.apache.commons.math.optimization.linear.LinearConstraint;
import org.apache.commons.math.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math.optimization.linear.Relationship;
import org.apache.commons.math.optimization.linear.SimplexSolver;



public class LinearSolver {
	

    public static final boolean MAX = true;
    public static final boolean MIN = false;
    public static final boolean GREATER = false;
    public static final boolean EQUAL = true;


 // Version Reporting function    
 /*   private static void APIVERSION(){
         StringBuffer szVersion = new StringBuffer(255);
         StringBuffer szBuild   = new StringBuffer(255);
         LSgetVersionInfo(szVersion, szBuild);
         System.out.println("\nLINDO API Version "+szVersion.toString() + " built on " + szBuild.toString());
         System.out.println();
    }*/
    
    

    
//**********************************************************************************************************    
    protected double[][] UTASolve(UtaLinearProblem problem, boolean minimize) {
// Solves the Linear Problem, problem contains all the arrays of the linear problem input ....
// Returns the results[] array with the weights Wij  
// if a=UTASolve(problem) then a[0][0] is the F* and a[1] is a vector with linear problem solution 
// minimize must set to true if we want to minimize the objective func, else objective func is maximized
//**********************************************************************************************************    	
    
 // Read the input arrays of the linear problem from the UTALinearProblem object   	
      boolean[] conF = problem.getConditionFunc();
      double d = problem.getD();
      double[] constrains = problem.getConstrains();
      
      String conTypes = new String();
      
      for (int i = 0; i < conF.length-1; i++) 
    	  conTypes += (conF[i] == EQUAL) ? "E" : "G";
      
		return solve(problem.getProblem(), constrains, conTypes, minimize);
    }

//**********************************************************************************************************    
protected static double[][] solve(double[][] problem, double[] constrains, String conTypes,boolean minimize) {
  	// Solves the Linear Problem, problem contains all the arrays of the linear problem input ....    
	//if a=solve(problem,constraints,conTypes) then a[0][0] is the F* and a[1] is a vector with linear problem solution 
//**********************************************************************************************************    	
	
	double[] objCoef=problem[problem.length-1]; //lipsi teleftaias grammis pou periexei tous sintelestes tis antikeimenikis
	
	LinearObjectiveFunction f = new LinearObjectiveFunction(objCoef, 0);
	Collection constraints = new ArrayList();
	for(int i=0; i<problem.length-2; i++) //eisagogi periorismon
	{
		if(conTypes.charAt(i)=='E') // E otan exoume periorismo isotitas
		{
			constraints.add(new LinearConstraint(problem[i], Relationship.EQ, constrains[i]));
		}
		else //allios exoume anisotita >=
		{
			constraints.add(new LinearConstraint(problem[i], Relationship.GEQ, constrains[i]));
		}
	}
	constraints.add(new LinearConstraint(problem[problem.length-2], Relationship.EQ, 1));//periorismos sum(wij)=1
	// create and run the solver
	RealPointValuePair solution=new RealPointValuePair(objCoef, 0);
	try {
		if(minimize) //epilogi metaksi megistopoiisis i elaxistopoiisis tis antikeimenikis
		{
			solution = new SimplexSolver().optimize(f, constraints, GoalType.MINIMIZE, true);
		}
		else
		{
			solution = new SimplexSolver().optimize(f, constraints, GoalType.MAXIMIZE , true);
		}
	} catch (OptimizationException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		//eksagogi apotelesmaton
      double[] result;
      result=solution.getPoint();
      double[] value=new double[]{solution.getValue()};
 
      return new double[][] {value,result};
    }
//*********************************************************************************************************************
private static int getNNZ(double[][] problem) {
// Ypologizei to a8roisma twn mh-mhdenikwn ari8mwn toy pinaka ...	
// Prosexte oti den koitazei tin teleytaia grammi poy einai i antikeimeniki synartisi	
//*********************************************************************************************************************	
      int nNZ = 0;
      for (int i = 0; i < problem.length - 1 ; i++)
        for (int j = 0; j < problem[i].length; j++)
          if (problem[i][j] != 0)
            nNZ++;
      return nNZ;
    }
//*********************************************************************************************************************
private static int transformaMatrix(double[][] problem, double[] value, int[]colStart, int[] rowIdx) {
//	Ypologizei ta value[], colStart[], rowIndx[]
// value[] > adA  - Einai oi nonzero coefficients	
// colStart[] > anBegCol - Einai ta indices of the first nonzero in each column
// rowIndx[] > 	anRowX  - Einai ta row indices of the nonzero coefficients
//*********************************************************************************************************************	
      int nNZ = 0;
      for (int i = 0; i < problem[0].length ; i++) {
        colStart[i] = nNZ;
        for (int j = 0; j < problem.length - 1; j++) {
          if (problem[j][i] == 0)
            continue;
            value[nNZ++] = problem[j][i];
            rowIdx[nNZ - 1] = j;
        }
      }
      colStart[problem[0].length] = nNZ;
      return nNZ;
    }

}
