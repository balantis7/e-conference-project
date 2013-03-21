package UTA;

public class UtaSolve {
private Double[][] utaValues; // einai o polykritirios pinakas Products X Criteria
	
	private double[][] weights;   // einai oi epimerous times toy pediou timwn gia ka8e kritirio (ypologizontai mesw toy ari8moy twn intervals)
	private double[][] uWeigths;  // einai o pinakas [Nproducts][sumOfIntervals] poy ekfrazei ka8e enallaktiki ws grammiko syndyasmo twn varwn wij (UTASTAR paper, page 14 panw meros)
	private UtaLinearProblem problem;              // einai to Object poy dimiourgei ton pinaka toy grammikoy provlimatos
	private LinearSolver linear = new LinearSolver(); // einai to Object poy lynei to grammiko provlima
	
	private double[] result; // Einai i lysi toy grammikoy provlimatos
	private int[] criteriaVar;
	private int maxVar; // Ari8mos Kritiriwn
	private int weightCount;
	private double[] preOptimize;
	private double[][] optimizedCr;

//public UtaSolve(final UtaProblem problem) {
//@param problem UtaProblem
//this(problem.getValues(), problem.getIntervals(), problem.getBest(),
//  problem.getWorst(), problem.getRank(), problem.getD());
//}
	
public UtaSolve() {
	super();
}

//**********************************************************************************************************************	
public void UtaSolveMethod(Double[][] ratings, int[] intervals, double[] best, double[] worst, Integer[] ranks, double d) {
// Ylopoiisi tis UTASTAR(constructs the linear problem, solves and save it
// values[Nproducts][Mcriteria]: O polykritirios pinakas me tis times toy katanaloti gia ka8e enallaktiki (rows) kai ka8e criterion (columns).
// intervals[Mcriteria]: O ari8mos twn diastimatwn toy pedioy timwn ka8e kritirioy
// best[Mcriteria], worst[Mcriteria]: H best kai worst timi ka8e kritirioy 
// rank[Nproducts]: Einai i katataxi twn enallaktikwn
// d: Einai i delta parameter toy UTA problem	
//**********************************************************************************************************************	

// 1. Enimerwsi tis object variable me tis times toy polykritiriou pinaka ....	
	utaValues = ratings;
	
	// An den exoyn oristei diastimata toy pedioy timwn enos kritirioy, vazoyme by default tin timi 3.....
	//if (intervals == null) {
	//	intervals = new int[worst.length];
	//	for (int i = 0; i < intervals.length; i++) {
	//		intervals[i] = 3;
	//	}
	//}
	
// 2. Constructs ton pinaka toy grammikoy provlimatos, lamvanontas ypopsi ton polykritirio pinaka, ta diastimata kai tis times twn kritiriwn 
	constructProblem(ratings, intervals, best, worst, ranks, d);
	
// 3. Epilisi toy grammikoy provlimatos
	solve();
}

//**********************************************************************************************************************
private void constructProblem(Double[][] ratings, int[] intervals, double[] best, double[] worst, Integer[] ranks, double d) {
//Kataskeyazetai to grammiko provlima ....	
//**********************************************************************************************************************

//1. Ston pinaka weights ypologizei tis endiameses times sto pedio timwn ka8w krithriou    
	 
	// Pleon ta ypologizoume stin arxi, prin trexoume ti UtaSolveMethod (1 fora, oxi gia ka8e customer)
	createWeigths(intervals, best, worst);  
	 weightCount = sum(intervals);
	 
// 2. Ston pinaka uWeights[Nproducts][sumOfIntervals]  
//   ka8e enallaktiki toy katanaloti ekfrazetai ws grammikos syndiasmos twn varwn wij "UTASTAR paper, page 14 sto panw meros"     
	uWeigths = new double[ratings.length][];
	for (int i = 0; i < ratings.length; i++) { //for each ennalaktiki
	  uWeigths[i] = getWeight(ratings[i], weightCount);
	}
	
// 3. Orismos toy object problem poy einai typoy UtaLinearProblem kai periexei metaxy twn allwn
//   ton pinaka toy grammikoy provlhmatos tis UTASTAR ("UTASTAR paper, page 14 sto katw meros") 
	problem = new UtaLinearProblem(uWeigths, ranks, d);
	
	// pkonto 
	result = new double[weightCount];
	criteriaVar = new int[weightCount];
	
	int [] cr_intervals = new int[intervals.length];
	for (int i=0; i < intervals.length; i++)
		cr_intervals[i] = intervals[i];
	
	for (int i = 0, vCount = 1; i < criteriaVar.length; i++) {
	  criteriaVar[i] = (--cr_intervals[vCount - 1] > 0) ? vCount : vCount++;
	}
}


//*******************************************************************************************
private void createWeigths(int[] intervals, double[] best,  double[] worst) {
//Ypologizei tis endiameses times toy pediou timwn gia ka8e kritirio (vasei twn internals)
//To apotelesma einai o pinakas weights[Mcriteria][var] me metavlito ari8mo timwn gia ka8e kritirio (columns)	
//*******************************************************************************************	
	maxVar = intervals.length;

//	Orismos toy pinaka weights
	weights = new double[maxVar][];

//Ypologismos twn endiameswn timwn
	for (int i = 0; i < maxVar; i++) 
		weights[i] = estimateIntervals(intervals[i], worst[i], best[i]);

}

//*******************************************************************************************
private static double[] estimateIntervals(int intervals, double start, double end) {
//Ypologizei tis endiameses times sto pedio timwn ka8e kritiriou, vasei twn intervals - "Typos 7-9, UTASTAR paper, page 8" 	
//*******************************************************************************************	

	double[] result = new double[intervals + 1];

	result[0] = start;
	result[intervals] = end;

	double step = (end - start) / intervals;
	
	for (int i = 1; i < intervals; i++) 
		result[i] = D3D( result[i - 1] + step);
	//pkonto 07-06-2012
	//result[i] = result[i - 1] + step;
	
	return result;
}

//*******************************************************************************************
private double[] getWeight(final Double[] ratings, int columns) {
//  Epistrefei pinaka poy ekfrazei mia enallaktiki ws grammiko syndiasmo twn varwn wij "UTASTAR paper, page 14 sto panw meros"
// u11 = 0, u12 = w11, u13 = w11 + w12, u14 = w11 + w12 + w13 ...	
//*******************************************************************************************	
	double[] result = new double[columns];
	int count = 0;
	for (int j = 0; j < ratings.length; j++) {
		double[] w = weights[j];
		boolean isSet = false;
		
		for (int i = 0; i < w.length - 1; i++) {
			if (isSet) 
				result[count++] = 0;
			else if (isBetween(w[i], w[i + 1], ratings[j])) {
				result[count++] = interpolation(w[i], w[i + 1], ratings[j]);
				isSet = true;
			}
			else 
				result[count++] = 1;
			
		}
	}
	return result;
}

//*******************************************************************************************
private static double interpolation(final double previous, final double next, final double val) {
// Kanei interpolation tis timis val, metaxy 2 akrwn enos diastimatos timwn ....	
//*******************************************************************************************	
	return (val - previous) / (next - previous);
}

//*******************************************************************************************
public double[][] estimateInitialWeights(int[] intervals, double[] best, double[] worst) {
//Epistrefei ton pinaka weights poy einai oi epimerous times toy pediou timwn gia ka8e kritirio 
//(ypologizontai mesw toy ari8moy twn intervals)
//*******************************************************************************************	
	createWeigths(intervals, best, worst);
	return weights;
}


//*******************************************************************************************
private double[] solve() {
// Lynei to grammiko provlima kai epistrefei to result[] me ta weights (Table 2.1, UTASTAR paper, page 14) 
//*******************************************************************************************	
	result = new double[problem.getProblemVariablesCount()];
	double[][] objective = linear.UTASolve(problem,true);
	
	result=objective[1]; //save the solution of the linear problem
	
	if (objective[0][0] == 0) //check if F*=0 (isos i metaveltistopoiisi prepei na trexei panta)
		postOptimize();
	
	return result;
}



//*******************************************************************************************
// Getters and Setters
//*******************************************************************************************

//*******************************************************************************************
public double[] getSolution() {
// Retrieves the solution.	
// Mas dinei tis olikes xrisimotites gia ka8e proion 	
//*******************************************************************************************	
	return getTotalUtility(utaValues);
}

//*******************************************************************************************
public double[][] getPartialUtilities() {
//Retrieves the solution.	
//Mas dinei tis merikes xrisimotites gia ka8e proion, gia ka8e kritirio 	
//*******************************************************************************************	
		
		return getPartialUtilities(utaValues);
	}

//******************************************************************************************
public double[][] getMarginalValues() {
//Epistrefei tis marginal functions gia ka8e kritirio (einai ta vari se ka8e timi toy pediou timwn)
//******************************************************************************************	
	return getMarginalValuesFromResult(result);
}

//*******************************************************************************************
public double[] getTotalUtility(Double[][] utaValues2) {
//  values einai ta utaValues, dhladi oi arxikoi polykritirioi pinakes ka8e katanalwti
//  Ypologizei tin oliki xrisimotita gia ka8e proion, vw a8roisma twn merikwn xrisimotitwn ka8e kritiriou toy proiontos
//*******************************************************************************************	
	double[] solution = new double[uWeigths.length];
	
	for (int i = 0; i < solution.length; i++) {
	  for (int j = 0; j < maxVar; j++) {
		  solution[i] = solution[i] + getPartialUtility(utaValues2[i][j], j);
	  }
	}

	return solution;
}

//*******************************************************************************************
public double[][] getPartialUtilities(Double[][] utaValues2) {
// Ypologizei tis merikes xrisimotites gia ka8e proion, gia ka8e kritirio	
// values einai ta utaValues, dhladi oi arxikoi polykritirioi pinakes ka8e katanalwti
// res[Nproducts][Ncriteria]	
//*******************************************************************************************	
	double[][] res = new double[uWeigths.length][maxVar];
	
	for (int i = 0; i < res.length; i++) {
	  for (int j = 0; j < maxVar; j++) {
		  res[i][j] = getPartialUtility(utaValues2[i][j], j);
	  }
	}

	return res;
}

//**********************************************************************************************************
public double getPartialUtility(final double value, final int crID) {
//Ypologizei ti meriki xrisimotita gia tin timi enos kritiriou
//value einai i timi toy kritiriou, crID einai to indx toy kritiriou ston pinaka kritiriwn
//Xrhsimopoiei ton pinaka weights[Ncriteria] me to pedio timwn toy kritiriou, kai
//ton pinaka results[] poy periexei ta Wij diladi ta apotelesmata toy grammikoy provlimatos.	
//**********************************************************************************************************	
	if (result == null) {
	  throw new IllegalArgumentException("No solution Defined");
	}
	
	double pu = 0;
	double[] w = weights[crID]; // O w periexei to pedio timwn toy kritiriou (apo ton pinaka weights)
	int startIdx = getWeightStart(crID);
	
	if (value == w[0]) {
	  return pu;
	}
	
	for (int i = 0; i < w.length - 1; i++) {
	  if (isBetween(w[i], w[i + 1], value)) {
	    pu += interpolation(w[i], w[i + 1], value) * result[startIdx + i];
	    return pu;
	  }
	  else {
	    pu += result[startIdx + i];
	  }
	}
	return pu;
}



//******************************************************************************************
private double[][] getMarginalValuesFromResult(final double[] result) {
//******************************************************************************************	
	int max = maxCols(weights);
	double[][] res = new double[weights.length][max];
	for (int i = 0; i < maxVar; i++) {
	  int count;
	  for (count = 0; count < max; count++) { //if (i != 0 && criteriaVar[i] != criteriaVar[i-1])
	    res[i][count] = (count < weights[i].length) ? getPartialUtility(result, weights[i][count], i) : -1;
	  }
	}
	return res;
}

//******************************************************************************************
public double[][] getWeights() {
// Retrieves the solution. 
// Epistrefei tis marginal functions gia ka8e kritirio	
//	It is an array where each tuple has the utility of the brand	
//******************************************************************************************	
	return getWeightsForResult(result);
}



//******************************************************************************************
private double[][] getWeightsForResult(final double[] result) {
//******************************************************************************************	
	int max = maxCols(weights);
	double[][] res = new double[max][weights.length];
	for (int i = 0; i < maxVar; i++) {
	  int count;
	  for (count = 0; count < max; count++) { //if (i != 0 && criteriaVar[i] != criteriaVar[i-1])
	    res[count][i] = (count < weights[i].length) ? getPartialUtility(result, weights[i][count], i) : -1;
	  }
	}
	return res;
}

//******************************************************************************************
public static int maxCols(final double[][] val) {
//******************************************************************************************	
	int res = 0;
	if (val == null) 
	  return 0;
	
	for (int i = 0; i < val.length; i++) 
		if (res < val[i].length) 
			res = val[i].length;
	
	return res;
}



/**
* gets the utility value of the
* @param values A double array with the values for each criterion.
* @return The utility value for the specified array of values
* @throws IllegalArgumentException when the length of the array values is not the same with the number criteria of the problem.
*/
//******************************************************************************************
public double getUtility(final double[] values) {
//******************************************************************************************	
	if (values.length != maxVar) {
	  throw new IllegalArgumentException("The length of values array must be " +
	                                     maxVar + " not " + values.length);
	}

	double solution = 0;
	
	for (int i = 0; i < maxVar; i++) {
	  solution += getPartialUtility(values[i], i);
	}
	
	return solution;
}



private int getMaxWeightCount() {
	int res = 0;
	int cur;
	int count;
	for (int i = 0; i < criteriaVar.length; ) {
	  cur = criteriaVar[i];
	  count = 0;
	  while (i < criteriaVar.length && criteriaVar[i] == cur) {
	    count++;
	    i++;
	  }
	  if (count > res) {
	    res = count;
	  }
	}
	return res;
}




//*************************************************************************************************
private int getWeightStart(final int crID) {
//*************************************************************************************************	
	for (int i = 0; i < criteriaVar.length; i++) {
	  if (crID + 1 == criteriaVar[i]) {
	    return i;
	  }
	}
	throw new IllegalArgumentException("no such id.");
}



//*******************************************************************************************
public static double max(final double[] value) {
// Gets the maximum number of a vector	
//*******************************************************************************************	
	double result = value[0];
	for (int i = 1; i < value.length; i++) {
	  if (result < value[i]) {
	    result = value[i];
	  }
	}
	return result;
}


//*******************************************************************************************
public static double min(final double[] value) {
// Gets the minimum number of a vector	
//*******************************************************************************************	
	double result = value[0];
	for (int i = 1; i < value.length; i++) {
	  if (result > value[i]) {
	    result = value[i];
	  }
	}
	return result;
}

//*******************************************************************************************
public static int sum(final int[] value) {
// 	Gets the sum of an array of values
//*******************************************************************************************	
int result = value[0];
for (int i = 1; i < value.length; i++) {
  result += value[i];
}
return result;
}


private static boolean isBetween(final double a, final double b, final double val) {
	return (a <= val && b >= val) || (a >= val && b <= val);
}

//***********************************************************************************************************
private void postOptimize() {
	// Kanei ti metaveltistopoiisi tis UTASTAR, trexontas xana to grammiko provlima prospa8wntas na kanei MAXIMUM tis U(g*) ka8e kritiriou xwrista (Table 5, page 15, paper UTASTAR)	
//***********************************************************************************************************	
	int varCount = criteriaVar.length;
	 optimizedCr = new double[maxVar][varCount];
	 preOptimize = (double[])result.clone();
	 double[][] out;
	for (int i = 0; i < maxVar; i++) {
	  boolean[] mask = new boolean[varCount];
	  for (int j = 0; j < varCount; j++) {
	    mask[j] = criteriaVar[j] == i + 1;
	  }
	  problem.reformProblem(mask);
	  out=linear.UTASolve(problem,false) ;
	  optimizedCr[i]=out[1];
	  for (int j = 0; j < optimizedCr[i].length; j++) {
	    result[j] = optimizedCr[i][j] + ( (i == 0) ? 0 : result[j]);
	  }
	}
// To apotelesma tis metaveltistopoiisis einai i mesi timi twn apotelesmatwn twn grammikwn provlimatwn megistopoiisis tis timis ka8e kritiriou xwrista....	
	for (int j = 0; j < result.length; j++) {
	  result[j] /= maxVar;
	}
}

/**
* Gets the weights of the UTA solution before applying post optimal techniques.
* @return A two dimensional array. Each row has the weigths for each interval of the solution.
*/
public double[][] getPreOptimalWeights() {
	return getWeightsForResult(preOptimize);
}

/**
* Gets the weights of the UTA optimal solution for a specific criterion.
* @param crid An int specifing the criterion index.
* @return A two dimensional array. Each row has the weigths for each interval of the solution.
*/

//**************************************************************************************************
public double[][] getOptimalWeightsForCriterion(int crid) {
//**************************************************************************************************	
	return getWeightsForResult(optimizedCr[crid]);
}

//******************************************************************************************
public double[][] getMaxWeightsForCriteria() {
// Apotelesmata metaveltistopoiisis - Epistrefei ta max weights ka8e kritiriou apo to linear problem poy megistopei to kritirio
// result [Ncriteria][Ncriteria]	
//******************************************************************************************	
	//int max = maxCols(weights);
	double[][] res = new double[maxVar][maxVar];
	int i, j, count;
 
	for(i=0; i< maxVar; i++){	
		for (j = 0; j < maxVar; j++) {
			// Gia ka8e kritirio vriskw ti 8esi toy ston opt pinaka kai to plh8os twn diastimatwn toy.
				int startIndx =getWeightStart(j);
				int num_intervals = weights[j].length-1;
	  	
				for (count = startIndx; count < startIndx + num_intervals; count++) 
						res[i][j] = res[i][j]  + optimizedCr[i][count]; 
		}		
	}
	return res;
	
	 //for (int j = 0; j < optimizedCr[i].length; j++) {
	//	    result[j] = optimizedCr[i][j] + ( (i == 0) ? 0 : result[j]);
	//}
	 
	 
}

public double[][] getIntervals()
{
	return weights;
}

/**
* Gets a partial utility for the value and criterion with index crid
* @param value a double with the value of the criterion
* @param crID an <code>int</code> indicating the criterion index
* @return a double with the partial utility
*/
private double getPartialUtility(final double[] result, final double value, final int crID) {
	if (result == null) {
	throw new IllegalArgumentException("No solution Defined");
	}
	double pu = 0;
	double[] w = weights[crID];
	int startIdx = getWeightStart(crID);
	
	if (value == w[0]) {
	return pu;
	}
	
	for (int i = 0; i < w.length - 1; i++) {
	if (isBetween(w[i], w[i + 1], value)) {
	  pu += interpolation(w[i], w[i + 1], value) * result[startIdx + i];
	  return pu;
	}
	else {
	  pu += result[startIdx + i];
	}
	}
	
	return pu;
}

//Converts a double to a double with 3 decimals	
private static double D3D(double d) {
	//int c = 3;
	//int temp=(int)((d*Math.pow(10,c)));
	//return (((double)temp)/Math.pow(10,c));
	return (round(d*1000.0) / 1000.0);
}

//Converts a double to a double with 2 decimals
private double D2D(double d) {
	//int c = 2;
	//int temp=(int)((d*Math.pow(10,c)));
	//return (((double)temp)/Math.pow(10,c));
	return (round(d*100.0) / 100.0);
}

public static double round(Double i) {
    return  Math.round(i + ((i > 0.0) ? 0.00000001 : -0.00000001));
}




}
