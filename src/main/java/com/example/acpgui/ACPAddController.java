package com.example.acpgui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class ACPAddController implements Initializable {

    private final DataBaseHandler dataBaseHandler;
    private String theCombMessage;
    @FXML
    private TextField username,password,webLink;
    @FXML
    private TextArea note;
    @FXML
    private ComboBox<String> comb;


    public ACPAddController() {
        dataBaseHandler = new DataBaseHandler();

    }

    public void backOnClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("acpgui-mainPage.fxml")));
        Scene scene = new Scene(root,600,500);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene.getClass().getResource("acpgui-mainPageStyleLight.css");
        stage.setScene(scene);
        stage.setTitle("Main");
        stage.getIcons().add( new Image(String.valueOf(getClass().getResource("/com/example/acpgui/imgResources/p-icon.png"))));
        stage.show();
    }

    /**
     * Parameter will be the enum that stores the information of some
     * big companies or famous web. This Java Docs is a reminder.
     *
     *
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<String> list = new ArrayList<>();
        list.add("Google");
        list.add("IBM");
        list.add("Apple");
        list.add("GitHub");
        list.add("Outlook");
        list.add("Linkin");
        list.add("JetBrains");
        ObservableList<String> observableList = FXCollections.observableList(list);
        comb.setItems(observableList);
    }
    public void combMessage(ActionEvent event){
        String s = comb.getSelectionModel().getSelectedItem();
        theCombMessage = s;
    }

    /**
     * Password record checker is required.
     * @throws NoSuchPaddingException
     * @throws IllegalBlockSizeException
     * @throws NoSuchAlgorithmException
     * @throws BadPaddingException
     * @throws InvalidKeyException
     */
    public void submitOnClick() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        dataBaseHandler.createConnection();
        PasswordModifier pm = new PasswordModifier(password.getText());
        String theNote = note.getText();



        if (theNote == null){
            theNote = "";
        }
        dataBaseHandler.execPreparedStmt2(ACPLoginController.loginId,pm.privateKeyToString(),pm.getEncodedMessage(),theCombMessage,webLink.getText(),username.getText(),theNote);
        dataBaseHandler.quit();
    }



}
