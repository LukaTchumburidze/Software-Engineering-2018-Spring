package assign3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.*;

/**
 * Database handling class for Metropolis.
 * This class is the main engine for the program.
 * It connects with the database and changes table
 * object's cells according to data passed by MetropolisFrame
 *
 */

public class MetropolisDB {

    /*
        MTable object to communicate with the table
     */
    private MTable table;
    //Of course communication object
    private Connection con;

    //Just some constants
    private static final String METROPOLIS_PATH = "metropolises.sql";
    private static final String TABLE_NAME = "metropolises";

    /**
     * Saves argument in Instance object, setups new connection and
     * executes provided sql script if there was no appropriate table found
      * @param tableArg
     */

    MetropolisDB (MTable tableArg) {
        table = tableArg;
        setUpConnection ();
        connectToDatabase ();
    }


    /**
     * Just establishes new connection with
     * database
     */
    private void setUpConnection () {
        con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection(MyDBInfo.MYSQL_DATABASE_SERVER, MyDBInfo.MYSQL_USERNAME, MyDBInfo.MYSQL_PASSWORD);
        }catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Couldn't setup connection");
        }
    }

    /**
     * Tries to connect with database if there is not
     * this database it will create one
     */

    private void connectToDatabase () {
        try {
            con.setCatalog(MyDBInfo.MYSQL_DATABASE_NAME);
        } catch (SQLException e) {
            System.out.println ("There was no Database named " + MyDBInfo.MYSQL_DATABASE_NAME + " creating from scratch!");
            createDatabase ();
        }
    }

    /**
     * Reads sql file which is located in this path
     * @param path
     * @return
     */

    private static String[] readSQLFile (String path) {
        StringBuilder fileData = new StringBuilder();
        File sqlFile = new File (path);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(sqlFile));
            String line = reader.readLine();
            while (line != null) {
                fileData.append(line);
                fileData.append("\n");
                line = reader.readLine();
            }
        } catch (Exception e) {
            System.out.println (e.getMessage());
            System.out.println("Couldn't read mysql");
        }

        return fileData.toString().split(";");
    }

    /**
     * Just creates database with provided MyDBInfo.MYSQL_DATABASE_NAME
     */

    private void createDatabase () {
        try {
            Statement statement = con.createStatement();
            statement.executeUpdate ("CREATE DATABASE " + MyDBInfo.MYSQL_DATABASE_NAME);
            String[] fileContents = readSQLFile(METROPOLIS_PATH);
            createTable (statement, fileContents);
        } catch (SQLException e) {
            System.out.println (e.getMessage());
        }
    }

    /**
     * Creates new table by running the script provided
     * in file found at METROPOLIS_PATH
     * @param statement
     * @param batches
     * @throws SQLException
     */

    private void createTable (Statement statement, String[]batches) throws SQLException {
        for (int i = 0; i < batches.length-1; i ++) {
            statement.addBatch(batches[i] + ";");
        }
        statement.executeBatch();
    }

    /**
     * This function is called when Add button is pressed
     * in MetropolisFrame.java class, this function just gets
     * data found in the text fields as arguments and
     * executes insert query on database. Also updates MTable's
     * contents accordingly.
     * @param mVal
     * @param cVal
     * @param pVal
     */

    public void addNewEntry (String mVal, String cVal, String pVal) {
        try {
            Statement statement = con.createStatement();
            String sqlQuery = "INSERT INTO " + TABLE_NAME + "(Metropolis, Continent, Population)\n VALUES (\"" +
                    mVal + "\", \"" + cVal + "\", " + Integer.parseInt(pVal) + ");";
            statement.executeUpdate(sqlQuery);
            //If population is not number following line will throw exception
            table.afterAdd (mVal, cVal, "" + +Integer.parseInt(pVal));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("There happened an error while adding new entries");
        }
    }

    /**
     * Builds select query according to passed arguments
     * @param mVal
     * @param cVal
     * @param pVal
     * @param pBoxVal
     * @param mBoxVal
     * @return
     */
    public String selectBuilder (String mVal, String cVal, String pVal, String pBoxVal, String mBoxVal) {
        String queryStart = "SELECT * FROM " + TABLE_NAME + "\n WHERE ";
        return queryStart + metropolisWhere (mVal, mBoxVal) + " AND " + continentWhere (cVal, mBoxVal) + " AND " + populationWhere (pVal, pBoxVal) + ";";


    }

    /**
     * Following function builds conditions for metropolis part in
     * "where" line
     * @param mVal
     * @param mBoxVal
     * @return
     */

    private String metropolisWhere (String mVal, String mBoxVal) {
        if (mVal.length() == 0) {
            return "0<1";
        }

        if (mBoxVal.equals("Exact Match")) {
            return "Metropolis like '" + mVal + "'";
        } else {
            return "Metropolis like '%" + mVal + "%'";
        }
    }

    /**
     * Following function builds conditions for continent part in
     * "where" line
     * @param cVal
     * @param mBoxVal
     * @return
     */

    private String continentWhere (String cVal, String mBoxVal) {
        if (cVal.length() == 0) {
            return "0<1";
        }

        if (mBoxVal.equals("Exact Match")) {
            return "Continent like '" + cVal + "'";
        } else {
            return "Continent like '%" + cVal + "%'";
        }
    }

    /**
     * Following function builds conditions for population part in
     * "where" line
     * @param pVal
     * @param pBoxVal
     * @return
     */

    private String populationWhere (String pVal, String pBoxVal) {
        if (pVal.length() == 0) {
            return "0<1";
        }

        int p = Integer.parseInt(pVal);

        if (pBoxVal.equals("Population Larger Than")) {
            return "Population >" + p;
        } else {
            return "Population <=" + p;
        }
    }

    /**
     * Just executes query built by selectBuilder function
     * and passes ResultSet to MTable object's function
     * to update table's inner contents-
     * @param query
     */

    public void executeQuery (String query) {
        try {
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query);
            table.passNewData(rs);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("There happened an error while searching in a base");
        }
    }
}
