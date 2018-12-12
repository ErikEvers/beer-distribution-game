package org.han.ica.asd.c.dbconnection;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBConnection {
	private static final String CONNECTIONSTRING = "jdbc:sqlite::resource:";
	private static final String DATABASENAME = "BeerGameDB";
	public static final Logger LOGGER = Logger.getLogger(DBConnection.class.getName());

	private DBConnection(){
		throw new IllegalStateException("Utility class");
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