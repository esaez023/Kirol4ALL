package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.List;

import businessLogic.BLFacade;
import domain.Room;
import domain.Session;

public class ManageRoomsGUI extends JFrame {

	private JList<String> roomList;
	private JList<String> sessionList;
	private JButton assignButton;
	private JButton backButton;
	public static JLabel titleLabel;
	public static JLabel subtitleLabel;
	private JPanel mainPanel;

	private BLFacade facade;

	public ManageRoomsGUI() {
		setTitle("Manage Rooms");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setLayout(null);

		// Initialize main panel
		mainPanel = new JPanel();
		mainPanel.setLayout(null);
		setContentPane(mainPanel);

		facade = MainGUI.getBusinessLogic();

		initializeComponents();
		addEventListeners();
		setVisible(true);
	}

	private void initializeComponents() {
		// Fetch rooms and sessions
		List<Room> rooms = facade.getRooms();
		List<Session> sessions = facade.getAllSessions();

		// Get screen dimensions
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = screenSize.width;

		// Configure and add title label
		titleLabel = new JLabel("Manage Rooms", SwingConstants.CENTER);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
		int titleWidth = 300;
		int titleHeight = 30;
		titleLabel.setBounds((screenWidth - titleWidth) / 2, 20, titleWidth, titleHeight);
		mainPanel.add(titleLabel);

		// Configure and add subtitle label
		subtitleLabel = new JLabel("Assign sessions to available rooms", SwingConstants.CENTER);
		subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 18));
		int subtitleWidth = 400;
		int subtitleHeight = 30;
		subtitleLabel.setBounds((screenWidth - subtitleWidth) / 2, 60, subtitleWidth, subtitleHeight);
		mainPanel.add(subtitleLabel);

		// Room list
		JLabel roomLabel = new JLabel("Rooms:");
		mainPanel.add(roomLabel);

		DefaultListModel<String> roomModel = new DefaultListModel<>();
		for (Room room : rooms) {
			roomModel.addElement(room.getRoomNumber() + " (Capacity: " + room.getCapacity() + ")");
		}
		roomList = new JList<>(roomModel);
		JScrollPane roomScrollPane = new JScrollPane(roomList);
		mainPanel.add(roomScrollPane);

		// Session list
		JLabel sessionLabel = new JLabel("Sessions:");
		mainPanel.add(sessionLabel);

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

		DefaultListModel<String> sessionModel = new DefaultListModel<>();
		for (Session session : sessions) {
			// Only include sessions with a null room
			if (session.getRoom() == null) {
				String formattedDate = dateFormat.format(session.getDate());
				String formattedTime = timeFormat.format(session.getHour());
				sessionModel
						.addElement(session.getActivity().getName() + " on " + formattedDate + " at " + formattedTime);
			}
		}
		sessionList = new JList<>(sessionModel);
		JScrollPane sessionScrollPane = new JScrollPane(sessionList);
		mainPanel.add(sessionScrollPane);

		// Buttons
		assignButton = new JButton("Assign Session to Room");
		mainPanel.add(assignButton);

		backButton = new JButton("Back");
		mainPanel.add(backButton);

		// Center components dynamically
		addComponentListener(new java.awt.event.ComponentAdapter() {
			@Override
			public void componentResized(java.awt.event.ComponentEvent e) {
				int frameWidth = getWidth();
				int frameHeight = getHeight();

				int labelWidth = 200;
				int labelHeight = 30;
				int listWidth = 300;
				int listHeight = 400;
				int buttonWidth = 200;
				int buttonHeight = 30;
				int spacing = 50;

				int centerX = frameWidth / 2;
				int centerY = frameHeight / 2;

				roomLabel.setBounds(centerX - listWidth - spacing - labelWidth / 2,
						centerY - listHeight / 2 - labelHeight + 100, labelWidth, labelHeight);
				roomScrollPane.setBounds(centerX - listWidth - spacing, centerY - listHeight / 2 + 100, listWidth,
						listHeight);

				sessionLabel.setBounds(centerX + spacing - labelWidth / 2, centerY - listHeight / 2 - labelHeight + 100,
						labelWidth, labelHeight);
				sessionScrollPane.setBounds(centerX + spacing, centerY - listHeight / 2 + 100, listWidth, listHeight);

				assignButton.setBounds(centerX - buttonWidth / 2, centerY + listHeight / 2 + spacing + 100, buttonWidth,
						buttonHeight);
				backButton.setBounds(centerX - buttonWidth / 2, centerY + listHeight / 2 + spacing + buttonHeight + 110,
						buttonWidth, buttonHeight);
			}
		});
	}

	private void addEventListeners() {
		assignButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRoomIndex = roomList.getSelectedIndex();
				int selectedSessionIndex = sessionList.getSelectedIndex();

				if (selectedRoomIndex != -1 && selectedSessionIndex != -1) {
					Room selectedRoom = facade.getRooms().get(selectedRoomIndex);
					Session selectedSession = facade.getAllSessions().stream()
							.filter(session -> session.getRoom() == null).toList().get(selectedSessionIndex);

					// Check if another session with the same date and hour is already assigned to
					// the room
					List<Session> allSessions = facade.getAllSessions();
					for (Session session : allSessions) {
						if (session.getRoom() != null && session.getRoom().equals(selectedRoom)
								&& session.getDate().equals(selectedSession.getDate())
								&& session.getHour().equals(selectedSession.getHour())) {
							JOptionPane.showMessageDialog(ManageRoomsGUI.this,
									"The selected room is already assigned to another session at the same date and time.",
									"Error", JOptionPane.ERROR_MESSAGE);
							return;
						}
					}

					// Assign the room to the session
					facade.assignSessionToRoom(selectedSession, selectedRoom);
					JOptionPane.showMessageDialog(ManageRoomsGUI.this, "Session assigned to room successfully!");

					// Refresh the session list to remove the assigned session
					DefaultListModel<String> sessionModel = (DefaultListModel<String>) sessionList.getModel();
					sessionModel.remove(selectedSessionIndex);
				} else {
					JOptionPane.showMessageDialog(ManageRoomsGUI.this, "Please select both a room and a session.",
							"Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose(); // Close the current window
			}
		});
	}
}
