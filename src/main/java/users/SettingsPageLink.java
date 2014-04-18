package users;

import org.apache.wicket.markup.html.link.Link;

public class SettingsPageLink extends Link {
	public SettingsPageLink(String id){
		super(id);
	}
	@Override
	public void onClick(){
		SettingsPage targetPage = new SettingsPage();
		this.setResponsePage(targetPage);
	}
}
