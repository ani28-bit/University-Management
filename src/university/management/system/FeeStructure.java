
package university.management.system;

import java.awt.Color;
import java.awt.Font;
import java.awt.LayoutManager;
import java.sql.ResultSet;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import net.proteanit.sql.DbUtils;

public class FeeStructure extends JFrame {
    FeeStructure() {
        this.setSize(1000, 700);
        this.setLocation(250, 50);
        this.setLayout((LayoutManager)null);


        this.getContentPane().setBackground(Color.WHITE);
        JLabel heading = new JLabel("Fee Structure");
        heading.setBounds(50, 10, 400, 30);
        heading.setFont(new Font("Tahoma", 1, 30));
        this.add(heading);

        JTable table = new JTable();

        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("select * from fee");
            table.setModel(DbUtils.resultSetToTableModel(rs));
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        JScrollPane jsp = new JScrollPane(table);
        jsp.setBounds(0, 60, 1000, 700);
        this.add(jsp);
        this.setVisible(true);
    }

    public static void main(String[] args) {

        new FeeStructure();
    }
}
