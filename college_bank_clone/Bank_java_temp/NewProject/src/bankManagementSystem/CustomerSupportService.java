package bankManagementSystem;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CustomerSupportService {

    public static void submitSupportRequest(JFrame parent) {
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

            // Enter the Issue or Query
            String issue = JOptionPane.showInputDialog(parent, "Enter your issue or query:");
            if (issue == null || issue.isEmpty()) return;

            // Insert Support Request into Database
            String insertRequest = "INSERT INTO support_requests (formno, issue) VALUES (?, ?)";
            ps = c.c.prepareStatement(insertRequest);
            ps.setString(1, formno);
            ps.setString(2, issue);
            ps.executeUpdate();

            // Show Confirmation Message
            JOptionPane.showMessageDialog(parent, "Your support request has been submitted successfully.\n" +
                    "Our customer support team will get back to you soon.", 
                    "Support Request Submitted", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(parent, "Error submitting support request: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void processPayment(AccountServices accountServices) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'processPayment'");
    }

    public static void processCustomer(AccountServices accountServices) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'processCustomer'");
    }
}
