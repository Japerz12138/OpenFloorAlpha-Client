package com.nyit.japerz;

import com.nyit.japerz.utils.HashingUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Register extends JFrame{
    private JTextField usernameTF;
    private JTextField nicknameTF;
    private JPasswordField passwordPTF;
    private JPasswordField passwordConfirmPTF;
    private JButton registerButton;
    private JPanel panel1;
    private JButton cancelButton;

    public Register() {
        ImageIcon img = new ImageIcon("I:\\CODE\\OpenFloorAlpha-Client\\chat_icon.png");
        setContentPane(panel1);
        setTitle("Open Floor Chat - Register");
        setSize(500,650);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);
        setLocationRelativeTo(null);
        setIconImage(img.getImage());
        setResizable(false);

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performRegistration();
            }
        });
    }

    private void performRegistration() {
        try {
            Connection connection = Database.connection; // Connect to database
            Statement stm = connection.createStatement();

            String username = usernameTF.getText();
            String nickname = nicknameTF.getText();
            String password = new String(passwordPTF.getPassword());
            String confirmPassword = new String(passwordConfirmPTF.getPassword());

            if (username.isEmpty() || nickname.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields before registering!");
                return;
            }

            if(!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Oops!, passwords do not match!");
                return;
            }

            String hashedPasswordEntered = HashingUtils.sha256(password);

            String query = "SELECT MAX(user_id) FROM accounts";
            ResultSet rs = stm.executeQuery(query);
            int userID = 1;
            if (rs.next()){
                userID = rs.getInt(1) + 1;
            }

            query = String.format("INSERT INTO accounts(user_id, username, nickname, password, is_banned) VALUES(%d, '%s', '%s', '%s', '%s')", userID, username, nickname, hashedPasswordEntered, 0);
            stm.executeUpdate(query);

            JOptionPane.showMessageDialog(this, "Registration successful! Please login!");

            //Close current window and back to log in window
            Login login = new Login();
            dispose();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error registering account! Please contact admin!");
        }

    }
}
