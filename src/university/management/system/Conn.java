

package university.management.system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Conn {
    Connection c;
    Statement s;

    Conn() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.c = DriverManager.getConnection("jdbc:mysql:///universitymanagementsystem", "root", "anita@pervin1601");
            this.s = this.c.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
