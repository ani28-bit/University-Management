package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import net.proteanit.sql.DbUtils;

public class LeaveDetails extends JFrame {
    JTable table;

    public LeaveDetails(String leaveType, String userType, String username) {
        setTitle(leaveType.substring(0, 1).toUpperCase() + leaveType.substring(1) + " Leave Details");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        table = new JTable();
        JScrollPane sp = new JScrollPane(table);
        add(sp, BorderLayout.CENTER);

        try {
            Conn c = new Conn();
            String query = "";

            // Pick the right table
            if (leaveType.equalsIgnoreCase("student")) {
                query = "SELECT * FROM studentleave";
            } else if (leaveType.equalsIgnoreCase("faculty")) {
                query = "SELECT * FROM teacherleave";
            }

            // Students/Teachers only see their own records
            if (userType.equalsIgnoreCase("Student")) {
                query += " WHERE username = '" + username + "'";
            } else if (userType.equalsIgnoreCase("Teacher")) {
                query += " WHERE username = '" + username + "'";
            }

            ResultSet rs = c.s.executeQuery(query);
            table.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (Exception e) {
            e.printStackTrace();
        }

        setVisible(true);
    }
}
