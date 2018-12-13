package org.han.ica.asd.c.dbconnection;


import com.douglasjose.tech.SQLFile;

import java.io.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBConnectionTest {
	private static final String CONNECTIONSTRING = "jdbc:sqlite:src/test/resources/";
	private static final String DATABASENAME = "BeerGameDBTest.db";
	public static final Logger LOGGER = Logger.getLogger(DBConnectionTest.class.getName());

	private DBConnectionTest() {
		throw new IllegalStateException("Utility class");
	}

	public static void createNewDatabase() {

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

	public static void runSQLScript(String scriptname)
	{
		String s = new String();
		StringBuffer sb = new StringBuffer();

		try
		{

			FileReader fr = new FileReader(new File(Thread.currentThread().getContextClassLoader().getResource(scriptname).toURI()));


			BufferedReader br = new BufferedReader(fr);

			while((s = br.readLine()) != null)
			{
				sb.append(s);
			}
			br.close();

			// here is our splitter ! We use ";" as a delimiter for each request
			// then we are sure to have well formed statements
			String[] inst = sb.toString().split(";");

			Connection connect = connect();
			Statement st = connect.createStatement();

			for(int i = 0; i<inst.length; i++)
			{
				// we ensure that there is no spaces before or after the request string
				// in order to not execute empty statements
				if(!inst[i].trim().equals(""))
				{
					st.executeUpdate(inst[i]);
				}
			}

		}
		catch(Exception e)
		{
			LOGGER.log(Level.SEVERE,e.toString(),e);
		}

	}

	public static Connection connect() {
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

	public static void rollBackTransaction(Connection conn) {
		try {
			conn.rollback();
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
		}
	}

	public static void cleanup() {
	runSQLScript("cleanup.sql");
	}

}
