package com.sirmatask.datamodel;
import java.util.ArrayList;

public class Team {
	
	private Employee employee1;
	private Employee employee2;
	private ArrayList<Integer> projectIDs;
	private int daysWorked;
	
	public Team(Employee employee1, Employee employee2) {
		this.employee1 = employee1;
		this.employee2 = employee2;
		this.projectIDs = new ArrayList<Integer>();
	}

	public Employee getEmployee1() {
		return employee1;
	}
	
	public Employee getEmployee2() {
		return employee2;
	}

	public ArrayList<Integer> getProjectIDs() {
		return projectIDs;
	}
	
	public int getDaysWorked() {
		return daysWorked;
	}
	
	public void addWorkedTime(int projectID, long daysWorked) {
		this.daysWorked += daysWorked;
		
		if(projectIDs.contains(projectID) == false) {
			projectIDs.add(projectID);
		}
	}
}
