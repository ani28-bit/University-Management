package university.management.system;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class StudentResultView extends JFrame implements ActionListener {
    String rollno;
    JButton back;

    StudentResultView(String rollno) {
        this.rollno = rollno;

        setTitle("Student Result");
        setSize(600, 500);
        setLocation(400, 150);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        JLabel heading = new JLabel("Dhaka University");
        heading.setBounds(180, 10, 300, 30);
        heading.setFont(new Font("Tahoma", Font.BOLD, 22));
        add(heading);

        JLabel subheading = new JLabel("Result of Examination");
        subheading.setBounds(180, 50, 300, 25);
        subheading.setFont(new Font("Tahoma", Font.BOLD, 18));
        add(subheading);

        JLabel lblRoll = new JLabel("Roll No: " + rollno);
        lblRoll.setBounds(50, 90, 300, 20);
        lblRoll.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(lblRoll);

        JTable table = new JTable();
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(50, 130, 480, 200);
        add(sp);


        JLabel lblCGPA = new JLabel("CGPA: ");
        lblCGPA.setBounds(50, 380, 300, 20);
        lblCGPA.setFont(new Font("Tahoma", Font.BOLD, 16));
        add(lblCGPA);

        try {
            Conn c = new Conn();
            // Check if result is published
            ResultSet rsCheck = c.s.executeQuery("SELECT published FROM results WHERE rollno='" + rollno + "' LIMIT 1");
            if (rsCheck.next() && rsCheck.getInt("published") == 0) {
                JOptionPane.showMessageDialog(null, "Your result is not published yet.");
                dispose();
                return;
            }

            // Load marks with subject names
            ResultSet rs = c.s.executeQuery(
                    "SELECT s.subject_name, m.marks, m.grade " +
                            "FROM marks m " +
                            "JOIN subjects s ON m.subject_id = s.id " +
                            "WHERE m.rollno = '" + rollno + "'");

            table.setModel(net.proteanit.sql.DbUtils.resultSetToTableModel(rs));

            ResultSet rs2 = c.s.executeQuery("SELECT gpa FROM results WHERE rollno='" + rollno + "'");
            if (rs2.next()) {
                lblCGPA.setText("CGPA: " + rs2.getString("gpa"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        back = new JButton("Back");
        back.setBounds(250, 420, 100, 25);
        back.setBackground(Color.RED);
        back.setForeground(Color.WHITE);
        back.addActionListener(this);
        add(back);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        setVisible(false);
    }

    public static void main(String[] args) {
        new StudentResultView("15331234"); // Example Roll
    }
}
