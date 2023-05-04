package net.schedge.cards.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.mysql.cj.jdbc.MysqlDataSource;

import net.schedge.cards.Logger;

public abstract class AccountDatabase {
	
	private MysqlDataSource dataSource;
	private Connection connection;
	
	public AccountDatabase(String url, String user, String pass) {
		dataSource = new MysqlDataSource();
		dataSource.setUrl(url);
		dataSource.setUser(user);
		dataSource.setPassword(pass);
		reset();
	}
	
	public PreparedStatement createStatement(String statement) {
		try {
			return this.connection.prepareStatement(statement);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public abstract void initStatements();
	
	public void reset() {
		try {
			if(connection != null) {
				connection.close();
			}
			connection = dataSource.getConnection();
			if(connection.isValid(0)) {
				Logger.info("Database [" + dataSource.getUrl() + "] connected");
			} else {
				Logger.fatal("Database [" + dataSource.getUrl() + "] could not connect");
				System.exit(-1);
			}
			initStatements();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
