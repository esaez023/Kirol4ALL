package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.*;

import com.formdev.flatlaf.FlatLightLaf;

import businessLogic.BLFacade;
import domain.Bill;
import domain.Manager;
import domain.Member;
import domain.Reservation;
import domain.User;

public class ManagementGUI extends JFrame {

	private static final long serialVersionUID = 1L;

	BLFacade facade = MainGUI.getBusinessLogic();

	public ManagementGUI(Manager manager) {

		try {
			UIManager.setLookAndFeel(new FlatLightLaf());
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		// Set the window to full screen
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Main container with null layout
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(null);
		this.setContentPane(mainPanel);

		// Get screen dimensions
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = screenSize.width;
		int screenHeight = screenSize.height;

		// Title at the top
		JLabel titleLabel = new JLabel("Management Panel", SwingConstants.CENTER);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
		titleLabel.setBounds((screenWidth - 300) / 2, 50, 300, 30); // Centered
		mainPanel.add(titleLabel);

		// Welcome text
		JLabel welcomeLabel = new JLabel("Welcome, " + manager.getCompleteName() + "!");
		welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 16));
		welcomeLabel.setBounds((screenWidth - 300) / 2, 100, 300, 30); // Centered
		mainPanel.add(welcomeLabel);

		// Buttons positioned in the center
		int buttonWidth = 150;
		int buttonHeight = 30;
		int buttonX = (screenWidth - buttonWidth) / 2; // Center horizontally

		JButton createUserButton = new JButton("Create Users");
		createUserButton.setBounds(buttonX, 200, buttonWidth, buttonHeight);
		mainPanel.add(createUserButton);

		JButton manageUserButton = new JButton("Manage Users");
		manageUserButton.setBounds(buttonX, 250, buttonWidth, buttonHeight);
		mainPanel.add(manageUserButton);

		JButton manageRoomsButton = new JButton("Manage Rooms");
		manageRoomsButton.setBounds(buttonX, 300, buttonWidth, buttonHeight);
		mainPanel.add(manageRoomsButton);

		JButton createSessionsButton = new JButton("Create Sessions");
		createSessionsButton.setBounds(buttonX, 350, buttonWidth, buttonHeight);
		mainPanel.add(createSessionsButton);

		JButton logOutButton = new JButton("Log Out");
		logOutButton.setBounds(buttonX, 400, buttonWidth, buttonHeight);
		mainPanel.add(logOutButton);

		JButton sendBillsButton = new JButton("Send Bills");
		sendBillsButton.setBounds(buttonX, 450, buttonWidth, buttonHeight);
		mainPanel.add(sendBillsButton);

		// Add action listener for the "Send Bills" button
		sendBillsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				java.util.List<User> users = facade.getAllUsers();
				Date today = new Date();
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(today);
				calendar.add(Calendar.DAY_OF_YEAR, -7);
				Date oneWeekAgo = calendar.getTime();

				for (User user : users) {
					if (user instanceof Member) {
						Member member = (Member) user;
						java.util.List<Reservation> memberReservations = facade.getReservations(member);
						float totalAmount = 0;

						for (Reservation reservation : memberReservations) {
							Date reservationDate = reservation.getReservationDate();
							if (reservationDate.after(oneWeekAgo)) {
								totalAmount += reservation.getSession().getActivity().getPrice();
							}
						}

						if (totalAmount > 0) {
							String formattedDate = new SimpleDateFormat("ddMMyyyy").format(new Date());
							String billCode = member.getCompleteName().replaceAll("\\s+", "") + formattedDate;
							Bill bill = new Bill(member, billCode.hashCode(), new Date(), totalAmount);

							// Persist the bill
							facade.createBill(bill);
						}
					}
				}

				JOptionPane.showMessageDialog(ManagementGUI.this,
						"Bills have been sent to all members with reservations this week.", "Success",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});

		// Add action listeners for buttons
		createUserButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new CreateUserGUI().setVisible(true);
			}
		});

		manageUserButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new ManageUsersGUI().setVisible(true);
			}
		});

		manageRoomsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new ManageRoomsGUI().setVisible(true);
			}
		});

		createSessionsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new CreateSessionsGUI().setVisible(true);
			}
		});

		logOutButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new MainGUI(null).setVisible(true);
				dispose(); // Close the management window
			}
		});
	}
}
