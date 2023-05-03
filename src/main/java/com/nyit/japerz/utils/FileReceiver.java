//package com.nyit.japerz.utils;
//
//import com.nyit.japerz.ChatRoom;
//
//import javax.swing.*;
//import javax.swing.table.DefaultTableModel;
//import java.awt.Point;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import java.io.*;
//import java.net.Socket;
//import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//public class FileReceiver extends JFrame{
//    private JPanel panel1;
//    private JTable table1;
//    private JButton BACKButton;
//
//    public FileReceiver() {
//        ImageIcon img = new ImageIcon("I:\\CODE\\OpenFloorAlpha-Client\\chat_icon.png");
//        setContentPane(panel1);
//        setTitle("File Receiver");
//        setSize(400,300);
//        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
//        setVisible(true);
//        setLocationRelativeTo(null);
//        setIconImage(img.getImage());
//        setResizable(false);
//
//        try {
//            Socket socket = new Socket("localhost", 2335);
//            System.out.println("Connected to file manage server");
//
//            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
//            List<String> fileNames = (List<String>) inputStream.readObject();
//
//            String[] columns = {"File Name"};
//            DefaultTableModel model = new DefaultTableModel(columns, 0);
//            for (String fileName : fileNames) {
//                Object[] row = {fileName};
//                model.addRow(row);
//            }
//            table1.setModel(model);
//
//            // Add a right-click listener to the table
//            table1.addMouseListener(new MouseAdapter() {
//                public void mousePressed(MouseEvent event) {
//                    Point point = event.getPoint();
//                    int row = table1.rowAtPoint(point);
//                    if (event.getButton() == MouseEvent.BUTTON3 && row != -1) {
//                        // Show a context menu with a "Download" option
//                        JPopupMenu menu = new JPopupMenu();
//                        JMenuItem downloadItem = new JMenuItem("Download");
//                        downloadItem.addActionListener(new ActionListener() {
//                            public void actionPerformed(ActionEvent e) {
//                                // Get the selected file name and download the file
//                                String fileName = (String) table1.getValueAt(row, 0);
//                                downloadFile(fileName);
//                            }
//                        });
//                        menu.add(downloadItem);
//                        menu.show(table1, event.getX(), event.getY());
//                    }
//                }
//            });
//
//        } catch (IOException ex) {
//            Logger.getLogger(ChatRoom.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//
//        System.out.println("Connected to server.");
//
//    }
//
//    private void downloadFile(String fileName) {
//        try {
//            Socket socket = new Socket("localhost", 2335);
//            System.out.println("Connected to file download server");
//
//            // Send the file name to the server
//            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
//            outputStream.writeObject(fileName);
//            System.out.println("Sent file name to download server.");
//
//            // Prompt the user to choose a directory to save the file in
//            JFileChooser chooser = new JFileChooser();
//            chooser.setDialogTitle("Save File");
//            chooser.setSelectedFile(new File(fileName));
//            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//            if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
//                // User canceled, do nothing
//                return;
//            }
//            File saveDir = chooser.getSelectedFile();
//            System.out.println("Saving file to: " + saveDir.getAbsolutePath());
//
//            // Receive the file contents from the server and save them to disk
//            InputStream inputStream = socket.getInputStream();
//            FileOutputStream fileOutputStream = new FileOutputStream(new File(saveDir, fileName));
//            byte[] buffer = new byte[4096];
//            int bytesRead;
//            while ((bytesRead = inputStream.read(buffer)) != -1) {
//                fileOutputStream.write(buffer, 0, bytesRead);
//            }
//            System.out.println("File downloaded.");
//
//            // Close the connection
//            socket.close();
//            System.out.println("Connection closed.");
//
//        } catch (IOException ex) {
//            Logger.getLogger(ChatRoom.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//}


package com.nyit.japerz.utils;

