package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import businessLogic.BLFacade;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import domain.Member;
import domain.User;

public class ManageUsersGUI extends JFrame {

	private JTable userTable;
	private DefaultTableModel tableModel;
	private BLFacade facade = MainGUI.getBusinessLogic(); // Access business logic

	private JLabel titleLabel;

	public ManageUsersGUI() {
		setTitle("Manage Users");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setLayout(null); // Use null layout

		// Title label
		titleLabel = new JLabel("Manage Users", SwingConstants.CENTER);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
		add(titleLabel);

		// Table setup
		String[] columnNames = { "Email", "Password", "Complete Name", "Document ID" };
		tableModel = new DefaultTableModel(columnNames, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false; // Disable editing for all cells
			}
		};

		userTable = new JTable(tableModel);
		userTable.setRowHeight(30);
		userTable.setFont(new Font("Arial", Font.PLAIN, 14));

		JScrollPane scrollPane = new JScrollPane(userTable);
		add(scrollPane);

		// Back button
		JButton backButton = new JButton("Back");
		backButton.setFont(new Font("Arial", Font.PLAIN, 14));
		add(backButton);

		// Center components dynamically
		addComponentListener(new java.awt.event.ComponentAdapter() {
			@Override
			public void componentResized(java.awt.event.ComponentEvent e) {
				int frameWidth = getWidth();
				int frameHeight = getHeight();

				int titleWidth = 300;
				int titleHeight = 30;
				int tableWidth = 800;
				int tableHeight = 400;
				int buttonWidth = 100;
				int buttonHeight = 30;

				titleLabel.setBounds((frameWidth - titleWidth) / 2, 20, titleWidth, titleHeight);
				scrollPane.setBounds((frameWidth - tableWidth) / 2, (frameHeight - tableHeight) / 2 - 40, tableWidth,
						tableHeight);
				backButton.setBounds((frameWidth - buttonWidth) / 2, (frameHeight + tableHeight) / 2 + 20, buttonWidth,
						buttonHeight);
			}
		});

		// Load user data into the table
		loadUserData();

		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose(); // Close the current window
			}
		});
	}

	private void loadUserData() {
		tableModel.setRowCount(0); // Clear existing rows
		List<User> users = facade.getAllUsers(); // Fetch all users

		for (User user : users) {
			if (user instanceof Member) { // Filter only Member users
				tableModel.addRow(new Object[] { user.getName(), user.getPassword(), user.getCompleteName(),
						user.getDocumentID() });
			}
		}
	}

	public void setVisible(boolean visible) {
		super.setVisible(visible);
	}
}
