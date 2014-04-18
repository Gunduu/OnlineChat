package auth;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;

import users.UserID;

import java.util.logging.*;

import database.Connector;

import java.sql.ResultSet;
import java.sql.SQLException;
public final class SignInSession extends AuthenticatedWebSession  {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String user;

    public SignInSession(Request request)
    {
        super(request);
    }

    @SuppressWarnings("resource")
	@Override
    public final boolean authenticate(final String username, final String password)
    {
    	ResultSet rs = null;
        Connector daoCase = new Connector();
        Logger logger = Logger.getLogger(Connector.class.getName());
        if(user == null)
        {
	        if(daoCase.connect() == true)
	        {
	        	logger.log(Level.INFO,username);
	        	rs = daoCase.query("SELECT * FROM USERS_TABLE");
	        	daoCase.disConnect();
	        	try {
					while(rs.next())
					{
						logger.log(Level.INFO,rs.getString("NAME"));
						logger.log(Level.INFO,rs.getString("PASSWORD"));
						if((username.equalsIgnoreCase(rs.getString("NAME"))) && (password.equalsIgnoreCase(rs.getString("PASSWORD"))))
						{
							UserID ui = new UserID();
							ui.setUser(rs.getInt("USER_ID"));
							user = username;
							break;
						}
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
        }
    	return user != null;
    }

    public String getUser()
    {
        return user;
    }

    public void setUser(final String user)
    {
        this.user = user;
    }

    @Override
    public Roles getRoles()
    {
        return null;
    }

}
