package com.example.fprogass2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class CourseManagement{

    private final ArrayList<Course> availableCourses = new ArrayList<Course>();
    private final ArrayList<Course> enrolledCourses = new ArrayList<Course>();
    private int courseMatchCount = 0;

    public void PopulateCourses() throws FileNotFoundException {
        Scanner coursesScanner = new Scanner(new File("course.csv"));
        coursesScanner.useDelimiter("\n"); //above code takes the csv file and scans each line, a line being determined by the \n new line character thing
        while (coursesScanner.hasNext()) {
            String scannedCourseString = coursesScanner.next();
            String[] splitCourse = scannedCourseString.split(",");
            if (!Objects.equals(splitCourse[0], "Course name")) { //splits lines into strings, determining where to split via the comma (","), and adding them to an arraylist. will not take lines that, before the first comma, are exactly "Course Name"
                availableCourses.add(new Course(splitCourse[0], splitCourse[1], splitCourse[2], splitCourse[3], splitCourse[4], splitCourse[5], Double.parseDouble(splitCourse[6])));
            }
        }
        coursesScanner.close();
    }

    public Course[] AddCoursesSearch(String searchedString) throws OutOfRangeException, IllegalDataTypeException {
        //scan for user input sting
        courseMatchCount = 0;
        Course[] courseMatchArr = new Course[availableCourses.size()];
        //compare to available course names then print the names of the corresponding courses
        searchedString = searchedString.toLowerCase();
        boolean inputPresent = false;
        for (int i = 0; i < availableCourses.size(); i += 1) {
            String lowerCourseName = availableCourses.get(i).getName().toLowerCase();
            if (lowerCourseName.contains(searchedString)) { //if user input is in any courseName
                inputPresent = true;
                courseMatchArr[courseMatchCount] = availableCourses.get(i);
                courseMatchCount += 1;
            }
        }
        if (!inputPresent) {
            return courseMatchArr;
        }
        return courseMatchArr;
    }

    public String AddCoursesSelect(Course[] courseMatchArr, String selectedNumberString) throws IllegalDataTypeException, OutOfRangeException {
        //allow selection for each refined course, if selected put into enrolled courses arraylist
        //assigns the courses numbers
        try {
            Integer.parseInt(selectedNumberString);
        } catch (Exception e) {
            throw new IllegalDataTypeException("menu input is not an integer");
        }
        int userAddSelection = Integer.parseInt(selectedNumberString);
        if (userAddSelection <= 0 || userAddSelection > courseMatchCount) { //throws if input is not one of the options
            throw new OutOfRangeException("menu selection is out of range");
        }
        //if course is already enrolled in, don't allow enrollment and print out string saying that they are already enrolled
        boolean coursePresent = false;
        userAddSelection -= 1;
        for (int i = 0; i < enrolledCourses.size(); i += 1) {
            if (courseMatchArr[userAddSelection].getName().equals(enrolledCourses.get(i).getName())) { //course was present in arraylist, not adding it any telling the user that it is already in
               coursePresent = true;
            }
        }
        if (!coursePresent) { //course was not present in arraylist therefore it adds the course to the array list
            enrolledCourses.add(courseMatchArr[userAddSelection]);
        }
        if(coursePresent) {
            return ("You have already enrolled in the selected course: " + courseMatchArr[userAddSelection].getName() + ", so there is no need to enroll again.");
        }
        else {
            return ("You have enrolled in the course: " + courseMatchArr[userAddSelection].getName());
        }
    }

    public String ShowCourses() {
        boolean emptyEnrolledCoursesArr = true;
        for (int i = 0; i < enrolledCourses.size(); i+=1) {
            if (enrolledCourses.get(i) != null) {
                emptyEnrolledCoursesArr = false;
                break;
            }
        }
        if (!emptyEnrolledCoursesArr) {
            String enrolledCoursesString = "Here are the courses you're enrolled in:\n";
            for (int i = 0; i < enrolledCourses.size(); i+=1) {
                if (enrolledCourses.get(i) != null) { //makes each enrolled course print out a nice readable string using getter methods
                    enrolledCoursesString += (" " + (i+1) + ") " + enrolledCourses.get(i).getName() + " | " +
                            enrolledCourses.get(i).getMode() + " | " + enrolledCourses.get(i).getDay() + " Beginning at: " +
                            enrolledCourses.get(i).getTime() + " and lasting for: " + enrolledCourses.get(i).getDuration() + " hours.\n");
                }
            }
            return enrolledCoursesString;
        }
        else {
            return "You do not have any enrolled courses, try selecting one to get started.";
        }
    }

    public String ShowAllCourses() {
        boolean emptyAvailableCoursesArr = true;
        for (int i = 0; i < availableCourses.size(); i+=1) {
            if (availableCourses.get(i) != null) {
                emptyAvailableCoursesArr = false;
                break;
            }
        }
        if (!emptyAvailableCoursesArr) {
            String availableCoursesString = "Here are the courses that are available:\n";
            for (int i = 0; i < availableCourses.size(); i+=1) {
                if (availableCourses.get(i) != null) { //makes each available course print out a nice readable string using getter methods
                    availableCoursesString += (" " + (i+1) + ") " + availableCourses.get(i).getName() + " | " +
                            availableCourses.get(i).getMode() + " | " + availableCourses.get(i).getDay() + " Beginning at: " +
                            availableCourses.get(i).getTime() + " and lasting for: " + availableCourses.get(i).getDuration() + " hours.\n");
                }
            }
            return availableCoursesString;
        }
        else {
            return "There are no available courses, the program must not have access to the correct files.";
        }
    }

    public String RemoveCourses(String removeNumber) throws OutOfRangeException, IllegalDataTypeException {
        boolean emptyEnrolledCoursesArr = true;
        for (int i = 0; i < enrolledCourses.size(); i+=1) {
            if (enrolledCourses.get(i) != null) {
                emptyEnrolledCoursesArr = false;
                break;
            }
        }
        if (!emptyEnrolledCoursesArr) {
            ShowCourses();
            //scan for user input
            try {
                Integer.parseInt(removeNumber);
            } catch (Exception e) {
                throw new IllegalDataTypeException("menu input is not an integer");
            }
            int userRemoveSelection = Integer.parseInt(removeNumber);
            if (userRemoveSelection <= 0 || userRemoveSelection > enrolledCourses.size()) { //throws if input is not one of the options
                throw new OutOfRangeException("menu selection is out of range");
            }
            //take the user selected number, correlate it to the arraylist index, and then remove the corresponding arraylist element
            String withdrawnString = "Withdrawn from the course: " + enrolledCourses.get(userRemoveSelection - 1) + "\n";
            enrolledCourses.remove(userRemoveSelection - 1);
            return withdrawnString;
        }
        else {
            return "You do not have any enrolled courses, try selecting one to get started.";
        }
    }

    public ArrayList<Course> getAvailableCourses() {
        return this.availableCourses;
    }

    public ArrayList<Course> getEnrolledCourses() {
        return this.enrolledCourses;
    }

    public void setEnrolledCourses(Course course) {
        this.enrolledCourses.add(course);
    }

    public void setCourseMatchCount(int number) {
        this.courseMatchCount = number;
    }
}
