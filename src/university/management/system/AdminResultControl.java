

package university.management.system;

import javax.swing.*;
import net.proteanit.sql.DbUtils;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AdminResultControl extends JFrame implements ActionListener {

    JTable table;
    JButton updateBtn, backBtn;

    public AdminResultControl() {
        setTitle("Result Lock / Publish");
        setLayout(new BorderLayout());

        table = new JTable();
        JScrollPane jsp = new JScrollPane(table);
        add(jsp, BorderLayout.CENTER);

        updateBtn = new JButton("Calculate & Publish GPA");
        backBtn = new JButton("Back");

        JPanel btnPanel = new JPanel();
        btnPanel.add(updateBtn);
        btnPanel.add(backBtn);
        add(btnPanel, BorderLayout.SOUTH);

        updateBtn.addActionListener(this);
        backBtn.addActionListener(this);

        displayTable();

        setSize(800, 400);
        setLocation(300, 150);
        setVisible(true);
    }

    public void displayTable() {
        try {
            Conn c = new Conn();

            ResultSet rs = c.s.executeQuery("SELECT rollno, gpa, published FROM results");
            table.setModel(DbUtils.resultSetToTableModel(rs));
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error displaying results: " + e.getMessage());
        }
    }



    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == updateBtn) {
            calculateAndPublishGPA();
        } else if (ae.getSource() == backBtn) {
            setVisible(false);
        }
    }

    public void calculateAndPublishGPA() {
        Connection conn = null;
        try {
            // 1. Create connection
            Conn c = new Conn();
            conn = c.c;
            conn.setAutoCommit(false); // Start transaction

            System.out.println("Finding all student roll numbers...");

            // 2. Get all student roll numbers
            PreparedStatement pstStudents = conn.prepareStatement("SELECT DISTINCT rollno FROM marks");
            ResultSet rsStudents = pstStudents.executeQuery();

            while (rsStudents.next()) {
                String rollno = rsStudents.getString("rollno");
                System.out.println("\nProcessing Roll: " + rollno);

                // 3. Check if all subjects are locked
                PreparedStatement pstCheck = conn.prepareStatement(
                        "SELECT COUNT(*) as total, SUM(locked=1) as locked_count FROM marks WHERE rollno = ?");
                pstCheck.setString(1, rollno);
                ResultSet rsCheck = pstCheck.executeQuery();

                if (rsCheck.next()) {
                    int total = rsCheck.getInt("total");
                    int lockedCount = rsCheck.getInt("locked_count");

                    System.out.println("Total subjects: " + total + ", Locked: " + lockedCount);

                    if (total == lockedCount && total > 0) {
                        // 4. Calculate GPA
                        System.out.println("Starting GPA calculation...");
                        double gpa = calculateGPA(conn, rollno);
                        System.out.println("Calculated GPA: " + gpa);

                        // 5. Update/Insert into results table
                        PreparedStatement pstUpdate = conn.prepareStatement(
                                "INSERT INTO results (rollno, gpa, published) VALUES (?, ?, 1) " +
                                        "ON DUPLICATE KEY UPDATE gpa = VALUES(gpa), published = 1");
                        pstUpdate.setString(1, rollno);
                        pstUpdate.setDouble(2, gpa);
                        int updated = pstUpdate.executeUpdate();
                        System.out.println("Updated rows: " + updated);
                        pstUpdate.close();
                    } else {
                        System.out.println("Not all subjects locked, keeping unpublished");
                        PreparedStatement pstUnpublish = conn.prepareStatement(
                                "UPDATE results SET published = 0 WHERE rollno = ?");
                        pstUnpublish.setString(1, rollno);
                        pstUnpublish.executeUpdate();
                        pstUnpublish.close();
                    }
                }
                rsCheck.close();
                pstCheck.close();
            }

            // 6. Commit all changes
            conn.commit();
            System.out.println("\n=== All changes committed successfully ===");
            JOptionPane.showMessageDialog(null, "Results published successfully!");
            displayTable();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            try {
                if (conn != null) {
                    System.out.println("Rolling back...");
                    conn.rollback();
                }
            } catch (SQLException ex) {
                System.out.println("Rollback error: " + ex.getMessage());
            }
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                    System.out.println("Connection closed");
                }
            } catch (SQLException e) {
                System.out.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    // GPA calculation helper method
    private double calculateGPA(Connection conn, String rollno) throws SQLException {
        PreparedStatement pst = conn.prepareStatement(
                "SELECT m.marks, s.credit FROM marks m JOIN subjects s ON m.subject_id = s.id " +
                        "WHERE m.rollno = ? AND m.locked = 1");
        pst.setString(1, rollno);
        ResultSet rs = pst.executeQuery();

        double totalPoints = 0;
        double totalCredits = 0;

        System.out.println("Subject-wise marks and credits:");
        while (rs.next()) {
            int marks = rs.getInt("marks");
            double credit = rs.getDouble("credit");
            double gradePoint = calculateGradePoint(marks);

            System.out.printf("Marks: %d, Credit: %.1f, Grade Point: %.1f%n",
                    marks, credit, gradePoint);

            totalPoints += gradePoint * credit;
            totalCredits += credit;
        }
        rs.close();
        pst.close();

        if (totalCredits == 0) return 0.0;

        double gpa = totalPoints / totalCredits;
        return Math.min(gpa, 4.0); // GPA capped at 4.0
    }

    // Grade point calculation method
    private double calculateGradePoint(int marks) {
        if (marks >= 80) return 4.0;
        else if (marks >= 70) return 3.5;
        else if (marks >= 60) return 3.0;
        else if (marks >= 50) return 2.5;
        else if (marks >= 40) return 2.0;
        else return 0.0;
    }

    public static void main(String[] args) {
        new AdminResultControl();
    }
}



























