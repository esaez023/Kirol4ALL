package gui;

import javax.swing.*;
import com.formdev.flatlaf.FlatLightLaf;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import businessLogic.BLFacade;
import domain.Member;

public class CreateUserGUI extends JFrame {

	private JLabel titleLabel;
	private JLabel infoLabel;
	private JTextField emailField;
	private JPasswordField passwordField;
	private JTextField completeNameField;
	private JTextField documentIDField;
	private JButton createButton;
	private JButton backButton;

	private BLFacade facade;

	public CreateUserGUI() {
	    setTitle("Create User");
	    try {
	        UIManager.setLookAndFeel(new FlatLightLaf());
	    } catch (UnsupportedLookAndFeelException e) {
	        e.printStackTrace();
	    }

	    facade = MainGUI.getBusinessLogic();

	    setExtendedState(JFrame.MAXIMIZED_BOTH);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setLayout(null); // Use null layout

	    // Title label
	    titleLabel = new JLabel("Create User", SwingConstants.CENTER);
	    titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
	    add(titleLabel);

	    // Info label
	    infoLabel = new JLabel("Fill in the fields below to create a new user.", SwingConstants.CENTER);
	    infoLabel.setFont(new Font("Arial", Font.PLAIN, 16));
	    add(infoLabel);

	    // Existing components
	    JLabel emailLabel = new JLabel("Email:");
	    emailField = new JTextField();

	    JLabel passwordLabel = new JLabel("Password:");
	    passwordField = new JPasswordField();

	    JLabel completeNameLabel = new JLabel("Complete Name:");
	    completeNameField = new JTextField();

	    JLabel documentIDLabel = new JLabel("Document ID:");
	    documentIDField = new JTextField();

	    createButton = new JButton("Create User");
	    backButton = new JButton("Back");

	    add(emailLabel);
	    add(emailField);
	    add(passwordLabel);
	    add(passwordField);
	    add(completeNameLabel);
	    add(completeNameField);
	    add(documentIDLabel);
	    add(documentIDField);
	    add(createButton);
	    add(backButton);

	    // Center components dynamically
	    Dimension screenSize = getToolkit().getScreenSize();
	    int frameWidth = screenSize.width;
	    int frameHeight = screenSize.height;

	    int labelWidth = 150;
	    int labelHeight = 30;
	    int fieldWidth = 300;
	    int fieldHeight = 30;
	    int buttonWidth = 150;
	    int buttonHeight = 40;
	    int verticalSpacing = 20;

	    int centerX = frameWidth / 2;
	    int startY = frameHeight / 2 - 3 * (labelHeight + verticalSpacing);

	    titleLabel.setBounds(centerX - 200, startY - 100, 400, 30);
	    infoLabel.setBounds(centerX - 250, startY - 60, 500, 20);

	    emailLabel.setBounds(centerX - fieldWidth / 2 - labelWidth, startY, labelWidth, labelHeight);
	    emailField.setBounds(centerX - fieldWidth / 2, startY, fieldWidth, fieldHeight);

	    passwordLabel.setBounds(centerX - fieldWidth / 2 - labelWidth, startY + labelHeight + verticalSpacing, labelWidth, labelHeight);
	    passwordField.setBounds(centerX - fieldWidth / 2, startY + labelHeight + verticalSpacing, fieldWidth, fieldHeight);

	    completeNameLabel.setBounds(centerX - fieldWidth / 2 - labelWidth, startY + 2 * (labelHeight + verticalSpacing), labelWidth, labelHeight);
	    completeNameField.setBounds(centerX - fieldWidth / 2, startY + 2 * (labelHeight + verticalSpacing), fieldWidth, fieldHeight);

	    documentIDLabel.setBounds(centerX - fieldWidth / 2 - labelWidth, startY + 3 * (labelHeight + verticalSpacing), labelWidth, labelHeight);
	    documentIDField.setBounds(centerX - fieldWidth / 2, startY + 3 * (labelHeight + verticalSpacing), fieldWidth, fieldHeight);

	    createButton.setBounds(centerX - buttonWidth / 2, startY + 4 * (labelHeight + verticalSpacing), buttonWidth, buttonHeight);
	    backButton.setBounds(centerX - buttonWidth / 2, startY + 5 * (labelHeight + verticalSpacing), buttonWidth, buttonHeight);

	    // Add action listeners (existing code)
	    createButton.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            // Existing action logic
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
