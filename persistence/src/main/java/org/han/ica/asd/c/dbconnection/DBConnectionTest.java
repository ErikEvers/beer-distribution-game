package org.han.ica.asd.c.dbconnection;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBConnectionTest implements DatabaseConnection {
	private static final String CONNECTIONSTRING = "jdbc:sqlite:src/test/resources/";
	private static final String DATABASENAME = "BeerGameDBTest.db";
	public static final Logger LOGGER = Logger.getLogger(DBConnectionTest.class.getName());
	private static volatile DBConnectionTest mInstance;

	private DBConnectionTest() {
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

		String url = CONNECTIONSTRING + DATABASENAME;

		try (Connection conn = DriverManager.getConnection(url)) {
			if (conn != null) {
				DatabaseMetaData meta = conn.getMetaData();
				runSQLScript("ddl.sql");
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}

	public void runSQLScript(String scriptname)
	{
		String s = new String();
		StringBuffer sb = new StringBuffer();

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
		}
	}

	public void cleanup() {
	runSQLScript("cleanup.sql");
	}

}
