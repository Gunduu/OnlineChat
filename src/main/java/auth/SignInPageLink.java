package auth;

import org.apache.wicket.markup.html.link.Link;

public class SignInPageLink extends Link {
	private static final long serialVersionUID = 1L;
	public SignInPageLink(String id){
		super(id);
	}
	@Override
	public void onClick(){
		SignIn targetPage = new SignIn();
		this.setResponsePage(targetPage);
	}
}
