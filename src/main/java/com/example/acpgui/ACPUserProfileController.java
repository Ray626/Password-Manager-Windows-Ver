package com.example.acpgui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.ResourceBundle;


public class ACPUserProfileController implements Initializable {
    @FXML
    private ImageView passwordImg;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField textField;

    @FXML
    private Label theUsername, theTime;

    @FXML
    private Label recordSaved;

    private int mode = 0;
    private DataBaseHandler dataBaseHandler;


    public ACPUserProfileController() throws SQLException {
        dataBaseHandler = new DataBaseHandler();

    }


    public void passwordHideOnClick(){
        if(mode == 0){

            mode = 1;
            passwordImg.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/acpgui/imgResources/eyeOpened.png"))));
            String Password = passwordField.getText();
            passwordField.setDisable(true);
            passwordField.setVisible(false);
            textField.setDisable(false);
            textField.setText(Password);
            textField.setVisible(true);

        }else{
            mode = 0;
            passwordImg.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/acpgui/imgResources/eyeClosed.png"))));
            String thePassword = textField.getText();
            textField.setDisable(true);
            textField.setVisible(false);
            passwordField.setDisable(false);
            passwordField.setText(thePassword);
            passwordField.setVisible(true);
        }
    }
    public void backOnClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("acpgui-mainPage.fxml")));
        Scene scene = new Scene(root,600,500);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene.getClass().getResource("acpgui-mainPageStyleLight.css");
        stage.setScene(scene);
        stage.setTitle("Main");
        stage.show();
    }

    public void initializeUserProfile() throws SQLException {
        dataBaseHandler.createConnection();
        Integer num = ACPLoginController.loginId;
        String qu = "SELECT * FROM USERS WHERE USER_ID = "+ num ;
        ResultSet resultSet = dataBaseHandler.execQuery(qu);
        String qu1 = "SELECT * FROM PASSWORDENTRIES WHERE OWNER_ID = " + num;
        ResultSet resultSet1 = dataBaseHandler.execQuery(qu1);
        int num1 = 0;

        if (resultSet.next()){
            theUsername.setText(resultSet.getString("USERNAME"));
           passwordField.setText(ACPLoginController.sPassWordT);
            Timestamp ts = resultSet.getTimestamp("DATE");
            theTime.setText(new SimpleDateFormat("yyyy-MM-dd").format(ts));
        }
        while (resultSet1.next()){
            num1++;
        }
        recordSaved.setText(String.valueOf(num1));


        dataBaseHandler.quit();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            initializeUserProfile();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public void editOnClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("acpgui-userEdit.fxml")));
        Scene scene = new Scene(root,600,400);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Edit");
        stage.show();
    }



}
