package com.sirmatask.datamodel;

import java.util.Date;

public class Assignment {

	private int projectID;
	private Date dateFrom;
	private Date dateTo;
	
	public Assignment(int projectID, Date dateFrom, Date dateTo) {
		this.projectID = projectID;
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
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
