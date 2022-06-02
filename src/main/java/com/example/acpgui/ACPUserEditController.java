package com.example.acpgui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class ACPUserEditController {
    @FXML
    private TextField username,password;

    private DataBaseHandler dataBaseHandler;

    public ACPUserEditController(){
        dataBaseHandler = new DataBaseHandler();

    }



    //Unfinished: check if username is taken
    public void applyOnClick() throws SQLException {
        dataBaseHandler.createConnection();
        String usernameEdited = username.getText();
       String passwordS = password.getText();
        int passwordEdited = passwordS.hashCode();
        int id = ACPLoginController.loginId;
        boolean check = false;
        String query = "SELECT USERNAME FROM USERS";
        ResultSet resultSet = dataBaseHandler.execQuery(query);
        while (resultSet.next()){
            check = usernameEdited.equals(resultSet.getString("username"));
            if (check){
                break;
            }
        }
        if(check){
            System.out.println("User Name is already taken.");

        }else {
            String qu = "UPDATE USERS SET USERNAME = " + "'" + usernameEdited + "'," +  " PASSWORD = "+ passwordEdited +" WHERE USER_ID = " +  id;
            dataBaseHandler.execUpdate(qu);
            ACPLoginController.sPassWordT = passwordS;
        }
        dataBaseHandler.quit();
        username.setText("");
        password.setText("");


    }

        public void cancelOnClick(ActionEvent event) throws IOException {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("acpgui-userProfile.fxml")));
            Scene scene = new Scene(root,600,500);
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Profile");
            stage.getIcons().add( new Image(String.valueOf(getClass().getResource("/com/example/acpgui/imgResources/p-icon.png"))));
            stage.show();
    }
}
