package com.example.fprogass2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class MyTimeTable extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MyTimeTable.class.getResource("loginview.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 300);
        primaryStage.setTitle("My Timetable");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        User user = new User();
        user.createUserDatabase();
        Application.launch(args);
    }
}