

package university.management.system;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;

public class Project extends JFrame implements ActionListener {
    private String userType;
    private String username;
    private String userId;

    Project(String userType, String username, String rollno) {
        this.userType = userType;
        this.username = username;
        this.userId = rollno;

        setSize(1540, 850);
        setTitle("University Management System - Logged in as: " + userType);

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/photo.jpg"));
        Image i2 = i1.getImage().getScaledInstance(1500, 750, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel image = new JLabel(i3);
        add(image);

        JMenuBar mb = new JMenuBar();

        // -------- New Info --------
        JMenu newInformation = new JMenu("New Information");
        newInformation.setForeground(Color.BLUE);
        mb.add(newInformation);

        JMenuItem facultyInfo = new JMenuItem("New Faculty Information");
        facultyInfo.addActionListener(this);
        newInformation.add(facultyInfo);

        JMenuItem studentInfo = new JMenuItem("New Student Information");
        studentInfo.addActionListener(this);
        newInformation.add(studentInfo);

        // -------- View Details --------
        JMenu details = new JMenu("View Details");
        details.setForeground(Color.RED);
        mb.add(details);

        JMenuItem facultydetails = new JMenuItem("View Faculty Details");
        facultydetails.addActionListener(this);
        details.add(facultydetails);

        JMenuItem studentdetails = new JMenuItem("View Student Details");
        studentdetails.addActionListener(this);
        details.add(studentdetails);

        // -------- Leave --------
        JMenu leave = new JMenu("Apply Leave");
        leave.setForeground(Color.BLUE);
        mb.add(leave);

        JMenuItem facultyleave = new JMenuItem("Faculty Leave");
        facultyleave.addActionListener(this);
        leave.add(facultyleave);

        JMenuItem studentleave = new JMenuItem("Student Leave");
        studentleave.addActionListener(this);
        leave.add(studentleave);

        JMenu leaveDetails = new JMenu("Leave Details");
        leaveDetails.setForeground(Color.RED);
        mb.add(leaveDetails);

        JMenuItem facultyleavedetails = new JMenuItem("Faculty Leave Details");
        facultyleavedetails.addActionListener(this);
        leaveDetails.add(facultyleavedetails);

        JMenuItem studentleavedetails = new JMenuItem("Student Leave Details");
        studentleavedetails.addActionListener(this);
        leaveDetails.add(studentleavedetails);

        // -------- Examination --------
        JMenu exam = new JMenu("Examination");
        exam.setForeground(Color.BLUE);
        mb.add(exam);

        JMenuItem examinationdetails = new JMenuItem("Examination Results");
        examinationdetails.addActionListener(this);
        exam.add(examinationdetails);

        JMenuItem entermarks = new JMenuItem("Enter Marks");
        entermarks.addActionListener(this);
        exam.add(entermarks);

        JMenuItem adminResultControl = new JMenuItem("Admin Result Control");
        adminResultControl.addActionListener(this);
        exam.add(adminResultControl);

        // -------- Update --------
        JMenu updateInfo = new JMenu("Update Details");
        updateInfo.setForeground(Color.RED);
        mb.add(updateInfo);

        JMenuItem updatefacultyinfo = new JMenuItem("Update Faculty Details");
        updatefacultyinfo.addActionListener(this);
        updateInfo.add(updatefacultyinfo);

        JMenuItem updatestudentinfo = new JMenuItem("Update Student Details");
        updatestudentinfo.addActionListener(this);
        updateInfo.add(updatestudentinfo);

        // -------- Fee --------
        JMenu fee = new JMenu("Fee Details");
        fee.setForeground(Color.BLUE);
        mb.add(fee);

        JMenuItem feestructure = new JMenuItem("Fee Structure");
        feestructure.addActionListener(this);
        fee.add(feestructure);

        JMenuItem feeform = new JMenuItem("Student Fee Form");
        feeform.addActionListener(this);
        fee.add(feeform);

        // -------- Utility --------
        JMenu utility = new JMenu("Utility");
        utility.setForeground(Color.RED);
        mb.add(utility);

        JMenuItem notepad = new JMenuItem("Notepad");
        notepad.addActionListener(this);
        utility.add(notepad);

        JMenuItem calc = new JMenuItem("Calculator");
        calc.addActionListener(this);
        utility.add(calc);

        // -------- About --------
        JMenu about = new JMenu("About");
        about.setForeground(Color.BLUE);
        mb.add(about);

        JMenuItem ab = new JMenuItem("About");
        ab.addActionListener(this);
        about.add(ab);

        // -------- Exit --------
        JMenu exit = new JMenu("Exit");
        exit.setForeground(Color.RED);
        mb.add(exit);

        JMenuItem ex = new JMenuItem("Exit");
        ex.addActionListener(this);
        exit.add(ex);

        setJMenuBar(mb);

        applyUserPermissions();
        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void disableMenuItems(String[] menuItems) {
        Component[] menus = getJMenuBar().getComponents();
        for (Component menu : menus) {
            if (menu instanceof JMenu) {
                Component[] items = ((JMenu) menu).getMenuComponents();
                for (Component item : items) {
                    if (item instanceof JMenuItem) {
                        JMenuItem menuItem = (JMenuItem) item;
                        for (String disabledItem : menuItems) {
                            if (menuItem.getText().equals(disabledItem)) {
                                menuItem.setEnabled(false);
                            }
                        }
                    }
                }
            }
        }
    }

    private void applyUserPermissions() {
        switch (userType) {
            case "Teacher":
                disableMenuItems(new String[]{
                        "New Student Information",
                        "New Faculty Information",
                        "View Faculty Details",
                        "View Student Details",
                        "Faculty Leave Details",
                        "Student Leave Details",
                        "Fee Structure",
                        "Student Fee Form",
                        "Student Leave",
                        "Update Student Details",
                        "Admin Result Control"
                });
                break;

            case "Student":
                disableMenuItems(new String[]{
                        "New Faculty Information",
                        "New Student Information",
                        "View Faculty Details",
                        "View Student Details",
                        "Faculty Leave Details",
                        "Student Leave Details",
                        "Enter Marks",
                        "Faculty Leave",
                        "Update Faculty Details",
                        "Admin Result Control"
                });
                break;

            case "Admin":
                disableMenuItems(new String[]{
                        "Faculty Leave",
                        "Student Leave",
                        "Enter Marks",
                        "New Student Information",
                        "New Faculty Information",
                        "Update Faculty Details",
                        "Update Student Details"
                });
                break;
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String msg = ae.getActionCommand();

        try {
            switch (msg) {
                case "Exit":
                    setVisible(false);
                    new Login().setVisible(true);
                    break;

                case "Calculator":
                    Runtime.getRuntime().exec("calc.exe");
                    break;

                case "Notepad":
                    Runtime.getRuntime().exec("notepad.exe");
                    break;

                case "New Faculty Information":
                    new AddTeacher();
                    break;

                case "New Student Information":
                    new AddStudent();
                    break;

                case "View Faculty Details":
                    new TeacherDetails();
                    break;

                case "View Student Details":
                    new StudentDetails();
                    break;

                case "Faculty Leave":
                    new TeacherLeave(userId);
                    break;

                case "Student Leave":
                    new StudentLeave(userId);
                    break;

                case "Faculty Leave Details":
                    new LeaveDetails("faculty", userType, userId);
                    break;

                case "Student Leave Details":
                    new LeaveDetails("student", userType, userId);
                    break;

                case "Update Faculty Details":
                    new UpdateTeacher(userId);
                    break;

                case "Update Student Details":
                    new UpdateStudent(userId);
                    break;

                case "Enter Marks":
                    new EnterMarks(userId,userType);
                    break;

                case "Examination Results":
                    if ("Student".equalsIgnoreCase(userType)) {

                        new StudentResultView(userId);
                    } else {
                        JOptionPane.showMessageDialog(this, "Only students can view their own results from here.");
                    }
                    break;

                case "Admin Result Control":
                    new AdminResultControl();
                    break;

                case "Fee Structure":
                    new FeeStructure();
                    break;

                case "Student Fee Form":
                    if ("Student".equalsIgnoreCase(userType)) {
                        new StudentFeeForm(userId);
                    } else {
                        JOptionPane.showMessageDialog(this, "Only students can access the Student Fee Form.");
                    }
                    break;

                case "About":
                    new About();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new Project("Admin", "admin_username", "rollno123");
    }
}
