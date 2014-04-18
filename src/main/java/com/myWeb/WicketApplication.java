package com.myWeb;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.Application;
import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.Session;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authorization.IAuthorizationStrategy;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;

import auth.SignInSession;
import auth.AuthenticatedWebPage;
import auth.SignIn;

import org.apache.wicket.request.component.IRequestableComponent;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.settings.IRequestLoggerSettings;

import database.Connector;
import de.agilecoders.wicket.core.Bootstrap;
import de.agilecoders.wicket.core.settings.BootstrapSettings;
import users.UserList;
/**
 * Application object for your web application. If you want to run this application without deploying, run the Start class.
 * 
 * @see com.myWeb.Start#main(String[])
 */
public class WicketApplication extends WebApplication
{    	
	public List<UserList> users = new LinkedList<UserList>();
	Logger logger = Logger.getLogger(Connector.class.getName());
	/**
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	public WicketApplication()
	{
	}
	@Override
	public Class<? extends WebPage> getHomePage()
	{
		return SignIn.class;
	}

    @Override
    public Session newSession(Request request, Response response)
    {
        return new SignInSession(request);
    }

	@Override
	protected  void init()
	{
		super.init();
		IRequestLoggerSettings reqLogger = Application.get().getRequestLoggerSettings();
		reqLogger.setRequestLoggerEnabled(true);
		PackageResourceReference prrFavicon = new PackageResourceReference(
        SignIn.class, "favicon.ico");
	    mountResource("favicon.ico", prrFavicon);
		configureBootstrap();
		this.getMarkupSettings().setStripWicketTags(true); //IMPORTANT!
		 // Register the authorization strategy
        getSecuritySettings().setAuthorizationStrategy(new IAuthorizationStrategy()
        {
            public boolean isActionAuthorized(Component component, Action action)
            {
                // authorize everything
                return true;
            }

            public <T extends IRequestableComponent> boolean isInstantiationAuthorized(
                Class<T> componentClass)
            {
                // Check if the new Page requires authentication (implements the marker interface)
                if (AuthenticatedWebPage.class.isAssignableFrom(componentClass))
                {
                    // Is user signed in?
                    if (((SignInSession)Session.get()).isSignedIn())
                    {
                        // okay to proceed
                        return true;
                    }

                    // Intercept the request, but remember the target for later.
                    // Invoke Component.continueToOriginalDestination() after successful logon to
                    // continue with the target remembered.

                    throw new RestartResponseAtInterceptPageException(SignIn.class);
                }

                // okay to proceed
                return true;
            }
        });
	}
	public void configureBootstrap(){
		BootstrapSettings settings = new BootstrapSettings();
		Bootstrap.install(this, settings);
	}
	public List<UserList> getUsers(){
		return this.users;
	}
	public void addUser(UserList user){
		logger.log(Level.INFO,user.getName());
		this.users.add(user);
	}
	public void deleteUser(UserList user){

		logger.log(Level.INFO,user.getName());

		for(UserList ul: users)
		{
			if(ul.getName() == user.getName())
			{
				logger.log(Level.INFO, "deleted");
				logger.log(Level.INFO,user.getName());
				this.users.remove(ul);
				break;
			}
		}
		for(UserList ul: users)
		{
			logger.log(Level.INFO, "now");
			logger.log(Level.INFO, ul.getName());
		}		
	}	
}
