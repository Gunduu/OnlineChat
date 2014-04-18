package register;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormValidatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.time.Duration;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.PatternValidator;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.SimpleFormComponentLabel;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.validation.validator.StringValidator;

import users.UserCount;

import com.googlecode.wicket.jquery.core.Options;

import database.Connector;

import java.util.logging.*;

public final class Register extends WebPage {
	private final String PASSWORD_PATTERN
    = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20})";
	private Person person = new Person();
	public Register(final PageParameters parameters){
		
		final FeedbackPanel feedback = new FeedbackPanel("feedback");
        feedback.setOutputMarkupId(true);
        add(feedback);

        Form<Person> form = new Form<Person>("form", new CompoundPropertyModel<Person>(person));
        add(form);

		form.setOutputMarkupId(true);

		RequiredTextField name = new RequiredTextField<String>("name");
		name.add(new StringValidator(4, null));
		name.setLabel(new ResourceModel("label.name"));
		name.setRequired(true);
		form.add(name);
		form.add(new SimpleFormComponentLabel("name-label", name));

		RequiredTextField email = new RequiredTextField<String>("email");
		email.setRequired(true);
		email.add(EmailAddressValidator.getInstance());
		email.setLabel(new ResourceModel("label.email"));
		form.add(email);
		form.add(new SimpleFormComponentLabel("email-label", email));

		PasswordTextField password = new PasswordTextField("password");
		password.setResetPassword(false);
		password.setLabel(new ResourceModel("label.password"));
		password.add(new PatternValidator(PASSWORD_PATTERN));
		form.add(password);
		form.add(new SimpleFormComponentLabel("password-label", password));	

		Options options = new Options();
		options.set("position", "{ my: 'center top+3', at: 'center bottom' }");

		AjaxFormValidatingBehavior.addToAllFormComponents(form, "keydown", Duration.ONE_SECOND);
		form.add(new AjaxButton("ajax-button", form)
		{
			private static final long serialVersionUID = 1L;
			@Override
		    protected void onSubmit(AjaxRequestTarget target, Form<?> form)
		    {
				Logger logger = Logger.getLogger(Connector.class.getName());
				
				if(person != null)
				{
					logger.log(Level.INFO, person.getUserName());
					logger.log(Level.INFO, person.getPassword());
					logger.log(Level.INFO, person.getEmail());
					UserCount count = new UserCount();
					int usersCount = count.getCount();
					Connector daoCase = new Connector();
					if(daoCase.connect() == true)
					{
						usersCount++;
						daoCase.update("INSERT INTO USERS_TABLE(USER_ID,NAME,EMAIL,PASSWORD) VALUES(?, ?, ?, ?)", person, usersCount);
						daoCase.updateNick("INSERT INTO USER_INFO(USER_ID,NICKNAME) VALUES(?, ?)", person.getUserName(), usersCount);
						daoCase.disConnect();
					}
				}				
		        // repaint the feedback panel so that it is hidden
				target.add(feedback);            
		    }
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form)
			{
			    // repaint the feedback panel so errors are shown
			        target.add(feedback);
		    }
		});
	}
}
