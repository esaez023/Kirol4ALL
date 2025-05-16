package gui;

import javax.swing.*;

import com.formdev.flatlaf.FlatLightLaf;

import domain.Member;
import domain.Reservation;
import domain.Session;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SessionDetailsGUI extends JFrame {

	public SessionDetailsGUI(Session session, Member loggedInUser) {
		super("Session Details");

		try {
			UIManager.setLookAndFeel(new FlatLightLaf());
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		// Set the window to full screen
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(null); // No layout manager

		// Calculate screen dimensions
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = screenSize.width;
		int screenHeight = screenSize.height;

		// Add title
		JLabel titleLabel = new JLabel("Session Details", SwingConstants.CENTER);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
		titleLabel.setBounds(screenWidth / 2 - 200, screenHeight / 2 - 200, 400, 40);
		add(titleLabel);

		// Add subtitle
		JLabel subtitleLabel = new JLabel("Here you can find all the details about the selected session.",
				SwingConstants.CENTER);
		subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
		subtitleLabel.setBounds(screenWidth / 2 - 300, screenHeight / 2 - 160, 600, 30);
		add(subtitleLabel);

		// Format session details
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

		String activityName = "Activity Name: " + session.getActivity().getName();
		String date = "Date: " + dateFormat.format(session.getDate());
		String hour = "Hour: " + timeFormat.format(session.getHour());
		String difficulty = "Difficulty: " + session.getActivity().getDifficulty();
		String room = "Room: " + session.getRoom().getRoomNumber();
		String price = "Price: " + session.getActivity().getPrice() + " €";

		// Create labels for session details
		JLabel activityLabel = new JLabel(activityName, SwingConstants.CENTER);
		activityLabel.setFont(new Font("Arial", Font.PLAIN, 16));
		activityLabel.setBounds(screenWidth / 2 - 150, screenHeight / 2 - 120, 300, 30);
		add(activityLabel);

		JLabel dateLabel = new JLabel(date, SwingConstants.CENTER);
		dateLabel.setFont(new Font("Arial", Font.PLAIN, 16));
		dateLabel.setBounds(screenWidth / 2 - 150, screenHeight / 2 - 80, 300, 30);
		add(dateLabel);

		JLabel hourLabel = new JLabel(hour, SwingConstants.CENTER);
		hourLabel.setFont(new Font("Arial", Font.PLAIN, 16));
		hourLabel.setBounds(screenWidth / 2 - 150, screenHeight / 2 - 40, 300, 30);
		add(hourLabel);

		JLabel difficultyLabel = new JLabel(difficulty, SwingConstants.CENTER);
		difficultyLabel.setFont(new Font("Arial", Font.PLAIN, 16));
		difficultyLabel.setBounds(screenWidth / 2 - 150, screenHeight / 2, 300, 30);
		add(difficultyLabel);

		JLabel roomLabel = new JLabel(room, SwingConstants.CENTER);
		roomLabel.setFont(new Font("Arial", Font.PLAIN, 16));
		roomLabel.setBounds(screenWidth / 2 - 150, screenHeight / 2 + 40, 300, 30);
		add(roomLabel);

		JLabel priceLabel = new JLabel(price, SwingConstants.CENTER);
		priceLabel.setFont(new Font("Arial", Font.PLAIN, 16));
		priceLabel.setBounds(screenWidth / 2 - 150, screenHeight / 2 + 80, 300, 30);
		add(priceLabel);

		// Add a reserve button
		JButton reserveButton = new JButton("Reserve");
		reserveButton.setFont(new Font("Arial", Font.PLAIN, 14));
		reserveButton.setBounds(screenWidth / 2 - 50, screenHeight / 2 + 140, 100, 30);
		reserveButton.addActionListener(e -> {
			// Create a reservation
			Reservation reservation = new Reservation(loggedInUser, session,
					"Confirmed", new Date());
			if (MainGUI.getBusinessLogic().makeReservation(reservation) == null) {
				JOptionPane.showMessageDialog(this, "Reservation failed!", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			} else {
				System.out.println("Reservation made successfully!" + reservation);
				JOptionPane.showMessageDialog(this, "Reservation made successfully!", "Success",
						JOptionPane.INFORMATION_MESSAGE);
			}
			dispose();
		});
		add(reserveButton);

		// Add a close button
		JButton closeButton = new JButton("Close");
		closeButton.setFont(new Font("Arial", Font.PLAIN, 14));
		closeButton.setBounds(screenWidth / 2 - 50, screenHeight / 2 + 180, 100, 30);
		closeButton.addActionListener(e -> dispose());
		add(closeButton);

		this.setVisible(true);
	}
}