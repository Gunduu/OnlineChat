package users;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.wicket.markup.html.WebPage;

import database.Connector;

public class UserCount{
	private int usercount;
	Logger logger = Logger.getLogger(Connector.class.getName());
	public UserCount()
	{
		logger.log(Level.INFO, "CHECK");

		ResultSet rs = null;
	    Connector daoCase = new Connector();
	    if(daoCase.connect() == true)
	    {
	    	rs = daoCase.query("SELECT * FROM USERS_TABLE");
	    	daoCase.disConnect();
	    	try {
				while(rs.next())
				{
					usercount++;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	}

	public int getCount()
	{
		logger.log(Level.INFO, Integer.toString(usercount));
		return usercount;
	}
}
