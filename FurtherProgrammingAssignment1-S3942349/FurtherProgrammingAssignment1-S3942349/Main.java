import java.util.Arrays;
import java.util.Scanner;
import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("----- MyTimetable Program -----");
        boolean userSelectExit = false;
        CourseManagement manage = new CourseManagement();
        manage.PopulateCourses();
        while (userSelectExit == false) { //while the user hasn't selected option 4 that will flip the boolean value
            String menuSelectString = "Please make a selection:\n" +
                                      "-------------------------------\n" +
                                      " 1) Search by keyword to enroll\n" +
                                      " 2) Show my enrolled courses\n" +
                                      " 3) Withdraw from a course\n" +
                                      " 4) Exit\n" +
                                      "Please Select: ";
            try {
                System.out.print(menuSelectString);
                Scanner userScan = new Scanner(System.in);
                String userMenuInput = userScan.nextLine();
                try {
                    Integer.parseInt(userMenuInput);
                } catch (Exception e) {
                    throw new IllegalDataTypeException("menu input is not an integer");
                }
                int userSelection = Integer.parseInt(userMenuInput);
                if (userSelection <= 0 || userSelection > 4) {
                    throw new OutOfRangeException("menu selection is out of range");
                }
                if (userSelection == 1) { //keyword search and enroll to course
                    Course[] matchingCourses = manage.AddCoursesSearch();
                    if (matchingCourses[0] != null) {
                        manage.AddCoursesSelect(matchingCourses);
                    }
                }
                else if (userSelection == 2) { //show enrolled courses
                    manage.ShowCourses();
                }
                else if (userSelection == 3) { //withdraw from course
                    manage.RemoveCourses();
                }
                else { //exit
                    userSelectExit = true; //doing this exits the while loop
                    System.out.println("Exiting Program.");
                }
            } catch (OutOfRangeException e) {
                System.err.println(e.getMessage());
            } catch (IllegalDataTypeException e) {
                System.err.println(e.getMessage());
            } catch(Exception e) {
                System.err.println("An unknown error has occurred");
            }
        }
    }
}

/*
sometimes the custom error handling message will occur the line after it is supposed to.
this only happens with System.err, not System.out and I don't know why, but I kept the System.err because it is cool
*/