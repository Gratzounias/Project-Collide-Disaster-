package mysql;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;


public class InsertToDatabase {//klasi enimerwsis tis D.B. apo ta new entries
    private String sql = "INSERT INTO `mydb`.`table`(`source`," +"`accx`," +"`accy`," +"`accz`," +"`prox`," +"`lat`," +"`lng`," +"`dt`," +"`collide`)" +"VALUES" +"(" +"?," +"?," +"?," +"?," +"?," +"?," +"?," +"?," +"0);";
    // query gia pros8iki sti db
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";//make connection
    private static final String DB_CONNECTION = "jdbc:mysql://localhost/mydb";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    public String valueOfSource = null; //arxikopoiisi timwn
    public Float valueOfAccx = null;
    public Float valueOfAccy = null;
    public Float valueOfAccz = null;
    public Float valueOfProx = null;
    public Float valueOfLat = null;
    public Float valueOfLn = null;
    public Float valueOfDate = null;
    public Float valueOfCollide = null;

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

    public int insert() {// me to paraatw Statement simplirwnontai ta '?' tou arxikou sql query
        try {
            Connection dbConnection = getDBConnection();

            PreparedStatement preparedStatement = dbConnection.prepareStatement(sql);

            int fieldid = 0;

            if (valueOfSource != null) { //enimerwse t antistoixo pedio tou insert
                preparedStatement.setString(++fieldid, valueOfSource);
            }

            if (valueOfAccx != null) {
                preparedStatement.setFloat(++fieldid, valueOfAccx);
            }

            if (valueOfAccy != null) {
                preparedStatement.setFloat(++fieldid, valueOfAccy);
            }

            if (valueOfAccz != null) {
                preparedStatement.setFloat(++fieldid, valueOfAccz);
            }

            if (valueOfProx != null) {
                preparedStatement.setFloat(++fieldid, valueOfProx);
            }

            if (valueOfLat != null) {
                preparedStatement.setFloat(++fieldid, valueOfLat);
            }

            if (valueOfLn != null) {
                preparedStatement.setFloat(++fieldid, valueOfLn);
            }

                java.sql.Timestamp ts = new java.sql.Timestamp(new java.util.Date().getTime());
                preparedStatement.setTimestamp(++fieldid, ts);




            // Ektelesi insert SQL update
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
