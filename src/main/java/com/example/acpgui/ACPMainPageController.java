package com.example.acpgui;


import com.dustinredmond.fxtrayicon.FXTrayIcon;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.imageio.ImageIO;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class ACPMainPageController implements Initializable{

    private final DataBaseHandler dataBaseHandler;

    @FXML
    private TableView<PasswordRecord> table;
    @FXML
    private TableColumn sitesAndAppsCol;
    @FXML
    private TableColumn accountUserNameCol;

    @FXML
    private Label tableTitle;

    @FXML
    private AnchorPane base, leftBar;

    @FXML
    private ImageView colorImg;


    private Stage stage;

    public FXTrayIcon icon;


    private ObservableList<PasswordRecord> records = FXCollections.observableArrayList();

    public static ArrayList<String> singleRecord = new ArrayList<>();
    // 0 represents day mode, 1 represents night mode
    public static int colorMode = 0;





    public ACPMainPageController(){
        dataBaseHandler  = new DataBaseHandler();
    }

    public void addOnClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("acpgui-add.fxml")));
        Scene scene = new Scene(root,600,500);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Add");
        stage.getIcons().add( new Image(String.valueOf(getClass().getResource("/com/example/acpgui/imgResources/p-icon.png"))));
        stage.show();
    }

    public void UserOnClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("acpgui-userProfile.fxml")));
        Scene scene = new Scene(root,600,500);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Profile");
        stage.getIcons().add( new Image(String.valueOf(getClass().getResource("/com/example/acpgui/imgResources/p-icon.png"))));
        stage.show();
    }
    public void DetailedOnClick() throws IOException{
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/acpgui/acpgui-detailedRecord.fxml")));
        Scene scene = new Scene(root,600,410);
        stage.setScene(scene);
        stage.setTitle("record");
        stage.getIcons().add( new Image(String.valueOf(getClass().getResource("/com/example/acpgui/imgResources/p-icon.png"))));
        stage.show();
    }

    public void getSelected(){
        if (table.getItems().size() > 0){
            singleRecord.clear();
            ObservableList<PasswordRecord> pr;

            try{

                pr = table.getSelectionModel().getSelectedItems();
                PasswordRecord psr = pr.get(0);
                if (psr!=null){
                    singleRecord.add(psr.getSiteAndApp());
                    singleRecord.add(psr.getAccountUserName());
                    System.out.println(singleRecord);
                    DetailedOnClick();


                }
            } catch (Exception exception) {

            }
        }

    }

    public void downloadOnClick() throws IOException, SQLException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        dataBaseHandler.createConnection();
        String home = System.getProperty("user.home");
        File file = new File(new File(home,"Downloads"),"passwordRecords" + ".txt");
        FileWriter fr = new FileWriter(file);
        BufferedWriter br = new BufferedWriter(fr);
        ResultSet resultSet = dataBaseHandler.execPreparedStmt3(ACPLoginController.loginId);
        while(resultSet.next()){
            PasswordModifier passwordModifier = new PasswordModifier(resultSet.getString("PRIVATE_KEY"),resultSet.getString("ENCODED_MESSAGE"));
            String thePassword = passwordModifier.decryptedMessage();

            br.write("Name of site or app: " + resultSet.getString("COMPANY") +
                    ", Account username: " + resultSet.getString("ACCOUNT_USERNAME") +
                    ", Password: " + thePassword  + ", Company link: " + resultSet.getString("COMPANY_LINK") +
                    ", Note: " + resultSet.getString("NOTE") + "\r\n");
        }
        br.close();
        dataBaseHandler.quit();
        successPopUp();
    }
    public void colormodeNight(){
        colorImg.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/acpgui/imgResources/moon.png"))));
        base.setStyle("-fx-background-color: #202020");
        leftBar.setStyle("-fx-background-color: #606060");
        tableTitle.setStyle("-fx-text-fill: #f0f0f0");
        table.setId("tableD");
    }
    public void colorModeDay(){
        colorImg.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/acpgui/imgResources/sun.png"))));
        base.setStyle("-fx-background-color: #ffffff");
        tableTitle.setStyle("-fx-text-fill: #000000");
        leftBar.setStyle("-fx-background-color: #d3d3d3");
        table.setId("tableL");
    }

    public void colorModeOnClick(){

        if (colorMode == 0){
            colorMode = 1;
            colormodeNight();
        }else {
            colorMode = 0;
            colorModeDay();

        }
    }

    public void exitButtonOnClick(){
        System.exit(0);
        
    }


    private void initializeTableView() throws SQLException{

        dataBaseHandler.createConnection();
        int num = ACPLoginController.loginId;
        ResultSet resultSet = dataBaseHandler.execPreparedStmt3(num);
        while(resultSet.next()){
            String one = resultSet.getString("COMPANY");
            String two = resultSet.getString("ACCOUNT_USERNAME");
            records.add(new PasswordRecord(one,two));
        }
        table.getItems().setAll(records);
        tableTitle.setText("You have " + table.getItems().size() + " password records saved");


    }
    private void initializeTableColumn(){
        sitesAndAppsCol.setCellValueFactory(new PropertyValueFactory<>("siteAndApp"));
        accountUserNameCol.setCellValueFactory(new PropertyValueFactory<>("accountUserName"));
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Platform.runLater(() ->{
            stage =(Stage) tableTitle.getScene().getWindow();
            icon = new FXTrayIcon(stage,getClass().getResource("/com/example/acpgui/imgResources/p-icon.png"));
            icon.show();
            MenuItem menuShowStage = new MenuItem("Show Stage");
            MenuItem menuExit = new MenuItem("Exit");
            menuShowStage.setOnAction(e -> {
                Platform.runLater(() -> com.sun.javafx.application.PlatformImpl.setTaskbarApplication(false));
                stage.show();
            });
            menuExit.setOnAction(e -> System.exit(0));
            icon.addMenuItem(menuShowStage);
            icon.addMenuItem(menuExit);
        });

        initializeTableColumn();
        try {
            initializeTableView();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(colorMode==0){
            colorModeDay();
        }else {
            colormodeNight();
        }

        }


        private void successPopUp(){
            Alert alert;
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Password Manager");
            alert.setContentText("password records has been downloaded your computer");
            alert.show();
        }









}
