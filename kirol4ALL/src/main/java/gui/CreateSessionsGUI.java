package gui;

import javax.swing.*;
import businessLogic.BLFacade;
import domain.Session;
import domain.Activity;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CreateSessionsGUI extends JFrame {

	private BLFacade facade = MainGUI.getBusinessLogic();

	private JComboBox<String> activityComboBox;
	private JComboBox<String> dayComboBox;
	private JSpinner timeSpinner;
	private JButton addButton;
	private JButton backButton;
	private JLabel titleLabel;
	private JLabel infoLabel;

	public CreateSessionsGUI() {
	    setTitle("Add Session to Activity");
	    setExtendedState(JFrame.MAXIMIZED_BOTH);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setLayout(null);

	    // Title label
	    titleLabel = new JLabel("Create Sessions", SwingConstants.CENTER);
	    titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
	    add(titleLabel);

	    // Info label
	    infoLabel = new JLabel("Fill in the fields below to create a new session for an activity.", SwingConstants.CENTER);
	    infoLabel.setFont(new Font("Arial", Font.PLAIN, 16));
	    add(infoLabel);

	    initializeComponents();
	    addEventListeners();
	    setVisible(true);

	    // Center title and info dynamically
	    addComponentListener(new java.awt.event.ComponentAdapter() {
	        @Override
	        public void componentResized(java.awt.event.ComponentEvent e) {
	            int frameWidth = getWidth();
	            int titleWidth = 400;
	            int titleHeight = 30;
	            int infoWidth = 500;
	            int infoHeight = 20;

	            titleLabel.setBounds((frameWidth - titleWidth) / 2, 20, titleWidth, titleHeight);
	            infoLabel.setBounds((frameWidth - infoWidth) / 2, 60, infoWidth, infoHeight);
	        }
	    });
	}


	private void initializeComponents() {
		// Activity selection
		JLabel activityLabel = new JLabel("Select Activity:");
		activityComboBox = new JComboBox<>(facade.getActivities().toArray(new String[0]));
		add(activityLabel);
		add(activityComboBox);

		// Day selection
		JLabel dayLabel = new JLabel("Select Day (Next Week):");
		dayComboBox = new JComboBox<>(getNextWeekDays());
		add(dayLabel);
		add(dayComboBox);

		// Time input
		JLabel timeLabel = new JLabel("Select Time:");
		SpinnerDateModel timeModel = new SpinnerDateModel();
		timeModel.setCalendarField(Calendar.MINUTE);
		timeSpinner = new JSpinner(timeModel);
		JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
		timeSpinner.setEditor(timeEditor);
		add(timeLabel);
		add(timeSpinner);

		// Buttons
		addButton = new JButton("Add Session");
		backButton = new JButton("Back");
		add(addButton);
		add(backButton);

		// Center components dynamically
		addComponentListener(new java.awt.event.ComponentAdapter() {
			@Override
			public void componentResized(java.awt.event.ComponentEvent e) {
				centerComponents(activityLabel, activityComboBox, dayLabel, dayComboBox, timeLabel, timeSpinner,
						addButton, backButton);
			}
		});
	}

	private void addEventListeners() {
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handleAddSession();
			}
		});

		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}

	private void handleAddSession() {
		String selectedActivity = (String) activityComboBox.getSelectedItem();
		String selectedDay = (String) dayComboBox.getSelectedItem();
		Date hour = (Date) timeSpinner.getValue();

		if (selectedActivity != null && selectedDay != null && hour != null) {
			try {
				SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
				Date date = dateFormat.parse(selectedDay);

				Activity activity = facade.getActivityByName(selectedActivity);
				if (activity != null) {
					Session session = new Session(date, hour, activity, null, null);
					facade.createSession(session);

					JOptionPane.showMessageDialog(this,
							"Session created successfully for activity: " + selectedActivity);
				} else {
					JOptionPane.showMessageDialog(this, "Activity not found.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Invalid date or time format.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void centerComponents(JLabel activityLabel, JComboBox<String> activityComboBox, JLabel dayLabel,
			JComboBox<String> dayComboBox, JLabel timeLabel, JSpinner timeSpinner, JButton addButton,
			JButton backButton) {
		int frameWidth = getWidth();
		int frameHeight = getHeight();

		int labelWidth = 200;
		int labelHeight = 30;
		int fieldWidth = 300;
		int fieldHeight = 30;
		int buttonWidth = 150;
		int buttonHeight = 30;
		int spacing = 20;

		int centerX = frameWidth / 2;
		int centerY = frameHeight / 2;

		activityLabel.setBounds(centerX - fieldWidth / 2 - labelWidth, centerY - 3 * (labelHeight + spacing),
				labelWidth, labelHeight);
		activityComboBox.setBounds(centerX - fieldWidth / 2, centerY - 3 * (labelHeight + spacing), fieldWidth,
				fieldHeight);

		dayLabel.setBounds(centerX - fieldWidth / 2 - labelWidth, centerY - 2 * (labelHeight + spacing), labelWidth,
				labelHeight);
		dayComboBox.setBounds(centerX - fieldWidth / 2, centerY - 2 * (labelHeight + spacing), fieldWidth, fieldHeight);

		timeLabel.setBounds(centerX - fieldWidth / 2 - labelWidth, centerY - labelHeight - spacing, labelWidth,
				labelHeight);
		timeSpinner.setBounds(centerX - fieldWidth / 2, centerY - labelHeight - spacing, fieldWidth, fieldHeight);

		addButton.setBounds(centerX - buttonWidth / 2, centerY + spacing, buttonWidth, buttonHeight);
		backButton.setBounds(centerX - buttonWidth / 2, centerY + 2 * (buttonHeight + spacing), buttonWidth,
				buttonHeight);
	}

	private String[] getNextWeekDays() {
		String[] days = new String[7];
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, 1);
		for (int i = 0; i < 7; i++) {
			days[i] = calendar.getTime().toString();
			calendar.add(Calendar.DAY_OF_YEAR, 1);
		}
		return days;
	}
}
