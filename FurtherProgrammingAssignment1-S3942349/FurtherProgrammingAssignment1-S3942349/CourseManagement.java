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
                availableCourses.add(new Course(splitCourse[0], Integer.parseInt(splitCourse[1]), splitCourse[2], splitCourse[3], splitCourse[4], splitCourse[5], Double.parseDouble(splitCourse[6])));
            }
        }
        coursesScanner.close();
    }

    public Course[] AddCoursesSearch() throws OutOfRangeException, IllegalDataTypeException {
        //scan for user input sting
        courseMatchCount = 0;
        Course[] courseMatchArr = new Course[availableCourses.size()];
        System.out.print("Please type the name or keywords of the course: ");
        Scanner userScan = new Scanner(System.in);
        String userAddInput = userScan.nextLine();
        String courseAddString = "-------------------------------\n" +
                "Please select one of the following options:\n" +
                "-------------------------------";
        System.out.println(courseAddString);
        //compare to available course names then print the names of the corresponding courses
        userAddInput = userAddInput.toLowerCase();
        boolean inputPresent = false;
        for (int i = 0; i < availableCourses.size(); i += 1) {
            String lowerCourseName = availableCourses.get(i).getName().toLowerCase();
            if (lowerCourseName.contains(userAddInput)) { //if user input is in any courseName
                inputPresent = true;
                System.out.println(" " + (courseMatchCount + 1) + ") " + availableCourses.get(i).getName()); //prints out refined course names with selection numbers
                courseMatchArr[courseMatchCount] = availableCourses.get(i);
                courseMatchCount += 1;
            }
        }
        if (inputPresent == false) {
            System.out.println("There were no results for the input names or keywords input.");
            return courseMatchArr;
        }
        return courseMatchArr;
    }

        public void AddCoursesSelect(Course[] courseMatchArr) throws IllegalDataTypeException, OutOfRangeException {
        //allow selection for each refined course, if selected put into enrolled courses arraylist
        //assigns the courses numbers
        System.out.print("Please select: ");
        Scanner userSelection = new Scanner(System.in);
        String userAddSelectionScan = userSelection.nextLine();
        try {
            Integer.parseInt(userAddSelectionScan);
        } catch (Exception e) {
            throw new IllegalDataTypeException("menu input is not an integer");
        }
        int userAddSelection = Integer.parseInt(userAddSelectionScan);
        if (userAddSelection <= 0 || userAddSelection > courseMatchCount) { //throws if input is not one of the options
            throw new OutOfRangeException("menu selection is out of range");
        }
        //if course is already enrolled in, don't allow enrollment and print out string saying that they are already enrolled
        boolean coursePresent = false;
        userAddSelection -= 1;
        for (int i = 0; i < enrolledCourses.size(); i += 1) {
            if (courseMatchArr[userAddSelection].getName().equals(enrolledCourses.get(i).getName())) { //course was present in arraylist, not adding it any telling the user that it is already in
                System.out.println("You have already enrolled in the selected course: " + courseMatchArr[userAddSelection].getName() + ", so there is no need to enroll again.");
                coursePresent = true;
            }
        }
        if (coursePresent == false) { //course was not present in arraylist therefore it adds the course to the array list
            enrolledCourses.add(courseMatchArr[userAddSelection]);
            System.out.println("You have enrolled in the course: " + courseMatchArr[userAddSelection].getName());
        }
    }

    public void ShowCourses() {
        boolean emptyEnrolledCoursesArr = true;
        for (int i = 0; i < enrolledCourses.size(); i+=1) {
            if (enrolledCourses.get(i) != null) {
                emptyEnrolledCoursesArr = false;
                break;
            }
        }
        if (emptyEnrolledCoursesArr == false) {
            String enrolledCoursesString = "Here are the courses you're enrolled in:\n" +
                    "-------------------------------";
            System.out.println(enrolledCoursesString);
            for (int i = 0; i < enrolledCourses.size(); i+=1) {
                if (enrolledCourses.get(i) != null) { //makes each enrolled course print out a nice readable string using getter methods
                    System.out.println(" " + (i+1) + ") " + enrolledCourses.get(i).getName() + " | " +
                            enrolledCourses.get(i).getMode() + " | " + enrolledCourses.get(i).getDay() + " Beginning at: " +
                            enrolledCourses.get(i).getTime() + " and lasting for: " + enrolledCourses.get(i).getDuration() + " hours.");
                }
            }
            System.out.println("-------------------------------");
        }
        else {
            String showCoursesString = "-------------------------------\n" +
                    "You do not have any enrolled courses, try selecting one to get started.\n" +
                    "-------------------------------";
            System.out.println(showCoursesString);
        }
    }

    public void RemoveCourses() throws OutOfRangeException, IllegalDataTypeException {
        System.out.println("-------------------------------");
        boolean emptyEnrolledCoursesArr = true;
        for (int i = 0; i < enrolledCourses.size(); i+=1) {
            if (enrolledCourses.get(i) != null) {
                emptyEnrolledCoursesArr = false;
                break;
            }
        }
        if (emptyEnrolledCoursesArr == false) {
            ShowCourses();
            //scan for user input
            System.out.println("Select a course to withdraw from: ");
            Scanner userRemoveScan = new Scanner(System.in);
            String userRemoveSelectionScan = userRemoveScan.nextLine();
            try {
                Integer.parseInt(userRemoveSelectionScan);
            } catch (Exception e) {
                throw new IllegalDataTypeException("menu input is not an integer");
            }
            int userRemoveSelection = Integer.parseInt(userRemoveSelectionScan);
            if (userRemoveSelection <= 0 || userRemoveSelection > enrolledCourses.size()) { //throws if input is not one of the options
                throw new OutOfRangeException("menu selection is out of range");
            }
            System.out.println("-------------------------------");
            //take the user selected number, correlate it to the arraylist index, and then remove the corresponding arraylist element
            String withdrawnString = "Withdrawn from the course: " + enrolledCourses.get(userRemoveSelection - 1) + "\n" +
                    "-------------------------------";
            System.out.println(withdrawnString);
            enrolledCourses.remove(userRemoveSelection - 1);
        }
        else {
            String noCoursesString = "You do not have any enrolled courses, try selecting one to get started.\n" +
                    "-------------------------------";
            System.out.println(noCoursesString);
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
