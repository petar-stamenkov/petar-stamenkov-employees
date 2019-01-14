package com.sirmatask.utils;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.sirmatask.exceptions.TasksParseException;

public class TasksParser {
	
	private final int COLUMNS_NUMBER = 4;
	private final String CVS_SEPARATOR = ", ";
	
	public List<String[]> readFile(String fileName) throws TasksParseException, IOException {
        BufferedReader bufferedReader = null;
        String line = "";
        List<String[]> tasksInfo = new ArrayList<String[]>();

        bufferedReader = new BufferedReader(new FileReader(fileName));
        
		while ((line = bufferedReader.readLine()) != null) {
			if(line.isEmpty()) {
				continue;
			}
			
			String[] row = line.split(CVS_SEPARATOR);
						
			if (row.length == this.COLUMNS_NUMBER) {
				tasksInfo.add(row);
			} else {
				bufferedReader.close();
				throw new TasksParseException();
			}
		}
		
		bufferedReader.close();
		return tasksInfo;
	}
}
