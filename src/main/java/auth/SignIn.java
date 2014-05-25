package auth;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

import org.apache.wicket.ajax.AbstractAjaxTimerBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.ajax.form.AjaxFormValidatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.SimpleFormComponentLabel;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.util.time.Duration;
import org.apache.wicket.util.value.ValueMap;


























import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.PatternValidator;
import org.apache.wicket.validation.validator.StringValidator;

import users.UserCount;
import users.UserID;
import users.UserList;

import com.googlecode.wicket.jquery.core.Options;
import com.googlecode.wicket.jquery.ui.widget.tooltip.CustomTooltipBehavior;
import com.googlecode.wicket.jquery.ui.widget.tooltip.TooltipBehavior;
import com.myWeb.ChatPage;
import com.myWeb.WicketApplication;

import database.Connector;


public final class SignIn extends WebPage {

	private static final long serialVersionUID = 1L;
	private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    Logger logger = Logger.getLogger(Connector.class.getName());
    // El-cheapo model for form
    private final ValueMap properties = new ValueMap();

	public SignIn()
	{
		final FeedbackPanel feedback = new FeedbackPanel("feedback");
		feedback.setOutputMarkupId(true);
        add(feedback);

        Form form = new Form("signInForm");
        add(form);

        form.setOutputMarkupId(true);

		UserCount uC = new UserCount();
		WicketApplication app = (WicketApplication) this.getApplication();
		final Label totalCount = new Label("totalcount", Integer.toString(uC.getCount()));
		final Label onlineCount = new Label("onlinecount", Integer.toString(app.getUsersCount()));
		totalCount.setOutputMarkupId(true);
		onlineCount.setOutputMarkupId(true);
		add(totalCount);
		add(onlineCount);
	    //encapsulate the ListView in a WebMarkupContainer in order for it to update
		
		form.add(new TextField<String>(USERNAME, new PropertyModel<String>(properties, USERNAME)));
        form.add(new PasswordTextField(PASSWORD, new PropertyModel<String>(properties, PASSWORD)));

        // add a button that can be used to submit the form via ajax
        form.add(new AjaxButton("ajax-button", form)
        {
			private static final long serialVersionUID = 1L;

			@Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form)
            {
                // repaint the feedback panel so that it is hidden
                //target.add(feedback);
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

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form)
            {
                // repaint the feedback panel so errors are shown
                //target.add(feedback);
            }
        });
		Options options = new Options();
		options.set("position", "{ my: 'center top+3', at: 'center bottom' }");
//		options.set("track",true); //used to track the mouse
		form.add(new TooltipBehavior(options));
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
	public static class Bean implements Serializable
    {
        private String name, password;

        /**
         * Gets email.
         * 
         * @return email
         */
        public String getPassword()
        {
            return password;
        }

        /**
         * Sets email.
         * 
         * @param email
         *            email
         */
        public void setPassword(String password)
        {
            this.password = password;
        }

        /**
         * Gets name.
         * 
         * @return name
         */
        public String getName()
        {
            return name;
        }

        /**
         * Sets name.
         * 
         * @param name
         *            name
         */
        public void setName(String name)
        {
            this.name = name;
        }
    }
}
