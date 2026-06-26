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

public class StudentDetails extends JFrame implements ActionListener {
    Choice crollno;
    JTable table;
    JButton search;
    JButton print;
    JButton update;
    JButton add;
    JButton cancel;

    StudentDetails() {
        getContentPane().setBackground(Color.WHITE);
        setLayout((LayoutManager) null);

        JLabel heading = new JLabel("Search by Roll Number");
        heading.setBounds(20, 20, 150, 20);
        add(heading);

        crollno = new Choice();
        crollno.setBounds(180, 20, 150, 20);
        add(crollno);

        // Load roll numbers in the dropdown
        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("SELECT * FROM student");
            while (rs.next()) {
                crollno.add(rs.getString("rollno"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Table setup
        table = new JTable();

        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("SELECT * FROM student");
            table.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Allow horizontal scrolling
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // Put table inside scroll pane
        JScrollPane jsp = new JScrollPane(table,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                jsp.setBounds(0, 100, 900, 600);
                add(jsp);

        // Optional: Set better column widths
        if (table.getColumnModel().getColumnCount() > 0) {
            table.getColumnModel().getColumn(0).setPreferredWidth(50);   // id
            table.getColumnModel().getColumn(1).setPreferredWidth(200);  // name
            table.getColumnModel().getColumn(2).setPreferredWidth(200);  // father_name
            table.getColumnModel().getColumn(3).setPreferredWidth(200);  // mother_name
            table.getColumnModel().getColumn(4).setPreferredWidth(100);  // rollno
            table.getColumnModel().getColumn(5).setPreferredWidth(100);  // dob
            table.getColumnModel().getColumn(6).setPreferredWidth(200);  // address1
            table.getColumnModel().getColumn(7).setPreferredWidth(200);  // address2
            table.getColumnModel().getColumn(8).setPreferredWidth(120);  // phone
            table.getColumnModel().getColumn(9).setPreferredWidth(200);  // email
        }

        // Buttons
        search = new JButton("Search");
        search.setBounds(20, 70, 80, 20);
        search.addActionListener(this);
        add(search);

        print = new JButton("Print");
        print.setBounds(120, 70, 80, 20);
        print.addActionListener(this);
        add(print);

        add = new JButton("Add");
        add.setBounds(220, 70, 80, 20);
        add.addActionListener(this);
        add(add);

        update = new JButton("Update");
        update.setBounds(320, 70, 80, 20);
        update.addActionListener(this);
        add(update);

        cancel = new JButton("Cancel");
        cancel.setBounds(420, 70, 80, 20);
        cancel.addActionListener(this);
        add(cancel);

        setSize(900, 700);
        setLocation(300, 100);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == search) {
            String query = "SELECT * FROM student WHERE rollno = '" + crollno.getSelectedItem() + "'";
            try {
                Conn c = new Conn();
                ResultSet rs = c.s.executeQuery(query);
                table.setModel(DbUtils.resultSetToTableModel(rs));

                // Keep column sizes after search
                table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (ae.getSource() == print) {
            try {
                table.print();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (ae.getSource() == add) {
            setVisible(false);
            new AddStudent();
        } else if (ae.getSource() == update) {
            setVisible(false);
            new UpdateStudent();
        } else {
            setVisible(false);
        }
    }

    public static void main(String[] args) {
        new StudentDetails();
    }
}
