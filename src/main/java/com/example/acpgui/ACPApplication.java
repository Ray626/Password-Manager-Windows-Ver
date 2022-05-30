package com.example.acpgui;
import javafx.application.Application;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;


public class ACPApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(ACPApplication.class.getResource("acpgui-login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 400);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {launch();
    }

    private void addAppToTray() throws IOException {
        java.awt.Toolkit.getDefaultToolkit();
        if (!java.awt.SystemTray.isSupported()) {
            System.out.println("No system tray support, application exiting.");
            Platform.exit();
        }
        java.awt.SystemTray tray = java.awt.SystemTray.getSystemTray();
        URL iconURL = new URL("com/example/acpgui/imgResources/p-icon.png");
        java.awt.Image image = ImageIO.read(iconURL);
        java.awt.TrayIcon trayIcon = new java.awt.TrayIcon(image);
        //trayIcon.addActionListener(event -> );
    }


}