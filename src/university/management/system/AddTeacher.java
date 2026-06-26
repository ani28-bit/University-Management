package university.management.system;

import com.toedter.calendar.JDateChooser;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Random;
import javax.swing.*;

public class AddTeacher extends JFrame implements ActionListener {

    JTextField tfname, tffname, tfmname, tfaddress1, tfaddress2, tfphone, tfemail, tfx, tfxii, tfidNum, tfusername;
    JPasswordField tfpassword;
    JLabel labelempId;
    JDateChooser dcdob;
    JComboBox<String> cbcourse, cbbranch, cbcourseAssign;
    JButton submit, cancel;
    Random ran = new Random();
    long first4;

    public AddTeacher() {
        first4 = Math.abs(ran.nextLong() % 9000L + 1000L);

        setSize(1000, 800);
        setLocation(300, 50);
        setLayout(null);

        JLabel heading = new JLabel("New Teacher Registration");
        heading.setBounds(300, 30, 500, 50);
        heading.setFont(new Font("serif", Font.BOLD, 30));
        add(heading);

        // LEFT SIDE
        addLabelAndField("Name:", 50, 100, 230, 100);
        tfname = new JTextField();
        tfname.setBounds(230, 100, 250, 30);
        add(tfname);

        addLabel("Employee ID:", 50, 150);
        labelempId = new JLabel("101" + first4);
        labelempId.setBounds(230, 150, 250, 30);
        labelempId.setFont(new Font("serif", Font.BOLD, 20));
        add(labelempId);

        addLabelAndField("Username:", 50, 200, 230, 200);
        tfusername = new JTextField();
        tfusername.setBounds(230, 200, 250, 30);
        add(tfusername);

        addLabelAndField("Present Address:", 50, 250, 230, 250);
        tfaddress1 = new JTextField();
        tfaddress1.setBounds(230, 250, 250, 30);
        add(tfaddress1);

        addLabelAndField("Class X:", 50, 300, 230, 300);
        tfx = new JTextField();
        tfx.setBounds(230, 300, 250, 30);
        add(tfx);

        addLabel("Date of Birth:", 50, 350);
        dcdob = new JDateChooser();
        dcdob.setBounds(230, 350, 250, 30);
        add(dcdob);

        addLabelAndField("Phone Number:", 50, 400, 230, 400);
        tfphone = new JTextField();
        tfphone.setBounds(230, 400, 250, 30);
        add(tfphone);

        addLabelAndField("Email:", 50, 450, 230, 450);
        tfemail = new JTextField();
        tfemail.setBounds(230, 450, 250, 30);
        add(tfemail);

        addLabelAndField("ID Number:", 50, 500, 230, 500);
        tfidNum = new JTextField();
        tfidNum.setBounds(230, 500, 250, 30);
        add(tfidNum);

        // RIGHT SIDE
        addLabelAndField("Father's Name:", 550, 100, 750, 100);
        tffname = new JTextField();
        tffname.setBounds(750, 100, 250, 30);
        add(tffname);

        addLabelAndField("Mother's Name:", 550, 150, 750, 150);
        tfmname = new JTextField();
        tfmname.setBounds(750, 150, 250, 30);
        add(tfmname);

        addLabel("Password:", 550, 200);
        tfpassword = new JPasswordField();
        tfpassword.setBounds(750, 200, 250, 30);
        add(tfpassword);

        addLabelAndField("Permanent Address:", 550, 250, 750, 250);
        tfaddress2 = new JTextField();
        tfaddress2.setBounds(750, 250, 250, 30);
        add(tfaddress2);

        addLabelAndField("Class XII:", 550, 300, 750, 300);
        tfxii = new JTextField();
        tfxii.setBounds(750, 300, 250, 30);
        add(tfxii);

        addLabel("Qualification:", 550, 350);
        String[] courses = {"BSc", "MSc", "PhD", "Diploma"};
        cbcourse = new JComboBox<>(courses);
        cbcourse.setBounds(750, 350, 250, 30);
        cbcourse.setBackground(Color.WHITE);
        add(cbcourse);

        addLabel("Department:", 550, 400);
        String[] branches = {"CSE", "ECE", "ME", "CE", "EE", "BIO"};
        cbbranch = new JComboBox<>(branches);
        cbbranch.setBounds(750, 400, 250, 30);
        cbbranch.setBackground(Color.WHITE);
        add(cbbranch);

        // Assign Course dropdown - use course codes exactly as in DB
        addLabel("Assign Course:", 550, 450);
        String[] coursesAssign = {"MATH101","STAT102","SOC103","DSA104","SE105","OOP106","CO107","DSAL104","OOPL106"};
        cbcourseAssign = new JComboBox<>(coursesAssign);
        cbcourseAssign.setBounds(750, 450, 250, 30);
        cbcourseAssign.setBackground(Color.WHITE);
        add(cbcourseAssign);

        // Buttons
        submit = new JButton("Submit");
        submit.setBounds(350, 650, 120, 35);
        submit.setBackground(new Color(0, 102, 204));
        submit.setForeground(Color.WHITE);
        submit.setFont(new Font("Tahoma", Font.BOLD, 15));
        submit.addActionListener(this);
        add(submit);

        cancel = new JButton("Cancel");
        cancel.setBounds(550, 650, 120, 35);
        cancel.setBackground(new Color(255, 204, 0));
        cancel.setForeground(Color.BLACK);
        cancel.setFont(new Font("Tahoma", Font.BOLD, 15));
        cancel.addActionListener(this);
        add(cancel);

        getContentPane().setBackground(Color.WHITE);
        setVisible(true);
    }

