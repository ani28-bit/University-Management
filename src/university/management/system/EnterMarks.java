
package university.management.system;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class EnterMarks extends JFrame implements ActionListener {
    String teacherId,userType;
    JTable table;
    JButton saveBtn, backBtn;
    JTextField markField;
    int selectedRow = -1;

    EnterMarks(String teacherId,String userType) {
        this.teacherId = teacherId;
        this.userType= userType;

        setTitle("Marks Entry - Teacher Panel");
        setSize(800, 500);
        setLocation(300, 150);
        setLayout(null);

        JLabel heading = new JLabel("Marks Entry - Teacher Panel");
        heading.setBounds(250, 10, 400, 30);
        heading.setFont(new Font("Tahoma", Font.BOLD, 20));
        add(heading);

        table = new JTable();
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(30, 60, 720, 250);
        add(sp);

        JLabel markLabel = new JLabel("Enter Marks:");
        markLabel.setBounds(30, 330, 100, 25);
        add(markLabel);

        markField = new JTextField();
        markField.setBounds(140, 330, 100, 25);
        add(markField);

        saveBtn = new JButton("Save");
        saveBtn.setBounds(260, 330, 100, 25);
        saveBtn.addActionListener(this);
        add(saveBtn);

        backBtn = new JButton("Back");
        backBtn.setBounds(380, 330, 100, 25);
        backBtn.addActionListener(this);
        add(backBtn);

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                    selectedRow = table.getSelectedRow();
                    String currentMarks = table.getValueAt(selectedRow, 3).toString();
                    markField.setText(currentMarks);
                }
            }
        });

        loadMarks();
        setVisible(true);
    }

    // Load Marks into Table
    private void loadMarks() {
        Conn c = null;
        ResultSet rsCheck = null;
        ResultSet rs = null;

        try {
            c = new Conn();

            // 1️⃣ Check course lock status
            rsCheck = c.s.executeQuery(
                    "SELECT isLocked FROM teacher_courses WHERE empId='" + teacherId + "' LIMIT 1"
            );
            boolean isLocked = false;
            if (rsCheck.next()) {
                isLocked = rsCheck.getInt("isLocked") == 1;
            }

            // 2️⃣ Load marks
            String query = "SELECT m.rollno, s.name, sub.subject_name, m.marks, m.subject_id " +
                    "FROM marks m " +
                    "JOIN student s ON s.rollno = m.rollno " +
                    "JOIN subjects sub ON m.subject_id = sub.id " +
                    "JOIN teacher_courses tc ON tc.course_id = sub.id " +
                    "WHERE tc.empId = '" + teacherId + "'";

            rs = c.s.executeQuery(query);

            javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(
                    new String[]{"Roll No", "Student Name", "Subject", "Marks", "subject_id"}, 0) {
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            boolean allMarksEntered = true;

            while (rs.next()) {
                int marks = rs.getInt("marks");
                if (marks == 0) {
                    allMarksEntered = false;
                }

                model.addRow(new Object[]{
                        rs.getString("rollno"),
                        rs.getString("name"),
                        rs.getString("subject_name"),
                        marks,
                        rs.getInt("subject_id")
                });
            }

            table.setModel(model);
            // Hide subject_id column
            table.getColumnModel().getColumn(4).setMinWidth(0);
            table.getColumnModel().getColumn(4).setMaxWidth(0);
            table.getColumnModel().getColumn(4).setWidth(0);

            // 3️⃣ Disable if locked
            if (isLocked && allMarksEntered) {
                JOptionPane.showMessageDialog(null, "Marks are locked. You cannot edit.");
                saveBtn.setEnabled(false);
                markField.setEnabled(false);
            } else {
                saveBtn.setEnabled(true);
                markField.setEnabled(true);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rsCheck != null) rsCheck.close();
                if (rs != null) rs.close();
                if (c != null) {
                    if (c.s != null) c.s.close();
                    if (c.c != null) c.c.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Save & Back Button Handler
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == saveBtn) {
            saveMarks();
        } else if (ae.getSource() == backBtn) {
            this.dispose();
        }
    }

    // Save Marks Logic with Auto Lock
    private void saveMarks() {
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a row first!");
            return;
        }

        String roll = table.getValueAt(selectedRow, 0).toString();
        String marksStr = markField.getText().trim();
        int subjectId = Integer.parseInt(table.getValueAt(selectedRow, 4).toString());

        if (marksStr.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter marks.");
            return;
        }

        Conn c = null;
        PreparedStatement pst = null;

        try {
            int marks = Integer.parseInt(marksStr);
            if (marks < 0 || marks > 100) {
                JOptionPane.showMessageDialog(null, "Marks must be between 0 and 100.");
                return;
            }

            String grade;
            if (marks >= 80) grade = "A+";
            else if (marks >= 70) grade = "A";
            else if (marks >= 60) grade = "A-";
            else if (marks >= 50) grade = "C";
            else if (marks >= 40) grade = "D";
            else grade = "F";

            c = new Conn();

            // Update marks
            String updateQuery = "UPDATE marks SET marks = ?, grade = ?, locked = 1 WHERE rollno = ? AND subject_id = ?";
            pst = c.c.prepareStatement(updateQuery);
            pst.setInt(1, marks);
            pst.setString(2, grade);
            pst.setString(3, roll);
            pst.setInt(4, subjectId);
            int updated = pst.executeUpdate();

            if (updated > 0) {
                JOptionPane.showMessageDialog(null, "Marks & Grade Updated Successfully");

                // Student-wise lock
                String checkStudentQuery = "SELECT COUNT(*) FROM marks WHERE rollno = ? AND (marks IS NULL OR marks = 0)";
                PreparedStatement checkStudentStmt = c.c.prepareStatement(checkStudentQuery);
                checkStudentStmt.setString(1, roll);
                ResultSet rsStudent = checkStudentStmt.executeQuery();
                if (rsStudent.next() && rsStudent.getInt(1) == 0) {
                    String lockStudentQuery = "UPDATE marks SET locked = 1 WHERE rollno = ?";
                    PreparedStatement lockStudentStmt = c.c.prepareStatement(lockStudentQuery);
                    lockStudentStmt.setString(1, roll);
                    lockStudentStmt.executeUpdate();
                    lockStudentStmt.close();
                    JOptionPane.showMessageDialog(null, "All marks entered for roll: " + roll + ". Student's marks locked.");
                }
                rsStudent.close();
                checkStudentStmt.close();

                // Course-wise lock
                String checkCourseQuery = "SELECT COUNT(*) FROM marks m " +
                        "JOIN subjects s ON m.subject_id = s.id " +
                        "JOIN teacher_courses tc ON tc.course_id = s.id " +
                        "WHERE tc.empId = ? AND (m.marks IS NULL OR m.marks = 0)";
                PreparedStatement checkCourseStmt = c.c.prepareStatement(checkCourseQuery);
                checkCourseStmt.setString(1, teacherId);
                ResultSet rsCourse = checkCourseStmt.executeQuery();
                if (rsCourse.next() && rsCourse.getInt(1) == 0) {
                    String lockCourseQuery = "UPDATE teacher_courses SET isLocked = 1 WHERE empId = ?";
                    PreparedStatement lockCourseStmt = c.c.prepareStatement(lockCourseQuery);
                    lockCourseStmt.setString(1, teacherId);
                    lockCourseStmt.executeUpdate();
                    lockCourseStmt.close();
                    JOptionPane.showMessageDialog(null, "All marks entered for this course. Course locked for teacher: " + teacherId);
                }
                rsCourse.close();
                checkCourseStmt.close();

                // Refresh Table
                loadMarks();
                selectedRow = -1;
                markField.setText("");
            } else {
                JOptionPane.showMessageDialog(null, "Update failed. Please try again.");
            }

        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(null, "Please enter a valid number.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pst != null) pst.close();
                if (c != null && c.c != null) c.c.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new EnterMarks("1011211","Teacher"); // Example Teacher ID
    }
}














































