package org.han.ica.asd.c.dbconnection;

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
public class DBConnection implements IDatabaseConnection {
	private static final Path currentDir = Paths.get("");
	private static final String DATABASENAME = "BeerGameDB.db";
	private static final String PATH = currentDir.toAbsolutePath().toString()+File.separator+"persistence"+ File.separator+"src"+ File.separator+"main"+File.separator+"resources"+File.separator;
	private static final String CONNECTIONSTRING = "jdbc:sqlite:" + PATH;
	private static final Logger LOGGER = Logger.getLogger(org.han.ica.asd.c.dbconnection.DBConnection.class.getName());

	public DBConnection() {
		createNewDatabase();
	}

	public void createNewDatabase() {
		File file = new File(PATH + DATABASENAME);
		if(!file.exists()) {
			runSQLScript("ddl.sql");
			runSQLScript("dml.sql");
		}
	}

	public void runSQLScript(String scriptname) {
		StringBuilder sb = new StringBuilder();
		findFileAndRun(scriptname, sb, connect(), LOGGER);
	}

	public static void findFileAndRun(String scriptname, StringBuilder sb, Connection connect2, Logger logger){
		String s;
		try(FileReader fr = new FileReader(new File(Thread.currentThread().getContextClassLoader().getResource(scriptname).toURI()))){
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
		for (String strings : inst) {
			if (!"".equals(strings)) {
				st.executeUpdate(strings); //NOSONAR because the SQL Scripts are written by ourselves. SQLInjection not applicable
			}
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
		} finally {
			try {
				conn.close();
			} catch (Exception e) {
				//
			}
		}
	}

	public void cleanup() {
	throw new UnsupportedOperationException();
	}

}


