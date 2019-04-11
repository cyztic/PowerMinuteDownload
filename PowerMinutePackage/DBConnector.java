package PowerMinutePackage;
import java.io.IOException;
import java.sql.*;
import java.io.FileWriter;

/**Interfaces with the PowerMinute database
 *
 */
public class DBConnector {

    //*********************************MEMBER VARIABLES******************************************
    //static db object
    private static DBConnector db_connector = null;

    //private member variables for the database connection and querys
    private Statement stmt;
    private Connection conn;

    //variables to store db log in info
    private String URL;
    private String USERNAME;
    private String PASSWORD;

    //integers to store user and partner id
    private int USER_ID;
    private int PARTNER_ID;

    //*********************************CLASS METHODS******************************************

    // INPUT:    none
    // TASK:     constructor for DBConnector class
    // OUTPUT:   instance of a DBConnector
    public DBConnector() {
        stmt = null;
        conn = null;
        URL = null;
        USERNAME = null;
        PASSWORD = null;
    }

    // INPUT:    string with database url, string with username,
    //           string with database password.
    // TASK:     sets the member variabls to the correct db
    //           information.
    // OUTPUT:   none
    public void setDBInfo(String DBUrl, String DBUsername, String DBPassword){
        URL = DBUrl;
        USERNAME = DBUsername;
        PASSWORD = DBPassword;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Done");
        } catch (Exception e) {
            System.out.print("Error " + e);
        }
    }

    // INPUT:    none
    // TASK:     method to insure there is only ever
    //           1 single instance created of this class.
    //           This makes sure the class is a singleton.
    // OUTPUT:   DBConnector object
    public static DBConnector getInstance(){
        if(null == db_connector)
            db_connector = new DBConnector();

        return db_connector;
    }

    // INPUT:    integer storing partners id that the user
    //           wants to add.
    // TASK:     inserts a user id to the current users partner
    //           id in the database.
    // OUTPUT:   none
    public void addAccountabilityPartner(int partnerID) {
        try {
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            stmt = conn.createStatement();
            PARTNER_ID = partnerID;
            String query = "UPDATE User_T" +
                    " SET partnerID = " + partnerID +
                    " WHERE userID = " + USER_ID + ";";
            int result = stmt.executeUpdate(query);
            conn.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    // INPUT:    integer holding a 1 or 0
    // TASK:     either adds a completed or declined
    //           power minute to suers table. This is
    //           used to track the users stats.
    // OUTPUT:   none
    public void addUserExercise(int completed) {
        try {
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            stmt = conn.createStatement();
            String query = "INSERT INTO Exercise_T VALUES (" +
                    USER_ID + ", NOW(), " + completed + ");";
            int result = stmt.executeUpdate(query);
            conn.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    // INPUT:    none
    // TASK:     get users partner id from database
    //           and store it in member variable.
    // OUTPUT:   partnersID
    public int getPartnerID() {
        try {
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            stmt = conn.createStatement();
            String query = "SELECT partnerID FROM User_T WHERE userID = " + USER_ID;
            ResultSet result = stmt.executeQuery(query);
            result.next();
            PARTNER_ID = result.getInt(1);
            conn.close();
            return PARTNER_ID;
        } catch (SQLException se) {
            se.printStackTrace();
            return -1;
        }
    }

    // INPUT:    none
    // TASK:     get amount of power minutes the user has
    //           completed in the last 24 hours.
    // OUTPUT:   integer holding users daily accepted power minutes
    public int getUserDailyAccepted() {
        try {
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            stmt = conn.createStatement();
            String query = "SELECT COUNT(*) FROM Exercise_T" +
                    " WHERE userID = " + USER_ID +
                    " AND time >= (DATE(NOW() - INTERVAL 1 DAY) + INTERVAL 0 SECOND)" +
                    " AND completed = 1;";
            ResultSet result = stmt.executeQuery(query);
            result.next();
            int numAccepted = result.getInt(1);
            conn.close();
            return numAccepted;
        } catch (SQLException se) {
            se.printStackTrace();
            return -1;
        }
    }

    // INPUT:    none
    // TASK:     get amount of power minutes the user has
    //           completed ever.
    // OUTPUT:   integer holding users total accepted power minutes
    public int getUserTotalAccepted() {
        try {
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            stmt = conn.createStatement();
            String query = "SELECT COUNT(*) FROM Exercise_T" +
                    " WHERE userID = " + USER_ID +
                    " AND completed = 1;";
            ResultSet result = stmt.executeQuery(query);
            result.next();
            int numAccepted = result.getInt(1);
            conn.close();
            return numAccepted;
        } catch (SQLException se) {
            se.printStackTrace();
            return -1;
        }
    }

    // INPUT:    none
    // TASK:     get amount of power minutes the user has
    //           completed in the last week.
    // OUTPUT:   integer holding users weekly accepted power minutes
    public int getUserWeeklyAccepted() {
        try {
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            stmt = conn.createStatement();
            String query = "SELECT COUNT(*) FROM Exercise_T" +
                    " WHERE userID = " + USER_ID +
                    " AND time >= (DATE(NOW() - INTERVAL 1 WEEK) + INTERVAL 0 SECOND)" +
                    " AND completed = 1;";
            ResultSet result = stmt.executeQuery(query);
            result.next();
            int numAccepted = result.getInt(1);
            conn.close();
            return numAccepted;
        } catch (SQLException se) {
            se.printStackTrace();
            return -1;
        }
    }

    // INPUT:    none
    // TASK:     get amount of power minutes the user has
    //           completed in the last month.
    // OUTPUT:   integer holding users monthly accepted power minutes
    public int getUserMonthlyAccepted() {
        try {
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            stmt = conn.createStatement();
            String query = "SELECT COUNT(*) FROM Exercise_T" +
                    " WHERE userID = " + USER_ID +
                    " AND time >= (DATE(NOW() - INTERVAL 1 MONTH) + INTERVAL 0 SECOND)" +
                    " AND completed = 1;";
            ResultSet result = stmt.executeQuery(query);
            result.next();
            int numAccepted = result.getInt(1);
            conn.close();
            return numAccepted;
        } catch (SQLException se) {
            se.printStackTrace();
            return -1;
        }
    }

    // INPUT:    none
    // TASK:     get amount of power minutes the user has
    //           declined in the last 24 hours.
    // OUTPUT:   integer holding users daily declined power minutes
    public int getUserDailyDeclined() {
        try {
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            stmt = conn.createStatement();
            String query = "SELECT COUNT(*) FROM Exercise_T" +
                    " WHERE userID = " + USER_ID +
                    " AND time >= (DATE(NOW() - INTERVAL 1 DAY) + INTERVAL 0 SECOND)" +
                    " AND completed = 0;";
            ResultSet result = stmt.executeQuery(query);
            result.next();
            int numDeclined = result.getInt(1);
            conn.close();
            return numDeclined;
        } catch (SQLException se) {
            se.printStackTrace();
            return -1;
        }
    }

    // INPUT:    none
    // TASK:     get amount of power minutes the user has
    //           declined in the last week.
    // OUTPUT:   integer holding users weekly declined power minutes
    public int getUserWeeklyDeclined() {
        try {
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            stmt = conn.createStatement();
            String query = "SELECT COUNT(*) FROM Exercise_T" +
                    " WHERE userID = " + USER_ID +
                    " AND time >= (DATE(NOW() - INTERVAL 1 WEEK) + INTERVAL 0 SECOND)" +
                    " AND completed = 0;";
            ResultSet result = stmt.executeQuery(query);
            result.next();
            int numDeclined = result.getInt(1);
            conn.close();
            return numDeclined;
        } catch (SQLException se) {
            se.printStackTrace();
            return -1;
        }
    }

    // INPUT:    none
    // TASK:     get amount of power minutes the user has
    //           declined in the last month.
    // OUTPUT:   integer holding users monthly declined power minutes
    public int getUserMonthlyDeclined() {
        try {
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            stmt = conn.createStatement();
            String query = "SELECT COUNT(*) FROM Exercise_T" +
                    " WHERE userID = " + USER_ID +
                    " AND time >= (DATE(NOW() - INTERVAL 1 MONTH) + INTERVAL 0 SECOND)" +
                    " AND completed = 0;";
            ResultSet result = stmt.executeQuery(query);
            result.next();
            int numDeclined = result.getInt(1);
            conn.close();
            return numDeclined;
        } catch (SQLException se) {
            se.printStackTrace();
            return -1;
        }
    }

    // INPUT:    none
    // TASK:     get amount of power minutes the user's
    //           partner has completed in the last 24 hours.
    // OUTPUT:   integer holding partners daily accepted power minutes
    public int getPartnerDailyAccepted() {
        try {
            int partnerID = getPartnerID();
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            stmt = conn.createStatement();
            String query = "SELECT COUNT(*) FROM Exercise_T" +
                    " WHERE userID = " + partnerID +
                    " AND time >= (DATE(NOW() - INTERVAL 1 DAY) + INTERVAL 0 SECOND)" +
                    " AND completed = 1;";
            ResultSet result = stmt.executeQuery(query);
            result.next();
            int numAccepted = result.getInt(1);
            conn.close();
            return numAccepted;
        } catch (SQLException se) {
            se.printStackTrace();
            return -1;
        }
    }

    // INPUT:    none
    // TASK:     get amount of power minutes the user's
    //           partner has completed in the last week.
    // OUTPUT:   integer holding partners weekly accepted power minutes
    public int getPartnerWeeklyAccepted() {
        try {
            int partnerID = getPartnerID();
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            stmt = conn.createStatement();
            String query = "SELECT COUNT(*) FROM Exercise_T" +
                    " WHERE userID = " + partnerID +
                    " AND time >= (DATE(NOW() - INTERVAL 1 WEEK) + INTERVAL 0 SECOND)" +
                    " AND completed = 1;";
            ResultSet result = stmt.executeQuery(query);
            result.next();
            int numAccepted = result.getInt(1);
            conn.close();
            return numAccepted;
        } catch (SQLException se) {
            se.printStackTrace();
            return -1;
        }
    }

    // INPUT:    none
    // TASK:     get amount of power minutes the user's
    //           partner has completed in the last month.
    // OUTPUT:   integer holding partners monthly accepted power minutes
    public int getPartnerMonthlyAccepted() {
        try {
            int partnerID = getPartnerID();
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            stmt = conn.createStatement();
            String query = "SELECT COUNT(*) FROM Exercise_T" +
                    " WHERE userID = " + partnerID +
                    " AND time >= (DATE(NOW() - INTERVAL 1 MONTH) + INTERVAL 0 SECOND)" +
                    " AND completed = 1;";
            ResultSet result = stmt.executeQuery(query);
            result.next();
            int numAccepted = result.getInt(1);
            conn.close();
            return numAccepted;
        } catch (SQLException se) {
            se.printStackTrace();
            return -1;
        }
    }

    // INPUT:    none
    // TASK:     get amount of power minutes the user's
    //           partner has declined in the last 24 hours.
    // OUTPUT:   integer holding partners daily declined power minutes
    public int getPartnerDailyDeclined() {
        try {
            int partnerID = getPartnerID();
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            stmt = conn.createStatement();
            String query = "SELECT COUNT(*) FROM Exercise_T" +
                    " WHERE userID = " + partnerID +
                    " AND time >= (DATE(NOW() - INTERVAL 1 DAY) + INTERVAL 0 SECOND)" +
                    " AND completed = 0;";
            ResultSet result = stmt.executeQuery(query);
            result.next();
            int numDeclined = result.getInt(1);
            conn.close();
            return numDeclined;
        } catch (SQLException se) {
            se.printStackTrace();
            return -1;
        }
    }

    // INPUT:    none
    // TASK:     get amount of power minutes the user's
    //           partner has declined in the last week.
    // OUTPUT:   integer holding partners weekly declined power minutes
    public int getPartnerWeeklyDeclined() {
        try {
            int partnerID = getPartnerID();
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            stmt = conn.createStatement();
            String query = "SELECT COUNT(*) FROM Exercise_T" +
                    " WHERE userID = " + partnerID +
                    " AND time >= (DATE(NOW() - INTERVAL 1 WEEK) + INTERVAL 0 SECOND)" +
                    " AND completed = 0;";
            ResultSet result = stmt.executeQuery(query);
            result.next();
            int numDeclined = result.getInt(1);
            conn.close();
            return numDeclined;
        } catch (SQLException se) {
            se.printStackTrace();
            return -1;
        }
    }

    // INPUT:    none
    // TASK:     get amount of power minutes the user's
    //           partner has declined in the last month.
    // OUTPUT:   integer holding partners monthly declined power minutes
    public int getPartnerMonthlyDeclined() {
        try {
            int partnerID = getPartnerID();
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            stmt = conn.createStatement();
            String query = "SELECT COUNT(*) FROM Exercise_T" +
                    " WHERE userID = " + partnerID +
                    " AND time >= (DATE(NOW() - INTERVAL 1 MONTH) + INTERVAL 0 SECOND)" +
                    " AND completed = 0;";
            ResultSet result = stmt.executeQuery(query);
            result.next();
            int numDeclined = result.getInt(1);
            conn.close();
            return numDeclined;
        } catch (SQLException se) {
            se.printStackTrace();
            return -1;
        }
    }

    // INPUT:    string with email, string with pass,
    //           stirng with first name,string with
    //           last name
    // TASK:     Insert a new user's information into
    //           the login and user tables.
    // OUTPUT:   boolean determining whether the sign up
    //           was successful or not.
    public boolean userSignUp(String email, String pass, String fname, String lname)
    {
        // Insert the new user into Login_T.
        if (insertLoginT(email, pass))
        {
            // Get the new user's ID.
            int ID = getUserID(email);
            // Insert the new user into User_T.
            insertUserT(ID, fname, lname);

            // Return true if the new user was inserted into both tables.
            return true;
        }
        else
            return false;
    }

    // INPUT:    string with email, string with pass
    // TASK:     Check the user's login information.
    // OUTPUT:   int determining whether the sign in
    //           info matched a users or not. -1 == false
    public int userSignIn(String email, String pass)
    {
        // Check if valid email and password are valid, return the userID.
        if (checkEmail(email) && checkPW(email, pass)) {
            //set user ID for future use
            USER_ID = getUserID(email);
            //set partner ID for future use
            PARTNER_ID = getPartnerID();
            return getUserID(email);
        }
        // Otherwise return -1.
        return -1;
    }

    // INPUT:    string with email, string with pass
    // TASK:     Insert the new user's information into the login table.
    // OUTPUT:   boolean true if it insertion worked, false
    //           if insertion failed.
    public boolean insertLoginT(String email, String pass)
    {
        try
        {
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            // Check if the email is already set up.
            if (checkEmail(email))
                return false;

            stmt = conn.createStatement();
            String myQuery = "INSERT INTO Login_T VALUES (null, " + "\"" + email +"\""
                    + ", " + "\"" + pass + "\")";
            System.out.println("Query: " + myQuery);

            stmt.execute(myQuery);
            System.out.println("Done.");

            return true;
        }
        // Handle SQL errors.
        catch (SQLException se)
        {
            se.printStackTrace();
        }
        // Handle any other errors.
        catch (Exception e)
        {
            System.out.print("Error " + e);
        }

        return false;
    }

    // INPUT:    int with id, string with first name,
    //           string with last name
    // TASK:     Insert the new user's information into the user table.
    // OUTPUT:   boolean true if it insertion worked, false
    //           if insertion failed.
    public void insertUserT(int ID, String fname, String lname)
    {
        try
        {
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            stmt = conn.createStatement();
            String myQuery = "INSERT INTO User_T VALUES ("+ ID + ", \"" + fname +"\""
                    + ", " + "\"" + lname + "\"" + ", null, 0)";
            System.out.println("Query: " + myQuery);
            stmt.execute(myQuery);
            System.out.println("Done.");
        }
        // Handle SQL errors.
        catch (SQLException se)
        {
            se.printStackTrace();
        }
        // Handle any other errors.
        catch (Exception e)
        {
            System.out.print("Error " + e);
        }
    }

    // INPUT:    none
    // TASK:     get the member variable storing user id
    //           this is for getting id outside of class.
    // OUTPUT:   integer storing user id
    public int getUSER_ID(){
        return USER_ID;
    }

    // INPUT:    none
    // TASK:     get the user id from the database
    //           table.
    // OUTPUT:   integer storing user id, -1 if none
    public int getUserID(String email)
    {
        int ID = -1;

        try
        {
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);


            stmt = conn.createStatement();
            String myQuery = "SELECT userID FROM Login_T WHERE email = \"" + email + "\"";

            stmt.execute(myQuery);

            ResultSet rs = stmt.executeQuery(myQuery);

            while(rs.next())
            {
                ID = rs.getInt("userID");
            }

        }
        // Handle SQL errors.
        catch (SQLException se)
        {
            se.printStackTrace();
        }
        // Handle any other errors.
        catch (Exception e)
        {
            System.out.print("Error " + e);
        }

        // Return the user's ID.
        return ID;
    }

    // INPUT:    none
    // TASK:     get the user first name
    // OUTPUT:   string storing first name
    public String getUsersFirstName()
    {
        String name = "";

        try
        {
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);


            stmt = conn.createStatement();
            String myQuery = "SELECT firstName FROM User_T WHERE userID = " + USER_ID;

            stmt.execute(myQuery);

            ResultSet rs = stmt.executeQuery(myQuery);

            while(rs.next())
            {
                name = rs.getString("firstName");
            }

        }
        // Handle SQL errors.
        catch (SQLException se)
        {
            se.printStackTrace();
        }
        // Handle any other errors.
        catch (Exception e)
        {
            System.out.print("Error " + e);
        }

        // Return the user's ID.
        return name;
    }

    // INPUT:    none
    // TASK:     get the user first name
    // OUTPUT:   string storing first name
    public String getUsersLastName()
    {
        String name = "";

        try
        {
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);


            stmt = conn.createStatement();
            String myQuery = "SELECT lastName FROM User_T WHERE userID = " + USER_ID;

            stmt.execute(myQuery);

            ResultSet rs = stmt.executeQuery(myQuery);

            while(rs.next())
            {
                name = rs.getString("lastName");
            }

        }
        // Handle SQL errors.
        catch (SQLException se)
        {
            se.printStackTrace();
        }
        // Handle any other errors.
        catch (Exception e)
        {
            System.out.print("Error " + e);
        }

        // Return the user's ID.
        return name;
    }
    // INPUT:    string storing email
    // TASK:     Check if the email that the user provides
    //           is the same as the one that is stored.
    // OUTPUT:   boolean if email is correct
    public boolean checkEmail(String email)
    {
        try
        {
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);


            stmt = conn.createStatement();
            String myQuery = "SELECT COUNT(*) AS duplicates FROM Login_T WHERE email = \"" + email + "\"";

            ResultSet rs = stmt.executeQuery(myQuery);

            int dup = 0;
            while(rs.next())
            {
                dup = rs.getInt("duplicates");
            }

            if (dup  > 0)
                return true;
        }
        // Handle SQL errors.
        catch (SQLException se)
        {
            se.printStackTrace();
        }
        // Handle any other errors.
        catch (Exception e)
        {
            System.out.print("Error " + e);
        }
        return false;
    }

    // INPUT:    string storing email
    // TASK:     Check if the password that the user provides
    //           is the same as the one that is stored.
    // OUTPUT:   boolean if pw is correct
    public boolean checkPW(String email, String pass)
    {
        try
        {
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);


            stmt = conn.createStatement();
            String myQuery = "SELECT password FROM Login_T WHERE email = \"" + email + "\"";
            ResultSet rs = stmt.executeQuery(myQuery);

            String hashedPass = "";
            while(rs.next())
            {
                hashedPass = rs.getString("password");
            }
            // Return if the password matched the stored password.
            return BCrypt.checkpw(pass, hashedPass);
        }
        // Handle SQL errors.
        catch (SQLException se)
        {
            se.printStackTrace();
        }
        // Handle any other errors.
        catch (Exception e)
        {
            System.out.print("Error " + e);
        }
        return false;
    }

    // INPUT:    integer storing userID
    // TASK:     Check if the user is an admin account
    // OUTPUT:   boolean if user is an admin account
    public boolean isAdmin()
    {
        try
        {
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);


            stmt = conn.createStatement();
            String myQuery = "SELECT adminAccount FROM User_T WHERE userID = " + USER_ID;
            ResultSet rs = stmt.executeQuery(myQuery);

            while (rs.next())
            {
                boolean admin = rs.getBoolean(1);
                return admin;
            }
        }
        catch (SQLException se)
        {
            se.printStackTrace();
        }
        catch (Exception e)
        {
            System.out.print("Error " + e);
        }
        return false;
    }

	public void generateReportToFile(String filename) {
        try {
            FileWriter fw = new FileWriter(filename);
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            stmt = conn.createStatement();
            String query = "SELECT email, ((COUNT(*) DIV 5)*1000) FROM Login_T, Exercise_T" +
                    " WHERE Login_T.userID = Exercise_T.userID " +
                    " AND time >= (DATE(NOW() - INTERVAL 1 WEEK) + INTERVAL 0 SECOND)" +
                    " GROUP BY Exercise_T.userID;";
            ResultSet result = stmt.executeQuery(query);
            fw.append("Email  Wellbucks");
            fw.append(System.getProperty("line.separator"));

            while (result.next()) {
                fw.append(result.getString(1));
                fw.append("  ");
                fw.append(Integer.toString(result.getInt(2)));
                fw.append("  ");
                fw.append(System.getProperty("line.separator"));
            }
            fw.close();
            conn.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (IOException ie) {
            System.out.print(ie);
        }
    }

}