package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.formdev.flatlaf.FlatLightLaf;

import businessLogic.BLFacade;
import domain.Bill;
import domain.Member;
import domain.Reservation;
import domain.Session;
import domain.User;

public class ProfileGUI extends JFrame {
	
	private static final long serialVersionUID = 1L;

	BLFacade facade = MainGUI.getBusinessLogic();

	private JTable reservationTable;
	private DefaultTableModel tableModel;
	private JButton payBillsButton;
	
	private Member loggedInUser;

	private List<Reservation> reservations;

	public ProfileGUI(Member member) {
		super("Profile - " + member.getName());

		try {
			UIManager.setLookAndFeel(new FlatLightLaf());
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		loggedInUser = member;

		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		// Set null layout
		getContentPane().setLayout(null);

		// Get screen dimensions
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = screenSize.width;
		int screenHeight = screenSize.height;

		// Member details panel
		JPanel memberDetailsPanel = new JPanel(null);
		memberDetailsPanel.setBounds(10, 10, screenWidth - 20, 100);
		memberDetailsPanel.setBorder(BorderFactory.createTitledBorder("Member Details"));

		JLabel nameLabel = new JLabel("Name: " + member.getCompleteName());
		nameLabel.setBounds(20, 20, 300, 30);
		memberDetailsPanel.add(nameLabel);

		JLabel documentIDLabel = new JLabel("Document ID: " + member.getDocumentID());
		documentIDLabel.setBounds(20, 50, 300, 30);
		memberDetailsPanel.add(documentIDLabel);

		JLabel emailLabel = new JLabel("Email: " + member.getName());
		emailLabel.setBounds(350, 20, 300, 30);
		memberDetailsPanel.add(emailLabel);

		// Add remaining reservations label
		JLabel remainingReservationsLabel = new JLabel("Remaining Reservations: " + member.getN_leftReservations());
		remainingReservationsLabel.setBounds(350, 50, 300, 30);
		memberDetailsPanel.add(remainingReservationsLabel);

		getContentPane().add(memberDetailsPanel);

		payBillsButton = new JButton("Pay Bills");
		payBillsButton.setBounds((screenWidth - 150) / 2, screenHeight - 100, 150, 30);
		
        updateBillsButtonVisibility();
        
		getContentPane().add(payBillsButton);
		
		payBillsButton.addActionListener(e -> {
			BillsPaymentGUI billsPaymentGUI = new BillsPaymentGUI(member);
			billsPaymentGUI.setVisible(true);
		});
		
		JButton backButton = new JButton("Back");
		backButton.setFont(new Font("Arial", Font.PLAIN, 14));
		backButton.setBounds(1750, 980, 100, 30);
		backButton.addActionListener(e -> dispose()); // Close the current window
		getContentPane().add(backButton);;

		// Initialize table model first
		String[] columnNames = { "Activity Name", "Date", "Hour", "Difficulty" };
		tableModel = new DefaultTableModel(columnNames, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false; // Disable editing for all cells
			}
		};

		// Assign the model to the table
		reservationTable = new JTable(tableModel);

		// Ensure the table is properly configured
		reservationTable.getTableHeader().setReorderingAllowed(false);
		reservationTable.setRowHeight(30);
		reservationTable.setFont(new Font("Arial", Font.PLAIN, 12));
		reservationTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

		// Adjust header appearance
		JTableHeader tableHeader = reservationTable.getTableHeader();
		tableHeader.setFont(new Font("Arial", Font.BOLD, 14));
		tableHeader.setPreferredSize(new Dimension(tableHeader.getPreferredSize().width, 30));

		// Center align table content
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		for (int i = 0; i < reservationTable.getColumnCount(); i++) {
			reservationTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}

		// Add table to scroll pane
		JScrollPane scrollPane = new JScrollPane(reservationTable);
		scrollPane.setBounds(10, 120, 1900, 823);
		getContentPane().add(scrollPane);

		// Load reservation data
		reservations = facade.getReservations(member);
		loadReservationData(reservations);

		setVisible(true);
	}

	private void loadReservationData(List<Reservation> reservations) {
		tableModel.setRowCount(0); // Clear existing rows

		// Define date and time formats
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		
		if (reservations == null || reservations.isEmpty()) {
			System.out.println("No reservations found for the member.");
			return;
		}

		for (Reservation reservation : reservations) {
			Session session = reservation.getSession();
			if (session != null && session.getActivity() != null) {

				tableModel.addRow(new Object[] { session.getActivity().getName(), dateFormat.format(session.getDate()),
						timeFormat.format(session.getHour()), session.getActivity().getDifficulty() });
			} else {
				// Debugging output for invalid data
				System.out.println("Invalid reservation or session data: " + reservation);
			}
		}

		// Refresh the table
		reservationTable.revalidate();
		reservationTable.repaint();
	}
	
    private void updateBillsButtonVisibility() {
        List<Bill> bills = MainGUI.getBusinessLogic().getBillsForMember(loggedInUser);
        boolean hasUnpaidBills = bills.stream().anyMatch(bill -> bill.getStatus()[0].equals("Unpaid"));
        payBillsButton.setEnabled(hasUnpaidBills);
    }
    
}
