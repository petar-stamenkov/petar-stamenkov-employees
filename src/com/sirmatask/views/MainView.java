package com.sirmatask.views;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;

import com.sirmatask.datamodel.Team;
import com.sirmatask.exceptions.TasksParseException;
import com.sirmatask.utils.TeamsListLoader;

import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.JScrollPane;

public class MainView {
	
	private final String TABLE_COLUMNS[] = {"Employee 1 ID","Employee 2 ID","Project IDs", "Days Worked"};
	
	private JFrame frame;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainView window = new MainView();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainView() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setLocationRelativeTo(null);	
		frame.setResizable(false);
		
		JButton btnSelectFile = new JButton("Select File");
		btnSelectFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {								
				JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

				int returnValue = jfc.showOpenDialog(null);
				
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = jfc.getSelectedFile();
					System.out.println(selectedFile.getAbsolutePath());
					
					TeamsListLoader teamsListLoader = new TeamsListLoader();
					List<Team> teams;
					
					try {
						teams = teamsListLoader.findTeams(selectedFile.getAbsolutePath());
						
						populateTableView(teams);
					
						JOptionPane.showMessageDialog(null,  teamsListLoader.getMessageForTeamWithMostDaysWorked());
					} catch (IOException | TasksParseException e1) {
						
						JOptionPane.showMessageDialog(null, e1.getMessage());
					}
				}			
			}
		});
		btnSelectFile.setBounds(160, 215, 127, 23);
		frame.getContentPane().add(btnSelectFile);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(33, 11, 379, 177);
		frame.getContentPane().add(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);
	}
	
	private void populateTableView(List<Team> teams) {

		DefaultTableModel tableModel = new DefaultTableModel(TABLE_COLUMNS, 0) {

			private static final long serialVersionUID = 1L;
			
			@Override
		    public boolean isCellEditable(int row, int column) {
		       return false;
		    }
		};
		
		for (Team team: teams) {
			Object[] columns = {team.getEmployee1().getEmployeeID(),
					team.getEmployee2().getEmployeeID(),
					team.getProjectIDs().toString(),
					team.getDaysWorked()};
			
			tableModel.addRow(columns);
		}
		
		this.table.setModel(tableModel);
	}
}
