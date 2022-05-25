package com.example.acpgui;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class ACPRegisterController {

    @FXML
    private TextField userNameRe,passwordRe;
    private final DataBaseHandler dataBaseHandler;

    public ACPRegisterController() throws SQLException {
        dataBaseHandler = new DataBaseHandler();
    }

    public void signUpOnClick() throws SQLException {

        dataBaseHandler.createConnection();
        String userNameR = userNameRe.getText();
        int passWordR = passwordRe.getText().hashCode();
        String query = "SELECT USERNAME FROM USERS";
        ResultSet resultSet = dataBaseHandler.execQuery(query);
        boolean check = false;
        while (resultSet.next()){
            check = userNameR.equals(resultSet.getString("username"));
            if (check){
                break;
            }
        }
        if(check){
            System.out.println("User Name is already taken.");

        }else {
            dataBaseHandler.execPreparedStmt1(userNameR, passWordR);
        }
        dataBaseHandler.quit();

    }
    public void logInSwitchOnClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("acpgui-login.fxml")));
        Scene scene = new Scene(root,500,400);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow() ;
        stage.setScene(scene);
        stage.setTitle("Login");
        stage.show();
    }

}
