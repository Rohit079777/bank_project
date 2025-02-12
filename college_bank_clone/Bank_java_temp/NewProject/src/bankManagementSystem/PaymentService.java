package bankManagementSystem;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PaymentService {

    public static void processPayment(JFrame parent) {
        try {
            Conn c = new Conn();

            // Enter Form Number (User Identification)
            String formno = JOptionPane.showInputDialog(parent, "Enter your Form Number:");
            if (formno == null || formno.isEmpty()) return;

            // Check if the Form Number exists in rg1 table
            String formnoQuery = "SELECT * FROM rg1 WHERE formno = ?";
            PreparedStatement ps = c.c.prepareStatement(formnoQuery);
            ps.setString(1, formno);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                JOptionPane.showMessageDialog(parent, "User not found! Invalid Form Number.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Select Payment Type
            String[] paymentTypes = {"Utility Bill Payment", "Mobile/DTH Recharge", "Credit Card Payment"};
            String paymentType = (String) JOptionPane.showInputDialog(parent, "Select Payment Type:",
                    "Payment Services", JOptionPane.QUESTION_MESSAGE, null, paymentTypes, paymentTypes[0]);

            if (paymentType == null) return;

            // Enter Payment Amount
            String amountStr = JOptionPane.showInputDialog(parent, "Enter Payment Amount:");
            if (amountStr == null || amountStr.isEmpty()) return;
            double amount = Double.parseDouble(amountStr);

            // Check if User has sufficient balance (for example, 1000 is the minimum balance)
            String balanceQuery = "SELECT income FROM rg2 WHERE formno = ?";
            ps = c.c.prepareStatement(balanceQuery);
            ps.setString(1, formno);
            rs = ps.executeQuery();

            if (rs.next()) {
                double balance = Double.parseDouble(rs.getString("income").replaceAll("[^0-9]", "")); // Extract balance from income field
                if (balance < amount) {
                    JOptionPane.showMessageDialog(parent, "Insufficient balance!", "Payment Failed", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Insert Payment Details into Database
                String insertPayment = "INSERT INTO payments (formno, payment_type, amount) VALUES (?, ?, ?)";
                ps = c.c.prepareStatement(insertPayment);
                ps.setString(1, formno);
                ps.setString(2, paymentType);
                ps.setDouble(3, amount);
                ps.executeUpdate();

                // Get the current date and format it to show only the date (without time)
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = dateFormat.format(new Date());

                // Show Confirmation Message
                JOptionPane.showMessageDialog(parent, "Payment Successful!\n" +
                        "Payment Type: " + paymentType + "\n" +
                        "Amount: ₹" + amount + "\n" +
                        "Date: " + formattedDate,
                        "Payment Successful", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(parent, "User not found! Invalid Form Number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(parent, "Error processing payment: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
