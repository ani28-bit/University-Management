package university.management.system;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Project extends JFrame implements ActionListener {
    private String userType;

    Project(String userType) {
        this.userType = userType;
        this.setSize(1540, 850);
        this.setTitle("University Management System - Logged in as: " + userType);

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/35ab518f-0321-4435-a38a-69fe39d8207a.jpeg"));
        Image i2 = i1.getImage().getScaledInstance(1500, 750, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel image = new JLabel(i3);
        this.add(image);

        JMenuBar mb = new JMenuBar();

        // New Information Menu
        JMenu newInformation = new JMenu("New Information");
        newInformation.setForeground(Color.BLUE);
        mb.add(newInformation);

        JMenuItem facultyInfo = new JMenuItem("New Faculty Information");
        facultyInfo.setBackground(Color.WHITE);
        facultyInfo.addActionListener(this);
        newInformation.add(facultyInfo);

        JMenuItem studentInfo = new JMenuItem("New Student Information");
        studentInfo.setBackground(Color.WHITE);
        studentInfo.addActionListener(this);
        newInformation.add(studentInfo);

        // View Details Menu
        JMenu details = new JMenu("View Details");
        details.setForeground(Color.RED);
        mb.add(details);

        JMenuItem facultydetails = new JMenuItem("View Faculty Details");
        facultydetails.setBackground(Color.WHITE);
        facultydetails.addActionListener(this);
        details.add(facultydetails);

        JMenuItem studentdetails = new JMenuItem("View Student Details");
        studentdetails.setBackground(Color.WHITE);
        studentdetails.addActionListener(this);
        details.add(studentdetails);

        // Apply Leave Menu
        JMenu leave = new JMenu("Apply Leave");
        leave.setForeground(Color.BLUE);
        mb.add(leave);

        JMenuItem facultyleave = new JMenuItem("Faculty Leave");
        facultyleave.setBackground(Color.WHITE);
        facultyleave.addActionListener(this);
        leave.add(facultyleave);

        JMenuItem studentleave = new JMenuItem("Student Leave");
        studentleave.setBackground(Color.WHITE);
        studentleave.addActionListener(this);
        leave.add(studentleave);

        // Leave Details Menu
        JMenu leaveDetails = new JMenu("Leave Details");
        leaveDetails.setForeground(Color.RED);
        mb.add(leaveDetails);

        JMenuItem facultyleavedetails = new JMenuItem("Faculty Leave Details");
        facultyleavedetails.setBackground(Color.WHITE);
        facultyleavedetails.addActionListener(this);
        leaveDetails.add(facultyleavedetails);

        JMenuItem studentleavedetails = new JMenuItem("Student Leave Details");
        studentleavedetails.setBackground(Color.WHITE);
        studentleavedetails.addActionListener(this);
        leaveDetails.add(studentleavedetails);

        // Examination Menu
        JMenu exam = new JMenu("Examination");
        exam.setForeground(Color.BLUE);
        mb.add(exam);

        JMenuItem examinationdetails = new JMenuItem("Examination Results");
        examinationdetails.setBackground(Color.WHITE);
        examinationdetails.addActionListener(this);
        exam.add(examinationdetails);

        JMenuItem entermarks = new JMenuItem("Enter Marks");
        entermarks.setBackground(Color.WHITE);
        entermarks.addActionListener(this);
        exam.add(entermarks);

        // Update Details Menu
        JMenu updateInfo = new JMenu("Update Details");
        updateInfo.setForeground(Color.RED);
        mb.add(updateInfo);

        JMenuItem updatefacultyinfo = new JMenuItem("Update Faculty Details");
        updatefacultyinfo.setBackground(Color.WHITE);
        updatefacultyinfo.addActionListener(this);
        updateInfo.add(updatefacultyinfo);

        JMenuItem updatestudentinfo = new JMenuItem("Update Student Details");
        updatestudentinfo.setBackground(Color.WHITE);
        updatestudentinfo.addActionListener(this);
        updateInfo.add(updatestudentinfo);

        // Fee Details Menu
        JMenu fee = new JMenu("Fee Details");
        fee.setForeground(Color.BLUE);
        mb.add(fee);

        JMenuItem feestructure = new JMenuItem("Fee Structure");
        feestructure.setBackground(Color.WHITE);
        feestructure.addActionListener(this);
        fee.add(feestructure);

        JMenuItem feeform = new JMenuItem("Student Fee Form");
        feeform.setBackground(Color.WHITE);
        feeform.addActionListener(this);
        fee.add(feeform);

        // Utility Menu
        JMenu utility = new JMenu("Utility");
        utility.setForeground(Color.RED);
        mb.add(utility);

        JMenuItem notepad = new JMenuItem("Notepad");
        notepad.setBackground(Color.WHITE);
        notepad.addActionListener(this);
        utility.add(notepad);

        JMenuItem calc = new JMenuItem("Calculator");
        calc.setBackground(Color.WHITE);
        calc.addActionListener(this);
        utility.add(calc);

        // About Menu
        JMenu about = new JMenu("About");
        about.setForeground(Color.BLUE);
        mb.add(about);

        JMenuItem ab = new JMenuItem("About");
        ab.setBackground(Color.WHITE);
        ab.addActionListener(this);
        about.add(ab);

        // Exit Menu
        JMenu exit = new JMenu("Exit");
        exit.setForeground(Color.RED);
        mb.add(exit);

        JMenuItem ex = new JMenuItem("Exit");
        ex.setBackground(Color.WHITE);
        ex.addActionListener(this);
        exit.add(ex);

        // Set menu bar
        this.setJMenuBar(mb);

        // Apply role-based access control
        applyUserPermissions();

        this.setVisible(true);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize window
    }

    private void applyUserPermissions() {
        switch(userType) {
            case "Student":
                // Disable admin/teacher specific menus
                disableMenuItems(new String[]{
                        "New Faculty Information",
                        "New Student Information",
                        "View Faculty Details",
                        "Faculty Leave",
                        "Faculty Leave Details",
                        "Enter Marks",
                        "Update Faculty Details",
                        "Update Student Details"
                });
                break;

            case "Teacher":
                // Disable admin specific menus
                disableMenuItems(new String[]{
                        "New Faculty Information",
                        "New Student Information",
                        "Update Faculty Details",
                        "Update Student Details"
                });
                break;

            // Admin has full access
        }
    }

    private void disableMenuItems(String[] menuItems) {
        Component[] menus = this.getJMenuBar().getComponents();
        for (Component menu : menus) {
            if (menu instanceof JMenu) {
                Component[] items = ((JMenu)menu).getMenuComponents();
                for (Component item : items) {
                    if (item instanceof JMenuItem) {
                        JMenuItem menuItem = (JMenuItem)item;
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

    public void actionPerformed(ActionEvent ae) {
        String msg = ae.getActionCommand();

        try {
            switch(msg) {
                case "Exit":
                    this.setVisible(false);
                    new Login().setVisible(true); // Return to login screen
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
                    new TeacherLeave();
                    break;

                case "Student Leave":
                    new StudentLeave();
                    break;

                case "Faculty Leave Details":
                    new TeacherLeaveDetails();
                    break;

                case "Student Leave Details":
                    new StudentLeaveDetails();
                    break;

                case "Update Faculty Details":
                    new UpdateTeacher();
                    break;

                case "Update Student Details":
                    new UpdateStudent();
                    break;

                case "Enter Marks":
                    new EnterMarks();
                    break;

                case "Examination Results":
                    new ExaminationDetails();
                    break;

                case "Fee Structure":
                    new FeeStructure();
                    break;

                case "About":
                    new About();
                    break;

                case "Student Fee Form":
                    new StudentFeeForm();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // For testing only - normally launched from Login class
        new Project("Admin");
    }
}