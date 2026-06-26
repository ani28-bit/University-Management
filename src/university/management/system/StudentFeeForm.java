package university.management.system;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class StudentFeeForm extends JFrame implements ActionListener {
    private String rollno;  // Logged-in student's roll number

    JLabel labelrollno, labelname, labelfname, labelmname, labeltotal;
    JComboBox<String> cbcourse, cbbranch, cbsemester;
    JButton update, pay, back;

    public StudentFeeForm(String rollno) {
        this.rollno = rollno;

        setSize(900, 500);
        setLocation(300, 100);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        // Image on right side
        ImageIcon x = new ImageIcon(ClassLoader.getSystemResource("icons/fee.jpg"));
        Image y = x.getImage().getScaledInstance(500, 300, Image.SCALE_SMOOTH);
        ImageIcon z = new ImageIcon(y);
        JLabel image = new JLabel(z);
        image.setBounds(400, 50, 500, 300);
        add(image);

        // Roll No label (not editable)
        JLabel lblrollnumber = new JLabel("Roll No:");
        lblrollnumber.setBounds(40, 60, 150, 20);
        lblrollnumber.setFont(new Font("Tahoma", Font.BOLD, 16));
        add(lblrollnumber);

        labelrollno = new JLabel(rollno);
        labelrollno.setBounds(200, 60, 150, 20);
        labelrollno.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(labelrollno);

        // Name label
        JLabel lblname = new JLabel("Name:");
        lblname.setBounds(40, 100, 150, 20);
        lblname.setFont(new Font("Tahoma", Font.BOLD, 16));
        add(lblname);

        labelname = new JLabel();
        labelname.setBounds(200, 100, 150, 20);
        labelname.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(labelname);

        // Father's Name
        JLabel lblfname = new JLabel("Father's Name:");
        lblfname.setBounds(40, 140, 150, 20);
        lblfname.setFont(new Font("Tahoma", Font.BOLD, 16));
        add(lblfname);

        labelfname = new JLabel();
        labelfname.setBounds(200, 140, 150, 20);
        labelfname.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(labelfname);

        // Mother's Name
        JLabel lblmname = new JLabel("Mother's Name:");
        lblmname.setBounds(40, 180, 150, 20);
        lblmname.setFont(new Font("Tahoma", Font.BOLD, 16));
        add(lblmname);

        labelmname = new JLabel();
        labelmname.setBounds(200, 180, 150, 20);
        labelmname.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(labelmname);

        // Course (from DB, disabled)
        JLabel lblcourse = new JLabel("Course:");
        lblcourse.setBounds(40, 220, 150, 20);
        lblcourse.setFont(new Font("Tahoma", Font.BOLD, 16));
        add(lblcourse);

        cbcourse = new JComboBox<>();
        cbcourse.setBounds(200, 220, 150, 20);
        cbcourse.setEnabled(false);
        add(cbcourse);

        // Branch (from DB, disabled)
        JLabel lblbranch = new JLabel("Branch:");
        lblbranch.setBounds(40, 260, 150, 20);
        lblbranch.setFont(new Font("Tahoma", Font.BOLD, 16));
        add(lblbranch);

        cbbranch = new JComboBox<>();
        cbbranch.setBounds(200, 260, 150, 20);
        cbbranch.setEnabled(false);
        add(cbbranch);

        // Semester (selectable)
        JLabel lblsemester = new JLabel("Semester:");
        lblsemester.setBounds(40, 300, 150, 20);
        lblsemester.setFont(new Font("Tahoma", Font.BOLD, 16));
        add(lblsemester);

        String[] semesters = {"Semester1", "Semester2", "Semester3", "Semester4",
                "Semester5", "Semester6", "Semester7", "Semester8"};
        cbsemester = new JComboBox<>(semesters);
        cbsemester.setBounds(200, 300, 150, 20);
        add(cbsemester);

        // Total payable label
        JLabel lbltotal = new JLabel("Total Payable:");
        lbltotal.setBounds(40, 340, 150, 20);
        lbltotal.setFont(new Font("Tahoma", Font.BOLD, 16));
        add(lbltotal);

        labeltotal = new JLabel();
        labeltotal.setBounds(200, 340, 150, 20);
        labeltotal.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(labeltotal);

        // Buttons
        update = new JButton("Calculate Fee");
        update.setBounds(30, 380, 120, 25);
        update.setBackground(Color.GREEN);
        update.setForeground(Color.WHITE);
        update.addActionListener(this);
        add(update);

        pay = new JButton("Pay Fee");
        pay.setBounds(160, 380, 100, 25);
        pay.setBackground(Color.BLUE);
        pay.setForeground(Color.WHITE);
        pay.addActionListener(this);
        add(pay);

        back = new JButton("Back");
        back.setBounds(270, 380, 100, 25);
        back.setBackground(Color.RED);
        back.setForeground(Color.WHITE);
        back.addActionListener(this);
        add(back);

        loadStudentData();

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void loadStudentData() {
        try {
            Conn c = new Conn();
            String query = "SELECT name, fname, mname, course, branch FROM student WHERE rollno = ?";
            java.sql.PreparedStatement pst = c.c.prepareStatement(query);
            pst.setString(1, rollno);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                labelname.setText(rs.getString("name"));
                labelfname.setText(rs.getString("fname"));
                labelmname.setText(rs.getString("mname"));

                cbcourse.removeAllItems();
                cbcourse.addItem(rs.getString("course"));

                cbbranch.removeAllItems();
                cbbranch.addItem(rs.getString("branch"));
            } else {
                JOptionPane.showMessageDialog(this, "Student data not found!");
                dispose();
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading student data");
            dispose();
        }
    }

    private void updateTotalPayable() {
        String course = (String) cbcourse.getSelectedItem();
        String semester = (String) cbsemester.getSelectedItem();

        if (course == null || semester == null) {
            JOptionPane.showMessageDialog(this, "Course or Semester not selected");
            return;
        }

        try {
            Conn c = new Conn();
            String query = "SELECT " + semester + " FROM fee WHERE course = ?";
            java.sql.PreparedStatement pst = c.c.prepareStatement(query);
            pst.setString(1, course);
            ResultSet rs = pst.executeQuery();

            if (rs.next() && rs.getString(1) != null) {
                labeltotal.setText(rs.getString(1));
            } else {
                labeltotal.setText("");
                JOptionPane.showMessageDialog(this, "Fee structure not found for this course and semester");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error calculating fee");
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == update) {
            updateTotalPayable();
        } else if (ae.getSource() == pay) {
            String course = (String) cbcourse.getSelectedItem();
            String semester = (String) cbsemester.getSelectedItem();
            String branch = (String) cbbranch.getSelectedItem();
            String total = labeltotal.getText();

            if (total == null || total.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please calculate fee amount first");
                return;
            }

            try {
                Conn c = new Conn();
                String query = "INSERT INTO universityfee (rollno, course, branch, semester, total, payment_date) VALUES (?, ?, ?, ?, ?, CURRENT_DATE)";
                java.sql.PreparedStatement pst = c.c.prepareStatement(query);
                pst.setString(1, rollno);
                pst.setString(2, course);
                pst.setString(3, branch);
                pst.setString(4, semester);
                pst.setString(5, total);
                pst.executeUpdate();

                JOptionPane.showMessageDialog(this, "Fee payment successful!");
                dispose();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error processing payment");
            }
        } else if (ae.getSource() == back) {
            dispose();
        }
    }

    public static void main(String[] args) {
        new StudentFeeForm("15331234");
    }
}
