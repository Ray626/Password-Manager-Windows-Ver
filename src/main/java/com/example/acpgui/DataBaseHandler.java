package com.example.acpgui;
import javafx.scene.control.Alert;

import javax.swing.*;
import java.sql.*;

public class DataBaseHandler {
    private final String DB_url = "jdbc:derby:database/user;create=true";
    private final String quit_url = "jdbc:derby:database/user;shutdown=true";
    private Connection conn = null;
    private Statement stmt = null;
    public DataBaseHandler() {
    }
    public void createConnection() {
        try {
            conn = DriverManager.getConnection(DB_url);
            System.out.println("successfully connected");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void quit(){
        try{
            DriverManager.getConnection(quit_url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Error code: " + e.getErrorCode());
            System.out.println("SQL State: " + e.getSQLState());
        }
    }
    public boolean execAction(String qu){
        try{
            stmt = conn.createStatement();
            stmt.execute(qu);
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error:" + e.getMessage(),"Error Occurred",JOptionPane.ERROR_MESSAGE);
            System.out.println("Exception at execQuery" + e.getLocalizedMessage());
            return false;
        }
    }
    public void execUpdate(String qu){
        try{
            stmt = conn.createStatement();
            stmt.executeUpdate(qu);
            System.out.println("Record updated");
        }catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error:" + e.getMessage(),"Error Occurred",JOptionPane.ERROR_MESSAGE);
            System.out.println("Exception at execQuery" + e.getLocalizedMessage());
        }
    }
    public void execPreparedStmt1(String var1, int var2) throws SQLException {
        try{
            PreparedStatement preStmt = conn.prepareStatement("insert into Users(username,password,date ) values(?, ?, CURRENT_TIMESTAMP )");
            preStmt.setString(1,var1);
            preStmt.setInt(2,var2);
            preStmt.execute();
        }catch (SQLException e){
            JOptionPane.showMessageDialog(null,"Error:" + e.getMessage(),"Error Occurred",JOptionPane.ERROR_MESSAGE);
            System.out.println("Exception at execQuery" + e.getLocalizedMessage());
        }
    }
    public void execPreparedStmt2(int var1, String var2, String var3, String var4, String var5, String var6,String var7){
        try{
            PreparedStatement preparedStmt = conn.prepareStatement("insert into PASSWORDENTRIES(owner_id, private_key, encoded_message, company, company_link, account_username,NOTE) values (?,?,?,?,?,?,?)");
            preparedStmt.setInt(1,var1);
            preparedStmt.setString(2,var2);
            preparedStmt.setString(3,var3);
            preparedStmt.setString(4,var4);
            preparedStmt.setString(5,var5);
            preparedStmt.setString(6,var6);
            preparedStmt.setString(7,var7);
            preparedStmt.execute();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error:" + e.getMessage(),"Error Occurred",JOptionPane.ERROR_MESSAGE);
            System.out.println("Exception at execQuery" + e.getLocalizedMessage());
        }
    }
    public ResultSet execPreparedStmt3(int var1){
        ResultSet resultSet;
        try{
            String stmt = "SELECT * from PASSWORDENTRIES WHERE OWNER_ID = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(stmt);
            preparedStatement.setInt(1,var1);
            resultSet = preparedStatement.executeQuery();


        } catch (SQLException exception) {
            System.out.println("Exception at Execute query");
            exception.printStackTrace();
            return null;
        }
        return resultSet;

    }

    public ResultSet execPreparedStmt4(int var1, String var2, String var3) {
        ResultSet resultSet;
        try{
            String stmt = "SELECT * from PASSWORDENTRIES WHERE OWNER_ID = ? AND COMPANY = ? AND ACCOUNT_USERNAME = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(stmt);
            preparedStatement.setInt(1,var1);
            preparedStatement.setString(2,var2);
            preparedStatement.setString(3,var3);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException exception) {
            System.out.println("Exception at Execute query");
            exception.printStackTrace();
            return null;
        }
        return resultSet;

    }

    public ResultSet execQuery(String query){
        ResultSet resultSet;
        try {
            stmt = conn.createStatement();
            resultSet = stmt.executeQuery(query);

        } catch (SQLException e) {
            System.out.println("Exception at Execute query");
            return null;
        }
        return resultSet;
    }

}
