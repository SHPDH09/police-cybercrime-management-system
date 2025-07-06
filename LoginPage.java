package cybercell;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.io.*;
import javax.sound.sampled.*;

public class LoginPage extends JFrame {
    private JTextField useridField;
    private JPasswordField passwordField;
    private JLabel nameLabel;
    private JCheckBox showPassword;
    private JButton loginBtn;

    public LoginPage() {
        // ✅ Set title and size
        setTitle("Cyber Cell Login");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ✅ Set background first
        String path = "C:/Users/rauna/eclipse-workspace/cybercell/resources/oFTHu7E.gif";
        File file = new File(path);
        JLabel bgLabel = null;

        if (file.exists()) {
            bgLabel = new JLabel(new ImageIcon(path));
            setContentPane(bgLabel);       // ✅ use this to make it visible
            bgLabel.setLayout(null);       // ✅ set layout AFTER content pane
            System.out.println("✅ Background image loaded.");
        } else {
            System.out.println("❌ File not found at: " + path);
        }

        // ✅ Panel for form
        JPanel panel = new JPanel();
        panel.setBounds(100, 60, 400, 280);
        panel.setLayout(null);
        panel.setOpaque(false);

        if (bgLabel != null) {
            bgLabel.add(panel);
        } else {
            getContentPane().add(panel);
        }

        // ✅ Play sound
        playSound("resources/scary-sound-effect-359877.wav");

        // ✅ Form fields
        JLabel userLabel = new JLabel("User ID:");
        userLabel.setForeground(Color.WHITE);
        userLabel.setBounds(20, 20, 100, 30);
        panel.add(userLabel);

        useridField = new JTextField();
        useridField.setBounds(120, 20, 200, 30);
        panel.add(useridField);

        nameLabel = new JLabel("Name: ");
        nameLabel.setForeground(Color.CYAN);
        nameLabel.setBounds(120, 55, 250, 25);
        panel.add(nameLabel);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(Color.WHITE);
        passLabel.setBounds(20, 90, 100, 30);
        panel.add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(120, 90, 200, 30);
        panel.add(passwordField);

        showPassword = new JCheckBox("Show Password");
        showPassword.setBounds(120, 130, 150, 20);
        showPassword.setOpaque(false);
        showPassword.setForeground(Color.LIGHT_GRAY);
        panel.add(showPassword);

        loginBtn = new JButton("Login");
        loginBtn.setBounds(120, 160, 100, 30);
        panel.add(loginBtn);

        showPassword.addActionListener(e -> {
            passwordField.setEchoChar(showPassword.isSelected() ? (char) 0 : '•');
        });

        useridField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                autoFetchName();
            }
        });

        loginBtn.addActionListener(e -> loginUser());

        setVisible(true);
    }

    private void autoFetchName() {
        String uid = useridField.getText();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement("SELECT name FROM users WHERE userid = ?")) {
            pst.setString(1, uid);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                nameLabel.setText("Name: " + rs.getString("name"));
            } else {
                nameLabel.setText("Name: ");
            }
        } catch (SQLException e) {
            nameLabel.setText("Error");
        }
    }

    private void loginUser() {
        String uid = useridField.getText();
        String pass = new String(passwordField.getPassword());

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement("SELECT * FROM users WHERE userid = ? AND password = ?")) {
            pst.setString(1, uid);
            pst.setString(2, pass);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Login Successful!");
                dispose();
                new CyberCellSystem(); // open main system
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Credentials!");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage());
        }
    }

    private void playSound(String filePath) {
        try {
            File file = new File(filePath);
            AudioInputStream audio = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            clip.start();
        } catch (Exception e) {
            System.out.println("Sound error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginPage::new);
    }
}
