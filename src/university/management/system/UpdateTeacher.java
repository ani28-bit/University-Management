package university.management.system;

import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import javax.swing.*;

public class UpdateTeacher extends JFrame implements ActionListener {

    JTextField tfbranch, tfcourse, tfaddress1, tfaddress2, tfphone, tfemail;

    JLabel labelempId, labelname, labelfname, labelmname, labeldob, labelx, labelxii, labelidNum;
    JButton submit, cancel;
    Choice cempId;


    public UpdateTeacher() {
        initUI();

        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("SELECT * FROM teacher");
            while (rs.next()) {
                cempId.add(rs.getString("empId"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (cempId.getItemCount() > 0) {
            loadTeacherData(cempId.getSelectedItem());
        }


        cempId.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                loadTeacherData(cempId.getSelectedItem());
            }
        });
    }


    public UpdateTeacher(String empId) {
        initUI();


        cempId.add(empId);
        cempId.setEnabled(false);

        loadTeacherData(empId);
    }

    private void initUI() {
        setSize(900, 650);
        setLocation(350, 50);
        setLayout(null);

        JLabel heading = new JLabel("Update Teacher Details");
        heading.setBounds(50, 10, 500, 50);
        heading.setFont(new Font("serif", Font.BOLD, 35));
        add(heading);

        JLabel lblrollnumber = new JLabel("Select Employee Id");
        lblrollnumber.setBounds(50, 70, 200, 20);
        lblrollnumber.setFont(new Font("serif", Font.PLAIN, 20));
        add(lblrollnumber);

        cempId = new Choice();
        cempId.setBounds(260, 71, 250, 20);
        add(cempId);

        labelname = createLabel("", 230, 100, 250, 30, 18);
        addLabel("Name :", 50, 100);
        add(labelname);

        labelempId = createLabel("", 230, 150, 150, 30, 20);
        addLabel("Employee Id :", 50, 150);
        add(labelempId);

        tfaddress1 = createTextField(230, 200);
        addLabel("Present Address :", 50, 200);
        add(tfaddress1);

        tfaddress2 = createTextField(230, 250);
        addLabel("Permanent Address:", 50, 250);
        add(tfaddress2);



       labelx = createLabel("", 230, 300, 250, 30, 20);
        addLabel("Class X (%):", 50, 300);
        add(labelx);

        labelxii = createLabel("", 230, 350, 250, 30, 20);
        addLabel("Class XII (%):", 50, 350);
        add(labelxii);

        tfcourse = createTextField(230, 400);
        addLabel("Qualification :", 50, 400);
        add(tfcourse);

        labelfname = createLabel("", 700, 100, 250, 30, 20);
        addLabel("Father's Name :", 500, 100);
        add(labelfname);

        labelmname = createLabel("", 700, 150, 250, 30, 20);
        addLabel("Mother's Name :", 500, 150);
        add(labelmname);



        labeldob = createLabel("", 700, 200, 250, 30, 20);
        addLabel("Date of Birth :", 500, 200);
        add(labeldob);

        tfemail = createTextField(700, 250);
        addLabel("Email ID :", 500, 250);
        add(tfemail);

        tfphone = createTextField(700, 300);
        addLabel("Phone Number :", 500, 300);
        add(tfphone);

        labelidNum = createLabel("", 700, 350, 250, 30, 20);
        addLabel("National ID Number :", 500, 350);
        add(labelidNum);

        tfbranch = createTextField(700, 400);
        addLabel("Department :", 500, 400);
        add(tfbranch);

        submit = new JButton("Update");
        submit.setBounds(350, 500, 120, 35);
        submit.setBackground(Color.GREEN);
        submit.setForeground(Color.WHITE);
        submit.setFont(new Font("serif", Font.BOLD, 15));
        submit.addActionListener(this);
        add(submit);

        cancel = new JButton("Cancel");
        cancel.setBounds(550, 500, 120, 35);
        cancel.setBackground(Color.RED);
        cancel.setForeground(Color.WHITE);
        cancel.setFont(new Font("serif", Font.BOLD, 15));
        cancel.addActionListener(this);
        add(cancel);

        setVisible(true);
    }


    private void loadTeacherData(String empId) {
        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("SELECT * FROM teacher WHERE empId='" + empId + "'");
            if (rs.next()) {
                labelname.setText(rs.getString("name"));
                labelname.setText(rs.getString("fname"));
                labelmname.setText(rs.getString("mname"));
                labeldob.setText(rs.getString("dob"));
                tfaddress1.setText(rs.getString("address1"));
                tfaddress2.setText(rs.getString("address2"));
                tfphone.setText(rs.getString("phone"));
                tfemail.setText(rs.getString("email"));
                labelx.setText(rs.getString("x"));
                labelxii.setText(rs.getString("xii"));
                labelidNum.setText(rs.getString("idNum"));
                labelempId.setText(rs.getString("empId"));
                tfcourse.setText(rs.getString("qualification"));
                tfbranch.setText(rs.getString("department"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private JLabel createLabel(String text, int x, int y, int w, int h, int fontSize) {
        JLabel lbl = new JLabel(text);
        lbl.setBounds(x, y, w, h);
        lbl.setFont(new Font("serif", Font.BOLD, fontSize));
        return lbl;
    }

    private void addLabel(String text, int x, int y) {
        JLabel lbl = new JLabel(text);
        lbl.setBounds(x, y, 200, 30);
        lbl.setFont(new Font("serif", Font.BOLD, 20));
        add(lbl);
    }

    private JTextField createTextField(int x, int y) {
        JTextField tf = new JTextField();
        tf.setBounds(x, y, 250, 30);
        return tf;
    }

    // ====== Update Button Action ======
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == submit) {
            String empId = labelempId.getText();
            String address1 = tfaddress1.getText();
            String address2 = tfaddress2.getText();
            String phone = tfphone.getText();
            String email = tfemail.getText();
            String course = tfcourse.getText();
            String branch = tfbranch.getText();

            try {
                String query = "UPDATE teacher SET address1='" + address1 + "', address2='" + address2 +
                        "', phone='" + phone + "', email='" + email + "', qualification='" + course +
                        "', department='" + branch + "'WHERE empId='" + empId + "'";
                Conn con = new Conn();
                con.s.executeUpdate(query);
                JOptionPane.showMessageDialog(null, "Teacher Details Updated Successfully");
                setVisible(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            setVisible(false);
        }
    }

    public static void main(String[] args) {

        new UpdateTeacher("1011550");
    }
}
