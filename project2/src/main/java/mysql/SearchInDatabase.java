package mysql;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class SearchInDatabase {//klasi anazitisis stin D.B.

    private String sql = "SELECT * FROM mydb.`table` "; // i vasi tou erwtiatos gia ta queries tis polikritiriakis anazitisis

    private String sqlmatch = "SELECT x.id as term1id, y.id as term2id FROM " +
            "(SELECT * FROM  `table` t1 where t1.`source`= '4fa2064ab69ede57' and now()-dt < ? order by t1.dt desc limit 1) as x " +
            "cross join " +//ektelei kartetsiano ginomeno
            "(SELECT * FROM  `table` t2 where t2.`source`= 'a07ea291c136cb8f' and now()-dt < ? order by t2.dt desc limit 1) as y " +
            "where abs(timediff(x.dt, y.dt)) < 1 ";//to paraanw query epistrefei ta IDs twn 2 pio prosfatwn eggrafwn twn termatikwn 1 kai 2
    //pou exoun xroniki diafora mikroteri tou 1 sec

    private String sqlone = "select id from `table` where `source`='4fa2064ab69ede57' and now()-dt < ? order by dt desc limit 1";
   //epistrefei gia to termatiko 1 tin pio prosfati eggrafi sti vasi
    private String sqltwo = "select id from `table` where `source`='a07ea291c136cb8f' and now()-dt < ? order by dt desc limit 1";
    //epistrefei gia to termatiko 2 tin pio prosfati eggrafi sti vasi

    private int fieldid = 0;

    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver"; //connecting to DB
    private static final String DB_CONNECTION = "jdbc:mysql://localhost/mydb";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    public String valueOfSource = null;  //Arxikopoiisi tn timwn apo ta TextFields se NULL
    public Float valueOfAccx = null;
    public Float valueOfAccy = null;
    public Float valueOfAccz = null;
    public Float valueOfProx = null;
    public Float valueOfLat = null;
    public Float valueOfLn = null;
    public Float valueOfDate = null;
    public Float valueOfCollide = null;

    public Float valueOfErrorAccx = 0.5f;//Arxikopoiisi tn timwn apo ta TextFields
    public Float valueOfErrorAccy = 0.5f;
    public Float valueOfErrorAccz = 0.5f;
    public Float valueOfErrorProx = 0.5f;
    public Float valueOfErrorLat = 0.5f;
    public Float valueOfErrorLn = 0.5f;
    public Float valueOfErrorDate = 0.5f;

    private Connection getDBConnection() { //dimiourgia sindesis me ti vasi
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


    private void addSqlString(String column) {//dimiourgia dinamikou query g String
        if (fieldid == 0) {
            sql += " WHERE ";
        } else {
            sql += " and ";
        }
        fieldid++;

        sql += column + " = ? ";
    }

    private void addSqlFloat(String column) {//dimiourgia dinamikou query g Float
        if (fieldid == 0) {
            sql += " WHERE ";
        } else {
            sql += " and ";
        }
        fieldid++;

        sql += column + " >= ? and " + column + " <= ? ";
    }

    private void addSqlFloatEquality(String column) {//dimiourgia dinamikou query g to collide
        if (fieldid == 0) {
            sql += " WHERE ";
        } else {
            sql += " and ";
        }
        fieldid++;

        sql += column + " = ? ";
    }

    private void addSqlDate(String column) { //dimiourgia dinamikou query g date
        if (fieldid == 0) {
            sql += " WHERE ";
        } else {
            sql += " and ";
        }
        fieldid++;
        sql += column + " >= ? and " + column + " <= ? ";
    }

    public ObservableList<DataModel> fetch() {
        try {
            ObservableList<DataModel> list =FXCollections.observableArrayList();

            Connection dbConnection = getDBConnection(); //1st connect to database

            //simplirwsi query (' ?  ) polikritiriakis anazitisis analoga me tis epiloges tou xristi

            if (valueOfSource != null) {//ean o xristis exei simpirwsi anazitisi vasei tou sourceto antistoixo pedio parakatw
                addSqlString("source");// sto sql erwtima pros8ese to source 'i to antistoixo kritirio anazitisis(parakatw)
            }

            // float
            if (valueOfAccx != null && valueOfErrorAccx != null) { //accx
                addSqlFloat("accx");
            }

            if (valueOfAccy != null && valueOfErrorAccy != null) {//accy
                addSqlFloat("accy");
            }

            if (valueOfAccz != null && valueOfErrorAccz != null) { //accz
                addSqlFloat("accz");
            }

            if (valueOfProx != null && valueOfErrorProx != null) {//prox
                addSqlFloat("prox");
            }

            if (valueOfLat != null && valueOfErrorLat != null) {//lat
                addSqlFloat("lat");
            }

            if (valueOfLn != null && valueOfErrorLn != null) {//ln
                addSqlFloat("lng");
            }

            if (valueOfCollide != null ){//collide
                addSqlFloatEquality("collide");
            }

            if (valueOfDate != null && valueOfErrorDate != null) {//date
                addSqlDate("dt");
            }

            //dimiourgia dinamikou query polikritiriakis anazitisis analoga
            //kai me to an o xristis exei dwsei peri8wrio apoklisis apo tin timi pou edwse

            PreparedStatement preparedStatement = dbConnection.prepareStatement(sql);

            fieldid = 0;
            if (valueOfSource != null) {
                preparedStatement.setString(++fieldid, valueOfSource);
            }

            if (valueOfAccx != null && valueOfErrorAccx != null) {
                preparedStatement.setFloat(++fieldid, valueOfAccx-valueOfErrorAccx);
                preparedStatement.setFloat(++fieldid, valueOfAccx+valueOfErrorAccx);
            }

            if (valueOfAccy != null && valueOfErrorAccy != null) {
                preparedStatement.setFloat(++fieldid, valueOfAccy-valueOfErrorAccy);
                preparedStatement.setFloat(++fieldid, valueOfAccy+valueOfErrorAccy);
            }

            if (valueOfAccz != null && valueOfErrorAccz != null) {
                preparedStatement.setFloat(++fieldid, valueOfAccz-valueOfErrorAccz);
                preparedStatement.setFloat(++fieldid, valueOfAccz+valueOfErrorAccz);
            }

            if (valueOfProx != null && valueOfErrorProx != null) {
                preparedStatement.setFloat(++fieldid, valueOfProx-valueOfErrorProx);
                preparedStatement.setFloat(++fieldid, valueOfProx+valueOfErrorProx);
            }
            if (valueOfLat != null && valueOfErrorLat != null) {
                preparedStatement.setFloat(++fieldid, valueOfLat-valueOfErrorLat);
                preparedStatement.setFloat(++fieldid, valueOfLat+valueOfErrorLat);
            }

            if (valueOfLn != null && valueOfErrorLn != null) {
                preparedStatement.setFloat(++fieldid, valueOfLn-valueOfErrorLn);
                preparedStatement.setFloat(++fieldid, valueOfLn+valueOfErrorLn);
            }

            if (valueOfCollide != null){
                preparedStatement.setFloat(++fieldid, valueOfCollide);

            }
            if (valueOfDate != null && valueOfErrorDate != null) {
                preparedStatement.setFloat(++fieldid, valueOfDate-valueOfErrorDate);
                preparedStatement.setFloat(++fieldid, valueOfDate+valueOfErrorDate);
            }


            // ektelesi twn querys

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) { //pairnei tis stiles tis vasis sta "",prosoxi sto seplling !!!
                String dbsource = rs.getString("source");
                String dbaccx = rs.getString("accx");
                String dbaccy = rs.getString("accy");
                String dbaccz = rs.getString("accz");
                String dbprox = rs.getString("prox");
                String dblat = rs.getString("lat");
                String dbln = rs.getString("lng");
                String dbdate = rs.getString("dt");
                String dbcollide = rs.getString("collide");

                DataModel e = new DataModel(dbsource,dbaccx,dbaccy,dbaccz,dbprox,dblat,dbln,dbdate,dbcollide);

                list.addAll(e);
            }

            dbConnection.close();

            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
            return null;
        }
    }

    public long[] fetchNearRows(int seconds) {
        try {
            Connection dbConnection = getDBConnection();
            //ektelesi tou query epistrefei ta IDs twn 2 pio prosfatwn eggrafwn twn termatikwn 1 kai 2
            //pou exoun xroniki diafora mikroteri tou 1 sec
            PreparedStatement preparedStatement = dbConnection.prepareStatement(sqlmatch);

            preparedStatement.setInt(1,seconds);
            preparedStatement.setInt(2,seconds);

            ResultSet rs = preparedStatement.executeQuery();//ekte;esi tou query




            long[] results;
            if (rs.next()) { //pairnei tis stiles tis vasis sta "",ki to spelling!!!
                results = new long[2];
                results[0] = rs.getLong("term1id");
                results[1] = rs.getLong("term2id");
                return results;
            } else {
                results = null;
            }

            dbConnection.close();

            return results;
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
            return null;
        }
    }

    public long fetchForTerminal1(int seconds) {
        try {
            Connection dbConnection = getDBConnection();

            PreparedStatement preparedStatement = dbConnection.prepareStatement(sqlone);//anazitisi gia tin pio prosfati eggrafi tou 1ou termatikou


            preparedStatement.setInt(1,seconds);

            ResultSet rs = preparedStatement.executeQuery();

            long results;
            if (rs.next()) { //pairnei tis stiles tis vasis sta ""
                results = rs.getLong("id");
                return results;
            } else {
                results = -1;
            }

            dbConnection.close();

            return results;
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
            return -1;
        }
    }

    public long fetchForTerminal2(int seconds) {
        try {
            Connection dbConnection = getDBConnection();

            PreparedStatement preparedStatement = dbConnection.prepareStatement(sqltwo);//anazitisi gia tin pio prosfati eggrafi tou 2ou termatikou

            preparedStatement.setInt(1,seconds);

            ResultSet rs = preparedStatement.executeQuery();

            long results;
            if (rs.next()) { //pairnei tis stiles tis vasis sta "",ki to spelling!!!
                results = rs.getLong("id");
                return results;
            } else {
                results = -1;
            }

            dbConnection.close();

            return results;
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
            return -1;
        }
    }

}
