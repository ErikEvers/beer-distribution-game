package org.han.ica.asd.c.dbconnection;

import javax.inject.Singleton;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class DBConnectionTest implements IDatabaseConnection {
	private static final String CONNECTIONSTRING = "jdbc:sqlite:src/test/resources/";
	private static final String DATABASENAME = "BeerGameDBTest.db";
	public static final Logger LOGGER = Logger.getLogger(DBConnectionTest.class.getName());
	private static volatile DBConnectionTest mInstance;

	public DBConnectionTest() {
	}

	public static DBConnectionTest getInstance() {
		if (mInstance == null) {
			synchronized (DBConnectionTest.class) {
				if (mInstance == null) {
					mInstance = new DBConnectionTest();
				}
			}
		}
		return mInstance;
	}


	public void createNewDatabase() {
		runSQLScript("ddl.sql");
	}

	public void runSQLScript(String scriptname)
	{
		StringBuilder sb = new StringBuilder();

		DBConnection.findFileAndRun(scriptname, sb, connect(), LOGGER);

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
		File file = new File("src" + File.separator + "test" + File.separator + "resources" + File.separator + DATABASENAME);
		file.delete();
	}

}
