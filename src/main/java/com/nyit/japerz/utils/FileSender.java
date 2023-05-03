package com.nyit.japerz.utils;

import com.nyit.japerz.DownloadConfirm;

import javax.swing.*;
import javax.xml.crypto.Data;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class FileSender extends JFrame{
    private JButton chooseFileButton;
    private JButton sendFileButton;
    private JPanel panel1;
    private JLabel fileNameTF;
    private JButton getFilesButton;

    private PrintWriter writer;

    final File[] fileToSend = new File[1];

    public FileSender() {
        ImageIcon img = new ImageIcon("I:\\CODE\\OpenFloorAlpha-Client\\chat_icon.png");
        setContentPane(panel1);
        setTitle("File Sender");
        setSize(400,300);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);
        setLocationRelativeTo(null);
        setIconImage(img.getImage());
        setResizable(false);

        chooseFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Open file chooser and store the file in fileToSend.
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setDialogTitle("Choose a file");

                if(jFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    fileToSend[0] = jFileChooser.getSelectedFile();
                    fileNameTF.setText("Selected file: " + fileToSend[0].getName());

                }
            }
        });

        sendFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //File Stream Operation - sending files streams.
                if (fileToSend[0] == null) {
                    fileNameTF.setText("Please choose a file first!");
                } else {
                    try {
                        FileInputStream fileInputStream = new FileInputStream(fileToSend[0].getAbsolutePath());
                        Socket socket = new Socket("localhost", 2334);

                        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

                        String fileName = fileToSend[0].getName();
                        byte[] fileNameBytes = fileName.getBytes(); //Convert fileName to byte

                        byte[] fileContentBytes = new byte[(int) fileToSend[0].length()];
                        fileInputStream.read(fileContentBytes);

                        dataOutputStream.writeInt(fileNameBytes.length); //Send File Byte to Server
                        dataOutputStream.write(fileNameBytes);

                        dataOutputStream.writeInt(fileContentBytes.length); //Send actual File Data Byte to server
                        dataOutputStream.write(fileContentBytes);


                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        getFilesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileReceiver fr = new FileReceiver();
            }
        });
    }
}