package com.nyit.japerz;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatRoom extends JFrame{
    private JButton logoutButton;
    private JPanel panel1;
    private JLabel header_username;
    private JList<String> onlineUserList;
    private JLabel onlineCounts;
    private JTextArea chatTA;
    private JTextArea sendingTA;
    private JButton sendButton;

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    private static String hostName = "localhost";
    private static int port = 2333;

    public ChatRoom() {
        ImageIcon img = new ImageIcon("I:\\CODE\\OpenFloorAlpha-Client\\chat_icon.png");
        setContentPane(panel1);
        setTitle("Open Floor Chat - Chat");
        setSize(700,500);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        setLocationRelativeTo(null);
        setIconImage(img.getImage());


        String username = getUsername();
        if (username != null) {
            header_username.setText(username);
        } else {
            header_username.setText("ERROR");
        }

        // Connect to the chat server
        try {
            socket = new Socket(hostName, port); // Replace "localhost" with the IP address of the server
            InputStreamReader streamReader = new InputStreamReader(socket.getInputStream());
            reader = new BufferedReader(streamReader);
            writer = new PrintWriter(socket.getOutputStream());
            writer.println("join " + username); // Send the user's nickname to the server
            writer.flush();
            chatTA.append("[INFO] You have connected to chat server! \n");
        } catch (IOException ex) {
            Logger.getLogger(ChatRoom.class.getName()).log(Level.SEVERE, null, ex);
        }

        Thread incomingReader = new Thread(new IncomingReader());
        incomingReader.start();


        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = sendingTA.getText();
                if (!message.isEmpty()) {
                    writer.println(message);
                    writer.flush();
                    sendingTA.setText("");
                    chatTA.append("\n>" + username + ": " + message);
                }
            }
        });

        // Log out when the logout button is clicked
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                writer.println("quit");
                writer.flush();
                Login login = new Login();
                dispose();
            }
        });

    }

    private class IncomingReader implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    String message = reader.readLine();
                    if (message != null) {
                        if (message.startsWith("onlineCounts ")) {
                            onlineCounts.setText(message.substring(13) + " Online"); // Update online user count
                        } else {
                            SwingUtilities.invokeLater(() -> {
                                chatTA.append(message + "\n"); // Display message in the chat area from EDT
                            });
                        }
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(ChatRoom.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    //Function to use username and password to get the user's legal name.
    public String getUsername() {
        String username = Login.getUsernamePT();
        String password = Login.getPasswordPT();

        try {
            Connection connection = Database.connection; // Connect to database
            Statement stm = connection.createStatement();

            ResultSet rs = stm.executeQuery("SELECT nickname FROM accounts WHERE username = '" + username + "' AND password = '" + password + "'");

            if (rs.next()) {
                return rs.getString("nickname");
            } else {
                JOptionPane.showMessageDialog(null, "Unable to establish connection with the chat server, please contact admin.", "Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
