package auth;

import java.util.Date;

public class Message {
	private String user;
	private String text;
	private Date date = new Date();
	
	public Message(){
		
	}
	
	public Message(final Message message)
	{
		this.text = message.text;
		this.date = message.date;
		this.user = message.user;
	}
	
	public String getText()
	{
		return this.text;
	}
	public void setText(String text)
	{
		this.text = text;
	}
	
    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }
    
    public String getUser()
    {
        return user;
    }

    public void setUser(String user)
    {
        this.user = user;
    }
    
    @Override
    public String toString()
    {
        return "[User = " + user + ", text = " + text + "]";
    }    
}
