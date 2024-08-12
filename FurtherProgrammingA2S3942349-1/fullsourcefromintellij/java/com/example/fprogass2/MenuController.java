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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import java.sql.*;


public class MenuController {

    CourseManagement mngmt = new CourseManagement();
    User user = new User();

    private User currentUser = null;
    private boolean alreadyPopulated = false;
    private Course[] courses = null;
    private Stage stage;
    private Scene scene;
    private Parent fxmlLoader;

    String url = "jdbc:sqlite:C:\\sqlite\\users.sqlite";

    @FXML
    private VBox menuvbox;
    @FXML
    private Label currentuserlabel;
    @FXML
    private Button editprofilebutton;
    @FXML
    private Button logoutbutton;
    @FXML
    private Label courselabel;
    @FXML
    private TextField textentrytextfield;
    @FXML
    private HBox hboxs;
    @FXML
    private Button searchbutton;
    @FXML
    private Button selectbutton;
    @FXML
    private Button removecoursebutton;
    @FXML
    private Button editbutton;
    @FXML
    private TextField textusername;
    @FXML
    private TextField textpassword;
    @FXML
    private TextField textfirstname;
    @FXML
    private TextField textlastname;
    @FXML
    private Label errorlabel;
    @FXML
    private Button exportbutton;

    public void receiveCurrentUser(MouseEvent event) {
        menuvbox.setOnMouseMoved(e -> {
            if(!alreadyPopulated) {
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                User currentUser = (User) stage.getUserData();
                setCurrentUser(currentUser);
                //set the enrolled courses for the specific user to what is in the database
                currentuserlabel.setText(currentUser.getStudentNumber() + " - " + currentUser.getFirstName() + " " + currentUser.getLastName());
                editprofilebutton.setVisible(true);
                logoutbutton.setVisible(true);
                try (Connection conn = DriverManager.getConnection(url); Statement stmt = conn.createStatement()) {
                    ResultSet rs = stmt.executeQuery("SELECT enrolledcourses FROM users WHERE username = '" + currentUser.getUsername() + "';");
                    String sqlEnrolledCoursesString = rs.getString("enrolledcourses");
                    if (!Objects.equals(sqlEnrolledCoursesString, null)) {
                        String[] sqlEnrolledCoursesSplit = sqlEnrolledCoursesString.split(", ");
                        List<String> sqlCourses = new ArrayList<>(Arrays.asList(sqlEnrolledCoursesSplit));
                        int coursesAmount = (sqlEnrolledCoursesSplit.length / 7);
                        for (int i = 0; i < coursesAmount; i++) {
                            //String name, String capacity, String year, String mode, String day, String time, double duration
                            String name = sqlCourses.get(i * 7);
                            if(name.contains("[")) {
                                name = name.substring(1);
                            }
                            String capacity = sqlCourses.get(1 + i*7);
                            String year = sqlCourses.get(2 + i*7);
                            String mode = sqlCourses.get(3 + i*7);
                            String day = sqlCourses.get(4 + i*7);
                            String time = sqlCourses.get(5 + i*7);
                            double duration = Double.parseDouble(sqlCourses.get(6 + i*7).substring(0, 2));
                            Course sqlEnrolledCourses = new Course(name, capacity, year, mode, day, time, duration);
                            mngmt.setEnrolledCourses(sqlEnrolledCourses);
                        }
                    }
                    mngmt.PopulateCourses();
                    alreadyPopulated = true;
                } catch (Exception ex) {
                    System.err.println("Populating courses failed [catch] " + ex.getMessage());
                    alreadyPopulated = true;
                }
            }
        });
    }

    public void onEditProfileButtonClicked(ActionEvent event) {
        //switches view to the editview fxml
        editprofilebutton.setOnMouseClicked(e -> {
            String[] userStudentNumber = currentuserlabel.getText().split(" - ");
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.close();
            try {
                fxmlLoader = FXMLLoader.load(getClass().getResource("editview.fxml"));
                stage.setUserData(userStudentNumber);
                scene = new Scene(fxmlLoader);
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                System.err.println("IO exception catch " + ex.getMessage());
            }
        });
    }

