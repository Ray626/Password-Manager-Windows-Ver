package com.example.acpgui;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Stage;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class ACPDetailedRecordController implements Initializable {
    @FXML
    private TextField username, password;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label pageTitle;

    @FXML
    private TextArea note, linkTextArea;

    @FXML
    private Hyperlink link;

    private final DataBaseHandler dataBaseHandler;

    private int mode = 0;

    private int record_id;

    @FXML
    private JFXButton edit,delete,cancel,apply;

    @FXML
    private ImageView passwordImg;




    public ACPDetailedRecordController(){
        dataBaseHandler = new DataBaseHandler();


    }

    public void initializeDetailedRecord() throws SQLException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        dataBaseHandler.createConnection();
        ResultSet resultSet = dataBaseHandler.execPreparedStmt4(ACPLoginController.loginId,
                ACPMainPageController.singleRecord.get(0),
                ACPMainPageController.singleRecord.get(1));
        if (resultSet.next()){
            pageTitle.setText(resultSet.getString("COMPANY"));
            PasswordModifier passwordModifier = new PasswordModifier(resultSet.getString("PRIVATE_KEY"),
                    resultSet.getString("ENCODED_MESSAGE"));
            passwordField.setText(passwordModifier.decryptedMessage());
            System.out.println(passwordModifier.decryptedMessage());
            username.setText(resultSet.getString("ACCOUNT_USERNAME"));
            link.setText(resultSet.getString("COMPANY_LINK"));
            note.setText(resultSet.getString("NOTE"));
            record_id = resultSet.getInt("ID");

        }
        dataBaseHandler.quit();
    }

    /**
     * unresolved
     */
    public void linkOnClick() throws IOException {
        Runtime.getRuntime().exec("explorer \"https://"+link.getText() +"\"\n");
    }

    public void editOnClick(){
        username.setEditable(true);
        if (passwordField.isVisible()){
            passwordHideOnClick();
        }
        password.setEditable(true);
        note.setEditable(true);
        link.setVisible(false);
        linkTextArea.setText(link.getText());
        linkTextArea.setEditable(true);
        linkTextArea.setVisible(true);
        edit.setVisible(false);
        delete.setVisible(false);
        cancel.setVisible(true);
        apply.setVisible(true);
    }
    public void cancelOnClick(){
        linkTextArea.setVisible(false);
        link.setVisible(true);
        cancel.setVisible(false);
        apply.setVisible(false);
        edit.setVisible(true);
        delete.setVisible(true);
        password.setEditable(false);
        username.setEditable(false);
        note.setEditable(false);
    }

    public void applyOnClick(ActionEvent event) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, IOException {
        dataBaseHandler.createConnection();
        PasswordModifier passwordModifier = new PasswordModifier(password.getText());
        String privateKey = passwordModifier.privateKeyToString();
        String encodedMessage = passwordModifier.getEncodedMessage();

        String qu = "UPDATE PASSWORDENTRIES set ACCOUNT_USERNAME = " + "'" + username.getText() + "',"
                + "PRIVATE_KEY = " + "'" + privateKey + "'," + "ENCODED_MESSAGE = " + "'" + encodedMessage + "',"
                + "NOTE = " + "'" + note.getText() + "'," + "COMPANY_LINK = " + "'" + linkTextArea.getText() + "'"
                + "WHERE ID = " + record_id;
        dataBaseHandler.execUpdate(qu);
        dataBaseHandler.quit();
        backOnClick(event);

    }

    public void deleteOnClick(ActionEvent event) throws IOException {
        dataBaseHandler.createConnection();
        String qu = "DELETE from PASSWORDENTRIES WHERE ID =" + record_id;
        dataBaseHandler.execAction(qu);
        dataBaseHandler.quit();
        backOnClick(event);
    }

    /**
     * unfinished
     */
    public void passwordHideOnClick(){
        if(mode == 0){
            mode = 1;
            passwordImg.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/acpgui/imgResources/show.png"))));
            String Password = passwordField.getText();
            passwordField.setDisable(true);
            passwordField.setVisible(false);
            password.setDisable(false);
            password.setText(Password);
            password.setVisible(true);
        }else{
            mode = 0;
            passwordImg.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/acpgui/imgResources/hide.png"))));
            String thePassword = password.getText();
            password.setDisable(true);
            password.setVisible(false);
            passwordField.setDisable(false);
            passwordField.setText(thePassword);
            passwordField.setVisible(true);
        }
    }

    public void backOnClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("acpgui-mainPage.fxml")));
        Scene scene = new Scene(root,600,500);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Main");
        stage.getIcons().add( new Image(String.valueOf(getClass().getResource("/com/example/acpgui/imgResources/p-icon.png"))));
        stage.show();
    }

    public void copyOnClickU(){
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent clipboardContent= new ClipboardContent();
        clipboardContent.putString(username.getText());
        clipboard.setContent(clipboardContent);
    }

    public void copyOnClickP(){
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent clipboardContent= new ClipboardContent();
        clipboardContent.putString(passwordField.getText());
        clipboard.setContent(clipboardContent);
    }







    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            initializeDetailedRecord();
        } catch (SQLException | IllegalBlockSizeException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeySpecException | BadPaddingException | InvalidKeyException e) {
            e.printStackTrace();
        }
    }






}
