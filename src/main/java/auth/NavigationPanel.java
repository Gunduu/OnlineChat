package auth;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;

import com.myWeb.ChatPage;

import users.SettingsPage;
import users.SettingsPageLink;

public class NavigationPanel extends Panel {

	private static final long serialVersionUID = 1L;

	public NavigationPanel(String id) {
        super(id);

        Link homePageLink = new Link("home") {
            @Override
            public void onClick() {
            	setResponsePage(new ChatPage());
            }
        };
        this.add(homePageLink);

        Link<SettingsPageLink> settingsPageLink = new Link<SettingsPageLink>("settings") {
			private static final long serialVersionUID = 1L;

			@Override
            public void onClick() {
            	this.setResponsePage(new SettingsPage());
            }
        };
        this.add(settingsPageLink);

        Link<SignInPageLink> SignInPageLink = new Link<SignInPageLink>("signOut") {
			private static final long serialVersionUID = 1L;

			@Override
            public void onClick() {
				SignOut signOut = new SignOut();
				signOut.Invalidate();
                this.setResponsePage(new SignIn());
            }
        };
        this.add(SignInPageLink);
    }
}
