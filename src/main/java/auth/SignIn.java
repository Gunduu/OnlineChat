package auth;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.util.value.ValueMap;


















import users.UserID;
import users.UserList;

import com.googlecode.wicket.jquery.core.Options;
import com.googlecode.wicket.jquery.ui.widget.tooltip.CustomTooltipBehavior;
import com.googlecode.wicket.jquery.ui.widget.tooltip.TooltipBehavior;
import com.myWeb.ChatPage;
import com.myWeb.WicketApplication;

import database.Connector;


public final class SignIn extends WebPage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public SignIn()
	{
		add(new FeedbackPanel("feedback"));
		
		add(new SignInForm("signInForm"));
	}


	public final class SignInForm extends Form<Void>
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private static final String USERNAME = "username";
        private static final String PASSWORD = "password";
        Logger logger = Logger.getLogger(Connector.class.getName());
        // El-cheapo model for form
        private final ValueMap properties = new ValueMap();
        
        public SignInForm(final String id)
        {	
        	super(id);
            add(new TextField<String>(USERNAME, new PropertyModel<String>(properties, USERNAME)));
            add(new PasswordTextField(PASSWORD, new PropertyModel<String>(properties, PASSWORD)));
    		Options options = new Options();
    		options.set("position", "{ my: 'center top+3', at: 'center bottom' }");
//    		options.set("track",true); //used to track the mouse

    		this.add(new TooltipBehavior(options));
        }
        @Override
        public final void onSubmit()
        {
            // Get session info
            SignInSession session = getMySession();
       
            // Sign the user in
            if (session.signIn(getUsername(), getPassword()))
            {
            	UserList user = new UserList();
            	Connector daoCase = new Connector();
            	if(daoCase.connect() == true)
            	{
            		ResultSet rs = null;
            		rs = daoCase.query("SELECT * FROM USER_INFO");
            		daoCase.disConnect();
            		UserID ui = new UserID();
            		try {
						while(rs.next())
						{
							if(ui.getUser() == rs.getInt("USER_ID"))
							{
								String nickname= rs.getString("NICKNAME");
					        	WicketApplication app = (WicketApplication) this.getApplication();
					        	session.setAttribute("name", nickname);
					        	user.setName(nickname);
					        	app.addUser(user);
					        	continueToOriginalDestination();
					        	setResponsePage(new ChatPage());			
							}
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            	}
            }
            else
            {
            	session.invalidate();
                // Get the error message from the properties file associated with the Component
                String errmsg = getString("loginError", null, "Unable to sign you in");
                
                // Register the error message with the feedback panel
                error(errmsg);
            }
        }

  
        private String getPassword()
        {
            return properties.getString(PASSWORD);
        }


        private String getUsername()
        {
            return properties.getString(USERNAME);
        }


        private SignInSession getMySession()
        {
            return (SignInSession)getSession();
        }
        
	}	
}
