package university.management.system;

import javax.swing.*;
import java.awt.*;

public class Splash extends JFrame implements Runnable {
    Thread t;
    private JLabel roleLabel;

    Splash() {
        // Frame setup
        setUndecorated(true);
        getContentPane().setBackground(new Color(0, 41, 204, 255));
        setLayout(null);

        // University Logo
        ImageIcon logoIcon = new ImageIcon(ClassLoader.getSystemResource("icons/Dhaka University.jpeg"));
        Image scaledLogo = logoIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(scaledLogo));
        logoLabel.setBounds(400, 100, 200, 200);
        add(logoLabel);

        // Role Loading Label
        roleLabel = new JLabel("Starting University Management System...");
        roleLabel.setBounds(350, 320, 400, 20);
        roleLabel.setForeground(Color.WHITE);
        roleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        add(roleLabel);


        t = new Thread(this);
        t.start();

        setSize(1000, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void run() {
        try {
            for (int i = 2, a = 1; i <= 600; i += 4, a += 1) {

                setSize(i + 3 * a, i + a / 2);


                setLocationRelativeTo(null);

                Thread.sleep(15);
            }

            Thread.sleep(1000);

            setVisible(false);
            new Login();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        new Splash();
    }
}
