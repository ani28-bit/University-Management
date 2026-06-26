
package university.management.system;

import java.awt.Choice;
import java.awt.Color;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import net.proteanit.sql.DbUtils;

public class TeacherLeaveDetails extends JFrame implements ActionListener {
    Choice cEmpId;
    JTable table;
    JButton search;
    JButton print;
    JButton cancel;

    TeacherLeaveDetails() {
        this.getContentPane().setBackground(Color.WHITE);
        this.setLayout((LayoutManager)null);

        JLabel heading = new JLabel("Search by Employee Id");
        heading.setBounds(20, 20, 150, 20);
        this.add(heading);
        this.cEmpId = new Choice();
        this.cEmpId.setBounds(180, 20, 150, 20);
        this.add(this.cEmpId);

        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("select * from teacher");

            while(rs.next()) {
                this.cEmpId.add(rs.getString("empId"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.table = new JTable();

        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("select * from teacherleave");
            this.table.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            e.printStackTrace();
        }

        JScrollPane jsp = new JScrollPane(this.table);
        jsp.setBounds(0, 100, 900, 600);
        this.add(jsp);
        this.search = new JButton("Search");
        this.search.setBounds(20, 70, 80, 20);
        this.search.addActionListener(this);
        this.add(this.search);


        this.print = new JButton("Print");
        this.print.setBounds(120, 70, 80, 20);
        this.print.addActionListener(this);
        this.add(this.print);


        this.cancel = new JButton("Cancel");
        this.cancel.setBounds(220, 70, 80, 20);
        this.cancel.addActionListener(this);
        this.add(this.cancel);

        this.setSize(900, 700);
        this.setLocation(300, 100);
        this.setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == this.search) {
            String query = "select * from teacherleave where empId = '" + this.cEmpId.getSelectedItem() + "'";

            try {
                Conn c = new Conn();
                ResultSet rs = c.s.executeQuery(query);
                this.table.setModel(DbUtils.resultSetToTableModel(rs));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (ae.getSource() == this.print) {
            try {
                this.table.print();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            this.setVisible(false);
        }

    }

    public static void main(String[] args) {
        new TeacherLeaveDetails();
    }
}
