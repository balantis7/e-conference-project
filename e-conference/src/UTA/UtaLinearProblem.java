package UTA;

public class UtaLinearProblem {
	private double[][] problem;         // Einai o pinakas toy grammikoy provlimatos (Table2 ektos twn 2 teleytaiwn columns - UTASTAR paper, page 14)
	  private boolean[] conditionFunc;    // Einai o pinakas me tis sxeseis twn periorismwn toy grammikoy provlimatos (pro-teleytaia column sto Table2 - UTASTAR paper, page 14)
	  private double[] constrains;        // Einai o pinakas me tis times twn periorismwn (teleytaia column sto Table2 - UTASTAR paper, page 14)
	  private int variables;			  // O ari8mos twn metavlitwn toy grammikoy provlimatos
	  private double d = .05; 
	  private static final double dConst = .05;
	  
	  
	//***********************************************************************************************************************
	 protected UtaLinearProblem(final double[][] utaProblem, final Integer[] ranks, final double d) {
	 // Arxikopoisi toy pinaka toy grammikoy provlimatos: problem, conditionFunc, constraints
	// utaProblem einai o pinakas poy ekfrazei tis enallaktikes ws grammiko syndyasmo twn weights, (des panw meros tis page 14, apo paper UTASTAR)	 
	 //  linearProblem[ari8mos enallaktikwn + 1][sum_intervals + 2*(rows-1) gia ton ari8mo twn sfalmatwn]
	//***********************************************************************************************************************

	// 1. Ypologismos twn diastaswvn toy pinaka me tis metavlites (weights, errors+, errors-)	 
		 int rows = utaProblem.length + 1; 
	     int columns = utaProblem[0].length + 2*(rows-1);
	     double[][] linearProblem = new double[rows][columns];

	// 2. Orizei ton ari8mo twn metavlitwn toy grammikoy provlimatos ...    
	     variables = utaProblem[0].length;

	//3. Friaxnoyme ton pinaka linearproblem[][], (Table2 ektos twn 2 teleytaiwn columns - UTASTAR paper, page 14)
	    for (int i = 0; i < rows; i++) {
	    //a. Ftiaxnoyme tis variable parameters (???? pkonto giati stin teleytaia grammi vazei 0 anti gia 1) (des Table 1.2, UTASTAR paper, Page 14)
	      for (int j = 0; j < variables; j++) {
	        linearProblem[i][j] = (i < rows-2) ?
	            utaProblem[i][j] - utaProblem[i+1][j] : (i==rows-2) ? 1 : 0;
	      }

	  // b. Ftiaxnoyme kai tis error parameters (??? pkonto giati vazei anapoda ta sfalmata yperektimisis ka ypoektimisis?)
	  // 1 -1 -1 1 anti gia -1 1 1 -1 (mallon den peirazei)    
	        int startIdx = variables + i * 2;
	        for (int j = variables; j < columns; j++) {
	          int idx = (i < rows-2) ? j - startIdx : (i==rows-2) ? 4 : 0;
	          linearProblem[i][j] = (idx == 0 || idx == 3) ? 1 : (idx==1 || idx==2) ? -1 : 0;
	        }
	    }
	   // c. Enimerwsi tis class parameter me to linear problem problem = linearProblem  
	    setProblem(linearProblem);

	//4. conditionFunc calculation
	// a. Edw friaxnoyme tin pro-teleytaia stili toy grammikoy provlimatos me ta GREATER or EQUAL (Table 1.2, UTASTAR paper, Page 14)    
	    
	    boolean[] conF = new boolean[rows];
	    for (int i = 0; i < rows-2; i++) {
	      conF[i] = (ranks[i] - ranks[i+1] == 0) ? LinearSolver.EQUAL : LinearSolver.GREATER;
	    }
	    
	// b. Oi dyo teleytaies periptwseis einai EQUAL kai MIN gia ta Wi    
	    conF[rows-2] = LinearSolver.EQUAL;
	    conF[rows-1] = LinearSolver.MIN;

	// c. Enimerwsi tis class parameter
	    setConditionFunc(conF);

	//5. constraints
	// Ftiaxnoyme kai tin teleytaia stili toy grammikoy provlimatos me tis times twn periorismwn   
	    setD(d);
	    constrains = new double[rows-1];
	    for (int i = 0; i < rows-2; i++) {
	        constrains[i] = (conF[i] == LinearSolver.EQUAL) ? 0 : d;
	    }
	   constrains[rows-2] = 1;

	 }

	//*********************************************************************************************************************** 
	  protected void reformProblem(boolean[] mask) {
	// Ftiaxnei to grammiko provlima tis metaveltistopoiisis.....	  
	//***********************************************************************************************************************	  
	    int row = problem.length-1;

	    if (problem[0].length > variables) {
	      double[][] newProblem = new double[problem.length][variables];
	      for (int i = 0; i < problem.length; i++) {
	        for (int j = 0; j < variables; j++) {
	          newProblem[i][j] = problem[i][j];
	        }
	      }
	      problem = newProblem;
	    }

	    for (int i = 0; i < problem[row].length; i++) {
	      problem[row][i] = mask[i] ? 1 : 0;
	    }
	    conditionFunc[row] = LinearSolver.MAX;
	  }

	//***********************************************************************************************************************  
	  protected boolean checkSolution(double[] solution) {
	//***********************************************************************************************************************	  
	    int rows = problem.length-1;
	    for (int i = 0; i < rows; i++) {
	      double sum = 0;
	      for (int j = 0; j < solution.length; j++) {
	        sum += solution[j]*problem[i][j];
	      }
	      sum = ((double)Math.round((double)sum*100))/100;
	      if ((conditionFunc[i] == LinearSolver.GREATER && sum < d) ||
	          (conditionFunc[i] == LinearSolver.EQUAL && ((i == rows-1 && sum != 1)
	                                                || (i != rows-1 && sum!=0))))
	        return false;
	    }
	    return true;
	  }

	  
	//************************************************************************************  
	// Getters and Setters
	//************************************************************************************
	  protected double[][] getProblem() {
		    return problem;
	  }

	 protected UtaLinearProblem(final double[][] utaProblem, final Integer[] rank) {
		 this(utaProblem, rank, dConst);
	 }
		  
	  protected int getProblemVariablesCount() {
	    return problem[0].length;
	  }

	  protected void setProblem(double[][] problem) {
	    this.problem = problem;
	  }
	  protected boolean[] getConditionFunc() {
	    return conditionFunc;
	  }
	  protected void setConditionFunc(boolean[] conditionFunc) {
	    this.conditionFunc = conditionFunc;
	  }
	  protected double getD() {
	    return d;
	  }
	  protected void setD(double d) {
	    this.d = d;
	  }
	  protected int getVariables() {
	    return variables;
	  }
	  protected double[] getConstrains() {
	    return constrains;
	  }
	  protected void setConstrains(double[] constrains) {
	    this.constrains = constrains;
	  }

}
