
package university.management.system;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.LayoutManager;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class About extends JFrame {
    About() {
        this.setSize(700, 500);
        this.setLocation(400, 150);
        this.getContentPane().setBackground(Color.WHITE);


        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/pic.jpeg"));
        Image i2 = i1.getImage().getScaledInstance(300, 270, 1);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel image = new JLabel(i3);
        image.setBounds(350, 0, 300, 200);
        this.add(image);


        JLabel heading = new JLabel("<html>University<br/>Management System</html>");
        heading.setBounds(70, 20, 300, 130);
        heading.setFont(new Font("serif", 1, 30));
        this.add(heading);


        JLabel name = new JLabel("Developed By:Anita Pervin");
        name.setBounds(70, 220, 800, 40);
        name.setFont(new Font("serif", 1, 30));
        this.add(name);


        JLabel rollno = new JLabel("Roll number:1601");
        rollno.setBounds(70, 280, 550, 40);
        rollno.setFont(new Font("serif", 1, 30));
        this.add(rollno);


        JLabel contact = new JLabel("Contact: bsse1601@iit.iit.du.ac.bd " );
        contact.setBounds(70, 340, 750, 50);
        contact.setFont(new Font("serif", 1, 20));
        this.add(contact);

        JLabel thk = new JLabel("  Thank You ! " );
        thk.setBounds(90, 400, 750, 50);
        thk.setFont(new Font("serif", 1, 26));
        this.add(thk);


        this.setLayout((LayoutManager)null);
        this.setVisible(true);



    }

    public static void main(String[] args) {

        new About();
    }
}
