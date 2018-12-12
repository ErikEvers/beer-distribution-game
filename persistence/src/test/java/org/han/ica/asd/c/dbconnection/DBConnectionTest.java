package org.han.ica.asd.c.dbconnection;


import com.douglasjose.tech.SQLFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBConnectionTest {
	private static final String CONNECTIONSTRING = "jdbc:sqlite:src/test/resources/";
	private static final String DATABASENAME = "BeerGameDBTest.db";
	public static final Logger LOGGER = Logger.getLogger(DBConnectionTest.class.getName());

	private DBConnectionTest(){
		throw new IllegalStateException("Utility class");
	}

	public static void createNewDatabase() {

		String url = CONNECTIONSTRING+DATABASENAME;

		try (Connection conn = DriverManager.getConnection(url)) {
			if (conn != null) {
				DatabaseMetaData meta = conn.getMetaData();
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public static void insertDatabase(){
		DBConnectionTest.createNewDatabase();
		Connection conn = DBConnectionTest.connect();
		PreparedStatement pstm;


		FileInputStream ddlPathName = null;
		try {
			ddlPathName = new FileInputStream("src/test/resources/ddl.sql");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		SQLFile sqlFile = new SQLFile(ddlPathName);
		String query = sqlFile.query("createDatabase");
		try {
			conn.setAutoCommit(false);
			pstm = conn.prepareStatement(query);
			pstm.execute();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static Connection connect() {
		// SQLite connection string
		String url = CONNECTIONSTRING + DATABASENAME;

		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url);
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, e.toString(),e);
		}
		return conn;
	}

	public static void rollBackTransaction(Connection conn) {
		try {
			conn.rollback();
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, e.toString(),e);
		}
	}
}