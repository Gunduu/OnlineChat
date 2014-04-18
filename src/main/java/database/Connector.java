package database;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

import org.apache.wicket.markup.html.WebPage;
import register.Person;
import java.util.logging.*;
public class Connector extends WebPage {
	Logger logger = Logger.getLogger(Connector.class.getName());
	Connection connect = null;
	public boolean connect()
	{
		try
		{
			Class.forName("org.hsqldb.jdbcDriver");
			connect = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost:9001/mytestdb", "sa", "");
			logger.log(Level.INFO,"initialize driver");
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			logger.log(Level.INFO,"Cant initialize driver");
			return false;
		}
	}

	public void disConnect() 
	{
		try {
			connect.close();
			logger.log(Level.INFO,"Disconnect");
		} catch (SQLException e) {
			logger.log(Level.INFO,"Cant disconnect");
			e.printStackTrace();
		}
	}
	//use for SQL command SELECT
	public synchronized ResultSet query(String expression)
	{
		Statement st = null;
		ResultSet rs = null;
		
		try {
			st = connect.createStatement();
			rs = st.executeQuery(expression);
			logger.log(Level.INFO,"Query completed");
			st.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.log(Level.INFO,"Query not completed");
		}
		return rs;
	}
	//use for SQL commands CREATE, DROP, INSERT and UPDATE
	public synchronized void update(String expression, Person person, int ID)
	{
		PreparedStatement st = null;
		try {
			st = connect.prepareStatement(expression);
			st.setInt(1, ID);
			st.setString(2, person.getUserName());
			st.setString(3, person.getEmail());
			st.setString(4, person.getPassword());
			st.executeUpdate();

			logger.log(Level.INFO,"Update completed");
			//st.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.log(Level.INFO,"Update not completed");
		}
	}

	public synchronized void updateNick(String expression, String nickname, int ID)
	{
		PreparedStatement st = null;
		try {
			st = connect.prepareStatement(expression);
			st.setInt(1, ID);
			st.setString(2, nickname);
			st.executeUpdate();

			logger.log(Level.INFO,"Update completed");
			//st.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.log(Level.INFO,"Update not completed");
		}
	}	
}
