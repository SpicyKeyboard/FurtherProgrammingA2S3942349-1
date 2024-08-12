import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;
import java.util.Scanner;
import java.util.ArrayList;

public class Course{

    private final String courseName;
    private final int courseCapacity;
    private final String year; //the year column in the provided csv file has no numbers and are all just called 'Year X' so I went with a string
    private final String deliveryMode;
    private final String dayOfLecture;
    private final String timeOfLecture;
    private final double durationOfLecture;

    public Course() { //default values for course object
        this.courseName = "Name";
        this.courseCapacity = 0;
        this.year = "Year";
        this. deliveryMode = "Delivery Mode";
        this.dayOfLecture = "Day";
        this.timeOfLecture = "Time";
        this.durationOfLecture = 0.0;
    }

    public Course(String name, int capacity, String year, String mode, String day, String time, double duration) {
        this.courseName = name;
        this.courseCapacity = capacity;
        this.year = year;
        this.deliveryMode = mode;
        this.dayOfLecture = day;
        this.timeOfLecture = time;
        this.durationOfLecture = duration;
    }

    public String toString() { //toString for course objects
        return String.format(this.courseName+", "+this.courseCapacity+", "+this.year+", "+this.deliveryMode+", "+
                             this.dayOfLecture+", "+this.timeOfLecture+", "+this.durationOfLecture);
    }

    //getters for course information
    public String getName() {
        return this.courseName;
    }
    public String getMode() {
        return this.deliveryMode;
    }
    public String getDay() {
        return this.dayOfLecture;
    }
    public String getTime() {
        return this.timeOfLecture;
    }
    public double getDuration() {
        return this.durationOfLecture;
    }
}