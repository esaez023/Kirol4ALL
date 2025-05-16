package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import businessLogic.BLFacade;
import domain.Member;
import domain.Reservation;
import domain.Room;
import domain.Session;
import domain.User;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.formdev.flatlaf.FlatLightLaf;

public class MainGUI extends JFrame {

	private static final long serialVersionUID = 1L;

	private Member loggedInUser;

	private JTable sessionTable;
	private DefaultTableModel tableModel;
	private JComboBox<String> difficultyComboBox;

	private JLabel welcomeLabel;
	private JButton reserveButton;
	private JButton logButton;
	private JButton viewProfileButton;

	private static BLFacade appFacadeInterface;

	public static BLFacade getBusinessLogic() {
		return appFacadeInterface;
	}

	public static void setBussinessLogic(BLFacade afi) {
		appFacadeInterface = afi;
	}

	/**
	 * Constructor for MainGUI
	 */
	public MainGUI(User user) {

		super("Main GUI");
		setTitle("kirol4ALL");
		this.loggedInUser = (Member) user;

		try {
			UIManager.setLookAndFeel(new FlatLightLaf()); // Use FlatDarkLaf() for a dark theme
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		// Set the window to full screen
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Set up the main layout
		getContentPane().setLayout(new BorderLayout());

		// Top panel with BorderLayout
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0)); // Add space above and below the title

		// Title label
		JLabel titleLabel = new JLabel("Kirol4ALL - All Sessions", SwingConstants.CENTER);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Larger, bold font
		topPanel.add(titleLabel, BorderLayout.NORTH);

