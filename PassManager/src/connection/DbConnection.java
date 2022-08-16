package connection;

import com.oxtise.PassManager;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DbConnection {
    public static Connection connect() {
        Connection con = null;
        //System.out.println("Connessione al database in corso...");
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:"+PassManager.file_config.getParent()+"/data.db");
            System.out.println(PassManager.ANSI_GREEN + PassManager.ANSI_CHECKMARK + " Connesso al database" + PassManager.ANSI_RESET + "\n--------------------");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(PassManager.ANSI_RED+"x Connessione non riuscita "+PassManager.ANSI_RESET);
        }
        return con;
    }
    public static void createTable() {
        Connection con = connect();
        String sql = "CREATE TABLE IF NOT EXISTS Account " +
                "(ID    INTEGER," +
                " title           TEXT    NOT NULL, " +
                " username            TEXT, " +
                " email        TEXT, " +
                " pwd         TEXT  NOT NULL," +
                "PRIMARY KEY(ID AUTOINCREMENT))";
        try {
            Statement stmt = con.createStatement();
            stmt.execute(sql);
            stmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
