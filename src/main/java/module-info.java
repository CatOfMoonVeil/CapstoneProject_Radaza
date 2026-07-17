module com.example.ridehailing {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.example.ridehailing.ui to javafx.fxml;
    exports com.example.ridehailing.ui;

    opens com.example.ridehailing.model to javafx.fxml;

}