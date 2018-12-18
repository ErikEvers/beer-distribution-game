package org.han.ica.asd.c.dbconnection;

import java.sql.Connection;

public interface IDatabaseConnection {

	void createNewDatabase();
	void runSQLScript(String scriptname);
	Connection connect();
	void rollBackTransaction(Connection conn);
}

