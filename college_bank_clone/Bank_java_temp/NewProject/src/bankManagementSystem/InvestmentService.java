package bankManagementSystem;

import javax.swing.*;
import java.sql.PreparedStatement;

public class InvestmentService {

    public static void processInvestment(JFrame parent) { // Make the method static
        try {
            Conn c = new Conn();

            // Select Investment Type
            String[] investmentTypes = {"Fixed Deposit", "Mutual Funds", "Stocks"};
            String investmentType = (String) JOptionPane.showInputDialog(parent, "Select Investment Type:",
                    "Investment Services", JOptionPane.QUESTION_MESSAGE, null, investmentTypes, investmentTypes[0]);

            if (investmentType == null) return;

            // Enter Investment Amount
            String amountStr = JOptionPane.showInputDialog(parent, "Enter Investment Amount:");
            if (amountStr == null || amountStr.isEmpty()) return;
            double amount = Double.parseDouble(amountStr);

            // Enter Tenure (Years)
            String tenureStr = JOptionPane.showInputDialog(parent, "Enter Investment Tenure (Years):");
            if (tenureStr == null || tenureStr.isEmpty()) return;
            int tenure = Integer.parseInt(tenureStr);

            // Interest Rate (Example: FD - 6%, Mutual Funds - 12%, Stocks - 15%)
            double interestRate = investmentType.equals("Fixed Deposit") ? 6.0 :
                    investmentType.equals("Mutual Funds") ? 12.0 : 15.0;

            // Calculate Expected Returns using Formula: A = P * (1 + R/100)^N
            double expectedReturn = amount * Math.pow(1 + (interestRate / 100), tenure);

            // Get Form Number
            String formno = JOptionPane.showInputDialog(parent, "Enter your Form Number:");
            if (formno == null || formno.isEmpty()) return;

            // Insert Investment Data into Database
            String insertInvestment = "INSERT INTO investments (formno, investment_type, amount, tenure, expected_return) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = c.c.prepareStatement(insertInvestment);
            ps.setString(1, formno);
            ps.setString(2, investmentType);
            ps.setDouble(3, amount);
            ps.setInt(4, tenure);
            ps.setDouble(5, expectedReturn);
            ps.executeUpdate();

            // Show Confirmation Message
            JOptionPane.showMessageDialog(parent, "Investment Successful!\n" +
                            "Investment Type: " + investmentType + "\n" +
                            "Investment Amount: ₹" + amount + "\n" +
                            "Interest Rate: " + interestRate + "%\n" +
                            "Tenure: " + tenure + " years\n" +
                            "Expected Return: ₹" + String.format("%.2f", expectedReturn),
                    "Investment Confirmation", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(parent, "Error processing investment: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