    public void onEditClicked(ActionEvent event) {
        editbutton.setOnMouseClicked(e -> {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            String[] userStudentNumber = (String[]) stage.getUserData();
            //get account creds from textfields
            String username = textusername.getText();
            String password = textpassword.getText();
            String firstname = textfirstname.getText();
            String lastname = textlastname.getText();
            String studentnumber = userStudentNumber[0];
            if(!Objects.equals(username, "") && !Objects.equals(password, "") && !Objects.equals(firstname, "") && !Objects.equals(lastname, "") && !Objects.equals(studentnumber, "")) { //account credentials are good
                try {
                    user.deleteUser(studentnumber);
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

    public void onEnrollButtonClicked() {
        //ask user to look up course
        removecoursebutton.setVisible(false);
        textentrytextfield.clear();
        searchbutton.setVisible(true);
        selectbutton.setVisible(false);
        hboxs.setVisible(true);
        textentrytextfield.setPromptText("Please type the name or keywords of the course here");
    }

    public void onSearchButtonClicked() {
        //take input string and give to method
        String userSearch = textentrytextfield.getText();
        try {
            courses = mngmt.AddCoursesSearch(userSearch);
            int i = 0;
            String searchResults = "";
            while(i < courses.length) {
                if(courses[i] != null) {
                    searchResults += ((i+1) + ") " + courses[i].getName() +"\n"); //display searched courses
                    i++;
                }
                else {
                    searchResults += "No results found for the requested search: " + userSearch;
                    break;
                }
            }
            courselabel.setText(searchResults);
        } catch (OutOfRangeException | IllegalDataTypeException ex) {
            System.err.println(ex.getMessage());
        }

        //clear search bar + deselect
        textentrytextfield.clear();
        textentrytextfield.deselect();
        //change button
        searchbutton.setVisible(false);
        selectbutton.setVisible(true);
    }

    public void onSelectButtonClicked() throws IllegalDataTypeException, OutOfRangeException {
        //get number from field and give to method
        textentrytextfield.setPromptText("Please select one of the courses by typing the number here");
        String inputNumber = textentrytextfield.getText();

        String confirmationText = "There were no present courses, please try again.";
        if (courses[0] != null) {
            confirmationText = mngmt.AddCoursesSelect(courses, inputNumber);
        }
        //state that course has been enrolled to
        user.saveUserCourses(currentUser.getUsername(), mngmt.getEnrolledCourses());
        courselabel.setText(confirmationText);
        hboxs.setVisible(false);
    }

    public void onDisplayButtonClicked() {
        hboxs.setVisible(false);
        courselabel.setText(mngmt.ShowCourses());
        user.saveUserCourses(currentUser.getUsername(), mngmt.getEnrolledCourses());
    }

    public void onDisplayAllButtonClicked() {
        hboxs.setVisible(false);
        courselabel.setText(mngmt.ShowAllCourses());
    }

    public void onRemoveButtonClicked() {
        //
        hboxs.setVisible(true);
        searchbutton.setVisible(false);
        selectbutton.setVisible(false);
        removecoursebutton.setVisible(true);
        textentrytextfield.clear();
        textentrytextfield.setPromptText("Please type the number of the course you want to remove here");
    }

    public void onRemoveCourseButtonClicked() throws IllegalDataTypeException, OutOfRangeException {
        String removeNumber = textentrytextfield.getText();
        String removeConfirmation = mngmt.RemoveCourses(removeNumber);
        courselabel.setText(removeConfirmation);
        textentrytextfield.clear();
        textentrytextfield.deselect();
        user.saveUserCourses(currentUser.getUsername(), mngmt.getEnrolledCourses());
        courselabel.setText(mngmt.ShowCourses());
    }

    public void onLogOutButtonClicked(ActionEvent event) {
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
        try {
            fxmlLoader = FXMLLoader.load(getClass().getResource("loginview.fxml"));
            stage.setUserData(currentUser);
            scene = new Scene(fxmlLoader);
            stage.setScene(scene);
            setCurrentUser(null);
            stage.show();
        } catch (IOException ex) {
            System.err.println("IO exception catch " + ex.getMessage());
        }
    }

    public void onExportButtonClicked() throws IOException {
        String filename = currentUser.getUsername() + "exportedcourses.txt";
        String exportText = mngmt.ShowCourses();
        try {
            File file = new File(filename);
            file.createNewFile();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        FileWriter filewriter = new FileWriter(filename);
        filewriter.write(exportText);
        filewriter.close();
        courselabel.setText("Exported to file.");
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

}

//part D bonus marks