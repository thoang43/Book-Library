package Controllers;

import Models.backend.Publisher;
import Models.backend.Semester;
import Models.frontend.Course;
import Models.frontend.Resource;
import com.sun.istack.internal.NotNull;
import Models.frontend.Person;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Window;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class controller {

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }

    public static List<String> getAllEdition() {
        String temp = "First;Second;Third;Fourth;Fifth;Sixth;Seventh;Eighth;Ninth;Tenth;" +
                "Eleventh;Twelfth;Thirteenth;Fourteenth;Fifteenth";
        String[] tempArray = temp.split(";");

        return Arrays.asList(tempArray);
    }

    public static List<String> getAllTypes() {
        String temp = "Book;Online Service;Website;";
        String[] tempArray = temp.split(";");

        return Arrays.asList(tempArray);
    }

    public static String stringAdjustment(String main, String modify) {

        int difference = main.length() - modify.length();

        if (difference >= 0) {
            StringBuilder modifyBuilder = new StringBuilder(modify);
            for (int i = 0; i < difference; i++)
                modifyBuilder.append(" ");
            modify = modifyBuilder.toString();


        }

        return modify;
    }

    public static String multiplyStr(String string, int times) {
        String returned = "";
        for (int i = 0; i < times; i++) {
            returned += string;
        }
        return returned;
    }


    @NotNull
    public static String resourcesFormat(ArrayList<Resource> allResources, int max) {

        StringBuilder resFormated = new StringBuilder();
        ArrayList<Integer> ids = new ArrayList<>();
//        ArrayList<String> editions = new ArrayList<String>();

        int counter = 0;
        for (Resource tempResource : allResources) {
            if (counter < max) {
                if (!ids.contains(tempResource.getID())) {
                    resFormated.append(tempResource.toString() + "\n");
                    ids.add(tempResource.getID());
                    if (counter < max - 1)
                        resFormated.append(multiplyStr("-", tempResource.toString().length()) + "\n");
                }
            } else {
//                resFormated.deleteCharAt(resFormated.length()-1);
                break;
            }
            counter++;
        }
        resFormated.append("\n");

        return resFormated.toString();
    }


    @NotNull
    public static Semester findDefaultSemester() {
        Calendar c = Calendar.getInstance();
        String season;
        int year;

        season = getSeason(c.get(Calendar.MONTH));
        year = (c.get(Calendar.YEAR));
        System.out.println("Semesterhey: " + season + "year: " + year);
        Semester semester1 = new Semester(season, year, 0);
        semester1.setId(DBManager.getSemesterIDByName(season, Integer.toString(year)));

        System.out.println("Sem: " + semester1.getSeason() + " year:  " + semester1.getYear());
        return semester1;
    }

    private static String getSeason(int monthNumber) {
        String monthName = getMonthName(monthNumber);
        switch (monthName) {
            case "January":

            case "February":

            case "December":
                return ("Winter");

            case "March":

            case "April":

            case "May":
                return ("Spring");
            case "June":
                return ("Summer 1");

            case "July":
            case "August":
                return ("Summer 2");

            case "September":

            case "October":

            case "November":
                return ("Fall");

            default:
                return ("Invalid argument");
        }
    }

    private static String getMonthName(int monthNumber) {


        switch (monthNumber) {
            case 0:
                return "January";

            case 1:
                return "February";

            case 2:
                return "March";

            case 3:
                return "April";

            case 4:
                return "May";

            case 5:
                return "June";

            case 6:
                return "July";

            case 7:
                return "August";

            case 8:
                return "September";


            case 9:
                return "October";

            case 10:
                return "November";

            case 11:
                return "December";

            default:
                return ("Invalid argument");
        }


    }


    public static Course searchForCourse(Course course, ArrayList<Course> courseList) {
        Course result = new Course();
        int lastCourseID = 0;
        for (Course item : courseList) {

            if (item.getID() != lastCourseID) {

                if (course.getID() == item.getID()) {

                    if (course.getCommonID() == item.getCommonID()) {
                        return item;
                    }
                } else
                    lastCourseID = item.getID();

            }


        }


        return result;
    }

    public static String convertSeasonGUItoDB(String season) {
        season = season.toLowerCase();
        season = season.substring(0, 1).toUpperCase() + season.substring(1);
        if (season.equals("Summer_1")) {
            season = "Summer 1";
        }
        if (season.equals("Summer_2")) {
            season = "Summer 2";
        }
        return season;
    }

    public static String convertSeasonDBtoGUI(String season) {
        season = season.toUpperCase();
        if (season.equals("SUMMER 1") || season.equals("SUMMER 2"))
            season = season.replace(' ', '_');

        return season;
    }

    public static ArrayList<Person> convertBackendPersonToFrontendPerson(ArrayList<Models.backend.Person> backendPeople) {
        ArrayList<Person> temp = new ArrayList<>();
        if (backendPeople != null)
            for (Models.backend.Person backendPerson : backendPeople) {
                temp.add(backendPerson.initPersonGUI());
            }
        return temp;
    }

    public static String capitalizeString(String s){
        String result = "";

        try {
            String[] tempString = s.split("\\s");
            for (String a : tempString) {
                result = result + a.substring(0, 1).toUpperCase() + a.substring(1) + " ";
            }
            result = result.substring(0, result.length() - 1);
        }
        catch (Exception e){
            e.printStackTrace();
            result = s;
        }
        return result;

    }

    public static boolean isISBN(String s){
        String regex ="\\d+";
        return s.matches(regex) && s.length() == 10;
    }

    public static boolean isISBN13(String s){
        String regex ="\\d+";
        return s.matches(regex) && s.length() == 13;
    }

    public static ArrayList<Course> convertArrayCC(ArrayList<Models.backend.Course> c) {
        ArrayList<Course> arr = new ArrayList<>();
        for (int i = 0; i < c.size(); i++) {
            arr.add(c.get(i).initCourseGUI());
        }
        return arr;
    }

    public static ArrayList<Models.frontend.Publisher> convertArrayPubPub(ArrayList<Publisher> pub) {
        ArrayList<Models.frontend.Publisher> arr = new ArrayList<>();
        for (int i = 0; i < pub.size(); i++) {
            arr.add(pub.get(i).initPublisherGUI());
        }
        return arr;
    }

    public static ArrayList<Course> convertArrayCCBasic(ArrayList<Models.backend.Course> c) {
        ArrayList<Course> arr = new ArrayList<>();
        for (int i = 0; i < c.size(); i++) {
            arr.add(c.get(i).initCourseGUIBasic());
        }
        return arr;
    }

    @NotNull
    public static void copyCourse(Course selectedCourse, Course updated ) {

        selectedCourse.setID(updated.getID());
        selectedCourse.setTitle(updated.getTitle());
        selectedCourse.setDescription(updated.getDescription());
        selectedCourse.setDepartment(updated.getDepartment());

        selectedCourse.setSEMESTER(updated.getSEMESTER());
        selectedCourse.setYEAR(updated.getYEAR());

        selectedCourse.setResource(updated.getResource());
        selectedCourse.setProfessor(updated.getProfessor());



    }

    public static ArrayList<Image> tutorialImages() {
        ArrayList<Image> images = new ArrayList<>();
        images.add(new Image("Models/media/mainView.png"));
        images.add(new Image("Models/media/courseView.png"));
        images.add(new Image("Models/media/profView.png"));
        images.add(new Image("Models/media/resourceView.png"));
        images.add(new Image("Models/media/pubView.png"));
        images.add(new Image("Models/media/endView.png"));




        return images;

    }


        public static ArrayList<String> tutorialText(){
        ArrayList<String> retrunedList = new ArrayList<String>();
//        retrunedList.add("1. What do you first? \n 2. testing button\n");
//        retrunedList.add("1. What do you second? \n 2. testing button\n");
//        retrunedList.add("1. What do you third? \n 2. testing button\n");
        retrunedList.add(
                "1. Current year & semester of all classes in the table \n" +
                "2. Course & Professor information of chosen class are shown here, you can open course & professor dialogue to modify \n" +
                "3. Resource list of the chosen classes, you can modify resources by clicking the Edit button \n" +
                "4. You can search for any available class by entering information you know and press search button \n" +
                "5. You can export data, import data or exit the program here.");
        retrunedList.add(
                "1. Press this button to open detailed course view \n" +
                "2. You can choose available courses from the box \n" +
                "3. These fields contain the detailed information of the course you choose \n" +
                "4. You can press add if it is a new course, or delete if it does not exist anymore \n" +
                "5. Press Fill to fulfill the text fields in the main view");
        retrunedList.add(
                "1. Press this button to open detailed professor view \n" +
                "2. You can choose available professors from the box \n" +
                "3. These fields contain the detailed information of the professor you choose \n" +
                "4. You can add/delete professor or check for resources that the chosen professor now has \n" +
                "5. Press Fill to fulfill the text fields in the main view");
        retrunedList.add(
                "1. Press Edit button to open detailed resources view \n" +
                "2. These fields contain the information of the chosen resource on the right table \n" +
                "3. You can search for new resources online by search button \n" +
                "4. You can choose an available resources from database by Auto Fill button \n" +
                "5. You can add/update/delete resources with the three buttons at the bottom \n" +
                "6. After modifying all resources, press Assign the selected Resources to fulfill the resource Table ");
        retrunedList.add(
                "1. Press Publisher button in the publisher fields to open detailed publisher view \n" +
                "2. You can choose available publishers in the box \n" +
                "3. These fields contain the information of the chosen publisher \n" +
                "4. You can delete publisher if the publisher is not available \n" +
                "5. Press Create & Assigns to create new publisher or assign exist publisher for resource");
        retrunedList.add(
                "After adding/changing all the information in the left column" +
                "\n" +
                "press these buttons to add/update/delete the classes");


        return retrunedList;

    }

    public static void addNoteAndCRN(Models.frontend.Course c, String note, String CRN){
        c.setNotes(null);
        c.setCRN(null);
       if(note!=null&&!note.isEmpty()) {
            c.setNotes(note);
        }
        if(CRN!=null&&!CRN.isEmpty()){
            c.setCRN(Integer.valueOf(CRN));
        }
    }


    public static void setTextStyle(Text t){

    //        t.setX(10.0f);
    //    t.setY(50.0f);
    //    t.setCache(true);
        t.setFill(Color.web("#21618C"));
        t.setFont(Font.font(null, FontWeight.BOLD, 18));

    //    Reflection r = new Reflection();
    //    r.setFraction(0.2f);
    //
    //    t.setEffect(r);

    //    t.setTranslateY(400);


    }


    public static boolean writeDBTxt(TextField userName, TextField password, TextField host, TextField port, TextField SID){

        String serverPath;
        File file = new File("DBinformation.txt");

            serverPath = "jdbc:oracle:thin:"
                    + userName.getText()
                    + "/"
                    + password.getText()
                    + "@"
                    + host.getText()
                    + ":"
                    + port.getText()
                    + ":" + SID.getText();

        try {

            FileWriter fw = new FileWriter(file, true); //the true will append the new data
            fw.write(serverPath);//appends the string to the file
            fw.close();
            return true;

        }
        catch (IOException ioe) {
            System.err.println("IOException: " + ioe.getMessage());

        }

        return false;

}

    public static boolean confirmation(){

        Alert alert = new Alert(Alert.AlertType.WARNING , "Depending on your internet speed," +
                " this process may tak up to 2 minutes! So please sit back and be patient :-)", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Warning!");
        alert.setHeaderText("Are you sure you want to delete all the data and re-install the database?");

        Window alertWindow = alert.getDialogPane().getScene().getWindow();

        Optional<ButtonType> result = alert.showAndWait();

        if("YES".equals(result.get().getText().toUpperCase()))
            return true;
        else
            return false;
    }

}