		// Left side: Difficulty filter
		JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		filterPanel.add(new JLabel("Filter by Difficulty:"));
		difficultyComboBox = new JComboBox<>(new String[] { "All", "1", "2", "3", "4", "5" });
		difficultyComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String selectedDifficulty = (String) difficultyComboBox.getSelectedItem();
				loadTableData(selectedDifficulty);
			}
		});
		filterPanel.add(difficultyComboBox);
		topPanel.add(filterPanel, BorderLayout.WEST);

		// Top panel with welcome label
		// JPanel topPanel = new JPanel(new BorderLayout());
		welcomeLabel = new JLabel("Please log in to start training.", SwingConstants.CENTER);
		welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 16));
		topPanel.add(welcomeLabel, BorderLayout.NORTH);

		// Add the top panel to the frame
		getContentPane().add(topPanel, BorderLayout.NORTH);

		// Table setup
		String[] columnNames = { "Activity Name", "Date", "Hour", "Difficulty" };
		// tableModel = new DefaultTableModel(columnNames, 0);
		sessionTable = new JTable(tableModel);// Updated table model to disable cell editing
		tableModel = new DefaultTableModel(columnNames, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false; // Disable editing for all cells
			}
		};

		// Initialize sessionTable
		sessionTable = new JTable(tableModel);

		// Disable column reordering
		sessionTable.getTableHeader().setReorderingAllowed(false);

		// Adjust table appearance
		sessionTable.setRowHeight(40);
		sessionTable.setFont(new Font("Arial", Font.PLAIN, 12));
		sessionTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

		// Adjust header appearance
		JTableHeader tableHeader = sessionTable.getTableHeader();
		tableHeader.setFont(new Font("Arial", Font.BOLD, 14));
		tableHeader.setPreferredSize(new Dimension(tableHeader.getPreferredSize().width, 30));

		// Center align table content
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		for (int i = 0; i < sessionTable.getColumnCount(); i++) {
			sessionTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}

		// Add table to scroll pane
		JScrollPane scrollPane = new JScrollPane(sessionTable);
		getContentPane().add(scrollPane, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));

		// Add this in the constructor, inside the bottom panel setup viewProfileButton
		viewProfileButton = new JButton("View Profile");
		viewProfileButton.setFont(new Font("Arial", Font.PLAIN, 12));
		viewProfileButton.setPreferredSize(new Dimension(100, 30));
		viewProfileButton.setVisible(false);
		viewProfileButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (loggedInUser != null) {
					// Pass the logged-in user to ProfileGUI
					new ProfileGUI(loggedInUser).setVisible(true);
				} else {
					JOptionPane.showMessageDialog(MainGUI.this, "No user is logged in.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		buttonPanel.add(viewProfileButton);

		// Right side: Log In button
		logButton = new JButton("Log In");
		logButton.setFont(new Font("Arial", Font.PLAIN, 12));
		logButton.setPreferredSize(new Dimension(100, 30)); // Adjust button size
		logButton.addActionListener(e -> {
			if (loggedInUser != null) {
				loggedInUser = null;
				updateUI();
				JOptionPane.showMessageDialog(MainGUI.this, "You have been logged out.", "Info",
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				// Log in the user (redirect to LoginGUI)
				new LoginGUI().setVisible(true);
				setVisible(false); // Close the current window
			}
		});
		buttonPanel.add(logButton);

		// Bottom panel with "Reserve" button
		reserveButton = new JButton("Reserve"); // Initialize the class-level reserveButton
		JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
		reserveButton = new JButton("Reserve");
		reserveButton.setFont(new Font("Arial", Font.PLAIN, 12));
		reserveButton.setPreferredSize(new Dimension(100, 30)); // Smaller button size
		reserveButton.setVisible(false);
		reserveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = sessionTable.getSelectedRow();
				if (selectedRow == -1) {
					JOptionPane.showMessageDialog(MainGUI.this, "Please select a session to view details.", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				// Retrieve session details from the selected row
				String activityName = (String) tableModel.getValueAt(selectedRow, 0);
				String date = (String) tableModel.getValueAt(selectedRow, 1);
				String hour = (String) tableModel.getValueAt(selectedRow, 2);

				SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

				// Fetch the session object from the business logic
				List<Session> sessions = getBusinessLogic().getAllSessions();
				Session selectedSession = null;
				for (Session session : sessions) {
					String formattedDate = dateFormat.format(session.getDate());
					String formattedTime = timeFormat.format(session.getHour());

					if (session.getActivity().getName().equals(activityName) && formattedDate.equals(date)
							&& formattedTime.equals(hour)) {
						selectedSession = session;
						break;
					}
				}

				if (selectedSession == null) {
					JOptionPane.showMessageDialog(MainGUI.this, "Selected session could not be found.", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				// Check if the room capacity is full
				Room sessionRoom = selectedSession.getRoom();
				if (sessionRoom != null && selectedSession.getReservations().size() >= sessionRoom.getCapacity()) {
					JOptionPane.showMessageDialog(MainGUI.this, "The room for this session is already full.", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				// Check if the user has already booked this session
				List<Reservation> userReservations = getBusinessLogic().getReservationsByMember(loggedInUser);
				for (Reservation reservation : userReservations) {
					if (reservation.getSession().equals(selectedSession)) {
						JOptionPane.showMessageDialog(MainGUI.this, "You have already booked this session.", "Error",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
				}

				// Open the SessionDetailsGUI to display session details
				new SessionDetailsGUI(selectedSession, loggedInUser).setVisible(true);
			}
		});
		bottomPanel.add(reserveButton);
		getContentPane().add(bottomPanel, BorderLayout.SOUTH);

		topPanel.add(buttonPanel, BorderLayout.EAST);

		// Load initial data into the table
		loadTableData("All");

		updateUI();
	}

	private void updateUI() {
		if (loggedInUser != null) {
			welcomeLabel.setText("Welcome, " + loggedInUser.getCompleteName() + "!");
			reserveButton.setVisible(true);
			viewProfileButton.setVisible(true);
			logButton.setText("Log Out");
		} else {
			welcomeLabel.setText("Please log in to start training.");
			reserveButton.setVisible(false);
			viewProfileButton.setVisible(false);
			logButton.setText("Log In");
		}
	}

	private void loadTableData(String difficultyFilter) {
		tableModel.setRowCount(0); // Clear the table

		// Fetch all sessions from the facade
		List<Session> sessions = this.getBusinessLogic().getAllSessions();

		// Define date and time formats
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

		// Add rows to the table based on the filter
		for (Session session : sessions) {
			// Skip sessions without an assigned room
			if (session.getRoom() == null) {
				continue;
			}

			int difficulty = session.getActivity().getDifficulty();

			if (difficultyFilter.equals("All") || difficultyFilter.equals(String.valueOf(difficulty))) {
				String formattedDate = dateFormat.format(session.getDate());
				String formattedTime = timeFormat.format(session.getHour());
				tableModel.addRow(
						new Object[] { session.getActivity().getName(), formattedDate, formattedTime, difficulty });
			}
		}
	}
}
