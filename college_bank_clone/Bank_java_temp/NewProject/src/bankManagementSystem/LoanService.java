package bankManagementSystem;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoanService {

    public static void processLoan(JFrame parent) {
        try {
            Conn c = new Conn();

            // Select Loan Type
            String[] loanTypes = {"Personal Loan", "Home Loan", "Car Loan"};
            String loanType = (String) JOptionPane.showInputDialog(parent, "Select Loan Type:",
                    "Loan Services", JOptionPane.QUESTION_MESSAGE, null, loanTypes, loanTypes[0]);

            if (loanType == null) return;

            // Enter Loan Amount
            String amountStr = JOptionPane.showInputDialog(parent, "Enter Loan Amount:");
            if (amountStr == null || amountStr.isEmpty()) return;
            double amount = Double.parseDouble(amountStr);

            // Enter Tenure (Years)
            String tenureStr = JOptionPane.showInputDialog(parent, "Enter Loan Tenure (Years):");
            if (tenureStr == null || tenureStr.isEmpty()) return;
            int tenure = Integer.parseInt(tenureStr);

            // Interest Rate (Example: Personal Loan - 12%, Home Loan - 8%, Car Loan - 10%)
            double interestRate = loanType.equals("Personal Loan") ? 12.0 :
                                  loanType.equals("Home Loan") ? 8.0 : 10.0;

            // Calculate Monthly EMI using Formula: EMI = [P * R * (1+R)^N] / [(1+R)^N - 1]
            double monthlyInterest = interestRate / (12 * 100);
            int months = tenure * 12;
            double emi = (amount * monthlyInterest * Math.pow(1 + monthlyInterest, months)) / 
                         (Math.pow(1 + monthlyInterest, months) - 1);
            double totalRepayment = emi * months;

            // Get Form Number (User Identification)
            String formno = JOptionPane.showInputDialog(parent, "Enter your Form Number:");
            if (formno == null || formno.isEmpty()) return;

            // Check Eligibility (Example: Minimum Salary for Personal Loan = 15,000)
            String incomeQuery = "SELECT income FROM rg2 WHERE formno = ?";
            PreparedStatement ps = c.c.prepareStatement(incomeQuery);
            ps.setString(1, formno);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int income = Integer.parseInt(rs.getString("income").replaceAll("[^0-9]", "")); // Convert to Integer
                if (income < 15000) {
                    JOptionPane.showMessageDialog(parent, "Loan Rejected! Minimum income required: ₹15,000", 
                                                  "Loan Services", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Insert Loan Details into Database
                String insertLoan = "INSERT INTO loans (formno, loan_type, amount, tenure, repayment) VALUES (?, ?, ?, ?, ?)";
                ps = c.c.prepareStatement(insertLoan);
                ps.setString(1, formno);
                ps.setString(2, loanType);
                ps.setDouble(3, amount);
                ps.setInt(4, tenure);
                ps.setDouble(5, totalRepayment);
                ps.executeUpdate();

                // Show Confirmation Message
                JOptionPane.showMessageDialog(parent, "Loan Approved!\n" +
                        "Loan Type: " + loanType + "\n" +
                        "Loan Amount: ₹" + amount + "\n" +
                        "Interest Rate: " + interestRate + "%\n" +
                        "Tenure: " + tenure + " years\n" +
                        "EMI: ₹" + String.format("%.2f", emi) + "\n" +
                        "Total Repayment: ₹" + String.format("%.2f", totalRepayment),
                        "Loan Approved", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(parent, "User not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(parent, "Error processing loan: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
