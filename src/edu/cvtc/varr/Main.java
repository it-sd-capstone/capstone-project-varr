package edu.cvtc.varr;

import java.sql.*;

public class Main {
    public static final String DATABASE_NAME = "projectVarr ";
    public static final String DATABASE_PATH = DATABASE_NAME + ".db";
    private static final int TIMEOUT_STATEMENT_S = 5;


    public static void main(String[] args) {
        System.out.println("Hello world!");
    }

    public static Connection createConnection() {
        Connection result = null;
        try {
            result = DriverManager.getConnection("jdbc:sqlite:" + DATABASE_PATH);
            Statement command = result.createStatement();
            command.setQueryTimeout(TIMEOUT_STATEMENT_S);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }


    public static ResultSet queryRaw(Connection db, String sql) {
        ResultSet result = null;
        try {
            Statement statement = db.createStatement();
            result = statement.executeQuery(sql);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
}