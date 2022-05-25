package com.example.acpgui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class ACPLoginController {
    @FXML
    private TextField userName, passWord;
    private final DataBaseHandler dataBaseHandler ;
    public static int loginId;
    public static String sPassWordT;


    public ACPLoginController() {
        dataBaseHandler = new DataBaseHandler();
    }

    public void LoginOnClick(ActionEvent event) throws SQLException, IOException {
        dataBaseHandler.createConnection();
        String userNameT = userName.getText();
        sPassWordT = passWord.getText();
        int passWordT = sPassWordT.hashCode();
        String query1 = "SELECT USER_ID, username, password FROM users";
        ResultSet resultSet = dataBaseHandler.execQuery(query1);
        boolean check;
        boolean check2;
        String reason = null;
        while(resultSet.next()){
            reason = "";
            check = userNameT.equals(resultSet.getString("username"));
            if (check) {
                check2 = passWordT == resultSet.getInt("password");
                if (check2){
                    reason = "success";
                    loginId = resultSet.getInt("user_id");
                    System.out.println(loginId);
                    dataBaseHandler.quit();
                    mainPageSwitchOnClick(event);
                }else{
                    reason = "Password is incorrect";

                }
                break;
            }else {
                reason = "User Name is invalid";
            }
        }
        System.out.println(reason);
    }
    public void registerSwitchOnClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("acpgui-register.fxml")));
        Scene scene = new Scene(root,500,400);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow() ;
        stage.setScene(scene);
        stage.setTitle("Register");
        stage.show();
    }

    private void mainPageSwitchOnClick(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("acpgui-mainPage.fxml")));
        Scene scene = new Scene(root,600,500);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow() ;
        stage.setScene(scene);
        stage.setTitle("Main page");
        stage.show();


    }

}