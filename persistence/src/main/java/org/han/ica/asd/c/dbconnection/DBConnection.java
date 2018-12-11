package org.han.ica.asd.c.dbconnection;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	private static final String CONNECTIONSTRING = "jdbc:sqlite::resource:";
	private static final String DATABASENAME = "BeerGameDB";

	public static Connection connect() {
		// SQLite connection string
		String url = CONNECTIONSTRING + DATABASENAME;

		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return conn;
	}

	public static void RollBackTransaction(Connection conn) {
		try {
			conn.rollback();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
}