import com.nyit.japerz.ChatRoom;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileReceiver extends JFrame {
    private JPanel panel1;
    private JTable table1;
    private JButton BACKButton;
    private Socket socket; // Added to store the socket
    final File[] fileToSend = new File[1];

    public FileReceiver() {
        ImageIcon img = new ImageIcon("I:\\CODE\\OpenFloorAlpha-Client\\chat_icon.png");
        setContentPane(panel1);
        setTitle("File Receiver");
        setSize(400, 300);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);
        setLocationRelativeTo(null);
        setIconImage(img.getImage());
        setResizable(false);

        try {
            socket = new Socket("localhost", 2335);
            System.out.println("Connected to file manage server");
//            OutputStream outputStream = socket.getOutputStream();
//            outputStream.write(1);
//            outputStream.flush();

            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            List<String> fileNames = (List<String>) inputStream.readObject();

            String[] columns = {"File Name"};
            DefaultTableModel model = new DefaultTableModel(columns, 0);
            for (String fileName : fileNames) {
                Object[] row = {fileName};
                model.addRow(row);
            }
            table1.setModel(model);

            socket.getOutputStream().write(1);
            socket.getOutputStream().flush();
            System.out.println("Sent signal to download server.");

            // Add a right-click listener to the table
            table1.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent event) {
                    Point point = event.getPoint();
                    int row = table1.rowAtPoint(point);
                    if (event.getButton() == MouseEvent.BUTTON3 && row != -1) {
                        // Show a context menu with a "Download" option
                        JPopupMenu menu = new JPopupMenu();
                        JMenuItem downloadItem = new JMenuItem("Download");
                        downloadItem.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                // Get the selected file name and download the file
                                String fileName = (String) table1.getValueAt(row, 0);
                                downloadFile(fileName);
                            }
                        });
                        menu.add(downloadItem);
                        menu.show(table1, event.getX(), event.getY());
                    }
                }
            });
        } catch (IOException ex) {
            Logger.getLogger(ChatRoom.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        BACKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void downloadFile(String fileName) {
        try {

            Socket downloadSocket = new Socket("localhost", 2336);
            System.out.println("Connected to file download server");
            // Send the file name to the server


            //ObjectOutputStream outputStream = new ObjectOutputStream(downloadSocket.getOutputStream());
            //outputStream.writeObject(fileName);
            //outputStream.flush();
            System.out.println("Sent file name to download server.");

//            // Prompt the user to choose a directory to save the file in
//            JFileChooser chooser = new JFileChooser();
//            chooser.setDialogTitle("Save File");
//            chooser.setSelectedFile(new File(fileName));
//            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//            if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
//                // User canceled, do nothing
//                return;
//            }
//            File saveDir = chooser.getSelectedFile();
//            System.out.println("Saving file to: " + saveDir.getAbsolutePath());

            // Create a new socket to download the file
            Socket socket = new Socket("localhost", 2335);
            System.out.println("Connected to file download server");

            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            byte[] fileNameBytes = fileName.getBytes();
            dataOutputStream.writeInt(fileNameBytes.length);
            dataOutputStream.write(fileNameBytes);
            // Send the file name to the server
            //outputStream.writeObject(fileName);
            System.out.println("Sent file name to download server.");

//            // Receive the file contents from the server and save them to disk
//            InputStream inputStream = socket.getInputStream();
//            //FileOutputStream fileOutputStream = new FileOutputStream(new File(saveDir, fileName));
//            FileOutputStream fileOutputStream = new FileOutputStream("I:/CODE/DATA/" + fileName);
//            byte[] buffer = new byte[4096];
//            int bytesRead;
//            while ((bytesRead = inputStream.read(buffer)) != -1) {
//                fileOutputStream.write(buffer, 0, bytesRead);
//            }
//            System.out.println("File downloaded.");
//
//
//            // Close the output stream and file output stream
//            outputStream.close();
//            fileOutputStream.close();
//            // Close the connection
//            //socket.close();
//            System.out.println("Connection closed.");
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

            int fileContentLength = dataInputStream.readInt();

            if (fileContentLength > 0) {
                byte[] fileContentBytes = new byte[fileContentLength];
                dataInputStream.readFully(fileContentBytes, 0, fileContentLength);

                String savePath = "I:/CODE/DATA/" + fileName;

                if (getFileExtension(fileName).equalsIgnoreCase("txt")) {
                    String txtContent = new String(fileContentBytes);
                    // Write file to disk
                    FileOutputStream fileOutputStream = new FileOutputStream(savePath);
                    fileOutputStream.write(fileContentBytes);
                    fileOutputStream.close();
                    System.out.println("A new file \"" + fileName + "\" has been saved to " + savePath + " !");
                } else if(getFileExtension(fileName).equalsIgnoreCase("png")) {
                    FileOutputStream fileOutputStream = new FileOutputStream(savePath);
                    fileOutputStream.write(fileContentBytes);
                    fileOutputStream.close();

                    System.out.println("A new file \"" + fileName + "\" has been saved to " + savePath + " !");
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(ChatRoom.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static String getFileExtension(String fileName) {
        //This would not work with .tar.gz
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            return fileName.substring(i + 1);
        } else {
            return "No extension found!";
        }
    }
}
