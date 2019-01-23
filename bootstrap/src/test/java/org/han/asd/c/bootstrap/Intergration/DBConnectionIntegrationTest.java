package org.han.asd.c.bootstrap.Intergration;

import org.han.ica.asd.c.dbconnection.IDatabaseConnection;

import javax.inject.Singleton;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class DBConnectionIntegrationTest implements IDatabaseConnection {
    private static final Path currentDir = Paths.get("");
    private static final String PATH = currentDir.toAbsolutePath().toString() + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator;
    private static final String CONNECTIONSTRING = "jdbc:sqlite:"+PATH;
    private static final String DATABASENAME = "BeerGameDBTest.db";
    private static final Logger LOGGER = Logger.getLogger(DBConnectionIntegrationTest.class.getName());

    private static volatile DBConnectionIntegrationTest mInstance;

    public DBConnectionIntegrationTest() {
        createNewDatabase();
    }

    public static DBConnectionIntegrationTest getInstance() {
        if (mInstance == null) {
            synchronized (DBConnectionIntegrationTest.class) {
                if (mInstance == null) {
                    mInstance = new DBConnectionIntegrationTest();
                }
            }
        }
        return mInstance;
    }


    public void createNewDatabase() {
        File file = new File(CONNECTIONSTRING+DATABASENAME);
        if(!file.exists()) {
            runSQLScript("cleanTestDatabase.sql");
            runSQLScript("ddl.sql");
        }
    }

    public void runSQLScript(String scriptname)
    {
        StringBuilder sb = new StringBuilder();

        try {
            findFileAndRun(scriptname, sb, connect(), LOGGER);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE,e.toString(),e);
        }

    }

    public Connection connect() {
        // SQLite connection string
        String url = CONNECTIONSTRING + DATABASENAME;

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
        return conn;
    }

    public void rollBackTransaction(Connection conn) {
        try {
            conn.setAutoCommit(false);
            conn.rollback();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        } finally {
            try {
                conn.close();
            } catch (Exception e) {
                //
            }
        }
    }

    public void cleanup() {
        runSQLScript("cleanTestDatabase.sql");
    }

    public static void findFileAndRun(String scriptname, StringBuilder sb, Connection connect2, Logger logger){
        String s;
        try(FileReader fr = new FileReader(new File(PATH+scriptname))){
            BufferedReader br = new BufferedReader(fr);

            while ((s = br.readLine()) != null) {
                sb.append(s);
            }
            br.close();

            String[] inst = sb.toString().split(";");

            try (Statement st = connect2.createStatement()) {
                executeSQLLine(inst, st);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString(), e);
        }
        finally {
            try {
                connect2.close();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE,e.toString(),e);
            }
        }
    }

    private static void executeSQLLine(String[] inst, Statement st) throws SQLException {
        for (int i = 0; i < inst.length; i++) {
            String strings = inst[i];
            if (!"".equals(strings)) {
                st.executeUpdate(strings); //NOSONAR because the SQL Scripts are written by ourselves. SQLInjection not applicable
            }
        }
    }

}