    private void addLabel(String text, int x, int y) {
        JLabel lbl = new JLabel(text);
        lbl.setBounds(x, y, 200, 30);
        lbl.setFont(new Font("serif", Font.BOLD, 20));
        add(lbl);
    }

    private void addLabelAndField(String labelText, int labelX, int labelY, int fieldX, int fieldY) {
        addLabel(labelText, labelX, labelY);
        // field is added manually in constructor
    }

    // New method to get course id from course code string
    private int getCourseIdFromCode(String courseCode) {
        int courseId = -1;
        try {
            Conn con = new Conn();
            String query = "SELECT id FROM subjects WHERE code = ?";
            PreparedStatement pst = con.c.prepareStatement(query);
            pst.setString(1, courseCode);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                courseId = rs.getInt("id");
            }
            rs.close();
            pst.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return courseId;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == submit) {
            String name = tfname.getText().trim();
            String fname = tffname.getText().trim();
            String mname = tfmname.getText().trim();
            String empId = labelempId.getText().trim();
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
            String qualification = (String) cbcourse.getSelectedItem();
            String department = (String) cbbranch.getSelectedItem();
            String username = tfusername.getText().trim();
            String password = new String(tfpassword.getPassword());
            String assignedCourse = (String) cbcourseAssign.getSelectedItem();

            // Basic validation
            if (name.isEmpty() || fname.isEmpty() || mname.isEmpty() || dob == null || address1.isEmpty() || address2.isEmpty()
                    || phone.isEmpty() || email.isEmpty() || x.isEmpty() || xii.isEmpty() || idNum.isEmpty()
                    || username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill all the fields correctly!", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Conn con = new Conn();

                // Check if username exists already
                String checkUser = "SELECT * FROM teacher WHERE username = ?";
                PreparedStatement pstCheck = con.c.prepareStatement(checkUser);
                pstCheck.setString(1, username);
                ResultSet rs = pstCheck.executeQuery();
                if (rs.next()) {
                    JOptionPane.showMessageDialog(null, "Username already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Insert teacher data
                String query = "INSERT INTO teacher (name, fname, mname, empId, dob, address1, address2, phone, email, x, xii, qualification, department, idNum, username, password, role) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement pst = con.c.prepareStatement(query);
                pst.setString(1, name);
                pst.setString(2, fname);
                pst.setString(3, mname);
                pst.setString(4, empId);
                pst.setDate(5, dob);
                pst.setString(6, address1);
                pst.setString(7, address2);
                pst.setString(8, phone);
                pst.setString(9, email);
                pst.setString(10, x);
                pst.setString(11, xii);
                pst.setString(12, qualification);
                pst.setString(13, department);
                pst.setString(14, idNum);
                pst.setString(15, username);
                pst.setString(16, password);
                pst.setString(17, "teacher");

                pst.executeUpdate();

                // Get course id for assigned course
                int courseId = getCourseIdFromCode(assignedCourse);
                if (courseId == -1) {
                    JOptionPane.showMessageDialog(null, "Invalid course selected!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Insert into teacher_courses table
                String queryCourse = "INSERT INTO teacher_courses (empId, course_id) VALUES (?, ?)";
                PreparedStatement pstCourse = con.c.prepareStatement(queryCourse);
                pstCourse.setString(1, empId);
                pstCourse.setInt(2, courseId);
                pstCourse.executeUpdate();

                JOptionPane.showMessageDialog(null, "Teacher registered successfully!");
                setVisible(false);

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }

        } else if (ae.getSource() == cancel) {
            setVisible(false);
        }
    }

    public static void main(String[] args) {
        new AddTeacher();
    }
}
