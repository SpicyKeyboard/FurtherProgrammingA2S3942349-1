import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CourseManagementTest { //all of these tests assume that the standard csv file that was provided is used.
    
    CourseManagement manage = new CourseManagement();
    private final ArrayList<Course> availableCourses = manage.getAvailableCourses();
    private final ArrayList<Course> enrolledCourses = manage.getEnrolledCourses();
    
    @BeforeEach
    void setUp() throws FileNotFoundException {
        manage.PopulateCourses();
        CourseManagement manage = new CourseManagement();
    }

    @Test
    void PopulateCoursesIsCorrectNameTest() {
        assertEquals(availableCourses.get(0).getName(), "Java programming");
    }

    @Test
    void PopulateCoursesIsCorrectSizeTest() {
        assertEquals(availableCourses.size(), 7);
    }

    @Test
    void AddCoursesShowsCorrectWrongInputTextTest() throws IllegalDataTypeException, OutOfRangeException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream prints = new PrintStream(bytes);
        ByteArrayInputStream input = new ByteArrayInputStream("123".getBytes());
        System.setIn(input);
        PrintStream old = System.out;
        System.setOut(prints);
        manage.AddCoursesSearch();
        System.out.flush();
        System.setOut(old);
        assertEquals(bytes.toString(), "Please type the name or keywords of the course: -------------------------------\nPlease select one of the following options:\n-------------------------------\r\nThere were no results for the input names or keywords input.\r\n");
    }

    @Test
    void AddCoursesShowsCorrectTextTest() throws IllegalDataTypeException, OutOfRangeException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream prints = new PrintStream(bytes);
        ByteArrayInputStream input = new ByteArrayInputStream("java".getBytes());
        System.setIn(input);
        PrintStream old = System.out;
        System.setOut(prints);
        manage.AddCoursesSearch();
        System.out.flush();
        System.setOut(old);
        assertEquals(bytes.toString(), "Please type the name or keywords of the course: -------------------------------\nPlease select one of the following options:\n-------------------------------\r\n 1) Java programming\r\n");
    }

    @Test
    void AddCoursesAddsCorrectCourseTest() throws IllegalDataTypeException, OutOfRangeException {
        Course[] courseMatching = new Course[1];
        courseMatching[0] = availableCourses.get(0);
        manage.setCourseMatchCount(1);
        ByteArrayInputStream input = new ByteArrayInputStream("1".getBytes());
        System.setIn(input);
        manage.AddCoursesSelect(courseMatching);
        assertEquals(enrolledCourses.get(0), availableCourses.get(0));
    }

    @Test
    void AddCoursesShowsCorrectCourseAlreadyPresentTextTest() throws IllegalDataTypeException, OutOfRangeException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream prints = new PrintStream(bytes);
        Course[] courseMatching = new Course[1];
        courseMatching[0] = availableCourses.get(0);
        manage.setCourseMatchCount(1);
        manage.setEnrolledCourses(availableCourses.get(0));
        ByteArrayInputStream input = new ByteArrayInputStream("1".getBytes());
        System.setIn(input);
        PrintStream old = System.out;
        System.setOut(prints);
        manage.AddCoursesSelect(courseMatching);
        System.out.flush();
        System.setOut(old);
        assertEquals(bytes.toString(), "Please select: You have already enrolled in the selected course: Java programming, so there is no need to enroll again.\r\n");
    }

    @Test
    void showCoursesDisplaysCorrectCourseTest() {
        manage.setEnrolledCourses(availableCourses.get(0));
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream prints = new PrintStream(bytes);
        PrintStream old = System.out;
        System.setOut(prints);
        manage.ShowCourses();
        System.out.flush();
        System.setOut(old);
        assertEquals(bytes.toString(), "Here are the courses you're enrolled in:\n-------------------------------\r\n 1) Java programming | Face-to-face | Wednesday Beginning at: 11:30 and lasting for: 2.0 hours.\r\n-------------------------------\r\n");
    }

    @Test
    void showCoursesDisplaysNoSelectedCourseTest() {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream prints = new PrintStream(bytes);
        PrintStream old = System.out;
        System.setOut(prints);
        manage.ShowCourses();
        System.out.flush();
        System.setOut(old);
        assertEquals(bytes.toString(), "-------------------------------\nYou do not have any enrolled courses, try selecting one to get started.\n-------------------------------\r\n");
    }

    @Test
    void removeCoursesDisplaysCorrectCourseTest() throws IllegalDataTypeException, OutOfRangeException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream prints = new PrintStream(bytes);
        manage.setEnrolledCourses(availableCourses.get(0));
        ByteArrayInputStream input = new ByteArrayInputStream("1".getBytes());
        System.setIn(input);
        PrintStream old = System.out;
        System.setOut(prints);
        manage.RemoveCourses();
        System.out.flush();
        System.setOut(old);
        assertEquals(bytes.toString(), "-------------------------------\r\nHere are the courses you're enrolled in:\n-------------------------------\r\n 1) Java programming | Face-to-face | Wednesday Beginning at: 11:30 and lasting for: 2.0 hours.\r\n-------------------------------\r\nSelect a course to withdraw from: \r\n-------------------------------\r\nWithdrawn from the course: Java programming, 120, Year 1, Face-to-face, Wednesday, 11:30, 2.0\n-------------------------------\r\n");
    }

    @Test
    void removeCoursesRemovesCorrectCourseTest() throws IllegalDataTypeException, OutOfRangeException {
        manage.setEnrolledCourses(availableCourses.get(0));
        ByteArrayInputStream input = new ByteArrayInputStream("1".getBytes());
        System.setIn(input);
        manage.RemoveCourses();
        assertEquals(enrolledCourses.size(), 0);
    }

    @Test
    void removeCoursesDisplaysNoSelectedCourseTest() throws IllegalDataTypeException, OutOfRangeException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream prints = new PrintStream(bytes);
        PrintStream old = System.out;
        System.setOut(prints);
        manage.RemoveCourses();
        System.out.flush();
        System.setOut(old);
        assertEquals(bytes.toString(), "-------------------------------\r\nYou do not have any enrolled courses, try selecting one to get started.\n-------------------------------\r\n");
    }
}
