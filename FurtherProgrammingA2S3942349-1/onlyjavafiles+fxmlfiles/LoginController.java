package com.example.fprogass2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class LoginController {

    User user = new User();

    private Stage stage;
    private Scene scene;
    private Parent fxmlLoader;

    @FXML
    private Button signinbutton;
    @FXML
    private Button signupbutton;
    @FXML
    private Button createaccountbutton;
    @FXML
    private TextField textusername;
    @FXML
    private TextField textpassword;
    @FXML
    private TextField textfirstname;
    @FXML
    private TextField textlastname;
    @FXML
    private TextField textstudentnumber;
    @FXML
    private Label errorlabel;
    @FXML
    private Label signinerrorlabel;

    @FXML
    public void onSignInClicked(ActionEvent event) {
        signinbutton.setOnMouseClicked(e -> {
            String username = textusername.getText();
            String password = textpassword.getText();
            user.setUser(user.searchCredentials(username, password));
            signinerrorlabel.setText("Username and password were incorrect.");
            if(!Objects.equals(user.getUser(), null)) {
                //proceed to menu under specific user
                User currentUser = user.getUser();
                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                stage.close();
                try {
                    fxmlLoader = FXMLLoader.load(getClass().getResource("menuview.fxml"));
                    stage.setUserData(currentUser);
                    scene = new Scene(fxmlLoader);
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException ex) {
                    System.err.println("IO exception catch " + ex.getMessage());
                }
            }
        });
    }

    public void onSignUpClicked(ActionEvent event) {
        signupbutton.setOnMouseClicked(e -> {
            try {
                fxmlLoader = FXMLLoader.load(getClass().getResource("signupview.fxml"));
            } catch (IOException ex) {
                System.out.println("IO exception");
            }
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(fxmlLoader);
            stage.setScene(scene);
            stage.show();
        });
    }

    public void onCreateAccountClicked(ActionEvent event) {
        createaccountbutton.setOnMouseClicked(e -> {
            //get account creds from textfields
            String username = textusername.getText();
            String password = textpassword.getText();
            String firstname = textfirstname.getText();
            String lastname = textlastname.getText();
            String studentnumber = textstudentnumber.getText();
            if(!Objects.equals(username, "") && !Objects.equals(password, "") && !Objects.equals(firstname, "") && !Objects.equals(lastname, "") && !Objects.equals(studentnumber, "")) { //account credentials are good
                try {
                    String userCreationResult = user.createUser(username, password, firstname, lastname, studentnumber);
                    errorlabel.setText(userCreationResult);
                    if (userCreationResult.contains("created")) {
                        fxmlLoader = FXMLLoader.load(getClass().getResource("loginview.fxml"));
                        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                        scene = new Scene(fxmlLoader);
                        stage.setScene(scene);
                        stage.show();
                    }
                } catch (IOException ex) {
                    System.out.println("Account creation failed.");
                }
            }
            else { //account credentials are bad
                errorlabel.setText("Please fill all the boxes.");
            }
        });
    }
}