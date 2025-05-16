package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.formdev.flatlaf.FlatLightLaf;

import businessLogic.BLFacade;
import domain.Bill;
import domain.Member;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class BillsPaymentGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private BLFacade facade = MainGUI.getBusinessLogic();
	private JTable billsTable;
	private DefaultTableModel tableModel;

	private JButton payButton;
	private JLabel totalAmountLabel;

	public BillsPaymentGUI(Member member) {
		try {
			UIManager.setLookAndFeel(new FlatLightLaf());
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		setTitle("Bills for " + member.getCompleteName());
		setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = screenSize.width;
		int screenHeight = screenSize.height;

		JLabel titleLabel = new JLabel("Bills for " + member.getCompleteName(), SwingConstants.CENTER);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
		titleLabel.setBounds((screenWidth - 400) / 2, 20, 400, 40);
		add(titleLabel);

		String[] columnNames = { "Code", "Date", "Amount (€)", "Status", "Session Cost (€)" };
		tableModel = new DefaultTableModel(columnNames, 0);
		billsTable = new JTable(tableModel);

		billsTable.setRowHeight(30);
		billsTable.setFont(new Font("Arial", Font.PLAIN, 14));
		billsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
		billsTable.getTableHeader().setReorderingAllowed(false);

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		for (int i = 0; i < billsTable.getColumnCount(); i++) {
			billsTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}

		JScrollPane scrollPane = new JScrollPane(billsTable);
		scrollPane.setBounds(50, 80, screenWidth - 100, screenHeight - 300);
		add(scrollPane);

		// Initialize totalAmountLabel before calling loadBillsData
		totalAmountLabel = new JLabel("Total Amount to Pay: €0.00", SwingConstants.CENTER);
		totalAmountLabel.setFont(new Font("Arial", Font.BOLD, 16));
		totalAmountLabel.setBounds((screenWidth - 400) / 2, screenHeight - 200, 400, 30);
		add(totalAmountLabel);

		// Load bills data after initializing totalAmountLabel
		List<Bill> bills = facade.getBillsForMember(member);
		loadBillsData(bills);

		payButton = new JButton("Pay");
		payButton.setFont(new Font("Arial", Font.PLAIN, 14));
		payButton.setBounds((screenWidth - 200) / 2, screenHeight - 150, 100, 30);
		payButton.addActionListener(e -> {
			int selectedRow = billsTable.getSelectedRow();
			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(this, "Please select a bill to pay.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			int billCode = (int) tableModel.getValueAt(selectedRow, 0);
			Bill selectedBill = bills.stream().filter(bill -> bill.getCode() == billCode).findFirst().orElse(null);

			if (selectedBill == null) {
				JOptionPane.showMessageDialog(this, "Selected bill could not be found.", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			if (selectedBill.getStatus()[0].equals("Paid")) {
				JOptionPane.showMessageDialog(this, "This bill is already paid.", "Info",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			selectedBill.setStatus(new String[] { "Paid" });
			facade.updateBill(selectedBill); // Persist the change in the database

			JOptionPane.showMessageDialog(this, "Bill paid successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
			loadBillsData(facade.getBillsForMember(member)); // Refresh the table
		});
		add(payButton);

		JButton closeButton = new JButton("Close");
		closeButton.setFont(new Font("Arial", Font.PLAIN, 14));
		closeButton.setBounds((screenWidth - 100) / 2, screenHeight - 100, 100, 30);
		closeButton.addActionListener(e -> dispose());
		add(closeButton);

		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setVisible(true);
	}

	private void loadBillsData(List<Bill> bills) {
		tableModel.setRowCount(0);
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		float totalAmount = 0;

		for (Bill bill : bills) {
			float sessionCost = bill.getAmount(); // Assuming the bill amount is the session cost
			totalAmount += sessionCost;

			tableModel.addRow(new Object[] { bill.getCode(), dateFormat.format(bill.getDate()), bill.getAmount(),
					bill.getStatus()[0], sessionCost });
		}

		totalAmountLabel.setText(String.format("Total Amount to Pay: €%.2f", totalAmount));
	}
}