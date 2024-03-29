package auth;

import org.apache.wicket.markup.html.WebPage;
import users.UserList;
import com.myWeb.WicketApplication;



public class SignOut extends WebPage {

	private static final long serialVersionUID = 1L;
	public SignOut(){
		WicketApplication app = (WicketApplication) this.getApplication();
		UserList user = new UserList();
		user.setName((String) getSession().getAttribute("name"));
        getSession().invalidateNow();	
        app.deleteUser(user);
    }
}
