package org.han.ica.asd.c.dbconnection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBConnection implements DatabaseConnection {
	private static final String CONNECTIONSTRING = "jdbc:sqlite:src/main/resources/";
	private static final String DATABASENAME = "BeerGameDB.db";
	public static final Logger LOGGER = Logger.getLogger(org.han.ica.asd.c.dbconnection.DBConnection.class.getName());
	private static volatile DBConnection mInstance;

	private DBConnection() {
	}

	public static DBConnection getInstance() {
		if (mInstance == null) {
			synchronized (DBConnection.class) {
				if (mInstance == null) {
					mInstance = new DBConnection();
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

	public void runSQLScript(String scriptname) {
		String s = new String();
		StringBuffer sb = new StringBuffer();
		findFileAndRun(scriptname, sb, connect(), LOGGER);

	}

	static void findFileAndRun(String scriptname, StringBuffer sb, Connection connect2, Logger logger) {
		String s;
		try {

			FileReader fr = new FileReader(new File(Thread.currentThread().getContextClassLoader().getResource(scriptname).toURI()));


			BufferedReader br = new BufferedReader(fr);

			while ((s = br.readLine()) != null) {
				sb.append(s);
			}
			br.close();

			// here is our splitter ! We use ";" as a delimiter for each request
			// then we are sure to have well formed statements
			String[] inst = sb.toString().split(";");

			Connection connect = connect2;
			Statement st = connect.createStatement();

			for (int i = 0; i < inst.length; i++) {
				// we ensure that there is no spaces before or after the request string
				// in order to not execute empty statements
				if (!inst[i].trim().equals("")) {
					st.executeUpdate(inst[i]);
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

	}

}


