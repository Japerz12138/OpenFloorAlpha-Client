package com.nyit.japerz;

import com.nyit.japerz.utils.FileSender;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
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
    private JButton filesButton;

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    private static String hostName = "localhost";
    private static int port = 2333;

    private String nickname = getNickname();



    public ChatRoom() {
        ImageIcon img = new ImageIcon("I:\\CODE\\OpenFloorAlpha-Client\\chat_icon.png");
        setContentPane(panel1);
        setTitle("Open Floor Chat - Chat");
        setSize(700,500);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        setLocationRelativeTo(null);
        setIconImage(img.getImage());

        onlineCounts.setText("1 Online");

        sendingTA.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    e.consume(); //Prevent enter key make a new line in text area
                    performSend();
                }
            }
        });

        if (nickname != null) {
            header_username.setText(nickname);
        } else {
            header_username.setText("ERROR");
        }

        // Connect to the chat server
        try {
            socket = new Socket(hostName, port); // Replace "localhost" with the IP address of the server
            InputStreamReader streamReader = new InputStreamReader(socket.getInputStream());
            reader = new BufferedReader(streamReader);
            writer = new PrintWriter(socket.getOutputStream());
            writer.println("join " + nickname); // Send the user's nickname to the server
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
                performSend();
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
        filesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileSender fs = new FileSender();
            }
        });
    }

    private void performSend() {
        String message = sendingTA.getText();
        if (!message.isEmpty()) {
            writer.println(message);
            writer.flush();
            sendingTA.setText("");
            chatTA.append("\n>" + nickname + ": " + message);
        }
    }

    private class IncomingReader implements Runnable {
        private List<String> onlineUsers = new ArrayList<>();


        @Override
        public void run() {
            try {
                while (true) {
                    String message = reader.readLine();

                    onlineUsers.add(nickname);
                    if (message != null) {
                        if (message.startsWith("join ")) {
                            String[] users = message.substring(5).split(",");
                            onlineUsers.clear();
                            for (String user : users) {
                                onlineUsers.add(user);
                            }
                            SwingUtilities.invokeLater(() -> {
                                updateOnlineUserList(onlineUsers);
                                onlineCounts.setText(onlineUsers.size()+1 + " Online");
                            });
                        }else if (message.startsWith("quit ")){
                            onlineUsers.remove(nickname);
                            onlineCounts.setText(onlineUsers.size()+1 + " Online");
                        } else {
                            SwingUtilities.invokeLater(() -> {
                                chatTA.append(message + "\n");
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
    public String getNickname() {
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

    private void updateOnlineUserList(List<String> onlineUsers) {
        onlineCounts.setText(onlineUsers.size() + " Online");

        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (String user : onlineUsers) {
            listModel.addElement(user);
        }
        onlineUserList.setModel(listModel);
    }

    private int saveImageToDatabase(byte[] imageBytes) {
        try{
            Connection connection = Database.connection; // Connect to database
            PreparedStatement stm = connection.prepareStatement("INSERT INTO images (image_data) VALUES (?), Statement.RETURN_GENERATED_KEYS"); // Create statement
            stm.setBytes(1, imageBytes);
            stm.executeUpdate();
            ResultSet rs = stm.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error saving image to database: " + e.getMessage());
        }
        return -1;
    }
}
