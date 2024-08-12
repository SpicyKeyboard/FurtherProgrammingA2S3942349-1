This program has three main functions.
It can add a course object, these objects being generated via scanning a CSV file with the correct layout, to an ArrayList
via a menu system where the specific object can be searched for using a word search.
It can display all objects that have been added to the ArrayList, along with all the course information.
It can remove course objects from the ArrayList using user input to determine which object to remove.

There are three larger classes and two error handling classes.

The first error handling class is called OutOfRangeException, this is for when the user inputs a number that is too
small or too large. An example would be the user inputting anything greater that 4 or lower than 1 (1>input>5) into
the main menu that only has four options, labelled from 1-4. This would be an out of range error.

The second error handling class is the IllegalDataTypeException, this is for when the user inputs the wrong data
type. An example would be the uer inputting anything other than an integer into the main menu, as the menu is
requesting an integer data type. If the user inputs a string or double this would be an IllegalDataTypeException.

The first class is the MyTimetable (renamed Main for submission) class that contains the main method. This class prints the initialisation
text along with some initialisation functions. Afterwards it runs the menu selection for whatever function
the user selects, until they exit the program.

The second class is the Course class, this contains all the fields and the constructors for the course object.
This is to provide encapsulation for the program, providing a slight boost to security. It has getters for everything
that is necessary along with a toString override because I don't want to look at Course[2351@2A] or something similar.

The third class is the CourseManagement class, which contains all the main functions / methods of the program.
It also has the two main ArrayLists. Depending on the function called from the main method, it will run a specific
function, usually returning a print command, or a user selection to manipulate the ArrayList of course objects.
This class is also the one that scans the CSV file into an ArrayList.

There is also the CourseManagementTest class that just contains the jUnit test methods.

|||RUNNING FROM COMMAND LINE|||

For running the code from command line, three commands will be needed.
The first line is to set the command directory to the file and is as follows: (where "..." is wherever it is installed locally)
cd ...\FurtherProgrammingAssignment1-S3942349

The second line is to compile the java files into class files that contain the batchcode and is as follows:
javac *.java

The third line is running the code via .class file using the installed jre and is as follows:
java Main