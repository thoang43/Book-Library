package com.mbox;

import frontend.data.PersonType;

import java.io.*;
import java.sql.*;
import java.util.*;

public class DBManager {
    public static Statement st;
    public static Statement stt;
    public static Statement st3;
    public static Statement st4;
    public static Statement st5;

    public static Connection conn;

    public DBManager() {
        //Method is empty for now
    }

    public static String readFromFile() {
        // The name of the file to open.
        String fileName = "DBinformation.txt";

        // This will reference one line at a time
        String line = null;

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader =
                    new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader =
                    new BufferedReader(fileReader);

            while ((line = bufferedReader.readLine()) != null) {
                return line;
            }

            // Always close files.
            bufferedReader.close();
        } catch (FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file '" +
                            fileName + "'");
        } catch (IOException ex) {
            System.out.println(
                    "Error reading file '"
                            + fileName + "'");
            // Or we could just do this:
            // ex.printStackTrace();
        }
        return null;
    }


    public static void openConnection() {
        try {
            String url = readFromFile();
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection(url);


            System.out.println("Successfully connected to the database");
            st = conn.createStatement();
            stt = conn.createStatement();
            st3 = conn.createStatement();
            st4 = conn.createStatement();
            st5 = conn.createStatement();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Connection to the database was NOT successful");

        }
    }

    public static void closeConnection() {
        try {
            if (conn.isClosed()) {

                System.out.println("The connection has already been closed");
            }

            conn.close();
            System.out.println("The connection to the database has been successfully terminated.");

        } catch (SQLException e) {

            System.out.println("Something went wrong when trying to close the connection");
        }
    }

    // Used to execute a non return query. Meaning? Either an update, insert or delete.
    public static void executeNoReturnQuery(String nrq) {

        try {

            st = conn.createStatement();

            st.executeQuery(nrq);

        } catch (SQLException e) {

            System.out.println("Something went wrong with the statement. Fix me");
        }
    }


    public static int getSemesterIDByName(String season, String year) {

        int id = 0;
        season = season.toLowerCase();
        season = season.substring(0, 1).toUpperCase() + season.substring(1);
        try {
            Statement st = conn.createStatement();
            String query = String.format("SELECT * FROM SEMESTER WHERE SEASON='%s' AND  YEAR='%s'", season, year);
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                id = rs.getInt(1);
            }

            return id;

        } catch (SQLException e) {

        }

        return 0;

    }


    public static Semester getSemesterNameByID(int id) {

        try {
            Statement st = conn.createStatement();
            String query = String.format("SELECT * FROM SEMESTER WHERE ID='%s'", id);
            ResultSet rs = st.executeQuery(query);
            Semester semester = new Semester();

            if (rs.next()) {
                semester.setYear( rs.getInt("YEAR"));
                String tempSeason = rs.getString("SEASON");
                tempSeason = tempSeason.toUpperCase();
                if(tempSeason == "SUMMER 1" || tempSeason == "SUMMER 2")
                    tempSeason.replace(' ', '_');
                semester.setSeason(tempSeason);

            }
            semester.setId(id);
            return semester;

        } catch (SQLException e) {

        }

        return null;

    }
    //============================================== PRINT METHODS =====================================================

    //======================================SELECT METHODS(PRINT TO SCREEN)=============================================

    //============================================PRINTS WHOLE TABLE====================================================

    // Regular Tables
    public static void printTablePerson() {

        try {

            ResultSet rs = st.executeQuery(getTablePersonQuery());

            while (rs.next()) {

                System.out.println(rs.getInt(1) + "|" + rs.getString(2) + "|" +
                        rs.getString(3) + "|" + rs.getString(4));
            }

        } catch (SQLException e) {

            e.printStackTrace();
            System.out.println("ERROR: Failed to execute 'printTablePerson()' method.");

        }
    }

    public static void printTableCourses() {

        try {

            ResultSet rs = st.executeQuery(getTableCourseQuery());

            while (rs.next()) {

                System.out.println(rs.getInt(1) + "|" + rs.getString(2) + "|" +
                        rs.getString(3) + "|" + rs.getString(4) + "|" +
                        rs.getString(5));
            }

        } catch (SQLException e) {

            e.printStackTrace();
            System.out.println("ERROR: Failed to execute 'printTableCourses()' method.");
        }
    }

    public static void printTableResources() {

        try {

            Statement st = conn.createStatement();

            ResultSet rs = st.executeQuery(getTableResourceQuery());

            while (rs.next()) {

                System.out.println(rs.getInt(1) + "|" + rs.getString(2) + "|" +
                        rs.getString(3) + "|" + rs.getString(4) + "|" +
                        rs.getInt(5) + "|" + rs.getInt(6) + rs.getString(7));
            }

        } catch (SQLException e) {

            e.printStackTrace();
            System.out.println("ERROR: Failed to execute 'printTableResources()' method.");

        }
    }

    public static void printTablePublishers() {

        try {

            ResultSet rs = st.executeQuery(getTablePublisherQuery());

            while (rs.next()) {

                System.out.println(rs.getInt(1) + "|" + rs.getString(2) + "|" +
                        rs.getString(3) + "|" + rs.getString(4));

            }
        } catch (SQLException e) {

            e.printStackTrace();
            System.out.println("ERROR: Failed to execute 'printTablePublishers()' method.");
        }
    }

    // Relationship Tables
    public static void printTableCoursePerson() {

        try {

            ResultSet rs = st.executeQuery(getTableCoursePersonQuery());
            while (rs.next()) {

                System.out.println(rs.getInt(1) + "|" + rs.getInt(2));
            }

        } catch (SQLException e) {

            e.printStackTrace();
            System.out.println("Exception when executing printTableCoursePerson() method.");
        }

    }

    public static void printTableCourseResource() {

        try {

            ResultSet rs = st.executeQuery(getTableCourseResourceQuery());
            while (rs.next()) {

                System.out.println(rs.getInt(1) + "|" + rs.getInt(2));
            }

        } catch (SQLException e) {

            e.printStackTrace();
            System.out.println("Exception when executing printTableCourseResource() method.");
        }
    }

    public static void printTableCourseSemester() {

        try {

            ResultSet rs = st.executeQuery(getTableCourseSemesterQuery());
            while (rs.next()) {

                System.out.println(rs.getInt(1) + "|" + rs.getInt(2) + "|" + rs.getInt(3));
            }

        } catch (SQLException e) {

            e.printStackTrace();
            System.out.println("Exception when executing printTableCourseSemester() method.");
        }

    }

    public static void printTablePersonResource() {

        try {

            ResultSet rs = st.executeQuery(getTablePersonResourceQuery());
            while (rs.next()) {

                System.out.println(rs.getInt(1) + "|" + rs.getInt(2));
            }

        } catch (SQLException e) {

            e.printStackTrace();
            System.out.println("Exception when executing printTablePersonResource() method.");
        }

    }

    public static void printTablePublisherResource() {

        try {

            ResultSet rs = st.executeQuery(getTablePublisherResourceQuery());
            while (rs.next()) {

                System.out.println(rs.getInt(1) + "|" + rs.getInt(2));
            }

        } catch (SQLException e) {

            e.printStackTrace();
            System.out.println("Exception when executing printTablePublisherResource() method.");
        }

    }

    //===========================================PRINTS MATCHING ID=====================================================

    public static void printPersonInTable(int id) {

        try {

            ResultSet rs = st.executeQuery(getPersonInTableQuery(id));
            while (rs.next()) {
                System.out.println(rs.getInt(1) + "|" + rs.getString(2) +
                        rs.getString(3) + rs.getString(4));
            }

        } catch (SQLException e) {

            e.printStackTrace();
            System.out.println("ERROR: Exception when trying to execute printPersonInTable() method.");
        }


        // return String.format("SELECT * FROM PERSON WHERE ID = %d", id);
    }

    public static void printCourseInTable(int id) {

        try {

            ResultSet rs = st.executeQuery(getCourseInTableQuery(id));

            while (rs.next()) {
                System.out.println(rs.getInt(1) + "|" + rs.getString(2) +
                        rs.getString(3) + rs.getString(4) + "|" + rs.getString(5));
            }

        } catch (SQLException e) {

            e.printStackTrace();
            System.out.println("ERROR: Exception when trying to execute printCourseInTable() method.");

        }
    }

    public static void printResourcesInTable(int id) {

        try {

            ResultSet rs = st.executeQuery(getResourceInTableQuery(id));

            while (rs.next()) {
                System.out.println(rs.getInt(1) + "|" + rs.getString(2) +
                        rs.getString(3) + rs.getString(4) + rs.getInt(5) +
                        rs.getInt(6) + rs.getString(7));
            }

        } catch (SQLException e) {

            e.printStackTrace();
            System.out.println("ERROR: Exception when trying to execute printResourcesInTable() method.");
        }

    }

    public static void printPublisherInTable(int id) {

        try {

            ResultSet rs = st.executeQuery(getPublisherInTableQuery(id));

            while (rs.next()) {
                System.out.println(rs.getInt(1) + "|" + rs.getString(2) +
                        rs.getString(3) + rs.getString(4));
            }

        } catch (SQLException e) {

            e.printStackTrace();
            System.out.println("ERROR: Exception when trying to execute printPublisherInTable() method.");
        }
    }



    //================================================= QUERIES ========================================================

    //===========================================SELECT METHODS (QUERIES)===============================================

    //===========================================SELECT WHOLE TABLE QUERY===============================================

    //Regular Tables
    public static String getTablePersonQuery() {

        return "SELECT * FROM PERSON";
    }

    public static String getTableCourseQuery() {

        return "SELECT * FROM COURSECT";
    }

    public static String getTableResourceQuery() {

        return "SELECT * FROM RESOURCES";
    }

    public static String getTablePublisherQuery() {

        return "SELECT * FROM PUBLISHERS";
    }

    //Relationship Tables
    public static String getTableCoursePersonQuery() {

        return "SELECT * FROM RELATION_COURSE_PERSON";

    }

    public static String getTableCourseResourceQuery() {

        return "SELECT * FROM RELATION_COURSE_RESOURCES";
    }

    public static String getTableCourseSemesterQuery() {

        return "SELECT * FROM RELATION_SEMESTER_COURSE";
    }

    public static String getTablePersonResourceQuery() {

        return "SELECT * FROM RELATION_PERSON_RESOURCES";
    }

    public static String getTablePublisherResourceQuery() {

        return "SELECT * FROM RELATION_PUBLISHER_RESOURCE";
    }

    //==============================================SELECT BY ID QUERY==================================================
    public static String getPersonInTableQuery(int id) {

        return String.format("SELECT * FROM PERSON WHERE ID=%d", id);
    }

    public static String getCourseInTableQuery(int id) {

        return String.format("SELECT * FROM COURSECT WHERE ID=%d", id);
    }

    public static String getResourceInTableQuery(int id) {

        return String.format("SELECT * FROM RESOURCES WHERE ID=%d", id);
    }

    public static String getPublisherInTableQuery(int id) {

        return String.format("SELECT * FROM PUBLISHERS WHERE ID=%d", id);
    }

    //===========================================Insert Method Queries==================================================

    public static String insertPersonQuery(Person person) {

        return String.format("INSERT INTO PERSON (FIRSTNAME, LASTNAME, TYPE) VALUES ('%s', '%s', '%s')",
                person.getFirstName(), person.getLastName(), person.getType());
    }

    public static int insertPersonQuery(frontend.data.Person person) {
        ResultSet rs;
        int id = 0;
        //boolean exit = false;
        person.setFirstName(person.getFirstName().toLowerCase());
        person.setFirstName(person.getFirstName().substring(0, 1).toUpperCase() + person.getFirstName().substring(1));

        person.setLastName(person.getLastName().toLowerCase());
        person.setLastName(person.getLastName().substring(0, 1).toUpperCase() + person.getLastName().substring(1));


        try {
            rs = st5.executeQuery(String.format("SELECT * FROM PERSON WHERE FIRSTNAME='%s' AND LASTNAME='%s' AND TYPE='%s'",
                    person.getFirstName(), person.getLastName(), person.getType()));

            while (rs.next()) {
                // Check if there is repetitive data in the db
                return rs.getInt(1);
//                if (person.getFirstName() == rs.getString(3)) {
//                    return rs.getInt(1);
//                }
            }
            String query = String.format("INSERT INTO PERSON (FIRSTNAME, LASTNAME, TYPE) VALUES ('%s', '%s', '%s')",
                    person.getFirstName(), person.getLastName(), PersonType.valueOf(person.getType()));
            st.executeQuery(query);
            String query2 = String.format("SELECT * FROM PERSON WHERE FIRSTNAME='%s' OR LASTNAME='%s'",
                    person.getFirstName(), person.getLastName());
            rs = st.executeQuery(query2);
            while (rs.next()) {

                id = (rs.getInt(1));
            }
            return id;

        } catch (SQLException err) {
            System.out.println(err);
        }
        return 0;
    }

    public static String insertCourseQuery(Course course) {

        return String.format("INSERT INTO COURSECT (TITLE, CNUMBER, DESCRIPTION, DEPARTMENT) VALUES " +
                        "('%s', '%s', '%s', '%s')", course.getTitle(), course.getCRN(), course.getDescription(),
                course.getDepartment());

    }

    public static int insertCourseQuery(frontend.data.Course course) {
        ResultSet rs;
        int id = 0;
        course.setTitle(course.getTitle().toUpperCase());
        course.setTitle(course.getTitle().replaceAll("\\s", ""));
        String[] cSplit = course.getTitle().split("(?<=\\D)(?=\\d)");

        System.out.println("INsertCOurseFunction: CourseTitle: " + cSplit[0] + " CourseN: " + cSplit[1]);
        try {

            String query2 = String.format("SELECT * FROM COURSECT WHERE TITLE='%s' AND CNUMBER = '%s'",
                    cSplit[0], cSplit[1]);
            rs = st.executeQuery(query2);
            while (rs.next()) {
                id = (rs.getInt(1));
                return id;
            }
            String query = String.format("INSERT INTO COURSECT (TITLE, CNUMBER, DESCRIPTION, DEPARTMENT) VALUES " +
                            "('%s', '%s', '%s', '%s')", cSplit[0], cSplit[1], course.getDescription(),
                    course.getDepartment());


            st.executeQuery(query);
            query2 = String.format("SELECT * FROM COURSECT WHERE TITLE='%s' AND CNUMBER = '%s' AND DESCRIPTION = '%s'",
                    cSplit[0], cSplit[1], course.getDescription());
            rs = st.executeQuery(query2);
            while (rs.next()) {

                id = (rs.getInt(1));
            }
            //System.out.println("ID dar akharin marhale hast: "+id);
            return id;


        } catch (SQLException err) {
            System.out.println(err);
            err.printStackTrace();
        }
        return 0;
    }


    public static String insertResourceQuery(Resource resource) {
//TODO: Khanh is here. IF in description, someone writes in it's a.... It will mistakenly understand that 'it's a test'(which three ' )
        return String.format("INSERT INTO RESOURCES (TYPE, TITLE, AUTHOR, ISBN, TOTAL_AMOUNT, CURRENT_AMOUNT, " +
                        "DESCRIPTION, ISBN13, EDITION) VALUES ('%s', '%s', '%s', '%s', %d, %d, '%s','%s','%s')",
                resource.getType(), resource.getTitle(), resource.getAuthor(), resource.getISBN(),
                resource.getTotalAmount(), resource.getCurrentAmount(), resource.getDescription(),
                resource.getIsbn13(),resource.getEdition());

    }

    public static String insertPublisherQuery(Publisher publisher) {

        return String.format("INSERT INTO PUBLISHERS (TITLE, CONTACT_INFO, DESCRIPTION) VALUES ('%s', '%s', '%s')",
                publisher.getTitle(), publisher.getContactInformation(), publisher.getDescription());

    }

    //============================================Update Methods Queries================================================

    public static String updatePersonQuery(Person person) {

        return String.format("UPDATE PERSON SET FIRSTNAME = '%s', LASTNAME = '%s', TYPE = '%s' WHERE ID = %d",
                person.getFirstName(), person.getLastName(), person.getType(), person.getID());
    }

    public static void updatePersonQuery(frontend.data.Person person) {

        String query = String.format("UPDATE PERSON SET FIRSTNAME = '%s', LASTNAME = '%s', TYPE = '%s' WHERE ID =%d",
                person.getFirstName(), person.getLastName(), PersonType.valueOf(person.getType()), person.getID());
        try {
            st.executeQuery(query);
        } catch (Exception e) {
            System.out.println("Update fail because of person error");
        }

    }

    public static String updateCourseQuery(Course course) {

        return String.format("UPDATE COURSECT SET TITLE = '%s', CNUMBER = '%s', DESCRIPTION = '%s', DEPARTMENT = '%s" +
                        "WHERE ID = %d", course.getTitle().substring(0, 4), course.getTitle().substring(4), course.getDescription(), course.getDepartment(),
                course.getID());

    }

    public static void updateCourseQuery(frontend.data.Course c) {
        System.out.println("Title: " + c.getTitle().substring(0, 4) + "  CNUMBER: " + c.getTitle().substring(4) +
                "  DESCRIPTION: " + c.getDescription() + "  DEPARTMENT: " + c.getDepartment() + "  ID: " + c.getID());

        String titletochange = c.getTitle();
        titletochange = titletochange.replaceAll("\\s", "");
        String[] part = titletochange.split("(?<=\\D)(?=\\d)");
        System.out.println(part[0]);
        System.out.println(part[1]);

        String query = String.format("UPDATE COURSECT SET TITLE = '%s', CNUMBER = '%s', DESCRIPTION = '%s', DEPARTMENT = '%s' " +
                "WHERE ID = %d", part[0], part[1], c.getDescription(), c.getDepartment(), c.getID());
        try {
            st.executeQuery(query);
        } catch (Exception e) {
            System.out.println("Update fail because of course error");
        }
    }

    //==================================

    // does not check person for now
    public static void updateCourseQuery2(frontend.data.Course c) {

        // check if the course already exists, check if the person already exists.
        // if course does not exist, create it and assign it.
        // if both do exist, just update

        //getting the person id

        String person_name = c.getProfessor().getFirstName();
        String person_last_name = c.getProfessor().getLastName();
        String person_type = c.getProfessor().getType();

        int id_1 = -1;

        //check if exists:

        try {

            ResultSet rs_1 = st3.executeQuery(String.format("SELECT * FROM PERSON WHERE FIRSTNAME = '%s' AND LASTNAME = '%s' AND TYPE = '%s'",
                    person_name, person_last_name, person_type));

            while (rs_1.next()) {

                id_1 = rs_1.getInt(1);
            }

            // if -1 means it found nothing
            if (id_1 == -1) {

                //create a new person

                System.out.println("It got here");
            } else {

                c.getProfessor().setID(id_1);
                System.out.println("or here");
            }

        } catch (SQLException e) {

            System.out.println("Something went wrong when looking for professors.");

        }


        //getting the semester:

        String semester = c.getSEMESTER();
        String year = String.valueOf(c.getYEAR());

        int semester_id = getSemesterIDByName(semester, year);

        // get c number:

        String title = c.getTitle();
        String cnumber;
        title = title.replaceAll("\\s", "");
        String[] part = title.split("(?<=\\D)(?=\\d)");

        title = part[0];
        cnumber = part[1];

        int id = -1;
        int new_id = -1;
        // determine if that course exists:

        try {

            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(String.format("SELECT * FROM COURSECT WHERE TITLE = '%s' AND CNUMBER = '%s'",
                    title, cnumber));


            while (rs.next()) {

                id = rs.getInt(1);
            }

            // if -1 means that it does not exist
            if (id == -1) {

                //create it here
                new_id = insertCourseQuery(c);


                //insert this new id into the semester_course table

                statement.executeQuery(String.format("UPDATE RELATION_SEMESTER_COURSE SET COURSEID = %d, SEMESTERID = %d WHERE COURSEID = %d", new_id, semester_id, c.getID()));
                statement.executeQuery(String.format("UPDATE RELATION_COURSE_PERSON SET COURSEID = %d, PERSONID = %d WHERE COURSEID = %d", new_id, c.getProfessor().getID(), c.getID()));

                if (c.getResource().isEmpty()) {

                } else {

                    statement.executeQuery(String.format("INSERT INTO RELATION)_COURSE_RESOURCES (COURSEID, RESOURCEID) VALUES (%d, %d)", new_id, c.getResource().get(0).getID()));

                }


            } else {

                //execute update method here
                executeNoReturnQuery(String.format("UPDATE COURSECT SET TITLE = '%s', CNUMBER = '%s', DESCRIPTION = '%s', DEPARTMENT = '%s' " +
                        "WHERE ID = %d", title, cnumber, c.getDescription(), c.getDepartment(), c.getID()));

            }

        } catch (SQLException e) {

            System.out.println("Turns out we are getting an exception @ updateCourseQuery2()");

        }


    }


    //==================================

    public static String updateResourceQuery(Resource resource) {

        return String.format("UPDATE RESOURCES SET TYPE = '%s', TITLE = '%s', AUTHOR = '%s', ISBN = '%s', " +
                        "TOTAL_AMOUNT = '%d', CURRENT_AMOUNT = '%d', DESCRIPTION = '%s', ISBN13 = '%s'," +
                        "EDITION = '%s' WHERE ID = '%d'",
                resource.getType(), resource.getTitle(), resource.getAuthor(), resource.getISBN(),
                resource.getTotalAmount(), resource.getCurrentAmount(), resource.getDescription(), resource.getIsbn13(),
                resource.getEdition(),
                resource.getID());

    }

    private String updatePublisherQuery(Publisher publisher) {

        return String.format("UPDATE PUBLISHER SET TITLE = '%s', CONTACT_INFO = '%s', DESCRIPTION = '%s' WHERE ID = %d",
                publisher.getTitle(), publisher.getContactInformation(), publisher.getDescription(),
                publisher.getID());
    }

    //==========================================Delete Method Queries===================================================

    private String deletePersonQuery(Person person) {

        return String.format("DELETE FROM PERSON WHERE ID = %d", person.getID());
    }

    private String deleteCourseQuery(Course course) {

        return String.format("DELETE FROM COURSECT WHERE ID = %d", course.getID());
    }

    private String deleteResourceQuery(Resource resource) {

        return String.format("DELETE FROM RESOURCES WHERE ID = %d", resource.getID());
    }

    private String deletePublisherQuery(Publisher publisher) {

        return String.format("DELETE FROM PUBLISHERS WHERE ID = %d", publisher.getID());
    }

    //============================================GUI METHODS===========================================================

    // 1st view:
    // Search by: Professor (name), Course(title), Resource(title), Semester-Year
    public static Person[] searchByProfessor(String name) {

        int i = 0;

        try {

            Statement st = conn.createStatement();
            String query = String.format("SELECT * FROM PERSON WHERE FIRSTNAME='%s'", name);
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                i++;
            }

            Person[] p = new Person[i];

            rs = st.executeQuery(query);
            i = 0;
            while (rs.next()) {

                p[i] = new Person(rs.getInt(1), rs.getString(2), rs.getString(3),
                        rs.getString(4));
                i++;
                return p;
            }

        } catch (SQLException e) {

        }

        return null;
    }

    public static ArrayList<Integer> searchGetCoursesIdsByProfessorName(String name) {

        ArrayList<Integer> arr = new ArrayList<>();

        try {

            Statement st = conn.createStatement();
            String query = String.format("SELECT * FROM PERSON WHERE FIRSTNAME='%s' OR LASTNAME='s'", name, name);

            ResultSet rs = st.executeQuery(query);

            ArrayList<Integer> professorids = new ArrayList<>();

            while (rs.next()) {

                professorids.add(rs.getInt(1));
            }

            for (int i = 0; i < professorids.size(); i++) {

                rs = st.executeQuery(String.format("SELECT * FROM RELATION_COURSE_PERSON WHERE PERSONID = %d",
                        professorids.get(i)));

                while (rs.next()) {

                    arr.add(rs.getInt(2));
                }
            }

            return arr;

        } catch (SQLException e) {

            System.out.println("Something went wrong");

        }

        return null;
    }

    public static ArrayList<frontend.data.Course> getCourseArrayByIDs(ArrayList<Integer> timmy) {

        ArrayList<frontend.data.Course> c = new ArrayList<>();
        frontend.data.Course course;

        try {

            Statement st = conn.createStatement();
            ResultSet rs;

            for (int i = 0; i < timmy.size(); i++) {

                rs = st.executeQuery(String.format("SELECT * FROM COURSECT WHERE ID=%d", timmy.get(i)));

                while (rs.next()) {

                    course = new frontend.data.Course(rs.getInt(1), rs.getString(2),
                            rs.getString(5), rs.getString(4));

                    c.add(course);
                }
            }

            return c;

        } catch (SQLException e) {


        }

        return null;

    }

    public static Course[] searchByCourse(String title) {
        int i = 0;

        try {

            Statement st = conn.createStatement();
            String query = String.format("SELECT * FROM COURSECT WHERE TITLE='%s'", title);
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                i++;
            }

            Course[] c = new Course[i];

            rs = st.executeQuery(query);
            i = 0;
            while (rs.next()) {

                c[i] = new Course(rs.getInt(1), rs.getString(2), rs.getString(3),
                        rs.getString(4), rs.getString(5));
                i++;
            }
            return c;
        } catch (SQLException e) {
            System.out.println(e);
        }

        return null;
    }

    public static Resource[] searchByResource(String title) {

        int i = 0;

        try {

            Statement st = conn.createStatement();
            String query = String.format("SELECT * FROM RESOURCES WHERE TITLE='%s'", title);
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                i++;
            }

            Resource[] r = new Resource[i];

            rs = st.executeQuery(query);
            i = 0;
            while (rs.next()) {

                r[i] = new Resource(rs.getInt(1), rs.getString(2), rs.getString(3),
                        rs.getString(4), rs.getString(5), rs.getInt(6),
                        rs.getInt(7), rs.getString(8));
                i++;
            }

            return r;

        } catch (SQLException e) {

        }

        return null;
    }

    // Work in progress
    public static Course[] searchBySemester(String semester, String year) {

        Course[] c = new Course[1];
        return c;

        /*

        //ID 49 for Semster -> Spring 2018
        int id[];
        int i = 0;
        Course[] c;

        try{

            String query = String.format("SELECT * FROM SEMESTER WHERE SEMESTER='%s', YEAR='%s'", semester, year);
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);

            while(rs.next()){

                i++;
            }

            id = new int[i];
            i = 0;

            while(rs.next()){

                id[i] = rs.getInt(1);
                i++;
            }


        }catch(SQLException e){

        }


        return c;
    }

    public static Course[] searchBySemester(String semester, String year){
        int i = 0;
        int j = 0;
        int[] arrayids;

        try{

            Statement st = conn.createStatement();
            String query = String.format("SELECT * FROM SEMESTER WHERE SEASON='%s', YEAR='%s'", semester, year);
            ResultSet rs = st.executeQuery(query);

            while(rs.next()){
                i++;
                j++;
            }

            arrayids = new int[j];

            Course[] c = new Course[i];

            rs = st.executeQuery(query);
            i = 0;
            while(rs.next()){

                c[i] = new Person(rs.getInt(1), rs.getString(2), rs.getString(3),
                        rs.getString(4));
                i++;
                return p;
            }

        }catch(SQLException e){

        }

        return null;
    }
    */
    }

    //================================All select methods returning object===============================================

    public static Person getPersonObject(int id) {

        Person p = new Person();

        try {

            ResultSet rs = st.executeQuery(getPersonInTableQuery(id));

            while (rs.next()) {

                p.setID(rs.getInt(1));
                p.setType(rs.getString(2));
                p.setFirstName(rs.getString(3));
                p.setLastName(rs.getString(4));
            }

        } catch (SQLException e) {

            System.out.println("Something went wrong inside of the method getPersonObject()");

        }

        return p;

    }

    public static Course getCourseObject(int id) {

        Course c = new Course();

        try {

            ResultSet rs = st.executeQuery(getCourseInTableQuery(id));

            while (rs.next()) {

                c.setID(rs.getInt(1));
                c.setTitle(rs.getString(2));
                c.setCRN(rs.getString(3));
                c.setDescription(rs.getString(4));
                c.setDepartment(rs.getString(5));
            }

        } catch (SQLException e) {

            System.out.println("Something went wrong inside of the method getPersonObject()");

        }

        return c;

    }

    public static Resource getResourceObject(int id) {

        Resource r = new Resource();

        try {

            ResultSet rs = st.executeQuery(getPersonInTableQuery(id));

            while (rs.next()) {

                r.setID(rs.getInt(1));
                r.setType(rs.getString(2));
                r.setTitle(rs.getString(3));
                r.setAuthor(rs.getString(4));
                r.setISBN(rs.getString(5));
                r.setTotalAmount(rs.getInt(6));
                r.setCurrentAmount(rs.getInt(7));
                r.setDescription(rs.getString(8));
            }

        } catch (SQLException e) {

            System.out.println("Something went wrong inside of the method getPersonObject()");

        }

        return r;

    }

    public static Publisher getPublisherObject(int id) {

        Publisher p = new Publisher();

        try {

            ResultSet rs = st.executeQuery(getPersonInTableQuery(id));

            while (rs.next()) {

                p.setID(rs.getInt(1));
                p.setTitle(rs.getString(2));
                p.setContactInformation(rs.getString(3));
                p.setDescription(rs.getString(4));
            }

        } catch (SQLException e) {

            System.out.println("Something went wrong inside of the method getPersonObject()");

        }

        return p;
    }

    //=====================================Methods for searching relationship tables====================================

    public static Person getPersonRelationTable(int id) {

        Person p = new Person();

        try {

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(String.format("SELECT * FROM RELATION_COURSE_PERSON WHERE COURSEID=%d", id));

            while (rs.next()) {

                p.setID(rs.getInt(2));
            }


            return p;


        } catch (SQLException e) {

            System.out.println("Something went wrong.");

        }

        return null;

    }

    public static Resource[] getResourcesRelationTable(int id) {

        Resource r[];
        try {

            st = conn.createStatement();
            ResultSet rs = st.executeQuery(String.format("SELECT * FROM RELATION_COURSE_RESOURCES WHERE COURSEID=%d", id));


            int i = 0;
            while (rs.next()) {

                i++;
            }

            r = new Resource[i];

            int j = 0;

            ResultSet sr = st.executeQuery(String.format("SELECT * FROM RELATION_COURSE_RESOURCES WHERE COURSEID=%d", id));

            while (rs.next()) {

                r[j].setID(sr.getInt(2));
            }

            return r;


        } catch (SQLException e) {

            System.out.println("Something went wrong");
        }

        return null;

    }

    //==================================================================================================================
    //                                                  Relational tables methods
    //==================================================================================================================


    public static Person setResourcesForPerson(Person person1) {
        ResultSet rss, rss2;

        int resourceID;
        int i = 0;

        //Resource[] resourcesList = new Resource[20];
        ArrayList<Resource> tempResourceList = new ArrayList<Resource>();

        try {
            Statement stTemp = conn.createStatement();

            rss = stTemp.executeQuery("SELECT * FROM RELATION_PERSON_RESOURCES WHERE PERSONID = " + person1.getID());
            while (rss.next()) {
                //there will be a list of all reousrces ID that is owned by Person
                resourceID = rss.getInt(2);

                rss2 = st3.executeQuery(getResourceInTableQuery(resourceID));
                while (rss2.next()) {
                    // ID, Type, Title, Author, ISBN, total, current, desc
//                    resourcesList[i] = new Resource(rss2.getInt(1), rss2.getString(2),
//                            rss2.getString(3), rss2.getString(4), rss2.getString(5),
//                            rss2.getInt(6), rss2.getInt(7), rss2.getString(8));

                    tempResourceList.add(new Resource(rss2.getInt(1), rss2.getString(2),
                            rss2.getString(3), rss2.getString(4), rss2.getString(5),
                            rss2.getInt(6), rss2.getInt(7), rss2.getString(8)));

                    //setPublisherForResource(resourcesList[i]);
                    setPublisherForResource(tempResourceList.get(i));

                    i++;
                }
            }
            //person1.setResourcesPerson(resourcesList);
            person1.setResourceList(tempResourceList);
            return person1;
        } catch (SQLException err) {
            err.printStackTrace();
        }
        // Adding the list of the resources to the person object
        return null;
    }


    public static ArrayList<Resource> findResourcesCourseAvoidRepetitive(int courseID) {

        ResultSet rs;
        int resourceID = 0,
                i = 0, before = -1;
        //Resource[] resourcesList = new Resource[20];
        ArrayList<Resource> listResources = new ArrayList<Resource>();

        try {

            rs = st.executeQuery("SELECT * FROM RELATION_COURSE_RESOURCES WHERE COURSEID = " + courseID +
                    "ORDER BY RESOURCEID ASC");
            //here all have a result set of all resources for courdeID

            while (rs.next()) {
                resourceID = rs.getInt("RESOURCEID");
                if (before != resourceID) {
                    before = resourceID;
                    ResultSet rss = st5.executeQuery(getResourceInTableQuery(resourceID));

                    while (rss.next()) {
                        System.out.println("");
                        // ID, Type, Title, Author, ISBN, total, current, desc

                        listResources.add(new Resource(rss.getInt(1), rss.getString(2),
                                rss.getString(3), rss.getString(4), rss.getString(5),
                                rss.getInt(6), rss.getInt(7), rss.getString(8)));
//                setPublisherForResource(resourcesList[i]);
                        setPublisherForResource(listResources.get(i));

                        i++;
                    }
                }

            }

            //return resourcesList;
            return listResources;
        } catch (SQLException err) {
            System.out.println(err);
        }
        // Adding the list of the resources to the person object
        listResources.clear();
        return listResources;

    }


    public static ArrayList<Resource> findResourcesCourse(int courseID, int commonID) {

        ResultSet rs;
        int resourceID = 0;
        int i = 0;
        ArrayList<Resource> listResources = new ArrayList<Resource>();

        try {

            rs = st.executeQuery(String.format("SELECT * FROM RELATION_COURSE_RESOURCES WHERE COURSEID = %d AND COMMONID = %d",
                    courseID, commonID));
            //here all have a result set of all resources for courdeID

            while (rs.next()) {
                resourceID = rs.getInt(2);
                ResultSet rss = st5.executeQuery(getResourceInTableQuery(resourceID));

                while (rss.next()) {
                    // ID, Type, Title, Author, ISBN, total, current, desc
                    listResources.add(new Resource(rss.getInt(1), rss.getString(2),
                            rss.getString(3), rss.getString(4), rss.getString(5),
                            rss.getInt(6), rss.getInt(7), rss.getString(8)));
                    listResources.get(i).setCommonID(commonID);
                    setPublisherForResource(listResources.get(i));

                    i++;
                }

            }

            return listResources;
        } catch (SQLException err) {
            System.out.println(err);
            err.printStackTrace();
        }
        // Adding the list of the resources to the person object
        return null;

    }


    public static Resource setPublisherForResource(Resource resource1) {
        ResultSet rss;
        ResultSet rs2;
        int publisherID = 0, i = 0;

        Publisher publisherInstance = new Publisher();

        //Resource[] resourcesList = new Resource[20];

        try {
            Statement stpublisher1 = conn.createStatement();
            Statement stpublisher2 = conn.createStatement();

            rs2 = stpublisher1.executeQuery("SELECT * FROM RELATION_PUBLISHER_RESOURCE WHERE RESOURCEID = " +
                    resource1.getID());

            while (rs2.next()) {
                //there will be a list of all reousrces ID that is owned by Person
                publisherID = rs2.getInt(1);

                rss = stpublisher2.executeQuery(getPublisherInTableQuery(publisherID));
                while (rss.next()) {
                    // ID, Type, Title, Author, ISBN, total, current, desc
                    publisherInstance = new Publisher(rss.getInt(1), rss.getString(2),
                            rss.getString(3), rss.getString(4));
                    i++;
                }
                rss.close();
            }
            rs2.close();
            resource1.setPublisherInstance(publisherInstance);
            return resource1;
        } catch (SQLException err) {
            err.printStackTrace();
        }
        // Adding the list of the resources to the person object
        return null;

    }


    public static ArrayList<Course> relationalReadByCourseID(int courseID) {
        long startTime = System.nanoTime();

        // This method is only accept ONE courseID and will find all relations to that course
        //So you may need to call the function N times with different courseID to get all information stored in table
        //Course[] courseArray = new Course[20]; //Will make it a dynamic array list
        ArrayList<Course> courseList = new ArrayList<>();
        ResultSet rsTmp;

        int personID = 0, i = 0, pID = 0, cID = 0, commonID = 0;
        int[] pr = new int[20], cr = new int[20];
        ResultSet rs;
        String cTitle = "", cDescription = "", cDepartment = "";

        Person personTmp = new Person();
        //Resource[] courseResources = new Resource[20];
        ArrayList<Resource> courseResources = new ArrayList<Resource>();

        try {
            Statement stTemp = conn.createStatement();
            Statement stTemp2 = conn.createStatement();

            ResultSet rsTemp;


            //=======================Getting information to create the course object====================================

            rs = st.executeQuery(getCourseInTableQuery(courseID));

            while (rs.next()) {

                cTitle = rs.getString("TITLE") + " " + rs.getString("CNUMBER");
                cID = rs.getInt("ID");
                cDescription = rs.getString("DESCRIPTION");
                cDepartment = rs.getString("DEPARTMENT");
                System.out.println("\ncourseID " + courseID);
            }
            //courseResources = findResourcesCourse(courseID);


            //=======================Finding and creating Persons list teaching that course=============================

            i = 0;
            rsTmp = stTemp2.executeQuery("SELECT * FROM RELATION_COURSE_PERSON WHERE COURSEID = " + courseID);
            //it supposed to get a list of all persons teaching that course. Assuming one person for now.

            int j = 0;
            while (rsTmp.next()) {

                //courseArray[i] = new Course(cID, cTitle, cDepartment, cDescription, "CRN");
                courseList.add(new Course(cID, cTitle, cDescription, cDepartment, "CRN"));
                System.out.println("\nThis is the: " + j);
                j++;

                personID = rsTmp.getInt(2);
                commonID = rsTmp.getInt("commonid");
                System.out.println("Commonid is: " + commonID);
                System.out.println("PersonID is: " + personID);
                courseList.get(i).setCommonID(commonID);


                rsTemp = stTemp.executeQuery(getPersonInTableQuery(personID));
                while (rsTemp.next()) {
                    // I'm getting all the info for each person,
                    // this loop would run n times for n persons, but must be only one result
                    //meaning the loop runs only once each time.

                    personTmp = new Person(personID, rsTemp.getString(3), rsTemp.getString(4),
                            rsTemp.getString(2));
                    personTmp.setCommonid(commonID);


                    personTmp = setResourcesForPerson(personTmp);
                    //courseArray[i].setPersonInstance(personTmp);
                    //courseArray[i].setResourceInstance(courseResources);
                    //courseArray[i].setResourceInstances(courseResources);
                    courseList.get(i).setPersonInstance(personTmp);

                    //mneed to check for resources with the same commonid
                    courseResources = findResourcesCourse(courseID, commonID);
                    courseList.get(i).setResourceInstances(courseResources);

                    i++;
                }


            }

            long endTime = System.nanoTime();
            long duration = (endTime - startTime);
            System.out.println("It took this time to run Read Relational: " + duration / 1000000 + "ms For " + courseList.size() + " courses.\n");
            return courseList;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void getHistory() throws SQLException{
        int i=0;
        StringBuilder lsd = new StringBuilder();
        ResultSet ts3 = st.executeQuery("select v.SQL_TEXT,\n" +
                "           v.FIRST_LOAD_TIME,\n" +
                "           v.DISK_READS,\n" +
                "           v.ROWS_PROCESSED,\n" +
                "           v.ELAPSED_TIME\n" +
                "           from v$sql v\n" +
                "where to_date(v.FIRST_LOAD_TIME,'YYYY-MM-DD hh24:mi:ss')>ADD_MONTHS(trunc(sysdate,'MM'),-1) " +
                " ORDER BY v.FIRST_LOAD_TIME DESC");
            while (ts3.next()){
                i++;
                    if(i<800)
                        continue;

                    lsd.append(String.format("%s: ",Integer.toString(i)));
                    lsd.append(ts3.getString("SQL_TEXT") +"\t"+ts3.getString("FIRST_LOAD_TIME")+"\n");
                    lsd.append(String.format("____________________________\n"));
            }
        ts3.close();
        try (PrintWriter out = new PrintWriter("DBHistory.txt")) {
            out.println(lsd.toString());

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }


//======================================================================================================================
//                                 Efficient new methods, reading db
// =====================================================================================================================

    public static frontend.data.Resource setPublisherForResource2(frontend.data.Resource resource1) {
        ResultSet rss, rs2;
        int publisherID = 0, i = 0;

        frontend.data.Publisher publisherInstance = new frontend.data.Publisher();

        try {
            Statement stpublisher1 = conn.createStatement();
            Statement stpublisher2 = conn.createStatement();

            rs2 = stpublisher1.executeQuery(String.format("SELECT * FROM RELATION_PUBLISHER_RESOURCE WHERE RESOURCEID = %s ORDER BY PUBLISHERID DESC",
                    resource1.getID() ));

            if (rs2.next()) {

                //there will be a list of all reousrces ID that is owned by Person
                publisherID = rs2.getInt("PUBLISHERID");

                    rss = stpublisher2.executeQuery(getPublisherInTableQuery(publisherID));
                    if (rss.next()) {

                        publisherInstance = new frontend.data.Publisher(rss.getInt("ID"),
                                rss.getString("TITLE"),
                                 rss.getString("DESCRIPTION"), rss.getString("CONTACT_INFO"));
                        i++;
                    }
                    rss.close();
                    resource1.setPublisher(publisherInstance);


            }
            rs2.close();
            return resource1;
        } catch (SQLException err) {
            err.printStackTrace();
        }
        return resource1;

    }

public static ArrayList<frontend.data.Resource> findResourcesCourse2(int courseID, int commonID, Map<Integer, frontend.data.Resource> tempCach ) {

    ResultSet rs, rss;
    int resourceID = 0, i = 0;
    Statement stTemp;
    frontend.data.Resource tempResource;
    ArrayList<frontend.data.Resource> listResources = new ArrayList<frontend.data.Resource>();

    try {
        stTemp = conn.createStatement();

        rs = st.executeQuery(String.format("SELECT * FROM RELATION_COURSE_RESOURCES WHERE COURSEID = %d AND COMMONID = %d",
                courseID, commonID));
        //here all have a result set of all resources for courdeID

        while (rs.next()) {
            resourceID = rs.getInt("RESOURCEID");
            if(tempCach.containsKey(resourceID))
                listResources.add(tempCach.get(resourceID));

            else {

                rss = stTemp.executeQuery(getResourceInTableQuery(resourceID));

                if (rss.next()) {
                    // ID, Type, Title, Author, ISBN, total, current, desc
                    tempResource = new frontend.data.Resource(rss.getInt("ID"),
                            rss.getString("ISBN"), rss.getString("TYPE"),
                            rss.getString("TITLE"), rss.getString("AUTHOR"),
                            rss.getString("DESCRIPTION"), rss.getInt("TOTAL_AMOUNT"),
                            rss.getInt("CURRENT_AMOUNT"));

                    tempResource.setISBN13(rss.getString("ISBN13"));
                    if ( rss.getString("EDITION")!="")
                        tempResource.setEdition(rss.getString("EDITION"));

                    listResources.add(tempResource);
                    setPublisherForResource2(tempResource);
                    tempCach.put(resourceID, tempResource);

                    i++;
                }
            }



        }

        return listResources;
    } catch (SQLException err) {
        System.out.println(err);
        err.printStackTrace();
    }
    // Adding the list of the resources to the person object
    return null;

}

    public static ArrayList<frontend.data.Course> relationalReadByCourseID2(Map<Integer, ArrayList<Integer>> courseIds, Semester semester ) {
        long startTime = System.nanoTime();
       String tempSemester = semester.getSeason();
       tempSemester = tempSemester.toUpperCase();
       tempSemester = tempSemester.replace(' ', '_');
       semester.setSeason(tempSemester);

        ArrayList<frontend.data.Course> courseList = new ArrayList<>();

        Map<Integer, frontend.data.Person> cachedPersons = new HashMap<Integer, frontend.data.Person>();
            Map<Integer, frontend.data.Resource> cachedResources = new HashMap<Integer, frontend.data.Resource>();

        //Map<Integer, Course> cachedCourses = new HashMap<Integer, Course>();  Think about caching course later on


        int personID = 0, i, cID = 0, courseID =0;
        ResultSet rs, rsTmp, rsTemp;
        Statement stTemp, stTemp2;

        String cTitle = "", cDescription = "", cDepartment = "";

        frontend.data.Person personTmp = new frontend.data.Person();
        ArrayList<frontend.data.Resource> courseResources = new ArrayList<frontend.data.Resource>();

        try {
             stTemp = conn.createStatement();
             stTemp2 = conn.createStatement();

            //=======================Getting information to create the course====================================
            i = 0;
            for (Map.Entry<Integer, ArrayList<Integer>> entry : courseIds.entrySet()){
                // Technically there should be only one entry, since there is only one key
                // Big O (1)
                courseID = entry.getKey();

            rs = st.executeQuery(getCourseInTableQuery(courseID));

            if (rs.next()) {
                cID = rs.getInt("ID");
                cTitle = rs.getString("TITLE") + " " + rs.getString("CNUMBER");
                cDescription = rs.getString("DESCRIPTION");
                cDepartment = rs.getString("DEPARTMENT");
                System.out.println("\ncourseID " + courseID);
            }

            else return courseList;

            //=======================Finding and creating Person teaching the course=============================


                for (Integer tempCommonID: entry.getValue()) {
                    // Big O (commonIDs.Size)

                    rsTmp = stTemp2.executeQuery("SELECT * FROM RELATION_COURSE_PERSON WHERE COMMONID = " +
                            tempCommonID);
                    if(rsTmp.next()){
                        System.out.println("semester: " + semester.getSeason());
                        courseList.add(new frontend.data.Course(cID, tempCommonID, semester.getYear(), semester.getSeason(),
                                cTitle, cDepartment, personTmp, cDescription, courseResources));
                        courseList.get(i).setCommonID(tempCommonID);

                        personID = rsTmp.getInt("PERSONID");

                        if(cachedPersons.containsKey(personID))

                            courseList.get(i).setProfessor(cachedPersons.get(personID));

                        else {
                            rsTemp = stTemp.executeQuery(getPersonInTableQuery(personID));
                            if (rsTemp.next()) {

                                PersonType enumTmp;
                                switch (rsTemp.getString("TYPE")){
                                    case "ProgramCoordinator" : enumTmp = PersonType.ProgramCoordinator; break;
                                    case "CourseCoordinator" : enumTmp = PersonType.CourseCoordinator; break;
                                    case "CourseInstructor" : enumTmp = PersonType.CourseInstructor; break;
                                    default: enumTmp = PersonType.CourseInstructor;
                                }

                                personTmp = new frontend.data.Person( rsTemp.getString("LASTNAME"),
                                        rsTemp.getString("FIRSTNAME"), personID,
                                        enumTmp.toString());

                                    cachedPersons.put(personID, personTmp);

                                courseList.get(i).setProfessor(personTmp);
                            }
                            else{
                                personTmp = new frontend.data.Person("NOT FOUND",
                                        "NOT FOUND", personID,"CourseInstructor");
                                courseList.get(i).setProfessor(personTmp);
                            }

                        }
                        //=======================Finding and creating course resources=============================


                        courseResources = findResourcesCourse2(courseID, tempCommonID, cachedResources);
                        courseList.get(i).setResource(courseResources);
                        i++;

                    }

                }

            }

            long endTime = System.nanoTime();
            long duration = (endTime - startTime);
            System.out.println("It took this time to run Read Relational: " + duration / 1000000 + "ms For " +
                    courseList.size() + " courses.\n");
            return courseList;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static Map<Integer, ArrayList<Integer>> getCourseIdsBySemesterID2(int id) {

        int i = 0, before =0;
        Map<Integer, ArrayList<Integer>> courseIDs = new HashMap<Integer, ArrayList<Integer>>();
        String query = String.format("SELECT * FROM RELATION_SEMESTER_COURSE WHERE SEMESTERID=%d ORDER BY COURSEID ASC",
                id);
        ArrayList<Integer> commonIDs = new ArrayList<Integer>();


        try {

            Statement st = DBManager.conn.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                if (before == rs.getInt("COURSEID")) {
                    commonIDs.add(rs.getInt("ID"));

                }
                else{
                    before = rs.getInt("COURSEID");
                    commonIDs = new ArrayList<Integer>();
                    commonIDs.add(rs.getInt("ID"));
                    courseIDs.put(rs.getInt("COURSEID"), commonIDs);
                }
            }

            return courseIDs;


        } catch (SQLException e) {

            System.out.println("Something went wrong");

        }

        return courseIDs;
    }


    public static ArrayList<frontend.data.Course> returnEverything2(int semesterid) {
        Semester semester = getSemesterNameByID(semesterid);
        Map<Integer, ArrayList<Integer>> courseIDs = getCourseIdsBySemesterID2(semesterid);
        ArrayList<frontend.data.Course> courses = new ArrayList<>();

        courses = relationalReadByCourseID2(courseIDs, semester);


        return courses;
    }


//======================================================================================================================
//                                       End of Efficient new methods, reading db
// =====================================================================================================================


    //==================================================================================================================
    //==================================================================================================================

    public static void relationalInsertByID() {

        Scanner scan = new Scanner(System.in);
        //DBManager DB = new DBManager();
        System.out.println("Format: <CourseID> <PersonID> <ResourceID> <PublisherID> <SemesterID>");
        System.out.println("Enter <exit> to exit.");

        while (true) {
            System.out.println("Enter  CourseID, PersonID, ResourceID, PublisherID, SemesterID: ");
            String input = scan.nextLine();
            if (!input.contains("exit")) {
                String[] values = input.split(" ");

                executeNoReturnQuery(String.format("INSERT INTO RELATION_SEMESTER_COURSE" +
                        " (COURSEID, SEMESTERID) VALUES ('%d', '%d')", Integer.parseInt(values[0]), Integer.parseInt(values[4])));


                executeNoReturnQuery(String.format("INSERT INTO RELATION_COURSE_PERSON" +
                        " (COURSEID, PERSONID) VALUES ('%d', '%d')", Integer.parseInt(values[0]), Integer.parseInt(values[1])));
                executeNoReturnQuery(String.format("INSERT INTO RELATION_COURSE_RESOURCES" +
                        " (COURSEID, RESOURCEID) VALUES ('%d', '%d')", Integer.parseInt(values[0]), Integer.parseInt(values[2])));
//
//                executeNoReturnQuery(String.format("INSERT INTO RELATION_PERSON_RESOURCES" +
//                        " (PERSONID, RESOURCEID) VALUES ('%d', '%d')", Integer.parseInt(values[1]), Integer.parseInt(values[2])));
                executeNoReturnQuery(String.format("INSERT INTO RELATION_PUBLISHER_RESOURCE" +
                        " (PUBLISHERID, RESOURCEID) VALUES ('%d', '%d')", Integer.parseInt(values[3]), Integer.parseInt(values[2])));
                System.out.println("Added ID");
            } else {
                break;
            }
        }
    }

    public static frontend.data.Course relationalInsertByID2(frontend.data.Course c) {

        //getting all the data
        int id = c.getID();
        String year = String.valueOf(c.getYEAR());
        String semester = c.getSEMESTER();
        String title = c.getTitle();
        String dept = c.getDepartment();
        String desc = c.getDescription();
        int personid = c.getProfessor().getID(), commonID=0;

        semester = semester.toLowerCase();
        semester = semester.substring(0,1).toUpperCase() + semester.substring(1);
        semester = semester.replace('_',' ');

        int semesterid = getSemesterIDByName(semester, year);

        System.out.println("Semester: "+semester + " ID foudn: " + semesterid);

        System.out.println(personid);
        System.out.println(semesterid);

        // Fall 2018 ID = 52
//        int semesterid = 57;
        ArrayList<frontend.data.Resource> r = c.getResource();

        System.out.println();
        System.out.println("CHECKING " + r.size());

        int[] resourceidlist = new int[r.size()];

        for (int i = 0; i < c.getResource().size(); i++) {

            resourceidlist[i] = r.get(i).getID();
        }

        try {
            Statement st = conn.createStatement();
            ResultSet rs;
            executeNoReturnQuery(String.format("INSERT INTO RELATION_SEMESTER_COURSE" +
                    " (COURSEID, SEMESTERID) VALUES ('%d', '%d')", id, semesterid));
            rs = st.executeQuery("select id from (select * from RELATION_SEMESTER_COURSE  order by id desc) where rownum = 1");
            if(rs.next()){
                commonID = rs.getInt("ID");
            }


            executeNoReturnQuery(String.format("INSERT INTO RELATION_COURSE_PERSON" +
                    " (COURSEID, PERSONID) VALUES ('%d', '%d')", id, personid));

            for (int j = 0; j < resourceidlist.length; j++) {

                executeNoReturnQuery(String.format("INSERT INTO RELATION_COURSE_RESOURCES" +
                        " (COURSEID, RESOURCEID) VALUES ('%d', '%d')", id, resourceidlist[j]));
            }




            for (int l = 0; l < resourceidlist.length; l++) {
                if(r.get(l).getPublisher() != null) {
                    if (!availablePublisher(r.get(l).getPublisher())) {

                        executeNoReturnQuery(String.format("INSERT INTO RELATION_PUBLISHER_RESOURCE" +
                                        " (PUBLISHERID, RESOURCEID) VALUES ('%d', '%d')", r.get(l).getPublisher().getID(),
                                resourceidlist[l]));
                    }
                }


            }
            c.setCommonID(commonID);
            return c;

        }

        catch (SQLException e){
            e.printStackTrace();
        }
        return c;

    }

    public static void deleteRelationCourseResources(frontend.data.Course c) {
        ResultSet rs;
        int personID = c.getProfessor().getID();
        int courseID = c.getID();

        try {

            executeNoReturnQuery(String.format("DELETE FROM RELATION_COURSE_RESOURCES WHERE" +
                    " COURSEID = '%d' AND COMMONID = '%d'", courseID, c.getCommonID()));
//            executeNoReturnQuery(String.format("DELETE FROM RELATION_PERSON_RESOURCES WHERE" +
//                    " PERSONID = '%d' AND COMMONID = '%d'", personID, c.getCommonID()));
//            executeNoReturnQuery(String.format("DELETE FROM RELATION_COURSE_PERSON WHERE" +
//                    " COURSEID = '%d' AND COMMONID = '%d'", courseID, c.getCommonID()));
            for (int i = 0; i < c.getResource().size(); i++) {
                executeNoReturnQuery(String.format("DELETE FROM RELATION_PUBLISHER_RESOURCE WHERE" +
                        " RESOURCEID = '%d'", c.getResource().get(i).getID()));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }

    public static void insertRelationCourseResources(frontend.data.Course c) {

        //getting all the data
        int id = c.getID();
        int commonID = c.getCommonID();

        int personid = c.getProfessor().getID();

        ArrayList<frontend.data.Resource> r = c.getResource();





        for (int j = 0; j < c.getResource().size(); j++) {
            String tempQuerry = String.format("INSERT INTO RELATION_COURSE_RESOURCES" +
                    " (COURSEID, RESOURCEID, COMMONID) VALUES ('%d', '%d', '%d')", id, c.getResource().get(j).getID(), commonID);
            executeNoReturnQuery(tempQuerry);

//            String tempQuery = String.format("INSERT INTO RELATION_PERSON_RESOURCES" +
//                    " (PERSONID, RESOURCEID, COMMONID) VALUES ('%d', '%d','%d')", personid, resourceidlist[j], commonID);
//            executeNoReturnQuery(tempQuery);


            // there is a problm with this
//            System.out.println("Testing purpose:\n" + tempQuery + "\n\n\n\n");



        }
        try {
            for (int k = 0; k < r.size(); k++) {

                int resourceID = 0;
                if(r.get(k).getPublisher() != null) {
                    ResultSet rs = st.executeQuery(String.format("SELECT * FROM RELATION_PUBLISHER_RESOURCE WHERE RESOURCEID ='%d' AND " +
                            "PUBLISHERID = '%d'", r.get(k).getID(), r.get(k).getPublisher().getID()));
                    while (rs.next()) {
                        resourceID = rs.getInt(2);
                    }
                    System.out.println("resourceID now " + resourceID);
                    if (resourceID == 0) {
                        executeNoReturnQuery(String.format("INSERT INTO RELATION_PUBLISHER_RESOURCE" +
                                        " (PUBLISHERID, RESOURCEID) VALUES ('%d', '%d')", r.get(k).getPublisher().getID(),
                                r.get(k).getID()));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }



    public static void setIDforResource(frontend.data.Resource resource) {

        try {
            Statement st = conn.createStatement();

            int resourceID = 0;

            String query = String.format("SELECT * FROM RESOURCES WHERE TITLE='%s' AND AUTHOR ='%s' AND " +
                            "EDITION = '%s' AND TYPE = '%s' ", resource.getTitle(), resource.getAuthor(), resource.getEdition(),
                    resource.getTYPE());

            ResultSet rs = st.executeQuery(query);
            if (resource.getDescription().isEmpty()) {
                resource.setDescription(" ");
            }
            if (rs.next()) {
                resourceID = rs.getInt(1);
                resource.setID(resourceID);
                rs.close();
            }
            else {
                rs.close();
                Resource tempRes = new Resource(0, resource.getTYPE(), resource.getTitle(), resource.getAuthor(),
                        resource.getISBN(), resource.getTotalAmount(), resource.getCurrentAmount(),
                        resource.getDescription());
                tempRes.setIsbn13(resource.getISBN13());
                tempRes.setEdition(resource.getEdition());

                String tempQr = insertResourceQuery(tempRes);
                System.out.println(tempQr);
                st.executeQuery(tempQr);

                rs = st.executeQuery(String.format("SELECT * FROM RESOURCES WHERE TITLE='%s'  AND AUTHOR ='%s' AND" +
                                "EDITION = '%s' AND TYPE = '%s' ",
                        tempRes.getTitle(), tempRes.getAuthor(), tempRes.getEdition(), tempRes.getType()));
                if (rs.next()) {
                    resourceID = rs.getInt(1);
                    resource.setID(resourceID);
                }

        }
        } catch (Exception e) {
            System.out.println("Error in returnIDinResource");
            e.printStackTrace();
        }

    }


    public static void setIDinResourceFromArrayList(ArrayList<frontend.data.Resource> resources) {

        try {
            Statement st = conn.createStatement();

            String title;
            String type;
            String author;

            for (int i = 0; i < resources.size(); i++) {
                int resourceID = 0;
                title = resources.get(i).getTitle();
                type = resources.get(i).getTYPE();
                author = resources.get(i).getAuthor();


                String query = String.format("SELECT * FROM RESOURCES WHERE TITLE='%s' AND AUTHOR ='%s' AND " +
                        "EDITION = '%s' AND TYPE = 's' ", title, author,resources.get(i).getEdition(), type);
                ResultSet rs = st.executeQuery(query);

                if (rs.next()) {
                    resourceID = rs.getInt(1);
                    resources.get(i).setID(resourceID);
                    continue;
                }
                if(resources.get(i).getDescription().isEmpty()){
                    resources.get(i).setDescription(" ");
                }
                Resource tempRes = new Resource(0, resources.get(i).getTYPE(), resources.get(i).getTitle(), resources.get(i).getAuthor(),
                        resources.get(i).getISBN(), resources.get(i).getTotalAmount(), resources.get(i).getCurrentAmount(),
                        resources.get(i).getDescription());
                tempRes.setIsbn13(resources.get(i).getISBN13());
                tempRes.setEdition(resources.get(i).getEdition());

                String tempQr = insertResourceQuery(tempRes);
                System.out.println(tempQr);
                st.executeQuery(tempQr);

                rs = st.executeQuery(String.format("SELECT * FROM RESOURCES WHERE TITLE='%s'  AND AUTHOR ='%s'",
                        tempRes.getTitle(),  tempRes.getAuthor()));
                while (rs.next()) {
                    resourceID = rs.getInt(1);
                    resources.get(i).setID(resourceID);

                }
            }

        } catch (Exception e) {
            System.out.println("Error in returnIDinResource");
            e.printStackTrace();
        }
    }

    public static boolean availablePublisher(frontend.data.Publisher p) {
        try {
            Statement st = conn.createStatement();

            String title;
            String info;
            String descrip;
            int pubID = 0;

            title = p.getName();
            info = p.getContacts();
            descrip = p.getDescription();

            String query = String.format("SELECT * FROM PUBLISHERS WHERE TITLE='%s' ", title);
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                pubID = rs.getInt(1);
            }

            if (pubID == 0) {
                //Errorrrrrrrrrr
                if(info.isEmpty()){
                    info = " ";
                }
                if(descrip.isEmpty()){
                    descrip = " ";
                }
                System.out.println("info is " + info + "descrip is " + descrip);
                Publisher tempPub = new Publisher(0, title, info, descrip);
                System.out.println(tempPub.getContactInformation() + "    " + tempPub.getDescription());



                String tempQr = insertPublisherQuery(tempPub);
                System.out.println(tempQr);
                st.executeQuery(tempQr);
                // Errorrrrrrrr


                rs = st.executeQuery(String.format("SELECT * FROM PUBLISHERS WHERE TITLE='%s' AND CONTACT_INFO ='%s' AND DESCRIPTION ='%s'",
                        title, info, descrip));
                while (rs.next()) {
                    pubID = rs.getInt(1);

                }
                p.setID(pubID);
                return false;

            } else {
                p.setID(pubID);
                return true;
            }


        } catch (Exception e) {
            System.out.println("Error in availablePublisher");
            e.printStackTrace();
        }
        return false;
    }

    //==================================================================================================================
    //                                                  Next
    //==================================================================================================================

    //method without user's input
    public static void relationalInsertByID(int courseID, int personID, int resourceID, int publisherID, int semesterID) {


        executeNoReturnQuery(String.format("INSERT INTO RELATION_COURSE_PERSON" +
                " (COURSEID, PERSONID) VALUES ('%d', '%d')", courseID, personID));
        executeNoReturnQuery(String.format("INSERT INTO RELATION_COURSE_RESOURCES" +
                " (COURSEID, RESOURCEID) VALUES ('%d', '%d')", courseID, resourceID));
        executeNoReturnQuery(String.format("INSERT INTO RELATION_SEMESTER_COURSE" +
                " (COURSEID, SEMESTERID) VALUES ('%d', '%d')", courseID, semesterID));
        executeNoReturnQuery(String.format("INSERT INTO RELATION_PERSON_RESOURCES" +
                " (PERSONID, RESOURCEID) VALUES ('%d', '%d')", personID, resourceID));
        executeNoReturnQuery(String.format("INSERT INTO RELATION_PUBLISHER_RESOURCE" +
                " (PUBLISHERID, RESOURCEID) VALUES ('%d', '%d')", publisherID, resourceID));
        System.out.println("Added ID");

    }


    public static ArrayList<Integer> getCourseIdsBySemesterID(int id) {

        int i = 0;
        String query = String.format("SELECT * FROM RELATION_SEMESTER_COURSE WHERE SEMESTERID=%d ORDER BY COURSEID ASC",
                id);
        ArrayList<Integer> idsList = new ArrayList<Integer>();

        try {

            Statement st = DBManager.conn.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                i++;
            }


            rs = st.executeQuery(query);
            i = 0;
            while (rs.next()) {
                idsList.add(rs.getInt(1));
                i++;
            }

            return idsList;


        } catch (SQLException e) {

            System.out.println("Something went wrong");

        }

        return null;
    }


    public static ArrayList<frontend.data.Course> returnEverything(int semesterid) {
        Semester semester = getSemesterNameByID(semesterid);
        int lastCourseID = 0;

        ArrayList<Integer> courseIDs = getCourseIdsBySemesterID(semesterid);

        ArrayList<frontend.data.Course> hugeshit2 = new ArrayList<>();

        for (int i = 0; i < courseIDs.size(); i++) {
            if (lastCourseID == courseIDs.get(i)) {
                continue;
            }
            ArrayList<Course> tmpCourse = DBManager.relationalReadByCourseID(courseIDs.get(i));
            lastCourseID = courseIDs.get(i);

            for (int j = 0; j < tmpCourse.size(); j++) {

                hugeshit2.add(tmpCourse.get(j).initCourseGUI(semester.getYear(), semester.getSeason()));


            }
        }

        return hugeshit2;
    }

    // Returns id needs to return title;
    public static Course[] getCourseTitlesByID(int[] ids) {

        Course[] c;
        try {

            Statement st = DBManager.conn.createStatement();
            c = new Course[ids.length];

            for (int i = 0; i < ids.length; i++) {

                ResultSet rs = st.executeQuery(String.format("SELECT * FROM COURSECT WHERE ID=%d", ids[i]));
                while (rs.next()) {

                    c[i] = new Course(rs.getString(2) + rs.getString(3));
                }

                return c;

            }


        } catch (SQLException e) {

        }
        return null;
    }


    // =======The methods to get the array of objects========================
    public static ArrayList<Person> getPersonFromTable() {
        DBManager DB = new DBManager();
        ArrayList<Person> arr = new ArrayList<>();
        try {


            String query = String.format("SELECT * FROM PERSON ORDER BY LASTNAME ASC");
            ResultSet rs = DB.st.executeQuery(query);

            while (rs.next()) {
                Person p = new Person(rs.getInt(1), rs.getString(3), rs.getString(4), rs.getString(2));
                arr.add(p);
            }
            return arr;
        } catch (Exception e) {
            System.out.println("DATA not found");
        }

        return null;
    }

    public static ArrayList<Course> getCourseFromTable() {
        DBManager DB = new DBManager();
        ArrayList<Course> arr = new ArrayList<>();
        try {


            String query = String.format("SELECT * FROM COURSECT ORDER BY CNUMBER ASC");
            ResultSet rs = DB.st.executeQuery(query);

            while (rs.next()) {
                Course p = new Course(rs.getInt(1), rs.getString(2) + rs.getString(3), rs.getString(4)
                        , rs.getString(5), rs.getString(1));
                arr.add(p);
            }
            return arr;
        } catch (Exception e) {
            System.out.println("DATA not found");
        }

        return null;
    }

    public static ArrayList<frontend.data.Resource> getResourceFromTable() {
        DBManager DB = new DBManager();
        ArrayList<frontend.data.Resource> arr = new ArrayList<>();
        try {

            String query = String.format("SELECT * FROM RESOURCES ORDER BY TITLE ASC");
            ResultSet rs = DB.st.executeQuery(query);

            while (rs.next()) {
                frontend.data.Resource p = new frontend.data.Resource(rs.getInt("ID"),
                        rs.getString("ISBN"), rs.getString("TYPE"),
                        rs.getString("TITLE"), rs.getString("AUTHOR"),
                        rs.getString("DESCRIPTION"),
                        Integer.parseInt(rs.getString("TOTAL_AMOUNT")),
                        Integer.parseInt(rs.getString("CURRENT_AMOUNT")));
                if(rs.getString("ISBN13")!=null)
                    p.setISBN13(rs.getString("ISBN13"));
                if(rs.getString("EDITION")!=null)
                    p.setEdition(rs.getString("EDITION"));
                setPublisherForResource2(p);

                arr.add(p);
            }
            rs.close();
            return arr;
        } catch (Exception e) {
            System.out.println("DATA not found");
        }

        return null;
    }

    public static ArrayList<Publisher> getPublisherFromTable() {
        DBManager DB = new DBManager();
        ArrayList<Publisher> arr = new ArrayList<>();
        try {


            String query = String.format("SELECT * FROM PUBLISHERS ORDER BY TITLE ASC");
            ResultSet rs = DB.st.executeQuery(query);

            while (rs.next()) {
                Publisher p = new Publisher(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4));
                arr.add(p);
            }
            return arr;
        } catch (Exception e) {
            System.out.println("DATA not found");
        }

        return null;
    }
    //======================================================================================

    public static ArrayList<frontend.data.Course> convertArrayCC(ArrayList<com.mbox.Course> c) {
        ArrayList<frontend.data.Course> arr = new ArrayList<>();
        for (int i = 0; i < c.size(); i++) {
            arr.add(c.get(i).initCourseGUI());
        }
        return arr;
    }

    public static ArrayList<frontend.data.Publisher> convertArrayPubPub(ArrayList<com.mbox.Publisher> pub) {
        ArrayList<frontend.data.Publisher> arr = new ArrayList<>();
        for (int i = 0; i < pub.size(); i++) {
            arr.add(pub.get(i).initPublisherGUI());
        }
        return arr;
    }

    public static ArrayList<frontend.data.Course> convertArrayCCBasic(ArrayList<com.mbox.Course> c) {
        ArrayList<frontend.data.Course> arr = new ArrayList<>();
        for (int i = 0; i < c.size(); i++) {
            arr.add(c.get(i).initCourseGUIBasic());
        }
        return arr;
    }


    public static ArrayList<frontend.data.Resource> findResourcesCourseReturnList(int courseID) {

        ResultSet rs;
        int resourceID = 0;
        int i = 0;
        ArrayList<frontend.data.Resource> resourceList = new ArrayList<>();


        try {

            rs = st.executeQuery("SELECT * FROM RELATION_COURSE_RESOURCES WHERE COURSEID = " + courseID);

            while (rs.next()) {
                resourceID = rs.getInt(2);
                rs = st.executeQuery(getResourceInTableQuery(resourceID));
                //System.out.println("lol");

                while (rs.next()) {
                    System.out.println("");

                    // ID, Type, Title, Author, ISBN, total, current, desc
                    frontend.data.Resource resource = new Resource(rs.getInt(1), rs.getString(2),
                            rs.getString(3), rs.getString(4), rs.getString(5),
                            rs.getInt(6), rs.getInt(7), rs.getString(8)).initResourceGUI();
                    resourceList.add(resource);
                    i++;
                }

            }

            return resourceList;
        } catch (SQLException err) {
            System.out.println(err);
        }
        // Adding the list of the resources to the person object
        return null;

    }

    public static ArrayList<frontend.data.Course> searchByNameSemesterCourseList(String fname, String lname, String semester, String year) {
        int semesterID = getSemesterIDByName(semester, year);
        ArrayList<frontend.data.Course> courseSemesterList = returnEverything(semesterID);
        ArrayList<frontend.data.Course> arr = new ArrayList<>();
        ArrayList<Integer> courseIDArr = new ArrayList<>();
        Person person;
        int personID = 0;
        String personType = "";
        lname = lname.substring(0, 1).toUpperCase() + lname.substring(1).toLowerCase();
        fname = fname.substring(0, 1).toUpperCase() + fname.substring(1).toLowerCase();
        int resourceID = 0;
        String cDescription = "", cDepartment = "", cTitle = "";
        Resource[] courseResources = new Resource[20];
        ArrayList<frontend.data.Resource> resourceList = new ArrayList<>();
        int cID = 0;
        try {
            String query = String.format("SELECT * FROM PERSON WHERE FIRSTNAME = '%s' and LASTNAME = '%s' ", fname, lname);
            Statement st = DBManager.conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                personID = rs.getInt(1);
                personType = rs.getString(2);
            }

            person = new Person(personID, fname, lname, personType);
            rs = st.executeQuery("SELECT * FROM RELATION_COURSE_PERSON WHERE PERSONID = " + personID);
            int i = 0;
            while (rs.next()) {
                courseIDArr.add(rs.getInt(1));
                i++;
            }

            person = new Person(personID, fname, lname, personType);
            System.out.println("This is the size of courseIDarr" + courseIDArr.size());
            for (int a = 0; a < courseIDArr.size(); a++) {

                rs = st.executeQuery(getCourseInTableQuery(courseIDArr.get(a)));

                while (rs.next()) {

                    cTitle = rs.getString(2) + rs.getString(3);
                    cID = rs.getInt(1);
                    cDescription = rs.getString(4);
                    cDepartment = rs.getString(5);
                }

                frontend.data.Course tempCourse = new frontend.data.Course(cID, cTitle, cDepartment, cDescription);
                resourceList = findResourcesCourseReturnList(courseIDArr.get(a));

                tempCourse.setResource(resourceList);
                tempCourse.setProfessor(person.initPersonGUI());
                System.out.println(cID + cTitle + cDepartment + cDescription);
                System.out.println(person.initPersonGUI().toString());
                //Restrict search by name with semester, haven’t test yet

                //Need to fix the semester
                for (int c = 0; c < courseSemesterList.size(); c++) {
                    if (tempCourse.getID() == courseSemesterList.get(c).getID()
                            && tempCourse.getProfessor().getID() == courseSemesterList.get(c).getProfessor().getID()
                            ) {
                        tempCourse.setSEMESTER(semester);
                        tempCourse.setYEAR(Integer.parseInt(year));
                        arr.add(tempCourse);
                    }
                }

            }

            return arr;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("wtf");
        }
        return arr;
    }


    public static ArrayList<frontend.data.Course> searchByNameCourseList(String fname, String lname) {
        ArrayList<frontend.data.Course> arr = new ArrayList<>();
        ArrayList<Integer> courseIDArr = new ArrayList<>();
        Person person;
        int personID = 0;
        String personType = "";
        lname = lname.substring(0, 1).toUpperCase() + lname.substring(1).toLowerCase();
        fname = fname.substring(0, 1).toUpperCase() + fname.substring(1).toLowerCase();
        int resourceID = 0;
        String cDescription = "", cDepartment = "", cTitle = "";
        Resource[] courseResources = new Resource[20];
        ArrayList<frontend.data.Resource> resourceList = new ArrayList<>();
        int cID = 0;
        try {
            String query = String.format("SELECT * FROM PERSON WHERE FIRSTNAME = '%s' and LASTNAME = '%s' ", fname, lname);
            Statement st = DBManager.conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                personID = rs.getInt(1);
                personType = rs.getString(2);
            }

            person = new Person(personID, fname, lname, personType);
            rs = st.executeQuery("SELECT * FROM RELATION_COURSE_PERSON WHERE PERSONID = " + personID);
            int i = 0;
            while (rs.next()) {
                courseIDArr.add(rs.getInt(1));
                i++;
            }

            person = new Person(personID, fname, lname, personType);
            System.out.println("This is the size of courseIDarr" + courseIDArr.size());
            for (int a = 0; a < courseIDArr.size(); a++) {

                rs = st.executeQuery(getCourseInTableQuery(courseIDArr.get(a)));

                while (rs.next()) {

                    cTitle = rs.getString(2) + rs.getString(3);
                    cID = rs.getInt(1);
                    cDescription = rs.getString(4);
                    cDepartment = rs.getString(5);
                }

                frontend.data.Course tempCourse = new frontend.data.Course(cID, cTitle, cDepartment, cDescription);
                resourceList = findResourcesCourseReturnList(courseIDArr.get(a));

                tempCourse.setResource(resourceList);
                tempCourse.setProfessor(person.initPersonGUI());
                System.out.println(cID + cTitle + cDepartment + cDescription);
                System.out.println(person.initPersonGUI().toString());
                //Restrict search by name with semester, haven’t test yet
                arr.add(tempCourse);


            }

            return arr;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("wtf");
        }
        return arr;
    }

    public static void delete_relation_course(frontend.data.Course c) {

        try {
            int commonID = c.getCommonID();
            Statement st = conn.createStatement();



                st.executeQuery(String.format("DELETE FROM RELATION_SEMESTER_COURSE WHERE ID = " +
                        "%d", commonID));
                st.executeQuery(String.format("DELETE FROM RELATION_COURSE_PERSON WHERE  COMMONID = " +
                        "%d", commonID));
                st.executeQuery(String.format("DELETE FROM RELATION_COURSE_RESOURCES WHERE COMMONID = " +
                        "%d", commonID));


        } catch (SQLException e) {

            System.out.println("Something went wrong when trying to delete resources");

        }
    }

    public static void delete_course(frontend.data.Course c) {
        executeNoReturnQuery(String.format("DELETE FROM RELATION_SEMESTER_COURSE WHERE COURSEID = %d", c.getID()));
        executeNoReturnQuery(String.format("DELETE FROM RELATION_COURSE_PERSON WHERE COURSEID = %d", c.getID()));
        executeNoReturnQuery(String.format("DELETE FROM RELATION_COURSE_RESOURCES WHERE COURSEID = %d",
                c.getID()));
        executeNoReturnQuery(String.format("DELETE FROM COURSECT WHERE ID = %d", c.getID()));
    }

    //todo (GUIDO):
    // Delete method person
    // put delete method (course in gui)


    public static void delete_person(frontend.data.Person p) {

        ArrayList<Integer> list_of_course_ids = new ArrayList<>();
        ArrayList<Integer> count_of_course_ids = new ArrayList<>();
        ArrayList<Integer> to_be_deleted = new ArrayList<>();
        int counter = 1;

        try {

            // get all the courses professor teaches.
            // delete the exact amount for every different course
            // execute the rest

            Statement st = conn.createStatement();

            ResultSet rs = st.executeQuery(String.format("SELECT * FROM RELATION_COURSE_PERSON WHERE PERSONID = %d " +
                            "ORDER BY COURSEID ASC",
                    p.getID()));

            while (rs.next()) {

                list_of_course_ids.add(rs.getInt(1));

            }

            int previousone = 0;
            int thisone = 0;
            int i = 0;

            while (list_of_course_ids.size() > i) {

                thisone = list_of_course_ids.get(i);

                if (i > 1) {

                    previousone = list_of_course_ids.get(i - 1);
                }

                if (thisone == previousone) {

                    counter++;

                } else {

                    counter = 1;
                }

                count_of_course_ids.add(counter);
                i++;
            }


            Statement statement = conn.createStatement();
            ResultSet result_set;
            String query;
            previousone = 0;
            thisone = 0;
            i = 0;

            while (list_of_course_ids.size() > i) {

                query = String.format("SELECT * FROM RELATION_SEMESTER_COURSE WHERE COURSEID = %d", list_of_course_ids.get(i));
                result_set = statement.executeQuery(query);

                if (i > 1) {

                    previousone = list_of_course_ids.get(i - 1);
                }
                while (result_set.next()) {

                    if (previousone != thisone) {

                        to_be_deleted.add(result_set.getInt(3));
                    }
                }

                i++;

            }

            for (int a = 0; a < list_of_course_ids.size(); a++) {

                System.out.println(list_of_course_ids.get(a));
            }

            for (int b = 0; b < list_of_course_ids.size(); b++) {

                System.out.println(count_of_course_ids.get(b));
            }

            for (int j = 0; j < to_be_deleted.size(); j++) {

                System.out.println(to_be_deleted.get(j));
            }


//            String query = "";
//            for(int i = 0; i < to_be_deleted.size(); i++){
//
//                query = String.format("DELETE FROM RELATION_SEMESTER_COURSE WHERE ID = %d",
//                        to_be_deleted.get(i));
//                System.out.println(query);
//                //executeNoReturnQuery(query);
//            }

//            executeNoReturnQuery(String.format("DELETE FROM RELATION_PERSON_RESOURCES WHERE PERSONID = %d", p.getID()));
//            executeNoReturnQuery(String.format("DELETE FROM RELATION_COURSE_PERSON WHERE PERSONID = %d", p.getID()));
//            executeNoReturnQuery(String.format("DELETE FROM PERSON WHERE ID = %d", p.getID()));

        } catch (SQLException e) {

            System.out.println("Something went wrong with the delete_person function");

        }

    }


    public static void deletePerson(frontend.data.Person person) {

        try {

            Statement st = conn.createStatement();
            Statement st2 = conn.createStatement();
            Statement st3 = conn.createStatement();
            Statement st4 = conn.createStatement();
            Statement st5 = conn.createStatement();
            Statement st6 = conn.createStatement();

            ArrayList<Integer> commonids_to_be_deleted = new ArrayList<>();

            System.out.println("asdfasdfasdf: 1");

            ResultSet rs = st.executeQuery(String.format("SELECT * FROM RELATION_COURSE_PERSON WHERE PERSONID = %d",
                    person.getID()));

            while (rs.next()) {
                commonids_to_be_deleted.add(rs.getInt("COMMONID"));
            }

            System.out.println("asdfasdfasdf: 2");

            for(int i = 0; i < commonids_to_be_deleted.size(); i++){

                st2.executeQuery(String.format("DELETE FROM RELATION_SEMESTER_COURSE WHERE ID = %d",
                        commonids_to_be_deleted.get(i)));

                System.out.println("Deleting from RELATION_SEMESTER_COURSE. COMMONID : " + commonids_to_be_deleted.get(i));
            }

            System.out.println("asdfasdfasdf: 3");

            for(int i = 0; i < commonids_to_be_deleted.size(); i++){

                st3.executeQuery(String.format("DELETE FORM RELATION_COURSE_RESOURCES WHERE COMMONID = %d",
                        commonids_to_be_deleted.get(i)));

                System.out.println("Deleting from RELATION_COURSE_RESOURCES. COMMONID : " + commonids_to_be_deleted.get(i));
            }

            System.out.println("asdfasdfasdf: 4");


            st4.executeQuery(String.format("DELETE FROM RELATION_COURSE_PERSON WHERE PERSONID = %d",
                    person.getID()));

            System.out.println("DELETED FROM RELATION_COURSE_PERSON: " + person.getID());

            st5.executeQuery(String.format("DELETE FROM RELATION_PERSON_RESOURCES WHERE PERSONID = %d",
                    person.getID()));

            System.out.println("DELETED FROM RELATION_PERSON_RESOURCES: " + person.getID());

            st6.executeQuery(String.format("DELETE FROM PERSON WHERE ID = %d", person.getID()));

            System.out.println("DELETED FROM PERSON: " + person.getID());

            System.out.println("asdfasdfasdf: 5");

        } catch (SQLException e) {
            System.out.println("Something went wrong @ deletePerson() @ DBManager.java");
        }
    }


    public static ArrayList<frontend.data.Resource> getResourceList() {
        ArrayList<frontend.data.Resource> resList = getResourceFromTable();

        return resList;
    }

    public static void updateCourseGUI(int courseID, String title, String description, String department) {
        Course c = new Course(courseID, title, description, department, courseID + "");
        executeNoReturnQuery(updateCourseQuery(c));

    }

    public static void updatePersonGUI(int personID, String fname, String lname, String type) {
        Person p = new Person(personID, fname, lname, type);
        executeNoReturnQuery(updatePersonQuery(p));
    }

    public static void updateCourseQuery123(ArrayList<frontend.data.Course> c) {

        if (c.isEmpty()) {

            System.out.println("Course array is empty. Something went wrong.");

        } else if (c.get(0).equals(c.get(1))) {


            //needs to check for non existing professor

            c.get(0).getProfessor().setID(find_person_by_name(c.get(0).getProfessor()));


            try {

                Statement st = conn.createStatement();

                st.executeQuery(String.format("UPDATE RELATION_COURSE_PERSON SET PERSONID = %d WHERE " +
                        "COMMONID = %d", c.get(0).getProfessor().getID(), c.get(0).getCommonID()));

            } catch (SQLException e) {

                System.out.println("Something went wrong when adding a new professor to the RELATION_COURSE_PERSON TABLE");

            }

            System.out.println("Second IF in updateCourseQuery123()");

        } else {

            try {

                Statement st = conn.createStatement();

                c.get(1).setID(find_courseid_by_title(c.get(1)));

                // if it exists
                st.executeQuery(String.format("UPDATE RELATION_SEMESTER_COURSE SET COURSEID = %d WHERE ID = %d",
                        c.get(1).getID(), c.get(0).getCommonID()));


                c.get(1).getProfessor().setID(find_person_by_name(c.get(1).getProfessor()));
                System.out.println(c.get(1).getProfessor().getID());


                st.executeQuery(String.format("UPDATE RELATION_COURSE_PERSON SET COURSEID = %d, PERSONID = %d WHERE " +
                                "COMMONID = %d", c.get(1).getID(), c.get(1).getProfessor().getID(),
                        c.get(0).getCommonID()));

            } catch (SQLException e) {

                System.out.println("Something went wrong when trying to update course and person table");
            }

        }

    }


    public static void updateCourseAndPerson(frontend.data.Course c) {



            c.getProfessor().setID(find_person_by_name(c.getProfessor()));

            try {

                Statement st = conn.createStatement();

                System.out.println("Course title: "+c.getTitle()+"id: "+c.getID() + "person name: "+ c.getProfessor().getFirstName()+" "+c.getProfessor().getLastName()+
                "Person ID: " + c.getProfessor().getID() + "\nResources list: "+c.getResource()+" Commonid: "+c.getCommonID());



                st.executeQuery(String.format("UPDATE RELATION_SEMESTER_COURSE SET COURSEID = %d WHERE ID = %d",
                        c.getID(), c.getCommonID()));


                c.getProfessor().setID(find_person_by_name(c.getProfessor()));
                System.out.println(c.getProfessor().getID());


                st.executeQuery(String.format("UPDATE RELATION_COURSE_PERSON SET COURSEID = %d, PERSONID = %d WHERE " +
                                "COMMONID = %d", c.getID(), c.getProfessor().getID(),
                        c.getCommonID()));

            } catch (SQLException e) {

                System.out.println("Something went wrong when trying to update course and person table");
            }

        }

    public static void updateSemester(frontend.data.Course c) {

        int semesterID = getSemesterIDByName(controller.convertSeasonGUItoDB(c.getSEMESTER()),String.valueOf(c.getYEAR()));
        int commonID = c.getCommonID();
        System.out.println("Semester ID now " + semesterID);

        try {

            Statement st = conn.createStatement();


            st.executeQuery(String.format("UPDATE RELATION_SEMESTER_COURSE SET SEMESTERID = %d WHERE ID = %d",
                    semesterID,commonID));

        } catch (SQLException e) {

            System.out.println("Something went wrong when trying to update course and person table");
        }

    }



    public static int find_courseid_by_title(frontend.data.Course c) {

        String title = c.getTitle();
        String cnumber;

        title = title.replaceAll("\\s", "");
        String[] part = title.split("(?<=\\D)(?=\\d)");

        title = part[0];
        cnumber = part[1];

        int id = -1;

        try {

            Statement st = conn.createStatement();

            ResultSet rs = st.executeQuery(String.format("SELECT * FROM COURSECT WHERE TITLE = '%s' AND CNUMBER = '%s'",
                    title, cnumber));

            while (rs.next()) {

                id = rs.getInt("id");
            }

            if (id == -1) {

                st.executeQuery(String.format("INSERT INTO COURSECT (TITLE, CNUMBER, DESCRIPTION, DEPARTMENT) VALUES " +
                        "('%s', '%s', '%s', '%s')", title, cnumber, c.getDescription(), c.getDepartment()));


                rs = st.executeQuery(String.format("SELECT * FROM COURSECT WHERE TITLE = '%s' AND CNUMBER = '%s'",
                        title, cnumber));

                while (rs.next()) {

                    id = rs.getInt("id");
                }

                return id;

            } else {

                return id;
            }


        } catch (SQLException e) {

            System.out.println("Something went wrong when trying to create / find a course @ find_courseid_by_title()");
        }

        return -999;

    }

    public static int find_person_by_name(frontend.data.Person p) {
        p.setFirstName(p.getFirstName().substring(0,1).toUpperCase()+p.getFirstName().substring(1).toLowerCase());
        p.setLastName(p.getLastName().substring(0,1).toUpperCase()+p.getLastName().substring(1).toLowerCase());
        System.out.println("Find this person " + p.getFirstName());

        int id = -1;

        try {

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(String.format("SELECT * FROM PERSON WHERE FIRSTNAME = '%s' AND LASTNAME = '%s'", p.getFirstName(), p.getLastName()));

            while (rs.next()) {

                id = rs.getInt("ID");
            }

            if (id == -1) {

                st.executeQuery(String.format("INSERT INTO PERSON (FIRSTNAME, LASTNAME, TYPE) VALUES('%s', '%s', '%s')"
                        , p.getFirstName(), p.getLastName(), p.getType()));

                rs = st.executeQuery(String.format("SELECT * FROM PERSON WHERE FIRSTNAME = '%s' AND LASTNAME = " +
                        "'%s'", p.getFirstName(), p.getLastName()));

                while (rs.next()) {

                    id = rs.getInt("ID");
                }

                return id;

            } else {
                return id;
            }

        } catch (SQLException e) {

            System.out.println("Something went wrong when trying to find a person @ find_person_by_name()");
        }

        return -999;

    }


    public static void updatePublisherForResource(frontend.data.Resource r, frontend.data.Publisher p) {
        try {
            st.executeQuery("DELETE FROM RELATION_PUBLISHER_RESOURCE WHERE RESOURCEID =" + r.getID());
            st.executeQuery(String.format("INSERT INTO RELATION_PUBLISHER_RESOURCE (RESOURCEID, PUBLISHERID) VALUES ('%d','%d')", r.getID(), p.getID()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void insertPersonResources(frontend.data.Person person) {

        int commonID = 0;
        setIDinResourceFromArrayList(person.getResources());
        executeNoReturnQuery(String.format("DELETE FROM RELATION_PERSON_RESOURCES WHERE" +
                " PERSONID = '%d' ", person.getID()));

        for (frontend.data.Resource resource : person.getResources()) {
            String tempQuery = String.format("INSERT INTO RELATION_PERSON_RESOURCES" +
                            " (PERSONID, RESOURCEID, COMMONID) VALUES ('%d', '%d','%d')", person.getID(),
                    resource.getID(), commonID);
            executeNoReturnQuery(tempQuery);
        }
        System.out.println("All resources succesfully imported to db for the person!");

    }


    public static String exportCSVCourseResources() {
        ArrayList<Course> allCourses = getCourseFromTable();
        StringBuilder sb = new StringBuilder();
        sb.append("id,");
        sb.append("Title,");
        sb.append("Description,");
        sb.append("Department,");
        sb.append("Resources -->");
        sb.append('\n');

        for (Course course : allCourses) {
            course.setResourceInstances(findResourcesCourseAvoidRepetitive(course.getID()));
            sb.append(course.getID());
            sb.append(",");
            sb.append(course.getTitle());
            sb.append(",");
            sb.append(course.getDescription());
            sb.append(",");
            sb.append(course.getDepartment());
            sb.append(",");
            for (Resource resource : course.getResourceInstances()) {
                sb.append(resource.getTitle());
                sb.append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append("\n");
        }

        //pw.write(sb.toString());
        System.out.println("done!");
        return sb.toString();

    }

    public static String exportCSVPublisherInfo() {

        ArrayList<Publisher> allPublishers = getPublisherFromTable();
//        PrintWriter pw = new PrintWriter(new File("Course_Resources.csv"));
        StringBuilder sb = new StringBuilder();
        sb.append("id,");
        sb.append("Title,");
        sb.append("Contact Info,");
        sb.append("Description");
        sb.append('\n');

        for (Publisher publisher : allPublishers) {
            sb.append(publisher.getID());
            sb.append(",");
            sb.append(publisher.getTitle());
            sb.append(",");
            sb.append(publisher.getContactInformation());
            sb.append(",");
            sb.append(publisher.getDescription());
            sb.append("\n");

        }

        return sb.toString();
    }


    public static String exportCSVResourcePublisher() {
        ArrayList<frontend.data.Resource> allResources = getResourceFromTable();

//        PrintWriter pw = new PrintWriter(new File("Course_Resources.csv"));
        StringBuilder sb = new StringBuilder();
        sb.append("id,");
        sb.append("Type,");
        sb.append("Title,");
        sb.append("Author,");
        sb.append("ISBN,");
        sb.append("Total amount,");
        sb.append("Current amount,");
        sb.append("Description,");
        sb.append("Publisher Title,");
        sb.append("Publisher contact info");
        sb.append('\n');

        for (frontend.data.Resource resource : allResources) {
            sb.append(resource.getID());
            sb.append(",");
            sb.append(resource.getTYPE());
            sb.append(",");
            sb.append(resource.getTitle());
            sb.append(",");
            sb.append(resource.getAuthor());
            sb.append(",");
            sb.append(resource.getISBN());
            sb.append(",");
            sb.append(resource.getTotalAmount());
            sb.append(",");
            sb.append(resource.getCurrentAmount());
            sb.append(",");
            sb.append(resource.getDescription());
            sb.append(",");
            sb.append(resource.getPublisher().getName());
            sb.append(",");
            sb.append(resource.getPublisher().getContacts());
            sb.append("\n");
        }
        return sb.toString();

    }

    public static String exportCSVPersonResources() {
        ArrayList<Person> allPerson = getPersonFromTable();

//        PrintWriter pw = new PrintWriter(new File("Course_Resources.csv"));
        StringBuilder sb = new StringBuilder();
        sb.append("id,");
        sb.append("First Name,");
        sb.append("Last Name,");
        sb.append("Type,");
        sb.append("Resources-->");
        sb.append('\n');

        for (Person person : allPerson) {
            setResourcesForPerson(person);
            sb.append(person.getID() + ",");
            sb.append(person.getFirstName() + ",");
            sb.append(person.getLastName() + ",");
            sb.append(person.getType() + ",");
            for (Resource resource : person.getResourceList()) {
                sb.append(resource.getTitle() + ",");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append('\n');

        }
        return sb.toString();
    }

    public static ArrayList<frontend.data.Resource> getAllResourcesNeededForPerson(frontend.data.Person person) {

        //GO to person-course, get a list of commonids of the courses being teached by the person
        ResultSet rss, rs, rs3;
        int commonID = 0, before=0;
        ArrayList<frontend.data.Resource> resourcesList = new ArrayList<frontend.data.Resource>();
        ArrayList<Integer> listOfIDs= new ArrayList<Integer>();
        Resource tempResource;

        try {
            Statement st = conn.createStatement();
            Statement st2 = conn.createStatement();
            Statement st3 = conn.createStatement();

            rss = st.executeQuery(String.format("SELECT * FROM RELATION_COURSE_PERSON WHERE PERSONID = '%d'",
                    person.getID()));
            while (rss.next()) {
                commonID = (rss.getInt("COMMONID"));
                rs = st2.executeQuery(String.format("SELECT * FROM RELATION_COURSE_RESOURCES WHERE COMMONID = '%d' ORDER BY RESOURCEID ASC",
                        commonID));

                while (rs.next()) {

                    if (before != rs.getInt("RESOURCEID") &&
                            !listOfIDs.contains(new Integer(rs.getInt("RESOURCEID")))) {

                        before = rs.getInt("RESOURCEID");
                        listOfIDs.add(before);
                        rs3 = st3.executeQuery(getResourceInTableQuery(rs.getInt("RESOURCEID")));

                        while (rs3.next()) {
                            // ID, Type, Title, Author, ISBN, total, current, desc
                            tempResource = new Resource(rs3.getInt(1), rs3.getString(2),
                                    rs3.getString(3), rs3.getString(4), rs3.getString(5),
                                    rs3.getInt(6), rs3.getInt(7), rs3.getString(8));
                            tempResource.setCommonID(commonID);
                            setPublisherForResource(tempResource);
                            resourcesList.add(tempResource.initResourceGUI());
                        }
                    }

                }
            }

            return resourcesList;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return resourcesList;
    }


    public static ArrayList<frontend.data.Resource> findDifferene(frontend.data.Person person, ArrayList<frontend.data.Resource> personRequiredResources){

        ArrayList<frontend.data.Resource> differences = new ArrayList<frontend.data.Resource>();


        for (frontend.data.Resource resource : personRequiredResources){
            if (!person.getResources().contains(resource)){
                differences.add(resource);
            }
        }

        return differences;

    }

    public static ArrayList<frontend.data.Course> return_everything_by_commonid(ArrayList<Integer> idlist){

        ArrayList<frontend.data.Course> courses = new ArrayList<>();

            for(int i : idlist){

                courses.add(find_class_by_commonid(idlist.get(i)));
            }

            return courses;

    }

    public static ArrayList<frontend.data.Course> find_classes_by_course_name(String name){

        ArrayList<frontend.data.Course> courses = new ArrayList<>();
        ArrayList<Integer> courseids = new ArrayList<>();
        ArrayList<Integer> classids = new ArrayList<>();
        ArrayList<Integer> tmp;


        try{

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM COURSECT WHERE TITLE LIKE '%"+name+"%' OR CNUMBER LIKE '%"+name+"%'");

            while(rs.next()){

                courseids.add(rs.getInt("ID"));
            }


            for(int i = 0; i < courseids.size(); i++){

                tmp = find_classids_by_courseid(courseids.get(i));
                for(int j = 0; j < tmp.size(); j++){

                    classids.add(tmp.get(j));
                }

            }



            for(int i = 0; i < classids.size(); i++){

                courses.add(find_class_by_commonid(classids.get(i)));
            }

            return courses;

        }catch(SQLException e){

            System.out.println("Something went wrong with find_classes_by_course_name(String name)");
        }

        return courses;

    }

    public static ArrayList<Integer> find_classids_by_resource_name(String name){

        ArrayList<Integer> resourceids = new ArrayList<>();
        ArrayList<Integer> classids = new ArrayList<>();

        try{

            Statement statement = conn.createStatement();
            Statement statement2 = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM RESOURCES WHERE TITLE LIKE '%"+name+"%' OR AUTHOR LIKE '%"+name+"%'");


            while(rs.next()) {

                resourceids.add(rs.getInt("ID"));
            }

            ResultSet rs2;

            for(int i = 0; i < resourceids.size(); i++){

                rs2 = statement2.executeQuery(String.format("SELECT * FROM RELATION_COURSE_RESOURCES WHERE RESOURCEID = %d", resourceids.get(i)));

                while(rs2.next()){

                    classids.add(rs2.getInt("COMMONID"));
                }
            }


            return classids;

        }catch(SQLException e) { System.out.println("Something went wrong with find_classids_by_resource_name()"); }

        return classids;
    }

    public static ArrayList<Integer> find_classids_by_course_name(String name){

        ArrayList<Integer> courseids = new ArrayList<>();
        ArrayList<Integer> classids = new ArrayList<>();
        ArrayList<Integer> tmp;


        try{

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM COURSECT WHERE TITLE LIKE '%"+name+"%' OR CNUMBER LIKE '%"+name+"%'");

            while(rs.next()){

                courseids.add(rs.getInt("ID"));
            }


            for(int i = 0; i < courseids.size(); i++){

                tmp = find_classids_by_courseid(courseids.get(i));
                for(int j = 0; j < tmp.size(); j++){

                    classids.add(tmp.get(j));
                }

            }

            return classids;

        }catch(SQLException e){

            System.out.println("Something went wrong with find_classes_by_course_name(String name)");
        }

        return classids;


    }

    public static ArrayList<Integer> find_classids_by_courseid(int id){

        ArrayList<Integer> classids = new ArrayList<>();

        try{

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(String.format("SELECT * FROM RELATION_SEMESTER_COURSE WHERE COURSEID = %d", id));

            while(rs.next()){

                classids.add(rs.getInt("ID"));
            }

            return classids;

        }catch(SQLException e){

            System.out.println("Something went wrong with find_classid_by_courseid(int id)");
        }

        return classids;
    }

    public static ArrayList<frontend.data.Course> find_classes_by_professor_name(String name){

        ArrayList<Integer> courseids = new ArrayList<>();
        ArrayList<Integer> personids = new ArrayList<>();
        ArrayList<frontend.data.Course> courses = new ArrayList<>();

        try{

            Statement st = conn.createStatement();
            Statement st2 = conn.createStatement();
            Statement st3 = conn.createStatement();
            ResultSet rs3;

            ResultSet rs = st.executeQuery("SELECT * FROM PERSON WHERE FIRSTNAME LIKE '%"+name+"%'");

            while(rs.next()){

                personids.add(rs.getInt("ID"));
            }

            ResultSet rs2 = st.executeQuery("SELECT * FROM PERSON WHERE LASTNAME LIKE '%"+name+"%'");

            while(rs2.next()){

                personids.add(rs2.getInt("ID"));
            }


            for(int i = 0; i < personids.size(); i++){

                 rs3 = st3.executeQuery(String.format("SELECT * FROM RELATION_COURSE_PERSON WHERE PERSONID = %d", personids.get(i)));

                while(rs3.next()){

                    courseids.add(rs3.getInt("COMMONID"));
                }

            }

            for(int i = 0; i < courseids.size(); i++){

                courses.add(find_class_by_commonid(courseids.get(i)));
            }

            return courses;


        }catch(SQLException e){

            System.out.println("Something went wrong with find_course_by_professor_name(String name)");
        }

        return courses;


    }

    public static ArrayList<Integer> find_classids_by_professor_name(String name){

        ArrayList<Integer> courseids = new ArrayList<>();
        ArrayList<Integer> personids = new ArrayList<>();

        try{

            Statement st = conn.createStatement();
            Statement st2 = conn.createStatement();
            ResultSet rs2;

            ResultSet rs = st.executeQuery("SELECT * FROM PERSON WHERE FIRSTNAME LIKE '%"+name+"%' OR LASTNAME LIKE '%"+name+"%'");

            while(rs.next()){

                personids.add(rs.getInt("ID"));
            }


            for(int i = 0; i < personids.size(); i++){

                rs2 = st2.executeQuery(String.format("SELECT * FROM RELATION_COURSE_PERSON WHERE PERSONID = %d", personids.get(i)));

                while(rs2.next()){

                    courseids.add(rs2.getInt("COMMONID"));
                }

            }

            return courseids;


        }catch(SQLException e){

            System.out.println("Something went wrong with find_course_by_professor_name(String name)");
        }

        return courseids;
    }

    public static frontend.data.Course find_class_by_commonid(int id){

        frontend.data.Person person = find_person_by_commonid(id);
        frontend.data.Course course = new frontend.data.Course();
        ArrayList<frontend.data.Resource> resources = find_resources_by_commonid(id);


        int courseid = -1;

        try{

            Statement st = conn.createStatement();
            Statement st2 = conn.createStatement();

            ResultSet rs = st.executeQuery(String.format("SELECT * FROM RELATION_SEMESTER_COURSE WHERE ID = %d",
                    id));

            while (rs.next()) {

                courseid = rs.getInt("COURSEID");

            }


            ResultSet rs2 = st.executeQuery(String.format("SELECT * FROM COURSECT WHERE ID = %d", courseid));
            course.setCommonID(id);

            while(rs2.next()){

                course.setID(rs2.getInt("ID"));
                course.setTitle(rs2.getString("TITLE") + rs2.getString("CNUMBER"));
                course.setDescription(rs2.getString("DESCRIPTION"));
                course.setDepartment(rs2.getString("DEPARTMENT"));
                course.setSEMESTER("FALL");
                course.setYEAR(2018);
            }

            course.setResource(resources);
            course.setProfessor(person);

            return course;

        }catch(SQLException e){

            System.out.println("Something went wrong with find_class_by_commonid()");
        }

        return course;
    }

    public static ArrayList<frontend.data.Resource> find_resources_by_name(String name) {

        ArrayList<frontend.data.Resource> resource = new ArrayList<>();
        ArrayList<Integer> resourceids = new ArrayList<>();
        frontend.data.Resource tmpresource;

        try{

            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM RESOURCES WHERE TITLE LIKE '%"+name+"%' OR AUTHOR LIKE '%"+name+"%'");

            while(rs.next()) {

                resourceids.add(rs.getInt("ID"));
            }

            for(int i : resourceids){

                resource.add(find_resource_by_id(i));
            }

            return resource;

        }catch(SQLException e) { System.out.println("Something went wrong with find_resources_by_name()"); }

        return resource;
    }

    public static ArrayList<frontend.data.Resource> find_resources_by_commonid(int id){

        ArrayList<Integer> resids = new ArrayList<>();
        ArrayList<frontend.data.Resource> reslist = new ArrayList<>();

        try{

            Statement st = conn.createStatement();

            ResultSet rs = st.executeQuery(String.format("SELECT * FROM RELATION_COURSE_RESOURCES WHERE COMMONID=%d",
                    id));

            while(rs.next()){

                resids.add(rs.getInt("RESOURCEID"));

            }

            for(int i = 0; i < resids.size(); i++){

                reslist.add(find_resource_by_id(resids.get(i)));

            }

            return reslist;


        }catch(SQLException e){

            System.out.println("Something went wrong with find_resources_by_commonid(int id)");

        }

        return reslist;

    }

    public static frontend.data.Person find_person_by_commonid(int id){

        frontend.data.Person person = new frontend.data.Person("","",frontend.data.PersonType.valueOf("CourseInstructor").toString());

        int idtmp = -1;

        try{

            Statement st = conn.createStatement();

            ResultSet rs = st.executeQuery(String.format("SELECT * FROM RELATION_COURSE_PERSON WHERE COMMONID = %d", id));

            while(rs.next()){

                idtmp = rs.getInt("PERSONID");
            }

            person = find_person_by_id(idtmp);

            return person;

        }catch(SQLException e){

            System.out.println("Something went wrong with find_person_by_commonid(int id)");
        }

        return person;
    }

    public static frontend.data.Person find_person_by_id(int id){

        frontend.data.Person person = new frontend.data.Person("","",frontend.data.PersonType.valueOf("CourseInstructor").toString());

        try{

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(String.format("SELECT * FROM PERSON WHERE ID = %d", id));

            while(rs.next()){

                person.setID(id);
                person.setType(rs.getString("TYPE"));
                person.setFirstName(rs.getString("FIRSTNAME"));
                person.setLastName(rs.getString("LASTNAME"));
            }

            return person;

        }catch(SQLException e){

            System.out.println("Something went wrong with find_resources_by_commonid(int id)");
        }

        return person;
    }

    public static frontend.data.Resource find_resource_by_id(int id){

        frontend.data.Resource resource = new frontend.data.Resource("", -1);
        frontend.data.Publisher publisher = new frontend.data.Publisher("","","");
        int publisherid = -1;

        try{

            Statement st = conn.createStatement();
            Statement st2 = conn.createStatement();

            ResultSet rs = st.executeQuery(String.format("SELECT * FROM RELATION_PUBLISHER_RESOURCE WHERE RESOURCEID = %d", id));
            ResultSet rs2 = st2.executeQuery(String.format("SELECT * FROM RESOURCES WHERE ID = %d", id));


            while(rs.next()){

                publisherid = rs.getInt("PUBLISHERID");

            }

            if (publisherid == -1){

                //Assign empty publisher

            }else{

                publisher = find_publisher_by_id(publisherid);

            }

            while (rs2.next()) {

                resource.setID(rs2.getInt("ID"));
                resource.setTYPE(rs2.getString("TYPE"));
                resource.setTitle(rs2.getString("TITLE"));
                resource.setAuthor(rs2.getString("AUTHOR"));
                resource.setISBN(rs2.getString("ISBN"));
                resource.setTotalAmount(rs2.getInt("TOTAL_AMOUNT"));
                resource.setCurrentAmount(rs2.getInt("CURRENT_AMOUNT"));
                resource.setDescription(rs2.getString("DESCRIPTION"));
                resource.setPublisher(publisher);

            }

            return resource;

        }catch(SQLException e){

            System.out.println("Something went wrong when trying to execute find_resource_by_id(int id)");

        }

        return resource;
    }

    public static frontend.data.Publisher find_publisher_by_id(int id){

        frontend.data.Publisher publisher = new frontend.data.Publisher(-1, "", "", "");

        try{

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(String.format("SELECT * FROM PUBLISHERS WHERE ID = %d", id));

            while(rs.next()){

                publisher.setID(rs.getInt("ID"));
                publisher.setName(rs.getString("TITLE"));
                publisher.setDescription(rs.getString("DESCRIPTION"));
                publisher.setContacts(rs.getString("CONTACT_INFO"));
            }


            if(publisher.getID() == -1){

            }else{

                return publisher;
            }


        }catch(SQLException e){

            System.out.println("Something went wrong when trying to findPublisherByID(int id)");

        }

        return publisher;

    }

    public static void print_semester_by_commonid(int id){

        try{

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(String.format("SELECT * FROM RELATION_SEMESTER_COURSE WHERE ID = %d", id));

            while(rs.next()){

                System.out.println("|" + rs.getInt("SEMESTERID") + "|");
            }

        }catch(SQLException e){

            System.out.println("asd");
        }
    }

    public static void muhcode(){

        //        ArrayList<Integer> asdf = DBManager.find_classids_by_course_name("140");
//
//        int[] varname = new int[asdf.size()];
//
//        for(int i = 0; i < asdf.size(); i++){
//
//            varname[i] = asdf.get(i);
//        }
//
//        Arrays.sort(varname);
//
//        for(int i = 0; i < asdf.size(); i++){
//
//            System.out.println(asdf.get(i));
//        }
//
//        System.out.println("===========");
//
//        for(int i = 0; i < varname.length; i++){
//
//            System.out.println(varname[i]);
//        }
//
//        int[] asdf = {1,2,3,3,4};
//        Set<Integer> asd = new HashSet<>();
//
//        for(int i = 0; i < asdf.length; i++){
//
//            asd.add(asdf[i]);
//        }
//
//        for(int i = 0; i < asdf.length; i++){
//
//            System.out.println(asdf[i]);
//        }
//
//        System.out.println("=============");
//
//        asd.toArray(asdf);
//
//        for(int i = 0; i < asdf.length; i++){
//
//            System.out.println(asdf[i]);
//        }

    }
    public static String capitalizeString(String s){
        String result = "";
        String[] tempString = s.split("\\s");
        for(String a : tempString){
            result = result + a.substring(0,1).toUpperCase() + a.substring(1) + " ";
        }
        return result;
    }

    public static boolean isISBN(String s){
        String regex ="\\d+";
        if(s.matches(regex) && s.length() == 10){
            return true;
        }
        else {return false;}
    }

    public static boolean isISBN13(String s){
        String regex ="\\d+";
        if(s.matches(regex) && s.length() == 13){
            return true;
        }
        else {return false;}
    }

    public static void deleteResourceInDB(frontend.data.Resource r){
        try {
            //delete in resource table
            st.executeQuery("DELETE FROM RESOURCES WHERE ID = " + r.getID());
            //delete course resource relation
            st.executeQuery("DELETE FROM RELATION_COURSE_RESOURCES WHERE RESOURCEID = " + r.getID());
            //delete person resource relation
            st.executeQuery("DELETE FROM RELATION_PERSON_RESOURCES WHERE RESOURCEID = " + r.getID());
            //delete publisher resource relation
            st.executeQuery("DELETE FROM RELATION_PUBLISHER_RESOURCE WHERE RESOURCEID = " +r.getID());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void deletePublisherInDB(frontend.data.Publisher p){
        try{
            //delete in publisher table
            st.executeQuery("DELETE FROM PUBLISHERS WHERE ID = " +p.getID());
            //delete publisher resource relation
            st.executeQuery("DELETE FROM RELATION_PUBLISHER_RESOURCE WHERE PUBLISHERID = " +p.getID());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void delete_person_by_id(int id){

        ArrayList<Integer> commonids = new ArrayList<>();

        try{

            Statement state1 = conn.createStatement(); // get the commonids of that professor id
            Statement state2 = conn.createStatement(); // delete those commonids from semester_course (listed as ID)
            Statement state3 = conn.createStatement(); // delete commonids from relation_course_resources
            Statement state4 = conn.createStatement(); // delete relation_course_person by personid
            Statement state5 = conn.createStatement(); // delete relation_person_resources by personid
            Statement state6 = conn.createStatement(); // delete person from PERSON table

            ResultSet rs = state1.executeQuery(String.format("SELECT * FROM RELATION_COURSE_PERSON WHERE PERSONID = %d", id));

            while (rs.next()) {

                commonids.add(rs.getInt("COMMONID"));

            }

            //deleting

            for(int i = 0; i < commonids.size(); i++) {
                state2.executeQuery(String.format("DELETE FROM RELATION_SEMESTER_COURSE WHERE ID = %d", commonids.get(i)));
                state3.executeQuery(String.format("DELETE FROM RELATION_COURSE_RESOURCES WHERE COMMONID = %d", commonids.get(i)));
            }

            state4.executeQuery(String.format("DELETE FROM RELATION_COURSE_PERSON WHERE PERSONID = %d", id));
            state5.executeQuery(String.format("DELETE FROM RELATION_PERSON_RESOURCES WHERE PERSONID = %d", id));
            state6.executeQuery(String.format("DELETE FROM PERSON WHERE ID = %d", id));


        }catch(SQLException e){

            System.out.println("Exception in the method delete_person_by_id()");

        }


    }


}



