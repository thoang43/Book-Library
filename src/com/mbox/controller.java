package com.mbox;

import com.sun.istack.internal.NotNull;
import frontend.data.Course;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class controller {

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }

    public static List<String> getAllEdition(){
        String temp = "First;Second;Third;Fourth;Fifth;Sixth;Seventh;Eighth;Ninth;Tenth;" +
                "Eleventh;Twelfth;Thirteenth;Fourteenth;Fifteenth";
        String[] tempArray = temp.split(";");

        return Arrays.asList(tempArray);
    }

    public static List<String> getAllTypes(){
        String temp = "Book;Online Service;Website;";
        String[] tempArray = temp.split(";");

        return Arrays.asList(tempArray);
    }

    public static String stringAdjustment(String main, String modify){

        int difference = main.length() - modify.length();

        if(difference>=0){
            for(int i=0; i<difference; i++){
                modify+=" ";
            }

        }

        return modify;
    }

    public static String multipyStr(String string, int times){
        String returned="";
        for(int i=0; i<times; i++){
            returned+=string;
        }
        return returned;
    }



    @NotNull
    public static String resourcesFormat(ArrayList<frontend.data.Resource> allResources, int max){

        StringBuilder resFormated = new StringBuilder();
        ArrayList<Integer> ids = new ArrayList<Integer>();
//        ArrayList<String> editions = new ArrayList<String>();

        int counter=0;
        for (frontend.data.Resource tempResource : allResources){
            if(counter<max){
                if(!ids.contains(tempResource.getID()) ){
                    resFormated.append(tempResource.toString()+"\n");
                    ids.add(tempResource.getID());
                    if(counter<max-1)
                        resFormated.append(multipyStr("-",tempResource.toString().length()) + "\n");
                }
            }
            else{
//                resFormated.deleteCharAt(resFormated.length()-1);
                break;
            }
            counter++;
        }
        resFormated.append("\n");

        return resFormated.toString();
    }


    @NotNull
    public static Semester findDefaultSemester(){
        Calendar c = Calendar.getInstance();
        String season="";
        int year=0;
        season = getSeason(c.get(Calendar.MONTH));
        year = (c.get(Calendar.YEAR));
        System.out.println("Semesterhey: "+ season+ "year: " +year);
        Semester semester1 = new Semester(season, year,0);
        semester1.setId(DBManager.getSemesterIDByName(season, Integer.toString(year)));

        System.out.println("Sem: " + semester1.getSeason() + " year:  " + semester1.getYear());
        return semester1;
    }
        public static String getSeason(int monthNumber) {
            String monthName = getMonthName(monthNumber);
            switch (monthName) {
                case "January":
                    return "Winter";

                case "February":
                    return "Winter";

                case "December":
                    return ("Winter");

                case "March":
                    return ("Spring");

                case "April":
                    return ("Spring");

                case "May":
                    return ("Summer 1");

                case "June":
                    return ("Summer 1");

                case "July":
                    return ("Summer 2");

                case "August":
                    return ("Summer 2");

                case "September":
                    return ("Fall");

                case "October":
                    return ("Fall");

                case "November":
                    return ("Fall");

                default:
                    return ("Invalid argument");
            }
        }

        public static String getMonthName(int monthNumber){


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


        public static Course searchForCourse(Course course, ArrayList<Course> courseList){
        Course result= new Course();
        int lastCourseID=0;
            for (Course item : courseList){

                if(item.getID() != lastCourseID){

                    if(course.getID() == item.getID()){

                        if (course.getCommonID() == item.getCommonID()) {
                            return item;
                        }
                    }
                    else
                        lastCourseID = item.getID();

                }


            }



        return result;
        }

        public static String convertSeasonGUItoDB(String season){
            season = season.toLowerCase();
            season = season.substring(0, 1).toUpperCase() + season.substring(1);
            if(season.equals("Summer_1")) {
                season = "Summer 1";
            }
            if(season.equals("Summer_2")){
                season = "Summer 2";
            }
            return season;
        }

        public static String convertSeasonDBtoGUI(String season){
            season = season.toUpperCase();
            if(season == "SUMMER 1" || season == "SUMMER 2")
                season = season.replace(' ', '_');

            return season;
        }
    }

