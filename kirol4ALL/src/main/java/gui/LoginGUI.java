package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.formdev.flatlaf.FlatLightLaf;

import businessLogic.BLFacade;
import domain.User;
import domain.Manager;
import domain.Member;

public class LoginGUI extends JFrame {

	private static final long serialVersionUID = 1L;

	BLFacade facade = MainGUI.getBusinessLogic();

	public LoginGUI() {
		super("Login - Kirol4ALL");

		// Apply FlatLaf look-and-feel
		try {
			UIManager.setLookAndFeel(new FlatLightLaf());
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		// Set up the main layout
		getContentPane().setLayout(new BorderLayout());

		// Title label
		JLabel titleLabel = new JLabel("Kirol4ALL - Login", SwingConstants.CENTER);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
		titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0)); // Add space above and below the title
		getContentPane().add(titleLabel, BorderLayout.NORTH);

		// Center panel for input fields
		JPanel centerPanel = new JPanel(new GridLayout(3, 1, 10, 10));
		centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

		// Username field
		JPanel usernamePanel = new JPanel(new BorderLayout());
		JLabel usernameLabel = new JLabel("Username:");
		usernameLabel.setFont(new Font("Arial", Font.BOLD, 18));
		JTextField usernameField = new JTextField();
		usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
		usernamePanel.add(usernameLabel, BorderLayout.NORTH);
		usernamePanel.add(usernameField, BorderLayout.CENTER);
		centerPanel.add(usernamePanel);

		// Password field
		JPanel passwordPanel = new JPanel(new BorderLayout());
		JLabel passwordLabel = new JLabel("Password:");
		passwordLabel.setFont(new Font("Arial", Font.BOLD, 18));
		JPasswordField passwordField = new JPasswordField();
		passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
		passwordPanel.add(passwordLabel, BorderLayout.NORTH);
		passwordPanel.add(passwordField, BorderLayout.CENTER);
		centerPanel.add(passwordPanel);

		getContentPane().add(centerPanel, BorderLayout.CENTER);

		// Bottom panel with "Log In" button
		JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
		JButton loginButton = new JButton("Log In");
		loginButton.setFont(new Font("Arial", Font.PLAIN, 14));
		loginButton.setPreferredSize(new Dimension(100, 30));
		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String username = usernameField.getText();
				String password = new String(passwordField.getPassword());
				if (username.isEmpty() || password.isEmpty()) {
					JOptionPane.showMessageDialog(LoginGUI.this, "Please enter both username and password.", "Error",
							JOptionPane.ERROR_MESSAGE);
				} else {
					User user = facade.getUserById(usernameField.getText());
					if (user == null || !user.getPassword().equals(password)) {
						JOptionPane.showMessageDialog(LoginGUI.this, "Invalid username or password.", "Error",
								JOptionPane.ERROR_MESSAGE);
					} else {
						if (user instanceof Member) {
							// Open Main GUI for Member
							MainGUI mainGUI = new MainGUI(user);
							mainGUI.setVisible(true);
							LoginGUI.this.dispose(); // Close the login window
						} else {
							ManagementGUI managementGUI = new ManagementGUI((Manager) user);
							managementGUI.setVisible(true);
							LoginGUI.this.dispose(); // Close the login window
						}
					}
				}
			}
		});
		bottomPanel.add(loginButton);

		getContentPane().add(bottomPanel, BorderLayout.SOUTH);

		// Set up the frame
		setSize(560, 420);
		setLocationRelativeTo(null); // Center the window
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void setVisible(Boolean visible) {
		this.setVisible(visible);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new LoginGUI().setVisible(true);
		});
	}
}