/*package university.management.system;

import javax.swing.*;
import net.proteanit.sql.DbUtils;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AdminResultControl extends JFrame implements ActionListener {

    JTable table;
    JButton updateBtn, backBtn;

    public AdminResultControl() {
        setTitle("Result Lock / Publish");
        setLayout(new BorderLayout());

        table = new JTable();
        JScrollPane jsp = new JScrollPane(table);
        add(jsp, BorderLayout.CENTER);

        updateBtn = new JButton("Calculate & Publish GPA");
        backBtn = new JButton("Back");

        JPanel btnPanel = new JPanel();
        btnPanel.add(updateBtn);
        btnPanel.add(backBtn);
        add(btnPanel, BorderLayout.SOUTH);

        updateBtn.addActionListener(this);
        backBtn.addActionListener(this);

        displayTable();

        setSize(800, 400);
        setLocation(300, 150);
        setVisible(true);
    }

    public void displayTable() {
        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("SELECT * FROM results");
            table.setModel(DbUtils.resultSetToTableModel(rs));
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error displaying results: " + e.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == updateBtn) {
            calculateAndPublishGPA();
        } else if (ae.getSource() == backBtn) {
            setVisible(false);
        }
    }

    public void calculateAndPublishGPA() {
        Connection conn = null;
        try {
            // 1. কানেকশন তৈরি
            Conn c = new Conn();
            conn = c.c;
            conn.setAutoCommit(false); // ট্রানজেকশন শুরু

            System.out.println("সমস্ত স্টুডেন্টের রোল নাম্বার খুঁজছি...");

            // 2. সকল স্টুডেন্টের রোল নাম্বার নিন
            PreparedStatement pstStudents = conn.prepareStatement("SELECT DISTINCT rollno FROM marks");
            ResultSet rsStudents = pstStudents.executeQuery();

            while (rsStudents.next()) {
                String rollno = rsStudents.getString("rollno");
                System.out.println("\nপ্রসেসিং রোল: " + rollno);

                // 3. চেক করুন সব সাবজেক্ট লক করা হয়েছে কিনা
                PreparedStatement pstCheck = conn.prepareStatement(
                        "SELECT COUNT(*) as total, SUM(locked=1) as locked_count FROM marks WHERE rollno = ?");
                pstCheck.setString(1, rollno);
                ResultSet rsCheck = pstCheck.executeQuery();

                if (rsCheck.next()) {
                    int total = rsCheck.getInt("total");
                    int lockedCount = rsCheck.getInt("locked_count");

                    System.out.println("মোট সাবজেক্ট: " + total + ", লক করা: " + lockedCount);

                    if (total == lockedCount && total > 0) {
                        // 4. GPA ক্যালকুলেট করুন
                        System.out.println("GPA ক্যালকুলেশন শুরু...");
                        double gpa = calculateGPA(conn, rollno);
                        System.out.println("ক্যালকুলেটেড GPA: " + gpa);

                        // 5. রেজাল্ট টেবিলে আপডেট/ইনসার্ট করুন
                        PreparedStatement pstUpdate = conn.prepareStatement(
                                "INSERT INTO results (rollno, gpa, published) VALUES (?, ?, 1) " +
                                        "ON DUPLICATE KEY UPDATE gpa = VALUES(gpa), published = 1");
                        pstUpdate.setString(1, rollno);
                        pstUpdate.setDouble(2, gpa);
                        int updated = pstUpdate.executeUpdate();
                        System.out.println("আপডেটেড রো: " + updated);
                        pstUpdate.close();
                    } else {
                        System.out.println("সব সাবজেক্ট লক করা নেই, unpublished রাখছি");
                        PreparedStatement pstUnpublish = conn.prepareStatement(
                                "UPDATE results SET published = 0 WHERE rollno = ?");
                        pstUnpublish.setString(1, rollno);
                        pstUnpublish.executeUpdate();
                        pstUnpublish.close();
                    }
                }
                rsCheck.close();
                pstCheck.close();
            }

            // 6. সব পরিবর্তন কমিট করুন
            conn.commit();
            System.out.println("\n=== সকল পরিবর্তন সফলভাবে কমিট করা হয়েছে ===");
            JOptionPane.showMessageDialog(null, "Results published successfully!");
            displayTable();

        } catch (Exception e) {
            System.out.println("ত্রুটি: " + e.getMessage());
            try {
                if (conn != null) {
                    System.out.println("রোলব্যাক করা হচ্ছে...");
                    conn.rollback();
                }
            } catch (SQLException ex) {
                System.out.println("রোলব্যাকে ত্রুটি: " + ex.getMessage());
            }
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                    System.out.println("কানেকশন বন্ধ করা হয়েছে");
                }
            } catch (SQLException e) {
                System.out.println("কানেকশন বন্ধ করতে ত্রুটি: " + e.getMessage());
            }
        }
    }

    // GPA ক্যালকুলেশন হেল্পার মেথড
    private double calculateGPA(Connection conn, String rollno) throws SQLException {
        PreparedStatement pst = conn.prepareStatement(
                "SELECT m.marks, s.credit FROM marks m JOIN subjects s ON m.subject_id = s.id " +
                        "WHERE m.rollno = ? AND m.locked = 1");
        pst.setString(1, rollno);
        ResultSet rs = pst.executeQuery();

        double totalPoints = 0;
        double totalCredits = 0;

        System.out.println("বিষয়ভিত্তিক মার্কস ও ক্রেডিট:");
        while (rs.next()) {
            int marks = rs.getInt("marks");
            double credit = rs.getDouble("credit");
            double gradePoint = calculateGradePoint(marks);

            System.out.printf("মার্কস: %d, ক্রেডিট: %.1f, গ্রেড পয়েন্ট: %.1f%n",
                    marks, credit, gradePoint);

            totalPoints += gradePoint * credit;
            totalCredits += credit;
        }
        rs.close();
        pst.close();

        if (totalCredits == 0) return 0.0;

        double gpa = totalPoints / totalCredits;
        return Math.min(gpa, 4.0); // GPA সর্বোচ্চ 4.0
    }


    // গ্রেড পয়েন্ট ক্যালকুলেশন মেথড
    private double calculateGradePoint(int marks) {
        if (marks >= 80) return 4.0;
        else if (marks >= 70) return 3.5;
        else if (marks >= 60) return 3.0;
        else if (marks >= 50) return 2.5;
        else if (marks >= 40) return 2.0;
        else return 0.0;
    }
}

        // টেবিল ডিসপ্লে মেথড
        public void displayTable() {
            try {
                Conn c = new Conn();
                ResultSet rs = c.s.executeQuery("SELECT * FROM results");
                table.setModel(DbUtils.resultSetToTableModel(rs));
                rs.close();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error displaying results: " + e.getMessage());
            }
        }

        // অ্যাকশন ইভেন্ট হ্যান্ডলার


        /*private void calculateStudentGPA(Connection conn, String rollno) throws SQLException {
            // Check if all required subjects are locked and have marks
            PreparedStatement pstCheck = conn.prepareStatement(
                    "SELECT COUNT(*) AS total_subjects, " +
                            "SUM(CASE WHEN m.locked = 1 AND m.marks IS NOT NULL THEN 1 ELSE 0 END) AS completed_subjects " +
                            "FROM marks m JOIN subjects s ON m.subject_id = s.id " +
                            "WHERE m.rollno = ?"
            );
            pstCheck.setString(1, rollno);
            ResultSet rsCheck = pstCheck.executeQuery();

            if (rsCheck.next()) {
                int totalSubjects = rsCheck.getInt("total_subjects");
                int completedSubjects = rsCheck.getInt("completed_subjects");

                if (totalSubjects == completedSubjects) {
                    // Calculate GPA
                    PreparedStatement pstMarks = conn.prepareStatement(
                            "SELECT m.marks, s.credit FROM marks m JOIN subjects s ON m.subject_id = s.id " +
                                    "WHERE m.rollno = ? AND m.locked = 1"
                    );
                    pstMarks.setString(1, rollno);
                    ResultSet rsMarks = pstMarks.executeQuery();

                    double totalPoints = 0;
                    double totalCredits = 0;

                    while (rsMarks.next()) {
                        int marks = rsMarks.getInt("marks");
                        double credit = rsMarks.getDouble("credit");
                        double gradePoint = calculateGradePoint(marks);
                        totalPoints += gradePoint * credit;
                        totalCredits += credit;
                    }
                    rsMarks.close();
                    pstMarks.close();

                    double gpa = (totalCredits > 0) ? totalPoints / totalCredits : 0;
                    if (gpa > 4.0) gpa = 4.0;

                    // Update or insert result
                    PreparedStatement pstResult = conn.prepareStatement(
                            "INSERT INTO results (rollno, gpa, cgpa, published) VALUES (?, ?, ?, 1) " +
                                    "ON DUPLICATE KEY UPDATE gpa = VALUES(gpa), cgpa = VALUES(cgpa), published = 1"
                    );
                    pstResult.setString(1, rollno);
                    pstResult.setDouble(2, gpa);
                    pstResult.setDouble(3, gpa); // Using same value for CGPA for simplicity
                    pstResult.executeUpdate();
                    pstResult.close();
                } else {
                    // Mark as unpublished if not all subjects are completed
                    PreparedStatement pstUnpublish = conn.prepareStatement(
                            "INSERT INTO results (rollno, published) VALUES (?, 0) " +
                                    "ON DUPLICATE KEY UPDATE published = 0"
                    );
                    pstUnpublish.setString(1, rollno);
                    pstUnpublish.executeUpdate();
                    pstUnpublish.close();
                }
            }
            rsCheck.close();
            pstCheck.close();
        }

        private double calculateGradePoint(int marks) {
            if (marks >= 80) return 4.0;
            else if (marks >= 70) return 3.5;
            else if (marks >= 60) return 3.0;
            else if (marks >= 50) return 2.5;
            else if (marks >= 40) return 2.0;
            else return 0.0;
        }



        public static void main(String[] args) {

            System.out.println("ডাটাবেস কানেকশন টেস্ট শুরু...");
            Conn c = new Conn();
            if (c.c != null) {
                System.out.println("✅ ডাটাবেসে সফলভাবে কানেক্ট হয়েছে!");
                try {
                    System.out.println("ডাটাবেস নাম: " + c.c.getCatalog());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("❌ ডাটাবেসে কানেক্ট করতে ব্যর্থ!");
            }

            // মূল অ্যাপ্লিকেশন শুরু করুন


            new AdminResultControl();
        }

    }*/
