package mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by panagiotis on 12/1/2017.
 */
public class UpdateDatabase {
    private String sql = "UPDATE `mydb`.`table` set collide = 1 where id = ? and collide = 0";

    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_CONNECTION = "jdbc:mysql://localhost/mydb";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    private Connection getDBConnection() {
        Connection dbConnection = null;

        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(0);
        }

        try {
            dbConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER,DB_PASSWORD);
            return dbConnection;

        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }

        return dbConnection;
    }

    public int update(long id) {
        try {
            Connection dbConnection = getDBConnection();

            // -------------------   fill questionmarks statement --------------------
            PreparedStatement preparedStatement = dbConnection.prepareStatement(sql);
            preparedStatement.setLong(1, id);


            // execute insertSQL stetement
            int x = preparedStatement.executeUpdate();

            dbConnection.close();

            return x;
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
            return 0;
        }
    }
}
