package com.nyit.japerz;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.nyit.japerz.utils.HashingUtils;

public class Login extends JFrame{
    private JPanel panel1;
    private JTextField usernameTF;
    private JButton loginButton;
    private JButton registerButton;
    private JButton aboutButton;
    private JPasswordField passwordPTF;

    //Set pass through to other classes.
    private static String usernamePT;
    private static String passwordPT;

    public Login() {
        setLookAndFeel();
        ImageIcon img = new ImageIcon("I:\\CODE\\OpenFloorAlpha-Client\\chat_icon.png");
        setContentPane(panel1);
        setTitle("Open Floor Chat - Login");
        setSize(500,600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        setLocationRelativeTo(null);
        setIconImage(img.getImage());
        setResizable(false);




        passwordPTF.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performLogin();
                }
            }
        });
        aboutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                About about = new About();
            }
        });
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Register register = new Register();
            }
        });
    }

    //Enter key detection


    public static void main(String[] args) {
        Database.connect();
        setupClosingDBConnection();
        Login login = new Login();
    }

    private void performLogin() {
        String username = usernameTF.getText();
        usernamePT = username;
        String password = new String(passwordPTF.getPassword());

        String sql = "SELECT * FROM openFloor_db.accounts WHERE username = ?";

        try (PreparedStatement statement = Database.connection.prepareStatement(sql)) {
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                String hashedPasswordFromDatabase = rs.getString("password");
                Boolean isBanned = rs.getBoolean("is_banned");
                //Encrypt the password to SHA256 foe later searching in database
                String hashedPasswordEntered = HashingUtils.sha256(password);
                passwordPT = hashedPasswordEntered;

                if(isBanned)
                {
                    JOptionPane.showMessageDialog(null, "This account has been banned.");
                    return;
                }

                if (hashedPasswordFromDatabase.equals(hashedPasswordEntered)) {
                    // Login successful
                    JOptionPane.showMessageDialog(null, "Login successful!");
                    System.out.println("[INFO] User " + username + " logged in! Password correct and encrypted.");
                    ChatRoom chatRoom = new ChatRoom();
                    dispose();

                } else {
                    // Incorrect password
                    JOptionPane.showMessageDialog(null, "Incorrect password!");
                }
            } else {
                // User does not exist
                JOptionPane.showMessageDialog(null, "User does not exist!");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

    //Setup Closing Database Connection Function
    public static void setupClosingDBConnection() {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                try { Database.connection.close(); System.out.println("[INFO] Application Closed - DB Connection Closed");
                } catch (SQLException e) { e.printStackTrace(); }
            }
        }, "Shutdown-thread"));
    }

    //Set the window looks modern on Windows
    public void setLookAndFeel() {
        try {
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Get the username pass through
    public static String getUsernamePT() {
        return usernamePT;
    }

    //Get the password after hashing operation pass through
    public static String getPasswordPT() {
        return passwordPT;
    }
}
