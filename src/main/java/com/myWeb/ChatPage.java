package com.myWeb;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.*;

import org.apache.wicket.Application;
import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AbstractAjaxTimerBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.feedback.FeedbackMessagesModel;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnLoadHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.ws.IWebSocketSettings;
import org.apache.wicket.protocol.ws.api.IWebSocketConnection;
import org.apache.wicket.protocol.ws.api.WebSocketBehavior;
import org.apache.wicket.protocol.ws.api.WebSocketPushBroadcaster;
import org.apache.wicket.protocol.ws.api.WebSocketRequestHandler;
import org.apache.wicket.protocol.ws.api.event.WebSocketPushPayload;
import org.apache.wicket.protocol.ws.api.message.ClosedMessage;
import org.apache.wicket.protocol.ws.api.message.ConnectedMessage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.time.Duration;
import org.apache.wicket.protocol.ws.api.message.TextMessage;
import org.apache.wicket.protocol.ws.api.registry.IWebSocketConnectionRegistry;
import org.apache.wicket.protocol.ws.api.registry.SimpleWebSocketConnectionRegistry;

import com.googlecode.wicket.kendo.ui.console.FeedbackConsole;

import auth.BasePage;
import auth.SignIn;
import auth.SignInSession;
import database.Connector;
import users.UserList;

public class ChatPage extends BasePage {
	private List<String> messages = Collections
			.synchronizedList(new LinkedList<String>());
	private WebMarkupContainer container;
	Logger logger = Logger.getLogger(Connector.class.getName());
	WicketApplication app = (WicketApplication) this.getApplication();
	List<UserList> users = null;
	ListView<UserList> userlist = null;

	public ChatPage(){
		this(null);
	}
	public ChatPage(IModel model){
		super(model);
		final Form<Void> ajaxForm = new Form<Void>("ajaxForm");
		add(ajaxForm.setOutputMarkupId(true));

		final TextField<String> ajaxMessage = new TextField<String>(
				"ajaxMessage", Model.of(""));
		ajaxForm.add(ajaxMessage);
		ajaxForm.add(new AjaxButton("sendAjax") {
			
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				String message = ajaxMessage.getModelObject();
				final FeedItem feedItem = new FeedItem(message);
				latestMessage.setDefaultModelObject(feedItem);
				ajaxMessage.setModelObject(null);
				target.add(ajaxForm);
				IWebSocketSettings webSocketSettings = IWebSocketSettings.Holder.get(getApplication());
				WebSocketPushBroadcaster broadcaster =
						new WebSocketPushBroadcaster(webSocketSettings.getConnectionRegistry());
				broadcaster.broadcastAll(Application.get(), feedItem);
			}
		});
		Form<Void> form = new Form<Void>("chatForm");
		
		setDefaultModel(new CompoundPropertyModel<ChatPage>(this));
		container = new WebMarkupContainer("container");
		add(container.setOutputMarkupId(true));
		container.add(new ListView<String>("messages",
				new PropertyModel<List<String>>(this, "messages")) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<String> item) {
				item.setDefaultModel(CompoundPropertyModel.of(item
						.getDefaultModel()));
				item.add(new Label("message", item.getModelObject()));
			}
		});

/*		AjaxButton sendButton = new AjaxButton( "button" ){
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				logger.log(Level.INFO,"Button pressed");
			}
		};*/
/*		sendButton.add(new AttributeAppender("onclick", new Model("Wicket.WebSocket.send(document.getElementById('field').value)"), ";"));
		form.add(sendButton);*/
		form.add(new Label("onlinecount", Integer.toString(app.getUsersCount())));
		
		form.add(new AjaxLink<Void>("signout"){
            @Override
            public void onClick(AjaxRequestTarget target)
            {
        		UserList user = new UserList();
        		user.setName((String) getSession().getAttribute("name"));
        		sendToAllConnectedClients( "client "+ user.getName() + " disconnected :)" +"\n");
                getSession().invalidate();
                app.deleteUser(user);
                continueToOriginalDestination();
                setResponsePage(new SignIn());
            }
        });

		add( new WebSocketBehavior() {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onMessage(WebSocketRequestHandler handler,
					TextMessage message) {
				super.onMessage(handler, message);
				//sendToAllConnectedClients( getSession().getAttribute("name") + ": " + message.getText()+"\n");
			}

			@Override
			protected void onConnect(ConnectedMessage message) {
				super.onConnect(message);				
				//sendToAllConnectedClients( "new client connected: "+getSession().getAttribute("name")+"\n");
			}
			
			@Override
			protected void onClose(ClosedMessage message) {
				super.onClose(message);
				//sendToAllConnectedClients( "client disconnected: "+getSession().getAttribute("name")+"\n");
			}			
		});

		users = app.getUsers();
		if(users != null)
		{
			userlist = new ListView<UserList>("userki", users){
				private static final long serialVersionUID = 1L;
	
				@Override
				public void populateItem(ListItem item){
					UserList ui = (UserList) item.getModelObject();
					item.add(new Label("fullname", ui.getName()));
				}
			};
		    //encapsulate the ListView in a WebMarkupContainer in order for it to update
		    WebMarkupContainer listContainer = new WebMarkupContainer("theContainer");
		    //generate a markup-id so the contents can be updated through an AJAX call
		    listContainer.setOutputMarkupId(true);
		    listContainer.add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(5)));
		    // add the list view to the container
		    listContainer.add(userlist);
		    // finally add the container to the page
		    form.add(listContainer);			
		}
		add(form);
	}

    @Override
    public void renderHead(IHeaderResponse response){
    	super.renderHead(response);	    
    	String javaScript = "Wicket.Event.subscribe('/websocket/message', function(jqEvent, message) {var messagesArea = document.getElementById('area');messagesArea.value =messagesArea.value+message;});";
    	response.render( OnLoadHeaderItem.forScript( javaScript));
    }

	private void sendToAllConnectedClients( String message){
		logger.log(Level.INFO,"sendToAllConnectedClients");
		Collection<IWebSocketConnection> wsConnections = getConnectedClients();
		for( IWebSocketConnection wsConnection : wsConnections){
			if (wsConnection != null && wsConnection.isOpen()) {
				try {
						wsConnection.sendMessage( message);
				} catch (IOException e) {}
			}
		}
	}
	private Collection<IWebSocketConnection> getConnectedClients(){
		IWebSocketConnectionRegistry registry = new SimpleWebSocketConnectionRegistry();
		return registry.getConnections( getApplication());	
	}
	@Override
	public void onEvent(IEvent<?> event) {
		if (event.getPayload() instanceof WebSocketPushPayload) {
			WebSocketPushPayload wsEvent = (WebSocketPushPayload) event
					.getPayload();
			handleMessage(wsEvent.getHandler(), (FeedItem) wsEvent.getMessage());
		}
	}
	public void handleMessage(WebSocketRequestHandler handler, FeedItem message) {
		messages.add(message.toString());
		while (messages.size() > 5) {
			messages.remove(0);
		}
		container.modelChanged();
		handler.add(container);
	}
}
