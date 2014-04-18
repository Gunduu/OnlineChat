package com.myWeb;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.util.value.ValueMap;

import auth.BasePage;
import auth.Message;
import database.Connector;
import users.UserList;
public class ChatPage extends BasePage {
	private static final List<Message> messageList = new ArrayList<Message>();
	
	public ChatPage(){
		add(new ChatPageForm("chatForm"));
		
/*        add(new PropertyListView<Message>("messages", messageList)
        {
			private static final long serialVersionUID = 1L;

			@Override
            public void populateItem(final ListItem<Message> listItem)
            {
            	listItem.add(new Label("user"));
                listItem.add(new Label("date"));
                listItem.add(new MultiLineLabel("text"));
            }
        }).setVersioned(false);*/
	}

	private static final long serialVersionUID = 1L;
	WicketApplication app = (WicketApplication) this.getApplication();

	public final class ChatPageForm extends Form<ValueMap>
	{
		private static final long serialVersionUID = 1L;
		List<UserList> users = null;
		final Logger logger = Logger.getLogger(Connector.class.getName());
		public ChatPageForm(final String id){
			super(id);

            // Add simple automated spam prevention measure.
            add(new TextField<String>("message").setType(String.class));
            
			users = app.getUsers();
			if(users != null)
			{
				add(new ListView<UserList>("userki", users){
					private static final long serialVersionUID = 1L;
	
					@Override
					public void populateItem(ListItem item){
						UserList ui = (UserList) item.getModelObject();
						item.add(new Label("fullname", ui.getName()));
					}
				});
			}
		}
	}
}
