package com.sirmatask.utils;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.sirmatask.datamodel.Assignment;
import com.sirmatask.datamodel.Employee;
import com.sirmatask.datamodel.Team;
import com.sirmatask.datamodel.WorkRecord;
import com.sirmatask.exceptions.TasksParseException;

public class TeamsListLoader {
	
	private final String TEAM_WITH_MOST_DAYS_MESSAGE = "Employees who worked together on a project the most time: ";
	private final String NO_TEAMS_FOUND_MESSAGE = "No teams found.";
	
	private final String[] DATE_FORMAT_STRINGS = {"yyyy/MM/dd", "yyyy-MM-dd"};
	
	private final int EMPLOYEEID_COLUMN_INDEX = 0;
	private final int PROJECTID_COLUMN_INDEX = 1;
	private final int DATEFROM_COLUMN_INDEX = 2;
	private final int DATETO_COLUMN_INDEX = 3;

	private List<Team> teams;
	
	public TeamsListLoader() {
		teams = new ArrayList<Team>();
	}
	
	public String getMessageForTeamWithMostDaysWorked() {
		if(teams.size() > 0) {
			return (TEAM_WITH_MOST_DAYS_MESSAGE +
				+ teams.get(0).getEmployee1().getEmployeeID() + ", " 
				+ teams.get(0).getEmployee2().getEmployeeID());
		}
		
		return NO_TEAMS_FOUND_MESSAGE;
	}
	
	public List<Team> findTeams(String fileName) throws IOException, TasksParseException {

		List<WorkRecord> workRecords = createTasksList(fileName);
		
		if(workRecords.size() <= 0) {
			return new ArrayList<Team>();
		}
		
		List<Employee> employees = createEmployeesList(workRecords);
		
		teams = createTeamsList(employees);
		
		return teams;
	}
	
	private List<WorkRecord> createTasksList(String fileName) throws TasksParseException, IOException{
		//create tasks list
		List<WorkRecord> workRecords = new ArrayList<WorkRecord>();
		TasksParser parser = new TasksParser();
		List<String[]> tasksInfo = parser.readFile(fileName);
		
		for (int i=0; i<tasksInfo.size(); i++) {
			int employeeID = Integer.parseInt(tasksInfo.get(i)[EMPLOYEEID_COLUMN_INDEX]);
			int projectID = Integer.parseInt(tasksInfo.get(i)[PROJECTID_COLUMN_INDEX]);
			
			Date dateFrom;
			Date dateTo;

			if(tasksInfo.get(i)[DATEFROM_COLUMN_INDEX].equals("NULL") == false) {
				dateFrom = tryParseDate(tasksInfo.get(i)[DATEFROM_COLUMN_INDEX]);
			} else {
				dateFrom = new Date();
			}
			
			if(tasksInfo.get(i)[DATETO_COLUMN_INDEX].equals("NULL") == false) {
				dateTo = tryParseDate(tasksInfo.get(i)[DATETO_COLUMN_INDEX]);
			} else {
				dateTo = new Date();
			}
			
			WorkRecord tempTask = new WorkRecord(employeeID, projectID, dateFrom, dateTo);
			workRecords.add(tempTask);	
		}
		
		//sort tasks by employeeID
		Collections.sort(workRecords, new Comparator<WorkRecord>() {

	        public int compare(WorkRecord task1, WorkRecord task2) {
	            return task1.getEmployeeID() - task2.getEmployeeID();
	        }
	    });
		
		return workRecords;
	}
	

	private Date tryParseDate(String dateString)
	{
	    for (String formatString : DATE_FORMAT_STRINGS)
	    {
	        try
	        {
	            return new SimpleDateFormat(formatString).parse(dateString);
	        }
	        catch (ParseException e) {}
	    }
	
	    return null;
	}
	
	private List<Employee> createEmployeesList(List<WorkRecord> workRecords){
		//create employees list
		List<Employee> employees = new ArrayList<Employee>();
		List<Assignment> tempAssignments = new ArrayList<Assignment>();
		
		int tempID = workRecords.get(0).getEmployeeID();
		
		for (int i=0; i<workRecords.size(); i++) {
			if(tempID != workRecords.get(i).getEmployeeID()) {
				employees.add(new Employee(tempID, new ArrayList<Assignment>(tempAssignments)));
				tempAssignments.clear();
				tempID = workRecords.get(i).getEmployeeID();
			}
			
			tempAssignments.add(new Assignment(workRecords.get(i).getProjectID(), workRecords.get(i).getDateFrom(), workRecords.get(i).getDateTo()));
		}
		
		if (tempAssignments.size() > 0) {
			employees.add(new Employee(tempID, new ArrayList<Assignment>(tempAssignments)));
		}
		
		return employees;
	}
	
	private List<Team> createTeamsList(List<Employee> employees){
		//create teams list
		List<Team> teams = new ArrayList<Team>();
		
		for (int i=0; i<employees.size()-1; i++) {
			for (int j=i+1; j<employees.size(); j++) {
				Team tempTeam = new Team(employees.get(i), employees.get(j));
				
				for (int a=0; a<employees.get(i).getAssignments().size(); a++) {
					for (int b=0; b<employees.get(j).getAssignments().size(); b++) {
						Assignment emp1Assignment = employees.get(i).getAssignments().get(a);
						Assignment emp2Assignment = employees.get(j).getAssignments().get(b);

						if(emp1Assignment.getProjectID() == emp2Assignment.getProjectID()) {
							Date overlapStartDate = getOverlapStartDate(emp1Assignment, emp2Assignment);
							Date overlapEndDate = getOverlapEndDate(emp1Assignment, emp2Assignment);
							
							long overlapMilliseconds = overlapEndDate.getTime() - overlapStartDate.getTime();
						    long overlapDays = TimeUnit.DAYS.convert(overlapMilliseconds, TimeUnit.MILLISECONDS);
						    
						    if(overlapDays > 0)
						    	tempTeam.addWorkedTime(emp1Assignment.getProjectID(), overlapDays);
						}
					}
				}	
				
				if(tempTeam.getProjectIDs().size() > 0) {
					teams.add(tempTeam);
				}
			}
		}
		
		//sort teams by days worked together
		Collections.sort(teams, new Comparator<Team>() {

	        public int compare(Team team1, Team team2) {
	            return team2.getDaysWorked() - team1.getDaysWorked();
	        }
	    });
		
		return teams;
	}
	
	private Date getOverlapStartDate(Assignment emp1Assignment, Assignment emp2Assignment) {
		Date overlapStartDate;
		
		if(emp1Assignment.getDateFrom().compareTo(emp2Assignment.getDateFrom()) > 0){
			overlapStartDate = emp1Assignment.getDateFrom();
		} else {
			overlapStartDate = emp2Assignment.getDateFrom();
		}
		
		return overlapStartDate;
	}
	
	private Date getOverlapEndDate(Assignment emp1Assignment, Assignment emp2Assignment) {
		Date overlapEndDate;
		
		if(emp1Assignment.getDateTo().compareTo(emp2Assignment.getDateTo()) < 0){
			overlapEndDate = emp1Assignment.getDateTo();
		} else {
			overlapEndDate = emp2Assignment.getDateTo();
		}
		
		return overlapEndDate;
	}
}
