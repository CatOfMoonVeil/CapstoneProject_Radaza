module com.example.project_radaza {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.project_radaza to javafx.fxml;
    exports com.example.project_radaza;
}