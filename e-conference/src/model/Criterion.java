package model;

import java.util.ArrayList;
import java.util.List;

import org.primefaces.model.chart.CartesianChartModel;

public class Criterion {
	private Integer criteriaID;
	private Integer decisionProjectID;
	private String name;
	private Integer intervals;
	private Double best;
	private Double worst;
	private boolean quantitative;
	private List<Scale> scale;
	private double weight;
	private CartesianChartModel graph;
	
	public Criterion()
	{
		name="";
		intervals=0;
		best=0.0;
		worst=0.0;
		quantitative=false;
		scale=new ArrayList<Scale>();
		graph=new CartesianChartModel();
		
		
	}

	
	public CartesianChartModel getGraph() {
		return graph;
	}


	public void setGraph(CartesianChartModel graph) {
		this.graph = graph;
	}


	public double getWeight() {
		return weight;
	}


	public void setWeight(double weight) {
		this.weight = weight;
	}


	public List<Scale> getScale() {
		return scale;
	}


	public void setScale(List<Scale> scale) {
		this.scale = scale;
	}


	public boolean isQuantitative() {
		return quantitative;
	}

	public void setQuantitative(boolean quantitative) {
		this.quantitative = quantitative;
	}

	public Integer getCriteriaID() {
		return criteriaID;
	}
	public void setCriteriaID(int criteriaID) {
		this.criteriaID = criteriaID;
	}
	public Integer getDecisionProjectID() {
		return decisionProjectID;
	}
	public void setDecisionProjectID(int decisionProjectID) {
		this.decisionProjectID = decisionProjectID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getIntervals() {
//		if(String.valueOf(intervals)!=null)
//			return String.valueOf(intervals);
//		else
//			return "0";
		return intervals;
	}
	public void setIntervals(Integer intervals) {
		this.intervals = intervals;
	}
	public Double getBest() {
		
//		if(String.valueOf(best)!=null)
//			return String.valueOf(best);
//		else
//			return "0";
		return best;
	}
	public void setBest(Double best) {
		this.best = best;
	}
	public Double getWorst() {
//		if(String.valueOf(worst)!=null)
//			return String.valueOf(worst);
//		else
//			return "0";
		return worst;
	}
	public void setWorst(Double worst) {
		this.worst = worst;
	}
	

}
