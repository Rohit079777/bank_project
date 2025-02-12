package bankManagementSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Login extends JFrame implements ActionListener {
    JButton login, clear, rg;
    JTextField card;
    JPasswordField Ipin;

    Login() {
        setTitle("Global Bank Management System");
        setLayout(null);

        JLabel text = new JLabel(" Welcome To Global Bank ");
        text.setFont(new Font("Arial", Font.BOLD, 38));
        text.setBounds(200, 50, 600, 100);
        add(text);

        JLabel text1 = new JLabel(" (USER LOGIN PAGE) ");
        text1.setFont(new Font("Arial", Font.BOLD, 15));
        text1.setBounds(330, 60, 500, 180);
        add(text1);

        JLabel cardno = new JLabel("Enter a Card Number: ");
        cardno.setFont(new Font("Arial", Font.BOLD, 28));
        cardno.setBounds(120, 150, 600, 100);
        add(cardno);

        card = new JTextField();
        card.setBounds(430, 187, 300, 30);
        card.setFont(new Font("Arial", Font.BOLD, 15));
        add(card);

        JLabel pin = new JLabel("Enter a Pin Number: ");
        pin.setFont(new Font("Arial", Font.BOLD, 28));
        pin.setBounds(120, 220, 600, 100);
        add(pin);

        Ipin = new JPasswordField();
        Ipin.setBounds(430, 255, 300, 30);
        Ipin.setFont(new Font("Arial", Font.BOLD, 15));
        add(Ipin);

        login = new JButton("SIGN IN");
        login.setBounds(430, 320, 100, 30);
        login.setBackground(Color.BLACK);
        login.setForeground(Color.WHITE);
        login.addActionListener(this);
        add(login);

        clear = new JButton("CLEAR ALL!");
        clear.setBounds(550, 320, 120, 30);
        clear.setBackground(Color.BLACK);
        clear.setForeground(Color.WHITE);
        clear.addActionListener(this);
        add(clear);

        rg = new JButton("! REGISTRATION !");
        rg.setBounds(430, 380, 244, 30);
        rg.setBackground(Color.BLACK);
        rg.setForeground(Color.WHITE);
        rg.addActionListener(this);
        add(rg);

        getContentPane().setBackground(Color.ORANGE);
        setSize(800, 480);
        setVisible(true);
        setLocation(350, 200);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == clear) {
            card.setText("");
            Ipin.setText("");
        } else if (ae.getSource() == login) {
            Conn c = new Conn();
            String cardno = card.getText();
            String pinnum = Ipin.getText();

            // Add this check before proceeding
            if (cardno.isEmpty() || pinnum.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter both card number and pin.");
                return;
            }
          
            String query = "SELECT * FROM login WHERE cardno = ? AND pinnum = ?";
            try (PreparedStatement ps = c.c.prepareStatement(query)) {
                ps.setString(1, cardno);
                ps.setString(2, pinnum);
                ResultSet rs = ps.executeQuery(); // Corrected the query execution
                if (rs.next()) {
                    setVisible(false);
                    new AccountServices(pinnum).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid Card Number or Pin");
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        } else if (ae.getSource() == rg) {
            setVisible(false);
            new rg1().setVisible(true);
        }
    }

    
    // public static void main(String[] args) {
    //     new Login();
    // }
}
