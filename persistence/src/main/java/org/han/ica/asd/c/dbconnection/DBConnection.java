package org.han.ica.asd.c.dbconnection;

import javax.inject.Singleton;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class DBConnection implements IDatabaseConnection {
	private static final String CONNECTIONSTRING = "jdbc:sqlite:src/main/resources/";
	private static final String DATABASENAME = "BeerGameDB.db";
	private static final Logger LOGGER = Logger.getLogger(org.han.ica.asd.c.dbconnection.DBConnection.class.getName());
	private static volatile DBConnection mInstance;

	public DBConnection() {
		//Empty constructor for GUICE
	}


	public void createNewDatabase() {

		String url = CONNECTIONSTRING + DATABASENAME;

		try (Connection conn = DriverManager.getConnection(url)) {
			if (conn != null) {
				runSQLScript("ddl.sql");
			}

		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
		}

	}

	public void runSQLScript(String scriptname) {
		StringBuilder sb = new StringBuilder();
		findFileAndRun(scriptname, sb, connect(), LOGGER);

	}

	static void findFileAndRun(String scriptname, StringBuilder sb, Connection connect2, Logger logger) {
		String s;
		try(FileReader fr = new FileReader(new File(Thread.currentThread().getContextClassLoader().getResource(scriptname).toURI()))){
			BufferedReader br = new BufferedReader(fr);

			while ((s = br.readLine()) != null) {
				sb.append(s);
			}
			br.close();

			// here is our splitter ! We use ";" as a delimiter for each request
			// then we are sure to have well formed statements
			String[] inst = sb.toString().split(";");

			Connection connect = connect2;
			try (Statement st = connect.createStatement()) {

				for (int i = 0; i < inst.length; i++) {
					// we ensure that there is no spaces before or after the request string
					// in order to not execute empty statements
					String strings = inst[i];
					if (!strings.equals("")) {
						st.executeUpdate(strings); //NOSONAR because the SQL Scripts are written by ourselves. SQLInjection not applicable
					}
				}
			}

		} catch (Exception e) {
			logger.log(Level.SEVERE, e.toString(), e);
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
			conn.rollback();
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
		}
	}

	public void cleanup() {
	throw new UnsupportedOperationException();
	}

}


