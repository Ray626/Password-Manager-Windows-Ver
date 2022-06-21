package com.example.acpgui;
import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


import javax.swing.*;
import java.io.IOException;
import java.util.Objects;


public class ACPApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(ACPApplication.class.getResource("acpgui-login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 400);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.getIcons().add( new Image(String.valueOf(getClass().getResource("/com/example/acpgui/imgResources/p-icon.png"))));
        stage.show();



    }

    public static void main(String[] args) {
        launch();
    }




}