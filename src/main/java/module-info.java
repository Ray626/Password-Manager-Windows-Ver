module com.example.acpgui {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.derby.engine;
    requires org.apache.derby.commons;
    requires java.sql;
    requires org.kordamp.bootstrapfx.core;
    requires FXTrayIcon;



    requires java.desktop;
    requires com.jfoenix;

    opens com.example.acpgui to javafx.fxml;
    exports com.example.acpgui;
}