module com.example.ridehailing {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.ridehailing.ui to javafx.fxml;
    exports com.example.ridehailing.ui;
}