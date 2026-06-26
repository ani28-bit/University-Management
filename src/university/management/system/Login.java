
package university.management.system;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class Login extends JFrame implements ActionListener {
    private JButton login, cancel, toggleEye;
    private JTextField tfusername;
    private JPasswordField tfpassword;
    private JComboBox<String> userTypeCombo;
    private boolean showPassword = false;


    private JButton btnNewStudent, btnNewTeacher;

    public Login() {
        this.getContentPane().setBackground(Color.WHITE);
        this.setLayout(null);


        JLabel lblUserType = new JLabel("Login As:");
        lblUserType.setBounds(40, 20, 100, 20);
        this.add(lblUserType);

        String[] userTypes = {"Student", "Teacher", "Admin"};
        userTypeCombo = new JComboBox<>(userTypes);
        userTypeCombo.setBounds(150, 20, 150, 20);
        userTypeCombo.setSelectedIndex(0);
        this.add(userTypeCombo);


        JLabel lblusername = new JLabel("Username");
        lblusername.setBounds(40, 60, 100, 20);
        this.add(lblusername);

        this.tfusername = new JTextField();
        this.tfusername.setBounds(150, 60, 150, 20);
        this.add(this.tfusername);


        JLabel lblpassword = new JLabel("Password");
        lblpassword.setBounds(40, 100, 100, 20);
        this.add(lblpassword);

        this.tfpassword = new JPasswordField();
        this.tfpassword.setBounds(150, 100, 150, 20);
        this.tfpassword.setEchoChar('•');
        this.add(this.tfpassword);


        ImageIcon eyeIcon = new ImageIcon(ClassLoader.getSystemResource("icons/eye_10784416.png"));
        ImageIcon eyeOffIcon = new ImageIcon(ClassLoader.getSystemResource("icons/Eyes Emoji PNG.jpeg"));

        toggleEye = new JButton(eyeIcon);
        toggleEye.setBounds(310, 100, 30, 20);
        toggleEye.setFocusable(false);
        toggleEye.setBorder(null);
        toggleEye.setContentAreaFilled(false);
        toggleEye.addActionListener(e -> {
            showPassword = !showPassword;
            if (showPassword) {
                tfpassword.setEchoChar((char) 0);
                toggleEye.setIcon(eyeOffIcon);
            } else {
                tfpassword.setEchoChar('•');
                toggleEye.setIcon(eyeIcon);
            }
        });
        this.add(toggleEye);


        this.login = new JButton("Login");
        this.login.setBounds(40, 140, 120, 30);
        this.login.setBackground(Color.GREEN);
        this.login.setForeground(Color.WHITE);
        this.login.addActionListener(this);
        this.login.setFont(new Font("serif", Font.BOLD, 15));
        this.add(this.login);


        this.cancel = new JButton("Cancel");
        this.cancel.setBounds(180, 140, 120, 30);
        this.cancel.setBackground(Color.RED);
        this.cancel.setForeground(Color.WHITE);
        this.cancel.addActionListener(this);
        this.cancel.setFont(new Font("serif", Font.BOLD, 15));
        this.add(this.cancel);


        btnNewStudent = new JButton("New Student Registration");
        btnNewStudent.setBounds(40, 190, 190, 30);
        btnNewStudent.setBackground(Color.BLUE);
        btnNewStudent.setForeground(Color.WHITE);
        btnNewStudent.addActionListener(e -> new AddStudent());
        this.add(btnNewStudent);

        btnNewTeacher = new JButton("New Teacher Registration");
        btnNewTeacher.setBounds(250, 190, 190, 30);
        btnNewTeacher.setBackground(Color.BLUE);
        btnNewTeacher.setForeground(Color.WHITE);
        btnNewTeacher.addActionListener(e -> new AddTeacher());
        this.add(btnNewTeacher);


        btnNewStudent.setVisible(true);
        btnNewTeacher.setVisible(false);


        userTypeCombo.addActionListener(e -> {
            String selected = (String) userTypeCombo.getSelectedItem();
            if ("Student".equals(selected)) {
                btnNewStudent.setVisible(true);
                btnNewTeacher.setVisible(false);
            } else if ("Teacher".equals(selected)) {
                btnNewTeacher.setVisible(true);
                btnNewStudent.setVisible(false);
            } else { // Admin
                btnNewStudent.setVisible(false);
                btnNewTeacher.setVisible(false);
            }
        });

        // Image
        ImageIcon x = new ImageIcon(ClassLoader.getSystemResource("icons/b4d81b1d-8d73-43db-9273-21457fb99df8.jpeg"));
        Image y = x.getImage().getScaledInstance(200, 200, Image.SCALE_DEFAULT);
        ImageIcon z = new ImageIcon(y);
        JLabel image = new JLabel(z);
        image.setBounds(350, 0, 200, 200);
        this.add(image);

        this.setSize(600, 300);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == login) {
            String username = tfusername.getText().trim();
            String password = new String(tfpassword.getPassword()).trim();
            String userType = (String) userTypeCombo.getSelectedItem();

            // Empty field validation
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter both username and password");
                return;
            }

            try {
                Conn c = new Conn();
                String query = "";
                switch (userType) {
                    case "Student":
                        query = "SELECT rollno FROM student WHERE username = ? AND password = ?";
                        break;
                    case "Teacher":
                        query = "SELECT empId FROM teacher WHERE username = ? AND password = ?";
                        break;
                    case "Admin":
                        query = "SELECT username FROM admin WHERE username = ? AND password = ?";
                        break;
                }

                PreparedStatement pst = c.c.prepareStatement(query);
                pst.setString(1, username);
                pst.setString(2, password);

                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    this.setVisible(false);
                    if ("Student".equalsIgnoreCase(userType)) {
                        String rollno = rs.getString("rollno");
                        new Project("Student", username, rollno).setVisible(true);
                    } else if ("Teacher".equalsIgnoreCase(userType)) {
                        String empId = rs.getString("empId");
                        new Project("Teacher", username, empId).setVisible(true);
                    } else { // Admin
                        String adminUsername = rs.getString("username");
                        new Project("Admin", adminUsername, "").setVisible(true);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid login credentials");
                    tfusername.setText("");
                    tfpassword.setText("");
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage());
            }

        } else if (ae.getSource() == cancel) {
            this.setVisible(false);
        }
    }

    public static void main(String[] args) {

        new Login();
    }
}

