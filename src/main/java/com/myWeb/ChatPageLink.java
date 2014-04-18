package com.myWeb;

import org.apache.wicket.markup.html.link.Link;

public class ChatPageLink extends Link {
	private static final long serialVersionUID = 1L;
	public ChatPageLink(String id){
		super(id);
	}
	@Override
	public void onClick(){
		ChatPage targetPage = new ChatPage();
		this.setResponsePage(targetPage);
	}
}
