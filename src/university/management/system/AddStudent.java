package university.management.system;

import com.toedter.calendar.JDateChooser;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;
import java.sql.*;

public class AddStudent extends JFrame implements ActionListener {

    JTextField tfname, tffname, tfmname, tfaddress1, tfaddress2, tfphone, tfemail, tfx, tfxii, tfidNum, tfusername;
    JPasswordField tfpassword;
    JLabel labelrollno;
    JDateChooser dcdob;
    JComboBox<String> cbcourse, cbbranch;
    JButton submit, cancel;
    Random ran = new Random();
    long first4;

    public AddStudent() {
        first4 = Math.abs(ran.nextLong() % 9000L + 1000L);

        setSize(1000, 800);
        setLocation(300, 50);
        setLayout(null);

        JLabel heading = new JLabel("New Student Registration");
        heading.setBounds(300, 30, 500, 50);
        heading.setFont(new Font("serif", Font.BOLD, 30));
        add(heading);

        // LEFT SIDE
        JLabel lblname = new JLabel("Name:");
        lblname.setBounds(50, 100, 150, 30);
        lblname.setFont(new Font("serif", Font.BOLD, 20));
        add(lblname);

        tfname = new JTextField();
        tfname.setBounds(230, 100, 250, 30);
        add(tfname);

        JLabel lblrollno = new JLabel("Roll Number:");
        lblrollno.setBounds(50, 150, 150, 30);
        lblrollno.setFont(new Font("serif", Font.BOLD, 20));
        add(lblrollno);

        labelrollno = new JLabel("1533" + first4);
        labelrollno.setBounds(230, 150, 250, 30);
        labelrollno.setFont(new Font("serif", Font.BOLD, 20));
        add(labelrollno);

        JLabel lblusername = new JLabel("Username:");
        lblusername.setBounds(50, 200, 150, 30);
        lblusername.setFont(new Font("serif", Font.BOLD, 20));
        add(lblusername);

        tfusername = new JTextField();
        tfusername.setBounds(230, 200, 250, 30);
        add(tfusername);

        JLabel lbladdress1 = new JLabel("Present Address :");
        lbladdress1.setBounds(50, 250, 225, 30);
        lbladdress1.setFont(new Font("serif", Font.BOLD, 20));
        add(lbladdress1);

        tfaddress1 = new JTextField();
        tfaddress1.setBounds(230, 250, 250, 30);
        add(tfaddress1);

        JLabel lblx = new JLabel("Class-X :");
        lblx.setBounds(50, 300, 150, 30);
        lblx.setFont(new Font("serif", Font.BOLD, 20));
        add(lblx);

        tfx = new JTextField();
        tfx.setBounds(230, 300, 250, 30);
        add(tfx);

        JLabel lbldob = new JLabel("Date of Birth:");
        lbldob.setBounds(50, 350, 150, 30);
        lbldob.setFont(new Font("serif", Font.BOLD, 20));
        add(lbldob);

        dcdob = new JDateChooser();
        dcdob.setBounds(230, 350, 250, 30);
        add(dcdob);

        JLabel lblphone = new JLabel("Phone Number:");
        lblphone.setBounds(50, 400, 150, 30);
        lblphone.setFont(new Font("serif", Font.BOLD, 20));
        add(lblphone);

        tfphone = new JTextField();
        tfphone.setBounds(230, 400, 250, 30);
        add(tfphone);

        JLabel lblemail = new JLabel("Email:");
        lblemail.setBounds(50, 450, 150, 30);
        lblemail.setFont(new Font("serif", Font.BOLD, 20));
        add(lblemail);

        tfemail = new JTextField();
        tfemail.setBounds(230, 450, 250, 30);
        add(tfemail);

        // RIGHT SIDE
        JLabel lblfname = new JLabel("Father's Name:");
        lblfname.setBounds(550, 100, 200, 30);
        lblfname.setFont(new Font("serif", Font.BOLD, 20));
        add(lblfname);

        tffname = new JTextField();
        tffname.setBounds(750, 100, 250, 30);
        add(tffname);

        JLabel lblmname = new JLabel("Mother's Name:");
        lblmname.setBounds(550, 150, 200, 30);
        lblmname.setFont(new Font("serif", Font.BOLD, 20));
        add(lblmname);

        tfmname = new JTextField();
        tfmname.setBounds(750, 150, 250, 30);
        add(tfmname);

        JLabel lblpassword = new JLabel("Password:");
        lblpassword.setBounds(550, 200, 200, 30);
        lblpassword.setFont(new Font("serif", Font.BOLD, 20));
        add(lblpassword);

        tfpassword = new JPasswordField();
        tfpassword.setBounds(750, 200, 250, 30);
        add(tfpassword);

        JLabel lbladdress2 = new JLabel("Permanent Address:");
        lbladdress2.setBounds(550, 250, 200, 30);
        lbladdress2.setFont(new Font("serif", Font.BOLD, 20));
        add(lbladdress2);

        tfaddress2 = new JTextField();
        tfaddress2.setBounds(750, 250, 250, 30);
        add(tfaddress2);

        JLabel lblxii = new JLabel("Class XII:");
        lblxii.setBounds(550, 300, 200, 30);
        lblxii.setFont(new Font("serif", Font.BOLD, 20));
        add(lblxii);

        tfxii = new JTextField();
        tfxii.setBounds(750, 300, 250, 30);
        add(tfxii);

        JLabel lblidNum = new JLabel("NID No:");
        lblidNum.setBounds(550, 350, 200, 30);
        lblidNum.setFont(new Font("serif", Font.BOLD, 20));
        add(lblidNum);

        tfidNum = new JTextField();
        tfidNum.setBounds(750, 350, 250, 30);
        add(tfidNum);

        JLabel lblcourse = new JLabel("Course:");
        lblcourse.setBounds(550, 400, 200, 30);
        lblcourse.setFont(new Font("serif", Font.BOLD, 20));
        add(lblcourse);

        String[] courses = {"BSSE", "MSSE", "MIT", "M.Tech", "M.Sc", "M.A"};
        cbcourse = new JComboBox<>(courses);
        cbcourse.setBounds(750, 400, 250, 30);
        cbcourse.setBackground(Color.WHITE);
        add(cbcourse);

        JLabel lblbranch = new JLabel("Branch:");
        lblbranch.setBounds(550, 450, 200, 30);
        lblbranch.setFont(new Font("serif", Font.BOLD, 20));
        add(lblbranch);

        String[] branches = {"CSE", "ECE", "ME", "CE", "EE", "BIO"};
        cbbranch = new JComboBox<>(branches);
        cbbranch.setBounds(750, 450, 250, 30);
        cbbranch.setBackground(Color.WHITE);
        add(cbbranch);

        // BUTTONS
        submit = new JButton("Submit");
        submit.setBounds(400, 650, 120, 35);
        submit.setBackground(new Color(0, 102, 204));
        submit.setForeground(Color.WHITE);
        submit.setFont(new Font("serif", Font.BOLD, 15));
        submit.addActionListener(this);
        add(submit);

        cancel = new JButton("Cancel");
        cancel.setBounds(580, 650, 120, 35);
        cancel.setBackground(new Color(255, 204, 0));
        cancel.setForeground(Color.BLACK);
        cancel.setFont(new Font("serif", Font.BOLD, 15));
        cancel.addActionListener(this);
        add(cancel);

        getContentPane().setBackground(Color.WHITE);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == submit) {
            String name = tfname.getText().trim();
            String fname = tffname.getText().trim();
            String mname = tfmname.getText().trim();
            String rollno = labelrollno.getText().trim();

            java.sql.Date dob = null;
            try {
                java.util.Date utilDate = dcdob.getDate();
                if (utilDate != null) {
                    dob = new java.sql.Date(utilDate.getTime());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            String address1 = tfaddress1.getText().trim();
            String address2 = tfaddress2.getText().trim();
            String phone = tfphone.getText().trim();
            String email = tfemail.getText().trim();
            String x = tfx.getText().trim();
            String xii = tfxii.getText().trim();
            String idNum = tfidNum.getText().trim();
            String course = (String) cbcourse.getSelectedItem();
            String branch = (String) cbbranch.getSelectedItem();
            String username = tfusername.getText().trim();
            String password = new String(tfpassword.getPassword()).trim();
            String role = "student";

            // -------- Validation --------
            if (name.isEmpty() || fname.isEmpty() || mname.isEmpty() || dob == null ||
                    address1.isEmpty() || address2.isEmpty() || phone.isEmpty() || email.isEmpty() ||
                    x.isEmpty() || xii.isEmpty() || idNum.isEmpty() ||
                    username.isEmpty() || password.isEmpty()) {

                JOptionPane.showMessageDialog(null,
                        "Invalid! Please fill all fields before submitting.");
                return;
            }
            // ----------------------------

            try {
                Conn con = new Conn();

                // Check if username already exists
                String checkUser = "SELECT * FROM student WHERE username = ?";
                PreparedStatement pstCheck = con.c.prepareStatement(checkUser);
                pstCheck.setString(1, username);
                ResultSet rs = pstCheck.executeQuery();
                if (rs.next()) {
                    JOptionPane.showMessageDialog(null, "Username already exists!");
                    return;
                }

                // Insert data
                String query = "INSERT INTO student VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement pst = con.c.prepareStatement(query);
                pst.setString(1, name);
                pst.setString(2, fname);
                pst.setString(3, mname);
                pst.setString(4, rollno);
                pst.setDate(5, dob);
                pst.setString(6, address1);
                pst.setString(7, address2);
                pst.setString(8, phone);
                pst.setString(9, email);
                pst.setString(10, x);
                pst.setString(11, xii);
                pst.setString(12, course);
                pst.setString(13, branch);
                pst.setString(14, idNum);
                pst.setString(15, username);
                pst.setString(16, password);
                pst.setString(17, role);

                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "Student registered successfully!");
                setVisible(false);

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            }

        } else if (ae.getSource() == cancel) {
            setVisible(false);
        }
    }

    public static void main(String[] args) {
        new AddStudent();
    }
}
