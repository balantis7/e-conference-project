package model;

import java.util.List;

public class Alternative {
	private String name;
	private Integer id;
	private Integer projectId;
	private List<Double> rates;
	private double utility;
	
	
	
	public double getUtility() {
		return utility;
	}
	public void setUtility(double utility) {
		this.utility = utility;
	}
	public List<Double> getRates() {
		return rates;
	}
	public void setRates(List<Double> rates) {
		this.rates = rates;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getProjectId() {
		return projectId;
	}
	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

}
