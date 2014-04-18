package auth;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;

public class BasePage extends WebPage {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BasePage() {
        this(null);
    }

    public BasePage(IModel model) {
        super(model);
        this.add(new NavigationPanel("mainNavigation"));
    }
}
