package org.han.asd.c.bootstrap.Intergration;

import org.han.ica.asd.c.dbconnection.DBConnection;
import org.han.ica.asd.c.dbconnection.IDatabaseConnection;

import javax.inject.Singleton;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class DBConnectionIntegrationTest implements IDatabaseConnection {
    private static final Path currentDir = Paths.get("");
    private static final String CONNECTIONSTRING = "jdbc:sqlite:"+currentDir.toAbsolutePath().toString()+File.separator+"src"+File.separator+"test"+File.separator+"resources"+File.separator;
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
            DBConnection.findFileAndRun(scriptname, sb, connect(), LOGGER);
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

}
