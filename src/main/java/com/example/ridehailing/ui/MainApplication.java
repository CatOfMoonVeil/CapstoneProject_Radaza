package com.example.ridehailing.ui;

import com.example.ridehailing.model.Driver;
import com.example.ridehailing.model.Passenger;
import com.example.ridehailing.model.User;
import com.example.ridehailing.model.UserSession;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        User activeUser = UserSession.getCurrentUser(); // Checks session.dat automatically

        String initialView = "/com/example/ridehailing/ui/LoginView.fxml";
        String title = "GoRide Gateway";

        if (activeUser instanceof Passenger) {
            initialView = "/com/example/ridehailing/ui/BookingView.fxml";
            title = "GoRide Terminal Panel";
        } else if (activeUser instanceof Driver) {
            initialView = "/com/example/ridehailing/ui/DriverDashboardView.fxml";
            title = "GoRide Driver Command Center";
        }

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(initialView));
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root, 450, 580);
        stage.setTitle(title);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}