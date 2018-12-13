package org.han.ica.asd.c.dbconnection;

public class DBConnectionFactory {

	private DBConnectionFactory(){
		throw new IllegalStateException("Helper class");
	}

	public static DatabaseConnection getInstance(String type){
		if (type == "test")
			return DBConnectionTest.getInstance();

		else{
			return DBConnection.getInstance();
		}
	}
}
