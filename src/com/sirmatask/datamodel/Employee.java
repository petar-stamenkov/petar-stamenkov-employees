package com.sirmatask.datamodel;
import java.util.List;

public class Employee {

	private int employeeID;
	private List<Assignment> assignments;
	
	public Employee(int employeeID, List<Assignment> assignments) {
		this.employeeID = employeeID;
		this.assignments = assignments;
	}

	public int getEmployeeID() {
		return employeeID;
	}
	
	public List<Assignment> getAssignments() {
		return assignments;
	}
}
