package com.example.fprogass2;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.sql.*;

public class User {

    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String studentNumber;
    private User user = null;

    String url = "jdbc:sqlite:C:\\sqlite\\users.sqlite";

    public User() {
        this.username = "username";
        this.password = "password";
        this.firstName = "firstName";
        this.lastName = "lastName";
        this.studentNumber = "studentNumber";
    }
    public User(String username, String password, String firstName, String lastName, String studentNumber) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.studentNumber = studentNumber;
    }

    public void createUserDatabase() {
        try (Connection conn = DriverManager.getConnection(url); Statement stmt = conn.createStatement()) {
            DatabaseMetaData meta = conn.getMetaData(); //generates the .sqlite file
            String sql = "CREATE TABLE IF NOT EXISTS users (\n" //creates the desired table and columns in the .sqlite file
                    + "	username text NOT NULL PRIMARY KEY,\n"
                    + "	password text NOT NULL,\n"
                    + "	firstname text NOT NULL,\n"
                    + "	lastname text NOT NULL,\n"
                    + "	studentnumber text NOT NULL,\n"
                    + "	enrolledcourses text\n"
                    + ");";
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public String createUser(String username, String password, String firstName, String lastName, String studentNumber) throws IOException {
        //append user to users.sqlite
        try {
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            //check if username is unique
            boolean usernameExists = false;
            ResultSet rs = stmt.executeQuery("SELECT * from users");
            while (rs.next()) {
                String sqlUsername = rs.getString("username");
                String sqlStudentNumber = rs.getString("studentnumber");
                if(Objects.equals(sqlUsername, username) || Objects.equals(sqlStudentNumber, studentNumber)) {
                    usernameExists = true;
                }
            }
            if(usernameExists) {
                return "Username or student number already exist, try a different one.";
            }
            else {
                PreparedStatement pstmt = conn.prepareStatement("insert into users (username, password, firstname, lastname, studentnumber) values (?,?,?,?,?)");
                pstmt.setString(1, username);
                pstmt.setString(2, password);
                pstmt.setString(3, firstName);
                pstmt.setString(4, lastName);
                pstmt.setString(5, studentNumber);
                pstmt.executeUpdate();
                stmt.close();
                conn.close();
                return "User created.";
            }
        } catch (SQLException se) {
            System.out.println("SQLError: " + se.getMessage());
        }
        return "error with SQL";
    }

    public User searchCredentials(String username, String password) {
        boolean loginSuccess = false;
        User user = null;
        try {
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * from users");
            while (rs.next()) {
                String sqlUsername = rs.getString("username");
                String sqlPassword = rs.getString("password");
                String sqlFirstName = rs.getString("firstname");
                String sqlLastName = rs.getString("lastname");
                String sqlStudentNumber = rs.getString("studentnumber");
                    //check if username and password combo exists in database
                    if(Objects.equals(sqlUsername, username) && Objects.equals(sqlPassword, password)) {
                        //allow them to login
                        loginSuccess = true;
                        user = new User(sqlUsername,sqlPassword,sqlFirstName,sqlLastName,sqlStudentNumber);
                    }
                }
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            System.out.println("SQLError: " + se.getMessage());
        }
        //returns if login was successful (true) or not (false)
        if(loginSuccess) {
            //return user
            return user;
        }
        else {
            return null;
        }
    }

    public void deleteUser(String studentNumber) {
        try (Connection conn = DriverManager.getConnection(url); Statement stmt = conn.createStatement()) {
            String sql = "DELETE FROM users WHERE studentnumber = '" + studentNumber + "';";
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void saveUserCourses(String username, ArrayList<Course> enrolledCourses) {
        try (Connection conn = DriverManager.getConnection(url); Statement stmt = conn.createStatement()) {
            String sql = "UPDATE users SET enrolledcourses = '" + enrolledCourses + "'WHERE username = '" + username + "';";
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public String getUsername() {
        return this.username;
    }
    public String getPassword() {
        return this.password;
    }
    public String getFirstName() {
        return this.firstName;
    }
    public String getLastName() {
        return this.lastName;
    }
    public String getStudentNumber() {
        return this.studentNumber;
    }
    public User getUser() {
        return this.user;
    }
}
