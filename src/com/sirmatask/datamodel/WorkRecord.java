package com.sirmatask.datamodel;
import java.util.Date;

public class WorkRecord {

	private int employeeID;
	private int projectID;
	private Date dateFrom;
	private Date dateTo;
	
	public WorkRecord(int employeeID, int projectID, Date dateFrom, Date dateTo) {
		this.employeeID = employeeID;
		this.projectID = projectID;
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;		
	}
	
	public int getEmployeeID() {
		return employeeID;
	}
	
	public int getProjectID() {
		return projectID;
	}
	
	public Date getDateFrom() {
		return dateFrom;
	}
	
	public Date getDateTo() {
		return dateTo;
	}
}
